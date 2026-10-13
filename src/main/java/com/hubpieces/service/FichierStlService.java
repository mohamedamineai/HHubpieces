package com.hubpieces.service;

import com.hubpieces.dto.FichierStlCreateRequest;
import com.hubpieces.exception.BadRequestException;
import com.hubpieces.exception.ForbiddenException;
import com.hubpieces.exception.ResourceNotFoundException;
import com.hubpieces.model.Demande;
import com.hubpieces.model.EtatDemande;
import com.hubpieces.model.EtatValidation;
import com.hubpieces.model.FichierStl;
import com.hubpieces.model.TypeNotification;
import com.hubpieces.model.Utilisateur;
import com.hubpieces.repository.DemandeRepository;
import com.hubpieces.repository.FichierStlRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class FichierStlService {

    private static final long MAX_FILE_SIZE = 50L * 1024 * 1024;
    private static final String UPLOAD_URL_PREFIX = "/uploads/stl/";

    private final FichierStlRepository fichierStlRepository;
    private final DemandeRepository demandeRepository;
    private final NotificationService notificationService;
    private final Path uploadDir;

    public FichierStlService(FichierStlRepository fichierStlRepository,
                              DemandeRepository demandeRepository,
                              NotificationService notificationService) {
        this.fichierStlRepository = fichierStlRepository;
        this.demandeRepository = demandeRepository;
        this.notificationService = notificationService;
        this.uploadDir = Path.of(System.getProperty("user.dir"), "uploads", "stl")
                .toAbsolutePath().normalize();
    }

    public List<FichierStl> findAll() {
        return fichierStlRepository.findAll();
    }

    public List<FichierStl> findValides() {
        return fichierStlRepository.findByEtatValidationOrderByDateUploadDesc(EtatValidation.VALIDE);
    }

    public FichierStl findById(Long id) {
        return fichierStlRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fichier 3D introuvable : " + id));
    }

    public List<FichierStl> findByUploadeur(Utilisateur u) {
        return fichierStlRepository.findByUploadeurOrderByDateUploadDesc(u);
    }

    public List<FichierStl> findEnAttente() {
        return fichierStlRepository.findByEtatValidationOrderByDateUploadDesc(EtatValidation.EN_ATTENTE);
    }

    @Transactional
    public FichierStl create(FichierStlCreateRequest req, Utilisateur uploadeur) {
        String nom = req.getNom() == null ? "" : req.getNom().trim();
        if (nom.isBlank()) {
            throw new BadRequestException("Le nom est obligatoire");
        }
        if (nom.length() > 200) {
            throw new BadRequestException("Le nom ne doit pas dépasser 200 caractères");
        }
        String chemin = req.getCheminFichier() == null ? "" : req.getCheminFichier().trim();
        if (!estCheminDeFichierInterne(chemin)) {
            throw new BadRequestException("Le fichier doit être une ressource Hubpieces (images/... ou /uploads/stl/...)");
        }
        if (!fichierExiste(chemin)) {
            throw new BadRequestException("Le fichier indiqué n'existe pas sur le serveur. Pour téléverser un fichier local, utilisez l'endpoint multipart.");
        }

        FichierStl f = new FichierStl(nom, chemin, uploadeur);
        f.setDescription(req.getDescription());
        f.setTailleFichier(req.getTailleFichier());
        f.setCategorie(req.getCategorie());
        f.setTags(req.getTags());
        f.setMarque(req.getMarque());
        f.setModeleAppareil(req.getModeleAppareil());
        f.setMateriauRecommande(req.getMateriauRecommande());
        f.setDimensions(req.getDimensions());
        f.setEtatValidation(EtatValidation.EN_ATTENTE);
        return fichierStlRepository.save(f);
    }

    @Transactional
    public FichierStl uploadAndCreate(MultipartFile file, String nom, String description,
                                      String categorie, String tags, String marque,
                                      String modeleAppareil, String materiauRecommande,
                                      String dimensions, Utilisateur uploadeur) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Sélectionnez un fichier 3D à transmettre");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("Le fichier ne doit pas dépasser 50 Mo");
        }

        String original = file.getOriginalFilename();
        String basename = original == null ? "modele.stl" : original.replace('\\', '/');
        basename = basename.substring(basename.lastIndexOf('/') + 1);
        String ext = basename.contains(".")
                ? basename.substring(basename.lastIndexOf('.') + 1).toLowerCase()
                : "";
        if (!ext.matches("stl|obj|3mf|step|stp")) {
            throw new BadRequestException("Format non supporté. Formats acceptés : STL, OBJ, 3MF, STEP ou STP");
        }

        String nomFinal = nom == null || nom.isBlank() ? basename : nom.trim();
        if (nomFinal.length() > 200) {
            throw new BadRequestException("Le nom ne doit pas dépasser 200 caractères");
        }

        Files.createDirectories(uploadDir);
        String storedFilename = UUID.randomUUID() + "." + ext;
        Path target = uploadDir.resolve(storedFilename).normalize();
        if (!target.startsWith(uploadDir)) {
            throw new BadRequestException("Nom de fichier invalide");
        }
        try (InputStream input = file.getInputStream()) {
            Files.copy(input, target, StandardCopyOption.REPLACE_EXISTING);
        }

        FichierStl f = new FichierStl(nomFinal, UPLOAD_URL_PREFIX + storedFilename, uploadeur);
        f.setTailleFichier(file.getSize());
        f.setDescription(description);
        f.setCategorie(categorie);
        f.setTags(tags);
        f.setMarque(marque);
        f.setModeleAppareil(modeleAppareil);
        f.setMateriauRecommande(materiauRecommande);
        f.setDimensions(dimensions);
        f.setEtatValidation(EtatValidation.EN_ATTENTE);
        FichierStl saved = fichierStlRepository.save(f);

        notificationService.envoyer(uploadeur,
                "Fichier 3D transmis",
                "Votre fichier « " + f.getNom() + " » attend la validation de l'administrateur.",
                TypeNotification.INFO);
        return saved;
    }

    @Transactional
    public FichierStl valider(Long id, Utilisateur admin) {
        FichierStl f = findById(id);
        if (f.getEtatValidation() != EtatValidation.EN_ATTENTE) {
            throw new BadRequestException("Seul un fichier en attente peut être validé");
        }
        f.setEtatValidation(EtatValidation.VALIDE);
        f.setValideur(admin);
        FichierStl saved = fichierStlRepository.save(f);

        // Le demandeur ne voit la demande dans la file des makers qu'après
        // validation administrative du modèle CAO.
        for (Demande demande : demandeRepository.findByFichierStl_Id(f.getId())) {
            if (demande.getEtat() == EtatDemande.MODELISATION_REQUISE) {
                demande.setEtat(EtatDemande.RECHERCHE_MAKER);
                demandeRepository.save(demande);
                notificationService.envoyer(demande.getDemandeur(),
                        "Modélisation validée",
                        "Le modèle 3D pour « " + demande.getTitre() + " » a été validé. Les makers peuvent maintenant proposer une offre.",
                        TypeNotification.SUCCESS);
            }
        }

        notificationService.envoyer(f.getUploadeur(),
                "Fichier validé",
                "Votre fichier 3D « " + f.getNom() + " » a été validé et peut être utilisé dans le catalogue.",
                TypeNotification.SUCCESS);
        return saved;
    }

    @Transactional
    public FichierStl rejeter(Long id, String motif, Utilisateur admin) {
        FichierStl f = findById(id);
        if (f.getEtatValidation() != EtatValidation.EN_ATTENTE) {
            throw new BadRequestException("Seul un fichier en attente peut être rejeté");
        }
        String motifFinal = motif == null || motif.isBlank() ? "Non conforme" : motif.trim();
        f.setEtatValidation(EtatValidation.REJETE);
        f.setValideur(admin);
        FichierStl saved = fichierStlRepository.save(f);

        // Une demande refusée retourne dans la file CAO. On détache le fichier
        // rejeté afin qu'il ne puisse jamais être proposé à un maker par erreur.
        for (Demande demande : demandeRepository.findByFichierStl_Id(f.getId())) {
            if (demande.getEtat() == EtatDemande.MODELISATION_REQUISE) {
                demande.setFichierStl(null);
                demandeRepository.save(demande);
                notificationService.envoyer(demande.getDemandeur(),
                        "Modélisation à corriger",
                        "Le premier fichier 3D transmis pour « " + demande.getTitre() + " » n'a pas été accepté. Une nouvelle modélisation est demandée.",
                        TypeNotification.WARNING);
            }
        }

        notificationService.envoyer(f.getUploadeur(),
                "Fichier rejeté",
                "Votre fichier 3D « " + f.getNom() + " » a été rejeté. Motif : " + motifFinal,
                TypeNotification.ERROR);
        return saved;
    }

    @Transactional
    public FichierStl update(Long id, FichierStlCreateRequest req, Utilisateur uploadeur) {
        FichierStl f = findById(id);
        if (!f.getUploadeur().getId().equals(uploadeur.getId())) {
            throw new ForbiddenException("Vous ne pouvez modifier que vos propres fichiers");
        }
        if (req.getNom() != null) {
            String nom = req.getNom().trim();
            if (nom.isBlank() || nom.length() > 200) {
                throw new BadRequestException("Le nom doit contenir entre 1 et 200 caractères");
            }
            f.setNom(nom);
        }
        if (req.getDescription() != null) f.setDescription(req.getDescription());
        if (req.getCategorie() != null) f.setCategorie(req.getCategorie());
        if (req.getTags() != null) f.setTags(req.getTags());
        if (req.getMarque() != null) f.setMarque(req.getMarque());
        if (req.getModeleAppareil() != null) f.setModeleAppareil(req.getModeleAppareil());
        if (req.getMateriauRecommande() != null) f.setMateriauRecommande(req.getMateriauRecommande());
        if (req.getDimensions() != null) f.setDimensions(req.getDimensions());
        if (f.getEtatValidation() != EtatValidation.EN_ATTENTE) {
            f.setEtatValidation(EtatValidation.EN_ATTENTE);
            f.setValideur(null);
        }
        return fichierStlRepository.save(f);
    }

    @Transactional
    public void delete(Long id, Utilisateur uploadeur) {
        FichierStl f = findById(id);
        if (!f.getUploadeur().getId().equals(uploadeur.getId())) {
            throw new ForbiddenException("Vous ne pouvez supprimer que vos propres fichiers");
        }
        if (f.getEtatValidation() == EtatValidation.VALIDE) {
            throw new BadRequestException("Un fichier validé du catalogue ne peut pas être supprimé");
        }
        if (demandeRepository.existsByFichierStl_Id(id)) {
            throw new BadRequestException("Ce fichier est utilisé par une demande et ne peut pas être supprimé");
        }
        fichierStlRepository.delete(f);
    }

    @Transactional
    public void incrementerTelechargements(Long id) {
        FichierStl f = findById(id);
        if (f.getEtatValidation() != EtatValidation.VALIDE) {
            throw new ResourceNotFoundException("Fichier 3D introuvable dans le catalogue public");
        }
        int telechargements = f.getNombreTelechargements() == null ? 0 : f.getNombreTelechargements();
        f.setNombreTelechargements(telechargements + 1);
        fichierStlRepository.save(f);
    }

    private boolean estCheminDeFichierInterne(String chemin) {
        return chemin.matches("images/[A-Za-z0-9._-]+\\.(glb|gltf|stl|obj|3mf|step|stp)")
                || chemin.matches("/uploads/stl/[A-Za-z0-9._-]+\\.(stl|obj|3mf|step|stp)");
    }

    private boolean fichierExiste(String chemin) {
        if (chemin.startsWith(UPLOAD_URL_PREFIX)) {
            Path fichier = uploadDir.resolve(chemin.substring(UPLOAD_URL_PREFIX.length())).normalize();
            return fichier.startsWith(uploadDir) && Files.isRegularFile(fichier);
        }
        return new ClassPathResource("static/" + chemin).exists();
    }
}

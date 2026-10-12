package com.hubpieces.service;

import com.hubpieces.dto.FichierStlCreateRequest;
import com.hubpieces.exception.BadRequestException;
import com.hubpieces.exception.ForbiddenException;
import com.hubpieces.exception.ResourceNotFoundException;
import com.hubpieces.model.EtatValidation;
import com.hubpieces.model.FichierStl;
import com.hubpieces.model.Utilisateur;
import com.hubpieces.repository.FichierStlRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FichierStlService {

    private final FichierStlRepository fichierStlRepository;
    private final NotificationService notificationService;
    private final String uploadDir = System.getProperty("user.dir") + "/uploads/stl/";

    public FichierStlService(FichierStlRepository fichierStlRepository,
                              NotificationService notificationService) {
        this.fichierStlRepository = fichierStlRepository;
        this.notificationService = notificationService;
        new java.io.File(uploadDir).mkdirs();
    }

    public List<FichierStl> findAll() {
        return fichierStlRepository.findAll();
    }

    public List<FichierStl> findValides() {
        return fichierStlRepository.findByEtatValidationOrderByDateUploadDesc(EtatValidation.VALIDE);
    }

    public FichierStl findById(Long id) {
        return fichierStlRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fichier STL introuvable : " + id));
    }

    public List<FichierStl> findByUploadeur(Utilisateur u) {
        return fichierStlRepository.findByUploadeurOrderByDateUploadDesc(u);
    }

    public List<FichierStl> findEnAttente() {
        return fichierStlRepository.findByEtatValidationOrderByDateUploadDesc(EtatValidation.EN_ATTENTE);
    }

    @Transactional
    public FichierStl create(FichierStlCreateRequest req, Utilisateur uploadeur) {
        if (req.getNom() == null || req.getNom().isBlank()) {
            throw new BadRequestException("Le nom est obligatoire");
        }
        if (req.getCheminFichier() == null || req.getCheminFichier().isBlank()) {
            throw new BadRequestException("Le chemin du fichier est obligatoire");
        }
        FichierStl f = new FichierStl(req.getNom(), req.getCheminFichier(), uploadeur);
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
            throw new BadRequestException("Fichier manquant");
        }
        String original = file.getOriginalFilename();
        String ext = original != null && original.contains(".")
                ? original.substring(original.lastIndexOf('.') + 1).toLowerCase()
                : "stl";
        if (!ext.matches("stl|obj|3mf|step|stp")) {
            throw new BadRequestException("Format non supporte. Formats acceptes : stl, obj, 3mf, step");
        }
        String filename = UUID.randomUUID() + "." + ext;
        Path target = Paths.get(uploadDir + filename);
        Files.createDirectories(target.getParent());
        Files.write(target, file.getBytes());

        FichierStl f = new FichierStl(nom != null ? nom : original, "/uploads/stl/" + filename, uploadeur);
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
                "Fichier uploade",
                "Votre fichier STL \"" + f.getNom() + "\" est en attente de validation",
                TypeNotification.INFO);
        return saved;
    }

    @Transactional
    public FichierStl valider(Long id, Utilisateur admin) {
        FichierStl f = findById(id);
        if (f.getEtatValidation() == EtatValidation.VALIDE) {
            throw new BadRequestException("Fichier deja valide");
        }
        f.setEtatValidation(EtatValidation.VALIDE);
        f.setValideur(admin);
        FichierStl saved = fichierStlRepository.save(f);

        notificationService.envoyer(f.getUploadeur(),
                "Fichier valide",
                "Votre fichier STL \"" + f.getNom() + "\" a ete valide",
                TypeNotification.SUCCESS);
        return saved;
    }

    @Transactional
    public FichierStl rejeter(Long id, String motif, Utilisateur admin) {
        FichierStl f = findById(id);
        if (f.getEtatValidation() == EtatValidation.REJETE) {
            throw new BadRequestException("Fichier deja rejete");
        }
        f.setEtatValidation(EtatValidation.REJETE);
        f.setValideur(admin);
        FichierStl saved = fichierStlRepository.save(f);

        notificationService.envoyer(f.getUploadeur(),
                "Fichier rejete",
                "Votre fichier STL \"" + f.getNom() + "\" a ete rejete. Motif : " + motif,
                TypeNotification.ERROR);
        return saved;
    }

    @Transactional
    public FichierStl update(Long id, FichierStlCreateRequest req, Utilisateur uploadeur) {
        FichierStl f = findById(id);
        if (!f.getUploadeur().getId().equals(uploadeur.getId())) {
            throw new ForbiddenException("Vous ne pouvez modifier que vos fichiers");
        }
        if (req.getNom() != null) f.setNom(req.getNom());
        if (req.getDescription() != null) f.setDescription(req.getDescription());
        if (req.getCategorie() != null) f.setCategorie(req.getCategorie());
        if (req.getTags() != null) f.setTags(req.getTags());
        if (req.getMarque() != null) f.setMarque(req.getMarque());
        if (req.getModeleAppareil() != null) f.setModeleAppareil(req.getModeleAppareil());
        if (req.getMateriauRecommande() != null) f.setMateriauRecommande(req.getMateriauRecommande());
        if (req.getDimensions() != null) f.setDimensions(req.getDimensions());
        if (f.getEtatValidation() == EtatValidation.VALIDE) {
            f.setEtatValidation(EtatValidation.EN_ATTENTE);
        }
        return fichierStlRepository.save(f);
    }

    @Transactional
    public void delete(Long id, Utilisateur uploadeur) {
        FichierStl f = findById(id);
        if (!f.getUploadeur().getId().equals(uploadeur.getId())) {
            throw new ForbiddenException("Vous ne pouvez supprimer que vos fichiers");
        }
        fichierStlRepository.delete(f);
    }

    @Transactional
    public void incrementerTelechargements(Long id) {
        FichierStl f = findById(id);
        f.setNombreTelechargements(f.getNombreTelechargements() + 1);
        fichierStlRepository.save(f);
    }
}

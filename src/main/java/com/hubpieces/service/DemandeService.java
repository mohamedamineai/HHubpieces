package com.hubpieces.service;

import com.hubpieces.dto.DemandeCreateRequest;
import com.hubpieces.exception.BadRequestException;
import com.hubpieces.exception.ForbiddenException;
import com.hubpieces.exception.ResourceNotFoundException;
import com.hubpieces.model.*;
import com.hubpieces.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DemandeService {

    private final DemandeRepository demandeRepository;
    private final FichierStlRepository fichierStlRepository;
    private final OffreMakerRepository offreMakerRepository;
    private final TransactionRepository transactionRepository;
    private final MessageRepository messageRepository;
    private final PhotoDemandeRepository photoDemandeRepository;
    private final NotificationService notificationService;
    private final SessionService sessionService;

    public DemandeService(DemandeRepository demandeRepository,
                          FichierStlRepository fichierStlRepository,
                          OffreMakerRepository offreMakerRepository,
                          TransactionRepository transactionRepository,
                          MessageRepository messageRepository,
                          PhotoDemandeRepository photoDemandeRepository,
                          NotificationService notificationService,
                          SessionService sessionService) {
        this.demandeRepository = demandeRepository;
        this.fichierStlRepository = fichierStlRepository;
        this.offreMakerRepository = offreMakerRepository;
        this.transactionRepository = transactionRepository;
        this.messageRepository = messageRepository;
        this.photoDemandeRepository = photoDemandeRepository;
        this.notificationService = notificationService;
        this.sessionService = sessionService;
    }

    public List<Demande> findAll() {
        return demandeRepository.findAll();
    }

    public Demande findById(Long id) {
        return demandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande introuvable : " + id));
    }

    public List<Demande> findByDemandeur(Utilisateur demandeur) {
        return demandeRepository.findByDemandeurOrderByDateCreationDesc(demandeur);
    }

    public List<Demande> findDisponiblesPourMaker(Utilisateur maker) {
        return demandeRepository.findByEtatOrderByDateCreationDesc(EtatDemande.RECHERCHE_MAKER).stream()
                .filter(d -> offreMakerRepository.findByDemandeAndMaker(d, maker).isEmpty())
                .collect(Collectors.toList());
    }

    public List<Demande> findAValiderParExpert() {
        return demandeRepository.findByEtatOrderByDateCreationDesc(EtatDemande.MODELISATION_REQUISE);
    }

    @Transactional
    public Demande create(DemandeCreateRequest req, Utilisateur demandeur) {
        if (req.getTitre() == null || req.getTitre().isBlank()) {
            throw new BadRequestException("Le titre est obligatoire");
        }
        if (req.getDescription() == null || req.getDescription().isBlank()) {
            throw new BadRequestException("La description est obligatoire");
        }

        Demande demande = new Demande(req.getTitre(), req.getDescription(), demandeur);
        demande.setMarque(req.getMarque());
        demande.setModeleAppareil(req.getModeleAppareil());
        demande.setMateriauSouhaite(req.getMateriauSouhaite());
        demande.setBudgetMax(req.getBudgetMax());
        demande.setVille(req.getVille());
        demande.setCodePostal(req.getCodePostal());
        demande.setLatitude(req.getLatitude());
        demande.setLongitude(req.getLongitude());

        if (req.getFichierStlId() != null) {
            FichierStl fichier = fichierStlRepository.findById(req.getFichierStlId())
                    .orElseThrow(() -> new ResourceNotFoundException("Fichier STL introuvable"));
            if (fichier.getEtatValidation() != EtatValidation.VALIDE) {
                throw new BadRequestException("Ce fichier STL n'est pas encore valide");
            }
            demande.setFichierStl(fichier);
            demande.setEtat(EtatDemande.RECHERCHE_MAKER);
        } else {
            demande.setEtat(EtatDemande.MODELISATION_REQUISE);
        }

        return demandeRepository.save(demande);
    }

    @Transactional
    public Demande update(Long id, DemandeCreateRequest req, Utilisateur demandeur) {
        Demande demande = findById(id);
        if (!demande.getDemandeur().getId().equals(demandeur.getId())) {
            throw new ForbiddenException("Vous ne pouvez modifier que vos propres demandes");
        }
        if (demande.getEtat() == EtatDemande.EN_COURS_IMPRESSION || demande.getEtat() == EtatDemande.CLOTUREE) {
            throw new BadRequestException("Cette demande ne peut plus etre modifiee");
        }
        if (req.getTitre() != null) demande.setTitre(req.getTitre());
        if (req.getDescription() != null) demande.setDescription(req.getDescription());
        if (req.getMarque() != null) demande.setMarque(req.getMarque());
        if (req.getModeleAppareil() != null) demande.setModeleAppareil(req.getModeleAppareil());
        if (req.getMateriauSouhaite() != null) demande.setMateriauSouhaite(req.getMateriauSouhaite());
        if (req.getBudgetMax() != null) demande.setBudgetMax(req.getBudgetMax());
        if (req.getVille() != null) demande.setVille(req.getVille());
        if (req.getCodePostal() != null) demande.setCodePostal(req.getCodePostal());
        if (req.getLatitude() != null) demande.setLatitude(req.getLatitude());
        if (req.getLongitude() != null) demande.setLongitude(req.getLongitude());
        if (req.getFichierStlId() != null) {
            FichierStl fichier = fichierStlRepository.findById(req.getFichierStlId())
                    .orElseThrow(() -> new ResourceNotFoundException("Fichier STL introuvable"));
            demande.setFichierStl(fichier);
        }
        return demandeRepository.save(demande);
    }

    @Transactional
    public Demande annuler(Long id, Utilisateur demandeur) {
        Demande demande = findById(id);
        if (!demande.getDemandeur().getId().equals(demandeur.getId())) {
            throw new ForbiddenException("Vous ne pouvez annuler que vos propres demandes");
        }
        if (demande.getEtat() == EtatDemande.EN_COURS_IMPRESSION) {
            throw new BadRequestException("Impossible d'annuler une demande en cours d'impression");
        }
        if (demande.getEtat() == EtatDemande.CLOTUREE) {
            throw new BadRequestException("Impossible d'annuler une demande cloturee");
        }
        demande.setEtat(EtatDemande.ANNULEE);
        return demandeRepository.save(demande);
    }

    @Transactional
    public Demande basculerEnRechercheMaker(Long demandeId, Utilisateur expertCao) {
        Demande demande = findById(demandeId);
        if (demande.getEtat() != EtatDemande.MODELISATION_REQUISE) {
            throw new BadRequestException("La demande n'est pas en attente de modelisation");
        }
        demande.setEtat(EtatDemande.RECHERCHE_MAKER);
        Demande saved = demandeRepository.save(demande);

        notificationService.envoyer(demande.getDemandeur(),
                "Demaine prete",
                "Votre demande \"" + demande.getTitre() + "\" est maintenant recherche de maker",
                TypeNotification.SUCCESS);

        return saved;
    }

    @Transactional
    public Demande marquerImpressionLancee(Long demandeId, Utilisateur maker) {
        Demande demande = findById(demandeId);
        boolean isMakerAccepteur = demande.getOffres().stream()
                .anyMatch(o -> o.getMaker().getId().equals(maker.getId()) && o.getEtat() == EtatOffre.ACCEPTEE);
        if (!isMakerAccepteur) {
            throw new ForbiddenException("Seul le maker ayant l'offre acceptee peut modifier cette demande");
        }
        if (demande.getEtat() != EtatDemande.RECHERCHE_MAKER) {
            throw new BadRequestException("Transition impossible depuis l'etat " + demande.getEtat());
        }
        demande.setEtat(EtatDemande.EN_COURS_IMPRESSION);
        return demandeRepository.save(demande);
    }

    @Transactional
    public Demande marquerEnAttenteValidation(Long demandeId, Utilisateur maker) {
        Demande demande = findById(demandeId);
        boolean isMakerAccepteur = demande.getOffres().stream()
                .anyMatch(o -> o.getMaker().getId().equals(maker.getId()) && o.getEtat() == EtatOffre.ACCEPTEE);
        if (!isMakerAccepteur) {
            throw new ForbiddenException("Seul le maker ayant l'offre acceptee peut modifier cette demande");
        }
        if (demande.getEtat() != EtatDemande.EN_COURS_IMPRESSION) {
            throw new BadRequestException("La demande doit etre en cours d'impression");
        }
        demande.setEtat(EtatDemande.ATTENTE_VALIDATION);
        Demande saved = demandeRepository.save(demande);

        notificationService.envoyer(demande.getDemandeur(),
                "Piece prete a valider",
                "Votre piece pour \"" + demande.getTitre() + "\" est prete. Vous pouvez la valider.",
                TypeNotification.INFO);

        return saved;
    }

    @Transactional
    public Demande accepterOffre(Long demandeId, Long offreId, Utilisateur demandeur) {
        Demande demande = findById(demandeId);
        if (!demande.getDemandeur().getId().equals(demandeur.getId())) {
            throw new ForbiddenException("Vous n'etes pas le demandeur");
        }
        if (demande.getEtat() != EtatDemande.RECHERCHE_MAKER) {
            throw new BadRequestException("Cette demande n'est pas en recherche de maker");
        }

        OffreMaker offre = offreMakerRepository.findById(offreId)
                .orElseThrow(() -> new ResourceNotFoundException("Offre introuvable"));
        if (!offre.getDemande().getId().equals(demandeId)) {
            throw new BadRequestException("Cette offre ne correspond pas a la demande");
        }
        if (offre.getEtat() != EtatOffre.EN_ATTENTE) {
            throw new BadRequestException("Cette offre n'est plus en attente");
        }

        List<OffreMaker> toutes = offreMakerRepository.findByDemandeOrderByPrixAsc(demande);
        for (OffreMaker o : toutes) {
            if (o.getId().equals(offreId)) {
                o.setEtat(EtatOffre.ACCEPTEE);
            } else if (o.getEtat() == EtatOffre.EN_ATTENTE) {
                o.setEtat(EtatOffre.REFUSEE);
            }
            offreMakerRepository.save(o);
        }

        demande.setEtat(EtatDemande.EN_COURS_IMPRESSION);
        Demande saved = demandeRepository.save(demande);

        Transaction transaction = new Transaction(offre, offre.getPrix());
        transaction.setEtatPaiement(EtatPaiement.EN_SEQUESTRE);
        transaction.setDatePaiement(LocalDateTime.now());
        transactionRepository.save(transaction);

        notificationService.envoyer(offre.getMaker(),
                "Offre acceptee",
                "Votre offre pour \"" + demande.getTitre() + "\" a ete acceptee",
                TypeNotification.SUCCESS);

        return saved;
    }

    @Transactional
    public Demande refuserOffre(Long demandeId, Long offreId, Utilisateur demandeur) {
        Demande demande = findById(demandeId);
        if (!demande.getDemandeur().getId().equals(demandeur.getId())) {
            throw new ForbiddenException("Vous n'etes pas le demandeur");
        }
        OffreMaker offre = offreMakerRepository.findById(offreId)
                .orElseThrow(() -> new ResourceNotFoundException("Offre introuvable"));
        if (!offre.getDemande().getId().equals(demandeId)) {
            throw new BadRequestException("Cette offre ne correspond pas a la demande");
        }
        if (offre.getEtat() != EtatOffre.EN_ATTENTE) {
            throw new BadRequestException("Cette offre n'est plus en attente");
        }
        offre.setEtat(EtatOffre.REFUSEE);
        offreMakerRepository.save(offre);

        notificationService.envoyer(offre.getMaker(),
                "Offre refusee",
                "Votre offre pour \"" + demande.getTitre() + "\" a ete refusee",
                TypeNotification.WARNING);

        return demande;
    }

    @Transactional
    public Demande cloturer(Long demandeId, Utilisateur demandeur) {
        Demande demande = findById(demandeId);
        if (!demande.getDemandeur().getId().equals(demandeur.getId())) {
            throw new ForbiddenException("Vous n'etes pas le demandeur");
        }
        if (demande.getEtat() != EtatDemande.ATTENTE_VALIDATION) {
            throw new BadRequestException("La demande doit etre en attente de validation");
        }
        demande.setEtat(EtatDemande.CLOTUREE);

        demande.getOffres().stream()
                .filter(o -> o.getEtat() == EtatOffre.ACCEPTEE)
                .findFirst()
                .ifPresent(offre -> transactionRepository.findByOffreMakerId(offre.getId())
                        .ifPresent(t -> {
                            t.setEtatPaiement(EtatPaiement.LIBERE);
                            t.setDateLiberaion(LocalDateTime.now());
                            transactionRepository.save(t);
                        }));

        return demandeRepository.save(demande);
    }

    public List<OffreMaker> getOffres(Long demandeId) {
        Demande demande = findById(demandeId);
        return offreMakerRepository.findByDemandeOrderByPrixAsc(demande);
    }

    public List<PhotoDemande> getPhotos(Long demandeId) {
        Demande demande = findById(demandeId);
        return photoDemandeRepository.findByDemandeOrderByDateUploadDesc(demande);
    }
}

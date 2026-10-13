package com.hubpieces.service;

import com.hubpieces.dto.LitigeCreateRequest;
import com.hubpieces.dto.LitigeResolutionRequest;
import com.hubpieces.exception.BadRequestException;
import com.hubpieces.exception.ForbiddenException;
import com.hubpieces.exception.ResourceNotFoundException;
import com.hubpieces.model.*;
import com.hubpieces.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Locale;
import java.util.List;

@Service
public class LitigeService {

    private final LitigeRepository litigeRepository;
    private final TransactionRepository transactionRepository;
    private final NotificationService notificationService;

    public LitigeService(LitigeRepository litigeRepository,
                         TransactionRepository transactionRepository,
                         NotificationService notificationService) {
        this.litigeRepository = litigeRepository;
        this.transactionRepository = transactionRepository;
        this.notificationService = notificationService;
    }

    public List<Litige> findAll() {
        return litigeRepository.findByOrderByDateOuvertureDesc();
    }

    public List<Litige> findOuverts() {
        return litigeRepository.findByEtatInOrderByDateOuvertureDesc(
                Arrays.asList(EtatLitige.OUVERT, EtatLitige.EN_COURS));
    }

    public Litige findById(Long id) {
        return litigeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Litige introuvable : " + id));
    }

    @Transactional
    public Litige ouvrir(LitigeCreateRequest req, Utilisateur demandeur) {
        if (req == null || req.getTransactionId() == null) {
            throw new BadRequestException("L'identifiant de la transaction est obligatoire");
        }
        if (req.getRaison() == null || req.getRaison().isBlank()) {
            throw new BadRequestException("La raison est obligatoire");
        }
        Transaction transaction = transactionRepository.findById(req.getTransactionId())
                .orElseThrow(() -> new ResourceNotFoundException("Transaction introuvable"));
        Utilisateur owner = transaction.getOffreMaker().getDemande().getDemandeur();
        if (!owner.getId().equals(demandeur.getId())) {
            throw new ForbiddenException("Vous n'êtes pas le demandeur de cette transaction");
        }
        if (litigeRepository.existsByTransaction_IdAndEtatIn(transaction.getId(),
                Arrays.asList(EtatLitige.OUVERT, EtatLitige.EN_COURS))) {
            throw new BadRequestException("Un litige est déjà ouvert pour cette transaction");
        }

        Litige litige = new Litige(transaction, demandeur, req.getRaison().trim());
        Litige saved = litigeRepository.save(litige);

        transaction.setEtatPaiement(EtatPaiement.LITIGE);
        transactionRepository.save(transaction);

        notificationService.envoyer(transaction.getOffreMaker().getMaker(),
                "Litige ouvert",
                "Un litige a été ouvert concernant votre commande. L'administrateur va intervenir.",
                TypeNotification.WARNING);

        return saved;
    }

    @Transactional
    public Litige resoudre(Long id, LitigeResolutionRequest req, Utilisateur admin) {
        Litige litige = findById(id);
        if (litige.getEtat() == EtatLitige.RESOLU || litige.getEtat() == EtatLitige.FERME) {
            throw new BadRequestException("Ce litige est déjà clôturé");
        }
        if (req == null || req.getDecision() == null || req.getDecision().isBlank()) {
            throw new BadRequestException("La décision de l'administrateur est obligatoire");
        }

        EtatLitige nouvelEtat = req.getEtat() == null ? EtatLitige.RESOLU : req.getEtat();
        if (nouvelEtat == EtatLitige.OUVERT) {
            throw new BadRequestException("Un litige pris en charge ne peut pas revenir à l'état OUVERT");
        }

        litige.setDecision(req.getDecision().trim());
        litige.setAdmin(admin);
        litige.setEtat(nouvelEtat);

        Transaction transaction = litige.getTransaction();
        boolean cloture = nouvelEtat == EtatLitige.RESOLU || nouvelEtat == EtatLitige.FERME;
        if (cloture) {
            litige.setDateResolution(LocalDateTime.now());
            if (req.getDecision().toLowerCase(Locale.ROOT).contains("rembours")) {
                transaction.setEtatPaiement(EtatPaiement.REMBOURSE);
            } else {
                transaction.setEtatPaiement(EtatPaiement.LIBERE);
                transaction.setDateLiberaion(LocalDateTime.now());
            }
        } else {
            // La transaction reste bloquée en séquestre pendant l'instruction.
            transaction.setEtatPaiement(EtatPaiement.LITIGE);
            litige.setDateResolution(null);
        }
        transactionRepository.save(transaction);
        Litige saved = litigeRepository.save(litige);

        String titreNotification = cloture ? "Litige résolu" : "Litige pris en charge";
        String messageNotification = cloture
                ? "Votre litige est clôturé : " + req.getDecision().trim()
                : "Votre litige est en cours d'instruction par l'administrateur.";
        notificationService.envoyer(litige.getDemandeur(), titreNotification, messageNotification,
                cloture ? TypeNotification.SUCCESS : TypeNotification.INFO);
        notificationService.envoyer(transaction.getOffreMaker().getMaker(), titreNotification,
                cloture ? "Décision concernant le litige : " + req.getDecision().trim()
                        : "L'administrateur a pris en charge le litige de votre commande.",
                cloture ? TypeNotification.INFO : TypeNotification.WARNING);
        return saved;
    }
}

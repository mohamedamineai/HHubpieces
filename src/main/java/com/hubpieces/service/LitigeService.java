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
import java.util.List;
import java.util.stream.Collectors;

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
        return litigeRepository.findByEtatOrderByDateOuvertureDesc(EtatLitige.OUVERT);
    }

    public Litige findById(Long id) {
        return litigeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Litige introuvable : " + id));
    }

    @Transactional
    public Litige ouvrir(LitigeCreateRequest req, Utilisateur demandeur) {
        if (req.getRaison() == null || req.getRaison().isBlank()) {
            throw new BadRequestException("La raison est obligatoire");
        }
        Transaction transaction = transactionRepository.findById(req.getTransactionId())
                .orElseThrow(() -> new ResourceNotFoundException("Transaction introuvable"));
        Utilisateur owner = transaction.getOffreMaker().getDemande().getDemandeur();
        if (!owner.getId().equals(demandeur.getId())) {
            throw new ForbiddenException("Vous n'etes pas le demandeur de cette transaction");
        }

        Litige litige = new Litige(transaction, demandeur, req.getRaison());
        Litige saved = litigeRepository.save(litige);

        transaction.setEtatPaiement(EtatPaiement.LITIGE);
        transactionRepository.save(transaction);

        // Notifier l'admin (premier admin trouve)
        // En production, on notifierait tous les admins. Pour le MVP, on notifie le maker
        notificationService.envoyer(transaction.getOffreMaker().getMaker(),
                "Litige ouvert",
                "Un litige a ete ouvert concernant votre commande. L'admin va intervenir.",
                TypeNotification.WARNING);

        return saved;
    }

    @Transactional
    public Litige resoudre(Long id, LitigeResolutionRequest req, Utilisateur admin) {
        Litige litige = findById(id);
        if (litige.getEtat() == EtatLitige.RESOLU || litige.getEtat() == EtatLitige.FERME) {
            throw new BadRequestException("Ce litige est deja cloture");
        }
        if (req.getDecision() == null || req.getDecision().isBlank()) {
            throw new BadRequestException("La decision est obligatoire");
        }

        litige.setDecision(req.getDecision());
        litige.setAdmin(admin);
        litige.setEtat(req.getEtat() != null ? req.getEtat() : EtatLitige.RESOLU);
        if (litige.getEtat() == EtatLitige.RESOLU || litige.getEtat() == EtatLitige.FERME) {
            litige.setDateResolution(LocalDateTime.now());

            // Si resolu en faveur du demandeur -> remboursement
            if (req.getDecision().toLowerCase().contains("rembours")) {
                litige.getTransaction().setEtatPaiement(EtatPaiement.REMBOURSE);
            } else {
                litige.getTransaction().setEtatPaiement(EtatPaiement.LIBERE);
                litige.getTransaction().setDateLiberaion(LocalDateTime.now());
            }
            transactionRepository.save(litige.getTransaction());
        }
        Litige saved = litigeRepository.save(litige);

        notificationService.envoyer(litige.getDemandeur(),
                "Litige resolu",
                "Votre litige a ete resolu : " + req.getDecision(),
                TypeNotification.INFO);
        return saved;
    }
}

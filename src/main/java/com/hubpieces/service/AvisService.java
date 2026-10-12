package com.hubpieces.service;

import com.hubpieces.dto.AvisCreateRequest;
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
public class AvisService {

    private final AvisRepository avisRepository;
    private final TransactionRepository transactionRepository;
    private final UtilisateurRepository utilisateurRepository;

    public AvisService(AvisRepository avisRepository,
                       TransactionRepository transactionRepository,
                       UtilisateurRepository utilisateurRepository) {
        this.avisRepository = avisRepository;
        this.transactionRepository = transactionRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    public List<Avis> findByDestinataire(Utilisateur destinataire) {
        return avisRepository.findByDestinataireAndPublieTrueOrderByDatePublicationDesc(destinataire);
    }

    public List<Avis> findByAuteur(Utilisateur auteur) {
        return avisRepository.findByAuteurOrderByDateCreationDesc(auteur);
    }

    @Transactional
    public Avis create(AvisCreateRequest req, Utilisateur auteur) {
        if (req.getNote() == null || req.getNote() < 1 || req.getNote() > 5) {
            throw new BadRequestException("La note doit etre entre 1 et 5");
        }
        if (req.getCommentaire() == null || req.getCommentaire().isBlank()) {
            throw new BadRequestException("Le commentaire est obligatoire");
        }

        Transaction transaction = transactionRepository.findById(req.getTransactionId())
                .orElseThrow(() -> new ResourceNotFoundException("Transaction introuvable"));

        // Verifier que l'auteur est bien partie prenante de la transaction
        Utilisateur maker = transaction.getOffreMaker().getMaker();
        Utilisateur demandeur = transaction.getOffreMaker().getDemande().getDemandeur();
        if (!auteur.getId().equals(maker.getId()) && !auteur.getId().equals(demandeur.getId())) {
            throw new ForbiddenException("Vous n'etes pas partie prenante de cette transaction");
        }

        Utilisateur destinataire = utilisateurRepository.findById(req.getDestinataireId())
                .orElseThrow(() -> new ResourceNotFoundException("Destinataire introuvable"));

        // Verifier qu'il n'a pas deja depose un avis pour cette transaction
        boolean deja = avisRepository.findByAuteurOrderByDateCreationDesc(auteur).stream()
                .anyMatch(a -> a.getTransaction().getId().equals(transaction.getId()));
        if (deja) {
            throw new BadRequestException("Vous avez deja depose un avis pour cette transaction");
        }

        Avis avis = new Avis(transaction, auteur, destinataire, req.getNote(), req.getCommentaire());
        // Publication directe pour les MVP (en production, on peut modérer)
        avis.setPublie(true);
        avis.setDatePublication(LocalDateTime.now());
        Avis saved = avisRepository.save(avis);

        // Mettre a jour la note moyenne du destinataire
        Double moyenneActuelle = destinataire.getNoteMoyenne();
        Integer nbActuel = destinataire.getNombreAvis();
        double nouvelleMoyenne = ((moyenneActuelle * nbActuel) + req.getNote()) / (nbActuel + 1);
        destinataire.setNoteMoyenne(nouvelleMoyenne);
        destinataire.setNombreAvis(nbActuel + 1);
        utilisateurRepository.save(destinataire);

        return saved;
    }
}

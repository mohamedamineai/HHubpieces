package com.hubpieces.service;

import com.hubpieces.dto.OffreCreateRequest;
import com.hubpieces.exception.BadRequestException;
import com.hubpieces.exception.ForbiddenException;
import com.hubpieces.exception.ResourceNotFoundException;
import com.hubpieces.model.*;
import com.hubpieces.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OffreMakerService {

    private final OffreMakerRepository offreMakerRepository;
    private final DemandeRepository demandeRepository;
    private final NotificationService notificationService;

    public OffreMakerService(OffreMakerRepository offreMakerRepository,
                              DemandeRepository demandeRepository,
                              NotificationService notificationService) {
        this.offreMakerRepository = offreMakerRepository;
        this.demandeRepository = demandeRepository;
        this.notificationService = notificationService;
    }

    public List<OffreMaker> findByMaker(Utilisateur maker) {
        return offreMakerRepository.findByMakerOrderByDateCreationDesc(maker);
    }

    public OffreMaker findById(Long id) {
        return offreMakerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Offre introuvable : " + id));
    }

    @Transactional
    public OffreMaker create(OffreCreateRequest req, Utilisateur maker) {
        if (req.getDemandeId() == null) {
            throw new BadRequestException("L'identifiant de la demande est obligatoire");
        }
        if (req.getPrix() == null || req.getPrix() <= 0) {
            throw new BadRequestException("Le prix doit etre superieur a 0");
        }
        if (req.getDelaiJours() == null || req.getDelaiJours() <= 0) {
            throw new BadRequestException("Le delai doit etre superieur a 0");
        }

        Demande demande = demandeRepository.findById(req.getDemandeId())
                .orElseThrow(() -> new ResourceNotFoundException("Demande introuvable"));
        if (demande.getEtat() != EtatDemande.RECHERCHE_MAKER) {
            throw new BadRequestException("Cette demande n'accepte pas d'offres");
        }

        if (offreMakerRepository.findByDemandeAndMaker(demande, maker).isPresent()) {
            throw new BadRequestException("Vous avez deja propose une offre pour cette demande");
        }

        OffreMaker offre = new OffreMaker(demande, maker, req.getPrix(), req.getDelaiJours());
        offre.setMessage(req.getMessage());
        if (req.getModeLivraison() != null) offre.setModeLivraison(req.getModeLivraison());
        OffreMaker saved = offreMakerRepository.save(offre);

        notificationService.envoyer(demande.getDemandeur(),
                "Nouvelle offre",
                maker.getPrenom() + " " + maker.getNom() + " a propose " + req.getPrix() + " EUR pour votre demande",
                TypeNotification.INFO);

        return saved;
    }

    @Transactional
    public OffreMaker update(Long id, OffreCreateRequest req, Utilisateur maker) {
        OffreMaker offre = findById(id);
        if (!offre.getMaker().getId().equals(maker.getId())) {
            throw new ForbiddenException("Vous ne pouvez modifier que vos propres offres");
        }
        if (offre.getEtat() != EtatOffre.EN_ATTENTE) {
            throw new BadRequestException("Cette offre ne peut plus etre modifiee");
        }
        if (req.getPrix() != null) offre.setPrix(req.getPrix());
        if (req.getDelaiJours() != null) offre.setDelaiJours(req.getDelaiJours());
        if (req.getMessage() != null) offre.setMessage(req.getMessage());
        if (req.getModeLivraison() != null) offre.setModeLivraison(req.getModeLivraison());
        return offreMakerRepository.save(offre);
    }

    @Transactional
    public void annuler(Long id, Utilisateur maker) {
        OffreMaker offre = findById(id);
        if (!offre.getMaker().getId().equals(maker.getId())) {
            throw new ForbiddenException("Vous ne pouvez annuler que vos propres offres");
        }
        if (offre.getEtat() != EtatOffre.EN_ATTENTE) {
            throw new BadRequestException("Cette offre ne peut plus etre annulee");
        }
        offre.setEtat(EtatOffre.ANNULEE);
        offreMakerRepository.save(offre);
    }

    public List<OffreMaker> findOffresAcceptees(Utilisateur maker) {
        return offreMakerRepository.findByMakerOrderByDateCreationDesc(maker).stream()
                .filter(o -> o.getEtat() == EtatOffre.ACCEPTEE)
                .collect(Collectors.toList());
    }
}

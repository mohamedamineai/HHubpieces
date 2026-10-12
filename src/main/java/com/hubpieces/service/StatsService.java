package com.hubpieces.service;

import com.hubpieces.dto.StatsDTO;
import com.hubpieces.model.*;
import com.hubpieces.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatsService {

    private final UtilisateurRepository utilisateurRepository;
    private final DemandeRepository demandeRepository;
    private final OffreMakerRepository offreMakerRepository;
    private final FichierStlRepository fichierStlRepository;
    private final TransactionRepository transactionRepository;
    private final LitigeRepository litigeRepository;

    public StatsService(UtilisateurRepository utilisateurRepository,
                        DemandeRepository demandeRepository,
                        OffreMakerRepository offreMakerRepository,
                        FichierStlRepository fichierStlRepository,
                        TransactionRepository transactionRepository,
                        LitigeRepository litigeRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.demandeRepository = demandeRepository;
        this.offreMakerRepository = offreMakerRepository;
        this.fichierStlRepository = fichierStlRepository;
        this.transactionRepository = transactionRepository;
        this.litigeRepository = litigeRepository;
    }

    public StatsDTO getStats() {
        StatsDTO stats = new StatsDTO();

        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        stats.setTotalUtilisateurs(utilisateurs.size());

        Map<RoleNom, Long> parRole = new HashMap<>();
        for (Utilisateur u : utilisateurs) {
            for (Role r : u.getRoles()) {
                parRole.merge(r.getNom(), 1L, Long::sum);
            }
        }
        stats.setTotalDemandeurs(parRole.getOrDefault(RoleNom.DEMANDEUR, 0L));
        stats.setTotalMakers(parRole.getOrDefault(RoleNom.MAKER, 0L));
        stats.setTotalExpertsCao(parRole.getOrDefault(RoleNom.EXPERT_CAO, 0L));

        stats.setTotalDemandes(demandeRepository.count());
        stats.setTotalOffres(offreMakerRepository.count());
        stats.setTotalFichiersStl(fichierStlRepository.count());
        stats.setFichiersEnAttente(fichierStlRepository.countByEtatValidation(EtatValidation.EN_ATTENTE));

        stats.setTotalTransactions(transactionRepository.count());
        Double mt = transactionRepository.sumMontantByEtat(EtatPaiement.LIBERE);
        stats.setMontantTotalTransactions(mt != null ? mt : 0.0);
        Double comm = transactionRepository.sumCommissionByEtat(EtatPaiement.LIBERE);
        stats.setCommissionTotalePlateforme(comm != null ? comm : 0.0);

        stats.setLitigesOuverts(litigeRepository.countByEtat(EtatLitige.OUVERT)
                + litigeRepository.countByEtat(EtatLitige.EN_COURS));
        stats.setDemandesNouvelles(demandeRepository.countByEtat(EtatDemande.NOUVELLE)
                + demandeRepository.countByEtat(EtatDemande.MODELISATION_REQUISE)
                + demandeRepository.countByEtat(EtatDemande.RECHERCHE_MAKER));
        stats.setDemandesEnCours(demandeRepository.countByEtat(EtatDemande.EN_COURS_IMPRESSION)
                + demandeRepository.countByEtat(EtatDemande.ATTENTE_VALIDATION));
        stats.setDemandesCloturees(demandeRepository.countByEtat(EtatDemande.CLOTUREE));

        LocalDateTime il30j = LocalDateTime.now().minusDays(30);
        stats.setInscriptionsMois(utilisateurs.stream()
                .filter(u -> u.getDateInscription() != null && u.getDateInscription().isAfter(il30j))
                .count());

        return stats;
    }
}

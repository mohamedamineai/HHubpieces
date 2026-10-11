package com.hubpieces.dto;

import com.hubpieces.model.Demande;
import com.hubpieces.model.EtatDemande;
import java.time.LocalDateTime;

public record DemandeView(Long id, String titre, String description, String marque,
        String modeleAppareil, String materiauSouhaite, Double budgetMax,
        String ville, String codePostal, EtatDemande etat, LocalDateTime dateCreation) {
    public static DemandeView from(Demande d) {
        return new DemandeView(d.getId(), d.getTitre(), d.getDescription(), d.getMarque(),
                d.getModeleAppareil(), d.getMateriauSouhaite(), d.getBudgetMax(), d.getVille(),
                d.getCodePostal(), d.getEtat(), d.getDateCreation());
    }
}

package com.hubpieces.service;

import com.hubpieces.dto.*;
import com.hubpieces.model.*;
import com.hubpieces.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.*;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.http.HttpStatus.*;

@Service
@Transactional
public class DemandeService {
    private final DemandeRepository demandes;
    private final UtilisateurRepository utilisateurs;
    public DemandeService(DemandeRepository demandes, UtilisateurRepository utilisateurs) {
        this.demandes = demandes;
        this.utilisateurs = utilisateurs;
    }
    private Utilisateur owner(Long id) {
        if (id == null) throw new ResponseStatusException(UNAUTHORIZED, "Connexion requise");
        Utilisateur u = utilisateurs.findById(id)
                .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Session invalide"));
        if (u.getStatut() != StatutUtilisateur.ACTIF || u.getRoles().stream()
                .noneMatch(r -> r.getNom() == RoleNom.DEMANDEUR))
            throw new ResponseStatusException(FORBIDDEN, "Compte demandeur actif requis");
        return u;
    }
    @Transactional(readOnly = true)
    public Page<DemandeView> list(Long ownerId, int page, int size) {
        owner(ownerId);
        if (page < 0 || size < 1 || size > 50)
            throw new ResponseStatusException(BAD_REQUEST, "Page positive ou nulle et taille de 1 à 50 requises");
        return demandes.findByDemandeurId(ownerId, PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "dateCreation", "id"))).map(DemandeView::from);
    }
    @Transactional(readOnly = true)
    public DashboardView dashboard(Long ownerId) {
        owner(ownerId);
        var counts = new java.util.EnumMap<EtatDemande, Long>(EtatDemande.class);
        for (var state : EtatDemande.values()) counts.put(state, 0L);
        long total = 0;
        for (var row : demandes.countByEtat(ownerId)) {
            total += row.getNombre();
            if (row.getEtat() != null) counts.put(row.getEtat(), row.getNombre());
        }
        var recent = demandes.findByDemandeurId(ownerId, PageRequest.of(0, 5,
                Sort.by(Sort.Direction.DESC, "dateCreation", "id"))).map(DemandeView::from).getContent();
        return new DashboardView(total, counts, recent);
    }
    private Demande owned(Long id, Long ownerId) {
        owner(ownerId);
        return demandes.findByIdAndDemandeurId(id, ownerId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Demande introuvable"));
    }
    @Transactional(readOnly = true)
    public DemandeView get(Long id, Long ownerId) { return DemandeView.from(owned(id, ownerId)); }
    public DemandeView create(Long ownerId, DemandeInput input) {
        Demande d = new Demande(input.titre().trim(), input.description().trim(), owner(ownerId));
        d.setMarque(input.marque()); d.setModeleAppareil(input.modeleAppareil());
        d.setMateriauSouhaite(input.materiauSouhaite()); d.setBudgetMax(input.budgetMax());
        d.setVille(input.ville().trim()); d.setCodePostal(input.codePostal());
        return DemandeView.from(demandes.save(d));
    }
    public DemandeView cancel(Long id, Long ownerId) {
        Demande d = owned(id, ownerId);
        if (d.getEtat() == EtatDemande.ANNULEE) return DemandeView.from(d);
        // Périmètre conservateur : aucune annulation après engagement d'un processus métier.
        if (d.getEtat() != EtatDemande.NOUVELLE)
            throw new ResponseStatusException(CONFLICT, "Seule une nouvelle demande peut être annulée");
        d.setEtat(EtatDemande.ANNULEE);
        return DemandeView.from(demandes.save(d));
    }
}

package com.hubpieces.controller;

import com.hubpieces.dto.DemandeDTO;
import com.hubpieces.dto.FichierStlDTO;
import com.hubpieces.dto.UtilisateurDTO;
import com.hubpieces.exception.ResourceNotFoundException;
import com.hubpieces.model.EtatDemande;
import com.hubpieces.model.FichierStl;
import com.hubpieces.model.RoleNom;
import com.hubpieces.model.Utilisateur;
import com.hubpieces.repository.UtilisateurRepository;
import com.hubpieces.service.DemandeService;
import com.hubpieces.service.FichierStlService;
import com.hubpieces.service.SessionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public")
@Tag(name = "Catalogue public", description = "Consultation publique (sans authentification) : catalogue STL, demandes recentes, liste des makers et experts CAO")
public class CatalogueController {

    private final FichierStlService fichierStlService;
    private final DemandeService demandeService;
    private final UtilisateurRepository utilisateurRepository;
    private final SessionService sessionService;

    public CatalogueController(FichierStlService fichierStlService,
                                DemandeService demandeService,
                                UtilisateurRepository utilisateurRepository,
                                SessionService sessionService) {
        this.fichierStlService = fichierStlService;
        this.demandeService = demandeService;
        this.utilisateurRepository = utilisateurRepository;
        this.sessionService = sessionService;
    }

    // ====== Catalogue STL (accessible a tous) ======

    @GetMapping("/catalogue-stl")
    public ResponseEntity<List<FichierStlDTO>> catalogueStl(
            @RequestParam(required = false) String categorie,
            @RequestParam(required = false) String marque) {
        List<FichierStl> fichiers = fichierStlService.findValides();
        if (categorie != null && !categorie.isBlank()) {
            fichiers = fichiers.stream()
                    .filter(f -> categorie.equalsIgnoreCase(f.getCategorie()))
                    .collect(Collectors.toList());
        }
        if (marque != null && !marque.isBlank()) {
            fichiers = fichiers.stream()
                    .filter(f -> marque.equalsIgnoreCase(f.getMarque()))
                    .collect(Collectors.toList());
        }
        return ResponseEntity.ok(fichiers.stream()
                .map(FichierStlDTO::fromEntity)
                .collect(Collectors.toList()));
    }

    @GetMapping("/catalogue-stl/{id}")
    public ResponseEntity<FichierStlDTO> getFichierStl(@PathVariable Long id) {
        FichierStl f = fichierStlService.findById(id);
        if (f.getEtatValidation() != com.hubpieces.model.EtatValidation.VALIDE) {
            // Seuls admin/expert voient les non valides -> pour l'instant on autorise tout
        }
        return ResponseEntity.ok(FichierStlDTO.fromEntity(f));
    }

    @PostMapping("/catalogue-stl/{id}/telecharger")
    public ResponseEntity<FichierStlDTO> incrementerTelechargement(@PathVariable Long id) {
        fichierStlService.incrementerTelechargements(id);
        return ResponseEntity.ok(FichierStlDTO.fromEntity(fichierStlService.findById(id)));
    }

    // ====== Demandes publiques (anonymisees pour vitrine) ======

    @GetMapping("/demandes-recentes")
    public ResponseEntity<List<DemandeDTO>> demandesRecentes() {
        return ResponseEntity.ok(demandeService.findAll().stream()
                .filter(d -> d.getEtat() == EtatDemande.RECHERCHE_MAKER
                        || d.getEtat() == EtatDemande.NOUVELLE
                        || d.getEtat() == EtatDemande.MODELISATION_REQUISE)
                .limit(20)
                .map(DemandeDTO::fromEntity)
                .collect(Collectors.toList()));
    }

    // ====== Profils publics (makers, experts) ======

    @GetMapping("/makers")
    public ResponseEntity<List<UtilisateurDTO>> listeMakers() {
        List<Utilisateur> makers = utilisateurRepository.findAll().stream()
                .filter(u -> u.getRoles().stream().anyMatch(r -> r.getNom() == RoleNom.MAKER))
                .collect(Collectors.toList());
        return ResponseEntity.ok(makers.stream()
                .map(u -> UtilisateurDTO.fromEntity(u, true))
                .collect(Collectors.toList()));
    }

    @GetMapping("/experts-cao")
    public ResponseEntity<List<UtilisateurDTO>> listeExperts() {
        List<Utilisateur> experts = utilisateurRepository.findAll().stream()
                .filter(u -> u.getRoles().stream().anyMatch(r -> r.getNom() == RoleNom.EXPERT_CAO))
                .collect(Collectors.toList());
        return ResponseEntity.ok(experts.stream()
                .map(u -> UtilisateurDTO.fromEntity(u, true))
                .collect(Collectors.toList()));
    }

    @GetMapping("/utilisateurs/{id}")
    public ResponseEntity<UtilisateurDTO> profilPublic(@PathVariable Long id) {
        Utilisateur u = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));
        return ResponseEntity.ok(UtilisateurDTO.fromEntity(u, true));
    }
}

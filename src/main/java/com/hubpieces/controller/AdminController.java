package com.hubpieces.controller;

import com.hubpieces.dto.*;
import com.hubpieces.exception.ForbiddenException;
import com.hubpieces.model.*;
import com.hubpieces.repository.*;
import com.hubpieces.service.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Administrateur", description = "Endpoints reserves au role ADMIN : gestion des utilisateurs, validation STL, resolution des litiges, statistiques globales")
public class AdminController {

    private final UtilisateurRepository utilisateurRepository;
    private final DemandeService demandeService;
    private final LitigeService litigeService;
    private final TransactionRepository transactionRepository;
    private final FichierStlService fichierStlService;
    private final StatsService statsService;
    private final SessionService sessionService;

    public AdminController(UtilisateurRepository utilisateurRepository,
                            DemandeService demandeService,
                            LitigeService litigeService,
                            TransactionRepository transactionRepository,
                            FichierStlService fichierStlService,
                            StatsService statsService,
                            SessionService sessionService) {
        this.utilisateurRepository = utilisateurRepository;
        this.demandeService = demandeService;
        this.litigeService = litigeService;
        this.transactionRepository = transactionRepository;
        this.fichierStlService = fichierStlService;
        this.statsService = statsService;
        this.sessionService = sessionService;
    }

    // ====== Utilisateurs ======

    @GetMapping("/utilisateurs")
    public ResponseEntity<List<UtilisateurDTO>> listeUtilisateurs(HttpSession session) {
        sessionService.requireRole(session, RoleNom.ADMIN);
        return ResponseEntity.ok(utilisateurRepository.findAll().stream()
                .map(u -> UtilisateurDTO.fromEntity(u, false))
                .collect(Collectors.toList()));
    }

    @GetMapping("/utilisateurs/{id}")
    public ResponseEntity<UtilisateurDTO> getUtilisateur(@PathVariable Long id, HttpSession session) {
        sessionService.requireRole(session, RoleNom.ADMIN);
        Utilisateur u = utilisateurRepository.findById(id)
                .orElseThrow(() -> new com.hubpieces.exception.ResourceNotFoundException("Utilisateur introuvable"));
        return ResponseEntity.ok(UtilisateurDTO.fromEntity(u, false));
    }

    @PutMapping("/utilisateurs/{id}/statut")
    @Transactional
    public ResponseEntity<UtilisateurDTO> changerStatut(@PathVariable Long id,
                                                          @RequestParam StatutUtilisateur statut,
                                                          HttpSession session) {
        sessionService.requireRole(session, RoleNom.ADMIN);
        Utilisateur u = utilisateurRepository.findById(id)
                .orElseThrow(() -> new com.hubpieces.exception.ResourceNotFoundException("Utilisateur introuvable"));
        u.setStatut(statut);
        return ResponseEntity.ok(UtilisateurDTO.fromEntity(utilisateurRepository.save(u), false));
    }

    @DeleteMapping("/utilisateurs/{id}")
    @Transactional
    public ResponseEntity<Void> supprimerUtilisateur(@PathVariable Long id, HttpSession session) {
        Utilisateur admin = sessionService.requireRole(session, RoleNom.ADMIN);
        if (admin.getId().equals(id)) {
            throw new ForbiddenException("Vous ne pouvez pas supprimer votre propre compte");
        }
        Utilisateur u = utilisateurRepository.findById(id)
                .orElseThrow(() -> new com.hubpieces.exception.ResourceNotFoundException("Utilisateur introuvable"));
        utilisateurRepository.delete(u);
        return ResponseEntity.ok().build();
    }

    // ====== Demandes ======

    @GetMapping("/demandes")
    public ResponseEntity<List<DemandeDTO>> listeDemandes(HttpSession session) {
        sessionService.requireRole(session, RoleNom.ADMIN);
        return ResponseEntity.ok(demandeService.findAll().stream()
                .map(DemandeDTO::fromEntity)
                .collect(Collectors.toList()));
    }

    // ====== Litiges ======

    @GetMapping("/litiges")
    public ResponseEntity<List<LitigeDTO>> listeLitiges(HttpSession session) {
        sessionService.requireRole(session, RoleNom.ADMIN);
        return ResponseEntity.ok(litigeService.findAll().stream()
                .map(LitigeDTO::fromEntity)
                .collect(Collectors.toList()));
    }

    @GetMapping("/litiges/ouverts")
    public ResponseEntity<List<LitigeDTO>> litigesOuverts(HttpSession session) {
        sessionService.requireRole(session, RoleNom.ADMIN);
        return ResponseEntity.ok(litigeService.findOuverts().stream()
                .map(LitigeDTO::fromEntity)
                .collect(Collectors.toList()));
    }

    @PutMapping("/litiges/{id}/resoudre")
    public ResponseEntity<LitigeDTO> resoudreLitige(@PathVariable Long id,
                                                      @RequestBody LitigeResolutionRequest req,
                                                      HttpSession session) {
        Utilisateur admin = sessionService.requireRole(session, RoleNom.ADMIN);
        Litige l = litigeService.resoudre(id, req, admin);
        return ResponseEntity.ok(LitigeDTO.fromEntity(l));
    }

    // ====== Transactions ======

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDTO>> listeTransactions(HttpSession session) {
        sessionService.requireRole(session, RoleNom.ADMIN);
        return ResponseEntity.ok(transactionRepository.findAll().stream()
                .map(TransactionDTO::fromEntity)
                .collect(Collectors.toList()));
    }

    // ====== Fichiers STL ======

    @GetMapping("/fichiers-stl/en-attente")
    public ResponseEntity<List<FichierStlDTO>> fichiersEnAttente(HttpSession session) {
        sessionService.requireRole(session, RoleNom.ADMIN);
        return ResponseEntity.ok(fichierStlService.findEnAttente().stream()
                .map(FichierStlDTO::fromEntity)
                .collect(Collectors.toList()));
    }

    @PutMapping("/fichiers-stl/{id}/valider")
    public ResponseEntity<FichierStlDTO> validerFichier(@PathVariable Long id, HttpSession session) {
        Utilisateur admin = sessionService.requireRole(session, RoleNom.ADMIN);
        FichierStl f = fichierStlService.valider(id, admin);
        return ResponseEntity.ok(FichierStlDTO.fromEntity(f));
    }

    @PutMapping("/fichiers-stl/{id}/rejeter")
    public ResponseEntity<FichierStlDTO> rejeterFichier(@PathVariable Long id,
                                                         @RequestParam(required = false, defaultValue = "Non conforme") String motif,
                                                         HttpSession session) {
        Utilisateur admin = sessionService.requireRole(session, RoleNom.ADMIN);
        FichierStl f = fichierStlService.rejeter(id, motif, admin);
        return ResponseEntity.ok(FichierStlDTO.fromEntity(f));
    }

    // ====== Statistiques ======

    @GetMapping("/stats")
    public ResponseEntity<StatsDTO> stats(HttpSession session) {
        sessionService.requireRole(session, RoleNom.ADMIN);
        return ResponseEntity.ok(statsService.getStats());
    }
}

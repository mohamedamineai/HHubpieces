package com.hubpieces.controller;

import com.hubpieces.dto.*;
import com.hubpieces.exception.BadRequestException;
import com.hubpieces.exception.ForbiddenException;
import com.hubpieces.model.*;
import com.hubpieces.repository.*;
import com.hubpieces.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    @Operation(summary = "Lister les comptes", description = "Retourne tous les utilisateurs avec leurs rôles et leur statut courant.")
    public ResponseEntity<List<UtilisateurDTO>> listeUtilisateurs(HttpSession session) {
        sessionService.requireRole(session, RoleNom.ADMIN);
        return ResponseEntity.ok(utilisateurRepository.findAll().stream()
                .map(u -> UtilisateurDTO.fromEntity(u, false))
                .collect(Collectors.toList()));
    }

    @GetMapping("/utilisateurs/{id}")
    @Operation(summary = "Consulter un compte", description = "Retourne les coordonnées et rôles visibles par l'administrateur.")
    public ResponseEntity<UtilisateurDTO> getUtilisateur(@PathVariable Long id, HttpSession session) {
        sessionService.requireRole(session, RoleNom.ADMIN);
        Utilisateur u = utilisateurRepository.findById(id)
                .orElseThrow(() -> new com.hubpieces.exception.ResourceNotFoundException("Utilisateur introuvable"));
        return ResponseEntity.ok(UtilisateurDTO.fromEntity(u, false));
    }

    @PutMapping("/utilisateurs/{id}/statut")
    @Operation(summary = "Activer ou suspendre un compte", description = "Paramètre query `statut` : ACTIF ou SUSPENDU. Un administrateur ne peut pas suspendre son propre compte; une suspension invalide la session au prochain appel.")
    @Transactional
    public ResponseEntity<UtilisateurDTO> changerStatut(@PathVariable Long id,
                                                          @RequestParam StatutUtilisateur statut,
                                                          HttpSession session) {
        Utilisateur admin = sessionService.requireRole(session, RoleNom.ADMIN);
        if (statut != StatutUtilisateur.ACTIF && statut != StatutUtilisateur.SUSPENDU) {
            throw new BadRequestException("Un administrateur peut uniquement activer ou suspendre un compte");
        }
        if (admin.getId().equals(id) && statut == StatutUtilisateur.SUSPENDU) {
            throw new ForbiddenException("Vous ne pouvez pas suspendre votre propre compte");
        }
        Utilisateur u = utilisateurRepository.findById(id)
                .orElseThrow(() -> new com.hubpieces.exception.ResourceNotFoundException("Utilisateur introuvable"));
        u.setStatut(statut);
        return ResponseEntity.ok(UtilisateurDTO.fromEntity(utilisateurRepository.save(u), false));
    }

    @DeleteMapping("/utilisateurs/{id}")
    @Operation(summary = "Supprimer un compte sans historique lié", description = "Suppression physique interdite pour le compte admin courant; si des données référencent le compte, préférez la suspension afin de conserver l'historique.")
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
    @Operation(summary = "Lister les demandes", description = "Vue d'ensemble des demandes et de leur état métier courant.")
    public ResponseEntity<List<DemandeDTO>> listeDemandes(HttpSession session) {
        sessionService.requireRole(session, RoleNom.ADMIN);
        return ResponseEntity.ok(demandeService.findAll().stream()
                .map(DemandeDTO::fromEntity)
                .collect(Collectors.toList()));
    }

    // ====== Litiges ======

    @GetMapping("/litiges")
    @Operation(summary = "Lister les litiges", description = "Retourne les dossiers ouverts, en cours et clôturés, du plus récent au plus ancien.")
    public ResponseEntity<List<LitigeDTO>> listeLitiges(HttpSession session) {
        sessionService.requireRole(session, RoleNom.ADMIN);
        return ResponseEntity.ok(litigeService.findAll().stream()
                .map(LitigeDTO::fromEntity)
                .collect(Collectors.toList()));
    }

    @GetMapping("/litiges/ouverts")
    @Operation(summary = "Lister les litiges à traiter", description = "Inclut les états OUVERT et EN_COURS; le paiement reste bloqué pendant l'instruction.")
    public ResponseEntity<List<LitigeDTO>> litigesOuverts(HttpSession session) {
        sessionService.requireRole(session, RoleNom.ADMIN);
        return ResponseEntity.ok(litigeService.findOuverts().stream()
                .map(LitigeDTO::fromEntity)
                .collect(Collectors.toList()));
    }

    @PutMapping("/litiges/{id}/resoudre")
    @Operation(summary = "Prendre en charge ou résoudre un litige", description = "Avec etat=EN_COURS, maintient le paiement bloqué. Avec etat=RESOLU ou FERME, le texte de decision détermine la libération ou le remboursement; la décision est notifiée aux deux acteurs.")
    public ResponseEntity<LitigeDTO> resoudreLitige(@PathVariable Long id,
                                                      @RequestBody LitigeResolutionRequest req,
                                                      HttpSession session) {
        Utilisateur admin = sessionService.requireRole(session, RoleNom.ADMIN);
        Litige l = litigeService.resoudre(id, req, admin);
        return ResponseEntity.ok(LitigeDTO.fromEntity(l));
    }

    // ====== Transactions ======

    @GetMapping("/transactions")
    @Operation(summary = "Consulter les transactions", description = "Retourne montants, acteurs, commissions et état du séquestre sans effectuer d'opération de paiement réelle.")
    public ResponseEntity<List<TransactionDTO>> listeTransactions(HttpSession session) {
        sessionService.requireRole(session, RoleNom.ADMIN);
        return ResponseEntity.ok(transactionRepository.findAll().stream()
                .map(TransactionDTO::fromEntity)
                .collect(Collectors.toList()));
    }

    // ====== Fichiers STL ======

    @GetMapping("/fichiers-stl/en-attente")
    @Operation(summary = "Lister les fichiers 3D en attente", description = "File de modération des fichiers transmis par les experts CAO.")
    public ResponseEntity<List<FichierStlDTO>> fichiersEnAttente(HttpSession session) {
        sessionService.requireRole(session, RoleNom.ADMIN);
        return ResponseEntity.ok(fichierStlService.findEnAttente().stream()
                .map(FichierStlDTO::fromEntity)
                .collect(Collectors.toList()));
    }

    @PutMapping("/fichiers-stl/{id}/valider")
    @Operation(summary = "Valider un fichier 3D", description = "Publie le fichier dans le catalogue; si le fichier est rattaché à une demande en CAO, cette demande passe en RECHERCHE_MAKER.")
    public ResponseEntity<FichierStlDTO> validerFichier(@PathVariable Long id, HttpSession session) {
        Utilisateur admin = sessionService.requireRole(session, RoleNom.ADMIN);
        FichierStl f = fichierStlService.valider(id, admin);
        return ResponseEntity.ok(FichierStlDTO.fromEntity(f));
    }

    @PutMapping("/fichiers-stl/{id}/rejeter")
    @Operation(summary = "Rejeter un fichier 3D", description = "Le paramètre motif est communiqué à l'expert; une demande CAO liée est replacée dans la file de modélisation.")
    public ResponseEntity<FichierStlDTO> rejeterFichier(@PathVariable Long id,
                                                         @RequestParam(required = false, defaultValue = "Non conforme") String motif,
                                                         HttpSession session) {
        Utilisateur admin = sessionService.requireRole(session, RoleNom.ADMIN);
        FichierStl f = fichierStlService.rejeter(id, motif, admin);
        return ResponseEntity.ok(FichierStlDTO.fromEntity(f));
    }

    // ====== Statistiques ======

    @GetMapping("/stats")
    @Operation(summary = "Consulter les statistiques globales", description = "Comptages d'utilisateurs, demandes, offres, fichiers, transactions, séquestres et litiges.")
    public ResponseEntity<StatsDTO> stats(HttpSession session) {
        sessionService.requireRole(session, RoleNom.ADMIN);
        return ResponseEntity.ok(statsService.getStats());
    }
}

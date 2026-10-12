package com.hubpieces.controller;

import com.hubpieces.dto.*;
import com.hubpieces.exception.UnauthorizedException;
import com.hubpieces.model.*;
import com.hubpieces.service.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/demandeur")
@Tag(name = "Demandeur", description = "Endpoints reserves au role DEMANDEUR : gestion des demandes, consultation des offres, messagerie, avis, litiges")
public class DemandeurController {

    private final DemandeService demandeService;
    private final MessageService messageService;
    private final AvisService avisService;
    private final LitigeService litigeService;
    private final NotificationService notificationService;
    private final SessionService sessionService;

    public DemandeurController(DemandeService demandeService,
                                MessageService messageService,
                                AvisService avisService,
                                LitigeService litigeService,
                                NotificationService notificationService,
                                SessionService sessionService) {
        this.demandeService = demandeService;
        this.messageService = messageService;
        this.avisService = avisService;
        this.litigeService = litigeService;
        this.notificationService = notificationService;
        this.sessionService = sessionService;
    }

    // ====== Demandes ======

    @PostMapping("/demandes")
    public ResponseEntity<DemandeDTO> creerDemande(@RequestBody DemandeCreateRequest req, HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.DEMANDEUR);
        Demande created = demandeService.create(req, u);
        return ResponseEntity.ok(DemandeDTO.fromEntity(created));
    }

    @GetMapping("/demandes")
    public ResponseEntity<List<DemandeDTO>> mesDemandes(HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.DEMANDEUR);
        List<DemandeDTO> dtos = demandeService.findByDemandeur(u).stream()
                .map(DemandeDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/demandes/{id}")
    public ResponseEntity<DemandeDTO> getDemande(@PathVariable Long id, HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.DEMANDEUR);
        Demande d = demandeService.findById(id);
        if (!d.getDemandeur().getId().equals(u.getId())) {
            throw new UnauthorizedException("Cette demande n'est pas la votre");
        }
        return ResponseEntity.ok(DemandeDTO.fromEntity(d));
    }

    @PutMapping("/demandes/{id}")
    public ResponseEntity<DemandeDTO> modifierDemande(@PathVariable Long id,
                                                       @RequestBody DemandeCreateRequest req,
                                                       HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.DEMANDEUR);
        Demande d = demandeService.update(id, req, u);
        return ResponseEntity.ok(DemandeDTO.fromEntity(d));
    }

    @DeleteMapping("/demandes/{id}")
    public ResponseEntity<DemandeDTO> annulerDemande(@PathVariable Long id, HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.DEMANDEUR);
        Demande d = demandeService.annuler(id, u);
        return ResponseEntity.ok(DemandeDTO.fromEntity(d));
    }

    @PostMapping("/demandes/{id}/cloturer")
    public ResponseEntity<DemandeDTO> cloturer(@PathVariable Long id, HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.DEMANDEUR);
        Demande d = demandeService.cloturer(id, u);
        return ResponseEntity.ok(DemandeDTO.fromEntity(d));
    }

    // ====== Offres ======

    @GetMapping("/demandes/{id}/offres")
    public ResponseEntity<List<OffreMakerDTO>> getOffres(@PathVariable Long id, HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.DEMANDEUR);
        Demande d = demandeService.findById(id);
        if (!d.getDemandeur().getId().equals(u.getId())) {
            throw new UnauthorizedException("Cette demande n'est pas la votre");
        }
        return ResponseEntity.ok(demandeService.getOffres(id).stream()
                .map(OffreMakerDTO::fromEntity)
                .collect(Collectors.toList()));
    }

    @PostMapping("/demandes/{demandeId}/offres/{offreId}/accepter")
    public ResponseEntity<DemandeDTO> accepterOffre(@PathVariable Long demandeId,
                                                     @PathVariable Long offreId,
                                                     HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.DEMANDEUR);
        Demande d = demandeService.accepterOffre(demandeId, offreId, u);
        return ResponseEntity.ok(DemandeDTO.fromEntity(d));
    }

    @PostMapping("/demandes/{demandeId}/offres/{offreId}/refuser")
    public ResponseEntity<DemandeDTO> refuserOffre(@PathVariable Long demandeId,
                                                    @PathVariable Long offreId,
                                                    HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.DEMANDEUR);
        Demande d = demandeService.refuserOffre(demandeId, offreId, u);
        return ResponseEntity.ok(DemandeDTO.fromEntity(d));
    }

    // ====== Messages ======

    @GetMapping("/messages")
    public ResponseEntity<List<MessageDTO>> mesMessages(HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.DEMANDEUR);
        return ResponseEntity.ok(messageService.findConversations(u).stream()
                .map(MessageDTO::fromEntity)
                .collect(Collectors.toList()));
    }

    @PostMapping("/messages")
    public ResponseEntity<MessageDTO> envoyerMessage(@RequestBody MessageCreateRequest req, HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.DEMANDEUR);
        Message m = messageService.envoyer(req, u);
        return ResponseEntity.ok(MessageDTO.fromEntity(m));
    }

    @PostMapping("/messages/{id}/lu")
    public ResponseEntity<Void> marquerLu(@PathVariable Long id, HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.DEMANDEUR);
        messageService.marquerCommeLu(id, u);
        return ResponseEntity.ok().build();
    }

    // ====== Avis ======

    @PostMapping("/avis")
    public ResponseEntity<AvisDTO> deposerAvis(@RequestBody AvisCreateRequest req, HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.DEMANDEUR);
        Avis a = avisService.create(req, u);
        return ResponseEntity.ok(AvisDTO.fromEntity(a));
    }

    // ====== Litiges ======

    @PostMapping("/litiges")
    public ResponseEntity<LitigeDTO> ouvrirLitige(@RequestBody LitigeCreateRequest req, HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.DEMANDEUR);
        Litige l = litigeService.ouvrir(req, u);
        return ResponseEntity.ok(LitigeDTO.fromEntity(l));
    }

    // ====== Notifications ======

    @GetMapping("/notifications")
    public ResponseEntity<List<NotificationDTO>> mesNotifications(HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.DEMANDEUR);
        return ResponseEntity.ok(notificationService.findByUtilisateur(u));
    }

    @PostMapping("/notifications/{id}/lu")
    public ResponseEntity<Void> marquerNotifLue(@PathVariable Long id, HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.DEMANDEUR);
        notificationService.marquerCommeLue(id, u);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/notifications/toutes-lues")
    public ResponseEntity<Void> marquerToutesLues(HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.DEMANDEUR);
        notificationService.marquerToutesCommeLues(u);
        return ResponseEntity.ok().build();
    }

    // ====== Statistiques personnelles ======

    @GetMapping("/stats")
    public ResponseEntity<java.util.Map<String, Long>> stats(HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.DEMANDEUR);
        java.util.Map<String, Long> s = new java.util.HashMap<>();
        s.put("total", demandeService.findByDemandeur(u).stream().count());
        s.put("nouvelles", demandeService.findByDemandeur(u).stream()
                .filter(d -> d.getEtat() == EtatDemande.NOUVELLE
                        || d.getEtat() == EtatDemande.MODELISATION_REQUISE
                        || d.getEtat() == EtatDemande.RECHERCHE_MAKER).count());
        s.put("enCours", demandeService.findByDemandeur(u).stream()
                .filter(d -> d.getEtat() == EtatDemande.EN_COURS_IMPRESSION).count());
        s.put("cloturees", demandeService.findByDemandeur(u).stream()
                .filter(d -> d.getEtat() == EtatDemande.CLOTUREE).count());
        s.put("notificationsNonLues", notificationService.countNonLues(u));
        return ResponseEntity.ok(s);
    }
}

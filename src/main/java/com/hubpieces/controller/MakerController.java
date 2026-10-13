package com.hubpieces.controller;

import com.hubpieces.dto.*;
import com.hubpieces.exception.ForbiddenException;
import com.hubpieces.model.*;
import com.hubpieces.repository.ProfilMakerRepository;
import com.hubpieces.service.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/maker")
@Tag(name = "Maker", description = "Endpoints reserves au role MAKER : consultation des demandes, depot d'offres, gestion des commandes et du profil")
public class MakerController {

    private final DemandeService demandeService;
    private final OffreMakerService offreMakerService;
    private final MessageService messageService;
    private final NotificationService notificationService;
    private final SessionService sessionService;
    private final ProfilMakerRepository profilMakerRepository;
    private final AvisService avisService;

    public MakerController(DemandeService demandeService,
                           OffreMakerService offreMakerService,
                           MessageService messageService,
                           NotificationService notificationService,
                           SessionService sessionService,
                           ProfilMakerRepository profilMakerRepository,
                           AvisService avisService) {
        this.demandeService = demandeService;
        this.offreMakerService = offreMakerService;
        this.messageService = messageService;
        this.notificationService = notificationService;
        this.sessionService = sessionService;
        this.profilMakerRepository = profilMakerRepository;
        this.avisService = avisService;
    }

    // ====== Demandes disponibles ======

    @GetMapping("/demandes-disponibles")
    public ResponseEntity<List<DemandeDTO>> demandesDisponibles(HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.MAKER);
        return ResponseEntity.ok(demandeService.findDisponiblesPourMaker(u).stream()
                .map(DemandeDTO::fromEntity)
                .collect(Collectors.toList()));
    }

    // ====== Offres ======

    @PostMapping("/offres")
    public ResponseEntity<OffreMakerDTO> deposerOffre(@RequestBody OffreCreateRequest req, HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.MAKER);
        OffreMaker o = offreMakerService.create(req, u);
        return ResponseEntity.ok(OffreMakerDTO.fromEntity(o));
    }

    @GetMapping("/offres")
    public ResponseEntity<List<OffreMakerDTO>> mesOffres(HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.MAKER);
        return ResponseEntity.ok(offreMakerService.findByMaker(u).stream()
                .map(OffreMakerDTO::fromEntity)
                .collect(Collectors.toList()));
    }

    @PutMapping("/offres/{id}")
    public ResponseEntity<OffreMakerDTO> modifierOffre(@PathVariable Long id,
                                                        @RequestBody OffreCreateRequest req,
                                                        HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.MAKER);
        OffreMaker o = offreMakerService.update(id, req, u);
        return ResponseEntity.ok(OffreMakerDTO.fromEntity(o));
    }

    @DeleteMapping("/offres/{id}")
    public ResponseEntity<Void> annulerOffre(@PathVariable Long id, HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.MAKER);
        offreMakerService.annuler(id, u);
        return ResponseEntity.ok().build();
    }

    // ====== Commandes (offres acceptées) ======

    @GetMapping("/commandes")
    public ResponseEntity<List<OffreMakerDTO>> mesCommandes(HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.MAKER);
        return ResponseEntity.ok(offreMakerService.findOffresAcceptees(u).stream()
                .map(OffreMakerDTO::fromEntity)
                .collect(Collectors.toList()));
    }

    @PostMapping("/commandes/{offreId}/lancer-impression")
    public ResponseEntity<DemandeDTO> lancerImpression(@PathVariable Long offreId, HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.MAKER);
        OffreMaker o = offreMakerService.findById(offreId);
        if (!o.getMaker().getId().equals(u.getId())) {
            throw new ForbiddenException("Cette offre ne vous appartient pas");
        }
        Demande d = demandeService.marquerImpressionLancee(o.getDemande().getId(), u);
        return ResponseEntity.ok(DemandeDTO.fromEntity(d));
    }

    @PostMapping("/commandes/{offreId}/terminer")
    public ResponseEntity<DemandeDTO> terminerImpression(@PathVariable Long offreId, HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.MAKER);
        OffreMaker o = offreMakerService.findById(offreId);
        if (!o.getMaker().getId().equals(u.getId())) {
            throw new ForbiddenException("Cette offre ne vous appartient pas");
        }
        Demande d = demandeService.marquerEnAttenteValidation(o.getDemande().getId(), u);
        return ResponseEntity.ok(DemandeDTO.fromEntity(d));
    }

    // ====== Profil ======

    @GetMapping("/profil")
    @Transactional
    public ResponseEntity<ProfilMaker> getProfil(HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.MAKER);
        return ResponseEntity.ok(profilMakerRepository.findAll().stream()
                .filter(p -> p.getUtilisateur().getId().equals(u.getId()))
                .findFirst()
                .orElseGet(() -> {
                    ProfilMaker p = new ProfilMaker(u);
                    return profilMakerRepository.save(p);
                }));
    }

    @PutMapping("/profil")
    @Transactional
    public ResponseEntity<ProfilMaker> updateProfil(@RequestBody com.hubpieces.dto.ProfilMakerUpdateRequest profil, HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.MAKER);
        ProfilMaker existing = profilMakerRepository.findAll().stream()
                .filter(p -> p.getUtilisateur().getId().equals(u.getId()))
                .findFirst()
                .orElseGet(() -> {
                    ProfilMaker p = new ProfilMaker(u);
                    return profilMakerRepository.save(p);
                });
        if (profil.getRayonActionKm() != null) existing.setRayonActionKm(profil.getRayonActionKm());
        if (profil.getMateriauxDisponibles() != null) existing.setMateriauxDisponibles(profil.getMateriauxDisponibles());
        if (profil.getDescriptionImprimante() != null) existing.setDescriptionImprimante(profil.getDescriptionImprimante());
        return ResponseEntity.ok(profilMakerRepository.save(existing));
    }

    // ====== Avis recus ======

    @GetMapping("/avis")
    public ResponseEntity<List<AvisDTO>> mesAvis(HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.MAKER);
        return ResponseEntity.ok(avisService.findByDestinataire(u).stream()
                .map(AvisDTO::fromEntity)
                .collect(Collectors.toList()));
    }

    // ====== Messages ======

    @GetMapping("/messages")
    public ResponseEntity<List<MessageDTO>> mesMessages(HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.MAKER);
        return ResponseEntity.ok(messageService.findConversations(u).stream()
                .map(MessageDTO::fromEntity)
                .collect(Collectors.toList()));
    }

    @PostMapping("/messages")
    public ResponseEntity<MessageDTO> envoyerMessage(@RequestBody MessageCreateRequest req, HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.MAKER);
        Message m = messageService.envoyer(req, u);
        return ResponseEntity.ok(MessageDTO.fromEntity(m));
    }

    // ====== Notifications ======

    @GetMapping("/notifications")
    public ResponseEntity<List<NotificationDTO>> mesNotifications(HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.MAKER);
        return ResponseEntity.ok(notificationService.findByUtilisateur(u));
    }

    @PostMapping("/notifications/{id}/lu")
    public ResponseEntity<Void> marquerNotifLue(@PathVariable Long id, HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.MAKER);
        notificationService.marquerCommeLue(id, u);
        return ResponseEntity.ok().build();
    }

    // ====== Statistiques ======

    @GetMapping("/stats")
    public ResponseEntity<java.util.Map<String, Long>> stats(HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.MAKER);
        java.util.Map<String, Long> s = new java.util.HashMap<>();
        s.put("offresTotal", offreMakerService.findByMaker(u).stream().count());
        s.put("offresEnAttente", offreMakerService.findByMaker(u).stream()
                .filter(o -> o.getEtat() == EtatOffre.EN_ATTENTE).count());
        s.put("commandesActives", offreMakerService.findByMaker(u).stream()
                .filter(o -> o.getEtat() == EtatOffre.ACCEPTEE).count());
        s.put("notificationsNonLues", notificationService.countNonLues(u));
        return ResponseEntity.ok(s);
    }
}

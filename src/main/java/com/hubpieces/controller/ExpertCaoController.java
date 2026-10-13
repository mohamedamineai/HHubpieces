package com.hubpieces.controller;

import com.hubpieces.dto.*;
import com.hubpieces.model.*;
import com.hubpieces.repository.ProfilExpertCaoRepository;
import com.hubpieces.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/expert-cao")
@Tag(name = "Expert CAO", description = "Endpoints reserves au role EXPERT_CAO : modelisation 3D, gestion du catalogue STL et du profil")
public class ExpertCaoController {

    private final DemandeService demandeService;
    private final FichierStlService fichierStlService;
    private final ProfilExpertCaoRepository profilExpertCaoRepository;
    private final NotificationService notificationService;
    private final SessionService sessionService;

    public ExpertCaoController(DemandeService demandeService,
                                FichierStlService fichierStlService,
                                ProfilExpertCaoRepository profilExpertCaoRepository,
                                NotificationService notificationService,
                                SessionService sessionService) {
        this.demandeService = demandeService;
        this.fichierStlService = fichierStlService;
        this.profilExpertCaoRepository = profilExpertCaoRepository;
        this.notificationService = notificationService;
        this.sessionService = sessionService;
    }

    // ====== Demandes a modeliser ======

    @GetMapping("/demandes-a-modeliser")
    public ResponseEntity<List<DemandeDTO>> demandesAModeliser(HttpSession session) {
        sessionService.requireRole(session, RoleNom.EXPERT_CAO);
        return ResponseEntity.ok(demandeService.findAValiderParExpert().stream()
                .map(DemandeDTO::fromEntity)
                .collect(Collectors.toList()));
    }

    @PostMapping("/demandes/{id}/modelisation")
    @Operation(summary = "Soumettre une ressource 3D déjà présente sur le serveur",
            description = "Crée une fiche de fichier en attente et l'associe à une demande CAO. Le chemin doit pointer vers une ressource existante sous images/ ou /uploads/stl/. Pour un fichier local, utilisez l'endpoint multipart /upload.")
    @Transactional
    public ResponseEntity<DemandeDTO> soumettreModelisation(@PathVariable Long id,
                                                             @RequestBody FichierStlCreateRequest req,
                                                             HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.EXPERT_CAO);
        demandeService.verifierDemandeAModeliser(id);
        FichierStl fichier = fichierStlService.create(req, u);
        Demande demande = demandeService.soumettreModelisation(id, fichier, u);
        return ResponseEntity.ok(DemandeDTO.fromEntity(demande));
    }

    @PostMapping(value = "/demandes/{id}/modelisation/upload", consumes = "multipart/form-data")
    @Operation(summary = "Transmettre un fichier CAO pour une demande",
            description = "Envoie un STL, OBJ, 3MF ou STEP. Le fichier reste en attente tant que l'admin ne l'a pas validé. La demande ne passe en recherche de maker qu'après validation.")
    @Transactional
    public ResponseEntity<DemandeDTO> uploadModelisation(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "nom", required = false) String nom,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "categorie", required = false) String categorie,
            @RequestParam(value = "tags", required = false) String tags,
            @RequestParam(value = "marque", required = false) String marque,
            @RequestParam(value = "modeleAppareil", required = false) String modeleAppareil,
            @RequestParam(value = "materiauRecommande", required = false) String materiauRecommande,
            @RequestParam(value = "dimensions", required = false) String dimensions,
            HttpSession session) throws IOException {
        Utilisateur u = sessionService.requireRole(session, RoleNom.EXPERT_CAO);
        demandeService.verifierDemandeAModeliser(id);
        FichierStl fichier = fichierStlService.uploadAndCreate(file, nom, description, categorie, tags,
                marque, modeleAppareil, materiauRecommande, dimensions, u);
        Demande demande = demandeService.soumettreModelisation(id, fichier, u);
        return ResponseEntity.ok(DemandeDTO.fromEntity(demande));
    }

    // ====== Fichiers STL ======

    @GetMapping("/fichiers-stl")
    public ResponseEntity<List<FichierStlDTO>> mesFichiers(HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.EXPERT_CAO);
        return ResponseEntity.ok(fichierStlService.findByUploadeur(u).stream()
                .map(FichierStlDTO::fromEntity)
                .collect(Collectors.toList()));
    }

    @GetMapping("/fichiers-stl/valides")
    public ResponseEntity<List<FichierStlDTO>> fichiersValides(HttpSession session) {
        sessionService.requireRole(session, RoleNom.EXPERT_CAO);
        return ResponseEntity.ok(fichierStlService.findValides().stream()
                .map(FichierStlDTO::fromEntity)
                .collect(Collectors.toList()));
    }

    @PostMapping("/fichiers-stl")
    public ResponseEntity<FichierStlDTO> creerFichier(@RequestBody FichierStlCreateRequest req, HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.EXPERT_CAO);
        FichierStl f = fichierStlService.create(req, u);
        return ResponseEntity.ok(FichierStlDTO.fromEntity(f));
    }

    @PutMapping("/fichiers-stl/{id}")
    public ResponseEntity<FichierStlDTO> modifierFichier(@PathVariable Long id,
                                                          @RequestBody FichierStlCreateRequest req,
                                                          HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.EXPERT_CAO);
        FichierStl f = fichierStlService.update(id, req, u);
        return ResponseEntity.ok(FichierStlDTO.fromEntity(f));
    }

    @DeleteMapping("/fichiers-stl/{id}")
    public ResponseEntity<Void> supprimerFichier(@PathVariable Long id, HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.EXPERT_CAO);
        fichierStlService.delete(id, u);
        return ResponseEntity.ok().build();
    }

    // ====== Profil ======

    @GetMapping("/profil")
    @Transactional
    public ResponseEntity<ProfilExpertCao> getProfil(HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.EXPERT_CAO);
        return ResponseEntity.ok(profilExpertCaoRepository.findAll().stream()
                .filter(p -> p.getUtilisateur().getId().equals(u.getId()))
                .findFirst()
                .orElseGet(() -> {
                    ProfilExpertCao p = new ProfilExpertCao(u);
                    return profilExpertCaoRepository.save(p);
                }));
    }

    @PutMapping("/profil")
    @Transactional
    public ResponseEntity<ProfilExpertCao> updateProfil(@RequestBody com.hubpieces.dto.ProfilExpertCaoUpdateRequest profil, HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.EXPERT_CAO);
        ProfilExpertCao existing = profilExpertCaoRepository.findAll().stream()
                .filter(p -> p.getUtilisateur().getId().equals(u.getId()))
                .findFirst()
                .orElseGet(() -> {
                    ProfilExpertCao p = new ProfilExpertCao(u);
                    return profilExpertCaoRepository.save(p);
                });
        if (profil.getLogicielsMaitrise() != null) existing.setLogicielsMaitrise(profil.getLogicielsMaitrise());
        if (profil.getPortfolioUrl() != null) existing.setPortfolioUrl(profil.getPortfolioUrl());
        if (profil.getDescription() != null) existing.setDescription(profil.getDescription());
        return ResponseEntity.ok(profilExpertCaoRepository.save(existing));
    }

    // ====== Notifications ======

    @GetMapping("/notifications")
    public ResponseEntity<List<NotificationDTO>> mesNotifications(HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.EXPERT_CAO);
        return ResponseEntity.ok(notificationService.findByUtilisateur(u));
    }

    // ====== Statistiques ======

    @GetMapping("/stats")
    public ResponseEntity<java.util.Map<String, Long>> stats(HttpSession session) {
        Utilisateur u = sessionService.requireRole(session, RoleNom.EXPERT_CAO);
        List<FichierStl> fichiers = fichierStlService.findByUploadeur(u);
        java.util.Map<String, Long> s = new java.util.HashMap<>();
        s.put("demandesAModeliser", (long) demandeService.findAValiderParExpert().size());
        s.put("fichiersTotal", (long) fichiers.size());
        s.put("fichiersEnAttente", fichiers.stream()
                .filter(f -> f.getEtatValidation() == EtatValidation.EN_ATTENTE).count());
        s.put("fichiersValides", fichiers.stream()
                .filter(f -> f.getEtatValidation() == EtatValidation.VALIDE).count());
        s.put("telechargements", fichiers.stream()
                .mapToLong(f -> f.getNombreTelechargements() == null ? 0 : f.getNombreTelechargements()).sum());
        s.put("notificationsNonLues", notificationService.countNonLues(u));
        return ResponseEntity.ok(s);
    }
}

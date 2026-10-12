package com.hubpieces.controller;

import com.hubpieces.dto.UtilisateurDTO;
import com.hubpieces.dto.UtilisateurUpdateRequest;
import com.hubpieces.exception.UnauthorizedException;
import com.hubpieces.model.Utilisateur;
import com.hubpieces.repository.UtilisateurRepository;
import com.hubpieces.service.SessionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    private final UtilisateurRepository utilisateurRepository;
    private final SessionService sessionService;

    public UtilisateurController(UtilisateurRepository utilisateurRepository,
                                  SessionService sessionService) {
        this.utilisateurRepository = utilisateurRepository;
        this.sessionService = sessionService;
    }

    @GetMapping("/me")
    public ResponseEntity<UtilisateurDTO> me(HttpSession session) {
        Utilisateur u = sessionService.requireUser(session);
        return ResponseEntity.ok(UtilisateurDTO.fromEntity(u, false));
    }

    @PutMapping("/me")
    @Transactional
    public ResponseEntity<UtilisateurDTO> updateMe(@RequestBody UtilisateurUpdateRequest req, HttpSession session) {
        Utilisateur u = sessionService.requireUser(session);
        if (req.getNom() != null) u.setNom(req.getNom());
        if (req.getPrenom() != null) u.setPrenom(req.getPrenom());
        if (req.getTelephone() != null) u.setTelephone(req.getTelephone());
        if (req.getVille() != null) u.setVille(req.getVille());
        if (req.getCodePostal() != null) u.setCodePostal(req.getCodePostal());
        if (req.getPhotoProfil() != null) u.setPhotoProfil(req.getPhotoProfil());
        return ResponseEntity.ok(UtilisateurDTO.fromEntity(utilisateurRepository.save(u), false));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> getById(@PathVariable Long id) {
        Utilisateur u = utilisateurRepository.findById(id)
                .orElseThrow(() -> new com.hubpieces.exception.ResourceNotFoundException("Utilisateur introuvable"));
        return ResponseEntity.ok(UtilisateurDTO.fromEntity(u, true));
    }
}

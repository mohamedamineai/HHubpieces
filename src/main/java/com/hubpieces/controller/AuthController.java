package com.hubpieces.controller;

import com.hubpieces.dto.AuthResponse;
import com.hubpieces.dto.LoginRequest;
import com.hubpieces.dto.RegisterRequest;
import com.hubpieces.model.RoleNom;
import com.hubpieces.model.Utilisateur;
import com.hubpieces.service.AuthService;
import com.hubpieces.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentification", description = "Inscription, connexion, deconnexion et informations de session")
public class AuthController {

    private final AuthService authService;
    private final SessionService sessionService;

    public AuthController(AuthService authService, SessionService sessionService) {
        this.authService = authService;
        this.sessionService = sessionService;
    }

    @PostMapping("/register")
    @Operation(summary = "Inscription d'un nouvel utilisateur",
               description = "Permet de créer un compte DEMANDEUR, MAKER ou EXPERT_CAO. La création d'un compte ADMIN n'est pas publique. Les champs spécifiques (rayon, matériaux, logiciels CAO, etc.) dépendent du rôle choisi.")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request, HttpSession session) {
        AuthResponse response = authService.register(request);
        if (response.isSuccess()) {
            storeSession(session, response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Connexion",
               description = "Authentifie un utilisateur par email/mot de passe. La session est conservee dans un cookie JSESSIONID.")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request, HttpSession session) {
        AuthResponse response = authService.login(request);
        if (response.isSuccess()) {
            storeSession(session, response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "Deconnexion", description = "Invalide la session courante")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    @Operation(summary = "Utilisateur courant", description = "Retourne les informations de l'utilisateur connecte")
    public ResponseEntity<?> me(HttpSession session) {
        Utilisateur utilisateur = sessionService.requireUser(session);
        String role = RoleNom.principal(utilisateur.getRoles().stream()
                .map(r -> r.getNom())
                .collect(java.util.stream.Collectors.toSet()))
                .name();
        Map<String, Object> body = new HashMap<>();
        body.put("userId", utilisateur.getId());
        body.put("nom", utilisateur.getNom());
        body.put("prenom", utilisateur.getPrenom());
        body.put("email", utilisateur.getEmail());
        body.put("role", role);
        return ResponseEntity.ok(body);
    }

    private void storeSession(HttpSession session, AuthResponse response) {
        session.setAttribute("userId", response.getUserId());
        session.setAttribute("nom", response.getNom());
        session.setAttribute("prenom", response.getPrenom());
        session.setAttribute("email", response.getEmail());
        session.setAttribute("role", response.getRole());
    }
}

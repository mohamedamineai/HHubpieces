package com.hubpieces.controller;

import com.hubpieces.dto.AuthResponse;
import com.hubpieces.dto.LoginRequest;
import com.hubpieces.dto.RegisterRequest;
import com.hubpieces.service.AuthService;
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

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "Inscription d'un nouvel utilisateur",
               description = "Permet de creer un compte DEMANDEUR, MAKER, EXPERT_CAO ou ADMIN. " +
                       "Les champs specifiques (rayon, materiaux, etc.) dependent du role choisi.")
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
        Object userId = session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Non connecte"));
        }
        Map<String, Object> body = new HashMap<>();
        body.put("userId", userId);
        body.put("nom", session.getAttribute("nom"));
        body.put("prenom", session.getAttribute("prenom"));
        body.put("email", session.getAttribute("email"));
        body.put("role", session.getAttribute("role"));
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

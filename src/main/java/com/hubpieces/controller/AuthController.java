package com.hubpieces.controller;

import com.hubpieces.dto.AuthResponse;
import com.hubpieces.dto.LoginRequest;
import com.hubpieces.dto.RegisterRequest;
import com.hubpieces.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@io.swagger.v3.oas.annotations.tags.Tag(name = "01 — Session")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request, HttpSession session, HttpServletRequest httpRequest) {
        AuthResponse response = authService.register(request);
        if (response.isSuccess()) {
            httpRequest.changeSessionId();
            storeSession(session, response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request, HttpSession session, HttpServletRequest httpRequest) {
        AuthResponse response = authService.login(request);
        if (response.isSuccess()) {
            httpRequest.changeSessionId();
            storeSession(session, response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpSession session) {
        try {
            AuthResponse user = authService.currentUser((Long) session.getAttribute("userId"),
                    (String) session.getAttribute("role"));
            return ResponseEntity.ok(user);
        } catch (org.springframework.web.server.ResponseStatusException error) {
            session.invalidate();
            throw error;
        }
    }

    private void storeSession(HttpSession session, AuthResponse response) {
        session.setAttribute("userId", response.getUserId());
        session.setAttribute("nom", response.getNom());
        session.setAttribute("prenom", response.getPrenom());
        session.setAttribute("email", response.getEmail());
        session.setAttribute("role", response.getRole());
    }
}

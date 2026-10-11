package com.hubpieces.controller;

import com.hubpieces.dto.*;
import com.hubpieces.service.DemandeService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.net.URI;
import java.util.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/demandes")
@Tag(name = "02 — Mes demandes", description = "Compte DEMANDEUR actif, session et jeton CSRF requis pour les écritures.")
public class DemandeController {
    private final DemandeService service;
    public DemandeController(DemandeService service) { this.service = service; }
    private Long owner(HttpSession session) { return (Long) session.getAttribute("userId"); }
    @GetMapping
    @Operation(summary = "Lister uniquement mes demandes, par date décroissante")
    public Map<String, Object> list(HttpSession session, @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "12") int size) {
        var result = service.list(owner(session), page, size);
        return Map.of("content", result.getContent(), "totalElements", result.getTotalElements(),
                "totalPages", result.getTotalPages(), "page", page);
    }
    @GetMapping("/dashboard")
    @Operation(summary = "Compteurs par état et mes cinq demandes les plus récentes")
    public DashboardView dashboard(HttpSession session) { return service.dashboard(owner(session)); }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter une de mes demandes")
    public DemandeView get(@PathVariable Long id, HttpSession session) { return service.get(id, owner(session)); }
    @PostMapping
    @Operation(summary = "Créer une demande à l'état NOUVELLE")
    public ResponseEntity<DemandeView> create(@Valid @RequestBody DemandeInput input, HttpSession session) {
        var result = service.create(owner(session), input);
        return ResponseEntity.created(URI.create("/api/demandes/" + result.id())).body(result);
    }
    @PostMapping("/{id}/annulation")
    @Operation(summary = "Annuler ma nouvelle demande (idempotent)")
    public DemandeView cancel(@PathVariable Long id, HttpSession session) { return service.cancel(id, owner(session)); }
}

package com.hubpieces.service;

import com.hubpieces.exception.ForbiddenException;
import com.hubpieces.exception.UnauthorizedException;
import com.hubpieces.model.RoleNom;
import com.hubpieces.model.StatutUtilisateur;
import com.hubpieces.model.Utilisateur;
import com.hubpieces.repository.UtilisateurRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class SessionService {

    private final UtilisateurRepository utilisateurRepository;

    public SessionService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    public Utilisateur requireUser(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId == null) {
            throw new UnauthorizedException("Vous devez etre connecte");
        }
        Utilisateur utilisateur = utilisateurRepository.findById((Long) userId)
                .orElseThrow(() -> new UnauthorizedException("Session invalide"));
        if (utilisateur.getStatut() != StatutUtilisateur.ACTIF) {
            session.invalidate();
            throw new UnauthorizedException("Votre compte est suspendu ou en attente d'activation");
        }
        return utilisateur;
    }

    public Utilisateur requireRole(HttpSession session, RoleNom... roles) {
        Utilisateur u = requireUser(session);
        Set<RoleNom> userRoles = u.getRoles().stream()
                .map(r -> r.getNom())
                .collect(java.util.stream.Collectors.toSet());
        for (RoleNom r : roles) {
            if (userRoles.contains(r)) return u;
        }
        throw new ForbiddenException("Accès réservé au rôle " + java.util.Arrays.toString(roles));
    }

    public Utilisateur getOptionalUser(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId == null) return null;
        return utilisateurRepository.findById((Long) userId).orElse(null);
    }

    public boolean hasRole(Utilisateur u, RoleNom role) {
        if (u == null || u.getRoles() == null) return false;
        return u.getRoles().stream().anyMatch(r -> r.getNom() == role);
    }
}

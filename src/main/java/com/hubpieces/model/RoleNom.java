package com.hubpieces.model;

import java.util.Collection;
import java.util.List;

public enum RoleNom {
    DEMANDEUR,
    MAKER,
    EXPERT_CAO,
    ADMIN;

    /** Choix stable pour l'interface et l'authentification quand un compte a plusieurs rôles. */
    public static RoleNom principal(Collection<RoleNom> roles) {
        if (roles == null || roles.isEmpty()) return DEMANDEUR;
        return List.of(ADMIN, MAKER, EXPERT_CAO, DEMANDEUR).stream()
                .filter(roles::contains)
                .findFirst()
                .orElse(DEMANDEUR);
    }
}

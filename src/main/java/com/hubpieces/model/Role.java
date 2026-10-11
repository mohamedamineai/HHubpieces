package com.hubpieces.model;

import jakarta.persistence.*;

@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "nom", nullable = false)
    private RoleNom nom;

    public Role() {}

    public Role(RoleNom nom) {
        this.nom = nom;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public RoleNom getNom() { return nom; }
    public void setNom(RoleNom nom) { this.nom = nom; }
}

package com.hubpieces.dto;

import jakarta.validation.constraints.*;

import com.hubpieces.model.RoleNom;

public class RegisterRequest {

    @NotBlank @Size(max = 100)
    private String nom;
    @NotBlank @Size(max = 100)
    private String prenom;
    @NotBlank @Email @Size(max = 150)
    private String email;
    @NotBlank @Size(min = 8, max = 72)
    private String motDePasse;
    @Size(max = 20)
    private String telephone;
    @Size(max = 100)
    private String ville;
    @Size(max = 10)
    private String codePostal;
    @NotNull
    private RoleNom role;

    @Positive
    private Integer rayonActionKm;
    private String materiauxDisponibles;
    private String descriptionImprimante;

    private String logicielsMaitrise;
    private String portfolioUrl;
    private String descriptionExpert;

    public RegisterRequest() {}

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public String getCodePostal() { return codePostal; }
    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }

    public RoleNom getRole() { return role; }
    public void setRole(RoleNom role) { this.role = role; }

    public Integer getRayonActionKm() { return rayonActionKm; }
    public void setRayonActionKm(Integer rayonActionKm) { this.rayonActionKm = rayonActionKm; }

    public String getMateriauxDisponibles() { return materiauxDisponibles; }
    public void setMateriauxDisponibles(String materiauxDisponibles) { this.materiauxDisponibles = materiauxDisponibles; }

    public String getDescriptionImprimante() { return descriptionImprimante; }
    public void setDescriptionImprimante(String descriptionImprimante) { this.descriptionImprimante = descriptionImprimante; }

    public String getLogicielsMaitrise() { return logicielsMaitrise; }
    public void setLogicielsMaitrise(String logicielsMaitrise) { this.logicielsMaitrise = logicielsMaitrise; }

    public String getPortfolioUrl() { return portfolioUrl; }
    public void setPortfolioUrl(String portfolioUrl) { this.portfolioUrl = portfolioUrl; }

    public String getDescriptionExpert() { return descriptionExpert; }
    public void setDescriptionExpert(String descriptionExpert) { this.descriptionExpert = descriptionExpert; }
}

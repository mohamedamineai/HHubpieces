package com.hubpieces.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "profil_expert_cao")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProfilExpertCao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false, unique = true)
    private Utilisateur utilisateur;

    @Column(name = "logiciels_maitrise", length = 255)
    private String logicielsMaitrise;

    @Column(name = "portfolio_url", length = 255)
    private String portfolioUrl;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    public ProfilExpertCao() {}

    public ProfilExpertCao(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }

    public String getLogicielsMaitrise() { return logicielsMaitrise; }
    public void setLogicielsMaitrise(String logicielsMaitrise) { this.logicielsMaitrise = logicielsMaitrise; }

    public String getPortfolioUrl() { return portfolioUrl; }
    public void setPortfolioUrl(String portfolioUrl) { this.portfolioUrl = portfolioUrl; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

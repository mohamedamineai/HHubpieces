package com.hubpieces.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "offre_maker")
public class OffreMaker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "demande_id", nullable = false)
    private Demande demande;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maker_id", nullable = false)
    private Utilisateur maker;

    @Column(name = "prix", nullable = false)
    private Double prix;

    @Column(name = "delai_jours", nullable = false)
    private Integer delaiJours;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "mode_livraison")
    private ModeLivraison modeLivraison = ModeLivraison.REMISE_EN_MAIN;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat")
    private EtatOffre etat = EtatOffre.EN_ATTENTE;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation = LocalDateTime.now();

    public OffreMaker() {}

    public OffreMaker(Demande demande, Utilisateur maker, Double prix, Integer delaiJours) {
        this.demande = demande;
        this.maker = maker;
        this.prix = prix;
        this.delaiJours = delaiJours;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Demande getDemande() { return demande; }
    public void setDemande(Demande demande) { this.demande = demande; }

    public Utilisateur getMaker() { return maker; }
    public void setMaker(Utilisateur maker) { this.maker = maker; }

    public Double getPrix() { return prix; }
    public void setPrix(Double prix) { this.prix = prix; }

    public Integer getDelaiJours() { return delaiJours; }
    public void setDelaiJours(Integer delaiJours) { this.delaiJours = delaiJours; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public ModeLivraison getModeLivraison() { return modeLivraison; }
    public void setModeLivraison(ModeLivraison modeLivraison) { this.modeLivraison = modeLivraison; }

    public EtatOffre getEtat() { return etat; }
    public void setEtat(EtatOffre etat) { this.etat = etat; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
}

package com.hubpieces.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "litige")
public class Litige {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "demandeur_id", nullable = false)
    private Utilisateur demandeur;

    @Column(name = "raison", nullable = false, columnDefinition = "TEXT")
    private String raison;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat")
    private EtatLitige etat = EtatLitige.OUVERT;

    @Column(name = "decision", columnDefinition = "TEXT")
    private String decision;

    @Column(name = "date_ouverture")
    private LocalDateTime dateOuverture = LocalDateTime.now();

    @Column(name = "date_resolution")
    private LocalDateTime dateResolution;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Utilisateur admin;

    public Litige() {}

    public Litige(Transaction transaction, Utilisateur demandeur, String raison) {
        this.transaction = transaction;
        this.demandeur = demandeur;
        this.raison = raison;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Transaction getTransaction() { return transaction; }
    public void setTransaction(Transaction transaction) { this.transaction = transaction; }

    public Utilisateur getDemandeur() { return demandeur; }
    public void setDemandeur(Utilisateur demandeur) { this.demandeur = demandeur; }

    public String getRaison() { return raison; }
    public void setRaison(String raison) { this.raison = raison; }

    public EtatLitige getEtat() { return etat; }
    public void setEtat(EtatLitige etat) { this.etat = etat; }

    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }

    public LocalDateTime getDateOuverture() { return dateOuverture; }
    public void setDateOuverture(LocalDateTime dateOuverture) { this.dateOuverture = dateOuverture; }

    public LocalDateTime getDateResolution() { return dateResolution; }
    public void setDateResolution(LocalDateTime dateResolution) { this.dateResolution = dateResolution; }

    public Utilisateur getAdmin() { return admin; }
    public void setAdmin(Utilisateur admin) { this.admin = admin; }
}

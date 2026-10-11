package com.hubpieces.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offre_maker_id", nullable = false, unique = true)
    private OffreMaker offreMaker;

    @Column(name = "montant_total", nullable = false)
    private Double montantTotal;

    @Column(name = "commission_plateforme", nullable = false)
    private Double commissionPlateforme;

    @Column(name = "montant_maker", nullable = false)
    private Double montantMaker;

    @Column(name = "montant_expert_cao")
    private Double montantExpertCao = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat_paiement")
    private EtatPaiement etatPaiement = EtatPaiement.EN_ATTENTE;

    @Column(name = "date_paiement")
    private LocalDateTime datePaiement;

    @Column(name = "date_liberation")
    private LocalDateTime dateLiberaion;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation = LocalDateTime.now();

    public Transaction() {}

    public Transaction(OffreMaker offreMaker, Double montantTotal) {
        this.offreMaker = offreMaker;
        this.montantTotal = montantTotal;
        this.commissionPlateforme = montantTotal * 0.08;
        this.montantMaker = montantTotal * 0.84;
        this.montantExpertCao = montantTotal * 0.08;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public OffreMaker getOffreMaker() { return offreMaker; }
    public void setOffreMaker(OffreMaker offreMaker) { this.offreMaker = offreMaker; }

    public Double getMontantTotal() { return montantTotal; }
    public void setMontantTotal(Double montantTotal) { this.montantTotal = montantTotal; }

    public Double getCommissionPlateforme() { return commissionPlateforme; }
    public void setCommissionPlateforme(Double commissionPlateforme) { this.commissionPlateforme = commissionPlateforme; }

    public Double getMontantMaker() { return montantMaker; }
    public void setMontantMaker(Double montantMaker) { this.montantMaker = montantMaker; }

    public Double getMontantExpertCao() { return montantExpertCao; }
    public void setMontantExpertCao(Double montantExpertCao) { this.montantExpertCao = montantExpertCao; }

    public EtatPaiement getEtatPaiement() { return etatPaiement; }
    public void setEtatPaiement(EtatPaiement etatPaiement) { this.etatPaiement = etatPaiement; }

    public LocalDateTime getDatePaiement() { return datePaiement; }
    public void setDatePaiement(LocalDateTime datePaiement) { this.datePaiement = datePaiement; }

    public LocalDateTime getDateLiberaion() { return dateLiberaion; }
    public void setDateLiberaion(LocalDateTime dateLiberaion) { this.dateLiberaion = dateLiberaion; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
}

package com.hubpieces.dto;

import com.hubpieces.model.EtatPaiement;
import com.hubpieces.model.Transaction;

import java.time.LocalDateTime;

public class TransactionDTO {
    private Long id;
    private Long offreId;
    private String demandeTitre;
    private String makerNom;
    private String demandeurNom;
    private Double montantTotal;
    private Double commissionPlateforme;
    private Double montantMaker;
    private Double montantExpertCao;
    private EtatPaiement etatPaiement;
    private LocalDateTime datePaiement;
    private LocalDateTime dateLiberation;
    private LocalDateTime dateCreation;

    public TransactionDTO() {}

    public static TransactionDTO fromEntity(Transaction t) {
        TransactionDTO dto = new TransactionDTO();
        dto.id = t.getId();
        if (t.getOffreMaker() != null) {
            dto.offreId = t.getOffreMaker().getId();
            if (t.getOffreMaker().getDemande() != null) {
                dto.demandeTitre = t.getOffreMaker().getDemande().getTitre();
                if (t.getOffreMaker().getDemande().getDemandeur() != null) {
                    Utilisateur d = t.getOffreMaker().getDemande().getDemandeur();
                    dto.demandeurNom = d.getPrenom() + " " + d.getNom();
                }
            }
            if (t.getOffreMaker().getMaker() != null) {
                Utilisateur m = t.getOffreMaker().getMaker();
                dto.makerNom = m.getPrenom() + " " + m.getNom();
            }
        }
        dto.montantTotal = t.getMontantTotal();
        dto.commissionPlateforme = t.getCommissionPlateforme();
        dto.montantMaker = t.getMontantMaker();
        dto.montantExpertCao = t.getMontantExpertCao();
        dto.etatPaiement = t.getEtatPaiement();
        dto.datePaiement = t.getDatePaiement();
        dto.dateLiberation = t.getDateLiberaion();
        dto.dateCreation = t.getDateCreation();
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOffreId() { return offreId; }
    public void setOffreId(Long offreId) { this.offreId = offreId; }
    public String getDemandeTitre() { return demandeTitre; }
    public void setDemandeTitre(String demandeTitre) { this.demandeTitre = demandeTitre; }
    public String getMakerNom() { return makerNom; }
    public void setMakerNom(String makerNom) { this.makerNom = makerNom; }
    public String getDemandeurNom() { return demandeurNom; }
    public void setDemandeurNom(String demandeurNom) { this.demandeurNom = demandeurNom; }
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
    public LocalDateTime getDateLiberation() { return dateLiberation; }
    public void setDateLiberation(LocalDateTime dateLiberation) { this.dateLiberation = dateLiberation; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
}

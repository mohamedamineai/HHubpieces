package com.hubpieces.dto;

import com.hubpieces.model.EtatLitige;
import com.hubpieces.model.Litige;

import java.time.LocalDateTime;

public class LitigeDTO {
    private Long id;
    private Long transactionId;
    private String demandeTitre;
    private Long demandeurId;
    private String demandeurNom;
    private String raison;
    private EtatLitige etat;
    private String decision;
    private LocalDateTime dateOuverture;
    private LocalDateTime dateResolution;
    private Long adminId;
    private String adminNom;

    public LitigeDTO() {}

    public static LitigeDTO fromEntity(Litige l) {
        LitigeDTO dto = new LitigeDTO();
        dto.id = l.getId();
        if (l.getTransaction() != null) {
            dto.transactionId = l.getTransaction().getId();
            if (l.getTransaction().getOffreMaker() != null
                    && l.getTransaction().getOffreMaker().getDemande() != null) {
                dto.demandeTitre = l.getTransaction().getOffreMaker().getDemande().getTitre();
            }
        }
        if (l.getDemandeur() != null) {
            dto.demandeurId = l.getDemandeur().getId();
            dto.demandeurNom = l.getDemandeur().getPrenom() + " " + l.getDemandeur().getNom();
        }
        dto.raison = l.getRaison();
        dto.etat = l.getEtat();
        dto.decision = l.getDecision();
        dto.dateOuverture = l.getDateOuverture();
        dto.dateResolution = l.getDateResolution();
        if (l.getAdmin() != null) {
            dto.adminId = l.getAdmin().getId();
            dto.adminNom = l.getAdmin().getPrenom() + " " + l.getAdmin().getNom();
        }
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTransactionId() { return transactionId; }
    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }
    public String getDemandeTitre() { return demandeTitre; }
    public void setDemandeTitre(String demandeTitre) { this.demandeTitre = demandeTitre; }
    public Long getDemandeurId() { return demandeurId; }
    public void setDemandeurId(Long demandeurId) { this.demandeurId = demandeurId; }
    public String getDemandeurNom() { return demandeurNom; }
    public void setDemandeurNom(String demandeurNom) { this.demandeurNom = demandeurNom; }
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
    public Long getAdminId() { return adminId; }
    public void setAdminId(Long adminId) { this.adminId = adminId; }
    public String getAdminNom() { return adminNom; }
    public void setAdminNom(String adminNom) { this.adminNom = adminNom; }
}

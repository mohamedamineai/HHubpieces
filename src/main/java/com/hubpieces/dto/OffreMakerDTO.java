package com.hubpieces.dto;

import com.hubpieces.model.EtatOffre;
import com.hubpieces.model.ModeLivraison;
import com.hubpieces.model.OffreMaker;

import java.time.LocalDateTime;

public class OffreMakerDTO {
    private Long id;
    private Long demandeId;
    private String demandeTitre;
    private Long makerId;
    private String makerNom;
    private String makerPrenom;
    private Double prix;
    private Integer delaiJours;
    private String message;
    private ModeLivraison modeLivraison;
    private EtatOffre etat;
    private LocalDateTime dateCreation;

    public OffreMakerDTO() {}

    public static OffreMakerDTO fromEntity(OffreMaker o) {
        OffreMakerDTO dto = new OffreMakerDTO();
        dto.id = o.getId();
        if (o.getDemande() != null) {
            dto.demandeId = o.getDemande().getId();
            dto.demandeTitre = o.getDemande().getTitre();
        }
        if (o.getMaker() != null) {
            dto.makerId = o.getMaker().getId();
            dto.makerNom = o.getMaker().getNom();
            dto.makerPrenom = o.getMaker().getPrenom();
        }
        dto.prix = o.getPrix();
        dto.delaiJours = o.getDelaiJours();
        dto.message = o.getMessage();
        dto.modeLivraison = o.getModeLivraison();
        dto.etat = o.getEtat();
        dto.dateCreation = o.getDateCreation();
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getDemandeId() { return demandeId; }
    public void setDemandeId(Long demandeId) { this.demandeId = demandeId; }
    public String getDemandeTitre() { return demandeTitre; }
    public void setDemandeTitre(String demandeTitre) { this.demandeTitre = demandeTitre; }
    public Long getMakerId() { return makerId; }
    public void setMakerId(Long makerId) { this.makerId = makerId; }
    public String getMakerNom() { return makerNom; }
    public void setMakerNom(String makerNom) { this.makerNom = makerNom; }
    public String getMakerPrenom() { return makerPrenom; }
    public void setMakerPrenom(String makerPrenom) { this.makerPrenom = makerPrenom; }
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

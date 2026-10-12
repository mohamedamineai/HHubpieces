package com.hubpieces.dto;

import com.hubpieces.model.ModeLivraison;

public class OffreCreateRequest {
    private Long demandeId;
    private Double prix;
    private Integer delaiJours;
    private String message;
    private ModeLivraison modeLivraison;

    public OffreCreateRequest() {}

    public Long getDemandeId() { return demandeId; }
    public void setDemandeId(Long demandeId) { this.demandeId = demandeId; }
    public Double getPrix() { return prix; }
    public void setPrix(Double prix) { this.prix = prix; }
    public Integer getDelaiJours() { return delaiJours; }
    public void setDelaiJours(Integer delaiJours) { this.delaiJours = delaiJours; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public ModeLivraison getModeLivraison() { return modeLivraison; }
    public void setModeLivraison(ModeLivraison modeLivraison) { this.modeLivraison = modeLivraison; }
}

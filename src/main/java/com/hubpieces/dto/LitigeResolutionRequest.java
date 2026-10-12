package com.hubpieces.dto;

import com.hubpieces.model.EtatLitige;

public class LitigeResolutionRequest {
    private EtatLitige etat;
    private String decision;

    public LitigeResolutionRequest() {}

    public EtatLitige getEtat() { return etat; }
    public void setEtat(EtatLitige etat) { this.etat = etat; }
    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }
}

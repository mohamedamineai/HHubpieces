package com.hubpieces.dto;

public class ProfilExpertCaoUpdateRequest {
    private String logicielsMaitrise;
    private String portfolioUrl;
    private String description;

    public ProfilExpertCaoUpdateRequest() {}

    public String getLogicielsMaitrise() { return logicielsMaitrise; }
    public void setLogicielsMaitrise(String logicielsMaitrise) { this.logicielsMaitrise = logicielsMaitrise; }
    public String getPortfolioUrl() { return portfolioUrl; }
    public void setPortfolioUrl(String portfolioUrl) { this.portfolioUrl = portfolioUrl; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

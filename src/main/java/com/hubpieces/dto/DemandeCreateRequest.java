package com.hubpieces.dto;

public class DemandeCreateRequest {
    private String titre;
    private String description;
    private String marque;
    private String modeleAppareil;
    private String materiauSouhaite;
    private Double budgetMax;
    private String ville;
    private String codePostal;
    private Double latitude;
    private Double longitude;
    private Long fichierStlId;

    public DemandeCreateRequest() {}

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getMarque() { return marque; }
    public void setMarque(String marque) { this.marque = marque; }
    public String getModeleAppareil() { return modeleAppareil; }
    public void setModeleAppareil(String modeleAppareil) { this.modeleAppareil = modeleAppareil; }
    public String getMateriauSouhaite() { return materiauSouhaite; }
    public void setMateriauSouhaite(String materiauSouhaite) { this.materiauSouhaite = materiauSouhaite; }
    public Double getBudgetMax() { return budgetMax; }
    public void setBudgetMax(Double budgetMax) { this.budgetMax = budgetMax; }
    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }
    public String getCodePostal() { return codePostal; }
    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public Long getFichierStlId() { return fichierStlId; }
    public void setFichierStlId(Long fichierStlId) { this.fichierStlId = fichierStlId; }
}

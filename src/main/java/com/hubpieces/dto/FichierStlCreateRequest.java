package com.hubpieces.dto;

public class FichierStlCreateRequest {
    private String nom;
    private String description;
    private String cheminFichier;
    private Long tailleFichier;
    private String categorie;
    private String tags;
    private String marque;
    private String modeleAppareil;
    private String materiauRecommande;
    private String dimensions;

    public FichierStlCreateRequest() {}

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCheminFichier() { return cheminFichier; }
    public void setCheminFichier(String cheminFichier) { this.cheminFichier = cheminFichier; }
    public Long getTailleFichier() { return tailleFichier; }
    public void setTailleFichier(Long tailleFichier) { this.tailleFichier = tailleFichier; }
    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public String getMarque() { return marque; }
    public void setMarque(String marque) { this.marque = marque; }
    public String getModeleAppareil() { return modeleAppareil; }
    public void setModeleAppareil(String modeleAppareil) { this.modeleAppareil = modeleAppareil; }
    public String getMateriauRecommande() { return materiauRecommande; }
    public void setMateriauRecommande(String materiauRecommande) { this.materiauRecommande = materiauRecommande; }
    public String getDimensions() { return dimensions; }
    public void setDimensions(String dimensions) { this.dimensions = dimensions; }
}

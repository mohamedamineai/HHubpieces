package com.hubpieces.dto;

import com.hubpieces.model.EtatValidation;
import com.hubpieces.model.FichierStl;

import java.time.LocalDateTime;

public class FichierStlDTO {
    private Long id;
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
    private Double noteMoyenne;
    private Integer nombreTelechargements;
    private EtatValidation etatValidation;
    private LocalDateTime dateUpload;
    private Long uploadeurId;
    private String uploadeurNom;
    private Long valideurId;

    public FichierStlDTO() {}

    public static FichierStlDTO fromEntity(FichierStl f) {
        FichierStlDTO dto = new FichierStlDTO();
        dto.id = f.getId();
        dto.nom = f.getNom();
        dto.description = f.getDescription();
        dto.cheminFichier = f.getCheminFichier();
        dto.tailleFichier = f.getTailleFichier();
        dto.categorie = f.getCategorie();
        dto.tags = f.getTags();
        dto.marque = f.getMarque();
        dto.modeleAppareil = f.getModeleAppareil();
        dto.materiauRecommande = f.getMateriauRecommande();
        dto.dimensions = f.getDimensions();
        dto.noteMoyenne = f.getNoteMoyenne();
        dto.nombreTelechargements = f.getNombreTelechargements();
        dto.etatValidation = f.getEtatValidation();
        dto.dateUpload = f.getDateUpload();
        if (f.getUploadeur() != null) {
            dto.uploadeurId = f.getUploadeur().getId();
            dto.uploadeurNom = f.getUploadeur().getPrenom() + " " + f.getUploadeur().getNom();
        }
        if (f.getValideur() != null) {
            dto.valideurId = f.getValideur().getId();
        }
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public Double getNoteMoyenne() { return noteMoyenne; }
    public void setNoteMoyenne(Double noteMoyenne) { this.noteMoyenne = noteMoyenne; }
    public Integer getNombreTelechargements() { return nombreTelechargements; }
    public void setNombreTelechargements(Integer nombreTelechargements) { this.nombreTelechargements = nombreTelechargements; }
    public EtatValidation getEtatValidation() { return etatValidation; }
    public void setEtatValidation(EtatValidation etatValidation) { this.etatValidation = etatValidation; }
    public LocalDateTime getDateUpload() { return dateUpload; }
    public void setDateUpload(LocalDateTime dateUpload) { this.dateUpload = dateUpload; }
    public Long getUploadeurId() { return uploadeurId; }
    public void setUploadeurId(Long uploadeurId) { this.uploadeurId = uploadeurId; }
    public String getUploadeurNom() { return uploadeurNom; }
    public void setUploadeurNom(String uploadeurNom) { this.uploadeurNom = uploadeurNom; }
    public Long getValideurId() { return valideurId; }
    public void setValideurId(Long valideurId) { this.valideurId = valideurId; }
}

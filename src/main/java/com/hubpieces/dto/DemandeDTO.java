package com.hubpieces.dto;

import com.hubpieces.model.Demande;
import com.hubpieces.model.EtatDemande;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class DemandeDTO {
    private Long id;
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
    private EtatDemande etat;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long demandeurId;
    private String demandeurNom;
    private String demandeurPrenom;
    private Long fichierStlId;
    private String fichierStlNom;
    private List<String> photos;
    private Integer nombreOffres;

    public DemandeDTO() {}

    public static DemandeDTO fromEntity(Demande d) {
        return fromEntity(d, true);
    }

    public static DemandeDTO fromEntity(Demande d, boolean withDetails) {
        DemandeDTO dto = new DemandeDTO();
        dto.id = d.getId();
        dto.titre = d.getTitre();
        dto.description = d.getDescription();
        dto.marque = d.getMarque();
        dto.modeleAppareil = d.getModeleAppareil();
        dto.materiauSouhaite = d.getMateriauSouhaite();
        dto.budgetMax = d.getBudgetMax();
        dto.ville = d.getVille();
        dto.codePostal = d.getCodePostal();
        dto.latitude = d.getLatitude();
        dto.longitude = d.getLongitude();
        dto.etat = d.getEtat();
        dto.dateCreation = d.getDateCreation();
        dto.dateModification = d.getDateModification();
        if (d.getDemandeur() != null) {
            dto.demandeurId = d.getDemandeur().getId();
            dto.demandeurNom = d.getDemandeur().getNom();
            dto.demandeurPrenom = d.getDemandeur().getPrenom();
        }
        if (d.getFichierStl() != null) {
            dto.fichierStlId = d.getFichierStl().getId();
            dto.fichierStlNom = d.getFichierStl().getNom();
        }
        if (withDetails) {
            if (d.getPhotos() != null) {
                dto.photos = d.getPhotos().stream()
                        .map(p -> p.getCheminPhoto())
                        .collect(Collectors.toList());
            }
            dto.nombreOffres = d.getOffres() != null ? d.getOffres().size() : 0;
        }
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public EtatDemande getEtat() { return etat; }
    public void setEtat(EtatDemande etat) { this.etat = etat; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public void setDateModification(LocalDateTime dateModification) { this.dateModification = dateModification; }
    public Long getDemandeurId() { return demandeurId; }
    public void setDemandeurId(Long demandeurId) { this.demandeurId = demandeurId; }
    public String getDemandeurNom() { return demandeurNom; }
    public void setDemandeurNom(String demandeurNom) { this.demandeurNom = demandeurNom; }
    public String getDemandeurPrenom() { return demandeurPrenom; }
    public void setDemandeurPrenom(String demandeurPrenom) { this.demandeurPrenom = demandeurPrenom; }
    public Long getFichierStlId() { return fichierStlId; }
    public void setFichierStlId(Long fichierStlId) { this.fichierStlId = fichierStlId; }
    public String getFichierStlNom() { return fichierStlNom; }
    public void setFichierStlNom(String fichierStlNom) { this.fichierStlNom = fichierStlNom; }
    public List<String> getPhotos() { return photos; }
    public void setPhotos(List<String> photos) { this.photos = photos; }
    public Integer getNombreOffres() { return nombreOffres; }
    public void setNombreOffres(Integer nombreOffres) { this.nombreOffres = nombreOffres; }
}

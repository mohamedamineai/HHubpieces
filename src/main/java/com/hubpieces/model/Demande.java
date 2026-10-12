package com.hubpieces.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "demande")
public class Demande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titre", nullable = false, length = 200)
    private String titre;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "marque", length = 100)
    private String marque;

    @Column(name = "modele_appareil", length = 100)
    private String modeleAppareil;

    @Column(name = "materiau_souhaite", length = 50)
    private String materiauSouhaite;

    @Column(name = "budget_max")
    private Double budgetMax;

    @Column(name = "ville", length = 100)
    private String ville;

    @Column(name = "code_postal", length = 10)
    private String codePostal;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat")
    private EtatDemande etat = EtatDemande.NOUVELLE;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation = LocalDateTime.now();

    @Column(name = "date_modification")
    private LocalDateTime dateModification = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "demandeur_id", nullable = false)
    private Utilisateur demandeur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fichier_stl_id")
    private FichierStl fichierStl;

    @OneToMany(mappedBy = "demande", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private java.util.List<PhotoDemande> photos = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "demande", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private java.util.List<OffreMaker> offres = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "demande", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private java.util.List<Message> messages = new java.util.ArrayList<>();

    public Demande() {}

    public Demande(String titre, String description, Utilisateur demandeur) {
        this.titre = titre;
        this.description = description;
        this.demandeur = demandeur;
    }

    @PreUpdate
    public void preUpdate() {
        this.dateModification = LocalDateTime.now();
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

    public Utilisateur getDemandeur() { return demandeur; }
    public void setDemandeur(Utilisateur demandeur) { this.demandeur = demandeur; }

    public FichierStl getFichierStl() { return fichierStl; }
    public void setFichierStl(FichierStl fichierStl) { this.fichierStl = fichierStl; }

    public java.util.List<PhotoDemande> getPhotos() { return photos; }
    public void setPhotos(java.util.List<PhotoDemande> photos) { this.photos = photos; }

    public java.util.List<OffreMaker> getOffres() { return offres; }
    public void setOffres(java.util.List<OffreMaker> offres) { this.offres = offres; }

    public java.util.List<Message> getMessages() { return messages; }
    public void setMessages(java.util.List<Message> messages) { this.messages = messages; }
}

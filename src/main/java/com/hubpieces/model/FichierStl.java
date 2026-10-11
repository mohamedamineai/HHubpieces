package com.hubpieces.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "fichier_stl")
public class FichierStl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", nullable = false, length = 200)
    private String nom;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "chemin_fichier", nullable = false, length = 255)
    private String cheminFichier;

    @Column(name = "taille_fichier")
    private Long tailleFichier;

    @Column(name = "categorie", length = 100)
    private String categorie;

    @Column(name = "tags", length = 255)
    private String tags;

    @Column(name = "marque", length = 100)
    private String marque;

    @Column(name = "modele_appareil", length = 100)
    private String modeleAppareil;

    @Column(name = "materiau_recommande", length = 50)
    private String materiauRecommande;

    @Column(name = "dimensions", length = 100)
    private String dimensions;

    @Column(name = "note_moyenne")
    private Double noteMoyenne = 0.0;

    @Column(name = "nombre_telechargements")
    private Integer nombreTelechargements = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat_validation")
    private EtatValidation etatValidation = EtatValidation.EN_ATTENTE;

    @Column(name = "date_upload")
    private LocalDateTime dateUpload = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploadeur_id", nullable = false)
    private Utilisateur uploadeur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "valideur_id")
    private Utilisateur valideur;

    public FichierStl() {}

    public FichierStl(String nom, String cheminFichier, Utilisateur uploadeur) {
        this.nom = nom;
        this.cheminFichier = cheminFichier;
        this.uploadeur = uploadeur;
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

    public Utilisateur getUploadeur() { return uploadeur; }
    public void setUploadeur(Utilisateur uploadeur) { this.uploadeur = uploadeur; }

    public Utilisateur getValideur() { return valideur; }
    public void setValideur(Utilisateur valideur) { this.valideur = valideur; }
}

package com.hubpieces.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "photo_demande")
public class PhotoDemande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "demande_id", nullable = false)
    private Demande demande;

    @Column(name = "chemin_photo", nullable = false, length = 255)
    private String cheminPhoto;

    @Column(name = "date_upload")
    private LocalDateTime dateUpload = LocalDateTime.now();

    public PhotoDemande() {}

    public PhotoDemande(Demande demande, String cheminPhoto) {
        this.demande = demande;
        this.cheminPhoto = cheminPhoto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Demande getDemande() { return demande; }
    public void setDemande(Demande demande) { this.demande = demande; }

    public String getCheminPhoto() { return cheminPhoto; }
    public void setCheminPhoto(String cheminPhoto) { this.cheminPhoto = cheminPhoto; }

    public LocalDateTime getDateUpload() { return dateUpload; }
    public void setDateUpload(LocalDateTime dateUpload) { this.dateUpload = dateUpload; }
}

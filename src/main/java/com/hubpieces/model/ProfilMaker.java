package com.hubpieces.model;

import jakarta.persistence.*;

@Entity
@Table(name = "profil_maker")
public class ProfilMaker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false, unique = true)
    private Utilisateur utilisateur;

    @Column(name = "rayon_action_km")
    private Integer rayonActionKm = 20;

    @Column(name = "materiaux_disponibles", length = 255)
    private String materiauxDisponibles;

    @Column(name = "description_imprimante", columnDefinition = "TEXT")
    private String descriptionImprimante;

    @Column(name = "nombre_commandes_actives")
    private Integer nombreCommandesActives = 0;

    @Column(name = "nombre_avis_positifs")
    private Integer nombreAvisPositifs = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "badge")
    private BadgeMaker badge = BadgeMaker.DEBUTANT;

    public ProfilMaker() {}

    public ProfilMaker(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }

    public Integer getRayonActionKm() { return rayonActionKm; }
    public void setRayonActionKm(Integer rayonActionKm) { this.rayonActionKm = rayonActionKm; }

    public String getMateriauxDisponibles() { return materiauxDisponibles; }
    public void setMateriauxDisponibles(String materiauxDisponibles) { this.materiauxDisponibles = materiauxDisponibles; }

    public String getDescriptionImprimante() { return descriptionImprimante; }
    public void setDescriptionImprimante(String descriptionImprimante) { this.descriptionImprimante = descriptionImprimante; }

    public Integer getNombreCommandesActives() { return nombreCommandesActives; }
    public void setNombreCommandesActives(Integer nombreCommandesActives) { this.nombreCommandesActives = nombreCommandesActives; }

    public Integer getNombreAvisPositifs() { return nombreAvisPositifs; }
    public void setNombreAvisPositifs(Integer nombreAvisPositifs) { this.nombreAvisPositifs = nombreAvisPositifs; }

    public BadgeMaker getBadge() { return badge; }
    public void setBadge(BadgeMaker badge) { this.badge = badge; }
}

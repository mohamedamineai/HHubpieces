package com.hubpieces.dto;

public class ProfilMakerUpdateRequest {
    private Integer rayonActionKm;
    private String materiauxDisponibles;
    private String descriptionImprimante;

    public ProfilMakerUpdateRequest() {}

    public Integer getRayonActionKm() { return rayonActionKm; }
    public void setRayonActionKm(Integer rayonActionKm) { this.rayonActionKm = rayonActionKm; }
    public String getMateriauxDisponibles() { return materiauxDisponibles; }
    public void setMateriauxDisponibles(String materiauxDisponibles) { this.materiauxDisponibles = materiauxDisponibles; }
    public String getDescriptionImprimante() { return descriptionImprimante; }
    public void setDescriptionImprimante(String descriptionImprimante) { this.descriptionImprimante = descriptionImprimante; }
}

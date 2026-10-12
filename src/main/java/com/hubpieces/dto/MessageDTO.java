package com.hubpieces.dto;

import com.hubpieces.model.Message;

import java.time.LocalDateTime;

public class MessageDTO {
    private Long id;
    private Long demandeId;
    private String demandeTitre;
    private Long expediteurId;
    private String expediteurNom;
    private Long destinataireId;
    private String destinataireNom;
    private String contenu;
    private Boolean lu;
    private LocalDateTime dateEnvoi;

    public MessageDTO() {}

    public static MessageDTO fromEntity(Message m) {
        MessageDTO dto = new MessageDTO();
        dto.id = m.getId();
        if (m.getDemande() != null) {
            dto.demandeId = m.getDemande().getId();
            dto.demandeTitre = m.getDemande().getTitre();
        }
        if (m.getExpediteur() != null) {
            dto.expediteurId = m.getExpediteur().getId();
            dto.expediteurNom = m.getExpediteur().getPrenom() + " " + m.getExpediteur().getNom();
        }
        if (m.getDestinataire() != null) {
            dto.destinataireId = m.getDestinataire().getId();
            dto.destinataireNom = m.getDestinataire().getPrenom() + " " + m.getDestinataire().getNom();
        }
        dto.contenu = m.getContenu();
        dto.lu = m.getLu();
        dto.dateEnvoi = m.getDateEnvoi();
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getDemandeId() { return demandeId; }
    public void setDemandeId(Long demandeId) { this.demandeId = demandeId; }
    public String getDemandeTitre() { return demandeTitre; }
    public void setDemandeTitre(String demandeTitre) { this.demandeTitre = demandeTitre; }
    public Long getExpediteurId() { return expediteurId; }
    public void setExpediteurId(Long expediteurId) { this.expediteurId = expediteurId; }
    public String getExpediteurNom() { return expediteurNom; }
    public void setExpediteurNom(String expediteurNom) { this.expediteurNom = expediteurNom; }
    public Long getDestinataireId() { return destinataireId; }
    public void setDestinataireId(Long destinataireId) { this.destinataireId = destinataireId; }
    public String getDestinataireNom() { return destinataireNom; }
    public void setDestinataireNom(String destinataireNom) { this.destinataireNom = destinataireNom; }
    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }
    public Boolean getLu() { return lu; }
    public void setLu(Boolean lu) { this.lu = lu; }
    public LocalDateTime getDateEnvoi() { return dateEnvoi; }
    public void setDateEnvoi(LocalDateTime dateEnvoi) { this.dateEnvoi = dateEnvoi; }
}

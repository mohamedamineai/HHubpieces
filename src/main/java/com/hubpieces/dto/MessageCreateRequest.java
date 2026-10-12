package com.hubpieces.dto;

public class MessageCreateRequest {
    private Long demandeId;
    private Long destinataireId;
    private String contenu;

    public MessageCreateRequest() {}

    public Long getDemandeId() { return demandeId; }
    public void setDemandeId(Long demandeId) { this.demandeId = demandeId; }
    public Long getDestinataireId() { return destinataireId; }
    public void setDestinataireId(Long destinataireId) { this.destinataireId = destinataireId; }
    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }
}

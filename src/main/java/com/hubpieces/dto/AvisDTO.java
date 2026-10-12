package com.hubpieces.dto;

import com.hubpieces.model.Avis;

import java.time.LocalDateTime;

public class AvisDTO {
    private Long id;
    private Long transactionId;
    private Long auteurId;
    private String auteurNom;
    private Long destinataireId;
    private String destinataireNom;
    private Integer note;
    private String commentaire;
    private Boolean publie;
    private LocalDateTime dateCreation;
    private LocalDateTime datePublication;

    public AvisDTO() {}

    public static AvisDTO fromEntity(Avis a) {
        AvisDTO dto = new AvisDTO();
        dto.id = a.getId();
        if (a.getTransaction() != null) dto.transactionId = a.getTransaction().getId();
        if (a.getAuteur() != null) {
            dto.auteurId = a.getAuteur().getId();
            dto.auteurNom = a.getAuteur().getPrenom() + " " + a.getAuteur().getNom();
        }
        if (a.getDestinataire() != null) {
            dto.destinataireId = a.getDestinataire().getId();
            dto.destinataireNom = a.getDestinataire().getPrenom() + " " + a.getDestinataire().getNom();
        }
        dto.note = a.getNote();
        dto.commentaire = a.getCommentaire();
        dto.publie = a.getPublie();
        dto.dateCreation = a.getDateCreation();
        dto.datePublication = a.getDatePublication();
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTransactionId() { return transactionId; }
    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }
    public Long getAuteurId() { return auteurId; }
    public void setAuteurId(Long auteurId) { this.auteurId = auteurId; }
    public String getAuteurNom() { return auteurNom; }
    public void setAuteurNom(String auteurNom) { this.auteurNom = auteurNom; }
    public Long getDestinataireId() { return destinataireId; }
    public void setDestinataireId(Long destinataireId) { this.destinataireId = destinataireId; }
    public String getDestinataireNom() { return destinataireNom; }
    public void setDestinataireNom(String destinataireNom) { this.destinataireNom = destinataireNom; }
    public Integer getNote() { return note; }
    public void setNote(Integer note) { this.note = note; }
    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
    public Boolean getPublie() { return publie; }
    public void setPublie(Boolean publie) { this.publie = publie; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    public LocalDateTime getDatePublication() { return datePublication; }
    public void setDatePublication(LocalDateTime datePublication) { this.datePublication = datePublication; }
}

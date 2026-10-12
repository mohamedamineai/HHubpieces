package com.hubpieces.dto;

import com.hubpieces.model.Notification;
import com.hubpieces.model.TypeNotification;

import java.time.LocalDateTime;

public class NotificationDTO {
    private Long id;
    private String titre;
    private String message;
    private TypeNotification type;
    private Boolean lu;
    private LocalDateTime dateCreation;

    public NotificationDTO() {}

    public static NotificationDTO fromEntity(Notification n) {
        NotificationDTO dto = new NotificationDTO();
        dto.id = n.getId();
        dto.titre = n.getTitre();
        dto.message = n.getMessage();
        dto.type = n.getType();
        dto.lu = n.getLu();
        dto.dateCreation = n.getDateCreation();
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public TypeNotification getType() { return type; }
    public void setType(TypeNotification type) { this.type = type; }
    public Boolean getLu() { return lu; }
    public void setLu(Boolean lu) { this.lu = lu; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
}

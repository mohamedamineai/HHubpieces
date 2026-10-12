package com.hubpieces.service;

import com.hubpieces.dto.NotificationDTO;
import com.hubpieces.model.Notification;
import com.hubpieces.model.TypeNotification;
import com.hubpieces.model.Utilisateur;
import com.hubpieces.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public Notification envoyer(Utilisateur destinataire, String titre, String message, TypeNotification type) {
        Notification notif = new Notification(destinataire, titre, message, type);
        return notificationRepository.save(notif);
    }

    public List<NotificationDTO> findByUtilisateur(Utilisateur u) {
        return notificationRepository.findByUtilisateurOrderByDateCreationDesc(u).stream()
                .map(NotificationDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public long countNonLues(Utilisateur u) {
        return notificationRepository.countByUtilisateurAndLuFalse(u);
    }

    @Transactional
    public void marquerCommeLue(Long id, Utilisateur u) {
        Notification n = notificationRepository.findById(id)
                .orElseThrow(() -> new com.hubpieces.exception.ResourceNotFoundException("Notification introuvable"));
        if (!n.getUtilisateur().getId().equals(u.getId())) {
            throw new com.hubpieces.exception.ForbiddenException("Cette notification n'est pas la votre");
        }
        n.setLu(true);
        notificationRepository.save(n);
    }

    @Transactional
    public void marquerToutesCommeLues(Utilisateur u) {
        List<Notification> notifs = notificationRepository.findByUtilisateurAndLuFalseOrderByDateCreationDesc(u);
        for (Notification n : notifs) {
            n.setLu(true);
        }
        notificationRepository.saveAll(notifs);
    }
}

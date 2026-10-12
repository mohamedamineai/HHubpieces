package com.hubpieces.repository;

import com.hubpieces.model.Notification;
import com.hubpieces.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUtilisateurOrderByDateCreationDesc(Utilisateur utilisateur);

    List<Notification> findByUtilisateurAndLuFalseOrderByDateCreationDesc(Utilisateur utilisateur);

    long countByUtilisateurAndLuFalse(Utilisateur utilisateur);
}

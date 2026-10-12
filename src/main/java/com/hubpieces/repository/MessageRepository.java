package com.hubpieces.repository;

import com.hubpieces.model.Demande;
import com.hubpieces.model.Message;
import com.hubpieces.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByDemandeOrderByDateEnvoiAsc(Demande demande);

    List<Message> findByDestinataireAndLuFalseOrderByDateEnvoiDesc(Utilisateur destinataire);

    long countByDestinataireAndLuFalse(Utilisateur destinataire);
}

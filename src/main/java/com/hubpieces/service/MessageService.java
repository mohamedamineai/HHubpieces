package com.hubpieces.service;

import com.hubpieces.dto.MessageCreateRequest;
import com.hubpieces.exception.BadRequestException;
import com.hubpieces.exception.ResourceNotFoundException;
import com.hubpieces.model.Demande;
import com.hubpieces.model.Message;
import com.hubpieces.model.RoleNom;
import com.hubpieces.model.Utilisateur;
import com.hubpieces.repository.DemandeRepository;
import com.hubpieces.repository.MessageRepository;
import com.hubpieces.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final DemandeRepository demandeRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final NotificationService notificationService;

    public MessageService(MessageRepository messageRepository,
                          DemandeRepository demandeRepository,
                          UtilisateurRepository utilisateurRepository,
                          NotificationService notificationService) {
        this.messageRepository = messageRepository;
        this.demandeRepository = demandeRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.notificationService = notificationService;
    }

    public List<Message> findConversations(Utilisateur u) {
        // Tous les messages ou l'utilisateur est expediteur ou destinataire
        return messageRepository.findAll().stream()
                .filter(m -> m.getExpediteur().getId().equals(u.getId())
                        || m.getDestinataire().getId().equals(u.getId()))
                .sorted((a, b) -> b.getDateEnvoi().compareTo(a.getDateEnvoi()))
                .collect(Collectors.toList());
    }

    public List<Message> findByDemande(Long demandeId) {
        Demande demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new ResourceNotFoundException("Demande introuvable"));
        return messageRepository.findByDemandeOrderByDateEnvoiAsc(demande);
    }

    @Transactional
    public Message envoyer(MessageCreateRequest req, Utilisateur expediteur) {
        if (req.getContenu() == null || req.getContenu().isBlank()) {
            throw new BadRequestException("Le contenu est obligatoire");
        }
        Demande demande = demandeRepository.findById(req.getDemandeId())
                .orElseThrow(() -> new ResourceNotFoundException("Demande introuvable"));
        Utilisateur destinataire = utilisateurRepository.findById(req.getDestinataireId())
                .orElseThrow(() -> new ResourceNotFoundException("Destinataire introuvable"));

        Message message = new Message(demande, expediteur, destinataire, req.getContenu());
        Message saved = messageRepository.save(message);

        notificationService.envoyer(destinataire,
                "Nouveau message",
                expediteur.getPrenom() + " " + expediteur.getNom() + " vous a envoye un message",
                TypeNotification.INFO);
        return saved;
    }

    @Transactional
    public void marquerCommeLu(Long id, Utilisateur destinataire) {
        Message m = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message introuvable"));
        if (!m.getDestinataire().getId().equals(destinataire.getId())) {
            throw new BadRequestException("Ce message ne vous est pas destine");
        }
        m.setLu(true);
        messageRepository.save(m);
    }
}

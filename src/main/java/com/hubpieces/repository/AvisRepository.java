package com.hubpieces.repository;

import com.hubpieces.model.Avis;
import com.hubpieces.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvisRepository extends JpaRepository<Avis, Long> {

    List<Avis> findByDestinataireAndPublieTrueOrderByDatePublicationDesc(Utilisateur destinataire);

    List<Avis> findByAuteurOrderByDateCreationDesc(Utilisateur auteur);

    long countByDestinataireAndPublieTrue(Utilisateur destinataire);

    Double findAverageNoteByDestinataire(Utilisateur destinataire);
}

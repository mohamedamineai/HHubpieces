package com.hubpieces.repository;

import com.hubpieces.model.Avis;
import com.hubpieces.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AvisRepository extends JpaRepository<Avis, Long> {

    List<Avis> findByDestinataireAndPublieTrueOrderByDatePublicationDesc(Utilisateur destinataire);

    List<Avis> findByAuteurOrderByDateCreationDesc(Utilisateur auteur);

    long countByDestinataireAndPublieTrue(Utilisateur destinataire);

    @Query("SELECT AVG(a.note) FROM Avis a WHERE a.destinataire = :destinataire AND a.publie = true")
    Double findAverageNoteByDestinataire(@Param("destinataire") Utilisateur destinataire);
}

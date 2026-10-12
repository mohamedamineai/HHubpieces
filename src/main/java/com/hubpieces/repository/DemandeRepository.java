package com.hubpieces.repository;

import com.hubpieces.model.Demande;
import com.hubpieces.model.EtatDemande;
import com.hubpieces.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DemandeRepository extends JpaRepository<Demande, Long> {

    List<Demande> findByDemandeurOrderByDateCreationDesc(Utilisateur demandeur);

    List<Demande> findByEtatOrderByDateCreationDesc(EtatDemande etat);

    @Query("SELECT d FROM Demande d WHERE d.etat IN :etats ORDER BY d.dateCreation DESC")
    List<Demande> findByEtatIn(@Param("etats") List<EtatDemande> etats);

    @Query("SELECT COUNT(d) FROM Demande d WHERE d.demandeur = :demandeur AND d.etat = :etat")
    long countByDemandeurAndEtat(@Param("demandeur") Utilisateur demandeur, @Param("etat") EtatDemande etat);

    @Query("SELECT COUNT(d) FROM Demande d WHERE d.demandeur = :demandeur")
    long countByDemandeur(@Param("demandeur") Utilisateur demandeur);

    @Query("SELECT COUNT(d) FROM Demande d WHERE d.etat = :etat")
    long countByEtat(@Param("etat") EtatDemande etat);

    long countByDateCreationAfter(java.time.LocalDateTime date);
}

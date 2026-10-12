package com.hubpieces.repository;

import com.hubpieces.model.Demande;
import com.hubpieces.model.EtatOffre;
import com.hubpieces.model.OffreMaker;
import com.hubpieces.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OffreMakerRepository extends JpaRepository<OffreMaker, Long> {

    List<OffreMaker> findByDemandeOrderByPrixAsc(Demande demande);

    List<OffreMaker> findByMakerOrderByDateCreationDesc(Utilisateur maker);

    List<OffreMaker> findByEtat(EtatOffre etat);

    Optional<OffreMaker> findByDemandeAndMaker(Demande demande, Utilisateur maker);

    @Query("SELECT COUNT(o) FROM OffreMaker o WHERE o.maker = :maker AND o.etat = :etat")
    long countByMakerAndEtat(@Param("maker") Utilisateur maker, @Param("etat") EtatOffre etat);

    @Query("SELECT COUNT(o) FROM OffreMaker o WHERE o.maker = :maker")
    long countByMaker(@Param("maker") Utilisateur maker);

    @Query("SELECT COUNT(o) FROM OffreMaker o WHERE o.demande = :demande AND o.etat = :etat")
    long countByDemandeAndEtat(@Param("demande") Demande demande, @Param("etat") EtatOffre etat);
}

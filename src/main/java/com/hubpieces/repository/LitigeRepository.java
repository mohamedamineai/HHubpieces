package com.hubpieces.repository;

import com.hubpieces.model.EtatLitige;
import com.hubpieces.model.Litige;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LitigeRepository extends JpaRepository<Litige, Long> {

    List<Litige> findByEtatOrderByDateOuvertureDesc(EtatLitige etat);

    @Query("SELECT l FROM Litige l ORDER BY l.dateOuverture DESC")
    List<Litige> findByOrderByDateOuvertureDesc();

    long countByEtat(EtatLitige etat);
}

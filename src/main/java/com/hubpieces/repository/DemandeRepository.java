package com.hubpieces.repository;

import com.hubpieces.model.Demande;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemandeRepository extends JpaRepository<Demande, Long> {
    interface EtatCount {
        com.hubpieces.model.EtatDemande getEtat();
        long getNombre();
    }
    @org.springframework.data.jpa.repository.Query("select d.etat as etat, count(d) as nombre from Demande d where d.demandeur.id = :ownerId group by d.etat")
    java.util.List<EtatCount> countByEtat(@org.springframework.data.repository.query.Param("ownerId") Long ownerId);

    Page<Demande> findByDemandeurId(Long demandeurId, Pageable pageable);
    java.util.Optional<Demande> findByIdAndDemandeurId(Long id, Long demandeurId);
}

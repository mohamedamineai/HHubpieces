package com.hubpieces.repository;

import com.hubpieces.model.Demande;
import com.hubpieces.model.PhotoDemande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoDemandeRepository extends JpaRepository<PhotoDemande, Long> {

    List<PhotoDemande> findByDemandeOrderByDateUploadDesc(Demande demande);
}

package com.hubpieces.repository;

import com.hubpieces.model.EtatValidation;
import com.hubpieces.model.FichierStl;
import com.hubpieces.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FichierStlRepository extends JpaRepository<FichierStl, Long> {

    List<FichierStl> findByEtatValidationOrderByDateUploadDesc(EtatValidation etat);

    List<FichierStl> findByUploadeurOrderByDateUploadDesc(Utilisateur uploadeur);

    List<FichierStl> findByCategorie(String categorie);

    List<FichierStl> findByMarqueIgnoreCase(String marque);

    long countByEtatValidation(EtatValidation etat);
}

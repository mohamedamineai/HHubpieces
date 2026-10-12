package com.hubpieces.repository;

import com.hubpieces.model.EtatPaiement;
import com.hubpieces.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByOffreMakerId(Long offreMakerId);

    List<Transaction> findByEtatPaiement(EtatPaiement etat);

    @Query("SELECT SUM(t.montantTotal) FROM Transaction t WHERE t.etatPaiement = :etat")
    Double sumMontantByEtat(@Param("etat") EtatPaiement etat);

    @Query("SELECT SUM(t.commissionPlateforme) FROM Transaction t WHERE t.etatPaiement = :etat")
    Double sumCommissionByEtat(@Param("etat") EtatPaiement etat);

    long countByEtatPaiement(EtatPaiement etat);
}

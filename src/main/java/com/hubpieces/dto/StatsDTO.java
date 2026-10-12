package com.hubpieces.dto;

public class StatsDTO {
    private long totalUtilisateurs;
    private long totalDemandeurs;
    private long totalMakers;
    private long totalExpertsCao;
    private long totalDemandes;
    private long totalOffres;
    private long totalFichiersStl;
    private long fichiersEnAttente;
    private long totalTransactions;
    private Double montantTotalTransactions;
    private Double commissionTotalePlateforme;
    private long litigesOuverts;
    private long demandesNouvelles;
    private long demandesEnCours;
    private long demandesCloturees;
    private long inscriptionsMois;

    public StatsDTO() {}

    public long getTotalUtilisateurs() { return totalUtilisateurs; }
    public void setTotalUtilisateurs(long totalUtilisateurs) { this.totalUtilisateurs = totalUtilisateurs; }
    public long getTotalDemandeurs() { return totalDemandeurs; }
    public void setTotalDemandeurs(long totalDemandeurs) { this.totalDemandeurs = totalDemandeurs; }
    public long getTotalMakers() { return totalMakers; }
    public void setTotalMakers(long totalMakers) { this.totalMakers = totalMakers; }
    public long getTotalExpertsCao() { return totalExpertsCao; }
    public void setTotalExpertsCao(long totalExpertsCao) { this.totalExpertsCao = totalExpertsCao; }
    public long getTotalDemandes() { return totalDemandes; }
    public void setTotalDemandes(long totalDemandes) { this.totalDemandes = totalDemandes; }
    public long getTotalOffres() { return totalOffres; }
    public void setTotalOffres(long totalOffres) { this.totalOffres = totalOffres; }
    public long getTotalFichiersStl() { return totalFichiersStl; }
    public void setTotalFichiersStl(long totalFichiersStl) { this.totalFichiersStl = totalFichiersStl; }
    public long getFichiersEnAttente() { return fichiersEnAttente; }
    public void setFichiersEnAttente(long fichiersEnAttente) { this.fichiersEnAttente = fichiersEnAttente; }
    public long getTotalTransactions() { return totalTransactions; }
    public void setTotalTransactions(long totalTransactions) { this.totalTransactions = totalTransactions; }
    public Double getMontantTotalTransactions() { return montantTotalTransactions; }
    public void setMontantTotalTransactions(Double montantTotalTransactions) { this.montantTotalTransactions = montantTotalTransactions; }
    public Double getCommissionTotalePlateforme() { return commissionTotalePlateforme; }
    public void setCommissionTotalePlateforme(Double commissionTotalePlateforme) { this.commissionTotalePlateforme = commissionTotalePlateforme; }
    public long getLitigesOuverts() { return litigesOuverts; }
    public void setLitigesOuverts(long litigesOuverts) { this.litigesOuverts = litigesOuverts; }
    public long getDemandesNouvelles() { return demandesNouvelles; }
    public void setDemandesNouvelles(long demandesNouvelles) { this.demandesNouvelles = demandesNouvelles; }
    public long getDemandesEnCours() { return demandesEnCours; }
    public void setDemandesEnCours(long demandesEnCours) { this.demandesEnCours = demandesEnCours; }
    public long getDemandesCloturees() { return demandesCloturees; }
    public void setDemandesCloturees(long demandesCloturees) { this.demandesCloturees = demandesCloturees; }
    public long getInscriptionsMois() { return inscriptionsMois; }
    public void setInscriptionsMois(long inscriptionsMois) { this.inscriptionsMois = inscriptionsMois; }
}

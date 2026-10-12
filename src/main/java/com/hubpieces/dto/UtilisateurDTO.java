package com.hubpieces.dto;

import com.hubpieces.model.RoleNom;
import com.hubpieces.model.StatutUtilisateur;
import com.hubpieces.model.Utilisateur;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class UtilisateurDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String ville;
    private String codePostal;
    private Double noteMoyenne;
    private Integer nombreAvis;
    private StatutUtilisateur statut;
    private LocalDateTime dateInscription;
    private String photoProfil;
    private java.util.Set<String> roles;
    private String rolePrincipal;

    public UtilisateurDTO() {}

    public static UtilisateurDTO fromEntity(Utilisateur u, boolean publicView) {
        UtilisateurDTO dto = new UtilisateurDTO();
        dto.id = u.getId();
        dto.nom = u.getNom();
        dto.prenom = u.getPrenom();
        dto.email = publicView ? null : u.getEmail();
        dto.telephone = publicView ? null : u.getTelephone();
        dto.ville = u.getVille();
        dto.codePostal = u.getCodePostal();
        dto.noteMoyenne = u.getNoteMoyenne();
        dto.nombreAvis = u.getNombreAvis();
        if (!publicView) {
            dto.statut = u.getStatut();
            dto.dateInscription = u.getDateInscription();
            dto.photoProfil = u.getPhotoProfil();
        }
        if (u.getRoles() != null) {
            dto.roles = u.getRoles().stream()
                    .map(r -> r.getNom().name())
                    .collect(Collectors.toSet());
            dto.rolePrincipal = dto.roles.stream().findFirst().orElse(RoleNom.DEMANDEUR.name());
        }
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }
    public String getCodePostal() { return codePostal; }
    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }
    public Double getNoteMoyenne() { return noteMoyenne; }
    public void setNoteMoyenne(Double noteMoyenne) { this.noteMoyenne = noteMoyenne; }
    public Integer getNombreAvis() { return nombreAvis; }
    public void setNombreAvis(Integer nombreAvis) { this.nombreAvis = nombreAvis; }
    public StatutUtilisateur getStatut() { return statut; }
    public void setStatut(StatutUtilisateur statut) { this.statut = statut; }
    public LocalDateTime getDateInscription() { return dateInscription; }
    public void setDateInscription(LocalDateTime dateInscription) { this.dateInscription = dateInscription; }
    public String getPhotoProfil() { return photoProfil; }
    public void setPhotoProfil(String photoProfil) { this.photoProfil = photoProfil; }
    public java.util.Set<String> getRoles() { return roles; }
    public void setRoles(java.util.Set<String> roles) { this.roles = roles; }
    public String getRolePrincipal() { return rolePrincipal; }
    public void setRolePrincipal(String rolePrincipal) { this.rolePrincipal = rolePrincipal; }
}

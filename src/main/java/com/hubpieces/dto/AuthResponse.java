package com.hubpieces.dto;

public class AuthResponse {
    private boolean success;
    private String message;
    private Long userId;
    private String nom;
    private String prenom;
    private String email;
    private String role;

    public AuthResponse() {}

    public AuthResponse(boolean success, String message, Long userId, String nom, String prenom, String email, String role) {
        this.success = success;
        this.message = message;
        this.userId = userId;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.role = role;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}

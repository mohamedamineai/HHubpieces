package com.hubpieces.dto;

import jakarta.validation.constraints.*;

public class LoginRequest {
    @NotBlank @Email @Size(max = 150)
    private String email;
    @NotBlank @Size(max = 72)
    private String motDePasse;

    public LoginRequest() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
}

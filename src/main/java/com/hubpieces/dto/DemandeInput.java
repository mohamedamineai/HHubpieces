package com.hubpieces.dto;

import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Création d'une demande. Le propriétaire et l'état sont attribués par le serveur.")
public record DemandeInput(
        @NotBlank @Size(max = 200) String titre,
        @NotBlank @Size(max = 10000) String description,
        @Size(max = 100) String marque,
        @Size(max = 100) String modeleAppareil,
        @Size(max = 50) String materiauSouhaite,
        @Positive @DecimalMax("10000000") Double budgetMax,
        @NotBlank @Size(max = 100) String ville,
        @Size(max = 10) String codePostal) {}

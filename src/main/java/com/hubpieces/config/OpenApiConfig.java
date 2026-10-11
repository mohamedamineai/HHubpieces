package com.hubpieces.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI hubpiecesOpenAPI() {
        return new OpenAPI().info(new Info().title("Hubpieces — API du premier lot")
                .version("0.1.0")
                .description("Session HTTP : avant toute écriture (inscription, connexion, déconnexion et demandes), "
                        + "exécutez GET /api/csrf ; Swagger transmet ensuite le cookie XSRF-TOKEN dans X-XSRF-TOKEN. Connectez-vous via /api/auth/login. "
                        + "Demandes : 400 validation, 401 absence de session, 403 rôle/statut ou CSRF, "
                        + "404 ressource absente ou appartenant à un autre compte, 409 transition interdite. "
                        + "Périmètre provisoire, à confronter aux consignes des séances 3 et 4."));
    }
}

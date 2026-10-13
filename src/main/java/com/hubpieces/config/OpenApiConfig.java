package com.hubpieces.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI hubpiecesOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hubpieces API")
                        .description("API REST de la plateforme Hubpieces - Repartition par impression 3D. " +
                                "Cette API gere 4 types d'acteurs : DEMANDEUR, MAKER, EXPERT_CAO et ADMIN. " +
                                "Toutes les operations d'ecriture necessitent une authentification via session (cookie JSESSIONID).")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipe Hubpieces")
                                .email("contact@hubpieces.fr"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")))
                // URL relative : Swagger fonctionne sur localhost comme derrière le proxy
                // de prévisualisation Arena, sans forcer le navigateur à appeler localhost.
                .servers(List.of(
                        new Server().url("/").description("Serveur courant")
                ));
    }
}

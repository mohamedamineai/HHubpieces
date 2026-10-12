package com.hubpieces.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Servir les fichiers uploades depuis le disque
        String uploadPath = "file:" + System.getProperty("user.dir") + "/uploads/";
        registry.addResourceHandler("/uploads/**").addResourceLocations(uploadPath);
    }

    /**
     * Redirections "propres" vers les pages statiques du site.
     * Garantit que http://localhost:9090/ et http://localhost:9090/login
     * ouvrent TOUJOURS les vraies pages HTML de Hubpieces (index.html, login.html, ...)
     * et jamais le formulaire par defaut de Spring Security ("Please sign in").
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Racine du site -> page d'accueil statique (forward, l'URL reste "/")
        registry.addViewController("/").setViewName("forward:/index.html");

        // URLs sans extension -> redirection vers la page HTML correspondante
        registry.addRedirectViewController("/login", "/login.html");
        registry.addRedirectViewController("/register", "/register.html");
        registry.addRedirectViewController("/catalogue", "/catalogue.html");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000", "http://localhost:4200", "http://localhost:8080", "http://localhost:9090")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}

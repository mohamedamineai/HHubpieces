package com.hubpieces.config;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * Point d'entree d'authentification Hubpieces (classe nommee, pas de lambda).
 *
 * <p>Depuis Spring Security 7, HttpSecurityConfiguration applique TOUJOURS un
 * DefaultLoginPageConfigurer par defaut. Son configure() ajoute le
 * DefaultLoginPageGeneratingFilter (la page "Please sign in") des que le point
 * d'entree est null. Declarer ce point d'entree court-circuite donc l'ajout du
 * filtre. Comme tout est en permitAll(), il n'est de toute facon jamais
 * declenche : il sert uniquement de verrou de configuration.</p>
 *
 * <p>Comportement si jamais il est appele : 401 JSON sur /api/** (jamais de
 * redirection HTML pour l'API), redirection vers la VRAIE page de connexion
 * Hubpieces (/login.html) pour les pages du site.</p>
 */
public class HubpiecesAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        String uri = request.getRequestURI();
        if (uri != null && uri.startsWith("/api/")) {
            // API : reponse JSON 401, jamais de redirection HTML
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"message\":\"Non connecte\"}");
        } else {
            // Pages du site : on renvoie vers la VRAIE page de connexion Hubpieces
            response.sendRedirect(request.getContextPath() + "/login.html");
        }
    }
}

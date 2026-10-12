package com.hubpieces.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/login.html",
                                "/register.html",
                                "/catalogue.html",
                                "/creer-demande.html",
                                "/mes-demandes.html",
                                "/mes-offres.html",
                                "/mes-modelisations.html",
                                "/mes-messages.html",
                                "/mon-profil.html",
                                "/profil-public.html",
                                "/gestion-admin.html",
                                "/dashboard-demandeur.html",
                                "/dashboard-maker.html",
                                "/dashboard-expertcao.html",
                                "/dashboard-admin.html",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/uploads/**",
                                "/api/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",
                                "/webjars/**"
                        ).permitAll()
                        .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                // On desactive TOUT mecanisme de login/logout par defaut de Spring Security :
                // l'authentification est geree par AuthController (/api/auth/login) + HttpSession.
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .logout(logout -> logout.disable())
                // VERROU DECISIF CONTRE LA PAGE "Please sign in" (Spring Security 7.0.x).
                //
                // Depuis Spring Security 7, HttpSecurityConfiguration applique TOUJOURS
                // un DefaultLoginPageConfigurer par defaut (.with(new DefaultLoginPageConfigurer<>())).
                // Son configure() ajoute le DefaultLoginPageGeneratingFilter (la page
                // "Please sign in") des que :
                //     loginPageGeneratingFilter.isEnabled() && authenticationEntryPoint == null
                //
                // Desactiver formLogin n'est donc plus un verrou suffisant : le seul moyen
                // fiable de supprimer cette page est de declarer soi-meme un
                // AuthenticationEntryPoint, ce qui rend "authenticationEntryPoint != null"
                // et court-circuite l'ajout du filtre.
                //
                // Comme tout est en permitAll(), ce point d'entree n'est de toute facon
                // jamais declenche : il sert uniquement de verrou de configuration.
                .exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, authException) -> {
                    String uri = request.getRequestURI();
                    if (uri != null && uri.startsWith("/api/")) {
                        // API : reponse JSON 401, jamais de redirection HTML
                        response.setStatus(401);
                        response.setContentType("application/json;charset=UTF-8");
                        response.getWriter().write("{\"message\":\"Non connecte\"}");
                    } else {
                        // Pages du site : on renvoie vers la VRAIE page de connexion Hubpieces
                        response.sendRedirect(request.getContextPath() + "/login.html");
                    }
                }));
        return http.build();
    }
}

package com.hubpieces.config;

import jakarta.servlet.Filter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
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
    @Order(1)
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
                                "/mes-mssages.html",
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
                // VERROU CONTRE LA PAGE "Please sign in" (Spring Security 7.0.x).
                //
                // Depuis Spring Security 7, HttpSecurityConfiguration applique TOUJOURS
                // un DefaultLoginPageConfigurer par defaut (.with(new DefaultLoginPageConfigurer<>())).
                // Son configure() ajoute le DefaultLoginPageGeneratingFilter (la page
                // "Please sign in") des que :
                //     loginPageGeneratingFilter.isEnabled() && authenticationEntryPoint == null
                //
                // Desactiver formLogin n'est donc plus un verrou suffisant : le seul moyen
                // fiable de supprimer cette page est de declarer soi-meme un
                // AuthenticationEntryPoint (classe nommee HubpiecesAuthenticationEntryPoint,
                // pas de lambda imbriquee : aucun risque d'inference de type), ce qui rend
                // "authenticationEntryPoint != null" et court-circuite l'ajout du filtre.
                //
                // Comme tout est en permitAll(), ce point d'entree n'est de toute facon
                // jamais declenche : il sert uniquement de verrou de configuration.
                //
                // NOTE : ce verrou seul ne suffit PAS si c'est la chaine PAR DEFAUT de
                // Spring Boot (ServletWebSecurityAutoConfiguration.defaultSecurityFilterChain)
                // qui sert les requetes. D'ou les deux autres verrous : exclusion de
                // ServletWebSecurityAutoConfiguration dans application.properties et
                // @Order(1) sur ce bean (sans ordre explicite la chaine est en
                // LOWEST_PRECEDENCE et toute autre chaine placee devant sert les requetes).
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new HubpiecesAuthenticationEntryPoint()));
        return http.build();
    }

    /**
     * Garantie dure de redirection des URLs publiques sans extension, AVANT
     * springSecurityFilterChain (Ordered.HIGHEST_PRECEDENCE) : /login -&gt; /login.html,
     * /register -&gt; /register.html, /catalogue -&gt; /catalogue.html (302).
     * Independante de la configuration interne des configurers.
     */
    @Bean
    public FilterRegistrationBean<Filter> publicPageRedirectFilterRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>(new PublicPageRedirectFilter());
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registration.addUrlPatterns("/login", "/register", "/catalogue");
        return registration;
    }
}

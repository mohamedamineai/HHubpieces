package com.hubpieces.config;

import jakarta.servlet.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Journal de diagnostic imprime une fois, au demarrage de l'application.
 *
 * <p>Il liste chaque {@link SecurityFilterChain} reellement presente dans le contexte
 * Spring, affiche la LISTE COMPLETE des filtres au niveau INFO, et signale :
 * <ul>
 *   <li>la presence de {@code DefaultLoginPageGeneratingFilter}, c'est-a-dire du filtre
 *       qui genere la page par defaut "Please sign in" de Spring Security ;</li>
 *   <li>la presence de la chaine PAR DEFAUT de Spring Boot
 *       ({@code ServletWebSecurityAutoConfiguration.defaultSecurityFilterChain}),
 *       detectee via ses marqueurs {@code CsrfFilter} +
 *       {@code UsernamePasswordAuthenticationFilter} + {@code BasicAuthenticationFilter}.</li>
 * </ul></p>
 *
 * <p>Triple utilite :</p>
 * <ul>
 *   <li><b>Preuve de build</b> : si ces lignes n'apparaissent pas dans la console au
 *       demarrage, l'application qui tourne n'est PAS celle du code source courant
 *       (build obsolete dans {@code target/}, vieux processus deja ecoute sur le port
 *       9090, ou autre repertoire de travail).</li>
 *   <li><b>Preuve de configuration</b> : si une chaine annonce
 *       "page de login Spring Security : PRESENTE", la page "Please sign in" est bien
 *       servie par l'application, et il manque le verrou {@code exceptionHandling}
 *       dans {@link SecurityConfig}.</li>
 *   <li><b>Preuve anti-chaine-par-defaut</b> : si une chaine annonce "CHAINE PAR
 *       DEFAUT SPRING BOOT DETECTEE", c'est {@code defaultSecurityFilterChain}
 *       (anyRequest().authenticated() + formLogin + httpBasic) qui sert les requetes
 *       au lieu de {@code SecurityConfig.filterChain} : l'exclusion de
 *       {@code ServletWebSecurityAutoConfiguration} n'est pas prise en compte
 *       (rebuild + redemarrage requis).</li>
 * </ul>
 */
@Component
public class SecurityChainReporter {

    private static final Logger log = LoggerFactory.getLogger(SecurityChainReporter.class);

    private static final String LOGIN_PAGE_FILTER = "DefaultLoginPageGeneratingFilter";

    // Marqueurs de la chaine PAR DEFAUT de Spring Boot : CSRF actif (CsrfFilter),
    // formLogin actif (UsernamePasswordAuthenticationFilter), httpBasic actif
    // (BasicAuthenticationFilter). La chaine Hubpieces desactive ces trois mecanismes,
    // donc leur presence SIMULTANEE dans une chaine prouve que c'est la chaine par
    // defaut qui sert les requetes, pas SecurityConfig.filterChain.
    private static final String CSRF_FILTER = "CsrfFilter";
    private static final String USERNAME_PASSWORD_FILTER = "UsernamePasswordAuthenticationFilter";
    private static final String BASIC_AUTH_FILTER = "BasicAuthenticationFilter";

    private final ObjectProvider<SecurityFilterChain> chains;

    public SecurityChainReporter(ObjectProvider<SecurityFilterChain> chains) {
        this.chains = chains;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void reportSecurityChains() {
        int total = 0;
        int withLoginPage = 0;
        int defaultChains = 0;

        for (SecurityFilterChain chain : chains) {
            total++;
            List<Filter> filters = chain.getFilters();
            Set<String> names = filters.stream()
                    .map(f -> f.getClass().getSimpleName())
                    .collect(Collectors.toSet());

            boolean loginPagePresent = names.contains(LOGIN_PAGE_FILTER);
            if (loginPagePresent) {
                withLoginPage++;
            }

            boolean defaultChain = names.contains(CSRF_FILTER)
                    && names.contains(USERNAME_PASSWORD_FILTER)
                    && names.contains(BASIC_AUTH_FILTER);
            if (defaultChain) {
                defaultChains++;
            }

            log.info("[SECURITE] Chaine #{} : {} filtres - page de login Spring Security : {}",
                    total, filters.size(), loginPagePresent ? "PRESENTE (page \"Please sign in\" active)" : "absente (OK)");

            // Liste COMPLETE des filtres au niveau INFO (pas DEBUG) : preuve lisible
            // dans la console de demarrage standard, sans changer le niveau de log.
            for (Filter filter : filters) {
                log.info("[SECURITE]     - {}", filter.getClass().getSimpleName());
            }

            if (defaultChain) {
                log.error("[SECURITE] Chaine #{} : CHAINE PAR DEFAUT SPRING BOOT DETECTEE "
                        + "(marqueurs CsrfFilter + UsernamePasswordAuthenticationFilter + "
                        + "BasicAuthenticationFilter) : c'est defaultSecurityFilterChain "
                        + "(anyRequest().authenticated() + formLogin + httpBasic) qui sert les "
                        + "requetes au lieu de SecurityConfig.filterChain. Verifiez "
                        + "spring.autoconfigure.exclude (ServletWebSecurityAutoConfiguration) "
                        + "puis faites Build > Rebuild Project + relance.", total);
            }
        }

        if (total == 0) {
            log.error("[SECURITE] AUCUNE SecurityFilterChain personnalisee trouvee dans le contexte : "
                    + "SecurityConfig n'est pas charge. Verifiez que l'application executee correspond "
                    + "bien a ce code source (rebuild + redemarrage).");
        } else {
            if (withLoginPage > 0) {
                log.error("[SECURITE] {} chaine(s) contiennent encore DefaultLoginPageGeneratingFilter : "
                        + "la page \"Please sign in\" sera servie sur /login.", withLoginPage);
            }
            if (defaultChains > 0) {
                log.error("[SECURITE] {} chaine(s) par defaut Spring Boot detectee(s) : le site est bloque "
                        + "par anyRequest().authenticated(). Excluez ServletWebSecurityAutoConfiguration "
                        + "(@Order(1) sur SecurityConfig.filterChain seul ne suffit pas).", defaultChains);
            }
            if (withLoginPage == 0 && defaultChains == 0) {
                log.info("[SECURITE] Configuration Hubpieces active : {} chaine(s), "
                        + "aucune page de login Spring Security. /login -> login.html", total);
            }
        }
    }
}

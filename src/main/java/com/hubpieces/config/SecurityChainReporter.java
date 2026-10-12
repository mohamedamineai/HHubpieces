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

/**
 * Journal de diagnostic imprime une fois, au demarrage de l'application.
 *
 * <p>Il liste chaque {@link SecurityFilterChain} reellement presente dans le contexte
 * Spring et signale la presence de {@code DefaultLoginPageGeneratingFilter}, c'est-a-dire
 * du filtre qui genere la page par defaut "Please sign in" de Spring Security.</p>
 *
 * <p>Double utilite :</p>
 * <ul>
 *   <li><b>Preuve de build</b> : si ces lignes n'apparaissent pas dans la console au
 *       demarrage, l'application qui tourne n'est PAS celle du code source courant
 *       (build obsolete dans {@code target/}, vieux processus deja ecoute sur le port
 *       9090, ou autre repertoire de travail).</li>
 *   <li><b>Preuve de configuration</b> : si une chaine annonce
 *       "page de login Spring Security : PRESENTE", la page "Please sign in" est bien
 *       servie par l'application, et il manque le verrou {@code exceptionHandling}
 *       dans {@link SecurityConfig}.</li>
 * </ul>
 */
@Component
public class SecurityChainReporter {

    private static final Logger log = LoggerFactory.getLogger(SecurityChainReporter.class);

    private static final String LOGIN_PAGE_FILTER = "DefaultLoginPageGeneratingFilter";

    private final ObjectProvider<SecurityFilterChain> chains;

    public SecurityChainReporter(ObjectProvider<SecurityFilterChain> chains) {
        this.chains = chains;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void reportSecurityChains() {
        int total = 0;
        int withLoginPage = 0;

        for (SecurityFilterChain chain : chains) {
            total++;
            List<Filter> filters = chain.getFilters();
            boolean loginPagePresent = filters.stream()
                    .anyMatch(f -> LOGIN_PAGE_FILTER.equals(f.getClass().getSimpleName()));
            if (loginPagePresent) {
                withLoginPage++;
            }

            log.info("[SECURITE] Chaine #{} : {} filtres - page de login Spring Security : {}",
                    total, filters.size(), loginPagePresent ? "PRESENTE (page \"Please sign in\" active)" : "absente (OK)");

            if (log.isDebugEnabled()) {
                for (Filter filter : filters) {
                    log.debug("[SECURITE]     - {}", filter.getClass().getSimpleName());
                }
            }
        }

        if (total == 0) {
            log.error("[SECURITE] AUCUNE SecurityFilterChain personnalisee trouvee dans le contexte : "
                    + "SecurityConfig n'est pas charge. Verifiez que l'application executee correspond "
                    + "bien a ce code source (rebuild + redemarrage).");
        } else if (withLoginPage > 0) {
            log.error("[SECURITE] {} chaine(s) contiennent encore DefaultLoginPageGeneratingFilter : "
                    + "la page \"Please sign in\" sera servie sur /login.", withLoginPage);
        } else {
            log.info("[SECURITE] Configuration Hubpieces active : {} chaine(s), "
                    + "aucune page de login Spring Security. /login -> login.html", total);
        }
    }
}

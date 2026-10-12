package com.hubpieces.config;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Garantie dure de redirection des URLs publiques sans extension vers les
 * vraies pages statiques Hubpieces :
 * /login -&gt; /login.html, /register -&gt; /register.html, /catalogue -&gt; /catalogue.html.
 *
 * <p>Ce filtre est enregistre via un FilterRegistrationBean en
 * Ordered.HIGHEST_PRECEDENCE, donc AVANT springSecurityFilterChain : la
 * redirection en 302 a lieu quelle que soit la configuration interne des
 * configurers Spring Security, et /login ne peut plus afficher la page
 * par defaut "Please sign in".</p>
 *
 * <p>Seules les correspondances EXACTES sont redirigees : /login.html,
 * /login/foo ou toute autre URL traversent le filtre sans modification.</p>
 */
public class PublicPageRedirectFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String contextPath = httpRequest.getContextPath();
        String uri = httpRequest.getRequestURI();
        String path = uri;
        if (uri != null && contextPath != null && uri.startsWith(contextPath)) {
            path = uri.substring(contextPath.length());
        }

        String target = null;
        if ("/login".equals(path)) {
            target = "/login.html";
        } else if ("/register".equals(path)) {
            target = "/register.html";
        } else if ("/catalogue".equals(path)) {
            target = "/catalogue.html";
        }

        if (target != null) {
            httpResponse.sendRedirect(contextPath + target);
            return;
        }
        chain.doFilter(request, response);
    }
}

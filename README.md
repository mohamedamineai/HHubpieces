# Hubpieces

Plateforme de réparation par impression 3D : Spring Boot, Java 17, MySQL et interfaces HTML/CSS/JS.

## Premier lot connecté

- Dashboard réel : `/dashboard-demandeur.html` (statistiques et demandes récentes).
- Interface : `/mes-demandes.html` (compte **DEMANDEUR**).
- Swagger UI : `/swagger-ui.html` ; contrat OpenAPI : `/v3/api-docs`.
- [Documentation détaillée, règles métier et procédures de test](docs/SEANCES-3-4.md).

Les consignes exactes des séances 3 et 4 n'étant pas jointes, ce lot est un **socle provisoire**, pas une déclaration de conformité ni une application achevée. Les dashboards maker, expert et administrateur restent des maquettes.

## Démarrage local

Prérequis : JDK 17+, MySQL 8 et accès aux dépôts Maven. Le serveur écoute sur le port 9090.

Configurer les variables `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME` et `SPRING_DATASOURCE_PASSWORD` pour votre base de développement, puis :

```sh
bash mvnw spring-boot:run
```

Windows : `mvnw.cmd spring-boot:run`. Ne pas utiliser une base de production : la configuration héritée utilise `ddl-auto=update`.

Tests unitaires du nouveau service, sans démarrage MySQL :

```sh
bash mvnw -Dtest=DemandeServiceTest,AuthSessionTest test
```

La suite complète `bash mvnw test` inclut un test de démarrage Spring nécessitant MySQL. Les tests Java n'ont pas été exécutés dans l'environnement d'édition (JDK absent).

Tests du client HTTP JavaScript (Node.js 18+) :

```sh
node --test src/test/js/api-client.test.cjs
```

Six tests réussis dans l'environnement d'édition. CSRF est désormais requis pour **toutes** les écritures, y compris login/register/logout : dans Swagger, exécuter `GET /api/csrf` avant de se connecter.

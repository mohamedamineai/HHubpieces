# Hubpieces — dossier technique et recette du premier lot

Date : 11 octobre 2026.

## Mise à jour — lot 2

Le dashboard demandeur est désormais connecté : compteurs personnels réels, cinq demandes récentes, état vide, reprise après erreur, accès direct au détail et à la création. Les autres dashboards restent des maquettes.

La protection CSRF couvre maintenant aussi inscription, connexion et déconnexion. `api-client.js` obtient un jeton avant chaque écriture de ces formulaires et transmet les cookies de session. `/api/auth/me` recharge l'identité depuis la base et invalide la session lorsque l'utilisateur a disparu, n'est plus actif ou a perdu le rôle de la session. Le header échappe les noms avant insertion HTML.

## 1. Périmètre et décisions

La demande vise les séances 3 et 4, mais ni le cahier des charges ni l'image de référence ne sont présents dans le dépôt ou dans les pièces jointes reçues. Aucun découpage officiel entre séances n'est donc affirmé ici.

Le lot implémente un parcours limité et cohérent avec les entités existantes : **demandeur connecté → création → liste personnelle → détail → annulation avant engagement**. Les règles ajoutées sont des hypothèses conservatrices à valider avec l'énoncé.

Hors périmètre de ce lot : offres makers, affectation CAO, téléversement STL/photos, paiement réel ou simulé, livraison, messagerie, notifications, avis, litiges, administration opérationnelle, édition de profil et connexion des dashboards maker, expert et administrateur. Aucun nouveau modèle GLB n'est nécessaire pour ce parcours.

## 2. État initial observé

- Spring Boot 4.0.8, Java 17, Spring MVC, JPA, Security, MySQL.
- Entités métier déjà présentes et export SQL dans `src/main/resources/static/DB/hubpieces_db.sql`.
- Quatre rôles : DEMANDEUR, MAKER, EXPERT_CAO, ADMIN.
- Le script `data.sql` initialise les rôles ; l'export versionné ne fournit pas de jeu d'utilisateurs ou de demandes.
- L'authentification stocke l'identité dans `HttpSession`, pas dans un JWT ni dans un contexte Spring Security authentifié.
- Les quatre dashboards historiques contiennent des exemples statiques.
- Logo, filigranes, styles bleu nuit/orange et modèles GLB déjà disponibles.

**Attention :** lecture de fichiers SQL ne signifie pas inspection d'une base vivante. La base MySQL réelle n'a pas été interrogée.

## 3. Changements réalisés

### Backend

- Ajout de Bean Validation et springdoc OpenAPI UI 3.0.1 (ligne Spring Boot 4 ; résolution des dépendances non vérifiée dans cet environnement).
- `DemandeInput` : contrat d'entrée sans propriétaire ni état modifiable par le client.
- `DemandeView` : sortie explicite, sans sérialisation des utilisateurs, mots de passe ou relations JPA.
- `DemandeRepository` : requêtes filtrées par propriétaire ; pagination.
- `DemandeService` : transactions, contrôle de compte actif et du rôle en base à chaque opération, création et annulation.
- `DemandeController` : liste, détail, création, annulation.
- `ApiErrors` : erreurs de validation structurées, JSON invalide, identifiants invalides et conflits de données ; pas de détails SQL exposés.
- `CsrfController` : acquisition du jeton CSRF.
- `OpenApiConfig` : titre, version, mode de session et explication des erreurs dans Swagger.

### Authentification : corrections limitées

- Validation des champs des DTO d'inscription/connexion.
- Inscription publique avec rôle ADMIN refusée.
- Inscription utilisateur + profil dans une transaction.
- Connexion refusée aux comptes non actifs.
- Rotation de l'identifiant de session après authentification réussie.
- Correction de l'encodage du fichier `AuthService.java` vers UTF-8.

L'authentification existante conserve ses réponses historiques `200` avec `success:false` pour certains refus fonctionnels. Les erreurs Bean Validation répondent `400`.

### Interface

`/mes-demandes.html` : page sans dépendance CDN ajoutée, liée au dashboard demandeur. Couleurs et logo existants réutilisés ; filigrane décoratif ; disposition responsive ; états chargement, liste vide et erreur ; pagination ; formulaires et fenêtres natives `<dialog>` ; bouton désactivé pendant envoi ; confirmation avant annulation ; détail textuel.

Les données reçues sont insérées via `textContent`, jamais interprétées comme HTML. Les contenus utilisateurs ne peuvent donc pas injecter du HTML dans cette nouvelle page. Les champs sont contrôlés dans le navigateur ET sur le serveur.

Le dashboard demandeur a été remplacé par une interface connectée à `/api/demandes/dashboard`. Les quatre compteurs représentent le total, les nouvelles, les clôturées et les annulées : « clôturée » ne signifie pas automatiquement « pièce livrée ». Toutes les catégories d'état sont renvoyées par l'API, même à zéro. Le total est calculé en base sur toutes les demandes du propriétaire, pas sur la première page. Les cinq demandes récentes sont triées par date puis ID décroissants.

La nouvelle interface n'utilise pas de CDN ni de modèle 3D supplémentaire. Elle affiche un logo décoratif, une animation d'apparition légère et respecte `prefers-reduced-motion`. Le parcours `mes-demandes.html?nouvelle=1` ouvre le formulaire ; `?demande=ID` ouvre le détail autorisé par l'API. Aucun chiffre fictif n'est affiché en cas d'erreur : les compteurs restent à « — » et un bouton permet de réessayer.

## 4. Règles métier implémentées

| Sujet | Règle |
|---|---|
| Accès | Session avec utilisateur existant, ACTIF et rôle DEMANDEUR en base |
| Propriété | Identité issue de la session, jamais du formulaire |
| Liste | Seulement ses demandes ; tri date décroissante puis ID décroissant |
| Pagination | Page ≥ 0 ; taille de 1 à 50, défaut 12 |
| Création | État imposé NOUVELLE ; date et propriétaire côté serveur |
| Titre | Obligatoire, non blanc, 200 caractères maximum |
| Description | Obligatoire, non blanche, 10 000 caractères maximum |
| Ville | Obligatoire, non blanche, 100 caractères maximum |
| Budget | Optionnel ; strictement positif, maximum 10 000 000 |
| Autres champs | Marque/modèle ≤ 100 ; matériau ≤ 50 ; code postal ≤ 10 |
| Détail | 404 aussi bien pour demande absente que pour celle d'un autre compte |
| Annulation | NOUVELLE → ANNULEE uniquement ; nouvelle annulation sans effet |
| Historique | Pas de suppression physique |

La devise du budget n'est pas définie par les documents reçus : aucun symbole monétaire n'est inventé. Le budget conserve le type `Double` du modèle existant ; pour une future comptabilité, migration vers `BigDecimal` à prévoir avant tout paiement.

Les états MODELISATION_REQUISE, RECHERCHE_MAKER, ATTENTE_VALIDATION, EN_COURS_IMPRESSION et CLOTUREE existent dans le modèle mais ne sont pas des transitions réalisables dans ce lot.

## 5. Contrat HTTP

| Méthode | URL | Succès | Utilisation |
|---|---|---|---|
| GET | `/api/csrf` | 200 | Jeton et nom du header CSRF |
| GET | `/api/demandes?page=0&size=12` | 200 | `{content,totalElements,totalPages,page}` |
| GET | `/api/demandes/dashboard` | 200 | `{total,parEtat,recentes}` : statistiques personnelles et 5 dernières demandes |
| GET | `/api/demandes/{id}` | 200 | Détail personnel |
| POST | `/api/demandes` | 201 + Location | Création |
| POST | `/api/demandes/{id}/annulation` | 200 | Annulation personnelle |

Erreurs : 400 données invalides ; 401 session absente/invalide ; 403 compte/rôle refusé ou CSRF ; 404 demande inaccessible ; 409 état incompatible ou conflit d'intégrité. Une écriture sans jeton CSRF peut répondre 403 avant le contrôle de session. Les erreurs CSRF du filtre Security ne suivent pas nécessairement le JSON de `ApiErrors`.

Exemple de création :

```json
{
  "titre": "Bouton de commande de four",
  "description": "Axe cassé. Dimensions à préciser avant fabrication.",
  "marque": "Exemple",
  "modeleAppareil": "Four A",
  "materiauSouhaite": "À déterminer",
  "budgetMax": 150,
  "ville": "Tanger",
  "codePostal": "90000"
}
```

## 6. Base de données et démarrage

1. Installer Java 17+, MySQL 8 ; vérifier `java -version`.
2. Utiliser une base locale dédiée avec un utilisateur disposant des droits nécessaires. Ne jamais mettre de secrets réels dans Git.
3. Configurer `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`.
4. Pour une base neuve, démarrer une première fois avec `SPRING_JPA_DEFER_DATASOURCE_INITIALIZATION=true` afin d'initialiser les rôles après la création des tables JPA.
5. Lancer `bash mvnw spring-boot:run` (Windows : `mvnw.cmd spring-boot:run`).
6. Vérifier dans MySQL la présence des quatre rôles ; créer un compte DEMANDEUR depuis l'interface.
7. Ouvrir `http://localhost:9090/mes-demandes.html`.

Aucune migration métier ni donnée personnelle n'a été ajoutée. La configuration héritée `ddl-auto=update` et `continue-on-error=true` doit être revue avant production : l'une modifie automatiquement le schéma, l'autre peut masquer les échecs du script d'initialisation. L'export SQL existant est un document de référence, pas une migration à importer aveuglément dans une base existante.

Pour une base déjà remplie, effectuer d'abord une sauvegarde et travailler sur une copie anonymisée. Examiner les comptes/rôles et les demandes existantes sans partager les secrets ni les hashes de mots de passe.

## 7. Tester avec Swagger UI

1. Démarrer le backend et ouvrir `/swagger-ui.html` sur la même origine.
2. Exécuter d’abord `GET /api/csrf` pour obtenir le cookie CSRF. Dans « 01 — Session », exécuter `POST /api/auth/register` pour créer un compte, ou `POST /api/auth/login` pour se connecter. Vérifier `success:true`.
3. Exécuter `GET /api/auth/me` pour vérifier le compte courant.
4. Exécuter `GET /api/csrf`. Le navigateur reçoit le cookie XSRF-TOKEN ; la configuration Swagger active sa transmission dans le header X-XSRF-TOKEN.
5. Dans « 02 — Mes demandes », créer une demande avec l'exemple ci-dessus. Attendre 201 et noter l'ID retourné.
6. Lister les demandes, consulter cet ID, puis l'annuler.
7. Répéter l'annulation : même état ANNULEE, pas d'erreur.
8. Pour tester l'isolation, se déconnecter puis se connecter avec un deuxième compte DEMANDEUR ; renouveler le jeton CSRF. Le détail du premier compte doit répondre 404.

Ne pas utiliser un bearer token : ce backend emploie une session et des cookies. Si Swagger renvoie 403 en écriture, vérifier d'abord la récupération du jeton et les cookies, puis le rôle et le statut.

## 8. Recette fonctionnelle et négative

| Test | Résultat attendu |
|---|---|
| Liste avec compte neuf | Liste vide, total 0 ; aucun exemple artificiel |
| Création valide | 201 ; propriétaire correct ; NOUVELLE ; visible après rechargement |
| Titre blanc, ville absente, description trop longue | 400 et champs en erreur |
| Budget nul/non fourni | Accepté |
| Budget 0, négatif ou > 10 000 000 | 400 |
| JSON invalide ou ID non numérique | 400 |
| GET sans session | 401 |
| POST sans CSRF | 403 |
| Compte maker/expert/admin ou suspendu | 403 sur les demandes |
| Demande d'un autre compte | 404, sans fuite de contenu |
| Annulation d'une nouvelle demande | 200, ANNULEE, conservée dans la liste |
| Annulation répétée | 200, ANNULEE |
| Annulation d'une demande engagée | 409 (état à préparer dans une base de test) |
| page=-1 ou size=51 | 400 |
| Inscription avec rôle ADMIN | Refus `success:false`, aucun utilisateur créé |
| Consultation `/DB/hubpieces_db.sql` | Accès interdit |
| Texte contenant des balises HTML | Affiché comme texte dans le nouvel espace |
| Mobile, clavier, Échap sur une modale | Mise en page lisible, focus natif et fermeture |
| Réseau interrompu | Erreur visible, pas de succès fictif |

Créer au moins 13 demandes pour vérifier la pagination. Les doubles clics sont limités par la désactivation du bouton côté interface, mais aucune clé d'idempotence n'est encore implémentée pour la création côté serveur.

## 9. Tests ajoutés et vérifications effectuées

`DemandeServiceTest` contient onze tests unitaires Mockito : accès anonyme, compte suspendu, mauvais rôle, isolation propriétaire, création, annulation idempotente, refus des autres états, pagination bornée et trois tests de dashboard. `AuthSessionTest` ajoute cinq tests sur la session (identité courante, suspension, rôle révoqué, utilisateur supprimé, session absente).

Commande : `bash mvnw -Dtest=DemandeServiceTest,AuthSessionTest test`.

**Exécuté ici :** contrôle syntaxique JavaScript avec `node --check`, contrôle XML du POM et `git diff --check`. Les **six tests du client HTTP** réussissent avec `node --test src/test/js/api-client.test.cjs` : GET avec session, POST avec CSRF/JSON, erreurs de validation, interruption si CSRF échoue, réponse vide, refus d'une URL externe. Ces tests utilisent un réseau simulé : ils ne remplacent pas la recette du backend.

**Non exécuté ici :** compilation Java, résolution Maven, tests JUnit, test Spring/MySQL, Swagger en navigateur et recette visuelle. L'environnement n'a pas de JDK ; les hôtes Maven ne font pas partie des hôtes réseau autorisés. Aucun résultat de test backend réussi n'est revendiqué.

Les tests ajoutés ne couvrent pas encore les contraintes Bean Validation en HTTP ni le filtre CSRF ; la recette ci-dessus doit être exécutée et des tests d'intégration doivent suivre.

## 10. Sécurité : limites explicites

- Le nouveau service contrôle les accès côté serveur. La configuration globale continue toutefois à autoriser les routes, comme dans l'existant : toute nouvelle API doit disposer de contrôles équivalents ou être intégrée à une authentification Spring Security complète.
- CSRF activé sur toutes les écritures, y compris les routes login/register/logout. Les scripts existants ont été adaptés. Swagger doit recevoir le cookie CSRF avant la première inscription ou connexion ; un client externe doit conserver les cookies et transmettre le header indiqué par `/api/csrf`.
- `/api/auth/me` relit l’identité et les droits en base, puis invalide une session devenue non valide. Le service des demandes vérifie également le rôle et le statut indépendamment du contrôle frontend.
- Le répertoire `/DB/**` n'est plus consultable en HTTP. Déplacer ultérieurement l'export hors du dossier statique reste recommandé.
- Cookies HttpOnly et SameSite=Lax pour la session ; HTTPS et cookie Secure doivent être imposés dans le déploiement de production.
- Pas de limitation de tentatives de connexion, récupération de mot de passe, audit de sécurité exhaustif ou verrouillage concurrent ajouté dans ce lot.
- Avant d'introduire des transitions maker/paiement concurrentes, ajouter un verrouillage adapté et des tests de concurrence.

## 11. Suite proposée, après réception de l'énoncé

Valider les règles et le périmètre par séance, préciser les devises et transitions, remplacer les autres maquettes par des données réelles seulement pour les fonctionnalités requises, documenter chaque nouvelle API et compléter les tests d'intégration. Les GLB déjà présents sont suffisants pour l'instant ; animations supplémentaires et modèles Tripo3D ne doivent pas retarder la logique métier ou l'accessibilité.

## 12. Recette supplémentaire du lot 2

1. Compte demandeur sans demande : dashboard à zéro, panneau vide, aucun faux projet.
2. Créer six demandes : total 6, cinq demandes récentes uniquement, page de liste contenant les six demandes.
3. Annuler une demande nouvelle : recharger le dashboard, total inchangé, nouvelles -1, annulées +1.
4. Cliquer une demande récente : détail correspondant, pas de première demande choisie arbitrairement.
5. Couper le réseau : pas de faux zéros ; message d'erreur et bouton Réessayer.
6. Tester la connexion, l'inscription et la déconnexion avec les formulaires historiques : jeton CSRF envoyé ; échec explicite si le serveur refuse.
7. Envoyer ces trois POST sans jeton depuis un client HTTP : 403 attendu.
8. Suspendre le compte dans une base de test, puis appeler `/api/auth/me` : 401 et session invalidée. Répéter avec un rôle révoqué ou un utilisateur supprimé.
9. Utiliser un prénom contenant `<img src=x onerror=alert(1)>` en base de test : le header et le dashboard doivent afficher du texte, pas exécuter du HTML.
10. Tester mobile, navigation clavier et préférence de réduction des animations.

Ces scénarios HTTP et navigateur restent à exécuter sur une instance Java/MySQL. Aucun serveur statique présenté comme application fonctionnelle n'a été lancé à leur place.

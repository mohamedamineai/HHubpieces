# Hubpieces

Plateforme de réparation par impression 3D : Spring Boot, Java 17, MySQL et interfaces HTML/CSS/JS modernes.

## 🎯 Fonctionnalités (Séances 3 et 4)

### Séance 3 — Modélisation JPA/Hibernate ✅
- 13 entités JPA mappées (Utilisateur, Role, Demande, FichierStl, OffreMaker, Transaction, Litige, Avis, Message, Notification, PhotoDemande, ProfilMaker, ProfilExpertCao), plus les énumérations métier
- Relations : @OneToOne, @ManyToOne, @OneToMany, @ManyToMany
- Configuration Hibernate + MySQL fonctionnelle

### Séance 4 — CRUD + API REST ✅
- 7 contrôleurs REST (Auth, Demandeur, Maker, ExpertCao, Admin, Catalogue public, Utilisateur)
- 11 services métier
- 21 DTOs pour isoler la couche métier
- Validation des transitions d'état
- Gestion des rôles (DEMANDEUR, MAKER, EXPERT_CAO, ADMIN)
- Documentation Swagger/OpenAPI complète
- 4 acteurs, ~70 endpoints

## 👥 Les 4 acteurs

### 🔵 DEMANDEUR
- Crée des demandes de réparation avec description, photos, STL
- Reçoit des offres des makers et choisit la meilleure
- Valide la pièce reçue, dépose un avis
- Peut ouvrir un litige en cas de problème
- Messagerie intégrée avec les makers

### 🖨️ MAKER
- Voit les demandes disponibles près de chez lui
- Dépose des offres (prix, délai, mode de livraison)
- Gère ses commandes (lancer impression, terminer)
- Profil avec rayon d'action, matériaux, description imprimante
- Reçoit des avis et développe sa réputation

### 🎨 EXPERT_CAO
- Traite les demandes réellement en attente de modélisation
- Téléverse un fichier de fabrication STL/OBJ/3MF/STEP (50 Mo max)
- Suit l'état de modération : en attente, validé ou rejeté
- Une demande ne passe aux makers qu'après validation de l'admin
- Gestion du catalogue STL validé et profil CAO

### 🛡️ ADMIN
- Statistiques et opérations réellement chargées depuis l'API
- Active ou suspend les comptes (une suspension bloque aussi les sessions ouvertes)
- Valide/rejette les fichiers CAO avec motif
- Prend en charge et clôture les litiges avec libération ou remboursement
- Consulte les transactions et montants en séquestre

## 🚀 Démarrage

### Prérequis
- JDK 17+
- MySQL 8 (port 3306, base `hubpieces_db`)
- Accès aux dépôts Maven

### Configuration
Les variables d'environnement suivantes peuvent être définies :
- `SPRING_DATASOURCE_URL` (défaut : `jdbc:mysql://localhost:3306/hubpieces_db`)
- `SPRING_DATASOURCE_USERNAME` (défaut : `root`)
- `SPRING_DATASOURCE_PASSWORD` (défaut : vide)

### Lancement
```sh
bash mvnw spring-boot:run
```

Le serveur démarre sur le port **9090**.

### URLs importantes
- **Accueil** : http://localhost:9090/ (ou http://localhost:9090/index.html)
- **Swagger UI** : http://localhost:9090/swagger-ui.html
- **OpenAPI JSON** : http://localhost:9090/v3/api-docs
- **Connexion** : http://localhost:9090/login (ou http://localhost:9090/login.html)
- **Inscription** : http://localhost:9090/register (ou http://localhost:9090/register.html)
- **Catalogue** : http://localhost:9090/catalogue (ou http://localhost:9090/catalogue.html)

> Les URLs sans extension (`/`, `/login`, `/register`, `/catalogue`) sont gérées par
> `WebMvcConfig#addViewControllers` : elles ouvrent toujours les vraies pages du site,
> jamais le formulaire par défaut de Spring Security.

### Comptes de test
| Role | Email | Mot de passe |
|------|-------|--------------|
| ADMIN | `admin@hubpieces.fr` | `admin123` |
| DEMANDEUR | `demandeur@hubpieces.fr` | `demo1234` |
| MAKER | `maker@hubpieces.fr` | `demo1234` |
| EXPERT_CAO | `expert@hubpieces.fr` | `demo1234` |

## 📚 Documentation

- [Documentation API REST complète](docs/API-REST-DOCUMENTATION.md) - Liste des endpoints, payloads, exemples Postman
- [Diagramme de classes Mermaid](docs/DIAGRAMME-CLASSES-MERMAID.md) - PK, FK, cardinalités, agrégation/composition et explication
- [Inventaire des modèles 3D](docs/PROMPT-TRIPO3D.md) - fichiers réellement présents et règles de placement par rôle
- [Prompts Tripo3D complémentaires](docs/PROMPT-TRIPO3D-MANQUANTS.md) - modèles optionnels; le dépôt ne contient pas encore ces six fichiers

## 🧪 Tests

```sh
# Tests d'intégration (H2 en mémoire, sans MySQL)
bash mvnw test
```

Les tests automatisés vérifient le cycle de validation d'une modélisation CAO. Pour lancer l'application, la base MySQL configurée dans `application.properties` reste nécessaire.

## 🛠️ Stack technique

- **Backend** : Spring Boot 4.0.8, Spring Data JPA, Spring Security
- **Base de données** : MySQL 8 (Hibernate)
- **Build** : Maven
- **Documentation API** : springdoc-openapi (Swagger UI)
- **Frontend** : Bootstrap 5, Font Awesome 6, Model-Viewer 3D, AOS animations
- **Java** : 17

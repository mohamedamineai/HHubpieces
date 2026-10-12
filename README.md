# Hubpieces

Plateforme de réparation par impression 3D : Spring Boot, Java 17, MySQL et interfaces HTML/CSS/JS modernes.

## 🎯 Fonctionnalités (Séances 3 et 4)

### Séance 3 — Modélisation JPA/Hibernate ✅
- 22 entités JPA mappées (Utilisateur, Demande, OffreMaker, Transaction, Litige, Avis, Message, Notification, FichierStl, PhotoDemande, ProfilMaker, ProfilExpertCao, Role)
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
- Modélise les pièces en 3D à partir des demandes
- Upload de fichiers STL/OBJ/3MF/STEP
- Gestion du catalogue STL validé
- Profil avec logiciels maîtrisés, portfolio

### 🛡️ ADMIN
- Vue d'ensemble de la plateforme (statistiques)
- Gestion des utilisateurs (activation, suspension, suppression)
- Validation/rejet des fichiers STL soumis
- Résolution des litiges
- Accès à toutes les transactions

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
- **Accueil** : http://localhost:9090/index.html
- **Swagger UI** : http://localhost:9090/swagger-ui.html
- **OpenAPI JSON** : http://localhost:9090/v3/api-docs
- **Connexion** : http://localhost:9090/login.html
- **Inscription** : http://localhost:9090/register.html

### Comptes de test
| Role | Email | Mot de passe |
|------|-------|--------------|
| ADMIN | `admin@hubpieces.fr` | `admin123` |
| DEMANDEUR | `demandeur@hubpieces.fr` | `demo1234` |
| MAKER | `maker@hubpieces.fr` | `demo1234` |
| EXPERT_CAO | `expert@hubpieces.fr` | `demo1234` |

## 📚 Documentation

- [Documentation API REST complète](docs/API-REST-DOCUMENTATION.md) - Liste des endpoints, payloads, exemples Postman
- [Prompt Tripo3D](docs/PROMPT-TRIPO3D.md) - Pour générer des modèles 3D custom

## 🧪 Tests

```sh
# Tests unitaires (sans MySQL)
bash mvnw -Dtest=DemandeServiceTest,AuthSessionTest test

# Tests complets (nécessite MySQL)
bash mvnw test
```

## 🛠️ Stack technique

- **Backend** : Spring Boot 4.0.8, Spring Data JPA, Spring Security
- **Base de données** : MySQL 8 (Hibernate)
- **Build** : Maven
- **Documentation API** : springdoc-openapi (Swagger UI)
- **Frontend** : Bootstrap 5, Font Awesome 6, Model-Viewer 3D, AOS animations
- **Java** : 17

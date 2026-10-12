# 🚀 Démarrage rapide Hubpieces

## Prérequis
- JDK 17+
- MySQL 8 actif sur le port 3306
- Maven (ou utiliser `mvnw` qui télécharge Maven automatiquement)

## Étape 1 : Préparer MySQL
```sh
mysql -uroot -e "CREATE DATABASE IF NOT EXISTS hubpieces_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
```

## Étape 2 : Configurer les credentials (optionnel)
Par défaut, l'app utilise `root` sans mot de passe. Si votre MySQL a un mot de passe :
```sh
export SPRING_DATASOURCE_USERNAME=root
export SPRING_DATASOURCE_PASSWORD=votre_mot_de_passe
```

## Étape 3 : Démarrer l'application
```sh
bash mvnw spring-boot:run
```

L'application démarre sur **http://localhost:9090**

## Étape 4 : Tester

### Comptes de test
- **Admin** : `admin@hubpieces.fr` / `admin123`
- **Demandeur** : `demandeur@hubpieces.fr` / `demo1234`
- **Maker** : `maker@hubpieces.fr` / `demo1234`
- **Expert CAO** : `expert@hubpieces.fr` / `demo1234`

### URLs
| Page | URL |
|------|-----|
| 🏠 Accueil | http://localhost:9090/ *(= /index.html)* |
| 🔐 Connexion | http://localhost:9090/login *(= /login.html)* |
| 📝 Inscription | http://localhost:9090/register *(= /register.html)* |
| 🖼️ Catalogue public | http://localhost:9090/catalogue *(= /catalogue.html)* |
| 📚 Swagger UI | http://localhost:9090/swagger-ui.html |
| 📋 OpenAPI | http://localhost:9090/v3/api-docs |
| 👤 Profil public | http://localhost:9090/profil-public.html?id=X |
| 📊 Dashboard demandeur | http://localhost:9090/dashboard-demandeur.html |
| 🖨️ Dashboard maker | http://localhost:9090/dashboard-maker.html |
| 🧊 Dashboard expert CAO | http://localhost:9090/dashboard-expertcao.html |
| 🛡️ Dashboard admin | http://localhost:9090/dashboard-admin.html |

> Les URLs sans extension sont des redirections/forward configurés dans `WebMvcConfig`.

### Test rapide avec Postman
1. Importez la collection : `docs/postman-collection.json`
2. Lancez la requête "Login (Admin)" (ou un autre role)
3. Toutes les requêtes suivantes garderont le cookie de session

## Scénario complet de test

### 1. En tant que DEMANDEUR
1. Connectez-vous avec le compte demandeur
2. Allez sur "Mes demandes" > "Nouvelle demande"
3. Créez une demande "Poignée de lave-linge"
4. L'état devient `MODELISATION_REQUISE`

### 2. En tant qu'EXPERT_CAO
1. Connectez-vous avec le compte expert
2. Allez sur "Mes modélisations"
3. Cliquez sur "Modéliser" la demande
4. Créez un fichier STL (chemin : `/uploads/stl/test.stl`)
5. La demande passe à `RECHERCHE_MAKER`

### 3. En tant que MAKER
1. Connectez-vous avec le compte maker
2. Allez sur "Mes offres" > onglet "Demandes disponibles"
3. Cliquez sur "Faire une offre" sur la demande
4. Renseignez prix, délai
5. L'offre est créée

### 4. En tant que DEMANDEUR (à nouveau)
1. Retournez sur votre demande
2. Vous voyez l'offre reçue
3. Cliquez sur "Accepter"
4. L'état passe à `EN_COURS_IMPRESSION` et une transaction est créée

### 5. En tant que MAKER (à nouveau)
1. Allez sur "Mes commandes"
2. Cliquez sur "Lancer l'impression"
3. Quand c'est fini, cliquez sur "Terminer"
4. L'état passe à `ATTENTE_VALIDATION`

### 6. En tant que DEMANDEUR
1. Validez la livraison
2. Déposez un avis
3. La transaction est libérée

## Résolution des problèmes

### "Communications link failure"
→ MySQL n'est pas démarré. Lancez : `sudo systemctl start mysql` (ou `brew services start mysql` sur Mac)

### "Access denied for user 'root'"
→ Configurez `SPRING_DATASOURCE_PASSWORD` avec votre mot de passe

### "Unknown database 'hubpieces_db'"
→ Créez la base : `mysql -uroot -e "CREATE DATABASE hubpieces_db;"`

### L'app démarre mais le `DataLoader` ne crée pas les utilisateurs
→ Supprimez toutes les tables et relancez : 
```sh
mysql -uroot -e "DROP DATABASE hubpieces_db; CREATE DATABASE hubpieces_db CHARACTER SET utf8mb4;"
```

## Logs

Pour voir les requêtes SQL :
```sh
logging.level.org.hibernate.SQL=DEBUG
```

Pour voir plus de détails sur le démarrage :
```sh
logging.level.org.springframework=INFO
```

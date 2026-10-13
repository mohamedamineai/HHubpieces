# Hubpieces - Documentation API REST

## 🌐 URLs

Après le démarrage de Spring Boot sur le port 9090 :

- **Application** : `http://localhost:9090`
- **Swagger UI** : `http://localhost:9090/swagger-ui.html`
- **OpenAPI JSON** : `http://localhost:9090/v3/api-docs`
- **OpenAPI YAML** : `http://localhost:9090/v3/api-docs.yaml`
- **Index** : `http://localhost:9090/index.html`

Swagger utilise une URL de serveur relative : « Try it out » appelle le même hôte que l'interface (local ou prévisualisation), pas un `localhost` codé en dur.

## 🔐 Authentification

L'API utilise une **session HTTP** (cookie `JSESSIONID`), pas un jeton Bearer. Pour tester avec Swagger UI, exécutez d'abord `POST /api/auth/login`, puis continuez dans le même navigateur/hôte : le navigateur conserve le cookie de session. Pour Postman, activez la gestion des cookies (« Send cookies » / Cookie Jar), appelez le login du rôle souhaité, puis exécutez les requêtes de ce rôle avant de vous connecter avec un autre compte.

- **401** : aucune session valide (ou compte suspendu / en attente).
- **403** : session valide, mais mauvais rôle ou ressource appartenant à un autre acteur.
- **404** : ressource absente ou fichier non publié dans le catalogue public.
- **400** : état métier ou payload invalide.

Les comptes suspendus sont refusés au login et une suspension révoque leur accès dès la requête suivante.

### Comptes de test (créés automatiquement par DataLoader)

| Role | Email | Mot de passe |
|------|-------|--------------|
| ADMIN | `admin@hubpieces.fr` | `admin123` |
| DEMANDEUR | `demandeur@hubpieces.fr` | `demo1234` |
| MAKER | `maker@hubpieces.fr` | `demo1234` |
| EXPERT_CAO | `expert@hubpieces.fr` | `demo1234` |

## 📚 Endpoints par rôle

### 🔓 Authentification (`/api/auth`)

| Méthode | URL | Description |
|---------|-----|-------------|
| POST | `/api/auth/register` | Inscription DEMANDEUR, MAKER ou EXPERT_CAO (ADMIN non public) |
| POST | `/api/auth/login` | Connexion |
| POST | `/api/auth/logout` | Déconnexion |
| GET | `/api/auth/me` | Infos utilisateur courant |

### 👤 Utilisateurs (`/api/utilisateurs`)

| Méthode | URL | Description | Rôle requis |
|---------|-----|-------------|-------------|
| GET | `/api/utilisateurs/me` | Mon profil complet | Tous |
| PUT | `/api/utilisateurs/me` | Modifier mon profil | Tous |
| GET | `/api/utilisateurs/{id}` | Profil public d'un utilisateur | Public |

### 🛒 Demandeur (`/api/demandeur`)

| Méthode | URL | Description |
|---------|-----|-------------|
| **Demandes** | | |
| GET | `/api/demandeur/demandes` | Liste mes demandes |
| POST | `/api/demandeur/demandes` | Créer une demande |
| GET | `/api/demandeur/demandes/{id}` | Détail d'une demande |
| PUT | `/api/demandeur/demandes/{id}` | Modifier ma demande |
| DELETE | `/api/demandeur/demandes/{id}` | Annuler ma demande |
| POST | `/api/demandeur/demandes/{id}/cloturer` | Valider et clôturer |
| **Offres** | | |
| GET | `/api/demandeur/demandes/{id}/offres` | Offres d'une demande |
| POST | `/api/demandeur/demandes/{id}/offres/{offreId}/accepter` | Accepter offre |
| POST | `/api/demandeur/demandes/{id}/offres/{offreId}/refuser` | Refuser offre |
| **Messagerie** | | |
| GET | `/api/demandeur/messages` | Mes messages |
| POST | `/api/demandeur/messages` | Envoyer message |
| POST | `/api/demandeur/messages/{id}/lu` | Marquer comme lu |
| **Avis** | | |
| POST | `/api/demandeur/avis` | Déposer un avis |
| **Litiges** | | |
| POST | `/api/demandeur/litiges` | Ouvrir un litige |
| **Notifications** | | |
| GET | `/api/demandeur/notifications` | Mes notifications |
| POST | `/api/demandeur/notifications/{id}/lu` | Marquer lue |
| POST | `/api/demandeur/notifications/toutes-lues` | Tout marquer lu |
| **Stats** | | |
| GET | `/api/demandeur/stats` | Stats personnelles |

### 🖨️ Maker (`/api/maker`)

| Méthode | URL | Description |
|---------|-----|-------------|
| GET | `/api/maker/demandes-disponibles` | Demandes où je peux faire une offre |
| POST | `/api/maker/offres` | Déposer une offre |
| GET | `/api/maker/offres` | Mes offres |
| PUT | `/api/maker/offres/{id}` | Modifier mon offre |
| DELETE | `/api/maker/offres/{id}` | Annuler mon offre |
| GET | `/api/maker/commandes` | Offres acceptées (commandes) |
| POST | `/api/maker/commandes/{offreId}/lancer-impression` | Démarrer impression |
| POST | `/api/maker/commandes/{offreId}/terminer` | Marquer terminé |
| GET | `/api/maker/profil` | Mon profil maker |
| PUT | `/api/maker/profil` | Modifier mon profil maker |
| GET | `/api/maker/avis` | Avis reçus |
| GET | `/api/maker/messages` | Mes messages |
| POST | `/api/maker/messages` | Envoyer message |
| GET | `/api/maker/notifications` | Mes notifications |
| POST | `/api/maker/notifications/{id}/lu` | Marquer une notification comme lue |
| GET | `/api/maker/stats` | Stats |

### 🎨 Expert CAO (`/api/expert-cao`)

| Méthode | URL | Description |
|---------|-----|-------------|
| GET | `/api/expert-cao/demandes-a-modeliser` | Demandes nécessitant modélisation |
| POST | `/api/expert-cao/demandes/{id}/modelisation` | Soumettre modélisation |
| POST | `/api/expert-cao/demandes/{id}/modelisation/upload` | Upload STL + modéliser |
| GET | `/api/expert-cao/fichiers-stl` | Mes fichiers STL |
| GET | `/api/expert-cao/fichiers-stl/valides` | Tous les fichiers validés |
| POST | `/api/expert-cao/fichiers-stl` | Créer fichier STL |
| PUT | `/api/expert-cao/fichiers-stl/{id}` | Modifier mon fichier |
| DELETE | `/api/expert-cao/fichiers-stl/{id}` | Supprimer mon fichier |
| GET | `/api/expert-cao/profil` | Mon profil expert |
| PUT | `/api/expert-cao/profil` | Modifier profil |
| GET | `/api/expert-cao/notifications` | Mes notifications |
| GET | `/api/expert-cao/stats` | Stats |

### 🛡️ Admin (`/api/admin`)

| Méthode | URL | Description |
|---------|-----|-------------|
| GET | `/api/admin/utilisateurs` | Liste tous les utilisateurs |
| GET | `/api/admin/utilisateurs/{id}` | Détail utilisateur |
| PUT | `/api/admin/utilisateurs/{id}/statut?statut=ACTIF` | Activer/suspendre |
| DELETE | `/api/admin/utilisateurs/{id}` | Supprimer utilisateur |
| GET | `/api/admin/demandes` | Toutes les demandes |
| GET | `/api/admin/litiges` | Tous les litiges |
| GET | `/api/admin/litiges/ouverts` | Litiges en cours |
| PUT | `/api/admin/litiges/{id}/resoudre` | Résoudre un litige |
| GET | `/api/admin/transactions` | Toutes les transactions |
| GET | `/api/admin/fichiers-stl/en-attente` | STL à valider |
| PUT | `/api/admin/fichiers-stl/{id}/valider` | Valider un STL |
| PUT | `/api/admin/fichiers-stl/{id}/rejeter?motif=...` | Rejeter un STL |
| GET | `/api/admin/stats` | Statistiques globales |

> La suppression physique d'un utilisateur est refusée en `409 Conflict` si l'historique métier le référence; dans ce cas, utilisez la suspension pour préserver commandes, avis, messages et transactions.

### 🌍 Public (`/api/public`)

| Méthode | URL | Description |
|---------|-----|-------------|
| GET | `/api/public/catalogue-stl?categorie=...&marque=...` | Catalogue STL validé |
| GET | `/api/public/catalogue-stl/{id}` | Détail d'un STL |
| POST | `/api/public/catalogue-stl/{id}/telecharger` | Incrémenter téléchargement |
| GET | `/api/public/demandes-recentes` | Demandes publiques récentes |
| GET | `/api/public/makers` | Liste des makers |
| GET | `/api/public/experts-cao` | Liste des experts CAO |
| GET | `/api/public/utilisateurs/{id}` | Profil public |

## 📦 Exemples de payloads

### Inscription (DEMANDEUR)
```json
POST /api/auth/register
{
  "nom": "Dupont",
  "prenom": "Jean",
  "email": "jean.dupont@example.com",
  "motDePasse": "motdepasse123",
  "telephone": "0612345678",
  "ville": "Paris",
  "codePostal": "75001",
  "role": "DEMANDEUR"
}
```

### Inscription (MAKER)
```json
POST /api/auth/register
{
  "nom": "Benali",
  "prenom": "Ahmed",
  "email": "ahmed@example.com",
  "motDePasse": "motdepasse123",
  "ville": "Marseille",
  "codePostal": "13001",
  "role": "MAKER",
  "rayonActionKm": 30,
  "materiauxDisponibles": "PLA, ABS, PETG",
  "descriptionImprimante": "Prusa MK3S+"
}
```

### Créer une demande
```json
POST /api/demandeur/demandes
{
  "titre": "Poignée de lave-linge Samsung cassée",
  "description": "La poignée de mon lave-linge Samsung EcoBubble s'est cassée net. Besoin d'une pièce solide.",
  "marque": "Samsung",
  "modeleAppareil": "EcoBubble WW80J",
  "materiauSouhaite": "ABS",
  "budgetMax": 20.00,
  "ville": "Lyon",
  "codePostal": "69001"
}
```

### Déposer une offre
```json
POST /api/maker/offres
{
  "demandeId": 1,
  "prix": 12.50,
  "delaiJours": 3,
  "message": "Je peux imprimer cette pièce en ABS solide, prêt en 3 jours.",
  "modeLivraison": "REMISE_EN_MAIN"
}
```

### Soumettre une modélisation CAO
```json
POST /api/expert-cao/demandes/{id}/modelisation
{
  "nom": "Engrenage denté précision",
  "description": "Exemple de référence locale déjà présente dans le dépôt",
  "cheminFichier": "images/model1.glb",
  "categorie": "electromenager",
  "marque": "Moulinex",
  "modeleAppareil": "Masterchef 5000",
  "materiauRecommande": "PETG",
  "dimensions": "À mesurer avant fabrication"
}
```

> Pour l'interface Expert CAO, utilisez de préférence `POST /api/expert-cao/demandes/{id}/modelisation/upload` avec un vrai fichier `multipart/form-data`. L'endpoint JSON ci-dessus ne téléverse pas le contenu local : il accepte seulement un chemin sous `images/` ou `/uploads/stl/` dont l'existence est vérifiée côté serveur.

## 🧪 Test avec Postman

1. **Importer une collection** : dans Postman, faites `Import` et choisissez [`postman-collection.json`](./postman-collection.json). Vous pouvez aussi importer l'URL `http://localhost:9090/v3/api-docs` après démarrage pour générer des requêtes à partir de l'OpenAPI.
2. **Tester manuellement** : appelez `POST /api/auth/login` avec `email` et `motDePasse`; gardez le Cookie Jar actif pour réutiliser `JSESSIONID`. Utilisez un compte par rôle et connectez-vous à nouveau pour tester un autre acteur.
3. **Tests automatisés** :
   ```bash
   bash mvnw test
   ```

## 🔄 Cycle de vie métier — entre les quatre acteurs

```text
Demandeur crée une demande sans modèle
  → MODELISATION_REQUISE
  → Expert CAO transmet un vrai fichier STL / OBJ / 3MF / STEP
  → FichierStl = EN_ATTENTE, la demande reste MODELISATION_REQUISE
  → Admin valide : FichierStl = VALIDE, demande = RECHERCHE_MAKER
  → Maker(s) déposent une ou plusieurs offres
  → Demandeur accepte une offre : offre = ACCEPTEE, les autres = REFUSEE
  → Demande = ATTENTE_IMPRESSION + Transaction = EN_SEQUESTRE
  → Maker lance : demande = EN_COURS_IMPRESSION
  → Maker termine : demande = ATTENTE_VALIDATION
  → Demandeur confirme la réception : demande = CLOTUREE, transaction = LIBERE
```

Si l'admin rejette le fichier, la demande reste `MODELISATION_REQUISE`, le fichier rejeté est détaché et la demande retourne à la file de l'expert. Un maker ne peut faire une offre qu'en `RECHERCHE_MAKER`; seule l'offre acceptée peut faire avancer la commande. Une annulation est possible avant acceptation d'une offre. Après acceptation, l'annulation passe par le traitement d'un litige afin de ne pas laisser un paiement en séquestre sans décision.

### Vérification manuelle du flux Expert CAO → Admin

1. Connectez-vous comme `expert@hubpieces.fr` et appelez `GET /api/expert-cao/demandes-a-modeliser`.
2. Choisissez l'ID retourné, puis envoyez un fichier via `POST /api/expert-cao/demandes/{id}/modelisation/upload` (`multipart/form-data`, champ `file` obligatoire; métadonnées `nom`, `description`, `categorie`, `tags`, `marque`, `modeleAppareil`, `materiauRecommande`, `dimensions` facultatives).
3. Vérifiez que le fichier apparaît dans `GET /api/expert-cao/fichiers-stl` avec `etatValidation=EN_ATTENTE`. La demande ne doit pas apparaître à nouveau dans la file CAO et n'est pas encore proposée aux makers.
4. Connectez-vous comme `admin@hubpieces.fr`, ouvrez `GET /api/admin/fichiers-stl/en-attente`, puis appelez `PUT /api/admin/fichiers-stl/{id}/valider`.
5. La réponse de la demande passe à `RECHERCHE_MAKER`; vérifiez avec `GET /api/admin/demandes` (ou l'écran maker). En cas de `.../rejeter?motif=...`, la demande reste en CAO et l'expert reçoit le motif par notification.
6. Testez `PUT /api/admin/utilisateurs/{id}/statut?statut=SUSPENDU`: l'utilisateur suspendu ne peut plus se connecter ni utiliser une session déjà ouverte.

## 💰 Système de paiement (séquestre)

Quand une offre est acceptée :
1. Une **Transaction** est créée
2. Le montant est en **séquestre** (`EN_SEQUESTRE`)
3. Quand le demandeur clôture la demande, le montant est **libéré** :
   - 84% au maker
   - 8% à l'expert CAO (si concerné)
   - 8% de commission plateforme
4. En cas de litige, l'admin arbitre et peut rembourser ou libérer

## 📂 Fichiers uploadés

Les fichiers STL uploadés sont stockés dans `uploads/stl/`.
Formats acceptés : `.stl`, `.obj`, `.3mf`, `.step`, `.stp`
Taille max : 50 MB

## 🧪 Smoke tests (Swagger, curl et tests automatisés)

### Swagger UI

Ouvrez **http://localhost:9090/swagger-ui.html**. Dans la section `Authentification`, faites `Try it out` sur `POST /api/auth/login` avec un des comptes ci-dessus. Le cookie de session est ensuite utilisé automatiquement pour les requêtes lancées depuis ce même navigateur. Essayez notamment les sections **Expert CAO** et **Administrateur**. Le JSON brut est disponible sur **http://localhost:9090/v3/api-docs**.

### Séquence curl — expert puis admin

Les IDs de demandes/fichiers sont générés par la base : récupérez-les dans les listes, ne supposez pas qu'ils valent 1 ou 4.

```bash
# 1. Session de l'expert
curl -i -c /tmp/hubpieces-expert.cookies \
  -H 'Content-Type: application/json' \
  -d '{"email":"expert@hubpieces.fr","motDePasse":"demo1234"}' \
  http://localhost:9090/api/auth/login

# 2. File CAO (repérer l'id d'une demande MODELISATION_REQUISE)
curl -b /tmp/hubpieces-expert.cookies \
  http://localhost:9090/api/expert-cao/demandes-a-modeliser

# 3. Téléverser un fichier réel (remplacer 42 et le chemin du fichier)
curl -b /tmp/hubpieces-expert.cookies -X POST \
  -F 'file=@./modele.stl' \
  -F 'nom=Support de remplacement' \
  -F 'categorie=electromenager' \
  -F 'description=Modèle CAO avec tolérances vérifiées' \
  -F 'marque=Dyson' \
  -F 'modeleAppareil=V8 Absolute' \
  -F 'materiauRecommande=PETG' \
  -F 'dimensions=80x40x12mm' \
  http://localhost:9090/api/expert-cao/demandes/42/modelisation/upload

# 4. Contrôler que le fichier est EN_ATTENTE côté expert
curl -b /tmp/hubpieces-expert.cookies http://localhost:9090/api/expert-cao/fichiers-stl

# 5. Nouvelle session d'admin (cookie séparé)
curl -i -c /tmp/hubpieces-admin.cookies \
  -H 'Content-Type: application/json' \
  -d '{"email":"admin@hubpieces.fr","motDePasse":"admin123"}' \
  http://localhost:9090/api/auth/login

# 6. Récupérer l'id du fichier en attente, puis valider (remplacer 17)
curl -b /tmp/hubpieces-admin.cookies http://localhost:9090/api/admin/fichiers-stl/en-attente
curl -b /tmp/hubpieces-admin.cookies -X PUT \
  http://localhost:9090/api/admin/fichiers-stl/17/valider
```

Le téléversement utilise `multipart/form-data`; **ne fixez pas vous-même l'en-tête `Content-Type`** car curl/Postman doit y ajouter la frontière multipart. Les mêmes opérations sont directement testables dans Swagger UI.

### Collection Postman

Importez [`postman-collection.json`](./postman-collection.json), changez `baseUrl` si nécessaire, et gardez l'option de cookie activée. La collection contient les dossiers par acteur; exécutez le login de l'acteur concerné avant ses opérations. Pour l'upload, ajoutez un vrai fichier `.stl`, `.obj`, `.3mf`, `.step` ou `.stp` dans le champ `file` de type *form-data*.

### Tests automatisés

```bash
# Tests d'intégration sur H2 en mémoire, sans serveur MySQL
bash mvnw test
```

Les tests automatisés vérifient le démarrage avec les comptes de démonstration, le stockage multipart d'un fichier, la mise en attente de validation du modèle CAO, le passage à la recherche maker après validation admin, le retour dans la file CAO après rejet et le séquestre lors de l'acceptation d'une offre. Ils utilisent H2 en mémoire; le serveur de l'application utilise toujours la configuration MySQL de `application.properties`. L'exécution reste à confirmer sur un environnement disposant d'un JDK et de Maven.

> **Migration base déjà créée** : le statut `ATTENTE_IMPRESSION` a été ajouté pour séparer « offre acceptée / paiement en séquestre » de « impression réellement démarrée ». Sur une base MySQL existante dont `demande.etat` est un `ENUM`, exécutez une fois :
> ```sql
> ALTER TABLE demande MODIFY etat ENUM(
>   'NOUVELLE','MODELISATION_REQUISE','RECHERCHE_MAKER','ATTENTE_IMPRESSION',
>   'EN_COURS_IMPRESSION','ATTENTE_VALIDATION','ANNULEE','CLOTUREE'
> ) DEFAULT 'NOUVELLE';
> ```
> Sur une base neuve, le schéma fourni dans `src/main/resources/static/DB/hubpieces_db.sql` inclut déjà cette valeur.

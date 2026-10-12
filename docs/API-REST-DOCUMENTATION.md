# Hubpieces - Documentation API REST

## 🌐 URLs

- **Application** : `http://localhost:9090`
- **Swagger UI** : `http://localhost:9090/swagger-ui.html`
- **OpenAPI JSON** : `http://localhost:9090/v3/api-docs`
- **Index** : `http://localhost:9090/index.html`

## 🔐 Authentification

L'API utilise une **session HTTP** (cookie `JSESSIONID`). Pour tester avec Postman :

1. Activez **"Send cookies"** dans Postman
2. Appelez `POST /api/auth/login` pour obtenir la session
3. Toutes les requêtes suivantes enverront automatiquement le cookie

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
| POST | `/api/auth/register` | Inscription (tous rôles) |
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
POST /api/expert-cao/demandes/1/modelisation
{
  "nom": "poignee-samsung-ecobubble-v2",
  "description": "Poignée robuste renforcée",
  "cheminFichier": "/uploads/stl/poignee-v2.stl",
  "categorie": "electromenager",
  "marque": "Samsung",
  "modeleAppareil": "EcoBubble WW80J",
  "materiauRecommande": "ABS",
  "dimensions": "120x40x25mm"
}
```

## 🧪 Test avec Postman

1. **Importer** la collection Swagger :
   - Ouvrir Swagger UI : http://localhost:9090/swagger-ui.html
   - Cliquer sur "Export" > "Download OpenAPI 3" (ou `/v3/api-docs`)
   - Dans Postman : File > Import > coller le JSON

2. **Tester manuellement** :
   - Login : `POST /api/auth/login` avec email/motDePasse
   - Postman conserve automatiquement le cookie JSESSIONID
   - Toutes les requêtes suivantes sont authentifiées

3. **Tests automatisés** :
   ```bash
   bash mvnw test
   ```

## 🔄 Cycle de vie d'une demande

```
NOUVELLE / MODELISATION_REQUISE
        ↓ (Expert CAO)
RECHERCHE_MAKER
        ↓ (Maker dépose offre)
RECHERCHE_MAKER
        ↓ (Demandeur accepte offre)
EN_COURS_IMPRESSION
        ↓ (Maker lance l'impression)
EN_COURS_IMPRESSION
        ↓ (Maker termine)
ATTENTE_VALIDATION
        ↓ (Demandeur valide)
CLOTUREE
```

À tout moment, le demandeur peut annuler (avant EN_COURS_IMPRESSION).
Un litige peut être ouvert par le demandeur.

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

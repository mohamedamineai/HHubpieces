# Plan d'implémentation Hubpieces - Séances 3 et 4

## 🎯 Objectifs Cahier des charges

### Séance 3 — Modélisation JPA/Hibernate
- ✅ 13 entités JPA et 10 énumérations métier déjà présentes (Utilisateur, Demande, OffreMaker, Transaction, etc.)
- ✅ Mappings : @OneToOne, @ManyToOne, @ManyToMany
- ✅ MySQL + ddl-auto=update

### Séance 4 — CRUD + API REST
À implémenter :
- Repositories manquants (Demande, Offre, FichierStl, Transaction, Avis, Litige, Message, Notification, PhotoDemande)
- Services métier
- REST Controllers par acteur avec DTOs
- Swagger / OpenAPI
- Exception handler global
- Upload fichiers STL/images
- Tests Postman

## 👥 4 Acteurs et leurs endpoints

### 1. DEMANDEUR
- POST /api/demandeur/demandes
- GET /api/demandeur/demandes
- GET /api/demandeur/demandes/{id}
- PUT /api/demandeur/demandes/{id}
- DELETE /api/demandeur/demandes/{id}
- GET /api/demandeur/demandes/{id}/offres
- POST /api/demandeur/demandes/{id}/offres/{offreId}/accepter
- POST /api/demandeur/demandes/{id}/offres/{offreId}/refuser
- POST /api/demandeur/messages
- GET /api/demandeur/messages
- POST /api/demandeur/avis
- POST /api/demandeur/litiges
- GET /api/demandeur/notifications

### 2. MAKER
- GET /api/maker/demandes-disponibles
- POST /api/maker/offres
- GET /api/maker/offres
- PUT /api/maker/offres/{id}
- DELETE /api/maker/offres/{id}
- GET /api/maker/commandes
- PUT /api/maker/commandes/{id}/statut
- GET /api/maker/profil
- PUT /api/maker/profil
- GET /api/maker/avis

### 3. EXPERT_CAO
- GET /api/expert-cao/demandes-a-modeliser
- POST /api/expert-cao/demandes/{id}/modelisation
- GET /api/expert-cao/fichiers-stl
- POST /api/expert-cao/fichiers-stl
- PUT /api/expert-cao/fichiers-stl/{id}
- DELETE /api/expert-cao/fichiers-stl/{id}
- GET /api/expert-cao/profil
- PUT /api/expert-cao/profil

### 4. ADMIN
- GET /api/admin/utilisateurs
- GET /api/admin/utilisateurs/{id}
- PUT /api/admin/utilisateurs/{id}/statut
- DELETE /api/admin/utilisateurs/{id}
- GET /api/admin/demandes
- GET /api/admin/litiges
- PUT /api/admin/litiges/{id}/resoudre
- GET /api/admin/transactions
- GET /api/admin/fichiers-stl/en-attente
- PUT /api/admin/fichiers-stl/{id}/valider
- PUT /api/admin/fichiers-stl/{id}/rejeter
- GET /api/admin/stats

### Communs (auth)
- POST /api/auth/register
- POST /api/auth/login
- POST /api/auth/logout
- GET /api/auth/me
- GET /api/utilisateurs/{id} (profil public)

## 🗄️ Repositories à créer
- DemandeRepository
- OffreMakerRepository
- FichierStlRepository
- TransactionRepository
- AvisRepository
- LitigeRepository
- MessageRepository
- NotificationRepository
- PhotoDemandeRepository

## 🔧 Services à créer
- DemandeService
- OffreMakerService
- FichierStlService
- TransactionService
- AvisService
- LitigeService
- MessageService
- NotificationService
- PhotoDemandeService
- UtilisateurService
- StatsService

## 📦 DTOs à créer
- DemandeDTO, DemandeCreateRequest, DemandeUpdateRequest
- OffreMakerDTO, OffreCreateRequest
- FichierStlDTO, FichierStlCreateRequest
- TransactionDTO
- AvisDTO, AvisCreateRequest
- LitigeDTO, LitigeCreateRequest, LitigeResolutionRequest
- MessageDTO, MessageCreateRequest
- NotificationDTO
- UtilisateurDTO, UtilisateurUpdateRequest
- StatsDTO (admin)

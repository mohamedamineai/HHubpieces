# 🎨 Prompts Tripo3D Hubpieces

> **État vérifié du checkout** : les 7 fichiers listés ci-dessous sont présents dans `src/main/resources/static/images/`. Les six modèles décrits dans [PROMPT-TRIPO3D-MANQUANTS.md](./PROMPT-TRIPO3D-MANQUANTS.md) ne sont pas présents dans ce dépôt : ce sont des visuels optionnels à ajouter seulement si les fichiers GLB réels ont été générés. Aucun faux modèle n’est créé ou renommé automatiquement.

---

## 🌐 Où générer les modèles

**Site principal** : **https://www.tripo3d.ai/**

**Workflow** :
1. Créez un compte gratuit (Google ou email)
2. Cliquez sur **"Create"** → **"Text to 3D"**
3. Collez le prompt
4. Choisissez le style (Realistic ou Sci-Fi)
5. Cliquez sur **"Generate"**
6. Attendez 1-3 minutes
7. Téléchargez en **.glb** (le plus compact)

**Alternatives gratuites** : Meshy.ai, Kaedim, Rodin, Genie (Luma AI)

---

## 🎯 Charte graphique Hubpieces

| Couleur | Hex |
|---------|-----|
| Violet profond | `#9d4edd` |
| Rose néon | `#ff5eae` |
| Noir profond | `#0a0514` |
| Violet sombre | `#1a0b33` |
| Vert éco | `#6ee7b7` |

**Style** : Futuriste, glassmorphism, économie circulaire, repair-tech, eco-friendly, industriel-moderne.

---

## ✅ Modèles EXISTANTS (inventaire vérifié)

Les sept fichiers suivants sont présents dans `src/main/resources/static/images/` :

| Fichier réel | Rôle actuel dans le site |
|---|---|
| `printer_logo.glb` | Hero de la page d'accueil |
| `logo_hub_3d.glb` | Identité/branding Hubpieces sur la page d'accueil |
| `auth_blob.glb` | Visuel commun aux pages connexion et inscription |
| `model1.glb` | Entrée catalogue « Engrenage denté précision » (Moulinex) |
| `model2.glb` | Entrée catalogue « Support articulé multi-axe » |
| `model3.glb` | Entrée catalogue « Molette crantée de réglage » |
| `model4.glb` | Entrée catalogue « Embout adaptateur turbine » (Dyson) |

Ces modèles n'ont pas été renommés en faux fichiers `repair-gear.glb`, `cao-model.glb` ou `admin-shield.glb`. Les pages CAO et admin utilisent les données de leurs rôles; un GLB de rôle ne sera branché que lorsque son vrai fichier sera ajouté.

## 🆕 Visuels de rôle optionnels décrits par les prompts

Voir [PROMPT-TRIPO3D-MANQUANTS.md](./PROMPT-TRIPO3D-MANQUANTS.md) pour les prompts. Ces chemins sont **optionnels et absents** du checkout vérifié; ne les référencez pas dans une page tant que le fichier correspondant n'existe pas.

| # | Nom exact attendu | Rôle/page prévue | Prompt |
|---|---|---|---|
| 1 | `repair-gear.glb` | Demandeur | 1 |
| 2 | `printed-piece.glb` | Maker | 2 |
| 3 | `cao-model.glb` | Expert CAO | 3 |
| 4 | `circuit-board.glb` | Catalogue | 4 |
| 5 | `admin-shield.glb` | Admin | 5 |
| 6 | `eco-recycle.glb` | Profil public | 6 |

---

## 🔧 Paramètres Tripo3D recommandés

- **Format** : `.glb` (GLB binaire)
- **Quality** : High (pas Ultra)
- **Poly count** : 50k-150k
- **Texture** : 2K
- **Taille** : 3-8 MB
- **Background** : Transparent
- **Animation** : Pas (géré par model-viewer)

---

## 💻 Intégration HTML

```html
<model-viewer
    src="images/nom-du-fichier.glb"
    alt="Description"
    auto-rotate
    camera-controls
    disable-zoom
    shadow-intensity="1"
    style="width:100%; height:400px;">
</model-viewer>
```

---

## 📂 Où placer les fichiers

Tous les GLB existants et futurs sont à placer dans : `src/main/resources/static/images/`.

Avant d'ajouter une référence, lancer `bash scripts/check-3d-models.sh`. Le script échoue si une page pointe vers un fichier absent. Un nom de prompt ne prouve pas que le GLB correspondant a été généré.

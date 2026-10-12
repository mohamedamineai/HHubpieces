# 🎨 Prompts Tripo3D Hubpieces

> **🎯 Voir aussi** : [PROMPT-TRIPO3D-MANQUANTS.md](./PROMPT-TRIPO3D-MANQUANTS.md) pour les **6 modèles 3D à générer** (les seuls qui manquent au projet).

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

## ✅ Modèles EXISTANTS (déjà dans le projet)

Ces 7 modèles sont déjà en place dans `src/main/resources/static/images/` :

| Fichier | Utilisation |
|---------|-------------|
| `printer_logo.glb` | Hero page d'accueil (rotation auto) |
| `logo_hub_3d.glb` | Logo flottant + sections |
| `auth_blob.glb` | Login & Register (panneau droit) |
| `model1.glb` à `model4.glb` | Catalogue STL + sections makers/experts |

---

## 🆕 Modèles MANQUANTS (6 à générer)

Voir [PROMPT-TRIPO3D-MANQUANTS.md](./PROMPT-TRIPO3D-MANQUANTS.md) pour les prompts détaillés.

| # | Fichier | Page cible | Prompt # |
|---|---------|-----------|----------|
| 1 | `repair-gear.glb` | dashboard-demandeur | Prompt 1 |
| 2 | `printed-piece.glb` | dashboard-maker | Prompt 2 |
| 3 | `cao-model.glb` | dashboard-expertcao | Prompt 3 |
| 4 | `circuit-board.glb` | catalogue hero | Prompt 4 |
| 5 | `admin-shield.glb` | dashboard-admin | Prompt 5 |
| 6 | `eco-recycle.glb` | profil-public | Prompt 6 |

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

Tous dans : `src/main/resources/static/images/`

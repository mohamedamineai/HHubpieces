# 🎨 Prompts Tripo3D — Modèles 3D manquants pour Hubpieces

> **Site Tripo3D** : https://www.tripo3d.ai/  
> **Compte gratuit** suffit. Connectez-vous avec Google ou email.  
> **Workflow** : New → Text to 3D → coller le prompt → Generate → Download en `.glb`

---

## 🎯 Charte graphique Hubpieces (à respecter)

| Couleur | Hex | Usage |
|---------|-----|-------|
| Violet profond | `#9d4edd` | Accents principaux, dégradés |
| Rose néon | `#ff5eae` | Boutons, glow, dégradés |
| Noir profond | `#0a0514` | Background, ambiance |
| Violet sombre | `#1a0b33` | Backgrounds secondaires |
| Blanc | `#ffffff` | Textes |

**Style général** : Futuriste, glassmorphism, économie circulaire, repair-tech, eco-friendly, industriel-moderne.

---

## 📋 Les 6 modèles 3D à générer

### 1️⃣ `repair-gear.glb` — Dashboard Demandeur (Aperçu pièce)

**Usage** : Remplace le placeholder du dashboard-demandeur.html (section "Aperçu de votre pièce"). Animation rotation lente.

**À ouvrir dans Tripo3D** : https://www.tripo3d.ai/app/creation/text-to-3d

**Prompt à copier-coller** :

```
A photorealistic 3D printable mechanical gear, 12 perfectly cut teeth, central axle hole, polished metallic finish in deep purple #9d4edd with neon pink #ff5eae rim lighting and glowing edges. The gear is slightly worn, with subtle use marks and engraved text "HUBPIECES" on its flat surface. Floating isolated on a dark gradient background from #0a0514 to #1a0b33. Cinematic studio lighting with soft volumetric fog, dramatic rim light, and bokeh particles. Industrial design meets futurism, eco-friendly repair aesthetic. 4K PBR textures (metallic + roughness), high detail, optimized for web GLB format. Centered composition, slight tilt angle. No people, no text overlay.
```

**Paramètres recommandés** :
- Style : Realistic
- Quality : High
- Format : GLB
- Size : Moderate (~5-10 MB)
- Background : Transparent (puis ajouter le fond noir en CSS)

**Une fois téléchargé** : Renommer en `repair-gear.glb` et placer dans :
```
/home/user/HHubpieces/src/main/resources/static/images/repair-gear.glb
```

---

### 2️⃣ `printed-piece.glb` — Dashboard Maker (Mes commandes)

**Usage** : Modèle 3D flottant dans la sidebar ou le hero du dashboard-maker.html. Représente une pièce imprimée.

**Prompt à copier-coller** :

```
A beautifully 3D printed mechanical replacement part, specifically a freshly printed door handle bracket for a home appliance, in vibrant ABS plastic with a smooth purple-to-pink gradient color transition from #9d4edd to #ff5eae. The piece shows visible but clean 3D printing layer lines (0.2mm), slightly glossy surface, and rests on a soft dark velvet surface. Soft studio lighting from above with subtle pink and purple neon rim lights reflecting on the plastic. Modern eco-friendly product photography style. Minimalist composition with a single object centered. High detail 4K textures, photorealistic PBR material. Dark moody atmosphere with bokeh. No text, no people, no other objects.
```

**Une fois téléchargé** : Renommer en `printed-piece.glb` et placer dans :
```
/home/user/HHubpieces/src/main/resources/static/images/printed-piece.glb
```

---

### 3️⃣ `cao-model.glb` — Dashboard Expert CAO (Modélisation)

**Usage** : Hero 3D du dashboard-expertcao.html. Représente un modèle CAO en cours.

**Prompt à copier-coller** :

```
A detailed 3D wireframe CAD model of a complex mechanical component (a turbine housing with curved surfaces, mounting holes, and technical features), displayed in two simultaneous render modes: solid translucent glass-like body in deep purple #9d4edd with internal structure visible, overlaid with bright neon pink #ff5eae glowing wireframe edges and construction lines. Floating technical measurement annotations and dimension arrows appear around the model. Dark space background #0a0514 with subtle grid floor. Dramatic studio lighting with magenta and purple volumetric beams. Futuristic engineering aesthetic, professional CAD software visualization style. High detail, 4K textures, clean and minimal composition. No real-world background, no people.
```

**Une fois téléchargé** : Renommer en `cao-model.glb` et placer dans :
```
/home/user/HHubpieces/src/main/resources/static/images/cao-model.glb
```

---

### 4️⃣ `circuit-board.glb` — Page Catalogue (Hero)

**Usage** : Hero 3D de la page catalogue.html. Représente un circuit électronique / carte mère pour évoquer la réparation.

**Prompt à copier-coller** :

```
A futuristic 3D printed circuit board motherboard replacement part, a small rectangular PCB fragment with visible micro-components (resistors, capacitors, traces) elegantly integrated into a 3D printed plastic carrier frame. The carrier frame is in glossy deep purple #9d4edd plastic with soft pink #ff5eae LED indicator lights glowing on the corners. The piece floats in the center of the frame with subtle anti-gravity effect. Dark cyberpunk background with deep purple gradient #0a0514 to #1a0b33, faint hexagonal grid pattern floor, soft volumetric fog. Cinematic lighting with neon rim lights, bokeh particles in the background. Eco-tech repair aesthetic. 4K photorealistic PBR textures, web-optimized GLB format. No people, no text overlay.
```

**Une fois téléchargé** : Renommer en `circuit-board.glb` et placer dans :
```
/home/user/HHubpieces/src/main/resources/static/images/circuit-board.glb
```

---

### 5️⃣ `admin-shield.glb` — Dashboard Admin (Sécurité)

**Usage** : Hero 3D du dashboard-admin.html. Représente un bouclier / symbole de gouvernance.

**Prompt à copier-coller** :

```
A futuristic 3D holographic shield icon, low-poly geometric design with hexagonal facets, glowing neon pink #ff5eae edges and deep purple #9d4edd body. The shield is semi-transparent with internal glowing energy lines and a small 3D gear or chip in its center representing technology and protection. Floating on a dark gradient background #0a0514 to #1a0b33. Cinematic studio lighting with magenta volumetric beams, soft particles floating around. Cybersecurity and admin control aesthetic. High detail 4K textures, PBR materials with emissive glow. Web-optimized GLB. Centered composition with slight rotation angle. No text, no people.
```

**Une fois téléchargé** : Renommer en `admin-shield.glb` et placer dans :
```
/home/user/HHubpieces/src/main/resources/static/images/admin-shield.glb
```

---

### 6️⃣ `eco-recycle.glb` — Page Profil Public (Éco-responsabilité)

**Usage** : Modèle 3D décoratif pour profil-public.html. Évoque le recyclage / l'économie circulaire.

**Prompt à copier-coller** :

```
A beautiful 3D printed eco-friendly symbol: a stylized circular arrow loop (recycle symbol) made of glossy 3D printed plastic in vibrant pink #ff5eae and deep purple #9d4edd, with small green leaves #6ee7b7 growing organically from one of the arrows. The symbol floats elegantly in the center with a subtle anti-gravity effect. Soft dark purple gradient background #0a0514 to #1a0b33, with floating bokeh particles and soft volumetric fog. Cinematic studio lighting with pink and purple rim lights. Eco-friendly, sustainable design aesthetic, modern and minimal. 4K PBR textures, photorealistic finish. Web-optimized GLB format. No people, no text overlay, centered composition.
```

**Une fois téléchargé** : Renommer en `eco-recycle.glb` et placer dans :
```
/home/user/HHubpieces/src/main/resources/static/images/eco-recycle.glb
```

---

## 🔧 Paramètres Tripo3D communs recommandés

| Paramètre | Valeur recommandée |
|-----------|-------------------|
| **Style** | Realistic (ou Sci-Fi pour shield/circuit) |
| **Quality** | High (pas Ultra pour limiter la taille) |
| **Poly count** | 50k-150k triangles (équilibre qualité/perf) |
| **Texture** | 2K (2048px) pour limiter le poids |
| **Format d'export** | **`.glb`** (GLB binaire, le plus compact) |
| **Taille cible** | 3-8 MB par fichier |
| **Background** | Transparent (on ajoutera le fond en CSS) |
| **Animation** | Pas d'auto-animation (on gère avec model-viewer) |

---

## 📂 Récapitulatif : où placer chaque fichier

Tous les fichiers `.glb` vont dans :
```
/home/user/HHubpieces/src/main/resources/static/images/
```

| Fichier | Page cible | Code HTML à ajouter |
|---------|-----------|---------------------|
| `repair-gear.glb` | dashboard-demandeur.html | Section "Aperçu de votre pièce" |
| `printed-piece.glb` | dashboard-maker.html | Hero / sidebar |
| `cao-model.glb` | dashboard-expertcao.html | Hero / sidebar |
| `circuit-board.glb` | catalogue.html | Hero de la page |
| `admin-shield.glb` | dashboard-admin.html | Hero / sidebar |
| `eco-recycle.glb` | profil-public.html | Hero décoratif |

---

## 💻 Exemple d'intégration HTML

Pour chaque modèle, le code HTML à utiliser est :

```html
<model-viewer
    src="images/nom-du-fichier.glb"
    alt="Description du modèle 3D"
    auto-rotate
    auto-rotate-delay="0"
    rotation-per-second="30deg"
    camera-controls
    disable-zoom
    shadow-intensity="1"
    exposure="1.1"
    style="width: 100%; height: 400px; --progress-bar-color: transparent;">
</model-viewer>
```

**Personnalisation** :
- `auto-rotate` : enlève si tu ne veux pas de rotation
- `camera-controls` : enlève pour un visuel statique
- `shadow-intensity="0.5"` : ajuste l'ombre
- `exposure="1.2"` : ajuste la luminosité

---

## 🆘 Si Tripo3D ne donne pas un bon résultat

**Alternatives gratuites** :
1. **Meshy.ai** (https://www.meshy.ai/) — alternative très qualitative
2. **Kaedim** (https://www.kaedim3d.com/) — bon pour les objets mécaniques
3. **Rodin** (https://hyperhuman.deemos.com/rodin) — excellent pour les pièces techniques
4. **Genie by Luma AI** (https://lumalabs.ai/genie) — rapide et bon

**Workaround si tu n'arrives pas à générer** :
- Utilise les modèles existants `model1.glb` à `model4.glb` qui sont déjà de très bonne qualité
- Renomme-les selon les besoins (ex: `model1.glb` → `repair-gear.glb`)

---

## ✅ Checklist de vérification après génération

Pour chaque fichier `.glb` :
- [ ] Format = `.glb` (pas `.gltf` ni `.fbx`)
- [ ] Taille < 10 MB
- [ ] Visible dans `<model-viewer>` (test sur la page)
- [ ] Rotation auto fluide
- [ ] Pas de fond (transparent)
- [ ] Cohérent avec la charte violet/rose

---

## 🎨 Résumé visuel

```
┌─────────────────────────────────────────────┐
│  Hubpieces 3D Models Map                    │
├─────────────────────────────────────────────┤
│  ✓ printer_logo.glb     (index - hero)     │ ← EXISTE
│  ✓ logo_hub_3d.glb      (index - logo)     │ ← EXISTE
│  ✓ auth_blob.glb        (login/register)   │ ← EXISTE
│  ✓ model1-4.glb         (catalogue/UI)     │ ← EXISTE
│  ✗ repair-gear.glb      (demandeur)        │ ← À CRÉER (prompt 1)
│  ✗ printed-piece.glb    (maker)            │ ← À CRÉER (prompt 2)
│  ✗ cao-model.glb        (expert CAO)       │ ← À CRÉER (prompt 3)
│  ✗ circuit-board.glb    (catalogue hero)   │ ← À CRÉER (prompt 4)
│  ✗ admin-shield.glb     (admin)            │ ← À CRÉER (prompt 5)
│  ✗ eco-recycle.glb      (profil)           │ ← À CRÉER (prompt 6)
└─────────────────────────────────────────────┘
```

**6 modèles à générer, ~30 minutes de travail total sur Tripo3D** (gratuit).

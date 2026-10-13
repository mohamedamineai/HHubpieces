# 🎨 Prompts Tripo3D — visuels de rôle Hubpieces

> **État vérifié le 13 octobre 2026** : les six modèles décrits dans ce fichier sont tous présents dans `src/main/resources/static/images/` et sont intégrés sur leurs pages dédiées. Les prompts ci-dessous sont conservés comme références de conception, pas comme une liste d'assets manquants. Aucun modèle n'est à régénérer ou à dupliquer.
>
> **Site Tripo3D** : https://www.tripo3d.ai/  
> **Compte gratuit** suffit. Connectez-vous avec Google ou email.  
> **Workflow futur** : New → Text to 3D → coller le prompt → Generate → Download en `.glb`.

---

## 🎯 Charte graphique Hubpieces (à respecter)

| Couleur | Hex | Usage |
|---------|-----|-------|
| Violet profond | `#9d4edd` | Accents principaux, dégradés |
| Rose néon | `#ff5eae` | Boutons, glow, dégradés |
| Noir profond | `#0a0514` | Background, ambiance |
| Violet sombre | `#1a0b33` | Backgrounds secondaires |
| Vert éco | `#6ee7b7` | Identité écoresponsable |
| Blanc | `#ffffff` | Textes |

**Style général** : Futuriste, glassmorphism, économie circulaire, repair-tech, eco-friendly, industriel-moderne. Les fonds sont ajoutés en CSS; les modèles GLB ne doivent pas contenir de fond opaque.

---

## 📋 Prompts de référence — les 6 fichiers sont présents

### 1️⃣ `repair-gear.glb` — Dashboard Demandeur (Aperçu pièce)

**Usage réel** : Décoration liée à la réparation dans `dashboard-demandeur.html`. Elle ne représente pas une demande ni une pièce de catalogue. Rotation lente avec `model-viewer` (désactivée en mode réduction des animations).

**À ouvrir dans Tripo3D** : https://www.tripo3d.ai/app/creation/text-to-3d

**Prompt à copier-coller** :

```
A photorealistic 3D printable mechanical gear, 12 perfectly cut teeth, central axle hole, polished metallic finish in deep purple #9d4edd with neon pink #ff5eae rim lighting and glowing edges. The gear is slightly worn, with subtle use marks and engraved text "HUBPIECES" on its flat surface. Floating isolated on a transparent alpha background. Do not include a floor, backdrop, card, or background geometry; the dark gradient and particles will be supplied by CSS. Use cinematic studio lighting, soft volumetric fog, and dramatic rim light on the object. Industrial design meets futurism, eco-friendly repair aesthetic. 4K PBR textures (metallic + roughness), high detail, optimized for web GLB format. Centered composition, slight tilt angle. No people, no text overlay.
```

**Paramètres recommandés** :
- Style : Realistic
- Quality : High
- Format : GLB
- Size : Moderate (cible 3-8 MB, plafond conseillé 10 MB)
- Background : Transparent (puis ajouter le fond noir en CSS)

**Si le modèle est régénéré à l’avenir** : utiliser le nom exact `repair-gear.glb` et placer dans :
```
/home/user/HHubpieces/src/main/resources/static/images/repair-gear.glb
```

---

### 2️⃣ `printed-piece.glb` — Dashboard Maker (illustration de l’espace)

**Usage** : Modèle 3D flottant dans la sidebar ou le hero du dashboard-maker.html. Représente une pièce imprimée.

**Prompt à copier-coller** :

```
A beautifully 3D printed mechanical replacement part, specifically a freshly printed door handle bracket for a home appliance, in vibrant ABS plastic with a smooth purple-to-pink gradient color transition from #9d4edd to #ff5eae. The piece shows visible but clean 3D printing layer lines (0.2mm), slightly glossy surface, floating as one isolated object on a transparent alpha background. Do not include a floor, surface, backdrop, or background geometry; the dark atmosphere will be supplied by CSS. Use soft studio lighting from above with subtle pink and purple neon rim lights reflecting on the plastic. Modern eco-friendly product photography style. Minimalist composition with a single object centered. High detail 4K textures, photorealistic PBR material. No text, no people, no other objects.
```

**Si le modèle est régénéré à l’avenir** : utiliser le nom exact `printed-piece.glb` et placer dans :
```
/home/user/HHubpieces/src/main/resources/static/images/printed-piece.glb
```

---

### 3️⃣ `cao-model.glb` — Dashboard Expert CAO (Modélisation)

**Usage réel** : Illustration technique de l’espace Expert CAO dans `dashboard-expertcao.html`; ne représente pas un modèle lié à une demande réelle ni un fichier fabriqué.

**Prompt à copier-coller** :

```
A detailed 3D wireframe CAD model of a complex mechanical component (a turbine housing with curved surfaces, mounting holes, and technical features), displayed in two simultaneous render modes: solid translucent glass-like body in deep purple #9d4edd with internal structure visible, overlaid with bright neon pink #ff5eae glowing wireframe edges and construction lines. Floating technical measurement annotations and dimension arrows appear around the model. Transparent alpha background: show only the isolated CAD model and its attached measurement annotations. No floor, backdrop, or background geometry; the dark #0a0514 grid environment will be supplied by CSS. Dramatic studio lighting with magenta and purple volumetric beams. Futuristic engineering aesthetic, professional CAD software visualization style. High detail, 4K textures, clean and minimal composition. No real-world background, no people.
```

**Si le modèle est régénéré à l’avenir** : utiliser le nom exact `cao-model.glb` et placer dans :
```
/home/user/HHubpieces/src/main/resources/static/images/cao-model.glb
```

---

### 4️⃣ `circuit-board.glb` — Page Catalogue (Hero)

**Usage** : Hero 3D de la page catalogue.html. Représente un circuit électronique / carte mère pour évoquer la réparation.

**Prompt à copier-coller** :

```
A futuristic 3D printed circuit board motherboard replacement part, a small rectangular PCB fragment with visible micro-components (resistors, capacitors, traces) elegantly integrated into a 3D printed plastic carrier frame. The carrier frame is in glossy deep purple #9d4edd plastic with soft pink #ff5eae LED indicator lights glowing on the corners. The piece floats in the center of the frame with subtle anti-gravity effect. Transparent alpha background: isolate the circuit-board object with no floor, backdrop, or background geometry. The dark cyberpunk gradient, hexagonal grid, and fog belong in CSS. Use cinematic neon rim lighting and subtle specular highlights on the isolated object. Eco-tech repair aesthetic. 4K photorealistic PBR textures, web-optimized GLB format. No people, no text overlay.
```

**Si le modèle est régénéré à l’avenir** : utiliser le nom exact `circuit-board.glb` et placer dans :
```
/home/user/HHubpieces/src/main/resources/static/images/circuit-board.glb
```

---

### 5️⃣ `admin-shield.glb` — Dashboard Admin (Sécurité)

**Usage** : Hero 3D du dashboard-admin.html. Représente un bouclier / symbole de gouvernance.

**Prompt à copier-coller** :

```
A futuristic 3D holographic shield icon, low-poly geometric design with hexagonal facets, glowing neon pink #ff5eae edges and deep purple #9d4edd body. The shield is semi-transparent with internal glowing energy lines and a small 3D gear or chip in its center representing technology and protection. Floating isolated on a transparent alpha background, with no floor, backdrop, or background geometry. Add the dark #0a0514-to-#1a0b33 gradient and any ambient particles in CSS. Cinematic studio lighting with magenta rim light on the shield. Cybersecurity and admin control aesthetic. High detail 4K textures, PBR materials with emissive glow. Web-optimized GLB. Centered composition with slight rotation angle. No text, no people.
```

**Si le modèle est régénéré à l’avenir** : utiliser le nom exact `admin-shield.glb` et placer dans :
```
/home/user/HHubpieces/src/main/resources/static/images/admin-shield.glb
```

---

### 6️⃣ `eco-recycle.glb` — Page Profil Public (Éco-responsabilité)

**Usage** : Modèle 3D décoratif pour profil-public.html. Évoque le recyclage / l'économie circulaire.

**Prompt à copier-coller** :

```
A beautiful 3D printed eco-friendly symbol: a stylized circular arrow loop (recycle symbol) made of glossy 3D printed plastic in vibrant pink #ff5eae and deep purple #9d4edd, with small green leaves #6ee7b7 growing organically from one of the arrows. The symbol floats elegantly in the center with a subtle anti-gravity effect on a transparent alpha background. No floor, backdrop, or background geometry; create the dark purple gradient, bokeh, and fog in CSS. Cinematic studio lighting with pink and purple rim lights. Eco-friendly, sustainable design aesthetic, modern and minimal. 4K PBR textures, photorealistic finish. Web-optimized GLB format. No people, no text overlay, centered composition.
```

**Si le modèle est régénéré à l’avenir** : utiliser le nom exact `eco-recycle.glb` et placer dans :
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

| Fichier | Page cible | Rôle intégré |
|---------|-----------|----------------|
| `repair-gear.glb` | `dashboard-demandeur.html` | Décoration de réparation, sans demande réelle |
| `printed-piece.glb` | `dashboard-maker.html` | Illustration Maker, sans commande réelle |
| `cao-model.glb` | `dashboard-expertcao.html` | Visuel technique, sans demande CAO réelle |
| `circuit-board.glb` | `catalogue.html` | Hero décoratif, hors catalogue fabricable |
| `admin-shield.glb` | `dashboard-admin.html` | Visuel décoratif de l’espace admin |
| `eco-recycle.glb` | `profil-public.html` | Décoration écoresponsable du profil public |

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

**Si un fichier manque dans un futur checkout** :
- Vérifie le vrai chemin dans `src/main/resources/static/images/`; un nom dans ce document ne prouve pas que l'asset est présent.
- Garde `model1.glb` à `model4.glb` sous leurs vrais noms et leurs usages actuels.
- Ne renomme pas un engrenage, un support ou un autre mesh en `admin-shield.glb`, `cao-model.glb`, etc.
- N'ajoute une référence de page qu'après avoir récupéré et vérifié le bon GLB. Dans le checkout vérifié le 13 octobre 2026, les six fichiers de rôle ci-dessus sont présents.

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
│  ✓ model1-4.glb         (pièces catalogue) │ ← PRÉSENTS
│  ✓ repair-gear.glb      (dashboard-demandeur) │ ← PRÉSENT, INTÉGRÉ
│  ✓ printed-piece.glb    (dashboard-maker)    │ ← PRÉSENT, INTÉGRÉ
│  ✓ cao-model.glb        (dashboard-expertcao)│ ← PRÉSENT, INTÉGRÉ
│  ✓ circuit-board.glb    (catalogue hero)     │ ← PRÉSENT, DÉCORATIF
│  ✓ admin-shield.glb     (dashboard-admin)    │ ← PRÉSENT, INTÉGRÉ
│  ✓ eco-recycle.glb      (profil-public)      │ ← PRÉSENT, INTÉGRÉ
└─────────────────────────────────────────────┘
```

**État du checkout contrôlé : les six fichiers sont présents et intégrés.** `auth_blob.glb` est réutilisé sans duplication sur `login.html` et `register.html`. Les six assets de rôle et `auth_blob.glb` ont été optimisés pour le web (4,72 à 7,53 MiB, dans la cible 3–8 MB) avec `scripts/optimize-glb.mjs`, puis revalidés par `scripts/check-3d-models.sh`. Les originaux Tripo3D (58–68 MiB) restent dans l'historique Git (commit `1158999`).

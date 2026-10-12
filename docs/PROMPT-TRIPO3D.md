# Prompt Tripo3D pour modèle 3D Hubpieces

## 🎨 Prompt principal

**Nom du fichier** : `repair-gear.glb`

**Prompt à utiliser sur Tripo3D** (https://www.tripo3d.ai/) :

```
A photorealistic 3D printable mechanical gear, smoothly rotating isolated on a dark purple gradient background with soft neon pink rim lighting. The gear is made of polished metallic material with subtle wear marks, featuring 12 perfectly cut teeth, a central axle hole, and engraved text "HUBPIECES" on its surface. Style: industrial design meets futurism, eco-friendly repair aesthetic. High detail, 4K textures, optimized for 3D printing in PLA/ABS materials. The lighting is dramatic with neon glow effects matching the Hubpieces brand colors (deep purple #9d4edd and pink #ff5eae).
```

## 📋 Variantes de prompt

### Pour la page d'accueil (hero principal)
- Fichier : `repair-gear.glb`
- Position : Remplace ou complète `printer_logo.glb`

### Pour la section "Makers" (3D flottants)
- Fichier : `printed-piece.glb`
- Prompt :
```
A beautiful 3D printed replacement part, specifically a household appliance handle (door handle style) freshly printed, smooth ABS plastic finish in vibrant pink-purple gradient. The piece is shown on a clean dark surface with soft studio lighting. The model is clean, modern, with visible layer lines that emphasize the 3D printing process. Eco-friendly vibe, Hubpieces brand colors.
```

### Pour la section "Experts CAO"
- Fichier : `cao-model.glb`
- Prompt :
```
A detailed 3D wireframe CAD model of a mechanical component (gear or bracket) with construction lines, measurement annotations, and technical dimensions floating in space. Neon purple and pink glowing edges on a dark background. The model shows both wireframe and solid render modes. Futuristic engineering aesthetic, perfect for showcasing 3D modeling expertise.
```

### Pour le login/auth
- Fichier : `auth-blob.glb` (déjà présent : `auth_blob.glb`)
- Prompt :
```
A fluid, organic metaball blob shape with iridescent purple-pink material, slowly morphing and rotating. Abstract and modern, evokes creativity and innovation. Smooth glossy surface with subtle reflections. Dark background, dramatic studio lighting. Perfect for a creative tech platform branding element.
```

### Pour le logo
- Fichier : `logo-3d.glb` (déjà présent : `logo_hub_3d.glb`)
- Prompt :
```
A stylized 3D logo mark featuring a hexagonal shape with the letter "H" integrated into it. The logo is made of polished metallic material with neon purple and pink gradient reflections. Floating on a dark background, slowly rotating. Modern, minimalist, tech-forward design suitable for a 3D printing platform.
```

## 🔧 Paramètres Tripo3D recommandés

- **Format d'export** : `.glb` (GLB binaire)
- **Qualité** : Haute (4K textures)
- **Taille** : Modérée (< 10 MB pour le web)
- **Poly count** : 50k-150k triangles (équilibre qualité/performance)
- **Textures** : PBR (Metallic + Roughness)
- **Animation** : Optionnelle, rotation lente si présente

## 📂 Où placer les fichiers

Une fois les fichiers `.glb` générés, placez-les dans :

```
src/main/resources/static/images/
```

Puis référencez-les dans le code HTML/JS :

```html
<model-viewer
    src="images/mon-nouveau-modele.glb"
    alt="Description"
    auto-rotate
    camera-controls
    shadow-intensity="1">
</model-viewer>
```

## 🎯 Où utiliser chaque modèle

| Fichier | Page | Section |
|---------|------|---------|
| `printer_logo.glb` | index.html | Hero (déjà en place) |
| `logo_hub_3d.glb` | index.html | Logo flottant (déjà en place) |
| `auth_blob.glb` | login.html, register.html | Panneau droit (déjà en place) |
| `model1-4.glb` | index.html | Catalogue, sections (déjà en place) |
| `repair-gear.glb` | dashboard-demandeur.html | Hero "Aperçu de votre pièce" |
| `printed-piece.glb` | dashboard-maker.html | Section "Missions" |
| `cao-model.glb` | dashboard-expertcao.html | Hero "Modélisation 3D" |

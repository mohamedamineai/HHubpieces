# 🎨 Prompts Tripo3D Hubpieces

> **Inventaire vérifié le 13 octobre 2026** : les 13 fichiers GLB présents dans `src/main/resources/static/images/` ont été contrôlés (GLB v2, chunks, JSON et ressources intégrées). Les six visuels de rôle décrits dans [PROMPT-TRIPO3D-MANQUANTS.md](./PROMPT-TRIPO3D-MANQUANTS.md) sont désormais présents et affectés à leur page. `auth_blob.glb` est partagé par connexion et inscription : il n'est pas dupliqué.
>
> **Aucun fichier factice n'a été créé, renommé ou substitué.** Les GLB décoratifs restent des ressources statiques : ils ne sont pas des entrées `FichierStl` ni des pièces proposées au téléchargement.

---

## 🌐 Génération (pour une future nouvelle version)

**Site principal** : **https://www.tripo3d.ai/**

Workflow conseillé : `Create` → `Text to 3D` → coller le prompt → choisir le style → générer → exporter en `.glb`. Les prompts existants sont conservés dans [PROMPT-TRIPO3D-MANQUANTS.md](./PROMPT-TRIPO3D-MANQUANTS.md) comme références; ils ne signifient plus que les fichiers sont absents.

---

## 🎯 Charte graphique Hubpieces

| Couleur | Hex |
|---------|-----|
| Violet profond | `#9d4edd` |
| Rose néon | `#ff5eae` |
| Noir profond | `#0a0514` |
| Violet sombre | `#1a0b33` |
| Vert éco | `#6ee7b7` |

**Style** : futuriste, glassmorphism, économie circulaire, repair-tech, eco-friendly, industriel-moderne. Les fonds d'ambiance sont dessinés en CSS; le composant `<model-viewer>` des pages utilise un fond transparent.

---

## ✅ Inventaire réel des modèles 3D présents

| Fichier réellement présent | Usage constaté / page autorisée | Nature |
|---|---|---|
| `printer_logo.glb` | Hero de `index.html` | Illustration d'accueil |
| `logo_hub_3d.glb` | Identité Hubpieces de `index.html` | Branding |
| `auth_blob.glb` | `login.html` et `register.html` | Illustration d'authentification partagée, un seul fichier |
| `model1.glb` | Carte de catalogue / sélection de pièce | Modèle existant de pièce, à distinguer des nouveaux visuels décoratifs |
| `model2.glb` | Carte de catalogue / sélection de pièce | Modèle existant de pièce |
| `model3.glb` | Carte de catalogue / sélection de pièce | Modèle existant de pièce |
| `model4.glb` | Carte de catalogue / sélection de pièce | Modèle existant de pièce |
| `repair-gear.glb` | `dashboard-demandeur.html` | Décoration du parcours de réparation, pas une demande réelle |
| `printed-piece.glb` | `dashboard-maker.html` | Décoration de l'espace Maker, pas une commande réelle |
| `cao-model.glb` | `dashboard-expertcao.html` | Illustration technique de l'espace CAO, pas un fichier de demande |
| `circuit-board.glb` | Hero de `catalogue.html` | Illustration du catalogue, **pas** un article fabricable par défaut |
| `admin-shield.glb` | `dashboard-admin.html` | Illustration de l'espace administrateur |
| `eco-recycle.glb` | `profil-public.html` | Décoration écoresponsable en violet/rose et vert `#6ee7b7` |

Les nouveaux visuels de rôle sont conservés uniquement sur leurs pages de destination. `auth_blob.glb` est la seule exception de partage, voulue entre connexion et inscription. Le hero `circuit-board.glb` est séparé de la grille et du parcours de téléchargement du catalogue.

### Alerte de poids

La cible de génération des prompts est de **3 à 8 MB**, avec un plafond recommandé de 10 MB. Les fichiers contrôlés sont lourds : `repair-gear.glb` (60,80 MiB), `printed-piece.glb` (58,48 MiB), `cao-model.glb` (67,71 MiB), `circuit-board.glb` (61,61 MiB), `admin-shield.glb` (60,52 MiB) et `eco-recycle.glb` (58,79 MiB). L'asset partagé `auth_blob.glb` pèse 62,61 MiB. Ils dépassent tous très largement la cible; dix des treize GLB du dépôt dépassent 10 MB (base décimale). Ils n'ont pas été altérés ici : prévoir une optimisation des maillages/textures avant une mise en production à fort trafic, puis refaire les contrôles.

---

## 🔧 Paramètres Tripo3D recommandés pour les prochaines versions

- **Format** : `.glb` (GLB binaire)
- **Quality** : High (pas Ultra)
- **Poly count** : 50k–150k
- **Texture** : 2K
- **Taille** : cible 3–8 MB; éviter de dépasser 10 MB
- **Background** : Transparent; créer le fond en CSS
- **Animation** : désactivée dans le fichier; rotation gérée par `model-viewer` et coupée si `prefers-reduced-motion` est demandé

---

## 💻 Intégration HTML existante

Le projet réutilise le composant `<model-viewer>` (chargé par les pages HTML). Exemple de principe :

```html
<div class="model-stage">
  <model-viewer
      src="images/nom-du-fichier.glb"
      alt="Description utile du modèle"
      auto-rotate
      data-motion-auto-rotate
      camera-controls
      disable-zoom
      loading="lazy"
      shadow-intensity="0.6"
      style="width:100%;height:280px;background:transparent;">
  </model-viewer>
</div>
```

Pour les éléments décoratifs, le texte autour du modèle indique explicitement qu'il ne s'agit ni d'une demande ni d'un fichier téléchargeable. Le script `js/model-viewer-motion.js` respecte le réglage de réduction des animations du navigateur.

---

## 📂 Emplacement et contrôles

Tous les GLB sont placés dans `src/main/resources/static/images/`.

Avant toute nouvelle référence ou tout remplacement :

```bash
bash scripts/check-3d-models.sh
```

Le script vérifie les fichiers, les références locales et l'affectation des visuels de rôle aux pages attendues. Lors du contrôle du 13 octobre 2026, les 13 GLB étaient structurellement valides, leurs ressources étaient intégrées, et aucune ressource externe n'était déclarée. Le script signale les tailles hors cible sans masquer les fichiers ni les renommer.

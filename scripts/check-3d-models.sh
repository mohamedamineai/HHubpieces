#!/bin/bash
# ===============================================
# Hubpieces — Vérification des modèles 3D
# ===============================================
# Usage : bash scripts/check-3d-models.sh

IMAGES_DIR="src/main/resources/static/images"

echo "================================================"
echo "  Hubpieces — État des modèles 3D"
echo "================================================"
echo ""

REQUIRED_MODELS=(
    "printer_logo.glb:index.html (hero - EXISTE)"
    "logo_hub_3d.glb:logo flottant (EXISTE)"
    "auth_blob.glb:login/register (EXISTE)"
    "model1.glb:catalogue 1 (EXISTE)"
    "model2.glb:catalogue 2 (EXISTE)"
    "model3.glb:catalogue 3 (EXISTE)"
    "model4.glb:catalogue 4 (EXISTE)"
    "repair-gear.glb:dashboard-demandeur ⚠ À GÉNÉRER"
    "printed-piece.glb:dashboard-maker ⚠ À GÉNÉRER"
    "cao-model.glb:dashboard-expertcao ⚠ À GÉNÉRER"
    "circuit-board.glb:catalogue hero ⚠ À GÉNÉRER"
    "admin-shield.glb:dashboard-admin ⚠ À GÉNÉRER"
    "eco-recycle.glb:profil-public ⚠ À GÉNÉRER"
)

MISSING=0
PRESENT=0

for entry in "${REQUIRED_MODELS[@]}"; do
    file="${entry%%:*}"
    desc="${entry#*:}"
    if [ -f "$IMAGES_DIR/$file" ]; then
        size=$(du -h "$IMAGES_DIR/$file" | cut -f1)
        printf "  ✓ %-22s %s (%s)\n" "$file" "$desc" "$size"
        PRESENT=$((PRESENT + 1))
    else
        printf "  ✗ %-22s %s\n" "$file" "$desc"
        MISSING=$((MISSING + 1))
    fi
done

echo ""
echo "================================================"
echo "  Résumé : $PRESENT présents, $MISSING manquants"
echo "================================================"
echo ""

if [ $MISSING -gt 0 ]; then
    echo "📖 Voir : docs/PROMPT-TRIPO3D-MANQUANTS.md pour les prompts"
    echo "🌐 Générer sur : https://www.tripo3d.ai/"
    echo "📂 Placer dans : $IMAGES_DIR/"
    echo ""
fi

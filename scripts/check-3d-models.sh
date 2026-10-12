#!/bin/bash
# ===============================================
# Hubpieces — Vérification de l'intégration des modèles 3D (.glb)
# ===============================================
# Usage : bash scripts/check-3d-models.sh
#
# Le script vérifie 3 choses :
#   1. Les fichiers .glb présents dans src/main/resources/static/images/
#      (en-tête binaire glTF v2 valide)
#   2. Que TOUTES les références "images/xxx.glb" des pages HTML / JS / CSS
#      pointent vers un fichier réellement présent  -> aucun lien 404
#   3. La liste des modèles optionnels encore absents (voir
#      docs/PROMPT-TRIPO3D-MANQUANTS.md pour les générer)
#
# Code retour : 1 si un fichier référencé est manquant (lien 404), sinon 0.

set -u

STATIC_DIR="src/main/resources/static"
IMAGES_DIR="$STATIC_DIR/images"

if [ ! -d "$IMAGES_DIR" ]; then
    echo "ERREUR : dossier introuvable -> $IMAGES_DIR"
    echo "Lancez le script depuis la racine du projet hubpieces."
    exit 2
fi

echo "======================================================"
echo "  Hubpieces — Intégration des modèles 3D (.glb)"
echo "======================================================"
echo ""
echo "-- 1. Fichiers .glb présents -------------------------------------"
echo ""

PRESENT=0
BROKEN=0
for f in "$IMAGES_DIR"/*.glb; do
    [ -e "$f" ] || continue
    PRESENT=$((PRESENT + 1))
    size=$(du -h "$f" | cut -f1)
    magic=$(head -c 4 "$f" 2>/dev/null)

    version="?"
    if command -v od >/dev/null 2>&1; then
        version=$(od -An -tu4 -j4 -N4 "$f" 2>/dev/null | tr -d ' ')
    fi

    if [ "$magic" = "glTF" ]; then
        printf "  ✓ %-20s %6s  (glTF v%s)\n" "$(basename "$f")" "$size" "$version"
    else
        printf "  ✗ %-20s %6s  *** EN-TETE glTF INVALIDE (fichier corrompu) ***\n" "$(basename "$f")" "$size"
        BROKEN=$((BROKEN + 1))
    fi
done

if [ "$PRESENT" -eq 0 ]; then
    echo "  (aucun fichier .glb trouvé)"
fi

echo ""
echo "-- 2. Références des pages (aucun lien 404 autorisé) -------------"
echo ""

MISSING_REFS=0
CHECKED_REFS=0
# Toutes les références "images/....glb" dans les HTML, JS et CSS
REFS=$(grep -rhoE 'images/[A-Za-z0-9_.-]+\.glb' \
        --include='*.html' --include='*.js' --include='*.css' \
        "$STATIC_DIR" 2>/dev/null | sort -u)

if [ -z "$REFS" ]; then
    echo "  (aucune référence 'images/*.glb' dans les pages)"
else
    for ref in $REFS; do
        CHECKED_REFS=$((CHECKED_REFS + 1))
        file="$STATIC_DIR/$ref"
        if [ -f "$file" ]; then
            pages=$(grep -rlF "$ref" --include='*.html' --include='*.js' "$STATIC_DIR" 2>/dev/null \
                    | xargs -r -n1 basename | sort -u | paste -sd ', ' -)
            printf "  ✓ %-28s -> %s\n" "$ref" "$pages"
        else
            printf "  ✗ %-28s -> FICHIER ABSENT (lien 404 !)\n" "$ref"
            MISSING_REFS=$((MISSING_REFS + 1))
        fi
    done
fi

echo ""
echo "-- 3. Modèles optionnels non encore générés ---------------------"
echo ""

OPTIONAL=(
    "repair-gear.glb:dashboard-demandeur"
    "printed-piece.glb:dashboard-maker"
    "cao-model.glb:dashboard-expertcao"
    "circuit-board.glb:catalogue"
    "admin-shield.glb:dashboard-admin"
    "eco-recycle.glb:profil-public"
)

OPT_MISSING=0
for entry in "${OPTIONAL[@]}"; do
    name="${entry%%:*}"
    usage="${entry#*:}"
    if [ -f "$IMAGES_DIR/$name" ]; then
        printf "  ✓ %-20s (utilisable dans %s)\n" "$name" "$usage"
    else
        printf "  · %-20s absent — prévu pour %s\n" "$name" "$usage"
        OPT_MISSING=$((OPT_MISSING + 1))
    fi
done

if [ "$OPT_MISSING" -gt 0 ]; then
    echo ""
    echo "  Rien n'est cassé : aucune page ne référence ces fichiers (donc aucun 404)."
    echo "  Pour les créer : voir docs/PROMPT-TRIPO3D-MANQUANTS.md (prompts Tripo3D),"
    echo "  puis les placer dans $IMAGES_DIR/ avant de les référencer dans les pages."
fi

echo ""
echo "======================================================"
if [ "$MISSING_REFS" -gt 0 ] || [ "$BROKEN" -gt 0 ]; then
    echo "  ECHEC : $MISSING_REFS référence(s) cassée(s), $BROKEN fichier(s) corrompu(s)"
    echo "======================================================"
    exit 1
fi
echo "  OK : $PRESENT modèle(s) .glb valide(s), $CHECKED_REFS référence(s) vérifiée(s), 0 lien 404"
echo "======================================================"
exit 0

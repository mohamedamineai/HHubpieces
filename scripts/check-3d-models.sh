#!/bin/bash
# ===============================================
# Hubpieces — vérification des modèles 3D (.glb)
# ===============================================
# Usage : bash scripts/check-3d-models.sh depuis la racine du dépôt.
# Vérifie :
#   1. l'en-tête GLB v2, les chunks, le JSON, les bufferViews et l'intégration
#      des ressources (buffer et images; aucune URL externe non embarquée);
#   2. que les références HTML/JS/CSS désignent un fichier présent;
#   3. que les visuels de rôle sont utilisés seulement sur les pages prévues.
# Les assets optionnels absents restent absents et ne sont jamais fabriqués,
# renommés ou remplacés par ce script. Les tailles excessives sont signalées.

set -u

STATIC_DIR="src/main/resources/static"
IMAGES_DIR="$STATIC_DIR/images"

if [ ! -d "$IMAGES_DIR" ]; then
    echo "ERREUR : dossier introuvable -> $IMAGES_DIR"
    echo "Lancez le script depuis la racine du projet Hubpieces."
    exit 2
fi

if ! command -v python3 >/dev/null 2>&1; then
    echo "ERREUR : python3 est requis pour vérifier le JSON et les chunks GLB."
    exit 2
fi

echo "======================================================"
echo "  Hubpieces — Intégration des modèles 3D (.glb)"
echo "======================================================"
echo ""

echo "-- 1. Fichiers GLB : structure et ressources intégrées -----------"

GLB_INVALID=0
python3 - "$IMAGES_DIR" <<'PY' || GLB_INVALID=1
import base64
import json
import struct
import sys
from pathlib import Path

images_dir = Path(sys.argv[1])
files = sorted(images_dir.glob("*.glb"))
if not files:
    print("  (aucun fichier .glb trouvé)")

JSON_CHUNK = 0x4E4F534A
BIN_CHUNK = 0x004E4942


def fail(errors, message):
    errors.append(message)


def decode_data_uri(uri, label, errors):
    try:
        header, payload = uri.split(",", 1)
        if ";base64" in header:
            return base64.b64decode(payload, validate=True)
        # glTF data URIs are normally base64, but percent-encoded payloads are
        # still counted as embedded and need not be materialized for this check.
        from urllib.parse import unquote_to_bytes
        return unquote_to_bytes(payload)
    except Exception as exc:
        fail(errors, f"{label}: URI data invalide ({exc})")
        return b""


for path in files:
    size = path.stat().st_size
    errors = []
    try:
        data = path.read_bytes()
    except OSError as exc:
        print(f"  ✗ {path.name}: lecture impossible ({exc})")
        continue

    if len(data) < 12:
        print(f"  ✗ {path.name}: en-tête GLB incomplet")
        continue

    magic, version, declared_total = struct.unpack_from("<4sII", data, 0)
    if magic != b"glTF":
        fail(errors, f"signature invalide ({magic!r})")
    if version != 2:
        fail(errors, f"version GLB {version}, version 2 attendue")
    if declared_total != len(data):
        fail(errors, f"longueur d'en-tête {declared_total} != taille réelle {len(data)}")

    chunks = []
    offset = 12
    while offset < len(data):
        if offset + 8 > len(data):
            fail(errors, f"en-tête de chunk tronqué à l'offset {offset}")
            break
        chunk_length, chunk_type = struct.unpack_from("<II", data, offset)
        start = offset + 8
        end = start + chunk_length
        if chunk_length % 4:
            fail(errors, f"longueur de chunk non alignée sur 4 octets à l'offset {offset}")
        if end > len(data):
            fail(errors, f"chunk {chunk_type:#010x} dépasse la longueur GLB")
            break
        chunks.append((chunk_type, start, end))
        offset = end

    if offset != len(data):
        fail(errors, f"les chunks se terminent à {offset}, taille fichier {len(data)}")
    json_chunks = [chunk for chunk in chunks if chunk[0] == JSON_CHUNK]
    bin_chunks = [chunk for chunk in chunks if chunk[0] == BIN_CHUNK]
    if not chunks or chunks[0][0] != JSON_CHUNK:
        fail(errors, "le premier chunk n'est pas le chunk JSON")
    if len(json_chunks) != 1:
        fail(errors, f"{len(json_chunks)} chunk(s) JSON, un seul attendu")
    if len(bin_chunks) > 1:
        fail(errors, f"{len(bin_chunks)} chunks BIN, au plus un attendu")

    doc = None
    if len(json_chunks) == 1:
        _, start, end = json_chunks[0]
        try:
            doc = json.loads(data[start:end].decode("utf-8"))
        except (UnicodeDecodeError, json.JSONDecodeError) as exc:
            fail(errors, f"JSON illisible/invalide ({exc})")

    if doc is not None:
        if not isinstance(doc, dict):
            fail(errors, "la racine JSON n'est pas un objet")
            doc = None
        elif doc.get("asset", {}).get("version") != "2.0":
            fail(errors, f"asset.version={doc.get('asset', {}).get('version')!r}, glTF 2.0 attendu")

    embedded_image_count = 0
    buffer_count = 0
    external_resources = []
    bin_data = b""
    if bin_chunks:
        _, start, end = bin_chunks[0]
        bin_data = data[start:end]

    if doc is not None:
        buffers = doc.get("buffers", [])
        buffer_count = len(buffers)
        buffer_lengths = []
        for index, buffer in enumerate(buffers):
            expected = buffer.get("byteLength")
            if not isinstance(expected, int) or expected < 0:
                fail(errors, f"buffer {index}: byteLength invalide")
                buffer_lengths.append(0)
                continue
            uri = buffer.get("uri")
            if uri is None:
                if index != 0 or not bin_chunks:
                    fail(errors, f"buffer {index}: aucun chunk BIN intégré")
                    available = 0
                else:
                    available = len(bin_data)
                    if expected > available or available - expected > 3:
                        fail(errors, f"buffer {index}: byteLength {expected} incompatible avec BIN ({available})")
            elif uri.startswith("data:"):
                embedded = decode_data_uri(uri, f"buffer {index}", errors)
                available = len(embedded)
                if expected > available or available - expected > 3:
                    fail(errors, f"buffer {index}: byteLength {expected} incompatible avec l'URI intégrée ({available})")
            else:
                external_resources.append(f"buffer {index}: {uri}")
                available = expected
            buffer_lengths.append(expected)

        buffer_views = doc.get("bufferViews", [])
        for index, view in enumerate(buffer_views):
            buffer_index = view.get("buffer", 0)
            view_offset = view.get("byteOffset", 0)
            view_length = view.get("byteLength")
            if not isinstance(buffer_index, int) or not (0 <= buffer_index < len(buffer_lengths)):
                fail(errors, f"bufferView {index}: index buffer invalide ({buffer_index!r})")
                continue
            if not isinstance(view_offset, int) or view_offset < 0 or not isinstance(view_length, int) or view_length < 0:
                fail(errors, f"bufferView {index}: offset/longueur invalide")
                continue
            if view_offset + view_length > buffer_lengths[buffer_index]:
                fail(errors, f"bufferView {index}: plage hors des {buffer_lengths[buffer_index]} octets déclarés")

        for index, accessor in enumerate(doc.get("accessors", [])):
            view_index = accessor.get("bufferView")
            if view_index is not None and (not isinstance(view_index, int) or not (0 <= view_index < len(buffer_views))):
                fail(errors, f"accessor {index}: bufferView invalide ({view_index!r})")
            sparse = accessor.get("sparse", {})
            for part in (sparse.get("indices", {}), sparse.get("values", {})):
                sparse_view = part.get("bufferView")
                if sparse_view is not None and (not isinstance(sparse_view, int) or not (0 <= sparse_view < len(buffer_views))):
                    fail(errors, f"accessor {index}: bufferView sparse invalide ({sparse_view!r})")

        images = doc.get("images", [])
        for index, image in enumerate(images):
            if "bufferView" in image:
                view_index = image["bufferView"]
                if not isinstance(view_index, int) or not (0 <= view_index < len(buffer_views)):
                    fail(errors, f"image {index}: bufferView invalide ({view_index!r})")
                else:
                    embedded_image_count += 1
            elif isinstance(image.get("uri"), str) and image["uri"].startswith("data:"):
                image_bytes = decode_data_uri(image["uri"], f"image {index}", errors)
                if not image_bytes:
                    fail(errors, f"image {index}: ressource intégrée vide")
                embedded_image_count += 1
            elif image.get("uri"):
                external_resources.append(f"image {index}: {image['uri']}")
            else:
                fail(errors, f"image {index}: ni URI ni bufferView")

        if external_resources:
            fail(errors, "ressource(s) externe(s) non intégrée(s): " + ", ".join(external_resources))

    if errors:
        print(f"  ✗ {path.name:20s} {size / (1024 * 1024):6.2f} MiB — " + "; ".join(errors))
    else:
        image_label = f", {embedded_image_count} image(s) intégrée(s)" if doc and doc.get("images") else ""
        print(f"  ✓ {path.name:20s} {size / (1024 * 1024):6.2f} MiB — glTF v2, chunks JSON/BIN, JSON valide, {buffer_count} buffer(s) intégré(s){image_label}")

    # The Tripo prompts specify 3–8 MB, and recommend staying below 10 MB.
    if size > 8_000_000:
        ceiling = "; dépasse aussi le plafond conseillé de 10 MB" if size > 10_000_000 else ""
        print(f"    ⚠ poids au-dessus de la cible 3–8 MB{ceiling}")
    if errors:
        sys.exit(1)
PY

printf '\n-- 2. Références HTML / JS / CSS (aucun lien 404 autorisé) --------\n'

MISSING_REFS=0
CHECKED_REFS=0
REFS=$(grep -rhoE 'images/[A-Za-z0-9_.-]+\.glb' \
        --include='*.html' --include='*.js' --include='*.css' \
        "$STATIC_DIR" 2>/dev/null | sort -u)

if [ -z "$REFS" ]; then
    echo "  (aucune référence 'images/*.glb' dans les pages)"
else
    while IFS= read -r ref; do
        [ -n "$ref" ] || continue
        CHECKED_REFS=$((CHECKED_REFS + 1))
        file="$STATIC_DIR/$ref"
        if [ -f "$file" ]; then
            pages=$(grep -rlF "$ref" --include='*.html' --include='*.js' --include='*.css' "$STATIC_DIR" 2>/dev/null \
                    | sed "s#^$STATIC_DIR/##" | sort -u | paste -sd ',' - | sed 's/,/, /g')
            printf "  ✓ %-28s -> %s\n" "$ref" "$pages"
        else
            printf "  ✗ %-28s -> FICHIER ABSENT (lien 404 !)\n" "$ref"
            MISSING_REFS=$((MISSING_REFS + 1))
        fi
    done <<< "$REFS"
fi

printf '\n-- 3. Affectation des visuels de rôle aux pages prévues ------------\n'

WRONG_PAGE_REFS=0
MISSING_TARGET_REFS=0
ROLE_MAP=(
    "eco-recycle.glb|profil-public.html"
    "circuit-board.glb|catalogue.html"
    "cao-model.glb|dashboard-expertcao.html"
    "admin-shield.glb|dashboard-admin.html"
    "repair-gear.glb|dashboard-demandeur.html"
    "printed-piece.glb|dashboard-maker.html"
    "auth_blob.glb|login.html register.html"
)

for entry in "${ROLE_MAP[@]}"; do
    name="${entry%%|*}"
    allowed="${entry#*|}"
    ref="images/$name"
    asset="$IMAGES_DIR/$name"
    if [ ! -f "$asset" ]; then
        printf "  · %-20s absent (aucune page ne doit le référencer)\n" "$name"
        continue
    fi

    mapfile -t pages < <(grep -rlF "$ref" --include='*.html' "$STATIC_DIR" 2>/dev/null | sed 's#.*/##' | sort -u)
    if [ "${#pages[@]}" -eq 0 ]; then
        printf "  ✗ %-20s aucune référence; cible : %s\n" "$name" "$allowed"
        MISSING_TARGET_REFS=$((MISSING_TARGET_REFS + 1))
        continue
    fi

    page_error=0
    for page in "${pages[@]}"; do
        if [[ " $allowed " != *" $page "* ]]; then
            printf "  ✗ %-20s mauvaise page : %s (attendu : %s)\n" "$name" "$page" "$allowed"
            WRONG_PAGE_REFS=$((WRONG_PAGE_REFS + 1))
            page_error=1
        fi
    done
    for target in $allowed; do
        if ! grep -qF "$ref" "$STATIC_DIR/$target" 2>/dev/null; then
            printf "  ✗ %-20s cible manquante : %s\n" "$name" "$target"
            MISSING_TARGET_REFS=$((MISSING_TARGET_REFS + 1))
            page_error=1
        fi
    done
    if [ "$page_error" -eq 0 ]; then
        printf "  ✓ %-20s -> %s\n" "$name" "$allowed"
    fi
done

echo ""
echo "======================================================"
if [ "$GLB_INVALID" -ne 0 ] || [ "$MISSING_REFS" -gt 0 ] || [ "$WRONG_PAGE_REFS" -gt 0 ] || [ "$MISSING_TARGET_REFS" -gt 0 ]; then
    echo "  ÉCHEC : GLB invalide(s)=$((GLB_INVALID)), référence(s) absente(s)=$MISSING_REFS, mauvaise(s) page(s)=$WRONG_PAGE_REFS, cible(s) manquante(s)=$MISSING_TARGET_REFS"
    echo "======================================================"
    exit 1
fi
PRESENT=$(find "$IMAGES_DIR" -maxdepth 1 -type f -name '*.glb' | wc -l | tr -d ' ')
echo "  OK : $PRESENT GLB(s) structurellement valide(s), $CHECKED_REFS référence(s) présente(s), affectation des visuels de rôle conforme"
echo "======================================================"
exit 0

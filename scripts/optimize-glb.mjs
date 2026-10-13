// =====================================================================
// Hubpieces — optimisation web d'un GLB Tripo3D (visuels décoratifs)
// ---------------------------------------------------------------------
// Pipeline : dedup -> weld -> simplification meshoptimizer -> prune
//            -> textures WebP 2048 px (qualité 88) -> quantification.
// Extensions produites : EXT_texture_webp, KHR_mesh_quantization
// (lues nativement par <model-viewer>, sans décodeur externe).
//
// Installation (hors dépôt, une seule fois) :
//   npm i --no-save @gltf-transform/core @gltf-transform/extensions \
//         @gltf-transform/functions meshoptimizer sharp
// Usage :
//   node scripts/optimize-glb.mjs <source.glb> <sortie.glb> [ratio]
//   ratio = part de triangles conservée (0.10 pour les visuels de rôle,
//           0.13 pour cao-model.glb afin de garder les ailettes fines).
// Les originaux restent récupérables dans l'historique Git (commit 1158999).
// Relancer ensuite : bash scripts/check-3d-models.sh
// =====================================================================
import { NodeIO } from '@gltf-transform/core';
import { ALL_EXTENSIONS } from '@gltf-transform/extensions';
import { dedup, weld, simplify, prune, textureCompress, quantize } from '@gltf-transform/functions';
import { MeshoptSimplifier } from 'meshoptimizer';
import sharp from 'sharp';
import fs from 'fs';
const [src, dst, ratio = '0.1'] = process.argv.slice(2);
if (!src || !dst) { console.error('Usage : node scripts/optimize-glb.mjs <source.glb> <sortie.glb> [ratio]'); process.exit(2); }
await MeshoptSimplifier.ready;
const io = new NodeIO().registerExtensions(ALL_EXTENSIONS);
const doc = await io.read(src);
await doc.transform(
  dedup(), weld(),
  simplify({ simplifier: MeshoptSimplifier, ratio: +ratio, error: 0.002, lockBorder: false }),
  prune(),
  textureCompress({ encoder: sharp, targetFormat: 'webp', resize: [2048, 2048], quality: 88 }),
  quantize(),
);
await io.write(dst, doc);
let tris=0; for (const m of doc.getRoot().listMeshes()) for (const p of m.listPrimitives()) tris += (p.getIndices()?.getCount()||0)/3;
console.log(src.split('/').pop(), '->', (fs.statSync(dst).size/1e6).toFixed(2)+'MB', 'tris', tris);

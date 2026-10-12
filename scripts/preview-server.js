const http = require('http');
const fs = require('fs');
const path = require('path');

const PORT = 8080;
const STATIC_DIR = path.join(__dirname, '..', 'src', 'main', 'resources', 'static');

const MIME_TYPES = {
    '.html': 'text/html; charset=utf-8',
    '.css': 'text/css; charset=utf-8',
    '.js': 'application/javascript; charset=utf-8',
    '.json': 'application/json; charset=utf-8',
    '.png': 'image/png',
    '.jpg': 'image/jpeg',
    '.jpeg': 'image/jpeg',
    '.glb': 'model/gltf-binary',
    '.svg': 'image/svg+xml',
    '.ico': 'image/x-icon',
    '.woff2': 'font/woff2',
    '.woff': 'font/woff',
    '.ttf': 'font/ttf'
};

// Utilisateur actif de session pour la prévisualisation
let sessionUser = {
    userId: 2,
    nom: 'Dupont',
    prenom: 'Jean',
    email: 'demandeur@hubpieces.fr',
    role: 'DEMANDEUR',
    telephone: '06 12 34 56 78',
    ville: 'Lyon',
    codePostal: '69001'
};

const server = http.createServer((req, res) => {
    // CORS headers
    res.setHeader('Access-Control-Allow-Origin', '*');
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS');
    res.setHeader('Access-Control-Allow-Headers', 'Content-Type, Authorization');

    if (req.method === 'OPTIONS') {
        res.writeHead(204);
        return res.end();
    }

    const url = new URL(req.url, `http://${req.headers.host}`);
    let pathname = decodeURIComponent(url.pathname);

    // ==================== API MOCKS ====================
    if (pathname.startsWith('/api/')) {
        res.setHeader('Content-Type', 'application/json; charset=utf-8');

        // Session / Auth
        if (pathname === '/api/auth/me') {
            return res.end(JSON.stringify(sessionUser));
        }
        if (pathname === '/api/auth/login' && req.method === 'POST') {
            return res.end(JSON.stringify({ success: true, ...sessionUser }));
        }
        if (pathname === '/api/auth/logout') {
            return res.end(JSON.stringify({ success: true }));
        }

        // Utilisateur courant
        if (pathname === '/api/utilisateurs/me') {
            if (req.method === 'PUT') {
                let body = '';
                req.on('data', chunk => body += chunk);
                req.on('end', () => {
                    try {
                        const data = JSON.parse(body);
                        Object.assign(sessionUser, data);
                        res.end(JSON.stringify({
                            id: sessionUser.userId,
                            ...sessionUser,
                            roles: [sessionUser.role],
                            rolePrincipal: sessionUser.role
                        }));
                    } catch (e) {
                        res.writeHead(400);
                        res.end(JSON.stringify({ message: 'JSON invalide' }));
                    }
                });
                return;
            }
            return res.end(JSON.stringify({
                id: sessionUser.userId,
                ...sessionUser,
                roles: [sessionUser.role],
                rolePrincipal: sessionUser.role
            }));
        }

        // Stats demandeur
        if (pathname === '/api/demandeur/stats') {
            return res.end(JSON.stringify({
                total: 5,
                nouvelles: 1,
                enCours: 2,
                cloturees: 1,
                annulees: 1,
                notificationsNonLues: 3
            }));
        }

        // Demandes demandeur
        if (pathname === '/api/demandeur/demandes') {
            return res.end(JSON.stringify([
                {
                    id: 1,
                    titre: 'Engrenage robot mixeur Moulinex Masterchef',
                    description: 'La roue dentée principale du bol de mixeur s\'est cassée après 5 ans d\'utilisation. Pièce introuvable en SAV officiel.',
                    marque: 'Moulinex',
                    modeleAppareil: 'Masterchef 5000',
                    materiauSouhaite: 'PETG ou Nylon',
                    budgetMax: 15.00,
                    ville: 'Lyon',
                    codePostal: '69001',
                    etat: 'RECHERCHE_MAKER',
                    nombreOffres: 2,
                    dateCreation: '2026-10-10T14:30:00'
                },
                {
                    id: 2,
                    titre: 'Bouton potentiomètre lave-linge Bosch Serie 6',
                    description: 'L\'axe intérieur du sélecteur de programme est fendu. Le bouton tourne dans le vide.',
                    marque: 'Bosch',
                    modeleAppareil: 'Serie 6 WAT28439FF',
                    materiauSouhaite: 'ABS blanc',
                    budgetMax: 12.00,
                    ville: 'Lyon',
                    codePostal: '69001',
                    etat: 'EN_COURS_IMPRESSION',
                    nombreOffres: 1,
                    dateCreation: '2026-10-08T09:15:00'
                },
                {
                    id: 3,
                    titre: 'Clip fixation pare-soleil Peugeot 208',
                    description: 'Passe-câble et agrafe de maintien du pare-soleil passager cassé net par temps froid.',
                    marque: 'Peugeot',
                    modeleAppareil: '208 (2018)',
                    materiauSouhaite: 'PETG résistant UV',
                    budgetMax: 18.00,
                    ville: 'Lyon',
                    codePostal: '69001',
                    etat: 'ATTENTE_VALIDATION',
                    nombreOffres: 1,
                    dateCreation: '2026-10-05T17:45:00'
                },
                {
                    id: 4,
                    titre: 'Support filtre aspirateur sans fil Dyson V8',
                    description: 'Pattes de verrouillage du capot arrière cassées. Besoin d\'un modèle 3D sur mesure.',
                    marque: 'Dyson',
                    modeleAppareil: 'V8 Absolute',
                    materiauSouhaite: 'PLA ou PETG',
                    budgetMax: 25.00,
                    ville: 'Lyon',
                    codePostal: '69001',
                    etat: 'MODELISATION_REQUISE',
                    nombreOffres: 0,
                    dateCreation: '2026-10-11T11:20:00'
                },
                {
                    id: 5,
                    titre: 'Poignée de bac à légumes réfrigérateur Samsung',
                    description: 'La poignée plastique transparente était brisée au niveau des ergots. Réparée et reçue avec succès.',
                    marque: 'Samsung',
                    modeleAppareil: 'RB29FWJNDSA',
                    materiauSouhaite: 'PLA Blanc',
                    budgetMax: 20.00,
                    ville: 'Lyon',
                    codePostal: '69001',
                    etat: 'CLOTUREE',
                    nombreOffres: 1,
                    dateCreation: '2026-09-24T18:00:00'
                }
            ]));
        }

        // Notifications demandeur
        if (pathname === '/api/demandeur/notifications') {
            return res.end(JSON.stringify([
                { id: 1, titre: 'Nouvelle offre reçue', message: 'Ahmed Benali a proposé 14.50 EUR pour votre engrenage Moulinex.', dateCreation: '2026-10-10T15:00:00', lu: false },
                { id: 2, titre: 'Impression lancée', message: 'Votre bouton de lave-linge Bosch est en cours d\'impression.', dateCreation: '2026-10-08T10:30:00', lu: false },
                { id: 3, titre: 'Livraison effectuée', message: 'Votre commande Samsung a été validée avec succès.', dateCreation: '2026-09-26T14:00:00', lu: true }
            ]));
        }

        // Catalogue STL public
        if (pathname === '/api/public/catalogue-stl') {
            return res.end(JSON.stringify([
                {
                    id: 1,
                    nom: 'Engrenage denté précision',
                    description: 'Pignon d\'entraînement hélicoïdal à haute résistance mécanique. Idéal pour robots de cuisine et petits appareils.',
                    categorie: 'electromenager',
                    marque: 'Moulinex',
                    modeleAppareil: 'Masterchef',
                    materiauRecommande: 'PETG / Nylon',
                    cheminFichier: 'images/model1.glb',
                    nombreTelechargements: 48,
                    noteMoyenne: 4.9
                },
                {
                    id: 2,
                    nom: 'Support articulé multi-axe',
                    description: 'Rotule orientable à double serrage pour lampes, caméras ou accessoires ménagers.',
                    categorie: 'quotidien',
                    marque: 'Universel',
                    modeleAppareil: 'Standard 20mm',
                    materiauRecommande: 'PLA haute densité',
                    cheminFichier: 'images/model2.glb',
                    nombreTelechargements: 76,
                    noteMoyenne: 4.8
                },
                {
                    id: 3,
                    nom: 'Molette crantée de réglage',
                    description: 'Bouton moleté intérieur méplat pour tableau de bord, climatisation ou commandes.',
                    categorie: 'automobile',
                    marque: 'Peugeot',
                    modeleAppareil: '208',
                    materiauRecommande: 'ABS résistant UV',
                    cheminFichier: 'images/model3.glb',
                    nombreTelechargements: 35,
                    noteMoyenne: 5.0
                },
                {
                    id: 4,
                    nom: 'Embout adaptateur turbine',
                    description: 'Raccord conique étanche pour filtration et flux d\'air haute vélocité.',
                    categorie: 'electromenager',
                    marque: 'Dyson',
                    modeleAppareil: 'V8 / V10',
                    materiauRecommande: 'PETG',
                    cheminFichier: 'images/model4.glb',
                    nombreTelechargements: 92,
                    noteMoyenne: 4.9
                }
            ]));
        }

        // Profil public utilisateur
        const pubUserMatch = pathname.match(/^\/api\/public\/utilisateurs\/(\d+)$/);
        if (pubUserMatch) {
            const id = parseInt(pubUserMatch[1]);
            if (id === 4) {
                return res.end(JSON.stringify({
                    id: 4,
                    nom: 'Martin',
                    prenom: 'Sophie',
                    roles: ['EXPERT_CAO'],
                    rolePrincipal: 'EXPERT_CAO',
                    ville: 'Bordeaux',
                    codePostal: '33000',
                    noteMoyenne: 5.0,
                    nombreAvis: 32,
                    profilExpert: {
                        description: 'Ingénieure mécanique spécialisée en rétro-ingénierie et dessin technique 3D paramétrique.',
                        logicielsMaitrise: 'Fusion 360, Blender, SolidWorks',
                        portfolioUrl: 'https://portfolio.hubpieces.fr/sophie'
                    }
                }));
            }
            return res.end(JSON.stringify({
                id: 3,
                nom: 'Benali',
                prenom: 'Ahmed',
                roles: ['MAKER'],
                rolePrincipal: 'MAKER',
                ville: 'Marseille',
                codePostal: '13001',
                noteMoyenne: 4.9,
                nombreAvis: 47,
                profilMaker: {
                    descriptionImprimante: 'Prusa i3 MK3S+ et Ender 3 V2 avec direct drive',
                    materiauxDisponibles: 'PLA, ABS, PETG, TPU',
                    rayonActionKm: 30,
                    badge: 'EXPERT'
                }
            }));
        }

        // Fallback générique API
        return res.end(JSON.stringify({ success: true }));
    }

    // ==================== STATIC FILES ====================
    if (pathname === '/' || pathname === '') {
        pathname = '/index.html';
    }
    // Nettoyer URLs sans extension
    if (!path.extname(pathname)) {
        if (fs.existsSync(path.join(STATIC_DIR, pathname + '.html'))) {
            pathname += '.html';
        }
    }

    const filePath = path.join(STATIC_DIR, pathname);

    // Sécurité chemin
    if (!filePath.startsWith(STATIC_DIR)) {
        res.writeHead(403);
        return res.end('Accès interdit');
    }

    fs.stat(filePath, (err, stats) => {
        if (err || !stats.isFile()) {
            res.writeHead(404, { 'Content-Type': 'text/plain; charset=utf-8' });
            return res.end('Fichier introuvable');
        }

        const ext = path.extname(filePath).toLowerCase();
        const contentType = MIME_TYPES[ext] || 'application/octet-stream';

        res.writeHead(200, {
            'Content-Type': contentType,
            'Content-Length': stats.size,
            'Cache-Control': 'no-cache'
        });

        const stream = fs.createReadStream(filePath);
        stream.pipe(res);
    });
});

server.listen(PORT, '0.0.0.0', () => {
    console.log(`[HUBPIECES] Serveur de prévisualisation actif sur http://0.0.0.0:${PORT}`);
});

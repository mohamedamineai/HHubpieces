package com.hubpieces.config;

import com.hubpieces.model.*;
import com.hubpieces.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner initData(UtilisateurRepository utilisateurRepository,
                                       RoleRepository roleRepository,
                                       ProfilMakerRepository profilMakerRepository,
                                       ProfilExpertCaoRepository profilExpertCaoRepository,
                                       DemandeRepository demandeRepository,
                                       FichierStlRepository fichierStlRepository,
                                       OffreMakerRepository offreMakerRepository,
                                       TransactionRepository transactionRepository,
                                       AvisRepository avisRepository,
                                       NotificationRepository notificationRepository,
                                       MessageRepository messageRepository,
                                       PasswordEncoder passwordEncoder) {
        return args -> {
            // 1. Créer les rôles si absents
            for (RoleNom nom : RoleNom.values()) {
                if (roleRepository.findByNom(nom).isEmpty()) {
                    roleRepository.save(new Role(nom));
                }
            }

            // 2. Administrateur
            Utilisateur admin = utilisateurRepository.findByEmail("admin@hubpieces.fr").orElse(null);
            if (admin == null) {
                admin = new Utilisateur("Admin", "Hubpieces",
                        "admin@hubpieces.fr", passwordEncoder.encode("admin123"));
                Set<Role> roles = new HashSet<>();
                roles.add(roleRepository.findByNom(RoleNom.ADMIN).orElseThrow());
                admin.setRoles(roles);
                admin.setVille("Paris");
                admin.setCodePostal("75001");
                admin = utilisateurRepository.save(admin);
            }

            // 3. Demandeur
            Utilisateur demandeur = utilisateurRepository.findByEmail("demandeur@hubpieces.fr").orElse(null);
            if (demandeur == null) {
                demandeur = new Utilisateur("Dupont", "Jean",
                        "demandeur@hubpieces.fr", passwordEncoder.encode("demo1234"));
                Set<Role> roles = new HashSet<>();
                roles.add(roleRepository.findByNom(RoleNom.DEMANDEUR).orElseThrow());
                demandeur.setRoles(roles);
                demandeur.setVille("Lyon");
                demandeur.setCodePostal("69001");
                demandeur.setTelephone("06 12 34 56 78");
                demandeur = utilisateurRepository.save(demandeur);
            }

            // 4. Maker
            Utilisateur maker = utilisateurRepository.findByEmail("maker@hubpieces.fr").orElse(null);
            if (maker == null) {
                maker = new Utilisateur("Benali", "Ahmed",
                        "maker@hubpieces.fr", passwordEncoder.encode("demo1234"));
                Set<Role> roles = new HashSet<>();
                roles.add(roleRepository.findByNom(RoleNom.MAKER).orElseThrow());
                maker.setRoles(roles);
                maker.setVille("Marseille");
                maker.setCodePostal("13001");
                maker.setTelephone("06 98 76 54 32");
                maker = utilisateurRepository.save(maker);

                ProfilMaker profil = new ProfilMaker(maker);
                profil.setRayonActionKm(30);
                profil.setMateriauxDisponibles("PLA, ABS, PETG, TPU");
                profil.setDescriptionImprimante("Prusa i3 MK3S+ et Ender 3 V2");
                profil.setBadge(BadgeMaker.CONFIRME);
                profilMakerRepository.save(profil);
            }

            // 5. Expert CAO
            Utilisateur expert = utilisateurRepository.findByEmail("expert@hubpieces.fr").orElse(null);
            if (expert == null) {
                expert = new Utilisateur("Martin", "Sophie",
                        "expert@hubpieces.fr", passwordEncoder.encode("demo1234"));
                Set<Role> roles = new HashSet<>();
                roles.add(roleRepository.findByNom(RoleNom.EXPERT_CAO).orElseThrow());
                expert.setRoles(roles);
                expert.setVille("Bordeaux");
                expert.setCodePostal("33000");
                expert.setTelephone("06 45 67 89 01");
                expert = utilisateurRepository.save(expert);

                ProfilExpertCao profil = new ProfilExpertCao(expert);
                profil.setLogicielsMaitrise("Fusion 360, Blender, SolidWorks");
                profil.setPortfolioUrl("https://portfolio.hubpieces.fr/sophie");
                profil.setDescription("Experte CAO spécialisée en rétro-ingénierie et dessin technique 3D");
                profilExpertCaoRepository.save(profil);
            }

            // 6. Fichiers STL initiaux (si catalogue vide)
            if (fichierStlRepository.count() == 0) {
                FichierStl f1 = new FichierStl();
                f1.setNom("Engrenage denté précision");
                f1.setDescription("Pignon d'entraînement hélicoïdal à haute résistance mécanique.");
                f1.setCategorie("electromenager");
                f1.setMarque("Moulinex");
                f1.setModeleAppareil("Masterchef 5000");
                f1.setMateriauRecommande("PETG / Nylon");
                f1.setCheminFichier("images/model1.glb");
                f1.setNombreTelechargements(48);
                f1.setNoteMoyenne(4.9);
                f1.setEtatValidation(EtatValidation.VALIDE);
                f1.setUploadeur(expert);
                f1.setValideur(admin);
                fichierStlRepository.save(f1);

                FichierStl f2 = new FichierStl();
                f2.setNom("Support articulé multi-axe");
                f2.setDescription("Rotule orientable à double serrage universelle.");
                f2.setCategorie("quotidien");
                f2.setMarque("Universel");
                f2.setMateriauRecommande("PLA haute densité");
                f2.setCheminFichier("images/model2.glb");
                f2.setNombreTelechargements(76);
                f2.setNoteMoyenne(4.8);
                f2.setEtatValidation(EtatValidation.VALIDE);
                f2.setUploadeur(expert);
                f2.setValideur(admin);
                fichierStlRepository.save(f2);

                FichierStl f3 = new FichierStl();
                f3.setNom("Molette crantée de réglage");
                f3.setDescription("Bouton moleté intérieur méplat pour commandes mécaniques.");
                f3.setCategorie("automobile");
                f3.setMarque("Peugeot");
                f3.setModeleAppareil("208");
                f3.setMateriauRecommande("ABS résistant UV");
                f3.setCheminFichier("images/model3.glb");
                f3.setNombreTelechargements(35);
                f3.setNoteMoyenne(5.0);
                f3.setEtatValidation(EtatValidation.VALIDE);
                f3.setUploadeur(expert);
                f3.setValideur(admin);
                fichierStlRepository.save(f3);

                FichierStl f4 = new FichierStl();
                f4.setNom("Embout adaptateur turbine");
                f4.setDescription("Raccord conique étanche pour filtration et flux d'air.");
                f4.setCategorie("electromenager");
                f4.setMarque("Dyson");
                f4.setModeleAppareil("V8 Absolute");
                f4.setMateriauRecommande("PETG");
                f4.setCheminFichier("images/model4.glb");
                f4.setNombreTelechargements(92);
                f4.setNoteMoyenne(4.9);
                f4.setEtatValidation(EtatValidation.VALIDE);
                f4.setUploadeur(expert);
                f4.setValideur(admin);
                fichierStlRepository.save(f4);
            }

            // 7. Demandes initiales pour le demandeur (si aucune demande existante)
            if (demandeRepository.count() == 0) {
                // Demande 1 : En recherche de maker
                Demande d1 = new Demande("Engrenage robot mixeur Moulinex Masterchef",
                        "La roue dentée principale du bol de mixeur s'est cassée après 5 ans d'utilisation. Pièce introuvable en SAV officiel. Nécessite une impression résistante aux efforts de torsion.",
                        demandeur);
                d1.setMarque("Moulinex");
                d1.setModeleAppareil("Masterchef 5000");
                d1.setMateriauSouhaite("PETG");
                d1.setBudgetMax(15.00);
                d1.setVille("Lyon");
                d1.setCodePostal("69001");
                d1.setEtat(EtatDemande.RECHERCHE_MAKER);
                Demande savedD1 = demandeRepository.save(d1);

                // Offre pour d1
                OffreMaker o1 = new OffreMaker(savedD1, maker, 14.50, 2);
                o1.setModeLivraison(ModeLivraison.REMISE_EN_MAIN);
                o1.setMessage("Bonjour, j'ai du PETG noir renforcé prêt dans mon imprimante Prusa MK3S+.");
                o1.setEtat(EtatOffre.EN_ATTENTE);
                offreMakerRepository.save(o1);

                // Demande 2 : En cours d'impression
                Demande d2 = new Demande("Bouton potentiomètre lave-linge Bosch Serie 6",
                        "L'axe intérieur du sélecteur de programme est fendu. Le bouton tourne dans le vide. Besoin d'un nouveau bouton avec méplat intérieur de 6mm.",
                        demandeur);
                d2.setMarque("Bosch");
                d2.setModeleAppareil("Serie 6 WAT28439FF");
                d2.setMateriauSouhaite("ABS");
                d2.setBudgetMax(12.00);
                d2.setVille("Lyon");
                d2.setCodePostal("69001");
                d2.setEtat(EtatDemande.EN_COURS_IMPRESSION);
                Demande savedD2 = demandeRepository.save(d2);

                OffreMaker o2 = new OffreMaker(savedD2, maker, 11.00, 3);
                o2.setModeLivraison(ModeLivraison.LIVRAISON_POSTALE);
                o2.setMessage("Impression en ABS blanc lancée à 240°C avec remplissage à 80%.");
                o2.setEtat(EtatOffre.ACCEPTEE);
                OffreMaker savedO2 = offreMakerRepository.save(o2);

                Transaction t2 = new Transaction(savedO2, 11.00);
                t2.setEtatPaiement(EtatPaiement.EN_SEQUESTRE);
                t2.setDatePaiement(LocalDateTime.now());
                transactionRepository.save(t2);

                // Demande 3 : À valider
                Demande d3 = new Demande("Clip fixation pare-soleil Peugeot 208",
                        "Passe-câble et agrafe de maintien du pare-soleil passager cassé net par temps froid.",
                        demandeur);
                d3.setMarque("Peugeot");
                d3.setModeleAppareil("208");
                d3.setMateriauSouhaite("PETG");
                d3.setBudgetMax(18.00);
                d3.setVille("Lyon");
                d3.setCodePostal("69001");
                d3.setEtat(EtatDemande.ATTENTE_VALIDATION);
                demandeRepository.save(d3);

                // Demande 4 : À modéliser
                Demande d4 = new Demande("Support filtre aspirateur sans fil Dyson V8",
                        "Pattes de verrouillage du capot arrière cassées. Besoin d'un modèle 3D sur mesure.",
                        demandeur);
                d4.setMarque("Dyson");
                d4.setModeleAppareil("V8 Absolute");
                d4.setMateriauSouhaite("PLA / PETG");
                d4.setBudgetMax(25.00);
                d4.setVille("Lyon");
                d4.setCodePostal("69001");
                d4.setEtat(EtatDemande.MODELISATION_REQUISE);
                demandeRepository.save(d4);

                // Demande 5 : Clôturée avec Avis
                Demande d5 = new Demande("Poignée de tiroir réfrigérateur Samsung",
                        "Poignée plastique transparente brisée au niveau des ergots de maintien.",
                        demandeur);
                d5.setMarque("Samsung");
                d5.setModeleAppareil("RB29FWJNDSA");
                d5.setMateriauSouhaite("PLA");
                d5.setBudgetMax(20.00);
                d5.setVille("Lyon");
                d5.setCodePostal("69001");
                d5.setEtat(EtatDemande.CLOTUREE);
                Demande savedD5 = demandeRepository.save(d5);

                OffreMaker o5 = new OffreMaker(savedD5, maker, 18.00, 2);
                o5.setModeLivraison(ModeLivraison.REMISE_EN_MAIN);
                o5.setMessage("Pièce imprimée et livrée.");
                o5.setEtat(EtatOffre.ACCEPTEE);
                OffreMaker savedO5 = offreMakerRepository.save(o5);

                Transaction t5 = new Transaction(savedO5, 18.00);
                t5.setEtatPaiement(EtatPaiement.LIBERE);
                t5.setDatePaiement(LocalDateTime.now().minusDays(5));
                t5.setDateLiberaion(LocalDateTime.now().minusDays(2));
                Transaction savedT5 = transactionRepository.save(t5);

                Avis avis5 = new Avis(savedT5, demandeur, maker, 5, "Pièce conforme à l'originale, impression rapide et soignée !");
                avis5.setPublie(true);
                avis5.setDatePublication(LocalDateTime.now().minusDays(2));
                avisRepository.save(avis5);

                // Messages de bienvenue
                Message m1 = new Message(savedD1, demandeur, maker, "Bonjour, est-il possible d'imprimer l'engrenage en noir mat résistant ?");
                messageRepository.save(m1);

                Message m2 = new Message(savedD1, maker, demandeur, "Bonjour Jean ! Oui tout à fait, j'ai du filament PETG noir prêt dans ma Prusa.");
                messageRepository.save(m2);

                // Notifications
                notificationRepository.save(new Notification(demandeur, "Nouvelle offre reçue", "Ahmed Benali a déposé une offre de 14.50 EUR pour votre engrenage Moulinex.", TypeNotification.INFO));
                notificationRepository.save(new Notification(demandeur, "Impression en cours", "Votre commande de bouton de lave-linge est actuellement en cours d'impression 3D.", TypeNotification.INFO));
                notificationRepository.save(new Notification(maker, "Offre acceptée !", "Le demandeur Jean Dupont a validé votre proposition de 11.00 EUR.", TypeNotification.SUCCESS));
            }
        };
    }
}

package com.hubpieces.config;

import com.hubpieces.model.*;
import com.hubpieces.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner initData(UtilisateurRepository utilisateurRepository,
                                       RoleRepository roleRepository,
                                       ProfilMakerRepository profilMakerRepository,
                                       ProfilExpertCaoRepository profilExpertCaoRepository,
                                       PasswordEncoder passwordEncoder) {
        return args -> {
            // 1. Creer les roles
            for (RoleNom nom : RoleNom.values()) {
                if (roleRepository.findByNom(nom).isEmpty()) {
                    roleRepository.save(new Role(nom));
                }
            }

            // 2. Creer un admin par defaut
            if (!utilisateurRepository.existsByEmail("admin@hubpieces.fr")) {
                Utilisateur admin = new Utilisateur("Admin", "Hubpieces",
                        "admin@hubpieces.fr", passwordEncoder.encode("admin123"));
                Set<Role> roles = new HashSet<>();
                roles.add(roleRepository.findByNom(RoleNom.ADMIN).orElseThrow());
                admin.setRoles(roles);
                admin.setVille("Paris");
                admin.setCodePostal("75001");
                utilisateurRepository.save(admin);
            }

            // 3. Creer un demandeur de demo
            if (!utilisateurRepository.existsByEmail("demandeur@hubpieces.fr")) {
                Utilisateur demandeur = new Utilisateur("Dupont", "Jean",
                        "demandeur@hubpieces.fr", passwordEncoder.encode("demo1234"));
                Set<Role> roles = new HashSet<>();
                roles.add(roleRepository.findByNom(RoleNom.DEMANDEUR).orElseThrow());
                demandeur.setRoles(roles);
                demandeur.setVille("Lyon");
                demandeur.setCodePostal("69001");
                utilisateurRepository.save(demandeur);
            }

            // 4. Creer un maker de demo
            if (!utilisateurRepository.existsByEmail("maker@hubpieces.fr")) {
                Utilisateur maker = new Utilisateur("Benali", "Ahmed",
                        "maker@hubpieces.fr", passwordEncoder.encode("demo1234"));
                Set<Role> roles = new HashSet<>();
                roles.add(roleRepository.findByNom(RoleNom.MAKER).orElseThrow());
                maker.setRoles(roles);
                maker.setVille("Marseille");
                maker.setCodePostal("13001");
                Utilisateur savedMaker = utilisateurRepository.save(maker);
                ProfilMaker profil = new ProfilMaker(savedMaker);
                profil.setRayonActionKm(30);
                profil.setMateriauxDisponibles("PLA, ABS, PETG");
                profil.setDescriptionImprimante("Prusa MK3S+ et Ender 3 V2");
                profil.setBadge(BadgeMaker.CONFIRME);
                profilMakerRepository.save(profil);
            }

            // 5. Creer un expert CAO de demo
            if (!utilisateurRepository.existsByEmail("expert@hubpieces.fr")) {
                Utilisateur expert = new Utilisateur("Martin", "Sophie",
                        "expert@hubpieces.fr", passwordEncoder.encode("demo1234"));
                Set<Role> roles = new HashSet<>();
                roles.add(roleRepository.findByNom(RoleNom.EXPERT_CAO).orElseThrow());
                expert.setRoles(roles);
                expert.setVille("Bordeaux");
                expert.setCodePostal("33000");
                Utilisateur savedExpert = utilisateurRepository.save(expert);
                ProfilExpertCao profil = new ProfilExpertCao(savedExpert);
                profil.setLogicielsMaitrise("Fusion 360, Blender, SolidWorks");
                profil.setPortfolioUrl("https://portfolio.hubpieces.fr/sophie");
                profil.setDescription("Experte CAO specialisee en retro-engineering de pieces cassees");
                profilExpertCaoRepository.save(profil);
            }
        };
    }
}

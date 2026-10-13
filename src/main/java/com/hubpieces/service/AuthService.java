package com.hubpieces.service;

import com.hubpieces.dto.AuthResponse;
import com.hubpieces.dto.LoginRequest;
import com.hubpieces.dto.RegisterRequest;
import com.hubpieces.model.ProfilExpertCao;
import com.hubpieces.model.ProfilMaker;
import com.hubpieces.model.Role;
import com.hubpieces.model.RoleNom;
import com.hubpieces.model.StatutUtilisateur;
import com.hubpieces.model.Utilisateur;
import com.hubpieces.repository.ProfilExpertCaoRepository;
import com.hubpieces.repository.ProfilMakerRepository;
import com.hubpieces.repository.RoleRepository;
import com.hubpieces.repository.UtilisateurRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final ProfilMakerRepository profilMakerRepository;
    private final ProfilExpertCaoRepository profilExpertCaoRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UtilisateurRepository utilisateurRepository,
                        RoleRepository roleRepository,
                        ProfilMakerRepository profilMakerRepository,
                        ProfilExpertCaoRepository profilExpertCaoRepository,
                        PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.roleRepository = roleRepository;
        this.profilMakerRepository = profilMakerRepository;
        this.profilExpertCaoRepository = profilExpertCaoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse register(RegisterRequest request) {
        if (request == null || request.getRole() == null) {
            return new AuthResponse(false, "Veuillez choisir un rôle", null, null, null, null, null);
        }
        if (request.getRole() == RoleNom.ADMIN) {
            return new AuthResponse(false, "La création d'un compte administrateur est réservée à la configuration de la plateforme", null, null, null, null, null);
        }

        if (request.getEmail() == null || request.getEmail().isBlank()
                || request.getMotDePasse() == null || request.getMotDePasse().isBlank()
                || request.getNom() == null || request.getNom().isBlank()
                || request.getPrenom() == null || request.getPrenom().isBlank()) {
            return new AuthResponse(false, "Nom, prénom, email et mot de passe sont obligatoires", null, null, null, null, null);
        }

        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse(false, "Cet email est déjà utilisé", null, null, null, null, null);
        }

        Role role = roleRepository.findByNom(request.getRole())
                .orElseThrow(() -> new IllegalStateException("Rôle introuvable : " + request.getRole()));

        Utilisateur utilisateur = new Utilisateur(
                request.getNom(),
                request.getPrenom(),
                request.getEmail(),
                passwordEncoder.encode(request.getMotDePasse())
        );
        utilisateur.setTelephone(request.getTelephone());
        utilisateur.setVille(request.getVille());
        utilisateur.setCodePostal(request.getCodePostal());

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        utilisateur.setRoles(roles);

        Utilisateur saved = utilisateurRepository.save(utilisateur);

        if (request.getRole() == RoleNom.MAKER) {
            ProfilMaker profil = new ProfilMaker(saved);
            if (request.getRayonActionKm() != null) {
                profil.setRayonActionKm(request.getRayonActionKm());
            }
            profil.setMateriauxDisponibles(request.getMateriauxDisponibles());
            profil.setDescriptionImprimante(request.getDescriptionImprimante());
            profilMakerRepository.save(profil);
        } else if (request.getRole() == RoleNom.EXPERT_CAO) {
            ProfilExpertCao profil = new ProfilExpertCao(saved);
            profil.setLogicielsMaitrise(request.getLogicielsMaitrise());
            profil.setPortfolioUrl(request.getPortfolioUrl());
            profil.setDescription(request.getDescriptionExpert());
            profilExpertCaoRepository.save(profil);
        }

        return new AuthResponse(true, "Inscription réussie", saved.getId(), saved.getNom(), saved.getPrenom(), saved.getEmail(), request.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        Optional<Utilisateur> opt = utilisateurRepository.findByEmail(request.getEmail());

        if (opt.isEmpty() || !passwordEncoder.matches(request.getMotDePasse(), opt.get().getMotDePasse())) {
            return new AuthResponse(false, "Email ou mot de passe incorrect", null, null, null, null, null);
        }

        Utilisateur utilisateur = opt.get();
        if (utilisateur.getStatut() != StatutUtilisateur.ACTIF) {
            return new AuthResponse(false, "Ce compte est suspendu ou en attente d'activation", null, null, null, null, null);
        }

        String role = RoleNom.principal(utilisateur.getRoles().stream()
                .map(Role::getNom)
                .collect(java.util.stream.Collectors.toSet()))
                .name();

        return new AuthResponse(true, "Connexion réussie", utilisateur.getId(), utilisateur.getNom(), utilisateur.getPrenom(), utilisateur.getEmail(), role);
    }
}

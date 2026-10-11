-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : ven. 25 sep. 2026 à 19:52
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `hubpieces_db`
--

-- --------------------------------------------------------

--
-- Structure de la table `avis`
--

CREATE TABLE `avis` (
  `id` bigint(20) NOT NULL,
  `transaction_id` bigint(20) NOT NULL,
  `auteur_id` bigint(20) NOT NULL,
  `destinataire_id` bigint(20) NOT NULL,
  `note` int(11) NOT NULL CHECK (`note` between 1 and 5),
  `commentaire` text NOT NULL,
  `publie` tinyint(1) DEFAULT 0,
  `date_creation` datetime DEFAULT current_timestamp(),
  `date_publication` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `demande`
--

CREATE TABLE `demande` (
  `id` bigint(20) NOT NULL,
  `titre` varchar(200) NOT NULL,
  `description` text NOT NULL,
  `marque` varchar(100) DEFAULT NULL,
  `modele_appareil` varchar(100) DEFAULT NULL,
  `materiau_souhaite` varchar(50) DEFAULT NULL,
  `budget_max` double DEFAULT NULL,
  `ville` varchar(100) DEFAULT NULL,
  `code_postal` varchar(10) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `etat` enum('NOUVELLE','MODELISATION_REQUISE','RECHERCHE_MAKER','ATTENTE_VALIDATION','EN_COURS_IMPRESSION','ANNULEE','CLOTUREE') DEFAULT 'NOUVELLE',
  `date_creation` datetime DEFAULT current_timestamp(),
  `date_modification` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `demandeur_id` bigint(20) NOT NULL,
  `fichier_stl_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `fichier_stl`
--

CREATE TABLE `fichier_stl` (
  `id` bigint(20) NOT NULL,
  `nom` varchar(200) NOT NULL,
  `description` text DEFAULT NULL,
  `chemin_fichier` varchar(255) NOT NULL,
  `taille_fichier` bigint(20) DEFAULT NULL,
  `categorie` varchar(100) DEFAULT NULL,
  `tags` varchar(255) DEFAULT NULL,
  `marque` varchar(100) DEFAULT NULL,
  `modele_appareil` varchar(100) DEFAULT NULL,
  `materiau_recommande` varchar(50) DEFAULT NULL,
  `dimensions` varchar(100) DEFAULT NULL,
  `note_moyenne` double DEFAULT 0,
  `nombre_telechargements` int(11) DEFAULT 0,
  `etat_validation` enum('EN_ATTENTE','VALIDE','REJETE') DEFAULT 'EN_ATTENTE',
  `date_upload` datetime DEFAULT current_timestamp(),
  `uploadeur_id` bigint(20) NOT NULL,
  `valideur_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `litige`
--

CREATE TABLE `litige` (
  `id` bigint(20) NOT NULL,
  `transaction_id` bigint(20) NOT NULL,
  `demandeur_id` bigint(20) NOT NULL,
  `raison` text NOT NULL,
  `etat` enum('OUVERT','EN_COURS','RESOLU','FERME') DEFAULT 'OUVERT',
  `decision` text DEFAULT NULL,
  `date_ouverture` datetime DEFAULT current_timestamp(),
  `date_resolution` datetime DEFAULT NULL,
  `admin_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `message`
--

CREATE TABLE `message` (
  `id` bigint(20) NOT NULL,
  `demande_id` bigint(20) NOT NULL,
  `expediteur_id` bigint(20) NOT NULL,
  `destinataire_id` bigint(20) NOT NULL,
  `contenu` text NOT NULL,
  `lu` tinyint(1) DEFAULT 0,
  `date_envoi` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `notification`
--

CREATE TABLE `notification` (
  `id` bigint(20) NOT NULL,
  `utilisateur_id` bigint(20) NOT NULL,
  `titre` varchar(200) NOT NULL,
  `message` text NOT NULL,
  `type` enum('INFO','SUCCESS','WARNING','ERROR') DEFAULT 'INFO',
  `lu` tinyint(1) DEFAULT 0,
  `date_creation` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `offre_maker`
--

CREATE TABLE `offre_maker` (
  `id` bigint(20) NOT NULL,
  `demande_id` bigint(20) NOT NULL,
  `maker_id` bigint(20) NOT NULL,
  `prix` double NOT NULL,
  `delai_jours` int(11) NOT NULL,
  `message` text DEFAULT NULL,
  `mode_livraison` enum('REMISE_EN_MAIN','LIVRAISON_POSTALE') DEFAULT 'REMISE_EN_MAIN',
  `etat` enum('EN_ATTENTE','ACCEPTEE','REFUSEE','ANNULEE') DEFAULT 'EN_ATTENTE',
  `date_creation` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `photo_demande`
--

CREATE TABLE `photo_demande` (
  `id` bigint(20) NOT NULL,
  `demande_id` bigint(20) NOT NULL,
  `chemin_photo` varchar(255) NOT NULL,
  `date_upload` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `profil_expert_cao`
--

CREATE TABLE `profil_expert_cao` (
  `id` bigint(20) NOT NULL,
  `utilisateur_id` bigint(20) NOT NULL,
  `logiciels_maitrise` varchar(255) DEFAULT NULL,
  `portfolio_url` varchar(255) DEFAULT NULL,
  `description` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `profil_maker`
--

CREATE TABLE `profil_maker` (
  `id` bigint(20) NOT NULL,
  `utilisateur_id` bigint(20) NOT NULL,
  `rayon_action_km` int(11) DEFAULT 20,
  `materiaux_disponibles` varchar(255) DEFAULT NULL,
  `description_imprimante` text DEFAULT NULL,
  `nombre_commandes_actives` int(11) DEFAULT 0,
  `nombre_avis_positifs` int(11) DEFAULT 0,
  `badge` enum('DEBUTANT','CONFIRME','EXPERT') DEFAULT 'DEBUTANT'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `role`
--

CREATE TABLE `role` (
  `id` bigint(20) NOT NULL,
  `nom` enum('DEMANDEUR','MAKER','EXPERT_CAO','ADMIN') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `role`
--

INSERT INTO `role` (`id`, `nom`) VALUES
(1, 'DEMANDEUR'),
(2, 'MAKER'),
(3, 'EXPERT_CAO'),
(4, 'ADMIN');

-- --------------------------------------------------------

--
-- Structure de la table `transaction`
--

CREATE TABLE `transaction` (
  `id` bigint(20) NOT NULL,
  `offre_maker_id` bigint(20) NOT NULL,
  `montant_total` double NOT NULL,
  `commission_plateforme` double NOT NULL,
  `montant_maker` double NOT NULL,
  `montant_expert_cao` double DEFAULT 0,
  `etat_paiement` enum('EN_ATTENTE','PAYE','EN_SEQUESTRE','LIBERE','REMBOURSE','LITIGE') DEFAULT 'EN_ATTENTE',
  `date_paiement` datetime DEFAULT NULL,
  `date_liberation` datetime DEFAULT NULL,
  `date_creation` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--

CREATE TABLE `utilisateur` (
  `id` bigint(20) NOT NULL,
  `nom` varchar(100) NOT NULL,
  `prenom` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL,
  `mot_de_passe` varchar(255) NOT NULL,
  `telephone` varchar(20) DEFAULT NULL,
  `ville` varchar(100) DEFAULT NULL,
  `code_postal` varchar(10) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `note_moyenne` double DEFAULT 0,
  `nombre_avis` int(11) DEFAULT 0,
  `statut` enum('ACTIF','SUSPENDU','EN_ATTENTE') DEFAULT 'ACTIF',
  `date_inscription` datetime DEFAULT current_timestamp(),
  `photo_profil` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur_role`
--

CREATE TABLE `utilisateur_role` (
  `utilisateur_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `avis`
--
ALTER TABLE `avis`
  ADD PRIMARY KEY (`id`),
  ADD KEY `transaction_id` (`transaction_id`),
  ADD KEY `auteur_id` (`auteur_id`),
  ADD KEY `destinataire_id` (`destinataire_id`);

--
-- Index pour la table `demande`
--
ALTER TABLE `demande`
  ADD PRIMARY KEY (`id`),
  ADD KEY `demandeur_id` (`demandeur_id`),
  ADD KEY `fichier_stl_id` (`fichier_stl_id`);

--
-- Index pour la table `fichier_stl`
--
ALTER TABLE `fichier_stl`
  ADD PRIMARY KEY (`id`),
  ADD KEY `uploadeur_id` (`uploadeur_id`),
  ADD KEY `valideur_id` (`valideur_id`);

--
-- Index pour la table `litige`
--
ALTER TABLE `litige`
  ADD PRIMARY KEY (`id`),
  ADD KEY `transaction_id` (`transaction_id`),
  ADD KEY `demandeur_id` (`demandeur_id`),
  ADD KEY `admin_id` (`admin_id`);

--
-- Index pour la table `message`
--
ALTER TABLE `message`
  ADD PRIMARY KEY (`id`),
  ADD KEY `demande_id` (`demande_id`),
  ADD KEY `expediteur_id` (`expediteur_id`),
  ADD KEY `destinataire_id` (`destinataire_id`);

--
-- Index pour la table `notification`
--
ALTER TABLE `notification`
  ADD PRIMARY KEY (`id`),
  ADD KEY `utilisateur_id` (`utilisateur_id`);

--
-- Index pour la table `offre_maker`
--
ALTER TABLE `offre_maker`
  ADD PRIMARY KEY (`id`),
  ADD KEY `demande_id` (`demande_id`),
  ADD KEY `maker_id` (`maker_id`);

--
-- Index pour la table `photo_demande`
--
ALTER TABLE `photo_demande`
  ADD PRIMARY KEY (`id`),
  ADD KEY `demande_id` (`demande_id`);

--
-- Index pour la table `profil_expert_cao`
--
ALTER TABLE `profil_expert_cao`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `utilisateur_id` (`utilisateur_id`);

--
-- Index pour la table `profil_maker`
--
ALTER TABLE `profil_maker`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `utilisateur_id` (`utilisateur_id`);

--
-- Index pour la table `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `transaction`
--
ALTER TABLE `transaction`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `offre_maker_id` (`offre_maker_id`);

--
-- Index pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Index pour la table `utilisateur_role`
--
ALTER TABLE `utilisateur_role`
  ADD PRIMARY KEY (`utilisateur_id`,`role_id`),
  ADD KEY `role_id` (`role_id`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `avis`
--
ALTER TABLE `avis`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `demande`
--
ALTER TABLE `demande`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `fichier_stl`
--
ALTER TABLE `fichier_stl`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `litige`
--
ALTER TABLE `litige`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `message`
--
ALTER TABLE `message`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `notification`
--
ALTER TABLE `notification`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `offre_maker`
--
ALTER TABLE `offre_maker`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `photo_demande`
--
ALTER TABLE `photo_demande`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `profil_expert_cao`
--
ALTER TABLE `profil_expert_cao`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `profil_maker`
--
ALTER TABLE `profil_maker`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `role`
--
ALTER TABLE `role`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `transaction`
--
ALTER TABLE `transaction`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `avis`
--
ALTER TABLE `avis`
  ADD CONSTRAINT `avis_ibfk_1` FOREIGN KEY (`transaction_id`) REFERENCES `transaction` (`id`),
  ADD CONSTRAINT `avis_ibfk_2` FOREIGN KEY (`auteur_id`) REFERENCES `utilisateur` (`id`),
  ADD CONSTRAINT `avis_ibfk_3` FOREIGN KEY (`destinataire_id`) REFERENCES `utilisateur` (`id`);

--
-- Contraintes pour la table `demande`
--
ALTER TABLE `demande`
  ADD CONSTRAINT `demande_ibfk_1` FOREIGN KEY (`demandeur_id`) REFERENCES `utilisateur` (`id`),
  ADD CONSTRAINT `demande_ibfk_2` FOREIGN KEY (`fichier_stl_id`) REFERENCES `fichier_stl` (`id`);

--
-- Contraintes pour la table `fichier_stl`
--
ALTER TABLE `fichier_stl`
  ADD CONSTRAINT `fichier_stl_ibfk_1` FOREIGN KEY (`uploadeur_id`) REFERENCES `utilisateur` (`id`),
  ADD CONSTRAINT `fichier_stl_ibfk_2` FOREIGN KEY (`valideur_id`) REFERENCES `utilisateur` (`id`);

--
-- Contraintes pour la table `litige`
--
ALTER TABLE `litige`
  ADD CONSTRAINT `litige_ibfk_1` FOREIGN KEY (`transaction_id`) REFERENCES `transaction` (`id`),
  ADD CONSTRAINT `litige_ibfk_2` FOREIGN KEY (`demandeur_id`) REFERENCES `utilisateur` (`id`),
  ADD CONSTRAINT `litige_ibfk_3` FOREIGN KEY (`admin_id`) REFERENCES `utilisateur` (`id`);

--
-- Contraintes pour la table `message`
--
ALTER TABLE `message`
  ADD CONSTRAINT `message_ibfk_1` FOREIGN KEY (`demande_id`) REFERENCES `demande` (`id`),
  ADD CONSTRAINT `message_ibfk_2` FOREIGN KEY (`expediteur_id`) REFERENCES `utilisateur` (`id`),
  ADD CONSTRAINT `message_ibfk_3` FOREIGN KEY (`destinataire_id`) REFERENCES `utilisateur` (`id`);

--
-- Contraintes pour la table `notification`
--
ALTER TABLE `notification`
  ADD CONSTRAINT `notification_ibfk_1` FOREIGN KEY (`utilisateur_id`) REFERENCES `utilisateur` (`id`);

--
-- Contraintes pour la table `offre_maker`
--
ALTER TABLE `offre_maker`
  ADD CONSTRAINT `offre_maker_ibfk_1` FOREIGN KEY (`demande_id`) REFERENCES `demande` (`id`),
  ADD CONSTRAINT `offre_maker_ibfk_2` FOREIGN KEY (`maker_id`) REFERENCES `utilisateur` (`id`);

--
-- Contraintes pour la table `photo_demande`
--
ALTER TABLE `photo_demande`
  ADD CONSTRAINT `photo_demande_ibfk_1` FOREIGN KEY (`demande_id`) REFERENCES `demande` (`id`);

--
-- Contraintes pour la table `profil_expert_cao`
--
ALTER TABLE `profil_expert_cao`
  ADD CONSTRAINT `profil_expert_cao_ibfk_1` FOREIGN KEY (`utilisateur_id`) REFERENCES `utilisateur` (`id`);

--
-- Contraintes pour la table `profil_maker`
--
ALTER TABLE `profil_maker`
  ADD CONSTRAINT `profil_maker_ibfk_1` FOREIGN KEY (`utilisateur_id`) REFERENCES `utilisateur` (`id`);

--
-- Contraintes pour la table `transaction`
--
ALTER TABLE `transaction`
  ADD CONSTRAINT `transaction_ibfk_1` FOREIGN KEY (`offre_maker_id`) REFERENCES `offre_maker` (`id`);

--
-- Contraintes pour la table `utilisateur_role`
--
ALTER TABLE `utilisateur_role`
  ADD CONSTRAINT `utilisateur_role_ibfk_1` FOREIGN KEY (`utilisateur_id`) REFERENCES `utilisateur` (`id`),
  ADD CONSTRAINT `utilisateur_role_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

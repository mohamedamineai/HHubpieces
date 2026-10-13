package com.hubpieces;

import com.hubpieces.dto.FichierStlCreateRequest;
import com.hubpieces.model.Demande;
import com.hubpieces.model.EtatDemande;
import com.hubpieces.model.EtatOffre;
import com.hubpieces.model.EtatPaiement;
import com.hubpieces.model.EtatValidation;
import com.hubpieces.model.FichierStl;
import com.hubpieces.model.OffreMaker;
import com.hubpieces.model.StatutUtilisateur;
import com.hubpieces.model.Utilisateur;
import com.hubpieces.repository.DemandeRepository;
import com.hubpieces.repository.OffreMakerRepository;
import com.hubpieces.repository.TransactionRepository;
import com.hubpieces.repository.UtilisateurRepository;
import com.hubpieces.service.DemandeService;
import com.hubpieces.service.FichierStlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class HubpiecesApplicationTests {

    @Autowired
    private DemandeRepository demandeRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private OffreMakerRepository offreMakerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private DemandeService demandeService;

    @Autowired
    private FichierStlService fichierStlService;

    @Test
    void contextLoads() {
        assertTrue(utilisateurRepository.findByEmail("admin@hubpieces.fr").isPresent());
        assertTrue(utilisateurRepository.findByEmail("expert@hubpieces.fr").isPresent());
    }

    @Test
    void modelisationNentreChezLesMakersQuApresValidationAdmin() {
        Utilisateur expert = utilisateur("expert@hubpieces.fr");
        Utilisateur admin = utilisateur("admin@hubpieces.fr");
        Demande demande = demandeAmodeliser();

        FichierStl fichier = fichierStlService.create(fichierRequest("validation-flow.stl"), expert);
        demandeService.soumettreModelisation(demande.getId(), fichier, expert);

        assertEquals(EtatDemande.MODELISATION_REQUISE, demandeRepository.findById(demande.getId()).orElseThrow().getEtat());
        assertEquals(EtatValidation.EN_ATTENTE, fichier.getEtatValidation());
        assertFalse(demandeService.findAValiderParExpert().stream()
                .anyMatch(d -> d.getId().equals(demande.getId())),
                "Une demande dont le modèle attend l'admin ne doit pas recevoir un second modèle");

        fichierStlService.valider(fichier.getId(), admin);

        Demande actualisee = demandeRepository.findById(demande.getId()).orElseThrow();
        assertEquals(EtatValidation.VALIDE, actualisee.getFichierStl().getEtatValidation());
        assertEquals(EtatDemande.RECHERCHE_MAKER, actualisee.getEtat());
    }

    @Test
    void uploadMultipartStockeLeFichierEtLeSoumetEnAttente() throws Exception {
        Utilisateur expert = utilisateur("expert@hubpieces.fr");
        byte[] contenu = "solid modele-test".getBytes(java.nio.charset.StandardCharsets.UTF_8);
        MockMultipartFile upload = new MockMultipartFile(
                "file", "modele-test.stl", "model/stl", contenu);
        Path fichierPhysique = null;

        try {
            FichierStl fichier = fichierStlService.uploadAndCreate(upload, "Modèle téléversé",
                    "Test d'upload multipart", "test", null, null, null, "PLA", null, expert);
            fichierPhysique = Path.of(System.getProperty("user.dir"), "uploads", "stl",
                    fichier.getCheminFichier().substring("/uploads/stl/".length()));

            assertTrue(Files.isRegularFile(fichierPhysique));
            assertEquals(EtatValidation.EN_ATTENTE, fichier.getEtatValidation());
            assertEquals(Long.valueOf(contenu.length), fichier.getTailleFichier());
            assertArrayEquals(contenu, Files.readAllBytes(fichierPhysique));
        } finally {
            if (fichierPhysique != null) Files.deleteIfExists(fichierPhysique);
        }
    }

    @Test
    void rejetAdminDetacheLeFichierEtRemetLaDemandeDansLaFileCao() {
        Utilisateur expert = utilisateur("expert@hubpieces.fr");
        Utilisateur admin = utilisateur("admin@hubpieces.fr");
        Demande demande = demandeAmodeliser();

        FichierStl fichier = fichierStlService.create(fichierRequest("rejected-flow.stl"), expert);
        demandeService.soumettreModelisation(demande.getId(), fichier, expert);
        fichierStlService.rejeter(fichier.getId(), "Dimensions à vérifier", admin);

        Demande actualisee = demandeRepository.findById(demande.getId()).orElseThrow();
        assertEquals(EtatDemande.MODELISATION_REQUISE, actualisee.getEtat());
        assertNull(actualisee.getFichierStl());
        assertEquals(EtatValidation.REJETE, fichier.getEtatValidation());
        assertTrue(demandeService.findAValiderParExpert().stream()
                .anyMatch(d -> d.getId().equals(demande.getId())));
    }

    @Test
    void acceptationOffrePlaceLePaiementEnSequestrePuisLeMakerPeutDemarrer() {
        Utilisateur demandeur = utilisateur("demandeur@hubpieces.fr");
        Utilisateur maker = utilisateur("maker@hubpieces.fr");
        Demande demande = demandeRepository.findByEtatOrderByDateCreationDesc(EtatDemande.RECHERCHE_MAKER)
                .get(0);
        OffreMaker offre = offreMakerRepository.findByDemandeOrderByPrixAsc(demande).stream()
                .filter(item -> item.getEtat() == EtatOffre.EN_ATTENTE)
                .findFirst().orElseThrow();

        demandeService.accepterOffre(demande.getId(), offre.getId(), demandeur);
        assertEquals(EtatDemande.ATTENTE_IMPRESSION,
                demandeRepository.findById(demande.getId()).orElseThrow().getEtat());
        assertEquals(EtatPaiement.EN_SEQUESTRE,
                transactionRepository.findByOffreMakerId(offre.getId()).orElseThrow().getEtatPaiement());

        demandeService.marquerImpressionLancee(demande.getId(), maker);
        assertEquals(EtatDemande.EN_COURS_IMPRESSION,
                demandeRepository.findById(demande.getId()).orElseThrow().getEtat());
        assertNotNull(transactionRepository.findByOffreMakerId(offre.getId()).orElseThrow());
    }

    private Utilisateur utilisateur(String email) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email).orElseThrow();
        assertEquals(StatutUtilisateur.ACTIF, utilisateur.getStatut());
        return utilisateur;
    }

    private Demande demandeAmodeliser() {
        List<Demande> demandes = demandeRepository.findByEtatOrderByDateCreationDesc(EtatDemande.MODELISATION_REQUISE);
        assertFalse(demandes.isEmpty(), "Le jeu de données de test doit fournir une demande CAO");
        return demandes.get(0);
    }

    private FichierStlCreateRequest fichierRequest(String nom) {
        FichierStlCreateRequest request = new FichierStlCreateRequest();
        request.setNom(nom);
        // Réutilise un asset 3D existant du dépôt pour rester sans fichier de test à générer.
        request.setCheminFichier("images/model1.glb");
        request.setCategorie("electromenager");
        return request;
    }
}

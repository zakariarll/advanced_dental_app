package ma.dentalTech.mvc.controllers.modules.actes;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.mvc.controllers.modules.actes.api.ActeController;
import ma.dentalTech.mvc.controllers.modules.actes.api.ActeInterventionController;
import ma.dentalTech.mvc.controllers.modules.actes.swing_implementation.ActeControllerImpl;
import ma.dentalTech.mvc.controllers.modules.actes.swing_implementation.ActeInterventionControllerImpl;
import ma.dentalTech.mvc.dto.actes.ActeDTO;
import ma.dentalTech.service.modules.actes.api.ActeInterventionService;
import ma.dentalTech.service.modules.actes.api.ActeService;
import ma.dentalTech.service.modules.actes.impl.ActeInterventionServiceImpl;
import ma.dentalTech.service.modules.actes.impl.ActeServiceImpl;

import java.util.Random;

public class ActesControllerTest {

    private static ActeService acteService;
    private static ActeInterventionService acteInterventionService;
    private static ActeController acteController;
    private static ActeInterventionController acteInterventionController;
    private static int testsReussis = 0;
    private static int testsEchoues = 0;
    private static Random random = new Random();

    public static void main(String[] args) {
        System.out.println("======================================================================");
        System.out.println("TEST FONCTIONNEL - MODULE ACTES (CONTROLLER)");
        System.out.println("======================================================================");
        System.out.println();
        initialiserControllers();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 1 : TESTS ACTE CONTROLLER");
        System.out.println("----------------------------------------------------------------------");
        testerActeController();
        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 2 : TESTS ACTE INTERVENTION CONTROLLER");
        System.out.println("----------------------------------------------------------------------");
        testerActeInterventionController();
        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 3 : FLUX COMPLET GESTION ACTES");
        System.out.println("----------------------------------------------------------------------");
        testerFluxGestionActes();
        afficherResultats();
    }

    private static void initialiserControllers() {
        System.out.println("[INIT] Initialisation des services et controllers...");
        acteService = new ActeServiceImpl();
        acteInterventionService = new ActeInterventionServiceImpl();
        acteController = new ActeControllerImpl(acteService);
        acteInterventionController = new ActeInterventionControllerImpl(acteInterventionService, acteService);
        System.out.println("[INIT] Initialisation terminee");
        System.out.println();
    }

    private static void testerActeController() {
        System.out.println();
        System.out.println("[CTRL] Test 1.1 : Afficher tous les actes");
        try {
            acteController.afficherTousLesActes();
            System.out.println("       [OK] Affichage des actes execute");
            testsReussis++;
        } catch (ServiceException e) {
            System.out.println("       [ECHEC] ServiceException: " + e.getMessage());
            testsEchoues++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] Exception inattendue: " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[CTRL] Test 1.2 : Creation d'un acte");
        try {
            ActeDTO acteDTO = ActeDTO.builder()
                    .libelle("Test Acte " + genererCodeUnique())
                    .categorie("Hygiene")
                    .prixDeBase(350.0)
                    .description("Acte de test")
                    .code(genererCodeUnique())
                    .build();
            acteController.creerActe(acteDTO);
            System.out.println("       [OK] Acte cree avec succes");
            testsReussis++;
        } catch (ValidationException e) {
            System.out.println("       [ECHEC] ValidationException: " + e.getMessage());
            testsEchoues++;
        } catch (ServiceException e) {
            System.out.println("       [ECHEC] ServiceException: " + e.getMessage());
            testsEchoues++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] Exception inattendue: " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[CTRL] Test 1.3 : Recherche par libelle");
        try {
            acteController.rechercherActesParLibelle("Detartrage");
            System.out.println("       [OK] Recherche executee");
            testsReussis++;
        } catch (ServiceException e) {
            System.out.println("       [ECHEC] ServiceException: " + e.getMessage());
            testsEchoues++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] Exception inattendue: " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[CTRL] Test 1.4 : Afficher actes par categorie");
        try {
            acteController.afficherActesParCategorie("Hygiene");
            System.out.println("       [OK] Actes par categorie affiches");
            testsReussis++;
        } catch (ServiceException e) {
            System.out.println("       [ECHEC] ServiceException: " + e.getMessage());
            testsEchoues++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] Exception inattendue: " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[CTRL] Test 1.5 : Afficher categories");
        try {
            acteController.afficherCategories();
            System.out.println("       [OK] Categories affichees");
            testsReussis++;
        } catch (ServiceException e) {
            System.out.println("       [ECHEC] ServiceException: " + e.getMessage());
            testsEchoues++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] Exception inattendue: " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[CTRL] Test 1.6 : Afficher actes par plage de prix");
        try {
            acteController.afficherActesParPrix(200.0, 500.0);
            System.out.println("       [OK] Actes par prix affiches");
            testsReussis++;
        } catch (ServiceException e) {
            System.out.println("       [ECHEC] ServiceException: " + e.getMessage());
            testsEchoues++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] Exception inattendue: " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[CTRL] Test 1.7 : Modification d'un acte");
        try {
            ActeDTO acteDTO = ActeDTO.builder()
                    .idActe(1L)
                    .libelle("Acte Modifie " + genererCodeUnique())
                    .categorie("Hygiene")
                    .prixDeBase(400.0)
                    .description("Description modifiee")
                    .code(genererCodeUnique())
                    .build();
            acteController.modifierActe(acteDTO);
            System.out.println("       [OK] Acte modifie avec succes");
            testsReussis++;
        } catch (ValidationException e) {
            System.out.println("       [ECHEC] ValidationException: " + e.getMessage());
            testsEchoues++;
        } catch (ServiceException e) {
            System.out.println("       [ECHEC] ServiceException: " + e.getMessage());
            testsEchoues++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] Exception inattendue: " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[CTRL] Test 1.8 : Afficher details d'un acte");
        try {
            acteController.afficherActe(1L);
            System.out.println("       [OK] Details affiches");
            testsReussis++;
        } catch (ServiceException e) {
            System.out.println("       [ECHEC] ServiceException: " + e.getMessage());
            testsEchoues++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] Exception inattendue: " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[CTRL] Test 1.9 : Appliquer une remise");
        try {
            acteController.appliquerRemise(1L, 10.0);
            System.out.println("       [OK] Remise de 10% appliquee");
            testsReussis++;
        } catch (ValidationException e) {
            System.out.println("       [ECHEC] ValidationException: " + e.getMessage());
            testsEchoues++;
        } catch (ServiceException e) {
            System.out.println("       [ECHEC] ServiceException: " + e.getMessage());
            testsEchoues++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] Exception inattendue: " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[CTRL] Test 1.10 : Modifier le prix");
        try {
            acteController.modifierPrix(1L, 450.0);
            System.out.println("       [OK] Prix modifie a 450.0 MAD");
            testsReussis++;
        } catch (ValidationException e) {
            System.out.println("       [ECHEC] ValidationException: " + e.getMessage());
            testsEchoues++;
        } catch (ServiceException e) {
            System.out.println("       [ECHEC] ServiceException: " + e.getMessage());
            testsEchoues++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] Exception inattendue: " + e.getMessage());
            testsEchoues++;
        }
    }

    private static void testerActeInterventionController() {
        System.out.println();
        System.out.println("[CTRL] Test 2.1 : Ajouter acte a une intervention");
        try {
            acteInterventionController.ajouterActeAIntervention(1L, 1L);
            System.out.println("       [OK] Acte ajoute a l'intervention");
            testsReussis++;
        } catch (ValidationException e) {
            System.out.println("       [ECHEC] ValidationException: " + e.getMessage());
            testsEchoues++;
        } catch (ServiceException e) {
            System.out.println("       [ECHEC] ServiceException: " + e.getMessage());
            testsEchoues++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] Exception inattendue: " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[CTRL] Test 2.2 : Afficher actes d'une intervention");
        try {
            acteInterventionController.afficherActesParIntervention(1L);
            System.out.println("       [OK] Actes de l'intervention affiches");
            testsReussis++;
        } catch (ServiceException e) {
            System.out.println("       [ECHEC] ServiceException: " + e.getMessage());
            testsEchoues++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] Exception inattendue: " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[CTRL] Test 2.3 : Afficher montant total intervention");
        try {
            acteInterventionController.afficherMontantTotalIntervention(1L);
            System.out.println("       [OK] Montant total affiche");
            testsReussis++;
        } catch (ServiceException e) {
            System.out.println("       [ECHEC] ServiceException: " + e.getMessage());
            testsEchoues++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] Exception inattendue: " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[CTRL] Test 2.4 : Afficher interventions utilisant un acte");
        try {
            acteInterventionController.afficherInterventionsParActe(1L);
            System.out.println("       [OK] Interventions affichees");
            testsReussis++;
        } catch (ServiceException e) {
            System.out.println("       [ECHEC] ServiceException: " + e.getMessage());
            testsEchoues++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] Exception inattendue: " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[CTRL] Test 2.5 : Afficher statistiques d'un acte");
        try {
            acteInterventionController.afficherStatistiquesActe(1L);
            System.out.println("       [OK] Statistiques affichees");
            testsReussis++;
        } catch (ServiceException e) {
            System.out.println("       [ECHEC] ServiceException: " + e.getMessage());
            testsEchoues++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] Exception inattendue: " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[CTRL] Test 2.6 : Retirer acte d'une intervention");
        try {
            acteInterventionController.retirerTousLesActesDeIntervention(1L);
            System.out.println("       [OK] Actes retires de l'intervention");
            testsReussis++;
        } catch (ServiceException e) {
            System.out.println("       [ECHEC] ServiceException: " + e.getMessage());
            testsEchoues++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] Exception inattendue: " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[CTRL] Test 2.7 : Verification apres suppression");
        try {
            acteInterventionController.afficherActesParIntervention(1L);
            System.out.println("       [OK] Verification effectuee");
            testsReussis++;
        } catch (ServiceException e) {
            System.out.println("       [ECHEC] ServiceException: " + e.getMessage());
            testsEchoues++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] Exception inattendue: " + e.getMessage());
            testsEchoues++;
        }
    }

    private static void testerFluxGestionActes() {
        System.out.println();
        System.out.println("[FLUX] Scenario : Gestion complete actes et interventions");
        System.out.println();
        try {
            int codeBase = genererCodeUnique();

            System.out.println("       Etape 1 : Creation d'un nouvel acte");
            ActeDTO acte1 = ActeDTO.builder()
                    .libelle("Extraction Test " + codeBase)
                    .categorie("Chirurgie")
                    .prixDeBase(600.0)
                    .description("Extraction d'une dent")
                    .code(codeBase)
                    .build();
            acteController.creerActe(acte1);
            System.out.println("                 Acte 'Extraction dentaire' cree");

            System.out.println();
            System.out.println("       Etape 2 : Creation d'un deuxieme acte");
            ActeDTO acte2 = ActeDTO.builder()
                    .libelle("Plombage Test " + (codeBase + 1))
                    .categorie("Restauration")
                    .prixDeBase(300.0)
                    .description("Plombage d'une carie")
                    .code(codeBase + 1)
                    .build();
            acteController.creerActe(acte2);
            System.out.println("                 Acte 'Plombage' cree");

            System.out.println();
            System.out.println("       Etape 3 : Consultation des actes par categorie");
            acteController.afficherActesParCategorie("Chirurgie");
            System.out.println("                 Actes de chirurgie consultes");

            System.out.println();
            System.out.println("       Etape 4 : Ajout des actes a une intervention");
            acteInterventionController.ajouterActeAIntervention(1L, 5L);
            acteInterventionController.ajouterActeAIntervention(2L, 5L);
            System.out.println("                 2 actes ajoutes a l'intervention 5");

            System.out.println();
            System.out.println("       Etape 5 : Consultation des actes de l'intervention");
            acteInterventionController.afficherActesParIntervention(5L);
            System.out.println("                 Actes de l'intervention consultes");

            System.out.println();
            System.out.println("       Etape 6 : Calcul du montant total");
            acteInterventionController.afficherMontantTotalIntervention(5L);
            System.out.println("                 Montant total calcule");

            System.out.println();
            System.out.println("       Etape 7 : Application remise sur un acte");
            acteController.appliquerRemise(1L, 15.0);
            System.out.println("                 Remise de 15% appliquee");

            System.out.println();
            System.out.println("       Etape 8 : Recalcul du montant total");
            acteInterventionController.afficherMontantTotalIntervention(5L);
            System.out.println("                 Nouveau montant apres remise");

            System.out.println();
            System.out.println("       Etape 9 : Statistiques d'utilisation des actes");
            acteInterventionController.afficherStatistiquesActe(1L);
            acteInterventionController.afficherStatistiquesActe(2L);
            System.out.println("                 Statistiques consultees");

            System.out.println();
            System.out.println("       Etape 10 : Modification d'un acte");
            ActeDTO acteModifie = ActeDTO.builder()
                    .idActe(1L)
                    .libelle("Extraction Modifiee " + genererCodeUnique())
                    .categorie("Chirurgie")
                    .prixDeBase(550.0)
                    .description("Extraction simple d'une dent")
                    .code(genererCodeUnique())
                    .build();
            acteController.modifierActe(acteModifie);
            System.out.println("                 Acte modifie avec nouveau prix");

            System.out.println();
            System.out.println("       [OK] Flux gestion actes execute avec succes");
            testsReussis++;
        } catch (ValidationException e) {
            System.out.println("       [ECHEC] ValidationException: " + e.getMessage());
            testsEchoues++;
        } catch (ServiceException e) {
            System.out.println("       [ECHEC] ServiceException: " + e.getMessage());
            testsEchoues++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] Exception inattendue: " + e.getMessage());
            testsEchoues++;
        }
    }

    private static void afficherResultats() {
        System.out.println();
        System.out.println("======================================================================");
        System.out.println("RESULTATS DES TESTS");
        System.out.println("======================================================================");
        System.out.println("Tests reussis : " + testsReussis);
        System.out.println("Tests echoues : " + testsEchoues);
        System.out.println("Total         : " + (testsReussis + testsEchoues));
        int total = testsReussis + testsEchoues;
        if (total > 0) {
            double taux = (testsReussis * 100.0) / total;
            System.out.println("Taux reussite : " + String.format("%.1f", taux) + "%");
        }
        System.out.println("======================================================================");
    }

    private static int genererCodeUnique() {
        long timestamp = System.currentTimeMillis();
        int randomPart = random.nextInt(1000);
        return (int) ((timestamp % 100000) + randomPart);
    }
}
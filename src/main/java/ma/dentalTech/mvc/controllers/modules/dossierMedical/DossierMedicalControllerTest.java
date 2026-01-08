package ma.dentalTech.mvc.controllers.modules.dossierMedical;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.mvc.controllers.modules.dossierMedical.api.ConsultationController;
import ma.dentalTech.mvc.controllers.modules.dossierMedical.api.DossierMedicalController;
import ma.dentalTech.mvc.controllers.modules.dossierMedical.api.InterventionMedecinController;
import ma.dentalTech.mvc.controllers.modules.dossierMedical.swing_implementation.ConsultationControllerImpl;
import ma.dentalTech.mvc.controllers.modules.dossierMedical.swing_implementation.DossierMedicalControllerImpl;
import ma.dentalTech.mvc.controllers.modules.dossierMedical.swing_implementation.InterventionMedecinControllerImpl;
import ma.dentalTech.mvc.dto.dossierMedical.ConsultationDTO;
import ma.dentalTech.mvc.dto.dossierMedical.InterventionMedecinDTO;
import ma.dentalTech.repository.modules.dossierMedical.impl.mySQL.DossierMedicalRepositoryImpl;
import ma.dentalTech.repository.modules.dossierMedical.impl.mySQL.InterventionMedecinRepositoryImpl;
import ma.dentalTech.service.modules.dossierMedical.api.ConsultationService;
import ma.dentalTech.service.modules.dossierMedical.api.DossierMedicalService;
import ma.dentalTech.service.modules.dossierMedical.api.InterventionMedecinService;
import ma.dentalTech.service.modules.dossierMedical.impl.ConsultationServiceImpl;
import ma.dentalTech.service.modules.dossierMedical.impl.DossierMedicalServiceImpl;
import ma.dentalTech.service.modules.dossierMedical.impl.InterventionMedecinServiceImpl;

import java.time.LocalDate;
import java.util.Random;

/**
 * Test fonctionnel pour le module Dossier Medical (Controller).
 * Note: Les tests RDV ont été supprimés car la gestion des RDV
 * est maintenant exclusivement dans le module RDVPatient.
 */
public class DossierMedicalControllerTest {

    private static ConsultationService consultationService;
    private static InterventionMedecinService interventionService;
    private static DossierMedicalService dossierMedicalService;

    private static ConsultationController consultationController;
    private static InterventionMedecinController interventionController;
    private static DossierMedicalController dossierMedicalController;

    private static int testsReussis = 0;
    private static int testsEchoues = 0;
    private static Random random = new Random();

    public static void main(String[] args) {
        System.out.println("======================================================================");
        System.out.println("TEST FONCTIONNEL - MODULE DOSSIER MEDICAL (CONTROLLER)");
        System.out.println("======================================================================");
        System.out.println();
        initialiserControllers();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 1 : TESTS CONSULTATION CONTROLLER");
        System.out.println("----------------------------------------------------------------------");
        testerConsultationController();
        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 2 : TESTS INTERVENTION MEDECIN CONTROLLER");
        System.out.println("----------------------------------------------------------------------");
        testerInterventionMedecinController();
        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 3 : TESTS DOSSIER MEDICAL CONTROLLER");
        System.out.println("----------------------------------------------------------------------");
        testerDossierMedicalController();
        afficherResultats();
    }

    private static void initialiserControllers() {
        System.out.println("[INIT] Initialisation des services et controllers...");

        consultationService = new ConsultationServiceImpl();
        interventionService = new InterventionMedecinServiceImpl(new InterventionMedecinRepositoryImpl());

        dossierMedicalService = new DossierMedicalServiceImpl(
                new DossierMedicalRepositoryImpl(
                        new ma.dentalTech.repository.modules.patient.impl.mySQL.PatientRepositoryImpl(),
                        new ma.dentalTech.repository.modules.patient.impl.mySQL.AntecedentRepositoryImpl()
                )
        );

        consultationController = new ConsultationControllerImpl(consultationService);
        interventionController = new InterventionMedecinControllerImpl(interventionService);
        dossierMedicalController = new DossierMedicalControllerImpl(dossierMedicalService);
        System.out.println("[INIT] Initialisation terminee");
        System.out.println();
    }

    private static void testerConsultationController() {
        System.out.println();
        System.out.println("[CTRL] Test 1.1 : Afficher toutes les consultations");
        try {
            consultationController.afficherToutesLesConsultations();
            System.out.println("       [OK] Consultations affichees");
            testsReussis++;
        } catch (ServiceException e) {
            System.out.println("       [ECHEC] ServiceException: " + e.getMessage());
            testsEchoues++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] Exception inattendue: " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[CTRL] Test 1.2 : Creation d'une consultation");
        try {
            ConsultationDTO consultationDTO = ConsultationDTO.builder()
                    .date(LocalDate.now())
                    .statut("EN_COURS")
                    .observationMedecin("Consultation initiale")
                    .idRDV(1L)
                    .build();
            consultationController.creerConsultation(consultationDTO);
            System.out.println("       [OK] Consultation creee");
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
        System.out.println("[CTRL] Test 1.3 : Afficher consultations par date");
        try {
            consultationController.afficherConsultationsParDate(LocalDate.now());
            System.out.println("       [OK] Consultations du jour affichees");
            testsReussis++;
        } catch (ServiceException e) {
            System.out.println("       [ECHEC] ServiceException: " + e.getMessage());
            testsEchoues++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] Exception inattendue: " + e.getMessage());
            testsEchoues++;
        }
    }

    private static void testerInterventionMedecinController() {
        System.out.println();
        System.out.println("[CTRL] Test 2.1 : Afficher toutes les interventions");
        try {
            interventionController.afficherToutesLesInterventions();
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
        System.out.println("[CTRL] Test 2.2 : Creation d'une intervention");
        try {
            InterventionMedecinDTO interventionDTO = InterventionMedecinDTO.builder()
                    .prixDePatient(500.0)
                    .idConsultation(1L)
                    .idMedecin(1L)
                    .build();
            interventionController.creerIntervention(interventionDTO);
            System.out.println("       [OK] Intervention creee");
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

    private static void testerDossierMedicalController() {
        System.out.println();
        System.out.println("[CTRL] Test 3.1 : Afficher dossier medical complet");
        try {
            dossierMedicalController.afficherDossierComplet(1L);
            System.out.println("       [OK] Dossier medical affiche");
            testsReussis++;
        } catch (ServiceException e) {
            System.out.println("       [ECHEC] ServiceException: " + e.getMessage());
            testsEchoues++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] Exception inattendue: " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[CTRL] Test 3.2 : Verifier risques critiques");
        try {
            dossierMedicalController.verifierRisquesCritiques(1L);
            System.out.println("       [OK] Risques critiques verifies");
            testsReussis++;
        } catch (ServiceException e) {
            System.out.println("       [ECHEC] ServiceException: " + e.getMessage());
            testsEchoues++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] Exception inattendue: " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[CTRL] Test 3.3 : Afficher statistiques patient");
        try {
            dossierMedicalController.afficherStatistiquesPatient(1L);
            System.out.println("       [OK] Statistiques affichees");
            testsReussis++;
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
}
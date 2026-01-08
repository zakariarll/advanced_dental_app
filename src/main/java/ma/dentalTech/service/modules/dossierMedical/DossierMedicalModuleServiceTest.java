package ma.dentalTech.service.modules.dossierMedical;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.dossierMedical.Consultation;
import ma.dentalTech.entities.dossierMedical.InterventionMedecin;
import ma.dentalTech.repository.modules.dossierMedical.impl.mySQL.ConsultationRepositoryImpl;
import ma.dentalTech.repository.modules.dossierMedical.impl.mySQL.InterventionMedecinRepositoryImpl;
import ma.dentalTech.service.modules.dossierMedical.api.ConsultationService;
import ma.dentalTech.service.modules.dossierMedical.api.InterventionMedecinService;
import ma.dentalTech.service.modules.dossierMedical.impl.ConsultationServiceImpl;
import ma.dentalTech.service.modules.dossierMedical.impl.InterventionMedecinServiceImpl;

import java.time.LocalDate;
import java.util.List;

/**
 * Test fonctionnel pour le module Dossier Medical (Service).
 * Note: Les tests RDV ont été supprimés car la gestion des RDV
 * est maintenant exclusivement dans le module RDVPatient.
 */
public class DossierMedicalModuleServiceTest {

    private static ConsultationService consultationService;
    private static InterventionMedecinService interventionService;
    private static int testsReussis = 0;
    private static int testsEchoues = 0;

    public static void main(String[] args) {
        System.out.println("======================================================================");
        System.out.println("TEST FONCTIONNEL - MODULE DOSSIER MEDICAL (SERVICE)");
        System.out.println("======================================================================");
        System.out.println();

        initialiserServices();

        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 1 : TESTS CONSULTATION SERVICE");
        System.out.println("----------------------------------------------------------------------");
        testerConsultationService();

        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 2 : TESTS INTERVENTION MEDECIN SERVICE");
        System.out.println("----------------------------------------------------------------------");
        testerInterventionService();

        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 3 : TESTS VALIDATION ET EXCEPTIONS");
        System.out.println("----------------------------------------------------------------------");
        testerValidationEtExceptions();

        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 4 : FLUX COMPLET PARCOURS PATIENT");
        System.out.println("----------------------------------------------------------------------");
        testerFluxCompletParcoursPatient();

        afficherResultats();
    }

    private static void initialiserServices() {
        System.out.println("[INIT] Initialisation des services...");
        consultationService = new ConsultationServiceImpl(new ConsultationRepositoryImpl());
        interventionService = new InterventionMedecinServiceImpl(new InterventionMedecinRepositoryImpl());
        System.out.println("[INIT] Initialisation terminee");
        System.out.println();
    }

    private static void testerConsultationService() {
        Long idConsultationTest = null;

        System.out.println();
        System.out.println("[SERVICE] Test 1.1 : Creation d'une consultation");
        try {
            Consultation consultation = Consultation.builder()
                    .idRDV(1L)
                    .date(LocalDate.now())
                    .statut("EN_COURS")
                    .observationMedecin("Patient presente une carie legere")
                    .build();
            Consultation resultat = consultationService.creerConsultation(consultation);
            idConsultationTest = resultat.getIdConsultation();
            if (idConsultationTest != null) {
                System.out.println("       [OK] Consultation creee avec ID : " + idConsultationTest);
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] ID non genere");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        if (idConsultationTest == null) {
            System.out.println("[ERREUR] Impossible de continuer sans ID de consultation valide");
            return;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 1.2 : Obtenir consultation par ID");
        try {
            Consultation c = consultationService.obtenirConsultation(idConsultationTest);
            if (c != null && c.getObservationMedecin().contains("carie")) {
                System.out.println("       [OK] Consultation recuperee");
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] Consultation non trouvee ou incorrecte");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 1.3 : Lister consultations par date");
        try {
            List<Consultation> consultations = consultationService.listerConsultationsParDate(LocalDate.now());
            System.out.println("       [OK] " + consultations.size() + " consultation(s) aujourd'hui");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 1.4 : Terminer consultation");
        try {
            consultationService.terminerConsultation(idConsultationTest, "Traitement complet effectue");
            Consultation c = consultationService.obtenirConsultation(idConsultationTest);
            if ("TERMINEE".equals(c.getStatut())) {
                System.out.println("       [OK] Consultation terminee : EN_COURS -> TERMINEE");
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] Statut non change");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }
    }

    private static void testerInterventionService() {
        Long idInterventionTest = null;

        System.out.println();
        System.out.println("[SERVICE] Test 2.1 : Creation d'une intervention");
        try {
            InterventionMedecin intervention = InterventionMedecin.builder()
                    .idConsultation(1L)
                    .idMedecin(1L)
                    .prixDePatient(750.0)
                    .build();
            InterventionMedecin resultat = interventionService.creerIntervention(intervention);
            idInterventionTest = resultat.getIdIM();
            if (idInterventionTest != null) {
                System.out.println("       [OK] Intervention creee avec ID : " + idInterventionTest);
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] ID non genere");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        if (idInterventionTest == null) {
            System.out.println("[ERREUR] Impossible de continuer sans ID d'intervention valide");
            return;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 2.2 : Obtenir intervention par ID");
        try {
            InterventionMedecin im = interventionService.obtenirIntervention(idInterventionTest);
            if (im != null && im.getPrixDePatient().equals(750.0)) {
                System.out.println("       [OK] Intervention recuperee : " + im.getPrixDePatient() + " DH");
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] Intervention non trouvee ou incorrecte");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 2.3 : Calculer montant total consultation");
        try {
            Double montant = interventionService.calculerMontantTotalConsultation(1L);
            System.out.println("       [OK] Montant total consultation : " + montant + " DH");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }
    }

    private static void testerValidationEtExceptions() {
        System.out.println();
        System.out.println("[VALIDATION] Test 3.1 : Creation consultation sans RDV");
        try {
            Consultation c = Consultation.builder()
                    .date(LocalDate.now())
                    .statut("EN_COURS")
                    .observationMedecin("Test")
                    .build();
            consultationService.creerConsultation(c);
            System.out.println("       [ECHEC] Exception attendue non levee");
            testsEchoues++;
        } catch (ValidationException e) {
            System.out.println("       [OK] ValidationException levee : " + e.getMessage());
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] Mauvaise exception : " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[VALIDATION] Test 3.2 : Creation intervention avec prix negatif");
        try {
            InterventionMedecin im = InterventionMedecin.builder()
                    .idConsultation(1L)
                    .idMedecin(1L)
                    .prixDePatient(-100.0)
                    .build();
            interventionService.creerIntervention(im);
            System.out.println("       [ECHEC] Exception attendue non levee");
            testsEchoues++;
        } catch (ValidationException e) {
            System.out.println("       [OK] ValidationException levee : " + e.getMessage());
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] Mauvaise exception : " + e.getMessage());
            testsEchoues++;
        }
    }

    private static void testerFluxCompletParcoursPatient() {
        System.out.println();
        System.out.println("[FLUX] Scenario : Parcours complet Consultation -> Intervention");
        System.out.println();

        try {
            System.out.println("       Etape 1 : Creation consultation");
            Consultation consultation = consultationService.creerConsultation(Consultation.builder()
                    .idRDV(100L)
                    .date(LocalDate.now())
                    .statut("EN_COURS")
                    .observationMedecin("Detartrage necessaire")
                    .build());
            System.out.println("                 Consultation ID : " + consultation.getIdConsultation());

            System.out.println();
            System.out.println("       Etape 2 : Ajout interventions");
            InterventionMedecin im1 = interventionService.creerIntervention(InterventionMedecin.builder()
                    .idConsultation(consultation.getIdConsultation())
                    .idMedecin(1L)
                    .prixDePatient(300.0)
                    .build());
            System.out.println("                 Intervention 1 : " + im1.getPrixDePatient() + " DH");

            InterventionMedecin im2 = interventionService.creerIntervention(InterventionMedecin.builder()
                    .idConsultation(consultation.getIdConsultation())
                    .idMedecin(1L)
                    .prixDePatient(450.0)
                    .build());
            System.out.println("                 Intervention 2 : " + im2.getPrixDePatient() + " DH");

            System.out.println();
            System.out.println("       Etape 3 : Calcul montant total");
            Double montant = interventionService.calculerMontantTotalConsultation(consultation.getIdConsultation());
            System.out.println("                 Montant total : " + montant + " DH");

            System.out.println();
            System.out.println("       Etape 4 : Fin de consultation");
            consultationService.terminerConsultation(consultation.getIdConsultation(), "Detartrage effectue avec succes");
            System.out.println("                 Statut consultation : TERMINEE");

            System.out.println();
            System.out.println("       [OK] Flux parcours patient execute avec succes");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] Erreur dans le flux : " + e.getMessage());
            e.printStackTrace();
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

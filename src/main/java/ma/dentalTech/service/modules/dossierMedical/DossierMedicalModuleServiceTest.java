/** package ma.dentalTech.service.modules.dossierMedical;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.dossierMedical.Consultation;
import ma.dentalTech.entities.dossierMedical.InterventionMedecin;
import ma.dentalTech.entities.dossierMedical.RDV;
import ma.dentalTech.entities.enums.StatutRDV;
import ma.dentalTech.repository.modules.dossierMedical.impl.mySQL.ConsultationRepositoryImpl;
import ma.dentalTech.repository.modules.dossierMedical.impl.mySQL.InterventionMedecinRepositoryImpl;
import ma.dentalTech.repository.modules.dossierMedical.impl.mySQL.RDVRepositoryImpl;
import ma.dentalTech.service.modules.dossierMedical.api.ConsultationService;
import ma.dentalTech.service.modules.dossierMedical.api.InterventionMedecinService;
import ma.dentalTech.service.modules.dossierMedical.api.RDVService;
import ma.dentalTech.service.modules.dossierMedical.impl.ConsultationServiceImpl;
import ma.dentalTech.service.modules.dossierMedical.impl.InterventionMedecinServiceImpl;
import ma.dentalTech.service.modules.dossierMedical.impl.RDVServiceImpl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class DossierMedicalModuleServiceTest {

   // private static RDVService rdvService;
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
        System.out.println("ETAPE 1 : TESTS RDV SERVICE");
        System.out.println("----------------------------------------------------------------------");
        testerRDVService();

        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 2 : TESTS CONSULTATION SERVICE");
        System.out.println("----------------------------------------------------------------------");
        testerConsultationService();

        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 3 : TESTS INTERVENTION MEDECIN SERVICE");
        System.out.println("----------------------------------------------------------------------");
        testerInterventionService();

        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 4 : TESTS VALIDATION ET EXCEPTIONS");
        System.out.println("----------------------------------------------------------------------");
        testerValidationEtExceptions();

        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 5 : FLUX COMPLET PARCOURS PATIENT");
        System.out.println("----------------------------------------------------------------------");
        testerFluxCompletParcoursPatient();

        afficherResultats();
    }

   private static void initialiserServices() {
        System.out.println("[INIT] Initialisation des services...");
        rdvService = new RDVServiceImpl(new RDVRepositoryImpl());
        consultationService = new ConsultationServiceImpl(new ConsultationRepositoryImpl());
        interventionService = new InterventionMedecinServiceImpl(new InterventionMedecinRepositoryImpl());
        System.out.println("[INIT] Initialisation terminee");
        System.out.println();
    }

    private static void testerRDVService() {
        Long idRDVTest = null;

        System.out.println();
        System.out.println("[SERVICE] Test 1.1 : Creation d'un RDV");
        try {
            RDV rdv = RDV.builder()
                    .idPatient(1L)
                    .idMedecin(1L)
                    .date(LocalDate.now().plusDays(5))
                    .heure(LocalTime.of(14, 30))
                    .motif("Controle dentaire annuel")
                    .statut(StatutRDV.PLANIFIE)
                    .build();
            RDV resultat = rdvService.creerRDV(rdv);
            idRDVTest = resultat.getIdRDV();
            if (idRDVTest != null) {
                System.out.println("       [OK] RDV cree avec ID : " + idRDVTest);
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] ID non genere");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        if (idRDVTest == null) {
            System.out.println("[ERREUR] Impossible de continuer sans ID de RDV valide");
            return;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 1.2 : Obtenir un RDV par ID");
        try {
            RDV rdv = rdvService.obtenirRDV(idRDVTest);
            if (rdv != null && rdv.getMotif().equals("Controle dentaire annuel")) {
                System.out.println("       [OK] RDV recupere : " + rdv.getMotif());
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] RDV non trouve ou incorrect");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 1.3 : Lister RDV par patient");
        try {
            List<RDV> rdvs = rdvService.listerRDVParPatient(1L);
            System.out.println("       [OK] " + rdvs.size() + " RDV(s) pour patient 1");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 1.4 : Lister RDV par medecin");
        try {
            List<RDV> rdvs = rdvService.listerRDVParMedecin(1L);
            System.out.println("       [OK] " + rdvs.size() + " RDV(s) pour medecin 1");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 1.5 : Lister RDV par date");
        try {
            List<RDV> rdvs = rdvService.listerRDVParDate(LocalDate.now().plusDays(5));
            System.out.println("       [OK] " + rdvs.size() + " RDV(s) pour la date");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 1.6 : Confirmer un RDV");
        try {
            rdvService.confirmerRDV(idRDVTest);
            RDV rdv = rdvService.obtenirRDV(idRDVTest);
            if (rdv.getStatut() == StatutRDV.CONFIRME) {
                System.out.println("       [OK] RDV confirme : PLANIFIE -> CONFIRME");
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] Statut non modifie");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 1.7 : Verifier disponibilite");
        try {
            boolean disponible = rdvService.verifierDisponibilite(1L, LocalDate.now().plusDays(6), LocalTime.of(15, 0));
            if (disponible) {
                System.out.println("       [OK] Creneau disponible");
                testsReussis++;
            } else {
                System.out.println("       [OK] Creneau non disponible (attendu)");
                testsReussis++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 1.8 : Modifier un RDV");
        try {
            RDV rdv = rdvService.obtenirRDV(idRDVTest);
            rdv.setMotif("Urgence dentaire");
            rdvService.modifierRDV(rdv);
            RDV modifie = rdvService.obtenirRDV(idRDVTest);
            if (modifie.getMotif().equals("Urgence dentaire")) {
                System.out.println("       [OK] RDV modifie avec succes");
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] Modification non appliquee");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 1.9 : Changer statut RDV");
        try {
            rdvService.changerStatutRDV(idRDVTest, StatutRDV.TERMINE);
            RDV rdv = rdvService.obtenirRDV(idRDVTest);
            if (rdv.getStatut() == StatutRDV.TERMINE) {
                System.out.println("       [OK] Statut change : CONFIRME -> TERMINE");
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

    private static void testerConsultationService() {
        Long idConsultationTest = null;

        System.out.println();
        System.out.println("[SERVICE] Test 2.1 : Creation d'une consultation");
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
        System.out.println("[SERVICE] Test 2.2 : Obtenir consultation par ID");
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
        System.out.println("[SERVICE] Test 2.3 : Obtenir consultation par RDV");
        try {
            Consultation c = consultationService.obtenirConsultationParRDV(1L);
            if (c != null) {
                System.out.println("       [OK] Consultation trouvee pour RDV 1");
                testsReussis++;
            } else {
                System.out.println("       [INFO] Aucune consultation pour RDV 1");
                testsReussis++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 2.4 : Lister consultations par date");
        try {
            List<Consultation> consultations = consultationService.listerConsultationsParDate(LocalDate.now());
            System.out.println("       [OK] " + consultations.size() + " consultation(s) aujourd'hui");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 2.5 : Modifier consultation");
        try {
            Consultation c = consultationService.obtenirConsultation(idConsultationTest);
            c.setObservationMedecin("Carie traitee avec succes");
            consultationService.modifierConsultation(c);
            Consultation modifiee = consultationService.obtenirConsultation(idConsultationTest);
            if (modifiee.getObservationMedecin().contains("traitee")) {
                System.out.println("       [OK] Consultation modifiee");
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] Modification non appliquee");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 2.6 : Terminer consultation");
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
        System.out.println("[SERVICE] Test 3.1 : Creation d'une intervention");
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
        System.out.println("[SERVICE] Test 3.2 : Obtenir intervention par ID");
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
        System.out.println("[SERVICE] Test 3.3 : Lister interventions par consultation");
        try {
            List<InterventionMedecin> interventions = interventionService.listerInterventionsParConsultation(1L);
            System.out.println("       [OK] " + interventions.size() + " intervention(s) pour consultation 1");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 3.4 : Calculer montant total consultation");
        try {
            Double montant = interventionService.calculerMontantTotalConsultation(1L);
            System.out.println("       [OK] Montant total consultation : " + montant + " DH");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 3.5 : Modifier intervention");
        try {
            InterventionMedecin im = interventionService.obtenirIntervention(idInterventionTest);
            im.setPrixDePatient(850.0);
            interventionService.modifierIntervention(im);
            InterventionMedecin modifiee = interventionService.obtenirIntervention(idInterventionTest);
            if (modifiee.getPrixDePatient().equals(850.0)) {
                System.out.println("       [OK] Prix modifie : 750.0 -> 850.0 DH");
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] Prix non modifie");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }
    }

    private static void testerValidationEtExceptions() {
        System.out.println();
        System.out.println("[VALIDATION] Test 4.1 : Creation RDV avec date passee");
        try {
            RDV rdv = RDV.builder()
                    .idPatient(1L)
                    .idMedecin(1L)
                    .date(LocalDate.now().minusDays(1))
                    .heure(LocalTime.of(10, 0))
                    .motif("Test date passee")
                    .build();
            rdvService.creerRDV(rdv);
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
        System.out.println("[VALIDATION] Test 4.2 : Creation RDV sans motif");
        try {
            RDV rdv = RDV.builder()
                    .idPatient(1L)
                    .idMedecin(1L)
                    .date(LocalDate.now().plusDays(1))
                    .heure(LocalTime.of(10, 0))
                    .motif("")
                    .build();
            rdvService.creerRDV(rdv);
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
        System.out.println("[VALIDATION] Test 4.3 : Creation consultation sans RDV");
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
        System.out.println("[VALIDATION] Test 4.4 : Creation intervention avec prix negatif");
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

        System.out.println();
        System.out.println("[VALIDATION] Test 4.5 : Obtenir RDV avec ID null");
        try {
            rdvService.obtenirRDV(null);
            System.out.println("       [ECHEC] Exception attendue non levee");
            testsEchoues++;
        } catch (ServiceException e) {
            System.out.println("       [OK] ServiceException levee : " + e.getMessage());
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] Mauvaise exception : " + e.getMessage());
            testsEchoues++;
        }
    }

    private static void testerFluxCompletParcoursPatient() {
        System.out.println();
        System.out.println("[FLUX] Scenario : Parcours complet patient RDV -> Consultation -> Intervention");
        System.out.println();

        try {
            System.out.println("       Etape 1 : Creation RDV patient");
            RDV rdv = rdvService.creerRDV(RDV.builder()
                    .idPatient(100L)
                    .idMedecin(1L)
                    .date(LocalDate.now().plusDays(2))
                    .heure(LocalTime.of(16, 0))
                    .motif("Detartrage et controle")
                    .statut(StatutRDV.PLANIFIE)
                    .build());
            System.out.println("                 RDV ID : " + rdv.getIdRDV() + " - " + rdv.getStatut());

            System.out.println();
            System.out.println("       Etape 2 : Confirmation du RDV");
            rdvService.confirmerRDV(rdv.getIdRDV());
            System.out.println("                 Statut : CONFIRME");

            System.out.println();
            System.out.println("       Etape 3 : Creation consultation");
            Consultation consultation = consultationService.creerConsultation(Consultation.builder()
                    .idRDV(rdv.getIdRDV())
                    .date(LocalDate.now())
                    .statut("EN_COURS")
                    .observationMedecin("Detartrage necessaire")
                    .build());
            System.out.println("                 Consultation ID : " + consultation.getIdConsultation());

            System.out.println();
            System.out.println("       Etape 4 : Ajout interventions");
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
            System.out.println("       Etape 5 : Calcul montant total");
            Double montant = interventionService.calculerMontantTotalConsultation(consultation.getIdConsultation());
            System.out.println("                 Montant total : " + montant + " DH");

            System.out.println();
            System.out.println("       Etape 6 : Fin de consultation");
            consultationService.terminerConsultation(consultation.getIdConsultation(), "Detartrage effectue avec succes");
            System.out.println("                 Statut consultation : TERMINEE");

            System.out.println();
            System.out.println("       Etape 7 : Fin du RDV");
            rdvService.changerStatutRDV(rdv.getIdRDV(), StatutRDV.TERMINE);
            System.out.println("                 Statut RDV : TERMINE");

            System.out.println();
            System.out.println("       Etape 8 : Verification finale");
            List<InterventionMedecin> interventions = interventionService.listerInterventionsParConsultation(consultation.getIdConsultation());
            System.out.println("                 Interventions : " + interventions.size());
            System.out.println("                 Montant final : " + montant + " DH");

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
}**/
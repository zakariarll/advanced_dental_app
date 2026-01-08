package ma.dentalTech.service.modules.RDVPatient;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.dossierMedical.RDV;
import ma.dentalTech.entities.enums.Assurance;
import ma.dentalTech.entities.enums.Sexe;
import ma.dentalTech.entities.enums.StatutRDV;
import ma.dentalTech.entities.patient.Patient;
//import ma.dentalTech.repository.DbTestUtils;
import ma.dentalTech.repository.modules.dossierMedical.impl.mySQL.RDVRepositoryImpl;
import ma.dentalTech.repository.modules.patient.impl.mySQL.PatientRepositoryImpl;
import ma.dentalTech.service.modules.RDVPatient.api.IGestionRDVService;
import ma.dentalTech.service.modules.RDVPatient.impl.GestionRDVServiceImpl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class GestionRDVModuleServiceTest {

    private static IGestionRDVService gestionRDVService;
    // On a besoin du PatientRepo juste pour créer des données de test (FK)
    private static PatientRepositoryImpl patientRepo;

    private static int testsReussis = 0;
    private static int testsEchoues = 0;

    // Variables globales pour le flux
    private static Long idPatient1;
    private static Long idPatient2;
    private static Long idRDVGlobal;

    public static void main(String[] args) {
        System.out.println("======================================================================");
        System.out.println("TEST FONCTIONNEL - MODULE GESTION RDV (AGENDA & FLUX)");
        System.out.println("======================================================================");
        System.out.println();

        try {
            initialiserServicesEtDonnees();
        } catch (Exception e) {
            System.err.println("[ERREUR CRITIQUE] Impossible d'initialiser la base de données : " + e.getMessage());
            return;
        }

        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 1 : PLANIFICATION ET DISPONIBILITE");
        System.out.println("----------------------------------------------------------------------");
        testerPlanification();

        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 2 : GESTION DES CONFLITS ET LISTE D'ATTENTE");
        System.out.println("----------------------------------------------------------------------");
        testerConflitEtListeAttente();

        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 3 : CONFIRMATION ET EMAIL (SIMULE)");
        System.out.println("----------------------------------------------------------------------");
        testerConfirmation();

        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 4 : ANNULATION ET LIBERATION");
        System.out.println("----------------------------------------------------------------------");
        testerAnnulation();

        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 5 : MODIFICATION ET AGENDA");
        System.out.println("----------------------------------------------------------------------");
        testerModificationEtAgenda();

        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 6 : VALIDATION ET EXCEPTIONS");
        System.out.println("----------------------------------------------------------------------");
        testerExceptions();

        afficherResultats();
    }

    private static void initialiserServicesEtDonnees() {
        System.out.println("[INIT] Nettoyage DB et Initialisation...");
        // Important : Nettoyer la base pour avoir un état propre
        //DbTestUtils.cleanDatabase();

        // Init Repos et Service
        patientRepo = new PatientRepositoryImpl();
        gestionRDVService = new GestionRDVServiceImpl(new RDVRepositoryImpl());

        // Création de 2 patients pour les tests
        Patient p1 = Patient.builder().nom("Alaoui").prenom("Ahmed").sexe(Sexe.Homme).assurance(Assurance.CNSS).build();
        patientRepo.create(p1);
        idPatient1 = p1.getIdPatient();

        Patient p2 = Patient.builder().nom("Benani").prenom("Sara").sexe(Sexe.Femme).assurance(Assurance.Autre).build();
        patientRepo.create(p2);
        idPatient2 = p2.getIdPatient();

        System.out.println("[INIT] Services prets. Patients tests crees (ID: " + idPatient1 + ", " + idPatient2 + ")");
        System.out.println();
    }

    private static void testerPlanification() {
        System.out.println();
        System.out.println("[SERVICE] Test 1.1 : Planifier un RDV valide");
        try {
            // RDV pour demain à 10h00
            RDV rdv = gestionRDVService.planifierRDV(
                    idPatient1,
                    1L, // Medecin ID 1
                    LocalDate.now().plusDays(1),
                    LocalTime.of(10, 0),
                    "Première consultation"
            );

            idRDVGlobal = rdv.getIdRDV();

            if (idRDVGlobal != null && rdv.getStatut() == StatutRDV.PLANIFIE) {
                System.out.println("       [OK] RDV planifie avec ID : " + idRDVGlobal);
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] Creation echouee ou statut incorrect");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            e.printStackTrace();
            testsEchoues++;
        }
    }

    private static void testerConflitEtListeAttente() {
        System.out.println();
        System.out.println("[SERVICE] Test 2.1 : Tentative de double reservation (Conflit)");
        try {
            // Mêmes date/heure/medecin que le Test 1.1
            gestionRDVService.planifierRDV(
                    idPatient2,
                    1L,
                    LocalDate.now().plusDays(1),
                    LocalTime.of(10, 0),
                    "Urgence"
            );
            System.out.println("       [ECHEC] Le conflit n'a pas ete detecte !");
            testsEchoues++;
        } catch (ServiceException e) {
            System.out.println("       [OK] Exception levee (Attendu) : " + e.getMessage());
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] Mauvaise exception : " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 2.2 : Ajout en Liste d'attente");
        try {
            Patient p2 = patientRepo.findById(idPatient2);
            LocalDate dateSouhaitee = LocalDate.now().plusDays(1);

            gestionRDVService.ajouterEnListeAttente(p2, dateSouhaitee);

            List<Patient> attente = gestionRDVService.getListeAttente(dateSouhaitee);

            if (attente.size() == 1 && attente.get(0).getIdPatient().equals(idPatient2)) {
                System.out.println("       [OK] Patient ajoute en liste d'attente pour le " + dateSouhaitee);
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] Liste d'attente vide ou incorrecte");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }
    }

    private static void testerConfirmation() {
        System.out.println();
        System.out.println("[SERVICE] Test 3.1 : Confirmation du RDV");
        try {
            gestionRDVService.confirmerRDV(idRDVGlobal);

            // Verif via historique
            List<RDV> hist = gestionRDVService.consulterHistoriquePatient(idPatient1);
            RDV rdv = hist.stream().filter(r -> r.getIdRDV().equals(idRDVGlobal)).findFirst().orElse(null);

            if (rdv != null && rdv.getStatut() == StatutRDV.CONFIRME) {
                System.out.println("       [OK] RDV confirme (Email simule envoye)");
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] Statut non mis a jour");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }
    }

    private static void testerAnnulation() {
        System.out.println();
        System.out.println("[SERVICE] Test 4.1 : Annulation du RDV");
        try {
            gestionRDVService.annulerRDV(idRDVGlobal, "Patient malade");

            List<RDV> hist = gestionRDVService.consulterHistoriquePatient(idPatient1);
            RDV rdv = hist.stream().filter(r -> r.getIdRDV().equals(idRDVGlobal)).findFirst().orElse(null);

            if (rdv != null && rdv.getStatut() == StatutRDV.ANNULE) {
                System.out.println("       [OK] RDV annule. (Verification liste attente declenchee)");
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] Statut non ANNULE");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 4.2 : Recuperation du creneau par le patient en attente");
        try {
            // Maintenant que le RDV de 10h est annulé, le patient 2 doit pouvoir le prendre
            RDV rdvRecupere = gestionRDVService.planifierRDV(
                    idPatient2,
                    1L,
                    LocalDate.now().plusDays(1),
                    LocalTime.of(10, 0),
                    "Recuperation creneau"
            );

            if (rdvRecupere.getIdRDV() != null) {
                System.out.println("       [OK] Creneau libere reattribue au Patient 2");
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] Impossible de prendre le creneau libere");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }
    }

    private static void testerModificationEtAgenda() {
        System.out.println();
        System.out.println("[SERVICE] Test 5.1 : Modifier un RDV");
        try {
            // On modifie le RDV de 10h vers 11h
            // Attention : il faut récupérer l'ID du dernier RDV créé pour le patient 2
            List<RDV> list = gestionRDVService.consulterHistoriquePatient(idPatient2);
            Long lastId = list.get(list.size()-1).getIdRDV();

            gestionRDVService.modifierRDV(lastId, LocalDate.now().plusDays(1), LocalTime.of(11, 0));

            // Verif via consultation Agenda
            List<RDV> agenda = gestionRDVService.consulterPlanningMedecin(1L, LocalDate.now().plusDays(1));
            boolean found = agenda.stream().anyMatch(r -> r.getIdRDV().equals(lastId) && r.getHeure().equals(LocalTime.of(11, 0)));

            if (found) {
                System.out.println("       [OK] RDV deplace a 11h00");
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] RDV non trouve a la nouvelle heure");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }
    }

    private static void testerExceptions() {
        System.out.println();
        System.out.println("[VALIDATION] Test 6.1 : Date dans le passe");
        try {
            gestionRDVService.planifierRDV(idPatient1, 1L, LocalDate.now().minusDays(1), LocalTime.of(10, 0), "Test");
            System.out.println("       [ECHEC] Exception non levee pour date passee");
            testsEchoues++;
        } catch (ValidationException e) {
            System.out.println("       [OK] ValidationException levee : " + e.getMessage());
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] Mauvaise exception");
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[VALIDATION] Test 6.2 : RDV Inexistant");
        try {
            gestionRDVService.confirmerRDV(99999L);
            System.out.println("       [ECHEC] Exception non levee pour ID inconnu");
            testsEchoues++;
        } catch (ServiceException e) {
            System.out.println("       [OK] ServiceException levee : " + e.getMessage());
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] Mauvaise exception");
            testsEchoues++;
        }
    }

    private static void afficherResultats() {
        System.out.println();
        System.out.println("======================================================================");
        System.out.println("RESULTATS DES TESTS GESTION RDV");
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
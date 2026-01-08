package ma.dentalTech.repository.modules.dossierMedical;

import ma.dentalTech.entities.dossierMedical.Consultation;
import ma.dentalTech.entities.dossierMedical.InterventionMedecin;
import ma.dentalTech.entities.dossierMedical.RDV;
import ma.dentalTech.entities.enums.StatutRDV;
import ma.dentalTech.repository.modules.dossierMedical.api.ConsultationRepository;
import ma.dentalTech.repository.modules.dossierMedical.api.InterventionMedecinRepository;
import ma.dentalTech.repository.modules.dossierMedical.api.RDVRepository;
import ma.dentalTech.repository.modules.dossierMedical.impl.mySQL.ConsultationRepositoryImpl;
import ma.dentalTech.repository.modules.dossierMedical.impl.mySQL.InterventionMedecinRepositoryImpl;
import ma.dentalTech.repository.modules.dossierMedical.impl.mySQL.RDVRepositoryImpl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class DossierMedicalModuleTest {

    private static RDVRepository rdvRepository;
    private static ConsultationRepository consultationRepository;
    private static InterventionMedecinRepository interventionRepository;
    private static int testsReussis = 0;
    private static int testsEchoues = 0;

    public static void main(String[] args) {
        System.out.println("======================================================================");
        System.out.println("TEST FONCTIONNEL - MODULE DOSSIER MEDICAL (REPOSITORY)");
        System.out.println("======================================================================");
        System.out.println();
        initialiserRepositories();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 1 : TESTS RDV REPOSITORY");
        System.out.println("----------------------------------------------------------------------");
        testerRDVRepository();
        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 2 : TESTS CONSULTATION REPOSITORY");
        System.out.println("----------------------------------------------------------------------");
        testerConsultationRepository();
        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 3 : TESTS INTERVENTION MEDECIN REPOSITORY");
        System.out.println("----------------------------------------------------------------------");
        testerInterventionRepository();
        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 4 : FLUX COMPLET PARCOURS PATIENT");
        System.out.println("----------------------------------------------------------------------");
        testerFluxParcoursPatient();
        afficherResultats();
    }

    private static void initialiserRepositories() {
        System.out.println("[INIT] Initialisation des repositories...");
        rdvRepository = new RDVRepositoryImpl();
        consultationRepository = new ConsultationRepositoryImpl();
        interventionRepository = new InterventionMedecinRepositoryImpl();
        System.out.println("[INIT] Initialisation terminee");
        System.out.println();
    }

    private static void testerRDVRepository() {
        Long idRDVTest = null;
        System.out.println();
        System.out.println("[REPO] Test 1.1 : Creation d'un RDV");
        try {
            RDV rdv = RDV.builder()
                    .idPatient(1L)
                    .idMedecin(1L)
                    .date(LocalDate.now().plusDays(7))
                    .heure(LocalTime.of(10, 0))
                    .motif("Consultation de controle")
                    .statut(StatutRDV.PLANIFIE)
                    .build();
            rdvRepository.create(rdv);
            idRDVTest = rdv.getIdRDV();
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
        System.out.println("[REPO] Test 1.2 : Lecture par ID (findById)");
        try {
            RDV rdv = rdvRepository.findById(idRDVTest);
            if (rdv != null && rdv.getMotif().equals("Consultation de controle")) {
                System.out.println("       [OK] RDV trouve : " + rdv.getMotif());
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
        System.out.println("[REPO] Test 1.3 : Recherche par date (findByDate)");
        try {
            List<RDV> rdvs = rdvRepository.findByDate(LocalDate.now().plusDays(7));
            System.out.println("       [OK] " + rdvs.size() + " RDV(s) pour cette date");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 1.4 : Recherche par medecin (findByMedecinId)");
        try {
            List<RDV> rdvs = rdvRepository.findByMedecinId(1L);
            System.out.println("       [OK] " + rdvs.size() + " RDV(s) pour medecin 1");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 1.5 : Recherche par patient (findByPatientId)");
        try {
            List<RDV> rdvs = rdvRepository.findByPatientId(1L);
            System.out.println("       [OK] " + rdvs.size() + " RDV(s) pour patient 1");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 1.6 : Modification statut RDV (update)");
        try {
            RDV rdv = rdvRepository.findById(idRDVTest);
            if (rdv != null) {
                rdv.setStatut(StatutRDV.CONFIRME);
                rdvRepository.update(rdv);
                RDV updated = rdvRepository.findById(idRDVTest);
                if (updated != null && updated.getStatut() == StatutRDV.CONFIRME) {
                    System.out.println("       [OK] Statut modifie : PLANIFIE -> CONFIRME");
                    testsReussis++;
                } else {
                    System.out.println("       [ECHEC] Statut non modifie");
                    testsEchoues++;
                }
            } else {
                System.out.println("       [ECHEC] RDV non trouve");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 1.7 : Liste tous les RDV (findAll)");
        try {
            List<RDV> rdvs = rdvRepository.findAll();
            System.out.println("       [OK] " + rdvs.size() + " RDV(s) au total");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }
    }

    private static void testerConsultationRepository() {
        Long idConsultationTest = null;
        System.out.println();
        System.out.println("[REPO] Test 2.1 : Creation d'une consultation");
        try {
            Consultation consultation = Consultation.builder()
                    .idRDV(1L)
                    .date(LocalDate.now())
                    .statut("EN_COURS")
                    .observationMedecin("Patient en bonne sante")
                    .build();
            consultationRepository.create(consultation);
            idConsultationTest = consultation.getIdConsultation();
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
        System.out.println("[REPO] Test 2.2 : Lecture par ID (findById)");
        try {
            Consultation c = consultationRepository.findById(idConsultationTest);
            if (c != null && c.getStatut().equals("EN_COURS")) {
                System.out.println("       [OK] Consultation trouvee : " + c.getStatut());
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
        System.out.println("[REPO] Test 2.3 : Recherche par RDV (findByRDVId)");
        try {
            Optional<Consultation> optionalC = consultationRepository.findByRDVId(1L);
            if (optionalC.isPresent()) {
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
        System.out.println("[REPO] Test 2.4 : Recherche par date (findByDate)");
        try {
            List<Consultation> list = consultationRepository.findByDate(LocalDate.now());
            System.out.println("       [OK] " + list.size() + " consultation(s) aujourd'hui");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 2.5 : Modification consultation (update)");
        try {
            Consultation c = consultationRepository.findById(idConsultationTest);
            if (c != null) {
                c.setStatut("TERMINEE");
                c.setObservationMedecin("Mise a jour observation");
                consultationRepository.update(c);
                Consultation updated = consultationRepository.findById(idConsultationTest);
                if (updated != null && updated.getStatut().equals("TERMINEE")) {
                    System.out.println("       [OK] Consultation terminee");
                    testsReussis++;
                } else {
                    System.out.println("       [ECHEC] Statut non modifie");
                    testsEchoues++;
                }
            } else {
                System.out.println("       [ECHEC] Consultation non trouvee");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 2.6 : Liste toutes consultations (findAll)");
        try {
            List<Consultation> list = consultationRepository.findAll();
            System.out.println("       [OK] " + list.size() + " consultation(s) au total");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }
    }

    private static void testerInterventionRepository() {
        Long idInterventionTest = null;
        System.out.println();
        System.out.println("[REPO] Test 3.1 : Creation d'une intervention");
        try {
            InterventionMedecin im = InterventionMedecin.builder()
                    .idConsultation(1L)
                    .idMedecin(1L)
                    .prixDePatient(500.0)
                    .build();
            interventionRepository.create(im);
            idInterventionTest = im.getIdIM();
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
        System.out.println("[REPO] Test 3.2 : Lecture par ID (findById)");
        try {
            InterventionMedecin im = interventionRepository.findById(idInterventionTest);
            if (im != null && im.getPrixDePatient().equals(500.0)) {
                System.out.println("       [OK] Intervention trouvee : " + im.getPrixDePatient() + " DH");
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
        System.out.println("[REPO] Test 3.3 : Recherche par consultation (findByConsultationId)");
        try {
            List<InterventionMedecin> list = interventionRepository.findByConsultationId(1L);
            System.out.println("       [OK] " + list.size() + " intervention(s) pour consultation 1");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 3.4 : Modification intervention (update)");
        try {
            InterventionMedecin im = interventionRepository.findById(idInterventionTest);
            if (im != null) {
                im.setPrixDePatient(600.0);
                interventionRepository.update(im);
                InterventionMedecin updated = interventionRepository.findById(idInterventionTest);
                if (updated != null && updated.getPrixDePatient().equals(600.0)) {
                    System.out.println("       [OK] Prix modifie : 500.0 -> 600.0 DH");
                    testsReussis++;
                } else {
                    System.out.println("       [ECHEC] Prix non modifie");
                    testsEchoues++;
                }
            } else {
                System.out.println("       [ECHEC] Intervention non trouvee");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 3.5 : Liste toutes interventions (findAll)");
        try {
            List<InterventionMedecin> list = interventionRepository.findAll();
            System.out.println("       [OK] " + list.size() + " intervention(s) au total");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }
    }

    private static void testerFluxParcoursPatient() {
        System.out.println();
        System.out.println("[FLUX] Scenario : Parcours complet RDV -> Consultation -> Intervention");
        System.out.println();
        try {
            System.out.println("       Etape 1 : Creation RDV");
            RDV rdv = RDV.builder()
                    .idPatient(100L)
                    .idMedecin(1L)
                    .date(LocalDate.now().plusDays(1))
                    .heure(LocalTime.of(14, 0))
                    .motif("Douleur dentaire")
                    .statut(StatutRDV.PLANIFIE)
                    .build();
            rdvRepository.create(rdv);
            System.out.println("                 RDV ID : " + rdv.getIdRDV());

            System.out.println();
            System.out.println("       Etape 2 : Confirmation RDV");
            rdv.setStatut(StatutRDV.CONFIRME);
            rdvRepository.update(rdv);
            System.out.println("                 Statut : " + rdv.getStatut());

            System.out.println();
            System.out.println("       Etape 3 : Creation consultation");
            Consultation consultation = Consultation.builder()
                    .idRDV(rdv.getIdRDV())
                    .date(LocalDate.now())
                    .statut("EN_COURS")
                    .observationMedecin("Carie sur molaire 36")
                    .build();
            consultationRepository.create(consultation);
            System.out.println("                 Consultation ID : " + consultation.getIdConsultation());

            System.out.println();
            System.out.println("       Etape 4 : Creation intervention");
            InterventionMedecin im = InterventionMedecin.builder()
                    .idConsultation(consultation.getIdConsultation())
                    .idMedecin(1L)
                    .prixDePatient(450.0)
                    .build();
            interventionRepository.create(im);
            System.out.println("                 Intervention ID : " + im.getIdIM());
            System.out.println("                 Prix : " + im.getPrixDePatient() + " DH");

            System.out.println();
            System.out.println("       Etape 5 : Fin consultation");
            consultation.setStatut("TERMINEE");
            consultationRepository.update(consultation);
            System.out.println("                 Statut consultation : " + consultation.getStatut());

            System.out.println();
            System.out.println("       Etape 6 : Fin RDV");
            rdv.setStatut(StatutRDV.TERMINE);
            rdvRepository.update(rdv);
            System.out.println("                 Statut RDV : " + rdv.getStatut());

            System.out.println();
            System.out.println("       [OK] Flux parcours patient execute avec succes");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] Erreur : " + e.getMessage());
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
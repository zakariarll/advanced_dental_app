package ma.dentalTech.repository.modules.actes;

import ma.dentalTech.entities.actes.Acte;
import ma.dentalTech.entities.actes.ActeIntervention;
import ma.dentalTech.repository.modules.actes.api.ActeRepository;
import ma.dentalTech.repository.modules.actes.api.ActeInterventionRepository;
import ma.dentalTech.repository.modules.actes.impl.mySQL.ActeRepositoryImpl;
import ma.dentalTech.repository.modules.actes.impl.mySQL.ActeInterventionRepositoryImpl;

import java.util.List;
import java.util.Optional;

public class ActeModuleTest {

    private static ActeRepository acteRepository;
    private static ActeInterventionRepository acteInterventionRepository;
    private static int testsReussis = 0;
    private static int testsEchoues = 0;

    public static void main(String[] args) {
        System.out.println("======================================================================");
        System.out.println("TEST FONCTIONNEL - MODULE ACTES (REPOSITORY)");
        System.out.println("======================================================================");
        System.out.println();

        initialiserRepositories();

        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 1 : TESTS ACTE REPOSITORY");
        System.out.println("----------------------------------------------------------------------");
        testerActeRepository();

        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 2 : TESTS ACTE_INTERVENTION REPOSITORY");
        System.out.println("----------------------------------------------------------------------");
        testerActeInterventionRepository();

        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 3 : FLUX COMPLET REPOSITORY");
        System.out.println("----------------------------------------------------------------------");
        testerFluxComplet();

        afficherResultats();
    }

    private static void initialiserRepositories() {
        System.out.println("[INIT] Initialisation des repositories...");
        acteRepository = new ActeRepositoryImpl();
        acteInterventionRepository = new ActeInterventionRepositoryImpl();
        System.out.println("[INIT] Initialisation terminee");
        System.out.println();
    }

    private static void testerActeRepository() {
        Long idActeTest = null;

        System.out.println();
        System.out.println("[REPO] Test 1.1 : Creation d'un acte");
        try {
            Acte acte = Acte.builder()
                    .libelle("Detartrage Test")
                    .categorie("SOINS")
                    .prixDeBase(300.0)
                    .code(9001)
                    .description("Nettoyage dentaire complet")
                    .build();
            acteRepository.create(acte);
            idActeTest = acte.getIdActe();
            if (idActeTest != null) {
                System.out.println("       [OK] Acte cree avec ID : " + idActeTest);
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] ID non genere");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        if (idActeTest == null) {
            System.out.println("[ERREUR] Impossible de continuer sans ID d'acte valide");
            return;
        }

        System.out.println();
        System.out.println("[REPO] Test 1.2 : Lecture par ID (findById)");
        try {
            Optional<Acte> optionalActe = Optional.ofNullable(acteRepository.findById(idActeTest));
            if (optionalActe.isPresent() && optionalActe.get().getLibelle().equals("Detartrage Test")) {
                System.out.println("       [OK] Acte trouve : " + optionalActe.get().getLibelle());
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] Acte non trouve ou incorrect");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 1.3 : Recherche par code (findByCode)");
        try {
            Optional<Acte> optionalActe = acteRepository.findByCode(9001);
            if (optionalActe.isPresent()) {
                System.out.println("       [OK] Acte trouve par code : " + optionalActe.get().getLibelle());
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] Acte non trouve");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 1.4 : Recherche par categorie (findByCategorie)");
        try {
            List<Acte> actes = acteRepository.findByCategorie("SOINS");
            System.out.println("       [OK] " + actes.size() + " acte(s) dans categorie SOINS");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 1.5 : Liste tous les actes (findAll)");
        try {
            List<Acte> actes = acteRepository.findAll();
            System.out.println("       [OK] " + actes.size() + " acte(s) au total");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 1.6 : Modification d'un acte (update)");
        try {
            Acte acte = acteRepository.findById(idActeTest);
            if (acte != null) {
                acte.setPrixDeBase(350.0);
                acte.setDescription("Description modifiee");
                acteRepository.update(acte);
                Acte acteModifie = acteRepository.findById(idActeTest);
                if (acteModifie != null && acteModifie.getPrixDeBase().equals(350.0)) {
                    System.out.println("       [OK] Prix modifie : 300.0 -> 350.0");
                    testsReussis++;
                } else {
                    System.out.println("       [ECHEC] Prix non modifie");
                    testsEchoues++;
                }
            } else {
                System.out.println("       [ECHEC] Acte non trouve");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 1.7 : Recherche par libelle (findByLibelleContaining)");
        try {
            List<Acte> actes = acteRepository.findByLibelleContaining("Test");
            System.out.println("       [OK] " + actes.size() + " acte(s) contenant 'Test'");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 1.8 : Recherche par fourchette de prix (findByPrixBetween)");
        try {
            List<Acte> actes = acteRepository.findByPrixBetween(100.0, 500.0);
            System.out.println("       [OK] " + actes.size() + " acte(s) entre 100 et 500 DH");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 1.9 : Liste des categories (findAllCategories)");
        try {
            List<String> categories = acteRepository.findAllCategories();
            System.out.println("       [OK] Categories : " + String.join(", ", categories));
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 1.10 : Verification existence code (existsByCode)");
        try {
            boolean existe = acteRepository.existsByCode(9001);
            boolean nexistePas = !acteRepository.existsByCode(99999);
            if (existe && nexistePas) {
                System.out.println("       [OK] Verification code fonctionne");
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] Verification incorrecte");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 1.11 : Verification existence libelle (existsByLibelle)");
        try {
            boolean existe = acteRepository.existsByLibelle("Detartrage Test");
            if (existe) {
                System.out.println("       [OK] Libelle existe");
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] Libelle non detecte");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }
    }

    private static void testerActeInterventionRepository() {
        Long idActeInterventionTest = null;

        System.out.println();
        System.out.println("[REPO] Test 2.1 : Creation association acte-intervention");
        try {
            Optional<Acte> optionalActe = acteRepository.findByCode(9001);
            if (optionalActe.isPresent()) {
                ActeIntervention ai = ActeIntervention.builder()
                        .idActe(optionalActe.get().getIdActe())
                        .idIM(1L)
                        .build();
                acteInterventionRepository.create(ai);
                idActeInterventionTest = ai.getIdActeIntervention();
                if (idActeInterventionTest != null) {
                    System.out.println("       [OK] Association creee avec ID : " + idActeInterventionTest);
                    testsReussis++;
                } else {
                    System.out.println("       [ECHEC] ID non genere");
                    testsEchoues++;
                }
            } else {
                System.out.println("       [ECHEC] Acte non trouve");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        if (idActeInterventionTest == null) {
            System.out.println("[ERREUR] Impossible de continuer sans ID d'association valide");
            return;
        }

        System.out.println();
        System.out.println("[REPO] Test 2.2 : Lecture par ID (findById)");
        try {
            ActeIntervention ai = acteInterventionRepository.findById(idActeInterventionTest);
            if (ai != null) {
                System.out.println("       [OK] Association trouvee : Acte " + ai.getIdActe() + " -> IM " + ai.getIdIM());
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] Association non trouvee");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 2.3 : Recherche par intervention (findByInterventionMedecinId)");
        try {
            List<ActeIntervention> associations = acteInterventionRepository.findByInterventionMedecinId(1L);
            System.out.println("       [OK] " + associations.size() + " acte(s) pour intervention 1");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 2.4 : Recherche par acte (findByActeId)");
        try {
            Optional<Acte> optionalActe = acteRepository.findByCode(9001);
            if (optionalActe.isPresent()) {
                List<ActeIntervention> associations = acteInterventionRepository.findByActeId(optionalActe.get().getIdActe());
                System.out.println("       [OK] " + associations.size() + " intervention(s) pour cet acte");
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] Acte non trouve");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 2.5 : Comptage utilisations (countByActeId)");
        try {
            Optional<Acte> optionalActe = acteRepository.findByCode(9001);
            if (optionalActe.isPresent()) {
                int count = acteInterventionRepository.countByActeId(optionalActe.get().getIdActe());
                System.out.println("       [OK] Acte utilise " + count + " fois");
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] Acte non trouve");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 2.6 : Verification existence (existsByActeIdAndInterventionMedecinId)");
        try {
            Optional<Acte> optionalActe = acteRepository.findByCode(9001);
            if (optionalActe.isPresent()) {
                boolean existe = acteInterventionRepository.existsByActeIdAndInterventionMedecinId(optionalActe.get().getIdActe(), 1L);
                if (existe) {
                    System.out.println("       [OK] Association detectee");
                    testsReussis++;
                } else {
                    System.out.println("       [ECHEC] Association non detectee");
                    testsEchoues++;
                }
            } else {
                System.out.println("       [ECHEC] Acte non trouve");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 2.7 : Liste toutes les associations (findAll)");
        try {
            List<ActeIntervention> associations = acteInterventionRepository.findAll();
            System.out.println("       [OK] " + associations.size() + " association(s) au total");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }
    }

    private static void testerFluxComplet() {
        System.out.println();
        System.out.println("[FLUX] Scenario : Gestion catalogue actes et associations");
        System.out.println();

        try {
            System.out.println("       Etape 1 : Creation de 3 nouveaux actes");
            Acte acte1 = Acte.builder()
                    .libelle("Consultation Flux")
                    .categorie("CONSULTATION")
                    .prixDeBase(150.0)
                    .code(8001)
                    .description("Consultation standard")
                    .build();
            acteRepository.create(acte1);
            System.out.println("                 - " + acte1.getLibelle() + " (ID: " + acte1.getIdActe() + ")");

            Acte acte2 = Acte.builder()
                    .libelle("Radio Panoramique Flux")
                    .categorie("RADIOLOGIE")
                    .prixDeBase(200.0)
                    .code(8002)
                    .description("Radiographie panoramique")
                    .build();
            acteRepository.create(acte2);
            System.out.println("                 - " + acte2.getLibelle() + " (ID: " + acte2.getIdActe() + ")");

            Acte acte3 = Acte.builder()
                    .libelle("Plombage Flux")
                    .categorie("SOINS")
                    .prixDeBase(400.0)
                    .code(8003)
                    .description("Obturation composite")
                    .build();
            acteRepository.create(acte3);
            System.out.println("                 - " + acte3.getLibelle() + " (ID: " + acte3.getIdActe() + ")");

            System.out.println();
            System.out.println("       Etape 2 : Association des actes a l'intervention 100");
            ActeIntervention ai1 = ActeIntervention.builder().idActe(acte1.getIdActe()).idIM(100L).build();
            acteInterventionRepository.create(ai1);
            ActeIntervention ai2 = ActeIntervention.builder().idActe(acte2.getIdActe()).idIM(100L).build();
            acteInterventionRepository.create(ai2);
            ActeIntervention ai3 = ActeIntervention.builder().idActe(acte3.getIdActe()).idIM(100L).build();
            acteInterventionRepository.create(ai3);
            System.out.println("                 3 actes associes");

            System.out.println();
            System.out.println("       Etape 3 : Verification des associations");
            List<ActeIntervention> associations = acteInterventionRepository.findByInterventionMedecinId(100L);
            System.out.println("                 " + associations.size() + " acte(s) pour intervention 100");

            System.out.println();
            System.out.println("       Etape 4 : Calcul montant total");
            double montantTotal = 0.0;
            for (ActeIntervention ai : associations) {
                Acte acte = acteRepository.findById(ai.getIdActe());
                if (acte != null) {
                    montantTotal += acte.getPrixDeBase();
                    System.out.println("                 - " + acte.getLibelle() + " : " + acte.getPrixDeBase() + " DH");
                }
            }
            System.out.println("                 Total : " + montantTotal + " DH");

            System.out.println();
            System.out.println("       Etape 5 : Modification prix d'un acte");
            acte3.setPrixDeBase(350.0);
            acteRepository.update(acte3);
            System.out.println("                 Plombage : 400.0 -> 350.0 DH");

            System.out.println();
            System.out.println("       Etape 6 : Nouveau calcul montant");
            montantTotal = 0.0;
            for (ActeIntervention ai : associations) {
                Acte acte = acteRepository.findById(ai.getIdActe());
                if (acte != null) {
                    montantTotal += acte.getPrixDeBase();
                }
            }
            System.out.println("                 Nouveau total : " + montantTotal + " DH");

            System.out.println();
            System.out.println("       Etape 7 : Statistiques finales");
            List<Acte> tousLesActes = acteRepository.findAll();
            List<String> categories = acteRepository.findAllCategories();
            System.out.println("                 Nombre total d'actes : " + tousLesActes.size());
            System.out.println("                 Categories : " + String.join(", ", categories));

            System.out.println();
            System.out.println("       Etape 8 : Suppression associations intervention 100");
            acteInterventionRepository.deleteByInterventionMedecinId(100L);
            List<ActeIntervention> apresSuppr = acteInterventionRepository.findByInterventionMedecinId(100L);
            System.out.println("                 Associations restantes : " + apresSuppr.size());

            System.out.println();
            System.out.println("       [OK] Flux complet execute avec succes");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] Erreur dans le flux : " + e.getMessage());
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
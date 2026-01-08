package ma.dentalTech.service.modules.actes;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.actes.Acte;
import ma.dentalTech.entities.actes.ActeIntervention;
import ma.dentalTech.service.modules.actes.api.ActeInterventionService;
import ma.dentalTech.service.modules.actes.api.ActeService;
import ma.dentalTech.service.modules.actes.impl.ActeInterventionServiceImpl;
import ma.dentalTech.service.modules.actes.impl.ActeServiceImpl;

import java.util.List;

public class ActeModuleServiceTest {

    private static ActeService acteService;
    private static ActeInterventionService acteInterventionService;
    private static int testsReussis = 0;
    private static int testsEchoues = 0;

    public static void main(String[] args) {
        System.out.println("======================================================================");
        System.out.println("TEST FONCTIONNEL - MODULE ACTES (SERVICE)");
        System.out.println("======================================================================");
        System.out.println();

        initialiserServices();

        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 1 : TESTS ACTE SERVICE");
        System.out.println("----------------------------------------------------------------------");
        testerActeService();

        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 2 : TESTS ACTE INTERVENTION SERVICE");
        System.out.println("----------------------------------------------------------------------");
        testerActeInterventionService();

        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 3 : TESTS VALIDATION ET EXCEPTIONS");
        System.out.println("----------------------------------------------------------------------");
        testerValidationEtExceptions();

        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 4 : FLUX COMPLET METIER");
        System.out.println("----------------------------------------------------------------------");
        testerFluxCompletMetier();

        afficherResultats();
    }

    private static void initialiserServices() {
        System.out.println("[INIT] Initialisation des services...");
        acteService = new ActeServiceImpl();
        acteInterventionService = new ActeInterventionServiceImpl();
        System.out.println("[INIT] Initialisation terminee");
        System.out.println();
    }

    private static void testerActeService() {
        Long idActeTest = null;

        System.out.println();
        System.out.println("[SERVICE] Test 1.1 : Creation d'un acte");
        try {
            Acte acte = Acte.builder()
                    .libelle("Extraction dentaire Test")
                    .categorie("CHIRURGIE")
                    .prixDeBase(800.0)
                    .code(9100)
                    .description("Extraction d'une dent")
                    .build();
            Acte resultat = acteService.creerActe(acte);
            idActeTest = resultat.getIdActe();
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
        System.out.println("[SERVICE] Test 1.2 : Obtenir un acte par ID");
        try {
            Acte acte = acteService.obtenirActe(idActeTest);
            if (acte != null && acte.getLibelle().equals("Extraction dentaire Test")) {
                System.out.println("       [OK] Acte recupere : " + acte.getLibelle());
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
        System.out.println("[SERVICE] Test 1.3 : Obtenir acte par code");
        try {
            Acte acte = acteService.obtenirActeParCode(9100);
            if (acte != null) {
                System.out.println("       [OK] Acte trouve par code : " + acte.getLibelle());
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
        System.out.println("[SERVICE] Test 1.4 : Lister tous les actes");
        try {
            List<Acte> actes = acteService.listerTousLesActes();
            System.out.println("       [OK] " + actes.size() + " acte(s) au total");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 1.5 : Lister actes par categorie");
        try {
            List<Acte> actes = acteService.listerActesParCategorie("CHIRURGIE");
            System.out.println("       [OK] " + actes.size() + " acte(s) en CHIRURGIE");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 1.6 : Rechercher actes par libelle");
        try {
            List<Acte> actes = acteService.rechercherActesParLibelle("Test");
            System.out.println("       [OK] " + actes.size() + " acte(s) contenant 'Test'");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 1.7 : Lister actes par fourchette de prix");
        try {
            List<Acte> actes = acteService.listerActesParPrix(500.0, 1000.0);
            System.out.println("       [OK] " + actes.size() + " acte(s) entre 500 et 1000 DH");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 1.8 : Lister toutes les categories");
        try {
            List<String> categories = acteService.listerToutesLesCategories();
            System.out.println("       [OK] " + categories.size() + " categorie(s) : " + String.join(", ", categories));
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 1.9 : Modifier prix d'un acte");
        try {
            Acte acte = acteService.modifierPrix(idActeTest, 900.0);
            if (acte.getPrixDeBase().equals(900.0)) {
                System.out.println("       [OK] Prix modifie : 800.0 -> 900.0 DH");
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] Prix non modifie");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 1.10 : Appliquer remise (20%)");
        try {
            Acte acte = acteService.appliquerRemise(idActeTest, 20.0);
            if (acte.getPrixDeBase().equals(720.0)) {
                System.out.println("       [OK] Remise appliquee : 900.0 -> 720.0 DH (-20%)");
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] Remise non appliquee correctement");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 1.11 : Modifier un acte");
        try {
            Acte acte = acteService.obtenirActe(idActeTest);
            acte.setDescription("Description modifiee");
            Acte resultat = acteService.modifierActe(acte);
            if (resultat.getDescription().equals("Description modifiee")) {
                System.out.println("       [OK] Acte modifie avec succes");
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] Modification non prise en compte");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 1.12 : Verifier existence code");
        try {
            boolean existe = acteService.codeExiste(9100);
            boolean nexistePas = !acteService.codeExiste(99999);
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
        System.out.println("[SERVICE] Test 1.13 : Verifier existence libelle");
        try {
            boolean existe = acteService.libelleExiste("Extraction dentaire Test");
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

    private static void testerActeInterventionService() {
        Long idActeInterventionTest = null;

        System.out.println();
        System.out.println("[SERVICE] Test 2.1 : Ajouter acte a intervention");
        try {
            Acte acte = acteService.obtenirActeParCode(9100);
            if (acte != null) {
                ActeIntervention ai = acteInterventionService.ajouterActeAIntervention(acte.getIdActe(), 1L);
                idActeInterventionTest = ai.getIdActeIntervention();
                if (idActeInterventionTest != null) {
                    System.out.println("       [OK] Acte ajoute a l'intervention avec ID : " + idActeInterventionTest);
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

        System.out.println();
        System.out.println("[SERVICE] Test 2.2 : Lister actes par intervention");
        try {
            List<ActeIntervention> associations = acteInterventionService.listerActesParIntervention(1L);
            System.out.println("       [OK] " + associations.size() + " acte(s) pour intervention 1");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 2.3 : Obtenir details actes intervention");
        try {
            List<Acte> actes = acteInterventionService.obtenirActesDetailsParIntervention(1L);
            System.out.println("       [OK] " + actes.size() + " acte(s) avec details");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 2.4 : Calculer montant total intervention");
        try {
            Double montant = acteInterventionService.calculerMontantTotalIntervention(1L);
            System.out.println("       [OK] Montant total : " + montant + " DH");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 2.5 : Compter utilisations acte");
        try {
            Acte acte = acteService.obtenirActeParCode(9100);
            if (acte != null) {
                int count = acteInterventionService.compterUtilisationsActe(acte.getIdActe());
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
        System.out.println("[SERVICE] Test 2.6 : Verifier acte utilise dans intervention");
        try {
            Acte acte = acteService.obtenirActeParCode(9100);
            if (acte != null) {
                boolean utilise = acteInterventionService.acteEstUtiliseDansIntervention(acte.getIdActe(), 1L);
                if (utilise) {
                    System.out.println("       [OK] Acte detecte dans intervention");
                    testsReussis++;
                } else {
                    System.out.println("       [ECHEC] Acte non detecte");
                    testsEchoues++;
                }
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 2.7 : Lister interventions par acte");
        try {
            Acte acte = acteService.obtenirActeParCode(9100);
            if (acte != null) {
                List<ActeIntervention> interventions = acteInterventionService.listerInterventionsParActe(acte.getIdActe());
                System.out.println("       [OK] " + interventions.size() + " intervention(s) utilisent cet acte");
                testsReussis++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }
    }

    private static void testerValidationEtExceptions() {
        System.out.println();
        System.out.println("[VALIDATION] Test 3.1 : Creation acte avec libelle vide");
        try {
            Acte acte = Acte.builder()
                    .libelle("")
                    .categorie("TEST")
                    .prixDeBase(100.0)
                    .code(9999)
                    .build();
            acteService.creerActe(acte);
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
        System.out.println("[VALIDATION] Test 3.2 : Creation acte avec prix negatif");
        try {
            Acte acte = Acte.builder()
                    .libelle("Test Prix Negatif")
                    .categorie("TEST")
                    .prixDeBase(-100.0)
                    .code(9998)
                    .build();
            acteService.creerActe(acte);
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
        System.out.println("[VALIDATION] Test 3.3 : Creation acte avec code existant");
        try {
            Acte acte = Acte.builder()
                    .libelle("Doublon Code")
                    .categorie("TEST")
                    .prixDeBase(100.0)
                    .code(9100)
                    .build();
            acteService.creerActe(acte);
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
        System.out.println("[VALIDATION] Test 3.4 : Remise avec pourcentage invalide");
        try {
            acteService.appliquerRemise(1L, 150.0);
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
        System.out.println("[VALIDATION] Test 3.5 : Obtenir acte avec ID null");
        try {
            acteService.obtenirActe(null);
            System.out.println("       [ECHEC] Exception attendue non levee");
            testsEchoues++;
        } catch (ServiceException e) {
            System.out.println("       [OK] ServiceException levee : " + e.getMessage());
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] Mauvaise exception : " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[VALIDATION] Test 3.6 : Ajouter acte a intervention avec ID null");
        try {
            acteInterventionService.ajouterActeAIntervention(null, 1L);
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

    private static void testerFluxCompletMetier() {
        System.out.println();
        System.out.println("[FLUX] Scenario : Gestion complete actes et interventions");
        System.out.println();

        try {
            System.out.println("       Etape 1 : Creation de 3 actes pour une consultation");
            Acte acte1 = acteService.creerActe(Acte.builder()
                    .libelle("Consultation Flux Service")
                    .categorie("CONSULTATION")
                    .prixDeBase(200.0)
                    .code(9200)
                    .description("Consultation complete")
                    .build());
            System.out.println("                 - " + acte1.getLibelle() + " : " + acte1.getPrixDeBase() + " DH");

            Acte acte2 = acteService.creerActe(Acte.builder()
                    .libelle("Radio Flux Service")
                    .categorie("RADIOLOGIE")
                    .prixDeBase(300.0)
                    .code(9201)
                    .description("Radio panoramique")
                    .build());
            System.out.println("                 - " + acte2.getLibelle() + " : " + acte2.getPrixDeBase() + " DH");

            Acte acte3 = acteService.creerActe(Acte.builder()
                    .libelle("Plombage Flux Service")
                    .categorie("SOINS")
                    .prixDeBase(500.0)
                    .code(9202)
                    .description("Obturation amalgame")
                    .build());
            System.out.println("                 - " + acte3.getLibelle() + " : " + acte3.getPrixDeBase() + " DH");

            System.out.println();
            System.out.println("       Etape 2 : Association des actes a l'intervention 100");
            acteInterventionService.ajouterActeAIntervention(acte1.getIdActe(), 100L);
            acteInterventionService.ajouterActeAIntervention(acte2.getIdActe(), 100L);
            acteInterventionService.ajouterActeAIntervention(acte3.getIdActe(), 100L);
            System.out.println("                 3 actes associes");

            System.out.println();
            System.out.println("       Etape 3 : Calcul montant initial");
            Double montantInitial = acteInterventionService.calculerMontantTotalIntervention(100L);
            System.out.println("                 Montant initial : " + montantInitial + " DH");

            System.out.println();
            System.out.println("       Etape 4 : Application remise 15% sur acte3");
            acteService.appliquerRemise(acte3.getIdActe(), 15.0);
            System.out.println("                 Remise appliquee : 500.0 -> 425.0 DH");

            System.out.println();
            System.out.println("       Etape 5 : Nouveau calcul montant");
            Double nouveauMontant = acteInterventionService.calculerMontantTotalIntervention(100L);
            System.out.println("                 Nouveau montant : " + nouveauMontant + " DH");
            System.out.println("                 Economie : " + (montantInitial - nouveauMontant) + " DH");

            System.out.println();
            System.out.println("       Etape 6 : Retrait d'un acte");
            List<ActeIntervention> associations = acteInterventionService.listerActesParIntervention(100L);
            if (!associations.isEmpty()) {
                acteInterventionService.retirerActeDeIntervention(associations.get(0).getIdActeIntervention());
                System.out.println("                 Premier acte retire");
            }

            System.out.println();
            System.out.println("       Etape 7 : Montant final");
            Double montantFinal = acteInterventionService.calculerMontantTotalIntervention(100L);
            System.out.println("                 Montant final : " + montantFinal + " DH");

            System.out.println();
            System.out.println("       Etape 8 : Nettoyage - Retrait de tous les actes");
            acteInterventionService.retirerTousLesActesDeIntervention(100L);
            List<ActeIntervention> apresNettoyage = acteInterventionService.listerActesParIntervention(100L);
            System.out.println("                 Actes restants : " + apresNettoyage.size());

            System.out.println();
            System.out.println("       [OK] Flux metier execute avec succes");
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
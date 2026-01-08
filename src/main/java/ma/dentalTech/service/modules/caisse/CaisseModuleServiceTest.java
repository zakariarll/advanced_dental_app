package ma.dentalTech.service.modules.caisse;

import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.caisse.*;
import ma.dentalTech.service.modules.caisse.api.*;
import ma.dentalTech.service.modules.caisse.impl.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class CaisseModuleServiceTest {

    private static FactureService factureService;
    private static SituationFinanciereService situationService;
    private static RevenueService revenueService;
    private static ChargeService chargeService;
    private static CaisseService caisseService;
    private static int testsReussis = 0;
    private static int testsEchoues = 0;

    public static void main(String[] args) {
        System.out.println("======================================================================");
        System.out.println("TEST FONCTIONNEL - MODULE CAISSE (SERVICE)");
        System.out.println("======================================================================");
        System.out.println();

        initialiserServices();

        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 1 : TESTS FACTURE SERVICE");
        System.out.println("----------------------------------------------------------------------");
        testerFactureService();

        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 2 : TESTS SITUATION FINANCIERE SERVICE");
        System.out.println("----------------------------------------------------------------------");
        testerSituationFinanciereService();

        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 3 : TESTS REVENUE SERVICE");
        System.out.println("----------------------------------------------------------------------");
        testerRevenueService();

        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 4 : TESTS CHARGE SERVICE");
        System.out.println("----------------------------------------------------------------------");
        testerChargeService();

        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 5 : TESTS CAISSE SERVICE");
        System.out.println("----------------------------------------------------------------------");
        testerCaisseService();

        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 6 : TESTS VALIDATION ET EXCEPTIONS");
        System.out.println("----------------------------------------------------------------------");
        testerValidationEtExceptions();

        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 7 : FLUX COMPLET FACTURATION");
        System.out.println("----------------------------------------------------------------------");
        testerFluxCompletFacturation();

        afficherResultats();
    }

    private static void initialiserServices() {
        System.out.println("[INIT] Initialisation des services...");
        factureService = new FactureServiceImpl();
        situationService = new SituationFinanciereServiceImpl();
        revenueService = new RevenueServiceImpl();
        chargeService = new ChargeServiceImpl();
        caisseService = new CaisseServiceImpl();
        System.out.println("[INIT] Initialisation terminee");
        System.out.println();
    }

    private static void testerFactureService() {
        Long idFactureTest = null;

        System.out.println();
        System.out.println("[SERVICE] Test 1.1 : Creation d'une facture");
        try {
            Facture facture = Facture.builder()
                    .idConsultation(1L)
                    .totaleFacture(2000.0)
                    .totalePaye(0.0)
                    .reste(2000.0)
                    .statut("EN_ATTENTE")
                    .dateFacture(LocalDateTime.now())
                    .build();
            Facture resultat = factureService.creerFacture(facture);
            idFactureTest = resultat.getIdFacture();
            if (idFactureTest != null) {
                System.out.println("       [OK] Facture creee avec ID : " + idFactureTest);
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] ID non genere");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        if (idFactureTest == null) {
            System.out.println("[ERREUR] Impossible de continuer sans ID de facture valide");
            return;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 1.2 : Obtenir une facture");
        try {
            Facture facture = factureService.obtenirFacture(idFactureTest);
            if (facture != null && facture.getTotaleFacture().equals(2000.0)) {
                System.out.println("       [OK] Facture recuperee : " + facture.getTotaleFacture() + " DH");
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] Facture non trouvee ou incorrecte");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 1.3 : Enregistrer un paiement partiel");
        try {
            Facture facture = factureService.enregistrerPaiement(idFactureTest, 800.0);
            if (facture.getTotalePaye().equals(800.0) && facture.getReste().equals(1200.0)) {
                System.out.println("       [OK] Paiement enregistre : 800 DH, Reste : 1200 DH");
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] Paiement incorrect");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 1.4 : Enregistrer paiement final");
        try {
            Facture facture = factureService.enregistrerPaiement(idFactureTest, 1200.0);
            if (facture.getStatut().equals("PAYEE") && facture.getReste().equals(0.0)) {
                System.out.println("       [OK] Facture soldee : Statut = PAYEE");
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] Statut incorrect");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 1.5 : Lister factures impayees");
        try {
            List<Facture> impayees = factureService.listerFacturesImpayees();
            System.out.println("       [OK] " + impayees.size() + " facture(s) impayee(s)");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 1.6 : Calculer total impaye");
        try {
            Double totalImpaye = factureService.calculerTotalImpaye();
            System.out.println("       [OK] Total impaye : " + totalImpaye + " DH");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 1.7 : Lister factures par periode");
        try {
            LocalDateTime debut = LocalDateTime.now().minusDays(1);
            LocalDateTime fin = LocalDateTime.now().plusDays(1);
            List<Facture> factures = factureService.listerFacturesParPeriode(debut, fin);
            System.out.println("       [OK] " + factures.size() + " facture(s) dans la periode");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }
    }

    private static void testerSituationFinanciereService() {
        Long idSituationTest = null;

        System.out.println();
        System.out.println("[SERVICE] Test 2.1 : Creation situation financiere");
        try {
            SituationFinanciere situation = SituationFinanciere.builder()
                    .idFacture(1L)
                    .totaleDesActes(1500.0)
                    .totalePaye(0.0)
                    .credit(1500.0)
                    .statut("EN_COURS")
                    .build();
            SituationFinanciere resultat = situationService.creerSituationFinanciere(situation);
            idSituationTest = resultat.getIdSF();
            if (idSituationTest != null) {
                System.out.println("       [OK] Situation creee avec ID : " + idSituationTest);
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] ID non genere");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        if (idSituationTest == null) {
            System.out.println("[ERREUR] Impossible de continuer sans ID de situation valide");
            return;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 2.2 : Obtenir situation financiere");
        try {
            SituationFinanciere situation = situationService.obtenirSituationFinanciere(idSituationTest);
            if (situation != null && situation.getCredit().equals(1500.0)) {
                System.out.println("       [OK] Situation recuperee : Credit = " + situation.getCredit() + " DH");
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] Situation non trouvee ou incorrecte");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 2.3 : Appliquer promotion");
        try {
            SituationFinanciere situation = situationService.appliquerPromotion(idSituationTest, "Fidelite", 15.0);
            Double nouveauTotal = 1500.0 * 0.85;
            if (situation.getTotaleDesActes().equals(nouveauTotal)) {
                System.out.println("       [OK] Promotion appliquee : 1500 -> " + nouveauTotal + " DH (-15%)");
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] Promotion incorrecte");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 2.4 : Lister situations avec credit");
        try {
            List<SituationFinanciere> situations = situationService.listerSituationsAvecCredit();
            System.out.println("       [OK] " + situations.size() + " situation(s) avec credit");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 2.5 : Calculer total credits");
        try {
            Double totalCredits = situationService.calculerTotalCredits();
            System.out.println("       [OK] Total credits : " + totalCredits + " DH");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }
    }

    private static void testerRevenueService() {
        Long idRevenueTest = null;

        System.out.println();
        System.out.println("[SERVICE] Test 3.1 : Creation d'un revenue");
        try {
            Revenue revenue = Revenue.builder()
                    .titre("Vente materiel dentaire")
                    .description("Vente brosses a dents electriques")
                    .montant(800.0)
                    .date(LocalDateTime.now())
                    .idCabinet(1L)
                    .build();
            Revenue resultat = revenueService.creerRevenue(revenue);
            idRevenueTest = resultat.getIdRevenue();
            if (idRevenueTest != null) {
                System.out.println("       [OK] Revenue cree avec ID : " + idRevenueTest);
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] ID non genere");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        if (idRevenueTest == null) return;

        System.out.println();
        System.out.println("[SERVICE] Test 3.2 : Obtenir revenue");
        try {
            Revenue revenue = revenueService.obtenirRevenue(idRevenueTest);
            if (revenue != null && revenue.getMontant().equals(800.0)) {
                System.out.println("       [OK] Revenue recupere : " + revenue.getTitre());
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] Revenue non trouve ou incorrect");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 3.3 : Calculer total revenues cabinet");
        try {
            Double total = revenueService.calculerTotalRevenues(1L);
            System.out.println("       [OK] Total revenues cabinet 1 : " + total + " DH");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 3.4 : Rechercher revenues par titre");
        try {
            List<Revenue> revenues = revenueService.rechercherRevenuesParTitre("Vente");
            System.out.println("       [OK] " + revenues.size() + " revenue(s) contenant 'Vente'");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }
    }

    private static void testerChargeService() {
        Long idChargeTest = null;

        System.out.println();
        System.out.println("[SERVICE] Test 4.1 : Creation d'une charge");
        try {
            Charge charge = Charge.builder()
                    .titre("Salaires personnel")
                    .description("Salaires mois en cours")
                    .montant(15000.0)
                    .date(LocalDateTime.now())
                    .idCabinet(1L)
                    .build();
            Charge resultat = chargeService.creerCharge(charge);
            idChargeTest = resultat.getIdCharge();
            if (idChargeTest != null) {
                System.out.println("       [OK] Charge creee avec ID : " + idChargeTest);
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] ID non genere");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        if (idChargeTest == null) return;

        System.out.println();
        System.out.println("[SERVICE] Test 4.2 : Obtenir charge");
        try {
            Charge charge = chargeService.obtenirCharge(idChargeTest);
            if (charge != null && charge.getMontant().equals(15000.0)) {
                System.out.println("       [OK] Charge recuperee : " + charge.getTitre());
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] Charge non trouvee ou incorrecte");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 4.3 : Calculer total charges cabinet");
        try {
            Double total = chargeService.calculerTotalCharges(1L);
            System.out.println("       [OK] Total charges cabinet 1 : " + total + " DH");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 4.4 : Rechercher charges par titre");
        try {
            List<Charge> charges = chargeService.rechercherChargesParTitre("Salaires");
            System.out.println("       [OK] " + charges.size() + " charge(s) contenant 'Salaires'");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }
    }

    private static void testerCaisseService() {
        LocalDateTime debut = LocalDateTime.now().minusMonths(1);
        LocalDateTime fin = LocalDateTime.now();

        System.out.println();
        System.out.println("[SERVICE] Test 5.1 : Calculer chiffre d'affaires");
        try {
            Double ca = caisseService.calculerChiffreAffaires(1L, debut, fin);
            System.out.println("       [OK] Chiffre d'affaires : " + ca + " DH");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 5.2 : Calculer benefice net");
        try {
            Double benefice = caisseService.calculerBeneficeNet(1L, debut, fin);
            System.out.println("       [OK] Benefice net : " + benefice + " DH");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 5.3 : Obtenir statistiques journalieres");
        try {
            Map<String, Double> stats = caisseService.obtenirStatistiquesJournalieres(1L, LocalDate.now());
            System.out.println("       [OK] Statistiques journalieres : " + stats.size() + " indicateurs");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 5.4 : Obtenir statistiques mensuelles");
        try {
            Map<String, Double> stats = caisseService.obtenirStatistiquesMensuelles(1L, LocalDate.now().getMonthValue(), LocalDate.now().getYear());
            System.out.println("       [OK] Statistiques mensuelles : " + stats.size() + " indicateurs");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 5.5 : Calculer taux de recouvrement");
        try {
            Double taux = caisseService.calculerTauxRecouvrement(debut, fin);
            System.out.println("       [OK] Taux de recouvrement : " + String.format("%.2f", taux) + "%");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 5.6 : Generer rapport financier");
        try {
            List<Statistique> rapport = caisseService.genererRapportFinancier(1L, debut, fin);
            System.out.println("       [OK] Rapport genere avec " + rapport.size() + " statistique(s)");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[SERVICE] Test 5.7 : Obtenir tableau de bord");
        try {
            Map<String, Object> dashboard = caisseService.obtenirTableauDeBord(1L);
            System.out.println("       [OK] Tableau de bord avec " + dashboard.size() + " section(s)");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }
    }

    private static void testerValidationEtExceptions() {
        System.out.println();
        System.out.println("[VALIDATION] Test 6.1 : Creation facture avec montant negatif");
        try {
            Facture facture = Facture.builder()
                    .idConsultation(1L)
                    .totaleFacture(-100.0)
                    .totalePaye(0.0)
                    .build();
            factureService.creerFacture(facture);
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
        System.out.println("[VALIDATION] Test 6.2 : Enregistrer paiement superieur au reste");
        try {
            factureService.enregistrerPaiement(1L, 999999.0);
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
        System.out.println("[VALIDATION] Test 6.3 : Creation charge avec titre vide");
        try {
            Charge charge = Charge.builder()
                    .titre("")
                    .montant(100.0)
                    .idCabinet(1L)
                    .build();
            chargeService.creerCharge(charge);
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
        System.out.println("[VALIDATION] Test 6.4 : Appliquer promotion avec pourcentage invalide");
        try {
            situationService.appliquerPromotion(1L, "Test", 150.0);
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

    private static void testerFluxCompletFacturation() {
        System.out.println();
        System.out.println("[FLUX] Scenario : Cycle complet de facturation");
        System.out.println();

        try {
            System.out.println("       Etape 1 : Creation facture consultation");
            Facture facture = Facture.builder()
                    .idConsultation(1L)
                    .totaleFacture(3500.0)
                    .totalePaye(0.0)
                    .statut("EN_ATTENTE")
                    .dateFacture(LocalDateTime.now())
                    .build();
            facture = factureService.creerFacture(facture);
            System.out.println("                 Facture ID : " + facture.getIdFacture() + " - Montant : 3500 DH");

            System.out.println();
            System.out.println("       Etape 2 : Creation situation financiere");
            SituationFinanciere situation = SituationFinanciere.builder()
                    .idFacture(facture.getIdFacture())
                    .totaleDesActes(3500.0)
                    .totalePaye(0.0)
                    .credit(3500.0)
                    .statut("EN_COURS")
                    .build();
            situation = situationService.creerSituationFinanciere(situation);
            System.out.println("                 Situation ID : " + situation.getIdSF() + " - Credit : 3500 DH");

            System.out.println();
            System.out.println("       Etape 3 : Application promotion fidelite 10%");
            situation = situationService.appliquerPromotion(situation.getIdSF(), "Fidelite", 10.0);
            System.out.println("                 Nouveau total : " + situation.getTotaleDesActes() + " DH");

            System.out.println();
            System.out.println("       Etape 4 : Premier paiement 1500 DH");
            facture = factureService.enregistrerPaiement(facture.getIdFacture(), 1500.0);
            System.out.println("                 Paye : 1500 DH - Reste : " + facture.getReste() + " DH");

            System.out.println();
            System.out.println("       Etape 5 : Deuxieme paiement 1000 DH");
            facture = factureService.enregistrerPaiement(facture.getIdFacture(), 1000.0);
            System.out.println("                 Paye : 2500 DH - Reste : " + facture.getReste() + " DH");

            System.out.println();
            System.out.println("       Etape 6 : Calcul statistiques");
            LocalDateTime debut = LocalDateTime.now().minusDays(1);
            LocalDateTime fin = LocalDateTime.now().plusDays(1);
            Double ca = caisseService.calculerChiffreAffaires(1L, debut, fin);
            Double benefice = caisseService.calculerBeneficeNet(1L, debut, fin);
            Double taux = caisseService.calculerTauxRecouvrement(debut, fin);
            System.out.println("                 CA : " + ca + " DH");
            System.out.println("                 Benefice : " + benefice + " DH");
            System.out.println("                 Taux recouvrement : " + String.format("%.2f", taux) + "%");

            System.out.println();
            System.out.println("       [OK] Flux de facturation execute avec succes");
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
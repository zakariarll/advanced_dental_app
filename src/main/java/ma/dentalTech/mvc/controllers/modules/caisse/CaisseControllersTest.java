package ma.dentalTech.mvc.controllers.modules.caisse;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.mvc.controllers.modules.caisse.api.*;
import ma.dentalTech.mvc.controllers.modules.caisse.swing_implementation.*;
import ma.dentalTech.mvc.dto.caisse.*;
import ma.dentalTech.service.modules.caisse.api.*;
import ma.dentalTech.service.modules.caisse.impl.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

public class CaisseControllersTest {

    private static ChargeService chargeService;
    private static FactureService factureService;
    private static RevenueService revenueService;
    private static SituationFinanciereService situationFinanciereService;
    private static CaisseService caisseService;

    private static ChargeController chargeController;
    private static FactureController factureController;
    private static RevenueController revenueController;
    private static SituationFinanciereController situationFinanciereController;
    private static CaisseController caisseController;

    private static int testsReussis = 0;
    private static int testsEchoues = 0;
    private static Random random = new Random();

    public static void main(String[] args) {
        System.out.println("======================================================================");
        System.out.println("TEST FONCTIONNEL - MODULE CAISSE (CONTROLLERS)");
        System.out.println("======================================================================");
        System.out.println();
        initialiserControllers();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 1 : TESTS CHARGE CONTROLLER");
        System.out.println("----------------------------------------------------------------------");
        testerChargeController();
        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 2 : TESTS REVENUE CONTROLLER");
        System.out.println("----------------------------------------------------------------------");
        testerRevenueController();
        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 3 : TESTS FACTURE CONTROLLER");
        System.out.println("----------------------------------------------------------------------");
        testerFactureController();
        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 4 : TESTS SITUATION FINANCIERE CONTROLLER");
        System.out.println("----------------------------------------------------------------------");
        testerSituationFinanciereController();
        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 5 : TESTS CAISSE CONTROLLER (TABLEAU DE BORD)");
        System.out.println("----------------------------------------------------------------------");
        testerCaisseController();
        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 6 : FLUX COMPLET GESTION FINANCIERE");
        System.out.println("----------------------------------------------------------------------");
        testerFluxGestionFinanciere();
        afficherResultats();
    }

    private static void initialiserControllers() {
        System.out.println("[INIT] Initialisation des services et controllers...");
        chargeService = new ChargeServiceImpl();
        factureService = new FactureServiceImpl();
        revenueService = new RevenueServiceImpl();
        situationFinanciereService = new SituationFinanciereServiceImpl();
        caisseService = new CaisseServiceImpl();

        chargeController = new ChargeControllerImpl(chargeService);
        factureController = new FactureControllerImpl(factureService);
        revenueController = new RevenueControllerImpl(revenueService);
        situationFinanciereController = new SituationFinanciereControllerImpl(situationFinanciereService);
        caisseController = new CaisseControllerImpl(caisseService);

        System.out.println("[INIT] Initialisation terminee");
        System.out.println();
    }

    private static void testerChargeController() {
        System.out.println();
        System.out.println("[CTRL] Test 1.1 : Afficher toutes les charges");
        try {
            chargeController.afficherToutesLesCharges();
            System.out.println("       [OK] Affichage des charges execute");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 1.2 : Creation d'une charge");
        try {
            ChargeDTO chargeDTO = ChargeDTO.builder()
                    .titre("Charge Test " + genererCodeUnique())
                    .description("Achat materiel dentaire")
                    .montant(1500.0)
                    .date(LocalDateTime.now())
                    .idCabinet(1L)
                    .build();
            chargeController.creerCharge(chargeDTO);
            System.out.println("       [OK] Charge creee avec succes");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 1.3 : Afficher charges par cabinet");
        try {
            chargeController.afficherChargesParCabinet(1L);
            System.out.println("       [OK] Charges du cabinet affichees");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 1.4 : Recherche charges par titre");
        try {
            chargeController.rechercherChargesParTitre("Materiel");
            System.out.println("       [OK] Recherche executee");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 1.5 : Afficher total des charges");
        try {
            chargeController.afficherTotalCharges(1L);
            System.out.println("       [OK] Total des charges affiche");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 1.6 : Afficher charges par periode");
        try {
            LocalDateTime debut = LocalDateTime.now().minusMonths(1);
            LocalDateTime fin = LocalDateTime.now();
            chargeController.afficherChargesParPeriode(debut, fin);
            System.out.println("       [OK] Charges de la periode affichees");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 1.7 : Modification d'une charge");
        try {
            ChargeDTO chargeDTO = ChargeDTO.builder()
                    .idCharge(1L)
                    .titre("Charge Modifiee " + genererCodeUnique())
                    .description("Description modifiee")
                    .montant(1800.0)
                    .date(LocalDateTime.now())
                    .idCabinet(1L)
                    .build();
            chargeController.modifierCharge(chargeDTO);
            System.out.println("       [OK] Charge modifiee avec succes");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 1.8 : Total charges par periode");
        try {
            LocalDateTime debut = LocalDateTime.now().minusMonths(1);
            LocalDateTime fin = LocalDateTime.now();
            chargeController.afficherTotalChargesParPeriode(1L, debut, fin);
            System.out.println("       [OK] Total charges periode affiche");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }
    }

    private static void testerRevenueController() {
        System.out.println();
        System.out.println("[CTRL] Test 2.1 : Afficher tous les revenus");
        try {
            revenueController.afficherTousLesRevenues();
            System.out.println("       [OK] Affichage des revenus execute");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 2.2 : Creation d'un revenu");
        try {
            RevenueDTO revenueDTO = RevenueDTO.builder()
                    .titre("Revenue Test " + genererCodeUnique())
                    .description("Paiement consultation")
                    .montant(800.0)
                    .date(LocalDateTime.now())
                    .idCabinet(1L)
                    .build();
            revenueController.creerRevenue(revenueDTO);
            System.out.println("       [OK] Revenu cree avec succes");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 2.3 : Afficher revenus par cabinet");
        try {
            revenueController.afficherRevenuesParCabinet(1L);
            System.out.println("       [OK] Revenus du cabinet affiches");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 2.4 : Recherche revenus par titre");
        try {
            revenueController.rechercherRevenuesParTitre("Consultation");
            System.out.println("       [OK] Recherche executee");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 2.5 : Afficher total des revenus");
        try {
            revenueController.afficherTotalRevenues(1L);
            System.out.println("       [OK] Total des revenus affiche");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 2.6 : Afficher revenus par periode");
        try {
            LocalDateTime debut = LocalDateTime.now().minusMonths(1);
            LocalDateTime fin = LocalDateTime.now();
            revenueController.afficherRevenuesParPeriode(debut, fin);
            System.out.println("       [OK] Revenus de la periode affiches");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }
    }

    private static void testerFactureController() {
        System.out.println();
        System.out.println("[CTRL] Test 3.1 : Afficher toutes les factures");
        try {
            factureController.afficherToutesLesFactures();
            System.out.println("       [OK] Affichage des factures execute");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 3.2 : Creation d'une facture");
        try {
            FactureDTO factureDTO = FactureDTO.builder()
                    .totaleFacture(1200.0)
                    .totalePaye(600.0)
                    .reste(600.0)
                    .statut("PARTIELLEMENT_PAYEE")
                    .dateFacture(LocalDateTime.now())
                    .idConsultation(1L)
                    .build();
            factureController.creerFacture(factureDTO);
            System.out.println("       [OK] Facture creee avec succes");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 3.3 : Afficher factures par statut");
        try {
            factureController.afficherFacturesParStatut("IMPAYEE");
            System.out.println("       [OK] Factures impayees affichees");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 3.4 : Afficher factures impayees");
        try {
            factureController.afficherFacturesImpayees();
            System.out.println("       [OK] Factures impayees affichees");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 3.5 : Enregistrer un paiement");
        try {
            factureController.enregistrerPaiement(1L, 300.0);
            System.out.println("       [OK] Paiement enregistre");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 3.6 : Afficher facture par consultation");
        try {
            factureController.afficherFactureParConsultation(1L);
            System.out.println("       [OK] Facture de la consultation affichee");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 3.7 : Afficher total factures");
        try {
            LocalDateTime debut = LocalDateTime.now().minusMonths(1);
            LocalDateTime fin = LocalDateTime.now();
            factureController.afficherTotalFactures(debut, fin);
            System.out.println("       [OK] Total factures affiche");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 3.8 : Afficher total paye");
        try {
            LocalDateTime debut = LocalDateTime.now().minusMonths(1);
            LocalDateTime fin = LocalDateTime.now();
            factureController.afficherTotalPaye(debut, fin);
            System.out.println("       [OK] Total paye affiche");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 3.9 : Afficher total impaye");
        try {
            factureController.afficherTotalImpaye();
            System.out.println("       [OK] Total impaye affiche");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 3.10 : Generer facture depuis consultation");
        try {
            factureController.genererFactureDepuisConsultation(2L);
            System.out.println("       [OK] Facture generee depuis consultation");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }
    }

    private static void testerSituationFinanciereController() {
        System.out.println();
        System.out.println("[CTRL] Test 4.1 : Afficher toutes les situations");
        try {
            situationFinanciereController.afficherToutesLesSituations();
            System.out.println("       [OK] Affichage des situations execute");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 4.2 : Creation d'une situation");
        try {
            SituationFinanciereDTO situationDTO = SituationFinanciereDTO.builder()
                    .totaleDesActes(1500.0)
                    .totalePaye(1000.0)
                    .credit(500.0)
                    .statut("EN_CREDIT")
                    .enPromo("NON")
                    .idFacture(1L)
                    .build();
            situationFinanciereController.creerSituationFinanciere(situationDTO);
            System.out.println("       [OK] Situation creee avec succes");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 4.3 : Afficher situations avec credit");
        try {
            situationFinanciereController.afficherSituationsAvecCredit();
            System.out.println("       [OK] Situations avec credit affichees");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 4.4 : Afficher total credits");
        try {
            situationFinanciereController.afficherTotalCredits();
            System.out.println("       [OK] Total credits affiche");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 4.5 : Appliquer une promotion");
        try {
            situationFinanciereController.appliquerPromotion(1L, "PROMO_ETE", 10.0);
            System.out.println("       [OK] Promotion appliquee");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 4.6 : Afficher situation par facture");
        try {
            situationFinanciereController.afficherSituationParFacture(1L);
            System.out.println("       [OK] Situation de la facture affichee");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 4.7 : Mettre a jour depuis facture");
        try {
            situationFinanciereController.mettreAJourDepuisFacture(1L);
            System.out.println("       [OK] Situation mise a jour");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }
    }

    private static void testerCaisseController() {
        System.out.println();
        System.out.println("[CTRL] Test 5.1 : Afficher tableau de bord");
        try {
            caisseController.afficherTableauDeBord(1L);
            System.out.println("       [OK] Tableau de bord affiche");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 5.2 : Afficher chiffre d'affaires");
        try {
            LocalDateTime debut = LocalDateTime.now().minusMonths(1);
            LocalDateTime fin = LocalDateTime.now();
            caisseController.afficherChiffreAffaires(1L, debut, fin);
            System.out.println("       [OK] Chiffre d'affaires affiche");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 5.3 : Afficher benefice net");
        try {
            LocalDateTime debut = LocalDateTime.now().minusMonths(1);
            LocalDateTime fin = LocalDateTime.now();
            caisseController.afficherBeneficeNet(1L, debut, fin);
            System.out.println("       [OK] Benefice net affiche");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 5.4 : Afficher statistiques journalieres");
        try {
            caisseController.afficherStatistiquesJournalieres(1L, LocalDate.now());
            System.out.println("       [OK] Statistiques journalieres affichees");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 5.5 : Afficher statistiques mensuelles");
        try {
            caisseController.afficherStatistiquesMensuelles(1L, 12, 2024);
            System.out.println("       [OK] Statistiques mensuelles affichees");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 5.6 : Afficher statistiques annuelles");
        try {
            caisseController.afficherStatistiquesAnnuelles(1L, 2024);
            System.out.println("       [OK] Statistiques annuelles affichees");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 5.7 : Generer rapport financier");
        try {
            LocalDateTime debut = LocalDateTime.now().minusMonths(1);
            LocalDateTime fin = LocalDateTime.now();
            caisseController.genererRapportFinancier(1L, debut, fin);
            System.out.println("       [OK] Rapport financier genere");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 5.8 : Afficher taux de recouvrement");
        try {
            LocalDateTime debut = LocalDateTime.now().minusMonths(1);
            LocalDateTime fin = LocalDateTime.now();
            caisseController.afficherTauxRecouvrement(debut, fin);
            System.out.println("       [OK] Taux de recouvrement affiche");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 5.9 : Afficher repartition revenus");
        try {
            caisseController.afficherRepartitionRevenusParMois(1L, 2024);
            System.out.println("       [OK] Repartition revenus affichee");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }

        System.out.println();
        System.out.println("[CTRL] Test 5.10 : Afficher repartition charges");
        try {
            caisseController.afficherRepartitionChargesParMois(1L, 2024);
            System.out.println("       [OK] Repartition charges affichee");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }
    }

    private static void testerFluxGestionFinanciere() {
        System.out.println();
        System.out.println("[FLUX] Scenario : Gestion complete financiere");
        System.out.println();
        try {
            int codeBase = genererCodeUnique();

            System.out.println("       Etape 1 : Enregistrement d'une charge");
            ChargeDTO charge = ChargeDTO.builder()
                    .titre("Achat equipement " + codeBase)
                    .description("Nouveau fauteuil dentaire")
                    .montant(25000.0)
                    .date(LocalDateTime.now())
                    .idCabinet(1L)
                    .build();
            chargeController.creerCharge(charge);
            System.out.println("                 Charge de 25000 MAD enregistree");

            System.out.println();
            System.out.println("       Etape 2 : Enregistrement d'un revenu");
            RevenueDTO revenue = RevenueDTO.builder()
                    .titre("Consultations journee " + codeBase)
                    .description("Revenus des consultations")
                    .montant(8500.0)
                    .date(LocalDateTime.now())
                    .idCabinet(1L)
                    .build();
            revenueController.creerRevenue(revenue);
            System.out.println("                 Revenu de 8500 MAD enregistre");

            System.out.println();
            System.out.println("       Etape 3 : Creation d'une facture");
            FactureDTO facture = FactureDTO.builder()
                    .totaleFacture(3200.0)
                    .totalePaye(1500.0)
                    .reste(1700.0)
                    .statut("PARTIELLEMENT_PAYEE")
                    .dateFacture(LocalDateTime.now())
                    .idConsultation(1L)
                    .build();
            factureController.creerFacture(facture);
            System.out.println("                 Facture de 3200 MAD creee");

            System.out.println();
            System.out.println("       Etape 4 : Enregistrement paiement partiel");
            factureController.enregistrerPaiement(1L, 1000.0);
            System.out.println("                 Paiement de 1000 MAD enregistre");

            System.out.println();
            System.out.println("       Etape 5 : Creation situation financiere");
            SituationFinanciereDTO situation = SituationFinanciereDTO.builder()
                    .totaleDesActes(3200.0)
                    .totalePaye(2500.0)
                    .credit(700.0)
                    .statut("EN_CREDIT")
                    .enPromo("NON")
                    .idFacture(1L)
                    .build();
            situationFinanciereController.creerSituationFinanciere(situation);
            System.out.println("                 Situation financiere creee");

            System.out.println();
            System.out.println("       Etape 6 : Application promotion 15%");
            situationFinanciereController.appliquerPromotion(1L, "PROMO_NOEL", 15.0);
            System.out.println("                 Promotion de 15% appliquee");

            System.out.println();
            System.out.println("       Etape 7 : Consultation factures impayees");
            factureController.afficherFacturesImpayees();
            System.out.println("                 Liste des factures impayees consultee");

            System.out.println();
            System.out.println("       Etape 8 : Consultation total credits");
            situationFinanciereController.afficherTotalCredits();
            System.out.println("                 Total des credits consulte");

            System.out.println();
            System.out.println("       Etape 9 : Consultation statistiques mensuelles");
            caisseController.afficherStatistiquesMensuelles(1L, 12, 2024);
            System.out.println("                 Statistiques mensuelles consultees");

            System.out.println();
            System.out.println("       Etape 10 : Affichage tableau de bord complet");
            caisseController.afficherTableauDeBord(1L);
            System.out.println("                 Tableau de bord affiche");

            System.out.println();
            System.out.println("       Etape 11 : Calcul chiffre d'affaires");
            LocalDateTime debut = LocalDateTime.now().minusMonths(1);
            LocalDateTime fin = LocalDateTime.now();
            caisseController.afficherChiffreAffaires(1L, debut, fin);
            System.out.println("                 Chiffre d'affaires calcule");

            System.out.println();
            System.out.println("       Etape 12 : Calcul benefice net");
            caisseController.afficherBeneficeNet(1L, debut, fin);
            System.out.println("                 Benefice net calcule");

            System.out.println();
            System.out.println("       Etape 13 : Calcul taux recouvrement");
            caisseController.afficherTauxRecouvrement(debut, fin);
            System.out.println("                 Taux de recouvrement calcule");

            System.out.println();
            System.out.println("       Etape 14 : Generation rapport financier");
            caisseController.genererRapportFinancier(1L, debut, fin);
            System.out.println("                 Rapport financier genere");

            System.out.println();
            System.out.println("       Etape 15 : Consultation repartition annuelle");
            caisseController.afficherRepartitionRevenusParMois(1L, 2024);
            caisseController.afficherRepartitionChargesParMois(1L, 2024);
            System.out.println("                 Repartitions annuelles consultees");

            System.out.println();
            System.out.println("       [OK] Flux gestion financiere execute avec succes");
            testsReussis++;
        } catch (Exception e) {
            gererException(e);
        }
    }

    // Methode utilitaire pour gerer les exceptions et garder le code propre
    private static void gererException(Exception e) {
        if (e instanceof ServiceException || e instanceof ValidationException) {
            System.out.println("       [ERREUR METIER] " + e.getMessage());
        } else {
            System.out.println("       [ECHEC] Exception inattendue: " + e.getMessage());
        }
        testsEchoues++;
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
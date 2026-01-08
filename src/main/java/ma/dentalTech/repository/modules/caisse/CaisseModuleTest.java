package ma.dentalTech.repository.modules.caisse;

import ma.dentalTech.entities.caisse.Facture;
import ma.dentalTech.entities.caisse.SituationFinanciere;
import ma.dentalTech.entities.caisse.Revenue;
import ma.dentalTech.entities.caisse.Charge;
import ma.dentalTech.repository.modules.caisse.api.FactureRepository;
import ma.dentalTech.repository.modules.caisse.api.SituationFinanciereRepository;
import ma.dentalTech.repository.modules.caisse.api.RevenueRepository;
import ma.dentalTech.repository.modules.caisse.api.ChargeRepository;
import ma.dentalTech.repository.modules.caisse.impl.mySQL.FactureRepositoryImpl;
import ma.dentalTech.repository.modules.caisse.impl.mySQL.SituationFinanciereRepositoryImpl;
import ma.dentalTech.repository.modules.caisse.impl.mySQL.RevenueRepositoryImpl;
import ma.dentalTech.repository.modules.caisse.impl.mySQL.ChargeRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class CaisseModuleTest {

    private static FactureRepository factureRepository;
    private static SituationFinanciereRepository situationRepository;
    private static RevenueRepository revenueRepository;
    private static ChargeRepository chargeRepository;
    private static int testsReussis = 0;
    private static int testsEchoues = 0;

    public static void main(String[] args) {
        System.out.println("======================================================================");
        System.out.println("TEST FONCTIONNEL - MODULE CAISSE (REPOSITORY)");
        System.out.println("======================================================================");
        System.out.println();

        initialiserRepositories();

        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 1 : TESTS FACTURE REPOSITORY");
        System.out.println("----------------------------------------------------------------------");
        testerFactureRepository();

        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 2 : TESTS SITUATION FINANCIERE REPOSITORY");
        System.out.println("----------------------------------------------------------------------");
        testerSituationFinanciereRepository();

        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 3 : TESTS REVENUE REPOSITORY");
        System.out.println("----------------------------------------------------------------------");
        testerRevenueRepository();

        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 4 : TESTS CHARGE REPOSITORY");
        System.out.println("----------------------------------------------------------------------");
        testerChargeRepository();

        System.out.println();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ETAPE 5 : FLUX COMPLET FACTURATION");
        System.out.println("----------------------------------------------------------------------");
        testerFluxFacturation();

        afficherResultats();
    }

    private static void initialiserRepositories() {
        System.out.println("[INIT] Initialisation des repositories...");
        factureRepository = new FactureRepositoryImpl();
        situationRepository = new SituationFinanciereRepositoryImpl();
        revenueRepository = new RevenueRepositoryImpl();
        chargeRepository = new ChargeRepositoryImpl();
        System.out.println("[INIT] Initialisation terminee");
        System.out.println();
    }

    private static void testerFactureRepository() {
        Long idFactureTest = null;

        System.out.println();
        System.out.println("[REPO] Test 1.1 : Creation d'une facture");
        try {
            Facture facture = Facture.builder()
                    .idConsultation(1001L)
                    .totaleFacture(1500.0)
                    .totalePaye(0.0)
                    .reste(1500.0)
                    .statut("EN_ATTENTE")
                    .dateFacture(LocalDateTime.now())
                    .build();
            factureRepository.create(facture);
            idFactureTest = facture.getIdFacture();
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
        System.out.println("[REPO] Test 1.2 : Lecture par ID (findById)");
        try {
            Facture facture = factureRepository.findById(idFactureTest);
            if (facture != null && facture.getTotaleFacture().equals(1500.0)) {
                System.out.println("       [OK] Facture trouvee : " + facture.getTotaleFacture() + " DH");
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
        System.out.println("[REPO] Test 1.3 : Recherche par consultation (findByConsultationId)");
        try {
            Optional<Facture> optionalFacture = factureRepository.findByConsultationId(1001L);
            if (optionalFacture.isPresent()) {
                System.out.println("       [OK] Facture trouvee pour consultation 1001");
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] Facture non trouvee");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 1.4 : Recherche par statut (findByStatut)");
        try {
            List<Facture> factures = factureRepository.findByStatut("EN_ATTENTE");
            System.out.println("       [OK] " + factures.size() + " facture(s) EN_ATTENTE");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 1.5 : Modification facture - paiement partiel (update)");
        try {
            Facture facture = factureRepository.findById(idFactureTest);
            if (facture != null) {
                facture.setTotalePaye(500.0);
                facture.setReste(1000.0);
                facture.setStatut("PARTIELLEMENT_PAYEE");
                factureRepository.update(facture);
                Facture updated = factureRepository.findById(idFactureTest);
                if (updated != null && updated.getTotalePaye().equals(500.0)) {
                    System.out.println("       [OK] Paiement enregistre : 500.0 DH");
                    testsReussis++;
                } else {
                    System.out.println("       [ECHEC] Paiement non enregistre");
                    testsEchoues++;
                }
            } else {
                System.out.println("       [ECHEC] Facture non trouvee");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 1.6 : Liste factures impayees (findFacturesImpayees)");
        try {
            List<Facture> impayees = factureRepository.findFacturesImpayees();
            System.out.println("       [OK] " + impayees.size() + " facture(s) impayee(s)");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 1.7 : Calcul total impaye (calculateTotalImpaye)");
        try {
            Double totalImpaye = factureRepository.calculateTotalImpaye();
            System.out.println("       [OK] Total impaye : " + totalImpaye + " DH");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 1.8 : Liste toutes les factures (findAll)");
        try {
            List<Facture> factures = factureRepository.findAll();
            System.out.println("       [OK] " + factures.size() + " facture(s) au total");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 1.9 : Recherche par periode (findByDateBetween)");
        try {
            LocalDateTime debut = LocalDateTime.now().minusDays(1);
            LocalDateTime fin = LocalDateTime.now().plusDays(1);
            List<Facture> factures = factureRepository.findByDateBetween(debut, fin);
            System.out.println("       [OK] " + factures.size() + " facture(s) dans la periode");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }
    }

    private static void testerSituationFinanciereRepository() {
        Long idSituationTest = null;

        System.out.println();
        System.out.println("[REPO] Test 2.1 : Creation d'une situation financiere");
        try {
            SituationFinanciere situation = SituationFinanciere.builder()
                    .idFacture(1L)
                    .totaleDesActes(1000.0)
                    .totalePaye(0.0)
                    .credit(1000.0)
                    .statut("EN_COURS")
                    .build();
            situationRepository.create(situation);
            idSituationTest = situation.getIdSF();
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
        System.out.println("[REPO] Test 2.2 : Lecture par ID (findById)");
        try {
            SituationFinanciere situation = situationRepository.findById(idSituationTest);
            if (situation != null && situation.getCredit().equals(1000.0)) {
                System.out.println("       [OK] Situation trouvee : Credit = " + situation.getCredit() + " DH");
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
        System.out.println("[REPO] Test 2.3 : Recherche par facture (findByFactureId)");
        try {
            Optional<SituationFinanciere> optionalSituation = situationRepository.findByFactureId(1L);
            if (optionalSituation.isPresent()) {
                System.out.println("       [OK] Situation trouvee pour facture 1");
                testsReussis++;
            } else {
                System.out.println("       [ECHEC] Situation non trouvee");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 2.4 : Modification situation (update)");
        try {
            SituationFinanciere situation = situationRepository.findById(idSituationTest);
            if (situation != null) {
                situation.setTotalePaye(300.0);
                situation.setCredit(700.0);
                situation.setEnPromo("Fidelite -10%");
                situationRepository.update(situation);
                SituationFinanciere updated = situationRepository.findById(idSituationTest);
                if (updated != null && updated.getTotalePaye().equals(300.0)) {
                    System.out.println("       [OK] Situation mise a jour : Paye = 300.0 DH");
                    testsReussis++;
                } else {
                    System.out.println("       [ECHEC] Mise a jour echouee");
                    testsEchoues++;
                }
            } else {
                System.out.println("       [ECHEC] Situation non trouvee");
                testsEchoues++;
            }
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 2.5 : Liste par statut (findByStatut)");
        try {
            List<SituationFinanciere> situations = situationRepository.findByStatut("EN_COURS");
            System.out.println("       [OK] " + situations.size() + " situation(s) EN_COURS");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 2.6 : Liste situations avec credit (findWithCredit)");
        try {
            List<SituationFinanciere> situations = situationRepository.findWithCredit();
            System.out.println("       [OK] " + situations.size() + " situation(s) avec credit");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 2.7 : Liste toutes les situations (findAll)");
        try {
            List<SituationFinanciere> situations = situationRepository.findAll();
            System.out.println("       [OK] " + situations.size() + " situation(s) au total");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }
    }

    private static void testerRevenueRepository() {
        Long idRevenueTest = null;

        System.out.println();
        System.out.println("[REPO] Test 3.1 : Creation d'un revenue");
        try {
            Revenue revenue = Revenue.builder()
                    .titre("Vente produits")
                    .description("Vente de brosses a dents")
                    .montant(500.0)
                    .date(LocalDateTime.now())
                    .idCabinet(1L)
                    .build();
            revenueRepository.create(revenue);
            idRevenueTest = revenue.getIdRevenue();
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

        if (idRevenueTest == null) {
            System.out.println("[ERREUR] Impossible de continuer sans ID de revenue valide");
            return;
        }

        System.out.println();
        System.out.println("[REPO] Test 3.2 : Lecture par ID (findById)");
        try {
            Revenue revenue = revenueRepository.findById(idRevenueTest);
            if (revenue != null && revenue.getTitre().equals("Vente produits")) {
                System.out.println("       [OK] Revenue trouve : " + revenue.getTitre());
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
        System.out.println("[REPO] Test 3.3 : Recherche par cabinet (findByCabinetId)");
        try {
            List<Revenue> revenues = revenueRepository.findByCabinetId(1L);
            System.out.println("       [OK] " + revenues.size() + " revenue(s) pour cabinet 1");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 3.4 : Calcul total revenues (calculateTotalRevenues)");
        try {
            Double total = revenueRepository.calculateTotalRevenues(1L);
            System.out.println("       [OK] Total revenues cabinet 1 : " + total + " DH");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 3.5 : Recherche par titre (findByTitreContaining)");
        try {
            List<Revenue> revenues = revenueRepository.findByTitreContaining("Vente");
            System.out.println("       [OK] " + revenues.size() + " revenue(s) contenant 'Vente'");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 3.6 : Liste tous les revenues (findAll)");
        try {
            List<Revenue> revenues = revenueRepository.findAll();
            System.out.println("       [OK] " + revenues.size() + " revenue(s) au total");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }
    }

    private static void testerChargeRepository() {
        Long idChargeTest = null;

        System.out.println();
        System.out.println("[REPO] Test 4.1 : Creation d'une charge");
        try {
            Charge charge = Charge.builder()
                    .titre("Loyer mensuel")
                    .description("Loyer du cabinet")
                    .montant(3000.0)
                    .date(LocalDateTime.now())
                    .idCabinet(1L)
                    .build();
            chargeRepository.create(charge);
            idChargeTest = charge.getIdCharge();
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

        if (idChargeTest == null) {
            System.out.println("[ERREUR] Impossible de continuer sans ID de charge valide");
            return;
        }

        System.out.println();
        System.out.println("[REPO] Test 4.2 : Lecture par ID (findById)");
        try {
            Charge charge = chargeRepository.findById(idChargeTest);
            if (charge != null && charge.getTitre().equals("Loyer mensuel")) {
                System.out.println("       [OK] Charge trouvee : " + charge.getTitre());
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
        System.out.println("[REPO] Test 4.3 : Recherche par cabinet (findByCabinetId)");
        try {
            List<Charge> charges = chargeRepository.findByCabinetId(1L);
            System.out.println("       [OK] " + charges.size() + " charge(s) pour cabinet 1");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 4.4 : Calcul total charges (calculateTotalCharges)");
        try {
            Double total = chargeRepository.calculateTotalCharges(1L);
            System.out.println("       [OK] Total charges cabinet 1 : " + total + " DH");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 4.5 : Recherche par titre (findByTitreContaining)");
        try {
            List<Charge> charges = chargeRepository.findByTitreContaining("Loyer");
            System.out.println("       [OK] " + charges.size() + " charge(s) contenant 'Loyer'");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }

        System.out.println();
        System.out.println("[REPO] Test 4.6 : Liste toutes les charges (findAll)");
        try {
            List<Charge> charges = chargeRepository.findAll();
            System.out.println("       [OK] " + charges.size() + " charge(s) au total");
            testsReussis++;
        } catch (Exception e) {
            System.out.println("       [ECHEC] " + e.getMessage());
            testsEchoues++;
        }
    }

    private static void testerFluxFacturation() {
        System.out.println();
        System.out.println("[FLUX] Scenario : Cycle complet de facturation");
        System.out.println();

        try {
            System.out.println("       Etape 1 : Creation facture pour consultation 5000");
            Facture facture = Facture.builder()
                    .idConsultation(5000L)
                    .totaleFacture(3000.0)
                    .totalePaye(0.0)
                    .reste(3000.0)
                    .statut("EN_ATTENTE")
                    .dateFacture(LocalDateTime.now())
                    .build();
            factureRepository.create(facture);
            System.out.println("                 Facture ID : " + facture.getIdFacture());
            System.out.println("                 Montant : " + facture.getTotaleFacture() + " DH");

            System.out.println();
            System.out.println("       Etape 2 : Premier paiement (1000 DH)");
            facture.setTotalePaye(1000.0);
            facture.setReste(2000.0);
            facture.setStatut("PARTIELLEMENT_PAYEE");
            factureRepository.update(facture);
            System.out.println("                 Paye : " + facture.getTotalePaye() + " DH");
            System.out.println("                 Reste : " + facture.getReste() + " DH");

            System.out.println();
            System.out.println("       Etape 3 : Creation situation financiere");
            SituationFinanciere situation = SituationFinanciere.builder()
                    .idFacture(facture.getIdFacture())
                    .totaleDesActes(3000.0)
                    .totalePaye(1000.0)
                    .credit(2000.0)
                    .statut("EN_CREDIT")
                    .build();
            situationRepository.create(situation);
            System.out.println("                 Credit patient : " + situation.getCredit() + " DH");

            System.out.println();
            System.out.println("       Etape 4 : Deuxieme paiement (1500 DH)");
            facture.setTotalePaye(2500.0);
            facture.setReste(500.0);
            factureRepository.update(facture);
            situation.setTotalePaye(2500.0);
            situation.setCredit(500.0);
            situationRepository.update(situation);
            System.out.println("                 Paye : " + facture.getTotalePaye() + " DH");
            System.out.println("                 Reste : " + facture.getReste() + " DH");

            System.out.println();
            System.out.println("       Etape 5 : Paiement final (500 DH)");
            facture.setTotalePaye(3000.0);
            facture.setReste(0.0);
            facture.setStatut("PAYEE");
            factureRepository.update(facture);
            situation.setTotalePaye(3000.0);
            situation.setCredit(0.0);
            situation.setStatut("SOLDE");
            situationRepository.update(situation);
            System.out.println("                 Statut facture : " + facture.getStatut());
            System.out.println("                 Statut situation : " + situation.getStatut());

            System.out.println();
            System.out.println("       Etape 6 : Verification des totaux");
            Double totalImpaye = factureRepository.calculateTotalImpaye();
            Double totalRevenues = revenueRepository.calculateTotalRevenues(1L);
            Double totalCharges = chargeRepository.calculateTotalCharges(1L);
            System.out.println("                 Total impaye : " + totalImpaye + " DH");
            System.out.println("                 Total revenues : " + totalRevenues + " DH");
            System.out.println("                 Total charges : " + totalCharges + " DH");

            System.out.println();
            System.out.println("       [OK] Flux de facturation execute avec succes");
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
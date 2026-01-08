package ma.dentalTech.service.caisse;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.entities.caisse.Charge;
import ma.dentalTech.entities.caisse.Facture;
import ma.dentalTech.entities.caisse.Revenue;
import ma.dentalTech.entities.caisse.Statistique;
import ma.dentalTech.repository.modules.caisse.api.ChargeRepository;
import ma.dentalTech.repository.modules.caisse.api.FactureRepository;
import ma.dentalTech.repository.modules.caisse.api.RevenueRepository;
import ma.dentalTech.service.modules.caisse.impl.CaisseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class CaisseServiceTest {

    private CaisseServiceImpl caisseService;
    private MockFactureRepository mockFactureRepository;
    private MockRevenueRepository mockRevenueRepository;
    private MockChargeRepository mockChargeRepository;

    @BeforeEach
    void setUp() {
        mockFactureRepository = new MockFactureRepository();
        mockRevenueRepository = new MockRevenueRepository();
        mockChargeRepository = new MockChargeRepository();
        caisseService = new CaisseServiceImpl(mockFactureRepository, mockRevenueRepository, mockChargeRepository);
    }

    @Test
    @DisplayName("Calculer le chiffre d'affaires")
    void calculerChiffreAffaires() throws ServiceException {
        LocalDateTime now = LocalDateTime.now();
        Facture f1 = creerFactureTest(1000.0, now);
        mockFactureRepository.create(f1);
        Facture f2 = creerFactureTest(500.0, now);
        mockFactureRepository.create(f2);
        Revenue r1 = creerRevenueTest(200.0, 1L, now);
        mockRevenueRepository.create(r1);
        Double result = caisseService.calculerChiffreAffaires(1L, now.minusDays(1), now.plusDays(1));
        assertThat(result).isEqualTo(1700.0);
    }

    @Test
    @DisplayName("Calculer le bénéfice net")
    void calculerBeneficeNet() throws ServiceException {
        LocalDateTime now = LocalDateTime.now();
        Facture f1 = creerFactureTest(1000.0, now);
        mockFactureRepository.create(f1);
        Revenue r1 = creerRevenueTest(500.0, 1L, now);
        mockRevenueRepository.create(r1);
        Charge c1 = creerChargeTest(300.0, 1L, now);
        mockChargeRepository.create(c1);
        Double result = caisseService.calculerBeneficeNet(1L, now.minusDays(1), now.plusDays(1));
        assertThat(result).isEqualTo(1200.0);
    }

    @Test
    @DisplayName("Calculer le total des encaissements")
    void calculerTotalEncaissements() throws ServiceException {
        LocalDateTime now = LocalDateTime.now();
        Facture f1 = creerFactureTest(1000.0, now);
        f1.setTotalePaye(800.0);
        mockFactureRepository.create(f1);
        Facture f2 = creerFactureTest(500.0, now);
        f2.setTotalePaye(500.0);
        mockFactureRepository.create(f2);
        Double result = caisseService.calculerTotalEncaissements(now.minusDays(1), now.plusDays(1));
        assertThat(result).isEqualTo(1300.0);
    }

    @Test
    @DisplayName("Calculer le total des décaissements")
    void calculerTotalDecaissements() throws ServiceException {
        LocalDateTime now = LocalDateTime.now();
        Charge c1 = creerChargeTest(300.0, 1L, now);
        mockChargeRepository.create(c1);
        Charge c2 = creerChargeTest(200.0, 1L, now);
        mockChargeRepository.create(c2);
        Double result = caisseService.calculerTotalDecaissements(1L, now.minusDays(1), now.plusDays(1));
        assertThat(result).isEqualTo(500.0);
    }

    @Test
    @DisplayName("Calculer avec dates nulles doit échouer")
    void calculerAvecDatesNulles() {
        assertThatThrownBy(() -> caisseService.calculerChiffreAffaires(1L, null, null))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("obligatoires");
    }

    @Test
    @DisplayName("Calculer avec dates inversées doit échouer")
    void calculerAvecDatesInversees() {
        LocalDateTime now = LocalDateTime.now();
        assertThatThrownBy(() -> caisseService.calculerChiffreAffaires(1L, now, now.minusDays(5)))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("antérieure");
    }

    @Test
    @DisplayName("Obtenir statistiques journalières")
    void obtenirStatistiquesJournalieres() throws ServiceException {
        LocalDate today = LocalDate.now();
        LocalDateTime now = today.atStartOfDay().plusHours(10);
        Facture f1 = creerFactureTest(1000.0, now);
        f1.setTotalePaye(800.0);
        mockFactureRepository.create(f1);
        Revenue r1 = creerRevenueTest(200.0, 1L, now);
        mockRevenueRepository.create(r1);
        Charge c1 = creerChargeTest(100.0, 1L, now);
        mockChargeRepository.create(c1);
        Map<String, Double> stats = caisseService.obtenirStatistiquesJournalieres(1L, today);
        assertThat(stats).containsKey("chiffreAffaires");
        assertThat(stats).containsKey("encaissements");
        assertThat(stats).containsKey("decaissements");
        assertThat(stats).containsKey("beneficeNet");
    }

    @Test
    @DisplayName("Obtenir statistiques journalières avec date nulle doit échouer")
    void obtenirStatistiquesJournalieresDateNulle() {
        assertThatThrownBy(() -> caisseService.obtenirStatistiquesJournalieres(1L, null))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("obligatoire");
    }

    @Test
    @DisplayName("Obtenir statistiques mensuelles")
    void obtenirStatistiquesMensuelles() throws ServiceException {
        LocalDateTime now = LocalDateTime.now();
        Facture f1 = creerFactureTest(1000.0, now);
        f1.setTotalePaye(800.0);
        mockFactureRepository.create(f1);
        Map<String, Double> stats = caisseService.obtenirStatistiquesMensuelles(1L, now.getMonthValue(), now.getYear());
        assertThat(stats).containsKey("chiffreAffaires");
        assertThat(stats).containsKey("tauxRecouvrement");
    }

    @Test
    @DisplayName("Obtenir statistiques mensuelles avec mois invalide doit échouer")
    void obtenirStatistiquesMensuellesMoisInvalide() {
        assertThatThrownBy(() -> caisseService.obtenirStatistiquesMensuelles(1L, 13, 2024))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("entre 1 et 12");
    }

    @Test
    @DisplayName("Obtenir statistiques annuelles")
    void obtenirStatistiquesAnnuelles() throws ServiceException {
        LocalDateTime now = LocalDateTime.now();
        Facture f1 = creerFactureTest(1000.0, now);
        mockFactureRepository.create(f1);
        Revenue r1 = creerRevenueTest(500.0, 1L, now);
        mockRevenueRepository.create(r1);
        Charge c1 = creerChargeTest(200.0, 1L, now);
        mockChargeRepository.create(c1);
        Map<String, Double> stats = caisseService.obtenirStatistiquesAnnuelles(1L, now.getYear());
        assertThat(stats).containsKey("chiffreAffaires");
        assertThat(stats).containsKey("totalRevenues");
        assertThat(stats).containsKey("totalCharges");
    }

    @Test
    @DisplayName("Calculer taux de recouvrement")
    void calculerTauxRecouvrement() throws ServiceException {
        LocalDateTime now = LocalDateTime.now();
        Facture f1 = creerFactureTest(1000.0, now);
        f1.setTotalePaye(800.0);
        mockFactureRepository.create(f1);
        Facture f2 = creerFactureTest(500.0, now);
        f2.setTotalePaye(500.0);
        mockFactureRepository.create(f2);
        Double taux = caisseService.calculerTauxRecouvrement(now.minusDays(1), now.plusDays(1));
        assertThat(taux).isCloseTo(86.67, within(0.1));
    }

    @Test
    @DisplayName("Calculer taux de recouvrement sans factures")
    void calculerTauxRecouvrementSansFactures() throws ServiceException {
        LocalDateTime now = LocalDateTime.now();
        Double taux = caisseService.calculerTauxRecouvrement(now.minusDays(1), now.plusDays(1));
        assertThat(taux).isEqualTo(100.0);
    }

    @Test
    @DisplayName("Générer rapport financier")
    void genererRapportFinancier() throws ServiceException {
        LocalDateTime now = LocalDateTime.now();
        Facture f1 = creerFactureTest(1000.0, now);
        f1.setTotalePaye(800.0);
        f1.setReste(200.0);
        mockFactureRepository.create(f1);
        List<Statistique> rapport = caisseService.genererRapportFinancier(1L, now.minusDays(1), now.plusDays(1));
        assertThat(rapport).isNotEmpty();
        assertThat(rapport).anyMatch(s -> "Chiffre d'Affaires".equals(s.getNom()));
        assertThat(rapport).anyMatch(s -> "Bénéfice Net".equals(s.getNom()));
        assertThat(rapport).anyMatch(s -> "Taux de Recouvrement".equals(s.getNom()));
    }

    @Test
    @DisplayName("Obtenir tableau de bord")
    void obtenirTableauDeBord() throws ServiceException {
        LocalDateTime now = LocalDateTime.now();
        Facture f1 = creerFactureTest(1000.0, now);
        f1.setReste(200.0);
        mockFactureRepository.create(f1);
        Map<String, Object> dashboard = caisseService.obtenirTableauDeBord(1L);
        assertThat(dashboard).containsKey("statistiquesJournalieres");
        assertThat(dashboard).containsKey("statistiquesMensuelles");
        assertThat(dashboard).containsKey("facturesImpayees");
        assertThat(dashboard).containsKey("totalImpaye");
    }

    @Test
    @DisplayName("Obtenir répartition revenus par mois")
    void obtenirRepartitionRevenusParMois() throws ServiceException {
        LocalDateTime now = LocalDateTime.now();
        Revenue r1 = creerRevenueTest(500.0, 1L, now);
        mockRevenueRepository.create(r1);
        Map<String, Double> repartition = caisseService.obtenirRepartitionRevenusParMois(1L, now.getYear());
        assertThat(repartition).hasSize(12);
        assertThat(repartition).containsKey("Janvier");
        assertThat(repartition).containsKey("Décembre");
    }

    @Test
    @DisplayName("Obtenir répartition charges par mois")
    void obtenirRepartitionChargesParMois() throws ServiceException {
        LocalDateTime now = LocalDateTime.now();
        Charge c1 = creerChargeTest(200.0, 1L, now);
        mockChargeRepository.create(c1);
        Map<String, Double> repartition = caisseService.obtenirRepartitionChargesParMois(1L, now.getYear());
        assertThat(repartition).hasSize(12);
        assertThat(repartition).containsKey("Janvier");
        assertThat(repartition).containsKey("Décembre");
    }

    private Facture creerFactureTest(Double montant, LocalDateTime date) {
        return Facture.builder().idConsultation(1L).totaleFacture(montant).totalePaye(0.0).reste(montant).statut("EN_ATTENTE").dateFacture(date).build();
    }

    private Revenue creerRevenueTest(Double montant, Long idCabinet, LocalDateTime date) {
        return Revenue.builder().titre("Revenue test").montant(montant).idCabinet(idCabinet).date(date).build();
    }

    private Charge creerChargeTest(Double montant, Long idCabinet, LocalDateTime date) {
        return Charge.builder().titre("Charge test").montant(montant).idCabinet(idCabinet).date(date).build();
    }

    private static class MockFactureRepository implements FactureRepository {
        private final List<Facture> factures = new ArrayList<>();
        private Long nextId = 1L;
        @Override public List<Facture> findAll() { return new ArrayList<>(factures); }
        @Override public Facture findById(Long id) { return factures.stream().filter(f -> f.getIdFacture().equals(id)).findFirst().orElse(null); }
        @Override public void create(Facture f) { f.setIdFacture(nextId++); factures.add(f); }
        @Override public void update(Facture f) { factures.removeIf(x -> x.getIdFacture().equals(f.getIdFacture())); factures.add(f); }
        @Override public void delete(Facture f) { factures.remove(f); }
        @Override public void deleteById(Long id) { factures.removeIf(f -> f.getIdFacture().equals(id)); }
        @Override public Optional<Facture> findByConsultationId(Long id) { return Optional.empty(); }
        @Override public List<Facture> findByStatut(String s) { return new ArrayList<>(); }
        @Override public List<Facture> findByDateBetween(LocalDateTime d, LocalDateTime f) { return factures.stream().filter(x -> x.getDateFacture() != null && !x.getDateFacture().isBefore(d) && !x.getDateFacture().isAfter(f)).toList(); }
        @Override public List<Facture> findFacturesImpayees() { return factures.stream().filter(f -> f.getReste() != null && f.getReste() > 0).toList(); }
        @Override public List<Facture> findByPatientId(Long id) { return new ArrayList<>(); }
        @Override public Double calculateTotalFactures(LocalDateTime d, LocalDateTime f) { return factures.stream().filter(x -> x.getDateFacture() != null && !x.getDateFacture().isBefore(d) && !x.getDateFacture().isAfter(f)).mapToDouble(x -> x.getTotaleFacture() != null ? x.getTotaleFacture() : 0.0).sum(); }
        @Override public Double calculateTotalPaye(LocalDateTime d, LocalDateTime f) { return factures.stream().filter(x -> x.getDateFacture() != null && !x.getDateFacture().isBefore(d) && !x.getDateFacture().isAfter(f)).mapToDouble(x -> x.getTotalePaye() != null ? x.getTotalePaye() : 0.0).sum(); }
        @Override public Double calculateTotalImpaye() { return factures.stream().filter(f -> f.getReste() != null && f.getReste() > 0).mapToDouble(Facture::getReste).sum(); }
    }

    private static class MockRevenueRepository implements RevenueRepository {
        private final List<Revenue> revenues = new ArrayList<>();
        private Long nextId = 1L;
        @Override public List<Revenue> findAll() { return new ArrayList<>(revenues); }
        @Override public Revenue findById(Long id) { return revenues.stream().filter(r -> r.getIdRevenue().equals(id)).findFirst().orElse(null); }
        @Override public void create(Revenue r) { r.setIdRevenue(nextId++); revenues.add(r); }
        @Override public void update(Revenue r) { revenues.removeIf(x -> x.getIdRevenue().equals(r.getIdRevenue())); revenues.add(r); }
        @Override public void delete(Revenue r) { revenues.remove(r); }
        @Override public void deleteById(Long id) { revenues.removeIf(r -> r.getIdRevenue().equals(id)); }
        @Override public List<Revenue> findByCabinetId(Long id) { return revenues.stream().filter(r -> r.getIdCabinet().equals(id)).toList(); }
        @Override public List<Revenue> findByDateBetween(LocalDateTime d, LocalDateTime f) { return revenues.stream().filter(r -> r.getDate() != null && !r.getDate().isBefore(d) && !r.getDate().isAfter(f)).toList(); }
        @Override public List<Revenue> findByTitreContaining(String t) { return new ArrayList<>(); }
        @Override public Double calculateTotalRevenues(Long id) { return revenues.stream().filter(r -> r.getIdCabinet().equals(id)).mapToDouble(Revenue::getMontant).sum(); }
        @Override public Double calculateTotalRevenuesBetween(Long id, LocalDateTime d, LocalDateTime f) { return revenues.stream().filter(r -> r.getIdCabinet().equals(id) && r.getDate() != null && !r.getDate().isBefore(d) && !r.getDate().isAfter(f)).mapToDouble(Revenue::getMontant).sum(); }
    }

    private static class MockChargeRepository implements ChargeRepository {
        private final List<Charge> charges = new ArrayList<>();
        private Long nextId = 1L;
        @Override public List<Charge> findAll() { return new ArrayList<>(charges); }
        @Override public Charge findById(Long id) { return charges.stream().filter(c -> c.getIdCharge().equals(id)).findFirst().orElse(null); }
        @Override public void create(Charge c) { c.setIdCharge(nextId++); charges.add(c); }
        @Override public void update(Charge c) { charges.removeIf(x -> x.getIdCharge().equals(c.getIdCharge())); charges.add(c); }
        @Override public void delete(Charge c) { charges.remove(c); }
        @Override public void deleteById(Long id) { charges.removeIf(c -> c.getIdCharge().equals(id)); }
        @Override public List<Charge> findByCabinetId(Long id) { return charges.stream().filter(c -> c.getIdCabinet().equals(id)).toList(); }
        @Override public List<Charge> findByDateBetween(LocalDateTime d, LocalDateTime f) { return charges.stream().filter(c -> c.getDate() != null && !c.getDate().isBefore(d) && !c.getDate().isAfter(f)).toList(); }
        @Override public List<Charge> findByTitreContaining(String t) { return new ArrayList<>(); }
        @Override public Double calculateTotalCharges(Long id) { return charges.stream().filter(c -> c.getIdCabinet().equals(id)).mapToDouble(Charge::getMontant).sum(); }
        @Override public Double calculateTotalChargesBetween(Long id, LocalDateTime d, LocalDateTime f) { return charges.stream().filter(c -> c.getIdCabinet().equals(id) && c.getDate() != null && !c.getDate().isBefore(d) && !c.getDate().isAfter(f)).mapToDouble(Charge::getMontant).sum(); }
    }
}
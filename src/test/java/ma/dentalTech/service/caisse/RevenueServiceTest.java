package ma.dentalTech.service.caisse;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.caisse.Revenue;
import ma.dentalTech.repository.modules.caisse.api.RevenueRepository;
import ma.dentalTech.service.modules.caisse.impl.RevenueServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class RevenueServiceTest {

    private RevenueServiceImpl revenueService;
    private MockRevenueRepository mockRepository;

    @BeforeEach
    void setUp() {
        mockRepository = new MockRevenueRepository();
        revenueService = new RevenueServiceImpl(mockRepository);
    }

    @Test
    @DisplayName("Créer un revenu valide")
    void creerRevenueValide() throws ServiceException, ValidationException {
        Revenue revenue = Revenue.builder()
                .titre("Paiement consultation")
                .description("Paiement patient X")
                .montant(500.0)
                .idCabinet(1L)
                .build();

        Revenue result = revenueService.creerRevenue(revenue);

        assertThat(result).isNotNull();
        assertThat(result.getIdRevenue()).isNotNull();
        assertThat(result.getDate()).isNotNull();
    }

    @Test
    @DisplayName("Créer un revenu sans titre doit échouer")
    void creerRevenueSansTitre() {
        Revenue revenue = Revenue.builder()
                .montant(500.0)
                .idCabinet(1L)
                .build();

        assertThatThrownBy(() -> revenueService.creerRevenue(revenue))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("titre");
    }

    @Test
    @DisplayName("Créer un revenu avec titre trop court doit échouer")
    void creerRevenueTitreTropCourt() {
        Revenue revenue = Revenue.builder()
                .titre("AB")
                .montant(500.0)
                .idCabinet(1L)
                .build();

        assertThatThrownBy(() -> revenueService.creerRevenue(revenue))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("3 caractères");
    }

    @Test
    @DisplayName("Créer un revenu sans montant doit échouer")
    void creerRevenueSansMontant() {
        Revenue revenue = Revenue.builder()
                .titre("Paiement")
                .idCabinet(1L)
                .build();

        assertThatThrownBy(() -> revenueService.creerRevenue(revenue))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("montant");
    }

    @Test
    @DisplayName("Créer un revenu avec montant négatif doit échouer")
    void creerRevenueMontantNegatif() {
        Revenue revenue = Revenue.builder()
                .titre("Paiement")
                .montant(-100.0)
                .idCabinet(1L)
                .build();

        assertThatThrownBy(() -> revenueService.creerRevenue(revenue))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("supérieur à 0");
    }

    @Test
    @DisplayName("Créer un revenu avec montant zéro doit échouer")
    void creerRevenueMontantZero() {
        Revenue revenue = Revenue.builder()
                .titre("Paiement")
                .montant(0.0)
                .idCabinet(1L)
                .build();

        assertThatThrownBy(() -> revenueService.creerRevenue(revenue))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("supérieur à 0");
    }

    @Test
    @DisplayName("Créer un revenu sans cabinet doit échouer")
    void creerRevenueSansCabinet() {
        Revenue revenue = Revenue.builder()
                .titre("Paiement")
                .montant(500.0)
                .build();

        assertThatThrownBy(() -> revenueService.creerRevenue(revenue))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("cabinet");
    }

    @Test
    @DisplayName("Modifier un revenu valide")
    void modifierRevenueValide() throws ServiceException, ValidationException {
        Revenue revenue = creerRevenueTest();
        revenueService.creerRevenue(revenue);

        revenue.setMontant(750.0);
        revenue.setTitre("Nouveau titre");

        Revenue result = revenueService.modifierRevenue(revenue);

        assertThat(result.getMontant()).isEqualTo(750.0);
        assertThat(result.getTitre()).isEqualTo("Nouveau titre");
    }

    @Test
    @DisplayName("Modifier un revenu inexistant doit échouer")
    void modifierRevenueInexistant() {
        Revenue revenue = Revenue.builder()
                .idRevenue(999L)
                .titre("Test")
                .montant(500.0)
                .idCabinet(1L)
                .date(LocalDateTime.now())
                .build();

        assertThatThrownBy(() -> revenueService.modifierRevenue(revenue))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("introuvable");
    }

    @Test
    @DisplayName("Modifier un revenu sans ID doit échouer")
    void modifierRevenueSansId() {
        Revenue revenue = creerRevenueTest();

        assertThatThrownBy(() -> revenueService.modifierRevenue(revenue))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("identifiant");
    }

    @Test
    @DisplayName("Supprimer un revenu valide")
    void supprimerRevenueValide() throws ServiceException, ValidationException {
        Revenue revenue = creerRevenueTest();
        revenueService.creerRevenue(revenue);

        revenueService.supprimerRevenue(revenue.getIdRevenue());

        assertThat(revenueService.obtenirRevenue(revenue.getIdRevenue())).isNull();
    }

    @Test
    @DisplayName("Supprimer un revenu inexistant doit échouer")
    void supprimerRevenueInexistant() {
        assertThatThrownBy(() -> revenueService.supprimerRevenue(999L))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("introuvable");
    }

    @Test
    @DisplayName("Supprimer un revenu avec ID null doit échouer")
    void supprimerRevenueIdNull() {
        assertThatThrownBy(() -> revenueService.supprimerRevenue(null))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("obligatoire");
    }

    @Test
    @DisplayName("Lister les revenus par cabinet")
    void listerRevenuesParCabinet() throws ServiceException, ValidationException {
        Revenue r1 = creerRevenueTest();
        r1.setIdCabinet(1L);
        revenueService.creerRevenue(r1);

        Revenue r2 = creerRevenueTest();
        r2.setIdCabinet(1L);
        revenueService.creerRevenue(r2);

        Revenue r3 = creerRevenueTest();
        r3.setIdCabinet(2L);
        revenueService.creerRevenue(r3);

        List<Revenue> result = revenueService.listerRevenuesParCabinet(1L);

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(r -> r.getIdCabinet().equals(1L));
    }

    @Test
    @DisplayName("Lister les revenus par cabinet avec ID null doit échouer")
    void listerRevenuesParCabinetIdNull() {
        assertThatThrownBy(() -> revenueService.listerRevenuesParCabinet(null))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("obligatoire");
    }

    @Test
    @DisplayName("Lister les revenus par période")
    void listerRevenuesParPeriode() throws ServiceException, ValidationException {
        LocalDateTime now = LocalDateTime.now();

        Revenue r1 = creerRevenueTest();
        r1.setDate(now.minusDays(1));
        mockRepository.create(r1);

        Revenue r2 = creerRevenueTest();
        r2.setDate(now);
        mockRepository.create(r2);

        Revenue r3 = creerRevenueTest();
        r3.setDate(now.minusDays(10));
        mockRepository.create(r3);

        List<Revenue> result = revenueService.listerRevenuesParPeriode(now.minusDays(5), now.plusDays(1));

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Lister les revenus par période avec dates inversées doit échouer")
    void listerRevenuesParPeriodeDatesInversees() {
        LocalDateTime now = LocalDateTime.now();

        assertThatThrownBy(() -> revenueService.listerRevenuesParPeriode(now, now.minusDays(5)))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("antérieure");
    }

    @Test
    @DisplayName("Lister les revenus par période avec dates nulles doit échouer")
    void listerRevenuesParPeriodeDatesNulles() {
        assertThatThrownBy(() -> revenueService.listerRevenuesParPeriode(null, null))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("obligatoires");
    }

    @Test
    @DisplayName("Rechercher revenus par titre")
    void rechercherRevenuesParTitre() throws ServiceException, ValidationException {
        Revenue r1 = creerRevenueTest();
        r1.setTitre("Paiement consultation");
        mockRepository.create(r1);

        Revenue r2 = creerRevenueTest();
        r2.setTitre("Paiement acte");
        mockRepository.create(r2);

        Revenue r3 = creerRevenueTest();
        r3.setTitre("Remboursement");
        mockRepository.create(r3);

        List<Revenue> result = revenueService.rechercherRevenuesParTitre("Paiement");

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(r -> r.getTitre().contains("Paiement"));
    }

    @Test
    @DisplayName("Rechercher revenus avec titre vide doit échouer")
    void rechercherRevenuesParTitreVide() {
        assertThatThrownBy(() -> revenueService.rechercherRevenuesParTitre(""))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("obligatoire");
    }

    @Test
    @DisplayName("Calculer le total des revenus")
    void calculerTotalRevenues() throws ServiceException, ValidationException {
        Revenue r1 = creerRevenueTest();
        r1.setIdCabinet(1L);
        r1.setMontant(500.0);
        mockRepository.create(r1);

        Revenue r2 = creerRevenueTest();
        r2.setIdCabinet(1L);
        r2.setMontant(300.0);
        mockRepository.create(r2);

        Revenue r3 = creerRevenueTest();
        r3.setIdCabinet(2L);
        r3.setMontant(200.0);
        mockRepository.create(r3);

        Double result = revenueService.calculerTotalRevenues(1L);

        assertThat(result).isEqualTo(800.0);
    }

    @Test
    @DisplayName("Calculer le total des revenus par période")
    void calculerTotalRevenuesParPeriode() throws ServiceException, ValidationException {
        LocalDateTime now = LocalDateTime.now();

        Revenue r1 = creerRevenueTest();
        r1.setIdCabinet(1L);
        r1.setMontant(500.0);
        r1.setDate(now);
        mockRepository.create(r1);

        Revenue r2 = creerRevenueTest();
        r2.setIdCabinet(1L);
        r2.setMontant(300.0);
        r2.setDate(now.minusDays(1));
        mockRepository.create(r2);

        Revenue r3 = creerRevenueTest();
        r3.setIdCabinet(1L);
        r3.setMontant(200.0);
        r3.setDate(now.minusDays(10));
        mockRepository.create(r3);

        Double result = revenueService.calculerTotalRevenuesParPeriode(1L, now.minusDays(5), now.plusDays(1));

        assertThat(result).isEqualTo(800.0);
    }

    @Test
    @DisplayName("Lister tous les revenus")
    void listerTousLesRevenues() throws ServiceException, ValidationException {
        revenueService.creerRevenue(creerRevenueTest());
        revenueService.creerRevenue(creerRevenueTest());
        revenueService.creerRevenue(creerRevenueTest());

        List<Revenue> result = revenueService.listerTousLesRevenues();

        assertThat(result).hasSize(3);
    }

    @Test
    @DisplayName("Obtenir un revenu inexistant retourne null")
    void obtenirRevenueInexistant() throws ServiceException {
        Revenue result = revenueService.obtenirRevenue(999L);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Obtenir un revenu avec ID null doit échouer")
    void obtenirRevenueIdNull() {
        assertThatThrownBy(() -> revenueService.obtenirRevenue(null))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("obligatoire");
    }

    private Revenue creerRevenueTest() {
        return Revenue.builder()
                .titre("Paiement test")
                .description("Description test")
                .montant(500.0)
                .idCabinet(1L)
                .date(LocalDateTime.now())
                .build();
    }

    private static class MockRevenueRepository implements RevenueRepository {
        private final List<Revenue> revenues = new ArrayList<>();
        private Long nextId = 1L;

        @Override
        public List<Revenue> findAll() {
            return new ArrayList<>(revenues);
        }

        @Override
        public Revenue findById(Long id) {
            return revenues.stream()
                    .filter(r -> r.getIdRevenue().equals(id))
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public void create(Revenue revenue) {
            revenue.setIdRevenue(nextId++);
            revenues.add(revenue);
        }

        @Override
        public void update(Revenue revenue) {
            revenues.removeIf(r -> r.getIdRevenue().equals(revenue.getIdRevenue()));
            revenues.add(revenue);
        }

        @Override
        public void delete(Revenue revenue) {
            revenues.remove(revenue);
        }

        @Override
        public void deleteById(Long id) {
            revenues.removeIf(r -> r.getIdRevenue().equals(id));
        }

        @Override
        public List<Revenue> findByCabinetId(Long idCabinet) {
            return revenues.stream()
                    .filter(r -> r.getIdCabinet().equals(idCabinet))
                    .toList();
        }

        @Override
        public List<Revenue> findByDateBetween(LocalDateTime debut, LocalDateTime fin) {
            return revenues.stream()
                    .filter(r -> r.getDate() != null &&
                            !r.getDate().isBefore(debut) &&
                            !r.getDate().isAfter(fin))
                    .toList();
        }

        @Override
        public List<Revenue> findByTitreContaining(String titre) {
            return revenues.stream()
                    .filter(r -> r.getTitre() != null && r.getTitre().contains(titre))
                    .toList();
        }

        @Override
        public Double calculateTotalRevenues(Long idCabinet) {
            return revenues.stream()
                    .filter(r -> r.getIdCabinet().equals(idCabinet))
                    .mapToDouble(Revenue::getMontant)
                    .sum();
        }

        @Override
        public Double calculateTotalRevenuesBetween(Long idCabinet, LocalDateTime debut, LocalDateTime fin) {
            return revenues.stream()
                    .filter(r -> r.getIdCabinet().equals(idCabinet) &&
                            r.getDate() != null &&
                            !r.getDate().isBefore(debut) &&
                            !r.getDate().isAfter(fin))
                    .mapToDouble(Revenue::getMontant)
                    .sum();
        }
    }
}
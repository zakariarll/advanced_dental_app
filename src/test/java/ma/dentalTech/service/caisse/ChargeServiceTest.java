package ma.dentalTech.service.caisse;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.caisse.Charge;
import ma.dentalTech.repository.modules.caisse.api.ChargeRepository;
import ma.dentalTech.service.modules.caisse.impl.ChargeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ChargeServiceTest {

    private ChargeServiceImpl chargeService;
    private MockChargeRepository mockRepository;

    @BeforeEach
    void setUp() {
        mockRepository = new MockChargeRepository();
        chargeService = new ChargeServiceImpl(mockRepository);
    }

    @Test
    @DisplayName("Créer une charge valide")
    void creerChargeValide() throws ServiceException, ValidationException {
        Charge charge = Charge.builder()
                .titre("Achat fournitures")
                .description("Fournitures dentaires")
                .montant(200.0)
                .idCabinet(1L)
                .build();

        Charge result = chargeService.creerCharge(charge);

        assertThat(result).isNotNull();
        assertThat(result.getIdCharge()).isNotNull();
        assertThat(result.getDate()).isNotNull();
    }

    @Test
    @DisplayName("Créer une charge sans titre doit échouer")
    void creerChargeSansTitre() {
        Charge charge = Charge.builder()
                .montant(200.0)
                .idCabinet(1L)
                .build();

        assertThatThrownBy(() -> chargeService.creerCharge(charge))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("titre");
    }

    @Test
    @DisplayName("Créer une charge avec titre trop court doit échouer")
    void creerChargeTitreTropCourt() {
        Charge charge = Charge.builder()
                .titre("AB")
                .montant(200.0)
                .idCabinet(1L)
                .build();

        assertThatThrownBy(() -> chargeService.creerCharge(charge))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("3 caractères");
    }

    @Test
    @DisplayName("Créer une charge sans montant doit échouer")
    void creerChargeSansMontant() {
        Charge charge = Charge.builder()
                .titre("Achat")
                .idCabinet(1L)
                .build();

        assertThatThrownBy(() -> chargeService.creerCharge(charge))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("montant");
    }

    @Test
    @DisplayName("Créer une charge avec montant négatif doit échouer")
    void creerChargeMontantNegatif() {
        Charge charge = Charge.builder()
                .titre("Achat")
                .montant(-100.0)
                .idCabinet(1L)
                .build();

        assertThatThrownBy(() -> chargeService.creerCharge(charge))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("supérieur à 0");
    }

    @Test
    @DisplayName("Créer une charge avec montant zéro doit échouer")
    void creerChargeMontantZero() {
        Charge charge = Charge.builder()
                .titre("Achat")
                .montant(0.0)
                .idCabinet(1L)
                .build();

        assertThatThrownBy(() -> chargeService.creerCharge(charge))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("supérieur à 0");
    }

    @Test
    @DisplayName("Créer une charge sans cabinet doit échouer")
    void creerChargeSansCabinet() {
        Charge charge = Charge.builder()
                .titre("Achat")
                .montant(200.0)
                .build();

        assertThatThrownBy(() -> chargeService.creerCharge(charge))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("cabinet");
    }

    @Test
    @DisplayName("Modifier une charge valide")
    void modifierChargeValide() throws ServiceException, ValidationException {
        Charge charge = creerChargeTest();
        chargeService.creerCharge(charge);

        charge.setMontant(350.0);
        charge.setTitre("Nouveau titre");

        Charge result = chargeService.modifierCharge(charge);

        assertThat(result.getMontant()).isEqualTo(350.0);
        assertThat(result.getTitre()).isEqualTo("Nouveau titre");
    }

    @Test
    @DisplayName("Modifier une charge inexistante doit échouer")
    void modifierChargeInexistante() {
        Charge charge = Charge.builder()
                .idCharge(999L)
                .titre("Test")
                .montant(200.0)
                .idCabinet(1L)
                .date(LocalDateTime.now())
                .build();

        assertThatThrownBy(() -> chargeService.modifierCharge(charge))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("introuvable");
    }

    @Test
    @DisplayName("Modifier une charge sans ID doit échouer")
    void modifierChargeSansId() {
        Charge charge = creerChargeTest();

        assertThatThrownBy(() -> chargeService.modifierCharge(charge))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("identifiant");
    }

    @Test
    @DisplayName("Supprimer une charge valide")
    void supprimerChargeValide() throws ServiceException, ValidationException {
        Charge charge = creerChargeTest();
        chargeService.creerCharge(charge);

        chargeService.supprimerCharge(charge.getIdCharge());

        assertThat(chargeService.obtenirCharge(charge.getIdCharge())).isNull();
    }

    @Test
    @DisplayName("Supprimer une charge inexistante doit échouer")
    void supprimerChargeInexistante() {
        assertThatThrownBy(() -> chargeService.supprimerCharge(999L))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("introuvable");
    }

    @Test
    @DisplayName("Supprimer une charge avec ID null doit échouer")
    void supprimerChargeIdNull() {
        assertThatThrownBy(() -> chargeService.supprimerCharge(null))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("obligatoire");
    }

    @Test
    @DisplayName("Lister les charges par cabinet")
    void listerChargesParCabinet() throws ServiceException, ValidationException {
        Charge c1 = creerChargeTest();
        c1.setIdCabinet(1L);
        chargeService.creerCharge(c1);

        Charge c2 = creerChargeTest();
        c2.setIdCabinet(1L);
        chargeService.creerCharge(c2);

        Charge c3 = creerChargeTest();
        c3.setIdCabinet(2L);
        chargeService.creerCharge(c3);

        List<Charge> result = chargeService.listerChargesParCabinet(1L);

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(c -> c.getIdCabinet().equals(1L));
    }

    @Test
    @DisplayName("Lister les charges par cabinet avec ID null doit échouer")
    void listerChargesParCabinetIdNull() {
        assertThatThrownBy(() -> chargeService.listerChargesParCabinet(null))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("obligatoire");
    }

    @Test
    @DisplayName("Lister les charges par période")
    void listerChargesParPeriode() throws ServiceException, ValidationException {
        LocalDateTime now = LocalDateTime.now();

        Charge c1 = creerChargeTest();
        c1.setDate(now.minusDays(1));
        mockRepository.create(c1);

        Charge c2 = creerChargeTest();
        c2.setDate(now);
        mockRepository.create(c2);

        Charge c3 = creerChargeTest();
        c3.setDate(now.minusDays(10));
        mockRepository.create(c3);

        List<Charge> result = chargeService.listerChargesParPeriode(now.minusDays(5), now.plusDays(1));

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Lister les charges par période avec dates inversées doit échouer")
    void listerChargesParPeriodeDatesInversees() {
        LocalDateTime now = LocalDateTime.now();

        assertThatThrownBy(() -> chargeService.listerChargesParPeriode(now, now.minusDays(5)))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("antérieure");
    }

    @Test
    @DisplayName("Lister les charges par période avec dates nulles doit échouer")
    void listerChargesParPeriodeDatesNulles() {
        assertThatThrownBy(() -> chargeService.listerChargesParPeriode(null, null))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("obligatoires");
    }

    @Test
    @DisplayName("Rechercher charges par titre")
    void rechercherChargesParTitre() throws ServiceException, ValidationException {
        Charge c1 = creerChargeTest();
        c1.setTitre("Achat fournitures");
        mockRepository.create(c1);

        Charge c2 = creerChargeTest();
        c2.setTitre("Achat matériel");
        mockRepository.create(c2);

        Charge c3 = creerChargeTest();
        c3.setTitre("Loyer");
        mockRepository.create(c3);

        List<Charge> result = chargeService.rechercherChargesParTitre("Achat");

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(c -> c.getTitre().contains("Achat"));
    }

    @Test
    @DisplayName("Rechercher charges avec titre vide doit échouer")
    void rechercherChargesParTitreVide() {
        assertThatThrownBy(() -> chargeService.rechercherChargesParTitre(""))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("obligatoire");
    }

    @Test
    @DisplayName("Calculer le total des charges")
    void calculerTotalCharges() throws ServiceException, ValidationException {
        Charge c1 = creerChargeTest();
        c1.setIdCabinet(1L);
        c1.setMontant(200.0);
        mockRepository.create(c1);

        Charge c2 = creerChargeTest();
        c2.setIdCabinet(1L);
        c2.setMontant(150.0);
        mockRepository.create(c2);

        Charge c3 = creerChargeTest();
        c3.setIdCabinet(2L);
        c3.setMontant(100.0);
        mockRepository.create(c3);

        Double result = chargeService.calculerTotalCharges(1L);

        assertThat(result).isEqualTo(350.0);
    }

    @Test
    @DisplayName("Calculer le total des charges par période")
    void calculerTotalChargesParPeriode() throws ServiceException, ValidationException {
        LocalDateTime now = LocalDateTime.now();

        Charge c1 = creerChargeTest();
        c1.setIdCabinet(1L);
        c1.setMontant(200.0);
        c1.setDate(now);
        mockRepository.create(c1);

        Charge c2 = creerChargeTest();
        c2.setIdCabinet(1L);
        c2.setMontant(150.0);
        c2.setDate(now.minusDays(1));
        mockRepository.create(c2);

        Charge c3 = creerChargeTest();
        c3.setIdCabinet(1L);
        c3.setMontant(100.0);
        c3.setDate(now.minusDays(10));
        mockRepository.create(c3);

        Double result = chargeService.calculerTotalChargesParPeriode(1L, now.minusDays(5), now.plusDays(1));

        assertThat(result).isEqualTo(350.0);
    }

    @Test
    @DisplayName("Calculer le total des charges avec cabinet null doit échouer")
    void calculerTotalChargesCabinetNull() {
        assertThatThrownBy(() -> chargeService.calculerTotalCharges(null))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("obligatoire");
    }

    @Test
    @DisplayName("Lister toutes les charges")
    void listerToutesLesCharges() throws ServiceException, ValidationException {
        chargeService.creerCharge(creerChargeTest());
        chargeService.creerCharge(creerChargeTest());
        chargeService.creerCharge(creerChargeTest());

        List<Charge> result = chargeService.listerToutesLesCharges();

        assertThat(result).hasSize(3);
    }

    @Test
    @DisplayName("Obtenir une charge inexistante retourne null")
    void obtenirChargeInexistante() throws ServiceException {
        Charge result = chargeService.obtenirCharge(999L);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Obtenir une charge avec ID null doit échouer")
    void obtenirChargeIdNull() {
        assertThatThrownBy(() -> chargeService.obtenirCharge(null))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("obligatoire");
    }

    private Charge creerChargeTest() {
        return Charge.builder()
                .titre("Charge test")
                .description("Description test")
                .montant(200.0)
                .idCabinet(1L)
                .date(LocalDateTime.now())
                .build();
    }

    private static class MockChargeRepository implements ChargeRepository {
        private final List<Charge> charges = new ArrayList<>();
        private Long nextId = 1L;

        @Override
        public List<Charge> findAll() {
            return new ArrayList<>(charges);
        }

        @Override
        public Charge findById(Long id) {
            return charges.stream()
                    .filter(c -> c.getIdCharge().equals(id))
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public void create(Charge charge) {
            charge.setIdCharge(nextId++);
            charges.add(charge);
        }

        @Override
        public void update(Charge charge) {
            charges.removeIf(c -> c.getIdCharge().equals(charge.getIdCharge()));
            charges.add(charge);
        }

        @Override
        public void delete(Charge charge) {
            charges.remove(charge);
        }

        @Override
        public void deleteById(Long id) {
            charges.removeIf(c -> c.getIdCharge().equals(id));
        }

        @Override
        public List<Charge> findByCabinetId(Long idCabinet) {
            return charges.stream()
                    .filter(c -> c.getIdCabinet().equals(idCabinet))
                    .toList();
        }

        @Override
        public List<Charge> findByDateBetween(LocalDateTime debut, LocalDateTime fin) {
            return charges.stream()
                    .filter(c -> c.getDate() != null &&
                            !c.getDate().isBefore(debut) &&
                            !c.getDate().isAfter(fin))
                    .toList();
        }

        @Override
        public List<Charge> findByTitreContaining(String titre) {
            return charges.stream()
                    .filter(c -> c.getTitre() != null && c.getTitre().contains(titre))
                    .toList();
        }

        @Override
        public Double calculateTotalCharges(Long idCabinet) {
            return charges.stream()
                    .filter(c -> c.getIdCabinet().equals(idCabinet))
                    .mapToDouble(Charge::getMontant)
                    .sum();
        }

        @Override
        public Double calculateTotalChargesBetween(Long idCabinet, LocalDateTime debut, LocalDateTime fin) {
            return charges.stream()
                    .filter(c -> c.getIdCabinet().equals(idCabinet) &&
                            c.getDate() != null &&
                            !c.getDate().isBefore(debut) &&
                            !c.getDate().isAfter(fin))
                    .mapToDouble(Charge::getMontant)
                    .sum();
        }
    }
}
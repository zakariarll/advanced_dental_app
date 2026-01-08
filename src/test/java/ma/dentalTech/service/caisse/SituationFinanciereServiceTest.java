package ma.dentalTech.service.caisse;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.caisse.Facture;
import ma.dentalTech.entities.caisse.SituationFinanciere;
import ma.dentalTech.repository.modules.caisse.api.FactureRepository;
import ma.dentalTech.repository.modules.caisse.api.SituationFinanciereRepository;
import ma.dentalTech.service.modules.caisse.impl.SituationFinanciereServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class SituationFinanciereServiceTest {

    private SituationFinanciereServiceImpl situationService;
    private MockSituationFinanciereRepository mockSituationRepository;
    private MockFactureRepository mockFactureRepository;

    @BeforeEach
    void setUp() {
        mockSituationRepository = new MockSituationFinanciereRepository();
        mockFactureRepository = new MockFactureRepository();
        situationService = new SituationFinanciereServiceImpl(mockSituationRepository, mockFactureRepository);
    }

    @Test
    @DisplayName("Créer une situation financière valide")
    void creerSituationValide() throws ServiceException, ValidationException {
        SituationFinanciere situation = SituationFinanciere.builder()
                .idFacture(1L)
                .totaleDesActes(1000.0)
                .totalePaye(0.0)
                .build();

        SituationFinanciere result = situationService.creerSituationFinanciere(situation);

        assertThat(result).isNotNull();
        assertThat(result.getIdSF()).isNotNull();
        assertThat(result.getStatut()).isEqualTo("EN_COURS");
        assertThat(result.getCredit()).isEqualTo(1000.0);
    }

    @Test
    @DisplayName("Créer une situation sans facture doit échouer")
    void creerSituationSansFacture() {
        SituationFinanciere situation = SituationFinanciere.builder()
                .totaleDesActes(1000.0)
                .totalePaye(0.0)
                .build();

        assertThatThrownBy(() -> situationService.creerSituationFinanciere(situation))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("facture");
    }

    @Test
    @DisplayName("Créer une situation sans total des actes doit échouer")
    void creerSituationSansTotalActes() {
        SituationFinanciere situation = SituationFinanciere.builder()
                .idFacture(1L)
                .totalePaye(0.0)
                .build();

        assertThatThrownBy(() -> situationService.creerSituationFinanciere(situation))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("total des actes");
    }

    @Test
    @DisplayName("Créer une situation avec total négatif doit échouer")
    void creerSituationTotalNegatif() {
        SituationFinanciere situation = SituationFinanciere.builder()
                .idFacture(1L)
                .totaleDesActes(-100.0)
                .totalePaye(0.0)
                .build();

        assertThatThrownBy(() -> situationService.creerSituationFinanciere(situation))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("négatif");
    }

    @Test
    @DisplayName("Modifier une situation financière valide")
    void modifierSituationValide() throws ServiceException, ValidationException {
        SituationFinanciere situation = creerSituationTest();
        situationService.creerSituationFinanciere(situation);

        situation.setTotalePaye(500.0);
        SituationFinanciere result = situationService.modifierSituationFinanciere(situation);

        assertThat(result.getTotalePaye()).isEqualTo(500.0);
        assertThat(result.getCredit()).isEqualTo(500.0);
    }

    @Test
    @DisplayName("Modifier une situation inexistante doit échouer")
    void modifierSituationInexistante() {
        SituationFinanciere situation = SituationFinanciere.builder()
                .idSF(999L)
                .idFacture(1L)
                .totaleDesActes(1000.0)
                .totalePaye(0.0)
                .build();

        assertThatThrownBy(() -> situationService.modifierSituationFinanciere(situation))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("introuvable");
    }

    @Test
    @DisplayName("Supprimer une situation valide")
    void supprimerSituationValide() throws ServiceException, ValidationException {
        SituationFinanciere situation = creerSituationTest();
        situationService.creerSituationFinanciere(situation);

        situationService.supprimerSituationFinanciere(situation.getIdSF());

        assertThat(situationService.obtenirSituationFinanciere(situation.getIdSF())).isNull();
    }

    @Test
    @DisplayName("Supprimer une situation inexistante doit échouer")
    void supprimerSituationInexistante() {
        assertThatThrownBy(() -> situationService.supprimerSituationFinanciere(999L))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("introuvable");
    }

    @Test
    @DisplayName("Réinitialiser une situation")
    void reinitialiserSituation() throws ServiceException, ValidationException {
        SituationFinanciere situation = creerSituationTest();
        situation.setTotalePaye(500.0);
        situation.setCredit(500.0);
        situation.setStatut("EN_CREDIT");
        situation.setEnPromo("Promo été -10%");
        mockSituationRepository.create(situation);

        SituationFinanciere result = situationService.reinitialiserSituation(situation.getIdSF());

        assertThat(result.getTotalePaye()).isEqualTo(0.0);
        assertThat(result.getCredit()).isEqualTo(1000.0);
        assertThat(result.getStatut()).isEqualTo("EN_COURS");
        assertThat(result.getEnPromo()).isNull();
    }

    @Test
    @DisplayName("Réinitialiser une situation inexistante doit échouer")
    void reinitialiserSituationInexistante() {
        assertThatThrownBy(() -> situationService.reinitialiserSituation(999L))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("introuvable");
    }

    @Test
    @DisplayName("Appliquer une promotion valide")
    void appliquerPromotionValide() throws ServiceException, ValidationException {
        SituationFinanciere situation = creerSituationTest();
        situationService.creerSituationFinanciere(situation);

        SituationFinanciere result = situationService.appliquerPromotion(situation.getIdSF(), "Promo été", 10.0);

        assertThat(result.getTotaleDesActes()).isEqualTo(900.0);
        assertThat(result.getEnPromo()).contains("Promo été");
        assertThat(result.getEnPromo()).contains("-10.0%");
    }

    @Test
    @DisplayName("Appliquer une promotion sur situation avec promo existante doit échouer")
    void appliquerPromotionExistante() throws ServiceException, ValidationException {
        SituationFinanciere situation = creerSituationTest();
        situation.setEnPromo("Promo existante");
        mockSituationRepository.create(situation);

        assertThatThrownBy(() -> situationService.appliquerPromotion(situation.getIdSF(), "Nouvelle promo", 10.0))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("déjà appliquée");
    }

    @Test
    @DisplayName("Appliquer une promotion avec pourcentage invalide doit échouer")
    void appliquerPromotionPourcentageInvalide() throws ServiceException, ValidationException {
        SituationFinanciere situation = creerSituationTest();
        situationService.creerSituationFinanciere(situation);

        assertThatThrownBy(() -> situationService.appliquerPromotion(situation.getIdSF(), "Promo", 150.0))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("entre 0 et 100");
    }

    @Test
    @DisplayName("Appliquer une promotion avec pourcentage négatif doit échouer")
    void appliquerPromotionPourcentageNegatif() throws ServiceException, ValidationException {
        SituationFinanciere situation = creerSituationTest();
        situationService.creerSituationFinanciere(situation);

        assertThatThrownBy(() -> situationService.appliquerPromotion(situation.getIdSF(), "Promo", -10.0))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("entre 0 et 100");
    }

    @Test
    @DisplayName("Appliquer une promotion sans nom doit échouer")
    void appliquerPromotionSansNom() throws ServiceException, ValidationException {
        SituationFinanciere situation = creerSituationTest();
        situationService.creerSituationFinanciere(situation);

        assertThatThrownBy(() -> situationService.appliquerPromotion(situation.getIdSF(), "", 10.0))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("nom de la promotion");
    }

    @Test
    @DisplayName("Lister les situations avec crédit")
    void listerSituationsAvecCredit() throws ServiceException, ValidationException {
        SituationFinanciere s1 = creerSituationTest();
        s1.setCredit(500.0);
        mockSituationRepository.create(s1);

        SituationFinanciere s2 = creerSituationTest();
        s2.setIdFacture(2L);
        s2.setCredit(300.0);
        mockSituationRepository.create(s2);

        SituationFinanciere s3 = creerSituationTest();
        s3.setIdFacture(3L);
        s3.setCredit(0.0);
        mockSituationRepository.create(s3);

        List<SituationFinanciere> result = situationService.listerSituationsAvecCredit();

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(s -> s.getCredit() > 0);
    }

    @Test
    @DisplayName("Lister les situations par statut")
    void listerSituationsParStatut() throws ServiceException, ValidationException {
        SituationFinanciere s1 = creerSituationTest();
        s1.setStatut("EN_CREDIT");
        mockSituationRepository.create(s1);

        SituationFinanciere s2 = creerSituationTest();
        s2.setIdFacture(2L);
        s2.setStatut("EN_CREDIT");
        mockSituationRepository.create(s2);

        SituationFinanciere s3 = creerSituationTest();
        s3.setIdFacture(3L);
        s3.setStatut("SOLDE");
        mockSituationRepository.create(s3);

        List<SituationFinanciere> result = situationService.listerSituationsParStatut("EN_CREDIT");

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(s -> "EN_CREDIT".equals(s.getStatut()));
    }

    @Test
    @DisplayName("Calculer le total des crédits")
    void calculerTotalCredits() throws ServiceException, ValidationException {
        SituationFinanciere s1 = creerSituationTest();
        s1.setCredit(500.0);
        mockSituationRepository.create(s1);

        SituationFinanciere s2 = creerSituationTest();
        s2.setIdFacture(2L);
        s2.setCredit(300.0);
        mockSituationRepository.create(s2);

        Double result = situationService.calculerTotalCredits();

        assertThat(result).isEqualTo(800.0);
    }

    @Test
    @DisplayName("Mettre à jour situation depuis facture - création")
    void mettreAJourDepuisFactureCreation() throws ServiceException, ValidationException {
        Facture facture = Facture.builder()
                .idFacture(1L)
                .idConsultation(1L)
                .totaleFacture(1000.0)
                .totalePaye(400.0)
                .reste(600.0)
                .statut("PARTIELLEMENT_PAYEE")
                .dateFacture(LocalDateTime.now())
                .build();
        mockFactureRepository.create(facture);

        SituationFinanciere result = situationService.mettreAJourDepuisFacture(1L);

        assertThat(result).isNotNull();
        assertThat(result.getTotaleDesActes()).isEqualTo(1000.0);
        assertThat(result.getTotalePaye()).isEqualTo(400.0);
        assertThat(result.getCredit()).isEqualTo(600.0);
        assertThat(result.getStatut()).isEqualTo("EN_CREDIT");
    }

    @Test
    @DisplayName("Mettre à jour situation depuis facture - mise à jour")
    void mettreAJourDepuisFactureMiseAJour() throws ServiceException, ValidationException {
        Facture facture = Facture.builder()
                .idFacture(1L)
                .idConsultation(1L)
                .totaleFacture(1000.0)
                .totalePaye(1000.0)
                .reste(0.0)
                .statut("PAYEE")
                .dateFacture(LocalDateTime.now())
                .build();
        mockFactureRepository.create(facture);

        SituationFinanciere situation = creerSituationTest();
        mockSituationRepository.create(situation);

        SituationFinanciere result = situationService.mettreAJourDepuisFacture(1L);

        assertThat(result.getTotalePaye()).isEqualTo(1000.0);
        assertThat(result.getCredit()).isEqualTo(0.0);
        assertThat(result.getStatut()).isEqualTo("SOLDE");
    }

    @Test
    @DisplayName("Mettre à jour depuis facture inexistante doit échouer")
    void mettreAJourDepuisFactureInexistante() {
        assertThatThrownBy(() -> situationService.mettreAJourDepuisFacture(999L))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("introuvable");
    }

    @Test
    @DisplayName("Obtenir situation par facture")
    void obtenirSituationParFacture() throws ServiceException, ValidationException {
        SituationFinanciere situation = creerSituationTest();
        situation.setIdFacture(100L);
        situationService.creerSituationFinanciere(situation);

        SituationFinanciere result = situationService.obtenirSituationParFacture(100L);

        assertThat(result).isNotNull();
        assertThat(result.getIdFacture()).isEqualTo(100L);
    }

    @Test
    @DisplayName("Créer une situation avec statut invalide doit échouer")
    void creerSituationStatutInvalide() {
        SituationFinanciere situation = SituationFinanciere.builder()
                .idFacture(1L)
                .totaleDesActes(1000.0)
                .totalePaye(0.0)
                .statut("STATUT_INVALIDE")
                .build();

        assertThatThrownBy(() -> situationService.creerSituationFinanciere(situation))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Statut invalide");
    }

    @Test
    @DisplayName("Lister toutes les situations")
    void listerToutesLesSituations() throws ServiceException, ValidationException {
        mockSituationRepository.create(creerSituationTest());

        SituationFinanciere s2 = creerSituationTest();
        s2.setIdFacture(2L);
        mockSituationRepository.create(s2);

        SituationFinanciere s3 = creerSituationTest();
        s3.setIdFacture(3L);
        mockSituationRepository.create(s3);

        List<SituationFinanciere> result = situationService.listerToutesLesSituations();

        assertThat(result).hasSize(3);
    }

    private SituationFinanciere creerSituationTest() {
        return SituationFinanciere.builder()
                .idFacture(1L)
                .totaleDesActes(1000.0)
                .totalePaye(0.0)
                .credit(1000.0)
                .statut("EN_COURS")
                .build();
    }

    private static class MockSituationFinanciereRepository implements SituationFinanciereRepository {
        private final List<SituationFinanciere> situations = new ArrayList<>();
        private Long nextId = 1L;

        @Override
        public List<SituationFinanciere> findAll() {
            return new ArrayList<>(situations);
        }

        @Override
        public SituationFinanciere findById(Long id) {
            return situations.stream()
                    .filter(s -> s.getIdSF().equals(id))
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public void create(SituationFinanciere situation) {
            situation.setIdSF(nextId++);
            situations.add(situation);
        }

        @Override
        public void update(SituationFinanciere situation) {
            situations.removeIf(s -> s.getIdSF().equals(situation.getIdSF()));
            situations.add(situation);
        }

        @Override
        public void delete(SituationFinanciere situation) {
            situations.remove(situation);
        }

        @Override
        public void deleteById(Long id) {
            situations.removeIf(s -> s.getIdSF().equals(id));
        }

        @Override
        public Optional<SituationFinanciere> findByFactureId(Long idFacture) {
            return situations.stream()
                    .filter(s -> s.getIdFacture().equals(idFacture))
                    .findFirst();
        }

        @Override
        public List<SituationFinanciere> findByStatut(String statut) {
            return situations.stream()
                    .filter(s -> statut.equals(s.getStatut()))
                    .toList();
        }

        @Override
        public List<SituationFinanciere> findByPatientId(Long idPatient) {
            return new ArrayList<>();
        }

        @Override
        public List<SituationFinanciere> findWithCredit() {
            return situations.stream()
                    .filter(s -> s.getCredit() != null && s.getCredit() > 0)
                    .toList();
        }

        @Override
        public Double calculateTotalCredit() {
            return situations.stream()
                    .filter(s -> s.getCredit() != null && s.getCredit() > 0)
                    .mapToDouble(SituationFinanciere::getCredit)
                    .sum();
        }

        @Override
        public Double calculateTotalCreditsPatient(Long idPatient) {
            return 0.0;
        }
    }

    private static class MockFactureRepository implements FactureRepository {
        private final List<Facture> factures = new ArrayList<>();
        private Long nextId = 1L;

        @Override
        public List<Facture> findAll() {
            return new ArrayList<>(factures);
        }

        @Override
        public Facture findById(Long id) {
            return factures.stream()
                    .filter(f -> f.getIdFacture().equals(id))
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public void create(Facture facture) {
            if (facture.getIdFacture() == null) {
                facture.setIdFacture(nextId++);
            }
            factures.add(facture);
        }

        @Override
        public void update(Facture facture) {
            factures.removeIf(f -> f.getIdFacture().equals(facture.getIdFacture()));
            factures.add(facture);
        }

        @Override
        public void delete(Facture facture) {
            factures.remove(facture);
        }

        @Override
        public void deleteById(Long id) {
            factures.removeIf(f -> f.getIdFacture().equals(id));
        }

        @Override
        public Optional<Facture> findByConsultationId(Long idConsultation) {
            return Optional.empty();
        }

        @Override
        public List<Facture> findByStatut(String statut) {
            return new ArrayList<>();
        }

        @Override
        public List<Facture> findByDateBetween(LocalDateTime debut, LocalDateTime fin) {
            return new ArrayList<>();
        }

        @Override
        public List<Facture> findFacturesImpayees() {
            return new ArrayList<>();
        }

        @Override
        public List<Facture> findByPatientId(Long idPatient) {
            return new ArrayList<>();
        }

        @Override
        public Double calculateTotalFactures(LocalDateTime debut, LocalDateTime fin) {
            return 0.0;
        }

        @Override
        public Double calculateTotalPaye(LocalDateTime debut, LocalDateTime fin) {
            return 0.0;
        }

        @Override
        public Double calculateTotalImpaye() {
            return 0.0;
        }
    }
}
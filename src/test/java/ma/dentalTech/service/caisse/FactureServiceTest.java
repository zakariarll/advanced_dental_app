package ma.dentalTech.service.caisse;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.caisse.Facture;
import ma.dentalTech.entities.dossierMedical.InterventionMedecin;
import ma.dentalTech.repository.modules.caisse.api.FactureRepository;
import ma.dentalTech.repository.modules.dossierMedical.api.InterventionMedecinRepository;
import ma.dentalTech.service.modules.caisse.impl.FactureServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class FactureServiceTest {

    private FactureServiceImpl factureService;
    private MockFactureRepository mockFactureRepository;
    private MockInterventionMedecinRepository mockInterventionRepository;

    @BeforeEach
    void setUp() {
        mockFactureRepository = new MockFactureRepository();
        mockInterventionRepository = new MockInterventionMedecinRepository();
        factureService = new FactureServiceImpl(mockFactureRepository, mockInterventionRepository);
    }

    @Test
    @DisplayName("Créer une facture valide")
    void creerFactureValide() throws ServiceException, ValidationException {
        Facture facture = Facture.builder()
                .idConsultation(1L)
                .totaleFacture(1000.0)
                .totalePaye(0.0)
                .build();

        Facture result = factureService.creerFacture(facture);

        assertThat(result).isNotNull();
        assertThat(result.getIdFacture()).isNotNull();
        assertThat(result.getStatut()).isEqualTo("EN_ATTENTE");
        assertThat(result.getReste()).isEqualTo(1000.0);
        assertThat(result.getDateFacture()).isNotNull();
    }

    @Test
    @DisplayName("Créer une facture sans consultation doit échouer")
    void creerFactureSansConsultation() {
        Facture facture = Facture.builder()
                .totaleFacture(1000.0)
                .totalePaye(0.0)
                .build();

        assertThatThrownBy(() -> factureService.creerFacture(facture))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("consultation");
    }

    @Test
    @DisplayName("Créer une facture sans total doit échouer")
    void creerFactureSansTotal() {
        Facture facture = Facture.builder()
                .idConsultation(1L)
                .build();

        assertThatThrownBy(() -> factureService.creerFacture(facture))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("total");
    }

    @Test
    @DisplayName("Créer une facture avec total négatif doit échouer")
    void creerFactureTotalNegatif() {
        Facture facture = Facture.builder()
                .idConsultation(1L)
                .totaleFacture(-100.0)
                .build();

        assertThatThrownBy(() -> factureService.creerFacture(facture))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("négatif");
    }

    @Test
    @DisplayName("Créer une facture avec total payé supérieur au total doit échouer")
    void creerFactureTotalPayeSuperieur() {
        Facture facture = Facture.builder()
                .idConsultation(1L)
                .totaleFacture(100.0)
                .totalePaye(200.0)
                .build();

        assertThatThrownBy(() -> factureService.creerFacture(facture))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("dépasser");
    }

    @Test
    @DisplayName("Modifier une facture valide")
    void modifierFactureValide() throws ServiceException, ValidationException {
        Facture facture = creerFactureTest();
        factureService.creerFacture(facture);

        facture.setTotaleFacture(1500.0);
        Facture result = factureService.modifierFacture(facture);

        assertThat(result.getTotaleFacture()).isEqualTo(1500.0);
        assertThat(result.getReste()).isEqualTo(1500.0);
    }

    @Test
    @DisplayName("Modifier une facture inexistante doit échouer")
    void modifierFactureInexistante() {
        Facture facture = Facture.builder()
                .idFacture(999L)
                .idConsultation(1L)
                .totaleFacture(1000.0)
                .totalePaye(0.0)
                .dateFacture(LocalDateTime.now())
                .build();

        assertThatThrownBy(() -> factureService.modifierFacture(facture))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("introuvable");
    }

    @Test
    @DisplayName("Modifier une facture annulée doit échouer")
    void modifierFactureAnnulee() throws ServiceException, ValidationException {
        Facture facture = creerFactureTest();
        facture.setStatut("ANNULEE");
        mockFactureRepository.create(facture);

        facture.setTotaleFacture(2000.0);

        assertThatThrownBy(() -> factureService.modifierFacture(facture))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("annulée");
    }

    @Test
    @DisplayName("Supprimer une facture valide")
    void supprimerFactureValide() throws ServiceException, ValidationException {
        Facture facture = creerFactureTest();
        factureService.creerFacture(facture);

        factureService.supprimerFacture(facture.getIdFacture());

        assertThat(factureService.obtenirFacture(facture.getIdFacture())).isNull();
    }

    @Test
    @DisplayName("Supprimer une facture payée doit échouer")
    void supprimerFacturePayee() throws ServiceException, ValidationException {
        Facture facture = creerFactureTest();
        facture.setStatut("PAYEE");
        mockFactureRepository.create(facture);

        assertThatThrownBy(() -> factureService.supprimerFacture(facture.getIdFacture()))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("payée");
    }

    @Test
    @DisplayName("Supprimer une facture inexistante doit échouer")
    void supprimerFactureInexistante() {
        assertThatThrownBy(() -> factureService.supprimerFacture(999L))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("introuvable");
    }

    @Test
    @DisplayName("Enregistrer un paiement valide")
    void enregistrerPaiementValide() throws ServiceException, ValidationException {
        Facture facture = creerFactureTest();
        factureService.creerFacture(facture);

        Facture result = factureService.enregistrerPaiement(facture.getIdFacture(), 500.0);

        assertThat(result.getTotalePaye()).isEqualTo(500.0);
        assertThat(result.getReste()).isEqualTo(500.0);
        assertThat(result.getStatut()).isEqualTo("PARTIELLEMENT_PAYEE");
    }

    @Test
    @DisplayName("Enregistrer un paiement complet")
    void enregistrerPaiementComplet() throws ServiceException, ValidationException {
        Facture facture = creerFactureTest();
        factureService.creerFacture(facture);

        Facture result = factureService.enregistrerPaiement(facture.getIdFacture(), 1000.0);

        assertThat(result.getTotalePaye()).isEqualTo(1000.0);
        assertThat(result.getReste()).isEqualTo(0.0);
        assertThat(result.getStatut()).isEqualTo("PAYEE");
    }

    @Test
    @DisplayName("Enregistrer un paiement supérieur au reste doit échouer")
    void enregistrerPaiementSuperieurReste() throws ServiceException, ValidationException {
        Facture facture = creerFactureTest();
        factureService.creerFacture(facture);

        assertThatThrownBy(() -> factureService.enregistrerPaiement(facture.getIdFacture(), 1500.0))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("dépasse");
    }

    @Test
    @DisplayName("Enregistrer un paiement négatif doit échouer")
    void enregistrerPaiementNegatif() throws ServiceException, ValidationException {
        Facture facture = creerFactureTest();
        factureService.creerFacture(facture);

        assertThatThrownBy(() -> factureService.enregistrerPaiement(facture.getIdFacture(), -100.0))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("supérieur à 0");
    }

    @Test
    @DisplayName("Enregistrer un paiement sur facture annulée doit échouer")
    void enregistrerPaiementFactureAnnulee() throws ServiceException, ValidationException {
        Facture facture = creerFactureTest();
        facture.setStatut("ANNULEE");
        mockFactureRepository.create(facture);

        assertThatThrownBy(() -> factureService.enregistrerPaiement(facture.getIdFacture(), 500.0))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("annulée");
    }

    @Test
    @DisplayName("Enregistrer un paiement sur facture déjà payée doit échouer")
    void enregistrerPaiementFacturePayee() throws ServiceException, ValidationException {
        Facture facture = creerFactureTest();
        facture.setStatut("PAYEE");
        facture.setTotalePaye(1000.0);
        facture.setReste(0.0);
        mockFactureRepository.create(facture);

        assertThatThrownBy(() -> factureService.enregistrerPaiement(facture.getIdFacture(), 100.0))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("déjà entièrement payée");
    }

    @Test
    @DisplayName("Générer une facture depuis consultation")
    void genererFactureDepuisConsultation() throws ServiceException, ValidationException {
        InterventionMedecin im1 = InterventionMedecin.builder()
                .idIM(1L)
                .idConsultation(1L)
                .idMedecin(1L)
                .prixDePatient(500.0)
                .build();
        InterventionMedecin im2 = InterventionMedecin.builder()
                .idIM(2L)
                .idConsultation(1L)
                .idMedecin(1L)
                .prixDePatient(300.0)
                .build();
        mockInterventionRepository.create(im1);
        mockInterventionRepository.create(im2);

        Facture result = factureService.genererFactureDepuisConsultation(1L);

        assertThat(result).isNotNull();
        assertThat(result.getTotaleFacture()).isEqualTo(800.0);
        assertThat(result.getTotalePaye()).isEqualTo(0.0);
        assertThat(result.getReste()).isEqualTo(800.0);
        assertThat(result.getStatut()).isEqualTo("EN_ATTENTE");
    }

    @Test
    @DisplayName("Générer une facture pour consultation avec facture existante doit échouer")
    void genererFactureConsultationExistante() throws ServiceException, ValidationException {
        Facture facture = creerFactureTest();
        factureService.creerFacture(facture);

        assertThatThrownBy(() -> factureService.genererFactureDepuisConsultation(1L))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("existe déjà");
    }

    @Test
    @DisplayName("Annuler une facture valide")
    void annulerFactureValide() throws ServiceException, ValidationException {
        Facture facture = creerFactureTest();
        factureService.creerFacture(facture);

        factureService.annulerFacture(facture.getIdFacture());

        Facture result = factureService.obtenirFacture(facture.getIdFacture());
        assertThat(result.getStatut()).isEqualTo("ANNULEE");
    }

    @Test
    @DisplayName("Annuler une facture déjà annulée doit échouer")
    void annulerFactureDejaAnnulee() throws ServiceException, ValidationException {
        Facture facture = creerFactureTest();
        facture.setStatut("ANNULEE");
        mockFactureRepository.create(facture);

        assertThatThrownBy(() -> factureService.annulerFacture(facture.getIdFacture()))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("déjà annulée");
    }

    @Test
    @DisplayName("Annuler une facture payée doit échouer")
    void annulerFacturePayee() throws ServiceException, ValidationException {
        Facture facture = creerFactureTest();
        facture.setStatut("PAYEE");
        mockFactureRepository.create(facture);

        assertThatThrownBy(() -> factureService.annulerFacture(facture.getIdFacture()))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("payée");
    }

    @Test
    @DisplayName("Lister les factures par statut")
    void listerFacturesParStatut() throws ServiceException, ValidationException {
        Facture f1 = creerFactureTest();
        f1.setStatut("EN_ATTENTE");
        mockFactureRepository.create(f1);

        Facture f2 = creerFactureTest();
        f2.setIdConsultation(2L);
        f2.setStatut("EN_ATTENTE");
        mockFactureRepository.create(f2);

        Facture f3 = creerFactureTest();
        f3.setIdConsultation(3L);
        f3.setStatut("PAYEE");
        mockFactureRepository.create(f3);

        List<Facture> result = factureService.listerFacturesParStatut("EN_ATTENTE");

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(f -> "EN_ATTENTE".equals(f.getStatut()));
    }

    @Test
    @DisplayName("Lister les factures impayées")
    void listerFacturesImpayees() throws ServiceException, ValidationException {
        Facture f1 = creerFactureTest();
        f1.setReste(500.0);
        mockFactureRepository.create(f1);

        Facture f2 = creerFactureTest();
        f2.setIdConsultation(2L);
        f2.setReste(300.0);
        mockFactureRepository.create(f2);

        Facture f3 = creerFactureTest();
        f3.setIdConsultation(3L);
        f3.setReste(0.0);
        mockFactureRepository.create(f3);

        List<Facture> result = factureService.listerFacturesImpayees();

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(f -> f.getReste() > 0);
    }

    @Test
    @DisplayName("Lister les factures par période")
    void listerFacturesParPeriode() throws ServiceException, ValidationException {
        LocalDateTime now = LocalDateTime.now();

        Facture f1 = creerFactureTest();
        f1.setDateFacture(now.minusDays(1));
        mockFactureRepository.create(f1);

        Facture f2 = creerFactureTest();
        f2.setIdConsultation(2L);
        f2.setDateFacture(now);
        mockFactureRepository.create(f2);

        Facture f3 = creerFactureTest();
        f3.setIdConsultation(3L);
        f3.setDateFacture(now.minusDays(10));
        mockFactureRepository.create(f3);

        List<Facture> result = factureService.listerFacturesParPeriode(now.minusDays(5), now.plusDays(1));

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Lister les factures par période avec dates inversées doit échouer")
    void listerFacturesParPeriodeDatesInversees() {
        LocalDateTime now = LocalDateTime.now();

        assertThatThrownBy(() -> factureService.listerFacturesParPeriode(now, now.minusDays(5)))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("antérieure");
    }

    @Test
    @DisplayName("Calculer le total des factures")
    void calculerTotalFactures() throws ServiceException, ValidationException {
        LocalDateTime now = LocalDateTime.now();

        Facture f1 = creerFactureTest();
        f1.setTotaleFacture(1000.0);
        f1.setDateFacture(now);
        mockFactureRepository.create(f1);

        Facture f2 = creerFactureTest();
        f2.setIdConsultation(2L);
        f2.setTotaleFacture(500.0);
        f2.setDateFacture(now);
        mockFactureRepository.create(f2);

        Double result = factureService.calculerTotalFactures(now.minusDays(1), now.plusDays(1));

        assertThat(result).isEqualTo(1500.0);
    }

    @Test
    @DisplayName("Calculer le total payé")
    void calculerTotalPaye() throws ServiceException, ValidationException {
        LocalDateTime now = LocalDateTime.now();

        Facture f1 = creerFactureTest();
        f1.setTotalePaye(800.0);
        f1.setDateFacture(now);
        mockFactureRepository.create(f1);

        Facture f2 = creerFactureTest();
        f2.setIdConsultation(2L);
        f2.setTotalePaye(400.0);
        f2.setDateFacture(now);
        mockFactureRepository.create(f2);

        Double result = factureService.calculerTotalPaye(now.minusDays(1), now.plusDays(1));

        assertThat(result).isEqualTo(1200.0);
    }

    @Test
    @DisplayName("Calculer le total impayé")
    void calculerTotalImpaye() throws ServiceException, ValidationException {
        Facture f1 = creerFactureTest();
        f1.setReste(200.0);
        mockFactureRepository.create(f1);

        Facture f2 = creerFactureTest();
        f2.setIdConsultation(2L);
        f2.setReste(300.0);
        mockFactureRepository.create(f2);

        Double result = factureService.calculerTotalImpaye();

        assertThat(result).isEqualTo(500.0);
    }

    @Test
    @DisplayName("Obtenir facture par consultation")
    void obtenirFactureParConsultation() throws ServiceException, ValidationException {
        Facture facture = creerFactureTest();
        facture.setIdConsultation(100L);
        factureService.creerFacture(facture);

        Facture result = factureService.obtenirFactureParConsultation(100L);

        assertThat(result).isNotNull();
        assertThat(result.getIdConsultation()).isEqualTo(100L);
    }

    @Test
    @DisplayName("Créer une facture avec statut invalide doit échouer")
    void creerFactureStatutInvalide() {
        Facture facture = Facture.builder()
                .idConsultation(1L)
                .totaleFacture(1000.0)
                .totalePaye(0.0)
                .statut("STATUT_INVALIDE")
                .build();

        assertThatThrownBy(() -> factureService.creerFacture(facture))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Statut invalide");
    }

    private Facture creerFactureTest() {
        return Facture.builder()
                .idConsultation(1L)
                .totaleFacture(1000.0)
                .totalePaye(0.0)
                .reste(1000.0)
                .statut("EN_ATTENTE")
                .dateFacture(LocalDateTime.now())
                .build();
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
            facture.setIdFacture(nextId++);
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
            return factures.stream()
                    .filter(f -> f.getIdConsultation().equals(idConsultation))
                    .findFirst();
        }

        @Override
        public List<Facture> findByStatut(String statut) {
            return factures.stream()
                    .filter(f -> statut.equals(f.getStatut()))
                    .toList();
        }

        @Override
        public List<Facture> findByDateBetween(LocalDateTime debut, LocalDateTime fin) {
            return factures.stream()
                    .filter(f -> f.getDateFacture() != null &&
                            !f.getDateFacture().isBefore(debut) &&
                            !f.getDateFacture().isAfter(fin))
                    .toList();
        }

        @Override
        public List<Facture> findFacturesImpayees() {
            return factures.stream()
                    .filter(f -> f.getReste() != null && f.getReste() > 0)
                    .toList();
        }

        @Override
        public List<Facture> findByPatientId(Long idPatient) {
            return new ArrayList<>();
        }

        @Override
        public Double calculateTotalFactures(LocalDateTime debut, LocalDateTime fin) {
            return factures.stream()
                    .filter(f -> f.getDateFacture() != null &&
                            !f.getDateFacture().isBefore(debut) &&
                            !f.getDateFacture().isAfter(fin))
                    .mapToDouble(f -> f.getTotaleFacture() != null ? f.getTotaleFacture() : 0.0)
                    .sum();
        }

        @Override
        public Double calculateTotalPaye(LocalDateTime debut, LocalDateTime fin) {
            return factures.stream()
                    .filter(f -> f.getDateFacture() != null &&
                            !f.getDateFacture().isBefore(debut) &&
                            !f.getDateFacture().isAfter(fin))
                    .mapToDouble(f -> f.getTotalePaye() != null ? f.getTotalePaye() : 0.0)
                    .sum();
        }

        @Override
        public Double calculateTotalImpaye() {
            return factures.stream()
                    .filter(f -> f.getReste() != null && f.getReste() > 0)
                    .mapToDouble(Facture::getReste)
                    .sum();
        }
    }

    private static class MockInterventionMedecinRepository implements InterventionMedecinRepository {
        private final List<InterventionMedecin> interventions = new ArrayList<>();
        private Long nextId = 1L;

        @Override
        public List<InterventionMedecin> findAll() {
            return new ArrayList<>(interventions);
        }

        @Override
        public InterventionMedecin findById(Long id) {
            return interventions.stream()
                    .filter(i -> i.getIdIM().equals(id))
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public void create(InterventionMedecin im) {
            if (im.getIdIM() == null) {
                im.setIdIM(nextId++);
            }
            interventions.add(im);
        }

        @Override
        public void update(InterventionMedecin im) {
            interventions.removeIf(i -> i.getIdIM().equals(im.getIdIM()));
            interventions.add(im);
        }

        @Override
        public void delete(InterventionMedecin im) {
            interventions.remove(im);
        }

        @Override
        public void deleteById(Long id) {
            interventions.removeIf(i -> i.getIdIM().equals(id));
        }

        @Override
        public List<InterventionMedecin> findByConsultationId(Long idConsultation) {
            return interventions.stream()
                    .filter(i -> i.getIdConsultation().equals(idConsultation))
                    .toList();
        }
    }
}
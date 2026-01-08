package ma.dentalTech.service.dossierMedical;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.dossierMedical.InterventionMedecin;
import ma.dentalTech.repository.modules.dossierMedical.api.InterventionMedecinRepository;
import ma.dentalTech.service.modules.dossierMedical.impl.InterventionMedecinServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class InterventionMedecinServiceTest {

    private InterventionMedecinServiceImpl interventionService;
    private MockInterventionRepository mockRepository;

    @BeforeEach
    void setUp() {
        mockRepository = new MockInterventionRepository();
        interventionService = new InterventionMedecinServiceImpl(mockRepository);
    }

    @Test
    @DisplayName("Créer une intervention valide")
    void creerInterventionValide() throws ServiceException, ValidationException {
        InterventionMedecin intervention = InterventionMedecin.builder()
                .idConsultation(1L)
                .idMedecin(1L)
                .prixDePatient(500.0)
                .build();

        InterventionMedecin result = interventionService.creerIntervention(intervention);

        assertThat(result).isNotNull();
        assertThat(result.getIdIM()).isEqualTo(1L);
        assertThat(result.getPrixDePatient()).isEqualTo(500.0);
    }

    @Test
    @DisplayName("Créer une intervention sans consultation doit échouer")
    void creerInterventionSansConsultation() {
        InterventionMedecin intervention = InterventionMedecin.builder()
                .idMedecin(1L)
                .prixDePatient(500.0)
                .build();

        assertThatThrownBy(() -> interventionService.creerIntervention(intervention))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("consultation associée est obligatoire");
    }

    @Test
    @DisplayName("Créer une intervention sans médecin doit échouer")
    void creerInterventionSansMedecin() {
        InterventionMedecin intervention = InterventionMedecin.builder()
                .idConsultation(1L)
                .prixDePatient(500.0)
                .build();

        assertThatThrownBy(() -> interventionService.creerIntervention(intervention))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("médecin est obligatoire");
    }

    @Test
    @DisplayName("Créer une intervention sans prix doit échouer")
    void creerInterventionSansPrix() {
        InterventionMedecin intervention = InterventionMedecin.builder()
                .idConsultation(1L)
                .idMedecin(1L)
                .build();

        assertThatThrownBy(() -> interventionService.creerIntervention(intervention))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("prix est obligatoire");
    }

    @Test
    @DisplayName("Créer une intervention avec prix négatif doit échouer")
    void creerInterventionPrixNegatif() {
        InterventionMedecin intervention = InterventionMedecin.builder()
                .idConsultation(1L)
                .idMedecin(1L)
                .prixDePatient(-100.0)
                .build();

        assertThatThrownBy(() -> interventionService.creerIntervention(intervention))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("prix ne peut pas être négatif");
    }

    @Test
    @DisplayName("Modifier une intervention valide")
    void modifierInterventionValide() throws ServiceException, ValidationException {
        InterventionMedecin intervention = creerInterventionTest();
        mockRepository.create(intervention);

        intervention.setPrixDePatient(750.0);

        InterventionMedecin updated = interventionService.modifierIntervention(intervention);

        assertThat(updated.getPrixDePatient()).isEqualTo(750.0);
    }

    @Test
    @DisplayName("Modifier une intervention inexistante doit échouer")
    void modifierInterventionInexistante() {
        InterventionMedecin intervention = InterventionMedecin.builder()
                .idIM(999L)
                .idConsultation(1L)
                .idMedecin(1L)
                .prixDePatient(500.0)
                .build();

        assertThatThrownBy(() -> interventionService.modifierIntervention(intervention))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("introuvable");
    }

    @Test
    @DisplayName("Supprimer une intervention valide")
    void supprimerInterventionValide() throws ServiceException, ValidationException {
        InterventionMedecin intervention = creerInterventionTest();
        mockRepository.create(intervention);

        interventionService.supprimerIntervention(intervention.getIdIM());

        assertThat(mockRepository.findById(intervention.getIdIM())).isNull();
    }

    @Test
    @DisplayName("Supprimer une intervention inexistante doit échouer")
    void supprimerInterventionInexistante() {
        assertThatThrownBy(() -> interventionService.supprimerIntervention(999L))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("introuvable");
    }

    @Test
    @DisplayName("Lister les interventions par consultation")
    void listerInterventionsParConsultation() throws ServiceException, ValidationException {
        InterventionMedecin i1 = creerInterventionTest();
        i1.setIdConsultation(1L);
        mockRepository.create(i1);

        InterventionMedecin i2 = creerInterventionTest();
        i2.setIdConsultation(1L);
        i2.setPrixDePatient(600.0);
        mockRepository.create(i2);

        InterventionMedecin i3 = creerInterventionTest();
        i3.setIdConsultation(2L);
        mockRepository.create(i3);

        List<InterventionMedecin> interventions = interventionService.listerInterventionsParConsultation(1L);

        assertThat(interventions).hasSize(2);
        assertThat(interventions).allMatch(i -> i.getIdConsultation().equals(1L));
    }

    @Test
    @DisplayName("Calculer le montant total d'une consultation")
    void calculerMontantTotalConsultation() throws ServiceException, ValidationException {
        InterventionMedecin i1 = creerInterventionTest();
        i1.setIdConsultation(1L);
        i1.setPrixDePatient(500.0);
        mockRepository.create(i1);

        InterventionMedecin i2 = creerInterventionTest();
        i2.setIdConsultation(1L);
        i2.setPrixDePatient(300.0);
        mockRepository.create(i2);

        InterventionMedecin i3 = creerInterventionTest();
        i3.setIdConsultation(1L);
        i3.setPrixDePatient(200.0);
        mockRepository.create(i3);

        Double montantTotal = interventionService.calculerMontantTotalConsultation(1L);

        assertThat(montantTotal).isEqualTo(1000.0);
    }

    @Test
    @DisplayName("Calculer le montant total d'une consultation sans intervention")
    void calculerMontantTotalConsultationVide() throws ServiceException {
        Double montantTotal = interventionService.calculerMontantTotalConsultation(999L);

        assertThat(montantTotal).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Calculer le montant total ignore les prix null")
    void calculerMontantTotalAvecPrixNull() throws ServiceException, ValidationException {
        InterventionMedecin i1 = creerInterventionTest();
        i1.setIdConsultation(1L);
        i1.setPrixDePatient(500.0);
        mockRepository.create(i1);

        InterventionMedecin i2 = InterventionMedecin.builder()
                .idConsultation(1L)
                .idMedecin(1L)
                .prixDePatient(null)
                .build();
        i2.setIdIM(2L);
        mockRepository.interventions.add(i2);

        Double montantTotal = interventionService.calculerMontantTotalConsultation(1L);

        assertThat(montantTotal).isEqualTo(500.0);
    }

    @Test
    @DisplayName("Lister toutes les interventions")
    void listerToutesLesInterventions() throws ServiceException, ValidationException {
        mockRepository.create(creerInterventionTest());
        mockRepository.create(creerInterventionTest());
        mockRepository.create(creerInterventionTest());

        List<InterventionMedecin> interventions = interventionService.listerToutesLesInterventions();

        assertThat(interventions).hasSize(3);
    }

    @Test
    @DisplayName("Obtenir une intervention inexistante retourne null")
    void obtenirInterventionInexistante() throws ServiceException {
        InterventionMedecin result = interventionService.obtenirIntervention(999L);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Créer une intervention avec prix zéro est valide")
    void creerInterventionPrixZero() throws ServiceException, ValidationException {
        InterventionMedecin intervention = InterventionMedecin.builder()
                .idConsultation(1L)
                .idMedecin(1L)
                .prixDePatient(0.0)
                .build();

        InterventionMedecin result = interventionService.creerIntervention(intervention);

        assertThat(result.getPrixDePatient()).isEqualTo(0.0);
    }

    private InterventionMedecin creerInterventionTest() {
        return InterventionMedecin.builder()
                .idConsultation(1L)
                .idMedecin(1L)
                .prixDePatient(500.0)
                .build();
    }

    private static class MockInterventionRepository implements InterventionMedecinRepository {
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
        public void create(InterventionMedecin intervention) {
            intervention.setIdIM(nextId++);
            interventions.add(intervention);
        }

        @Override
        public void update(InterventionMedecin intervention) {
            interventions.removeIf(i -> i.getIdIM().equals(intervention.getIdIM()));
            interventions.add(intervention);
        }

        @Override
        public void delete(InterventionMedecin intervention) {
            interventions.remove(intervention);
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
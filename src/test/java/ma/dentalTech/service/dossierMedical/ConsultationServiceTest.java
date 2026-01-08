package ma.dentalTech.service.dossierMedical;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.dossierMedical.Consultation;
import ma.dentalTech.repository.modules.dossierMedical.api.ConsultationRepository;
import ma.dentalTech.service.modules.dossierMedical.api.ConsultationService;
import ma.dentalTech.service.modules.dossierMedical.impl.ConsultationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class ConsultationServiceTest {

    private ConsultationService consultationService;
    private MockConsultationRepository mockRepository;

    @BeforeEach
    void setUp() {
        mockRepository = new MockConsultationRepository();
        consultationService = new ConsultationServiceImpl(mockRepository);
    }

    @Test
    @DisplayName("Créer une consultation valide")
    void creerConsultationValide() throws ServiceException, ValidationException {
        Consultation consultation = Consultation.builder()
                .idRDV(1L)
                .date(LocalDate.now())
                .observationMedecin("Patient en bonne santé")
                .build();

        Consultation result = consultationService.creerConsultation(consultation);

        assertThat(result).isNotNull();
        assertThat(result.getIdConsultation()).isNotNull();
        assertThat(result.getStatut()).isEqualTo("EN_COURS");
        assertThat(result.getDate()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("Créer une consultation sans RDV doit échouer")
    void creerConsultationSansRDV() {
        Consultation consultation = Consultation.builder()
                .date(LocalDate.now())
                .observationMedecin("Observation")
                .build();

        assertThatThrownBy(() -> consultationService.creerConsultation(consultation))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("RDV");
    }

    @Test
    @DisplayName("Créer une consultation sans date utilise la date du jour")
    void creerConsultationSansDate() throws ServiceException, ValidationException {
        Consultation consultation = Consultation.builder()
                .idRDV(1L)
                .observationMedecin("Observation")
                .build();

        Consultation result = consultationService.creerConsultation(consultation);

        assertThat(result.getDate()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("Créer une consultation avec statut invalide doit échouer")
    void creerConsultationStatutInvalide() {
        Consultation consultation = Consultation.builder()
                .idRDV(1L)
                .date(LocalDate.now())
                .statut("STATUT_INVALIDE")
                .build();

        assertThatThrownBy(() -> consultationService.creerConsultation(consultation))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Statut");
    }

    @Test
    @DisplayName("Modifier une consultation valide")
    void modifierConsultationValide() throws ServiceException, ValidationException {
        Consultation consultation = creerConsultationTest();
        consultationService.creerConsultation(consultation);

        consultation.setObservationMedecin("Nouvelle observation");
        consultation.setStatut("TERMINEE");

        Consultation updated = consultationService.modifierConsultation(consultation);

        assertThat(updated.getObservationMedecin()).isEqualTo("Nouvelle observation");
        assertThat(updated.getStatut()).isEqualTo("TERMINEE");
    }

    @Test
    @DisplayName("Modifier une consultation inexistante doit échouer")
    void modifierConsultationInexistante() {
        Consultation consultation = Consultation.builder()
                .idConsultation(999L)
                .idRDV(1L)
                .date(LocalDate.now())
                .build();

        assertThatThrownBy(() -> consultationService.modifierConsultation(consultation))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("introuvable");
    }

    @Test
    @DisplayName("Terminer une consultation")
    void terminerConsultation() throws ServiceException, ValidationException {
        Consultation consultation = creerConsultationTest();
        consultationService.creerConsultation(consultation);

        consultationService.terminerConsultation(consultation.getIdConsultation(), "Consultation terminée avec succès");

        Consultation updated = consultationService.obtenirConsultation(consultation.getIdConsultation());
        assertThat(updated.getStatut()).isEqualTo("TERMINEE");
        assertThat(updated.getObservationMedecin()).isEqualTo("Consultation terminée avec succès");
    }

    @Test
    @DisplayName("Terminer une consultation déjà terminée doit échouer")
    void terminerConsultationDejaTerminee() throws ServiceException, ValidationException {
        Consultation consultation = creerConsultationTest();
        consultation.setStatut("TERMINEE");
        consultationService.creerConsultation(consultation);

        assertThatThrownBy(() -> consultationService.terminerConsultation(consultation.getIdConsultation(), "Observation"))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("terminée");
    }

    @Test
    @DisplayName("Obtenir une consultation par RDV")
    void obtenirConsultationParRDV() throws ServiceException, ValidationException {
        Consultation consultation = creerConsultationTest();
        consultation.setIdRDV(100L);
        consultationService.creerConsultation(consultation);

        Consultation result = consultationService.obtenirConsultationParRDV(100L);

        assertThat(result).isNotNull();
        assertThat(result.getIdRDV()).isEqualTo(100L);
    }

    @Test
    @DisplayName("Lister les consultations par date")
    void listerConsultationsParDate() throws ServiceException, ValidationException {
        LocalDate dateTest = LocalDate.now();

        Consultation c1 = creerConsultationTest();
        c1.setDate(dateTest);
        consultationService.creerConsultation(c1);

        Consultation c2 = creerConsultationTest();
        c2.setDate(dateTest);
        c2.setIdRDV(2L);
        consultationService.creerConsultation(c2);

        Consultation c3 = creerConsultationTest();
        c3.setDate(dateTest.plusDays(1));
        c3.setIdRDV(3L);
        consultationService.creerConsultation(c3);

        List<Consultation> consultations = consultationService.listerConsultationsParDate(dateTest);

        assertThat(consultations).hasSize(2);
        assertThat(consultations).allMatch(c -> c.getDate().equals(dateTest));
    }

    @Test
    @DisplayName("Supprimer une consultation valide")
    void supprimerConsultationValide() throws ServiceException, ValidationException {
        Consultation consultation = creerConsultationTest();
        consultationService.creerConsultation(consultation);

        consultationService.supprimerConsultation(consultation.getIdConsultation());

        assertThat(consultationService.obtenirConsultation(consultation.getIdConsultation())).isNull();
    }

    @Test
    @DisplayName("Supprimer une consultation inexistante doit échouer")
    void supprimerConsultationInexistante() {
        assertThatThrownBy(() -> consultationService.supprimerConsultation(999L))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("introuvable");
    }

    @Test
    @DisplayName("Lister toutes les consultations")
    void listerToutesLesConsultations() throws ServiceException, ValidationException {
        consultationService.creerConsultation(creerConsultationTest());

        Consultation c2 = creerConsultationTest();
        c2.setIdRDV(2L);
        consultationService.creerConsultation(c2);

        Consultation c3 = creerConsultationTest();
        c3.setIdRDV(3L);
        consultationService.creerConsultation(c3);

        List<Consultation> consultations = consultationService.listerToutesLesConsultations();

        assertThat(consultations).hasSize(3);
    }

    @Test
    @DisplayName("Obtenir une consultation inexistante retourne null")
    void obtenirConsultationInexistante() throws ServiceException {
        Consultation result = consultationService.obtenirConsultation(999L);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Créer une consultation avec statut valide")
    void creerConsultationAvecStatutValide() throws ServiceException, ValidationException {
        Consultation consultation = Consultation.builder()
                .idRDV(1L)
                .date(LocalDate.now())
                .statut("TERMINEE")
                .build();

        Consultation result = consultationService.creerConsultation(consultation);

        assertThat(result.getStatut()).isEqualTo("TERMINEE");
    }

    private Consultation creerConsultationTest() {
        return Consultation.builder()
                .idRDV(1L)
                .date(LocalDate.now())
                .statut("EN_COURS")
                .observationMedecin("Observation test")
                .build();
    }

    private static class MockConsultationRepository implements ConsultationRepository {
        private final List<Consultation> consultations = new ArrayList<>();
        private Long nextId = 1L;

        @Override
        public List<Consultation> findAll() {
            return new ArrayList<>(consultations);
        }

        @Override
        public Consultation findById(Long id) {
            return consultations.stream()
                    .filter(c -> c.getIdConsultation() != null && c.getIdConsultation().equals(id))
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public void create(Consultation consultation) {
            consultation.setIdConsultation(nextId++);
            consultations.add(consultation);
        }

        @Override
        public void update(Consultation consultation) {
            consultations.removeIf(c -> c.getIdConsultation().equals(consultation.getIdConsultation()));
            consultations.add(consultation);
        }

        @Override
        public void delete(Consultation consultation) {
            consultations.remove(consultation);
        }

        @Override
        public void deleteById(Long id) {
            consultations.removeIf(c -> c.getIdConsultation() != null && c.getIdConsultation().equals(id));
        }

        @Override
        public Optional<Consultation> findByRDVId(Long idRDV) {
            return consultations.stream()
                    .filter(c -> c.getIdRDV() != null && c.getIdRDV().equals(idRDV))
                    .findFirst();
        }

        @Override
        public List<Consultation> findByDate(LocalDate date) {
            return consultations.stream()
                    .filter(c -> c.getDate() != null && c.getDate().equals(date))
                    .toList();
        }
    }
}
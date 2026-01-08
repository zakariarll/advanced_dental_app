package ma.dentalTech.service.dossierMedical;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.dossierMedical.RDV;
import ma.dentalTech.entities.enums.StatutRDV;
import ma.dentalTech.repository.modules.dossierMedical.api.RDVRepository;
import ma.dentalTech.service.modules.dossierMedical.impl.RDVServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class RDVServiceTest {

    private RDVServiceImpl rdvService;
    private MockRDVRepository mockRepository;

    @BeforeEach
    void setUp() {
        mockRepository = new MockRDVRepository();
        rdvService = new RDVServiceImpl(mockRepository);
    }

    @Test
    @DisplayName("Créer un RDV valide")
    void creerRDVValide() throws ServiceException, ValidationException {
        RDV rdv = RDV.builder()
                .idPatient(1L)
                .idMedecin(1L)
                .date(LocalDate.now().plusDays(1))
                .heure(LocalTime.of(10, 0))
                .motif("Consultation dentaire")
                .build();

        RDV result = rdvService.creerRDV(rdv);

        assertThat(result).isNotNull();
        assertThat(result.getIdRDV()).isEqualTo(1L);
        assertThat(result.getStatut()).isEqualTo(StatutRDV.PLANIFIE);
    }

    @Test
    @DisplayName("Créer un RDV sans patient doit échouer")
    void creerRDVSansPatient() {
        RDV rdv = RDV.builder()
                .idMedecin(1L)
                .date(LocalDate.now().plusDays(1))
                .heure(LocalTime.of(10, 0))
                .motif("Consultation")
                .build();

        assertThatThrownBy(() -> rdvService.creerRDV(rdv))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("patient est obligatoire");
    }

    @Test
    @DisplayName("Créer un RDV sans médecin doit échouer")
    void creerRDVSansMedecin() {
        RDV rdv = RDV.builder()
                .idPatient(1L)
                .date(LocalDate.now().plusDays(1))
                .heure(LocalTime.of(10, 0))
                .motif("Consultation")
                .build();

        assertThatThrownBy(() -> rdvService.creerRDV(rdv))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("médecin est obligatoire");
    }

    @Test
    @DisplayName("Créer un RDV dans le passé doit échouer")
    void creerRDVDansLePasse() {
        RDV rdv = RDV.builder()
                .idPatient(1L)
                .idMedecin(1L)
                .date(LocalDate.now().minusDays(1))
                .heure(LocalTime.of(10, 0))
                .motif("Consultation")
                .build();

        assertThatThrownBy(() -> rdvService.creerRDV(rdv))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("ne peut pas être dans le passé");
    }

    @Test
    @DisplayName("Créer un RDV sans motif doit échouer")
    void creerRDVSansMotif() {
        RDV rdv = RDV.builder()
                .idPatient(1L)
                .idMedecin(1L)
                .date(LocalDate.now().plusDays(1))
                .heure(LocalTime.of(10, 0))
                .build();

        assertThatThrownBy(() -> rdvService.creerRDV(rdv))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("motif");
    }

    @Test
    @DisplayName("Créer un RDV avec motif trop court doit échouer")
    void creerRDVMotifTropCourt() {
        RDV rdv = RDV.builder()
                .idPatient(1L)
                .idMedecin(1L)
                .date(LocalDate.now().plusDays(1))
                .heure(LocalTime.of(10, 0))
                .motif("AB")
                .build();

        assertThatThrownBy(() -> rdvService.creerRDV(rdv))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    @DisplayName("Confirmer un RDV planifié")
    void confirmerRDVPlanifie() throws ServiceException, ValidationException {
        RDV rdv = creerRDVTest();
        mockRepository.create(rdv);

        rdvService.confirmerRDV(rdv.getIdRDV());

        RDV updated = mockRepository.findById(rdv.getIdRDV());
        assertThat(updated.getStatut()).isEqualTo(StatutRDV.CONFIRME);
    }

    @Test
    @DisplayName("Confirmer un RDV déjà terminé doit échouer")
    void confirmerRDVTermine() throws ServiceException, ValidationException {
        RDV rdv = creerRDVTest();
        rdv.setStatut(StatutRDV.TERMINE);
        mockRepository.create(rdv);

        assertThatThrownBy(() -> rdvService.confirmerRDV(rdv.getIdRDV()))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("planifiés ou en attente");
    }

    @Test
    @DisplayName("Annuler un RDV avec raison")
    void annulerRDVAvecRaison() throws ServiceException, ValidationException {
        RDV rdv = creerRDVTest();
        mockRepository.create(rdv);

        rdvService.annulerRDV(rdv.getIdRDV(), "Empêchement du patient");

        RDV updated = mockRepository.findById(rdv.getIdRDV());
        assertThat(updated.getStatut()).isEqualTo(StatutRDV.ANNULE);
        assertThat(updated.getNoteMedecin()).contains("Annulation");
        assertThat(updated.getNoteMedecin()).contains("Empêchement du patient");
    }

    @Test
    @DisplayName("Annuler un RDV déjà terminé doit échouer")
    void annulerRDVTermine() {
        RDV rdv = creerRDVTest();
        rdv.setStatut(StatutRDV.TERMINE);
        mockRepository.create(rdv);

        assertThatThrownBy(() -> rdvService.annulerRDV(rdv.getIdRDV(), "Raison"))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("RDV terminé");
    }

    @Test
    @DisplayName("Lister les RDV par patient")
    void listerRDVParPatient() throws ServiceException, ValidationException {
        RDV rdv1 = creerRDVTest();
        rdv1.setIdPatient(1L);
        mockRepository.create(rdv1);

        RDV rdv2 = creerRDVTest();
        rdv2.setIdPatient(1L);
        rdv2.setDate(LocalDate.now().plusDays(2));
        mockRepository.create(rdv2);

        RDV rdv3 = creerRDVTest();
        rdv3.setIdPatient(2L);
        mockRepository.create(rdv3);

        List<RDV> rdvs = rdvService.listerRDVParPatient(1L);

        assertThat(rdvs).hasSize(2);
        assertThat(rdvs).allMatch(rdv -> rdv.getIdPatient().equals(1L));
    }

    @Test
    @DisplayName("Lister les RDV par date")
    void listerRDVParDate() throws ServiceException, ValidationException {
        LocalDate dateTest = LocalDate.now().plusDays(1);

        RDV rdv1 = creerRDVTest();
        rdv1.setDate(dateTest);
        mockRepository.create(rdv1);

        RDV rdv2 = creerRDVTest();
        rdv2.setDate(dateTest);
        rdv2.setHeure(LocalTime.of(14, 0));
        mockRepository.create(rdv2);

        RDV rdv3 = creerRDVTest();
        rdv3.setDate(dateTest.plusDays(1));
        mockRepository.create(rdv3);

        List<RDV> rdvs = rdvService.listerRDVParDate(dateTest);

        assertThat(rdvs).hasSize(2);
        assertThat(rdvs).allMatch(rdv -> rdv.getDate().equals(dateTest));
    }

    @Test
    @DisplayName("Vérifier la disponibilité d'un créneau libre")
    void verifierDisponibiliteCreneauLibre() throws ServiceException {
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime heure = LocalTime.of(10, 0);

        boolean disponible = rdvService.verifierDisponibilite(1L, date, heure);

        assertThat(disponible).isTrue();
    }

    @Test
    @DisplayName("Vérifier la disponibilité d'un créneau occupé")
    void verifierDisponibiliteCreneauOccupe() throws ServiceException, ValidationException {
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime heure = LocalTime.of(10, 0);

        RDV rdv = creerRDVTest();
        rdv.setDate(date);
        rdv.setHeure(heure);
        rdv.setIdMedecin(1L);
        mockRepository.create(rdv);

        boolean disponible = rdvService.verifierDisponibilite(1L, date, heure);

        assertThat(disponible).isFalse();
    }

    @Test
    @DisplayName("Modifier un RDV valide")
    void modifierRDVValide() throws ServiceException, ValidationException {
        RDV rdv = creerRDVTest();
        mockRepository.create(rdv);

        rdv.setMotif("Nouveau motif");
        rdv.setHeure(LocalTime.of(15, 0));

        RDV updated = rdvService.modifierRDV(rdv);

        assertThat(updated.getMotif()).isEqualTo("Nouveau motif");
        assertThat(updated.getHeure()).isEqualTo(LocalTime.of(15, 0));
    }

    @Test
    @DisplayName("Modifier un RDV terminé doit échouer")
    void modifierRDVTermine() {
        RDV rdv = creerRDVTest();
        rdv.setStatut(StatutRDV.TERMINE);
        mockRepository.create(rdv);

        rdv.setMotif("Nouveau motif");

        assertThatThrownBy(() -> rdvService.modifierRDV(rdv))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("RDV terminé");
    }

    @Test
    @DisplayName("Supprimer un RDV valide")
    void supprimerRDVValide() throws ServiceException, ValidationException {
        RDV rdv = creerRDVTest();
        mockRepository.create(rdv);

        rdvService.supprimerRDV(rdv.getIdRDV());

        assertThat(mockRepository.findById(rdv.getIdRDV())).isNull();
    }

    @Test
    @DisplayName("Supprimer un RDV terminé doit échouer")
    void supprimerRDVTermine() {
        RDV rdv = creerRDVTest();
        rdv.setStatut(StatutRDV.TERMINE);
        mockRepository.create(rdv);

        assertThatThrownBy(() -> rdvService.supprimerRDV(rdv.getIdRDV()))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("RDV terminé");
    }

    private RDV creerRDVTest() {
        return RDV.builder()
                .idPatient(1L)
                .idMedecin(1L)
                .date(LocalDate.now().plusDays(1))
                .heure(LocalTime.of(10, 0))
                .motif("Consultation dentaire")
                .statut(StatutRDV.PLANIFIE)
                .build();
    }

    private static class MockRDVRepository implements RDVRepository {
        private final List<RDV> rdvs = new ArrayList<>();
        private Long nextId = 1L;

        @Override
        public List<RDV> findAll() {
            return new ArrayList<>(rdvs);
        }

        @Override
        public RDV findById(Long id) {
            return rdvs.stream()
                    .filter(rdv -> rdv.getIdRDV().equals(id))
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public void create(RDV rdv) {
            rdv.setIdRDV(nextId++);
            rdvs.add(rdv);
        }

        @Override
        public void update(RDV rdv) {
            rdvs.removeIf(r -> r.getIdRDV().equals(rdv.getIdRDV()));
            rdvs.add(rdv);
        }

        @Override
        public void delete(RDV rdv) {
            rdvs.remove(rdv);
        }

        @Override
        public void deleteById(Long id) {
            rdvs.removeIf(rdv -> rdv.getIdRDV().equals(id));
        }

        @Override
        public List<RDV> findByPatientId(Long idPatient) {
            return rdvs.stream()
                    .filter(rdv -> rdv.getIdPatient().equals(idPatient))
                    .toList();
        }

        @Override
        public List<RDV> findByDate(LocalDate date) {
            return rdvs.stream()
                    .filter(rdv -> rdv.getDate().equals(date))
                    .toList();
        }

        @Override
        public List<RDV> findByMedecinId(Long idMedecin) {
            return rdvs.stream()
                    .filter(rdv -> rdv.getIdMedecin().equals(idMedecin))
                    .toList();
        }
    }
}
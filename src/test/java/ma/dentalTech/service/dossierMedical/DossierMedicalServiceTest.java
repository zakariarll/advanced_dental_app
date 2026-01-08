package ma.dentalTech.service.dossierMedical;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.entities.dossierMedical.Consultation;
import ma.dentalTech.entities.dossierMedical.DossierMedical;
import ma.dentalTech.entities.enums.Assurance;
import ma.dentalTech.entities.enums.CategorieAntecedent;
import ma.dentalTech.entities.enums.NiveauRisque;
import ma.dentalTech.entities.enums.Sexe;
import ma.dentalTech.entities.patient.Antecedent;
import ma.dentalTech.entities.patient.Patient;
import ma.dentalTech.repository.modules.dossierMedical.api.DossierMedicalRepository;
import ma.dentalTech.service.modules.dossierMedical.impl.DossierMedicalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class DossierMedicalServiceTest {

    private DossierMedicalServiceImpl dossierService;
    private MockDossierMedicalRepository mockRepository;

    @BeforeEach
    void setUp() {
        mockRepository = new MockDossierMedicalRepository();
        dossierService = new DossierMedicalServiceImpl(mockRepository);
    }

    @Test
    @DisplayName("Obtenir un dossier médical complet")
    void obtenirDossierComplet() throws ServiceException {
        Patient patient = creerPatientTest();
        mockRepository.ajouterPatient(patient);

        DossierMedical dossier = dossierService.obtenirDossierComplet(patient.getIdPatient());

        assertThat(dossier).isNotNull();
        assertThat(dossier.getPatient()).isNotNull();
        assertThat(dossier.getPatient().getIdPatient()).isEqualTo(patient.getIdPatient());
    }

    @Test
    @DisplayName("Obtenir un dossier pour un patient inexistant doit échouer")
    void obtenirDossierPatientInexistant() {
        assertThatThrownBy(() -> dossierService.obtenirDossierComplet(999L))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("introuvable");
    }

    @Test
    @DisplayName("Obtenir un dossier avec ID null doit échouer")
    void obtenirDossierIdNull() {
        assertThatThrownBy(() -> dossierService.obtenirDossierComplet(null))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("identifiant du patient est obligatoire");
    }

    @Test
    @DisplayName("Vérifier patient avec risques critiques")
    void patientAvecRisquesCritiques() throws ServiceException {
        Patient patient = creerPatientTest();
        mockRepository.ajouterPatient(patient);

        Antecedent antecedentCritique = Antecedent.builder()
                .idAntecedent(1L)
                .nom("Allergie sévère")
                .categorie(CategorieAntecedent.ALLERGIE)
                .niveauRisque(NiveauRisque.CRITIQUE)
                .idPatient(patient.getIdPatient())
                .build();

        mockRepository.ajouterAntecedent(patient.getIdPatient(), antecedentCritique);

        boolean aDesRisques = dossierService.patientADesRisquesCritiques(patient.getIdPatient());

        assertThat(aDesRisques).isTrue();
    }

    @Test
    @DisplayName("Vérifier patient sans risques critiques")
    void patientSansRisquesCritiques() throws ServiceException {
        Patient patient = creerPatientTest();
        mockRepository.ajouterPatient(patient);

        Antecedent antecedentModere = Antecedent.builder()
                .idAntecedent(1L)
                .nom("Diabète léger")
                .categorie(CategorieAntecedent.MALADIE_CHRONIQUE)
                .niveauRisque(NiveauRisque.MODERE)
                .idPatient(patient.getIdPatient())
                .build();

        mockRepository.ajouterAntecedent(patient.getIdPatient(), antecedentModere);

        boolean aDesRisques = dossierService.patientADesRisquesCritiques(patient.getIdPatient());

        assertThat(aDesRisques).isFalse();
    }

    @Test
    @DisplayName("Compter les consultations d'un patient")
    void compterConsultationsPatient() throws ServiceException {
        Patient patient = creerPatientTest();
        mockRepository.ajouterPatient(patient);

        Consultation c1 = creerConsultationTest();
        Consultation c2 = creerConsultationTest();

        mockRepository.ajouterConsultation(patient.getIdPatient(), c1);
        mockRepository.ajouterConsultation(patient.getIdPatient(), c2);

        int count = dossierService.compterConsultationsPatient(patient.getIdPatient());

        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Compter consultations pour patient sans consultations")
    void compterConsultationsPatientSansConsultations() throws ServiceException {
        Patient patient = creerPatientTest();
        mockRepository.ajouterPatient(patient);

        int count = dossierService.compterConsultationsPatient(patient.getIdPatient());

        assertThat(count).isEqualTo(0);
    }

    @Test
    @DisplayName("Vérifier que le dossier contient toutes les informations")
    void dossierContientToutesLesInformations() throws ServiceException {
        Patient patient = creerPatientTest();
        mockRepository.ajouterPatient(patient);

        Antecedent antecedent = Antecedent.builder()
                .idAntecedent(1L)
                .nom("Hypertension")
                .categorie(CategorieAntecedent.MALADIE_CHRONIQUE)
                .niveauRisque(NiveauRisque.MODERE)
                .build();

        Consultation consultation = creerConsultationTest();

        mockRepository.ajouterAntecedent(patient.getIdPatient(), antecedent);
        mockRepository.ajouterConsultation(patient.getIdPatient(), consultation);

        DossierMedical dossier = dossierService.obtenirDossierComplet(patient.getIdPatient());

        assertThat(dossier.getPatient()).isNotNull();
        assertThat(dossier.getAntecedents()).hasSize(1);
        assertThat(dossier.getConsultations()).hasSize(1);
    }

    @Test
    @DisplayName("Patient avec plusieurs risques critiques")
    void patientAvecPlusieursRisquesCritiques() throws ServiceException {
        Patient patient = creerPatientTest();
        mockRepository.ajouterPatient(patient);

        Antecedent a1 = Antecedent.builder()
                .idAntecedent(1L)
                .nom("Allergie pénicilline")
                .categorie(CategorieAntecedent.ALLERGIE)
                .niveauRisque(NiveauRisque.CRITIQUE)
                .build();

        Antecedent a2 = Antecedent.builder()
                .idAntecedent(2L)
                .nom("Problème cardiaque")
                .categorie(CategorieAntecedent.ANTECEDENT_CHIRURGICAL)
                .niveauRisque(NiveauRisque.CRITIQUE)
                .build();

        mockRepository.ajouterAntecedent(patient.getIdPatient(), a1);
        mockRepository.ajouterAntecedent(patient.getIdPatient(), a2);

        boolean aDesRisques = dossierService.patientADesRisquesCritiques(patient.getIdPatient());

        assertThat(aDesRisques).isTrue();
    }

    private Patient creerPatientTest() {
        return Patient.builder()
                .idPatient(1L)
                .nom("Benali")
                .prenom("Ahmed")
                .dateDeNaissance(LocalDate.of(1990, 5, 15))
                .sexe(Sexe.Homme)
                .adresse("Rabat")
                .telephone("0612345678")
                .assurance(Assurance.CNSS)
                .dateCreation(LocalDate.now())
                .build();
    }

    private Consultation creerConsultationTest() {
        return Consultation.builder()
                .idRDV(1L)
                .date(LocalDate.now())
                .statut("EN_COURS")
                .build();
    }

    private static class MockDossierMedicalRepository implements DossierMedicalRepository {
        private final List<DossierMedical> dossiers = new ArrayList<>();

        public void ajouterPatient(Patient patient) {
            DossierMedical dossier = DossierMedical.builder()
                    .patient(patient)
                    .antecedents(new ArrayList<>())
                    .consultations(new ArrayList<>())
                    .build();
            dossiers.add(dossier);
        }

        public void ajouterAntecedent(Long idPatient, Antecedent antecedent) {
            DossierMedical dossier = getDossierComplet(idPatient);
            if (dossier != null) {
                dossier.getAntecedents().add(antecedent);
            }
        }

        public void ajouterConsultation(Long idPatient, Consultation consultation) {
            DossierMedical dossier = getDossierComplet(idPatient);
            if (dossier != null) {
                dossier.getConsultations().add(consultation);
            }
        }

        @Override
        public DossierMedical getDossierComplet(Long idPatient) {
            return dossiers.stream()
                    .filter(d -> d.getPatient().getIdPatient().equals(idPatient))
                    .findFirst()
                    .orElse(null);
        }
    }
}
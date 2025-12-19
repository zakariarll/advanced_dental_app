package ma.dentalTech.repository.patient;

import ma.dentalTech.entities.enums.Assurance;
import ma.dentalTech.entities.enums.CategorieAntecedent;
import ma.dentalTech.entities.enums.NiveauRisque;
import ma.dentalTech.entities.enums.Sexe;
import ma.dentalTech.entities.patient.Antecedent;
import ma.dentalTech.entities.patient.Patient;
import ma.dentalTech.repository.DbTestUtils;
import ma.dentalTech.repository.modules.patient.api.AntecedentRepository;
import ma.dentalTech.repository.modules.patient.api.PatientRepository;
import ma.dentalTech.repository.modules.patient.impl.mySQL.AntecedentRepositoryImpl;
import ma.dentalTech.repository.modules.patient.impl.mySQL.PatientRepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PatientRepositoryTest {

    private final PatientRepository patientRepo = new PatientRepositoryImpl();
    private final AntecedentRepository antecedentRepo = new AntecedentRepositoryImpl();

    @BeforeEach
    void setUp() {
        DbTestUtils.cleanDatabase();
    }

    @Test
    void testCreateAndFindPatient() {
        // Given
        Patient p = Patient.builder()
                .nom("Alaoui")
                .prenom("Ahmed")
                .sexe(Sexe.Homme)
                .assurance(Assurance.CNSS)
                .dateDeNaissance(LocalDate.of(1990, 1, 1))
                .telephone("0600000000")
                .build();

        // When
        patientRepo.create(p);

        // Then
        assertNotNull(p.getIdPatient(), "L'ID ne doit pas être null après insertion");
        Patient found = patientRepo.findById(p.getIdPatient());
        assertNotNull(found);
        assertEquals("Alaoui", found.getNom());
    }

    @Test
    void testAntecedentsLink() {
        // 1. Créer Patient
        Patient p = Patient.builder().nom("Test").prenom("User").sexe(Sexe.Femme).assurance(Assurance.Aucune).build();
        patientRepo.create(p);

        // 2. Créer Antécédent lié
        Antecedent a = Antecedent.builder()
                .nom("Allergie Pénicilline")
                .categorie(CategorieAntecedent.ALLERGIE)
                .niveauRisque(NiveauRisque.CRITIQUE)
                .idPatient(p.getIdPatient()) // Liaison FK
                .build();
        antecedentRepo.create(a);

        // 3. Vérifier la récupération
        List<Antecedent> list = antecedentRepo.findByPatientId(p.getIdPatient());
        assertEquals(1, list.size());
        assertEquals("Allergie Pénicilline", list.get(0).getNom());
    }
}
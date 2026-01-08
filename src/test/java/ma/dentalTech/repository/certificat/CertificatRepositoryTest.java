package ma.dentalTech.repository.certificat;



import ma.dentalTech.entities.certificat.Certificat;
import ma.dentalTech.entities.enums.Assurance;
import ma.dentalTech.entities.enums.Sexe;
import ma.dentalTech.entities.patient.Patient;
import ma.dentalTech.repository.DbTestUtils;
import ma.dentalTech.repository.modules.certificat.api.CertificatRepository;
import ma.dentalTech.repository.modules.certificat.impl.mySQL.CertificatRepositoryImpl;
import ma.dentalTech.repository.modules.patient.impl.mySQL.PatientRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CertificatRepositoryTest {

    private final CertificatRepository certifRepo = new CertificatRepositoryImpl();
    private final PatientRepositoryImpl patientRepo = new PatientRepositoryImpl();

    @BeforeEach
    void setUp() { DbTestUtils.cleanDatabase(); }

    @Test
    void testCertificatWorkflow() {
        // 1. Créer Patient
        Patient p = Patient.builder().nom("Tazi").prenom("Sara").sexe(Sexe.Femme).assurance(Assurance.CNSS).build();
        patientRepo.create(p);

        // 2. Créer Certificat (On suppose idMedecin = 1 pour le test, ou on crée un User avant)
        Certificat c = Certificat.builder()
                .dateDebut(LocalDate.now())
                .dateFin(LocalDate.now().plusDays(5))
                .duree(5)
                .noteMedecin("Repos strict")
                .idPatient(p.getIdPatient())
                .idMedecin(1L) // ID Dummy si FK non stricte dans le test, sinon créer User
                .build();

        certifRepo.create(c);

        // 3. Vérifier
        assertNotNull(c.getIdCertif());
        List<Certificat> history = certifRepo.findByPatientId(p.getIdPatient());
        assertEquals(1, history.size());
        assertEquals(5, history.get(0).getDuree());
    }
}
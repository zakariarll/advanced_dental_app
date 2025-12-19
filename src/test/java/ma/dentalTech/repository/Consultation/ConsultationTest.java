package ma.dentalTech.repository.Consultation;



import ma.dentalTech.entities.dossierMedical.Consultation;
import ma.dentalTech.entities.dossierMedical.RDV;
import ma.dentalTech.entities.enums.Assurance;
import ma.dentalTech.entities.enums.Sexe;
import ma.dentalTech.entities.enums.StatutRDV;
import ma.dentalTech.entities.patient.Patient;
import ma.dentalTech.repository.DbTestUtils;
import ma.dentalTech.repository.modules.dossierMedical.api.ConsultationRepository;
import ma.dentalTech.repository.modules.dossierMedical.api.RDVRepository;
import ma.dentalTech.repository.modules.dossierMedical.impl.mySQL.ConsultationRepositoryImpl;
import ma.dentalTech.repository.modules.dossierMedical.impl.mySQL.RDVRepositoryImpl;
import ma.dentalTech.repository.modules.patient.impl.mySQL.PatientRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ConsultationTest {

    private final ConsultationRepository consultRepo = new ConsultationRepositoryImpl();
    private final RDVRepository rdvRepo = new RDVRepositoryImpl();
    private final PatientRepositoryImpl patientRepo = new PatientRepositoryImpl();

    @BeforeEach
    void setUp() { DbTestUtils.cleanDatabase(); }

    @Test
    void testConsultationFlow() {
        // 1. Patient
        Patient p = Patient.builder().nom("Test").prenom("Patient").sexe(Sexe.Homme).assurance(Assurance.Aucune).build();
        patientRepo.create(p);

        // 2. RDV
        RDV rdv = RDV.builder()
                .date(LocalDate.now())
                .heure(LocalTime.of(9,0))
                .motif("Douleur")
                .statut(StatutRDV.CONFIRME)
                .idPatient(p.getIdPatient())
                .idMedecin(1L)
                .build();
        rdvRepo.create(rdv);

        // 3. Consultation
        Consultation c = Consultation.builder()
                .date(LocalDate.now())
                .statut("TERMINE")
                .observationMedecin("Carie profonde traitée")
                .idRDV(rdv.getIdRDV())
                .build();

        consultRepo.create(c);

        // 4. Verification
        assertNotNull(c.getIdConsultation());
        Consultation found = consultRepo.findByRDVId(rdv.getIdRDV()).orElse(null);
        assertNotNull(found);
        assertEquals("Carie profonde traitée", found.getObservationMedecin());
    }
}
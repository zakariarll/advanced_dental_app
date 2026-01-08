/*package ma.dentalTech.repository.Ordonnance;


import ma.dentalTech.entities.medicament.Medicament;
import ma.dentalTech.entities.ordonnance.Ordonnance;
import ma.dentalTech.entities.ordonnance.Prescription;
import ma.dentalTech.entities.patient.Patient;
import ma.dentalTech.entities.enums.Assurance;
import ma.dentalTech.entities.enums.Sexe;
import ma.dentalTech.repository.DbTestUtils;
import ma.dentalTech.repository.modules.medicament.impl.mySQL.MedicamentRepositoryImpl;
import ma.dentalTech.repository.modules.ordonnance.impl.mySQL.OrdonnanceRepositoryImpl;
import ma.dentalTech.repository.modules.ordonnance.impl.mySQL.PrescriptionRepositoryImpl;
import ma.dentalTech.repository.modules.patient.impl.mySQL.PatientRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrescriptionTest {

    private final PrescriptionRepositoryImpl prescRepo = new PrescriptionRepositoryImpl();
    private final OrdonnanceRepositoryImpl ordRepo = new OrdonnanceRepositoryImpl();
    private final MedicamentRepositoryImpl medRepo = new MedicamentRepositoryImpl();
    private final PatientRepositoryImpl patientRepo = new PatientRepositoryImpl();

    @BeforeEach
    void setUp() { DbTestUtils.cleanDatabase(); }

    @Test
    void testPrescriptionDetails() {
        // Setup : Patient -> Ordonnance -> Medicament
        Patient p = Patient.builder().nom("A").prenom("B").sexe(Sexe.Homme).assurance(Assurance.Aucune).build();
        patientRepo.create(p);

        Ordonnance ord = Ordonnance.builder().date(LocalDate.now()).idPatient(p.getIdPatient()).idMedecin(1L).build();
        ordRepo.create(ord);

        Medicament med = Medicament.builder().nom("Ibuprofene").type("Anti-inf").prixUnitaire(20.0).build();
        medRepo.create(med);

        // Test : Création ligne prescription
        Prescription pr = Prescription.builder()
                .quantite(2)
                .frequence("Matin et Soir")
                .dureeEnJours(5)
                .idOrd(ord.getIdOrd())
                .idMct(med.getIdMct())
                .build();

        prescRepo.create(pr);

        // Verify
        assertNotNull(pr.getIdPr());
        List<Prescription> lines = prescRepo.findByOrdonnanceId(ord.getIdOrd());
        assertEquals(1, lines.size());
        assertEquals("Matin et Soir", lines.get(0).getFrequence());
    }
}

 */
package ma.dentalTech.repository.medicament;


import ma.dentalTech.entities.enums.Assurance;
import ma.dentalTech.entities.enums.Sexe;
import ma.dentalTech.entities.medicament.Medicament;
import ma.dentalTech.entities.ordonnance.Ordonnance;
import ma.dentalTech.entities.patient.Patient;
import ma.dentalTech.repository.DbTestUtils;
import ma.dentalTech.repository.modules.medicament.impl.mySQL.MedicamentRepositoryImpl;
import ma.dentalTech.repository.modules.ordonnance.impl.mySQL.OrdonnanceRepositoryImpl;
import ma.dentalTech.repository.modules.patient.impl.mySQL.PatientRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class medicament {

    private final MedicamentRepositoryImpl medRepo = new MedicamentRepositoryImpl();
    private final OrdonnanceRepositoryImpl ordRepo = new OrdonnanceRepositoryImpl();
    private final PatientRepositoryImpl patientRepo = new PatientRepositoryImpl();

    @BeforeEach
    void setUp() { DbTestUtils.cleanDatabase(); }

    @Test
    void testmedicamentetordonnanceChain() {
        // 1. Créer Médicament
        Medicament m = Medicament.builder()
                .nom("Amoxicilline")
                .type("Antibiotique")
                .prixUnitaire(45.0)
                .remboursable(true)
                .build();
        medRepo.create(m);

        // 2. Créer Patient
        Patient p = Patient.builder().nom("Oumnia").prenom("K.").sexe(Sexe.Femme).assurance(Assurance.CNSS).build();
        patientRepo.create(p);

        // 3. Créer Ordonnance pour ce patient
        Ordonnance ord = Ordonnance.builder()
                .date(LocalDate.now())
                .idPatient(p.getIdPatient())
                .idMedecin(1L)
                .build();
        ordRepo.create(ord);

        // 4. Assertions
        assertNotNull(m.getIdMct());
        assertNotNull(ord.getIdOrd());

        // Vérifier lien Ordonnance -> Patient
        Ordonnance found = ordRepo.findById(ord.getIdOrd());
        assertEquals(p.getIdPatient(), found.getIdPatient());
    }
}
/* package ma.dentalTech.service;

import ma.dentalTech.entities.enums.Assurance;
import ma.dentalTech.entities.enums.Sexe;
import ma.dentalTech.entities.medicament.Medicament;
import ma.dentalTech.entities.ordonnance.Ordonnance;
import ma.dentalTech.entities.ordonnance.Prescription;
import ma.dentalTech.entities.patient.Patient;
import ma.dentalTech.repository.DbTestUtils;
import ma.dentalTech.repository.modules.medicament.impl.mySQL.MedicamentRepositoryImpl;
import ma.dentalTech.repository.modules.ordonnance.impl.mySQL.OrdonnanceRepositoryImpl;
import ma.dentalTech.repository.modules.ordonnance.impl.mySQL.PrescriptionRepositoryImpl;
import ma.dentalTech.repository.modules.patient.impl.mySQL.PatientRepositoryImpl;
import ma.dentalTech.service.modules.ordonnance.api.OrdonnanceService;
import ma.dentalTech.service.modules.ordonnance.impl.OrdonnanceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrdonnanceServiceTest {

    // Dépendances nécessaires pour préparer le test
    private final PatientRepositoryImpl patientRepo = new PatientRepositoryImpl();
    private final MedicamentRepositoryImpl medRepo = new MedicamentRepositoryImpl();

    // Le Service à tester et ses Repos
    private final OrdonnanceRepositoryImpl ordRepo = new OrdonnanceRepositoryImpl();
    private final PrescriptionRepositoryImpl prescRepo = new PrescriptionRepositoryImpl();

    // Injection manuelle
    private final OrdonnanceService service = new OrdonnanceServiceImpl(ordRepo, prescRepo);

    @BeforeEach
    void setUp() {
        DbTestUtils.cleanDatabase();
    }

    @Test
    void testCreerOrdonnanceComplete() {
        // 1. Préparation des données (Patient + Médicament)
        Patient p = Patient.builder().nom("Doe").prenom("John").sexe(Sexe.Homme).assurance(Assurance.Aucune).build();
        patientRepo.create(p);

        Medicament m = Medicament.builder().nom("Antibio").prixUnitaire(50.0).remboursable(false).build();
        medRepo.create(m);

        // 2. Préparation de l'Ordonnance (Sans ID pour l'instant)
        Ordonnance ord = Ordonnance.builder()
                .date(LocalDate.now())
                .idPatient(p.getIdPatient())
                .idMedecin(1L)
                .build();

        // 3. Préparation des Prescriptions (Lignes)
        List<Prescription> lignes = new ArrayList<>();
        lignes.add(Prescription.builder()
                .idMct(m.getIdMct())
                .quantite(2)
                .frequence("Matin et Soir")
                .dureeEnJours(7)
                .build());

        // 4. ACTION : Appel du Service
        service.creerOrdonnanceComplete(ord, lignes);

        // 5. VÉRIFICATION
        // L'ordonnance a-t-elle un ID ?
        assertNotNull(ord.getIdOrd(), "L'ordonnance devrait avoir un ID généré");

        // L'ordonnance est-elle en base ?
        Ordonnance savedOrd = service.getById(ord.getIdOrd());
        assertNotNull(savedOrd);

        // Les prescriptions sont-elles en base et liées ?
        List<Prescription> savedLignes = service.getPrescriptionsByOrdonnance(ord.getIdOrd());
        assertEquals(1, savedLignes.size());
        assertEquals("Matin et Soir", savedLignes.get(0).getFrequence());
        assertEquals(m.getIdMct(), savedLignes.get(0).getIdMct());
    }
}

 */
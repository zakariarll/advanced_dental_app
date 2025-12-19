package ma.dentalTech.repository.StatistiquesDashboard;


import ma.dentalTech.entities.caisse.Revenue;
import ma.dentalTech.entities.enums.Assurance;
import ma.dentalTech.entities.enums.Sexe;
import ma.dentalTech.entities.patient.Patient;
import ma.dentalTech.repository.DbTestUtils;
import ma.dentalTech.repository.modules.caisse.api.RevenueRepository;
import ma.dentalTech.repository.modules.caisse.impl.mySQL.RevenueRepositoryImpl;
import ma.dentalTech.repository.modules.dashboard.api.DashboardRepository;
import ma.dentalTech.repository.modules.dashboard.impl.mySQL.DashboardRepositoryImpl;
import ma.dentalTech.repository.modules.patient.impl.mySQL.PatientRepositoryImpl;
import ma.dentalTech.repository.modules.statistiques.api.StatistiqueRepository;
import ma.dentalTech.repository.modules.statistiques.impl.mySQL.StatistiqueRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class StatsAndDashboardTest {

    private final DashboardRepository dashboardRepo = new DashboardRepositoryImpl();
    private final StatistiqueRepository statRepo = new StatistiqueRepositoryImpl();

    // Repos pour créer la donnée
    private final PatientRepositoryImpl patientRepo = new PatientRepositoryImpl();
    private final RevenueRepository revenueRepo = new RevenueRepositoryImpl();

    @BeforeEach
    void setUp() { DbTestUtils.cleanDatabase(); }

    @Test
    void testCalculsStatistiques() {
        // 1. Ajouter 2 Patients
        patientRepo.create(Patient.builder().nom("A").prenom("A").sexe(Sexe.Homme).assurance(Assurance.Autre).build());
        patientRepo.create(Patient.builder().nom("B").prenom("B").sexe(Sexe.Femme).assurance(Assurance.Autre).build());

        // 2. Ajouter du Revenu (2 entrées de 500.0)
        revenueRepo.create(Revenue.builder().montant(500.0).date(LocalDateTime.now()).idCabinet(1L).build());
        revenueRepo.create(Revenue.builder().montant(500.0).date(LocalDateTime.now()).idCabinet(1L).build());

        // 3. Tester Dashboard
        assertEquals(9, dashboardRepo.getNombrePatients());

        // 4. Tester Statistique (Total Revenu)
        // Note: calculateTotalRevenue fait SUM(montant) sur la table Revenue
        assertEquals(1000.0, statRepo.calculateTotalRevenue());
        assertEquals(9, statRepo.countPatients());
    }
}
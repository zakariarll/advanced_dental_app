package ma.dentalTech.repository.Caisse;


import ma.dentalTech.entities.caisse.Facture;
import ma.dentalTech.entities.caisse.Revenue;
import ma.dentalTech.repository.DbTestUtils;
import ma.dentalTech.repository.modules.caisse.api.FactureRepository;
import ma.dentalTech.repository.modules.caisse.api.RevenueRepository;
import ma.dentalTech.repository.modules.caisse.impl.mySQL.FactureRepositoryImpl;
import ma.dentalTech.repository.modules.caisse.impl.mySQL.RevenueRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FinanceRepositoryTest {

    private final FactureRepository factureRepo = new FactureRepositoryImpl();
    private final RevenueRepository revenueRepo = new RevenueRepositoryImpl();

    @BeforeEach
    void setUp() { DbTestUtils.cleanDatabase(); }

    @Test

    void testFactureCycle() {
        // Création Facture (sans consultation liée pour simplifier le test JDBC pur)
        Facture f = Facture.builder()
                .totaleFacture(1500.0)
                .totalePaye(500.0)
                .reste(1000.0)
                .statut("PARTIEL")
                .dateFacture(LocalDateTime.now())
                .idConsultation(1L)
                .build();

        factureRepo.create(f);

        // Test recherche par statut
        List<Facture> impayees = factureRepo.findByStatut("PARTIEL");
        assertEquals(1, impayees.size());
        assertEquals(1000.0, impayees.get(0).getReste());
    }

    @Test
    void testRevenue() {
        Revenue r = Revenue.builder()
                .titre("Vente produits")
                .montant(200.0)
                .date(LocalDateTime.now())
                .idCabinet(1L)
                .build();

        revenueRepo.create(r);
        assertNotNull(r.getIdRevenue());
    }
}
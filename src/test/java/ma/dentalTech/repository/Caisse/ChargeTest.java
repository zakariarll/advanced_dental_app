package ma.dentalTech.repository.Caisse;



import ma.dentalTech.entities.caisse.Charge;
import ma.dentalTech.repository.DbTestUtils;
import ma.dentalTech.repository.modules.caisse.impl.mySQL.ChargeRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChargeTest {

    private final ChargeRepositoryImpl chargeRepo = new ChargeRepositoryImpl();

    @BeforeEach
    void setUp() { DbTestUtils.cleanDatabase(); }

    @Test
    void testCreateCharge() {
        Charge c = Charge.builder()
                .titre("Facture Electricité")
                .description("Mois Octobre")
                .montant(450.00)
                .date(LocalDateTime.now())
                .idCabinet(1L)
                .build();

        chargeRepo.create(c);

        assertNotNull(c.getIdCharge());
        List<Charge> list = chargeRepo.findAll();
        assertEquals(1, list.size());
        assertEquals(450.00, list.get(0).getMontant());
    }
}
package ma.dentalTech.repository.actes;



import ma.dentalTech.entities.actes.Acte;
import ma.dentalTech.repository.DbTestUtils;
import ma.dentalTech.repository.modules.actes.api.ActeRepository;
import ma.dentalTech.repository.modules.actes.impl.mySQL.ActeRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ActeRepositoryTest {

    private final ActeRepository acteRepo = new ActeRepositoryImpl();

    @BeforeEach
    void setUp() { DbTestUtils.cleanDatabase(); }

    @Test
    void testCrudActe() {
        // Create
        Acte a = Acte.builder()
                .libelle("Extraction Dent de Sagesse")
                .categorie("Chirurgie")
                .prixDeBase(800.0)
                .code(505)
                .description("Complexe")
                .build();
        acteRepo.create(a);

        assertNotNull(a.getIdActe());

        // Find
        Acte found = acteRepo.findById(a.getIdActe());
        assertEquals("Chirurgie", found.getCategorie());

        // Search
        List<Acte> list = acteRepo.findByCategorie("Chirurgie");
        assertFalse(list.isEmpty());
    }
}
package ma.dentalTech.service;

import ma.dentalTech.entities.medicament.Medicament;
import ma.dentalTech.repository.DbTestUtils;
import ma.dentalTech.repository.modules.medicament.impl.mySQL.MedicamentRepositoryImpl;
import ma.dentalTech.service.common.ServiceException;
import ma.dentalTech.service.modules.medicament.api.MedicamentService;
import ma.dentalTech.service.modules.medicament.impl.MedicamentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MedicamentServiceTest {

    // On instancie manuellement le Service et le Repo (grâce au constructeur qu'on a créé)
    private final MedicamentRepositoryImpl repo = new MedicamentRepositoryImpl();
    private final MedicamentService service = new MedicamentServiceImpl(repo);

    @BeforeEach
    void setUp() {
        DbTestUtils.cleanDatabase();
    }

    @Test
    void testCreateValidMedicament() {
        Medicament m = Medicament.builder()
                .nom("Doliprane")
                .prixUnitaire(15.0)
                .remboursable(false)
                .build();

        service.create(m);

        assertNotNull(m.getIdMct());
        assertEquals(1, service.getAll().size());
    }

    @Test
    void testCreateInvalidMedicament_PrixNegatif() {
        Medicament m = Medicament.builder()
                .nom("ErreurMed")
                .prixUnitaire(-10.0) // Interdit par le service
                .remboursable(false)
                .build();

        // On vérifie que le service lance bien une exception
        Exception exception = assertThrows(ServiceException.class, () -> {
            service.create(m);
        });

        assertEquals("Le prix d'un médicament ne peut pas être négatif.", exception.getMessage());
    }

    @Test
    void testSearchByNom() {
        service.create(Medicament.builder().nom("Aspirine").prixUnitaire(20.0).remboursable(false).build());
        service.create(Medicament.builder().nom("Aspégic").prixUnitaire(25.0).remboursable(false).build());
        service.create(Medicament.builder().nom("Doliprane").prixUnitaire(15.0).remboursable(false).build());

        List<Medicament> results = service.searchByNom("%Asp%");
        assertEquals(2, results.size());
    }
}
package ma.dentalTech.repository.Agenda;


import ma.dentalTech.entities.agenda.AgendaMensuel;
import ma.dentalTech.repository.DbTestUtils;
import ma.dentalTech.repository.modules.agenda.api.AgendaRepository;
import ma.dentalTech.repository.modules.agenda.impl.mySQL.AgendaRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AgendaRepositoryTest {

    private final AgendaRepository agendaRepo = new AgendaRepositoryImpl();

    @BeforeEach
    void setUp() { DbTestUtils.cleanDatabase(); }

    @Test
    void testAgendaCreation() {
        AgendaMensuel ag = AgendaMensuel.builder()
                .mois("Octobre 2023")
                .etat(true) // Ouvert
                .idMedecin(1L)
                .build();

        agendaRepo.create(ag);

        assertNotNull(ag.getIdAgenda());
        AgendaMensuel found = agendaRepo.findById(ag.getIdAgenda());
        assertEquals("Octobre 2023", found.getMois());
    }
}
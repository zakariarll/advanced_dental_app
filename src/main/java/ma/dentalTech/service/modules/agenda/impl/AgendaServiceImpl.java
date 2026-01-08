package ma.dentalTech.service.modules.agenda.impl;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.entities.agenda.AgendaMensuel;
import ma.dentalTech.repository.modules.agenda.api.AgendaRepository;
import ma.dentalTech.repository.modules.agenda.impl.mySQL.AgendaRepositoryImpl;
import ma.dentalTech.service.modules.agenda.api.AgendaService;

import java.util.List;

/**
 * Implémentation du service Agenda.
 * Note: La gestion des RDV est maintenant dans le module RDVPatient.
 */
public class AgendaServiceImpl implements AgendaService {

    private final AgendaRepository agendaRepo = new AgendaRepositoryImpl();

    @Override
    public void creerAgendaMensuel(AgendaMensuel agenda) throws ServiceException {
        if (agenda.getMois() == null || agenda.getMois().isEmpty()) {
            throw new ServiceException("Le mois est obligatoire (ex: 'Octobre 2023').");
        }
        agendaRepo.create(agenda);
    }

    @Override
    public List<AgendaMensuel> getAgendasMedecin(Long idMedecin) throws ServiceException {
        return agendaRepo.findByMedecin(idMedecin);
    }
}

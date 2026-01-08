package ma.dentalTech.service.modules.agenda.api;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.entities.agenda.AgendaMensuel;

import java.util.List;

/**
 * Interface service pour la gestion de l'agenda.
 * Note: La gestion des RDV est maintenant dans le module RDVPatient.
 */
public interface AgendaService {

    void creerAgendaMensuel(AgendaMensuel agenda) throws ServiceException;
    
    List<AgendaMensuel> getAgendasMedecin(Long idMedecin) throws ServiceException;
}

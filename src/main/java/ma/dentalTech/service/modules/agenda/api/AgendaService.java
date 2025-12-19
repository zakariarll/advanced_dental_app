package ma.dentalTech.service.modules.agenda.api;
import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.entities.agenda.AgendaMensuel;
import ma.dentalTech.entities.dossierMedical.RDV;

import java.time.LocalDate;
import java.util.List;
public interface AgendaService {
    void programmerRDV(RDV rdv) throws ServiceException;
    void modifierRDV(RDV rdv) throws ServiceException;
    void annulerRDV(Long idRDV) throws ServiceException;
    List<RDV> getRDVsDuJour(LocalDate date) throws ServiceException;
    List<RDV> getHistoriquePatient(Long idPatient) throws ServiceException;


    void creerAgendaMensuel(AgendaMensuel agenda) throws ServiceException;
    List<AgendaMensuel> getAgendasMedecin(Long idMedecin) throws ServiceException;
}

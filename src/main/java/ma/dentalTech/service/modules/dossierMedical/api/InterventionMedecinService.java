package ma.dentalTech.service.modules.dossierMedical.api;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.dossierMedical.InterventionMedecin;

import java.util.List;

public interface InterventionMedecinService {

    InterventionMedecin creerIntervention(InterventionMedecin intervention) throws ServiceException, ValidationException;

    InterventionMedecin modifierIntervention(InterventionMedecin intervention) throws ServiceException, ValidationException;

    void supprimerIntervention(Long idIntervention) throws ServiceException;

    InterventionMedecin obtenirIntervention(Long idIntervention) throws ServiceException;

    List<InterventionMedecin> listerToutesLesInterventions() throws ServiceException;

    List<InterventionMedecin> listerInterventionsParConsultation(Long idConsultation) throws ServiceException;

    Double calculerMontantTotalConsultation(Long idConsultation) throws ServiceException;
}
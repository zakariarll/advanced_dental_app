package ma.dentalTech.mvc.controllers.modules.dossierMedical.api;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.mvc.dto.dossierMedical.InterventionMedecinDTO;

public interface InterventionMedecinController {

    void afficherToutesLesInterventions() throws ServiceException;

    void afficherInterventionsParConsultation(Long idConsultation) throws ServiceException;

    void afficherIntervention(Long idIntervention) throws ServiceException;

    void creerIntervention(InterventionMedecinDTO interventionDTO) throws ServiceException, ValidationException;

    void modifierIntervention(InterventionMedecinDTO interventionDTO) throws ServiceException, ValidationException;

    void supprimerIntervention(Long idIntervention) throws ServiceException;

    void afficherMontantTotalConsultation(Long idConsultation) throws ServiceException;
}
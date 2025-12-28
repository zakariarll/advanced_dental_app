package ma.dentalTech.mvc.controllers.modules.actes.api;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;

public interface ActeInterventionController {

    void afficherActesParIntervention(Long idIM) throws ServiceException;

    void afficherInterventionsParActe(Long idActe) throws ServiceException;

    void ajouterActeAIntervention(Long idActe, Long idIM) throws ServiceException, ValidationException;

    void retirerActeDeIntervention(Long idActeIntervention) throws ServiceException;

    void retirerTousLesActesDeIntervention(Long idIM) throws ServiceException;

    void afficherMontantTotalIntervention(Long idIM) throws ServiceException;

    void afficherStatistiquesActe(Long idActe) throws ServiceException;
}
package ma.dentalTech.service.modules.actes.api;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.actes.Acte;
import ma.dentalTech.entities.actes.ActeIntervention;

import java.util.List;

public interface ActeInterventionService {

    ActeIntervention ajouterActeAIntervention(Long idActe, Long idIM) throws ServiceException, ValidationException;

    void retirerActeDeIntervention(Long idActeIntervention) throws ServiceException;

    void retirerTousLesActesDeIntervention(Long idIM) throws ServiceException;

    List<ActeIntervention> listerActesParIntervention(Long idIM) throws ServiceException;

    List<Acte> obtenirActesDetailsParIntervention(Long idIM) throws ServiceException;

    Double calculerMontantTotalIntervention(Long idIM) throws ServiceException;

    int compterUtilisationsActe(Long idActe) throws ServiceException;

    boolean acteEstUtiliseDansIntervention(Long idActe, Long idIM) throws ServiceException;

    List<ActeIntervention> listerInterventionsParActe(Long idActe) throws ServiceException;
}
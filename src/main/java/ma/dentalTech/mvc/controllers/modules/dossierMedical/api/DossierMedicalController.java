package ma.dentalTech.mvc.controllers.modules.dossierMedical.api;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.mvc.dto.dossierMedical.DossierMedicalDTO;

public interface DossierMedicalController {

    void afficherDossierComplet(Long idPatient) throws ServiceException;

    void verifierRisquesCritiques(Long idPatient) throws ServiceException;

    void afficherStatistiquesPatient(Long idPatient) throws ServiceException;
}
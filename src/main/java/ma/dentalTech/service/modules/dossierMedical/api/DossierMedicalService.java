package ma.dentalTech.service.modules.dossierMedical.api;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.entities.dossierMedical.DossierMedical;

public interface DossierMedicalService {

    DossierMedical obtenirDossierComplet(Long idPatient) throws ServiceException;

    boolean patientADesRisquesCritiques(Long idPatient) throws ServiceException;

    int compterConsultationsPatient(Long idPatient) throws ServiceException;
}
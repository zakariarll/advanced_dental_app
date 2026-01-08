package ma.dentalTech.mvc.controllers.modules.dossierMedical.api;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.mvc.dto.dossierMedical.ConsultationDTO;
import java.time.LocalDate;

public interface ConsultationController {

    void afficherToutesLesConsultations() throws ServiceException;

    void afficherConsultationsParDate(LocalDate date) throws ServiceException;

    void afficherConsultation(Long idConsultation) throws ServiceException;

    void afficherConsultationParRDV(Long idRDV) throws ServiceException;

    void creerConsultation(ConsultationDTO consultationDTO) throws ServiceException, ValidationException;

    void modifierConsultation(ConsultationDTO consultationDTO) throws ServiceException, ValidationException;

    void supprimerConsultation(Long idConsultation) throws ServiceException;

    void terminerConsultation(Long idConsultation, String observation) throws ServiceException, ValidationException;
}
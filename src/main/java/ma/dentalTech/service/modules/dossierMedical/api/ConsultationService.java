package ma.dentalTech.service.modules.dossierMedical.api;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.dossierMedical.Consultation;

import java.time.LocalDate;
import java.util.List;

public interface ConsultationService {

    Consultation creerConsultation(Consultation consultation) throws ServiceException, ValidationException;

    Consultation modifierConsultation(Consultation consultation) throws ServiceException, ValidationException;

    void supprimerConsultation(Long idConsultation) throws ServiceException;

    Consultation obtenirConsultation(Long idConsultation) throws ServiceException;

    List<Consultation> listerToutesLesConsultations() throws ServiceException;

    Consultation obtenirConsultationParRDV(Long idRDV) throws ServiceException;

    List<Consultation> listerConsultationsParDate(LocalDate date) throws ServiceException;

    void terminerConsultation(Long idConsultation, String observation) throws ServiceException, ValidationException;
}
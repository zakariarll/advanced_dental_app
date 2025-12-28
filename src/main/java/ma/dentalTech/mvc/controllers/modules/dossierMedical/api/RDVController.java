package ma.dentalTech.mvc.controllers.modules.dossierMedical.api;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.enums.StatutRDV;
import ma.dentalTech.mvc.dto.dossierMedical.RDVDTO;
import java.time.LocalDate;
import java.time.LocalTime;

public interface RDVController {

    void afficherTousLesRDV() throws ServiceException;

    void afficherRDVParPatient(Long idPatient) throws ServiceException;

    void afficherRDVParDate(LocalDate date) throws ServiceException;

    void afficherRDVParMedecin(Long idMedecin) throws ServiceException;

    void afficherRDV(Long idRDV) throws ServiceException;

    void creerRDV(RDVDTO rdvDTO) throws ServiceException, ValidationException;

    void modifierRDV(RDVDTO rdvDTO) throws ServiceException, ValidationException;

    void supprimerRDV(Long idRDV) throws ServiceException;

    void changerStatutRDV(Long idRDV, StatutRDV nouveauStatut) throws ServiceException, ValidationException;

    void confirmerRDV(Long idRDV) throws ServiceException, ValidationException;

    void annulerRDV(Long idRDV, String raison) throws ServiceException, ValidationException;

    void verifierDisponibilite(Long idMedecin, LocalDate date, LocalTime heure) throws ServiceException;
}
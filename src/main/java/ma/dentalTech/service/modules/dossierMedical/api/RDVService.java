package ma.dentalTech.service.modules.dossierMedical.api;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.dossierMedical.RDV;
import ma.dentalTech.entities.enums.StatutRDV;

import java.time.LocalDate;
import java.util.List;

public interface RDVService {

    RDV creerRDV(RDV rdv) throws ServiceException, ValidationException;

    RDV modifierRDV(RDV rdv) throws ServiceException, ValidationException;

    void supprimerRDV(Long idRDV) throws ServiceException;

    RDV obtenirRDV(Long idRDV) throws ServiceException;

    List<RDV> listerTousLesRDV() throws ServiceException;

    List<RDV> listerRDVParPatient(Long idPatient) throws ServiceException;

    List<RDV> listerRDVParDate(LocalDate date) throws ServiceException;

    List<RDV> listerRDVParMedecin(Long idMedecin) throws ServiceException;

    void changerStatutRDV(Long idRDV, StatutRDV nouveauStatut) throws ServiceException, ValidationException;

    void confirmerRDV(Long idRDV) throws ServiceException, ValidationException;

    void annulerRDV(Long idRDV, String raison) throws ServiceException, ValidationException;

    boolean verifierDisponibilite(Long idMedecin, LocalDate date, java.time.LocalTime heure) throws ServiceException;
}
package ma.dentalTech.service.modules.RDVPatient.api;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.dossierMedical.RDV;
import ma.dentalTech.entities.patient.Patient;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Interface regroupant tout le BLOC : GÉRER RENDEZ-VOUS PATIENTS
 */
public interface IGestionRDVService {

    // --- CAS : Planifier RDV (Include Consulter Agenda) ---
    RDV planifierRDV(Long idPatient, Long idMedecin, LocalDate date, LocalTime heure, String motif)
            throws ServiceException, ValidationException;

    // --- CAS : Confirmer RDV (Extends Envoyer Email) ---
    void confirmerRDV(Long idRDV) throws ServiceException;

    // --- CAS : Annuler RDV ---
    void annulerRDV(Long idRDV, String motifAnnulation) throws ServiceException;

    // --- CAS : Modifier RDV (Include Consulter Agenda) ---
    void modifierRDV(Long idRDV, LocalDate nouvelleDate, LocalTime nouvelleHeure)
            throws ServiceException, ValidationException;

    // --- CAS : Consulter Planning (Agenda) ---
    List<RDV> consulterPlanningMedecin(Long idMedecin, LocalDate date) throws ServiceException;

    // --- CAS : Consulter Historique RDV ---
    List<RDV> consulterHistoriquePatient(Long idPatient) throws ServiceException;

    // --- CAS : Gérer Liste d'Attente ---
    // Ajoute un patient en attente si le créneau souhaité est pris
    void ajouterEnListeAttente(Patient patient, LocalDate dateSouhaitee);
    List<Patient> getListeAttente(LocalDate date);
}
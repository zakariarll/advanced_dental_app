package ma.dentalTech.service.modules.patient.api;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.entities.patient.Patient;
import ma.dentalTech.mvc.dto.patient.PatientDTO;

import java.util.List;

public interface PatientService {

    // Créer un nouveau patient (avec validation)
    Patient ajouterPatient(Patient patient) throws ServiceException;

    // Modifier un patient existant
    Patient modifierPatient(Patient patient) throws ServiceException;

    // Supprimer un patient (par ID)
    void supprimerPatient(Long idPatient) throws ServiceException;

    // Trouver un patient par son ID
    Patient getPatientById(Long id) throws ServiceException;

    // Liste de tous les patients
    List<Patient> getAllPatients() throws ServiceException;

    // Rechercher par mot clé
    List<Patient> rechercherPatients(String motCle) throws ServiceException;

    // Vérifier si un patient existe (par téléphone par exemple)
    boolean patientExiste(String telephone) throws ServiceException;

    List<PatientDTO> getTodayPatientsAsDTO();
}
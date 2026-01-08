/* package ma.dentalTech.service.modules.patient.impl;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.entities.patient.Patient;
import ma.dentalTech.mvc.dto.PatientDTO;
import ma.dentalTech.repository.modules.patient.api.PatientRepository;
import ma.dentalTech.repository.modules.patient.impl.mySQL.PatientRepositoryImpl;
import ma.dentalTech.service.modules.patient.api.PatientService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class PatientServiceImpl implements PatientService {

    //  connecte Repository
    private final PatientRepository patientRepo = new PatientRepositoryImpl();

    @Override
    public Patient ajouterPatient(Patient patient) throws ServiceException {
        //  Validation
        validerDonneesPatient(patient);

        // 2. Vérifier unicité (Pas de doublon de téléphone)
        if (patient.getTelephone() != null && !patient.getTelephone().isBlank()) {
            Optional<Patient> existe = patientRepo.findByTelephone(patient.getTelephone());
            if (existe.isPresent()) {
                throw new ServiceException("Un patient existe déjà avec ce numéro de téléphone : " + patient.getTelephone());
            }
        }

        // 3. Appel au Repository
        try {
            patientRepo.create(patient);
            return patient; // Retourne l'objet avec son nouvel ID
        } catch (Exception e) {
            throw new ServiceException("Erreur technique lors de la création du patient : " + e.getMessage());
        }
    }

    @Override
    public Patient modifierPatient(Patient patient) throws ServiceException {
        // 1. Validation
        if (patient.getIdPatient() == null) {
            throw new ServiceException("Impossible de modifier : ID du patient manquant.");
        }
        validerDonneesPatient(patient);

        // 2. Vérifier si le patient existe vraiment
        Patient existant = patientRepo.findById(patient.getIdPatient());
        if (existant == null) {
            throw new ServiceException("Patient introuvable avec l'ID " + patient.getIdPatient());
        }

        // 3. Appel Repository
        try {
            patientRepo.update(patient);
            return patient;
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la modification : " + e.getMessage());
        }
    }

    @Override
    public void supprimerPatient(Long idPatient) throws ServiceException {
        if (idPatient == null) throw new ServiceException("ID invalide");

        try {
            if (!patientRepo.existsById(idPatient)) {
                throw new ServiceException("Patient introuvable, suppression impossible.");
            }
            patientRepo.deleteById(idPatient);
        } catch (Exception e) {
            throw new ServiceException("Impossible de supprimer (Le patient a peut-être des RDV ou Dossiers liés).");
        }
    }

    @Override
    public Patient getPatientById(Long id) throws ServiceException {
        if (id == null) return null;
        return patientRepo.findById(id);
    }

    @Override
    public List<Patient> getAllPatients() throws ServiceException {
        return patientRepo.findAll();
    }

    @Override
    public List<Patient> rechercherPatients(String motCle) throws ServiceException {
        if (motCle == null || motCle.isBlank()) {
            return getAllPatients();
        }
        return patientRepo.searchByNomOrPrenom(motCle);
    }

    @Override
    public boolean patientExiste(String telephone) {
        return patientRepo.findByTelephone(telephone).isPresent();
    }

    @Override
    public List<PatientDTO> getTodayPatientsAsDTO() {
        return List.of();
    }


    private void validerDonneesPatient(Patient p) throws ServiceException {
        if (p == null) {
            throw new ServiceException("Les données du patient sont vides.");
        }
        if (p.getNom() == null || p.getNom().trim().isEmpty()) {
            throw new ServiceException("Le nom du patient est obligatoire.");
        }
        if (p.getPrenom() == null || p.getPrenom().trim().isEmpty()) {
            throw new ServiceException("Le prénom du patient est obligatoire.");
        }
        // Validation format téléphone
        if (p.getTelephone() != null && !p.getTelephone().matches("^0[5-7][0-9]{8}$")) {

        }
        // Validation date de naissance
        if (p.getDateDeNaissance() != null && p.getDateDeNaissance().isAfter(LocalDate.now())) {
            throw new ServiceException("La date de naissance ne peut pas être dans le futur !");
        }
    }
}

 */
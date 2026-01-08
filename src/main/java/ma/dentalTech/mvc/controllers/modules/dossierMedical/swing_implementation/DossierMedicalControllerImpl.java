package ma.dentalTech.mvc.controllers.modules.dossierMedical.swing_implementation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.entities.dossierMedical.Consultation;
import ma.dentalTech.entities.dossierMedical.DossierMedical;
import ma.dentalTech.entities.patient.Antecedent;
import ma.dentalTech.mvc.controllers.modules.dossierMedical.api.DossierMedicalController;
import ma.dentalTech.mvc.dto.patient.AntecedentDTO;
import ma.dentalTech.mvc.dto.patient.PatientDTO;
import ma.dentalTech.mvc.dto.dossierMedical.ConsultationDTO;
import ma.dentalTech.mvc.dto.dossierMedical.DossierMedicalDTO;
import ma.dentalTech.mvc.ui.modules.dossierMedical.DossierMedicalView;
import ma.dentalTech.service.modules.dossierMedical.api.DossierMedicalService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DossierMedicalControllerImpl implements DossierMedicalController {

    private DossierMedicalService dossierMedicalService;

    @Override
    public void afficherDossierComplet(Long idPatient) throws ServiceException {
        try {
            DossierMedical dossier = dossierMedicalService.obtenirDossierComplet(idPatient);
            DossierMedicalDTO dto = convertirDossierEnDTO(dossier);
            DossierMedicalView.afficherDossierComplet(dto);
        } catch (ServiceException e) {
            DossierMedicalView.afficherErreur("Erreur lors de la récupération du dossier médical: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void verifierRisquesCritiques(Long idPatient) throws ServiceException {
        try {
            boolean aDesRisques = dossierMedicalService.patientADesRisquesCritiques(idPatient);
            if (aDesRisques) {
                DossierMedicalView.afficherAlerte("ATTENTION: Le patient présente des risques critiques!");
            } else {
                DossierMedicalView.afficherSucces("Aucun risque critique détecté");
            }
        } catch (ServiceException e) {
            DossierMedicalView.afficherErreur("Erreur lors de la vérification des risques: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void afficherStatistiquesPatient(Long idPatient) throws ServiceException {
        try {
            int nombreConsultations = dossierMedicalService.compterConsultationsPatient(idPatient);
            boolean risquesCritiques = dossierMedicalService.patientADesRisquesCritiques(idPatient);

            DossierMedicalView.afficherStatistiques(nombreConsultations, risquesCritiques);
        } catch (ServiceException e) {
            DossierMedicalView.afficherErreur("Erreur lors de la récupération des statistiques: " + e.getMessage());
            throw e;
        }
    }

    private DossierMedicalDTO convertirDossierEnDTO(DossierMedical dossier) {
        if (dossier == null) {
            return null;
        }

        PatientDTO patientDTO = convertirPatientEnDTO(dossier.getPatient());
        List<AntecedentDTO> antecedentsDTO = convertirListeAntecedentsEnDTO(dossier.getAntecedents());
        List<ConsultationDTO> consultationsDTO = convertirListeConsultationsEnDTO(dossier.getConsultations());

        return DossierMedicalDTO.builder()
                .patient(patientDTO)
                .antecedents(antecedentsDTO)
                .consultations(consultationsDTO)
                .aDesRisquesCritiques(dossier.aDesRisquesCritiques())
                .nombreConsultations(dossier.getConsultations() != null ? dossier.getConsultations().size() : 0)
                .statutRisque(dossier.aDesRisquesCritiques() ? "CRITIQUE" : "NORMAL")
                .build();
    }

    private PatientDTO convertirPatientEnDTO(ma.dentalTech.entities.patient.Patient patient) {
        if (patient == null) {
            return null;
        }
        return PatientDTO.builder()
                .idPatient(patient.getIdPatient())
                .nom(patient.getNom())
                .prenom(patient.getPrenom())
                .dateDeNaissance(patient.getDateDeNaissance())
                .sexe(patient.getSexe())
                .adresse(patient.getAdresse())
                .telephone(patient.getTelephone())
                .assurance(patient.getAssurance())
                .build();
    }

    private List<AntecedentDTO> convertirListeAntecedentsEnDTO(List<Antecedent> antecedents) {
        if (antecedents == null) {
            return List.of();
        }
        return antecedents.stream()
                .map(this::convertirAntecedentEnDTO)
                .collect(Collectors.toList());
    }

    private AntecedentDTO convertirAntecedentEnDTO(Antecedent antecedent) {
        if (antecedent == null) {
            return null;
        }
        return new AntecedentDTO(
                antecedent.getIdAntecedent(),
                antecedent.getNom(),
                antecedent.getCategorie(),
                antecedent.getNiveauRisque(),
                antecedent.getIdPatient()
        );
    }

    private List<ConsultationDTO> convertirListeConsultationsEnDTO(List<Consultation> consultations) {
        if (consultations == null) {
            return List.of();
        }
        return consultations.stream()
                .map(this::convertirConsultationEnDTO)
                .collect(Collectors.toList());
    }

    private ConsultationDTO convertirConsultationEnDTO(Consultation consultation) {
        if (consultation == null) {
            return null;
        }
        return ConsultationDTO.builder()
                .idConsultation(consultation.getIdConsultation())
                .date(consultation.getDate())
                .statut(consultation.getStatut())
                .observationMedecin(consultation.getObservationMedecin())
                .idRDV(consultation.getIdRDV())
                .dateFormatee(formaterDate(consultation.getDate()))
                .statutAffichage(consultation.getStatut())
                .build();
    }

    private String formaterDate(LocalDate date) {
        if (date == null) {
            return "Non définie";
        }
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
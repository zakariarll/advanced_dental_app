package ma.dentalTech.mvc.controllers.modules.dossierMedical.swing_implementation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.dossierMedical.Consultation;
import ma.dentalTech.mvc.controllers.modules.dossierMedical.api.ConsultationController;
import ma.dentalTech.mvc.dto.dossierMedical.ConsultationDTO;
import ma.dentalTech.mvc.ui.modules.dossierMedical.ConsultationView;
import ma.dentalTech.service.modules.dossierMedical.api.ConsultationService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultationControllerImpl implements ConsultationController {

    private ConsultationService consultationService;

    @Override
    public void afficherToutesLesConsultations() throws ServiceException {
        try {
            List<Consultation> consultations = consultationService.listerToutesLesConsultations();
            List<ConsultationDTO> dtos = convertirListeConsultationsEnDTO(consultations);
            ConsultationView.afficherListeConsultations(dtos, "Toutes les consultations");
        } catch (ServiceException e) {
            ConsultationView.afficherErreur("Erreur lors de la récupération des consultations: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void afficherConsultationsParDate(LocalDate date) throws ServiceException {
        try {
            List<Consultation> consultations = consultationService.listerConsultationsParDate(date);
            List<ConsultationDTO> dtos = convertirListeConsultationsEnDTO(consultations);
            ConsultationView.afficherListeConsultations(dtos, "Consultations du " + formaterDate(date));
        } catch (ServiceException e) {
            ConsultationView.afficherErreur("Erreur lors de la récupération des consultations par date: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void afficherConsultation(Long idConsultation) throws ServiceException {
        try {
            Consultation consultation = consultationService.obtenirConsultation(idConsultation);
            ConsultationDTO dto = convertirConsultationEnDTO(consultation);
            ConsultationView.afficherDetailConsultation(dto);
        } catch (ServiceException e) {
            ConsultationView.afficherErreur("Erreur lors de la récupération de la consultation: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void afficherConsultationParRDV(Long idRDV) throws ServiceException {
        try {
            Consultation consultation = consultationService.obtenirConsultationParRDV(idRDV);
            if (consultation == null) {
                ConsultationView.afficherErreur("Aucune consultation trouvée pour ce RDV");
                return;
            }
            ConsultationDTO dto = convertirConsultationEnDTO(consultation);
            ConsultationView.afficherDetailConsultation(dto);
        } catch (ServiceException e) {
            ConsultationView.afficherErreur("Erreur lors de la récupération de la consultation: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void creerConsultation(ConsultationDTO consultationDTO) throws ServiceException, ValidationException {
        try {
            Consultation consultation = convertirDTOEnConsultation(consultationDTO);
            consultationService.creerConsultation(consultation);
            ConsultationView.afficherSucces("Consultation créée avec succès");
        } catch (ServiceException | ValidationException e) {
            ConsultationView.afficherErreur("Erreur lors de la création de la consultation: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void modifierConsultation(ConsultationDTO consultationDTO) throws ServiceException, ValidationException {
        try {
            Consultation consultation = convertirDTOEnConsultation(consultationDTO);
            consultationService.modifierConsultation(consultation);
            ConsultationView.afficherSucces("Consultation modifiée avec succès");
        } catch (ServiceException | ValidationException e) {
            ConsultationView.afficherErreur("Erreur lors de la modification de la consultation: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void supprimerConsultation(Long idConsultation) throws ServiceException {
        try {
            consultationService.supprimerConsultation(idConsultation);
            ConsultationView.afficherSucces("Consultation supprimée avec succès");
        } catch (ServiceException e) {
            ConsultationView.afficherErreur("Erreur lors de la suppression de la consultation: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void terminerConsultation(Long idConsultation, String observation) throws ServiceException, ValidationException {
        try {
            consultationService.terminerConsultation(idConsultation, observation);
            ConsultationView.afficherSucces("Consultation terminée avec succès");
        } catch (ServiceException | ValidationException e) {
            ConsultationView.afficherErreur("Erreur lors de la finalisation de la consultation: " + e.getMessage());
            throw e;
        }
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
                .statutAffichage(formaterStatut(consultation.getStatut()))
                .build();
    }

    private Consultation convertirDTOEnConsultation(ConsultationDTO dto) {
        if (dto == null) {
            return null;
        }
        return Consultation.builder()
                .idConsultation(dto.getIdConsultation())
                .date(dto.getDate())
                .statut(dto.getStatut())
                .observationMedecin(dto.getObservationMedecin())
                .idRDV(dto.getIdRDV())
                .build();
    }

    private List<ConsultationDTO> convertirListeConsultationsEnDTO(List<Consultation> consultations) {
        return consultations.stream()
                .map(this::convertirConsultationEnDTO)
                .collect(Collectors.toList());
    }

    private String formaterDate(LocalDate date) {
        if (date == null) {
            return "Non définie";
        }
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private String formaterStatut(String statut) {
        if (statut == null) {
            return "INCONNU";
        }
        switch (statut.toUpperCase()) {
            case "EN_COURS":
                return "En cours";
            case "TERMINEE":
                return "Terminée";
            case "ANNULEE":
                return "Annulée";
            default:
                return statut;
        }
    }
}
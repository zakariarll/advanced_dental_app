package ma.dentalTech.mvc.controllers.modules.dossierMedical.swing_implementation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.dossierMedical.InterventionMedecin;
import ma.dentalTech.mvc.controllers.modules.dossierMedical.api.InterventionMedecinController;
import ma.dentalTech.mvc.dto.dossierMedical.InterventionMedecinDTO;
import ma.dentalTech.mvc.ui.modules.dossierMedical.InterventionMedecinView;
import ma.dentalTech.service.modules.dossierMedical.api.InterventionMedecinService;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterventionMedecinControllerImpl implements InterventionMedecinController {

    private InterventionMedecinService interventionService;

    @Override
    public void afficherToutesLesInterventions() throws ServiceException {
        try {
            List<InterventionMedecin> interventions = interventionService.listerToutesLesInterventions();
            List<InterventionMedecinDTO> dtos = convertirListeInterventionsEnDTO(interventions);
            InterventionMedecinView.afficherListeInterventions(dtos, "Toutes les interventions");
        } catch (ServiceException e) {
            InterventionMedecinView.afficherErreur("Erreur lors de la récupération des interventions: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void afficherInterventionsParConsultation(Long idConsultation) throws ServiceException {
        try {
            List<InterventionMedecin> interventions = interventionService.listerInterventionsParConsultation(idConsultation);
            List<InterventionMedecinDTO> dtos = convertirListeInterventionsEnDTO(interventions);
            InterventionMedecinView.afficherListeInterventions(dtos, "Interventions de la consultation #" + idConsultation);
        } catch (ServiceException e) {
            InterventionMedecinView.afficherErreur("Erreur lors de la récupération des interventions: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void afficherIntervention(Long idIntervention) throws ServiceException {
        try {
            InterventionMedecin intervention = interventionService.obtenirIntervention(idIntervention);
            InterventionMedecinDTO dto = convertirInterventionEnDTO(intervention);
            InterventionMedecinView.afficherDetailIntervention(dto);
        } catch (ServiceException e) {
            InterventionMedecinView.afficherErreur("Erreur lors de la récupération de l'intervention: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void creerIntervention(InterventionMedecinDTO interventionDTO) throws ServiceException, ValidationException {
        try {
            InterventionMedecin intervention = convertirDTOEnIntervention(interventionDTO);
            interventionService.creerIntervention(intervention);
            InterventionMedecinView.afficherSucces("Intervention créée avec succès");
        } catch (ServiceException | ValidationException e) {
            InterventionMedecinView.afficherErreur("Erreur lors de la création de l'intervention: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void modifierIntervention(InterventionMedecinDTO interventionDTO) throws ServiceException, ValidationException {
        try {
            InterventionMedecin intervention = convertirDTOEnIntervention(interventionDTO);
            interventionService.modifierIntervention(intervention);
            InterventionMedecinView.afficherSucces("Intervention modifiée avec succès");
        } catch (ServiceException | ValidationException e) {
            InterventionMedecinView.afficherErreur("Erreur lors de la modification de l'intervention: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void supprimerIntervention(Long idIntervention) throws ServiceException {
        try {
            interventionService.supprimerIntervention(idIntervention);
            InterventionMedecinView.afficherSucces("Intervention supprimée avec succès");
        } catch (ServiceException e) {
            InterventionMedecinView.afficherErreur("Erreur lors de la suppression de l'intervention: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void afficherMontantTotalConsultation(Long idConsultation) throws ServiceException {
        try {
            Double montantTotal = interventionService.calculerMontantTotalConsultation(idConsultation);
            InterventionMedecinView.afficherMontantTotal(montantTotal, idConsultation);
        } catch (ServiceException e) {
            InterventionMedecinView.afficherErreur("Erreur lors du calcul du montant: " + e.getMessage());
            throw e;
        }
    }

    private InterventionMedecinDTO convertirInterventionEnDTO(InterventionMedecin intervention) {
        if (intervention == null) {
            return null;
        }
        return InterventionMedecinDTO.builder()
                .idIM(intervention.getIdIM())
                .prixDePatient(intervention.getPrixDePatient())
                .idConsultation(intervention.getIdConsultation())
                .idMedecin(intervention.getIdMedecin())
                .prixFormate(formaterPrix(intervention.getPrixDePatient()))
                .build();
    }

    private InterventionMedecin convertirDTOEnIntervention(InterventionMedecinDTO dto) {
        if (dto == null) {
            return null;
        }
        return InterventionMedecin.builder()
                .idIM(dto.getIdIM())
                .prixDePatient(dto.getPrixDePatient())
                .idConsultation(dto.getIdConsultation())
                .idMedecin(dto.getIdMedecin())
                .build();
    }

    private List<InterventionMedecinDTO> convertirListeInterventionsEnDTO(List<InterventionMedecin> interventions) {
        return interventions.stream()
                .map(this::convertirInterventionEnDTO)
                .collect(Collectors.toList());
    }

    private String formaterPrix(Double prix) {
        if (prix == null) {
            return "0.00 MAD";
        }
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("fr", "MA"));
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(2);
        return prix + " MAD";
    }
}
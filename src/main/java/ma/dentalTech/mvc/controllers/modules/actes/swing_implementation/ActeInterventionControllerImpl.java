package ma.dentalTech.mvc.controllers.modules.actes.swing_implementation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.actes.Acte;
import ma.dentalTech.entities.actes.ActeIntervention;
import ma.dentalTech.mvc.controllers.modules.actes.api.ActeInterventionController;
import ma.dentalTech.mvc.dto.actes.ActeDTO;
import ma.dentalTech.mvc.dto.actes.ActeInterventionDTO;
import ma.dentalTech.mvc.ui.modules.actes.ActeInterventionView;
import ma.dentalTech.service.modules.actes.api.ActeInterventionService;
import ma.dentalTech.service.modules.actes.api.ActeService;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActeInterventionControllerImpl implements ActeInterventionController {

    private ActeInterventionService acteInterventionService;
    private ActeService acteService;

    @Override
    public void afficherActesParIntervention(Long idIM) throws ServiceException {
        try {
            List<Acte> actes = acteInterventionService.obtenirActesDetailsParIntervention(idIM);
            List<ActeDTO> dtos = convertirListeActesEnDTO(actes);
            Double montantTotal = acteInterventionService.calculerMontantTotalIntervention(idIM);
            ActeInterventionView.afficherActesIntervention(dtos, montantTotal, "Actes de l'intervention #" + idIM);
        } catch (ServiceException e) {
            ActeInterventionView.afficherErreur("Erreur lors de la récupération des actes: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void afficherInterventionsParActe(Long idActe) throws ServiceException {
        try {
            List<ActeIntervention> interventions = acteInterventionService.listerInterventionsParActe(idActe);
            List<ActeInterventionDTO> dtos = convertirListeActeInterventionEnDTO(interventions);
            int nbUtilisations = acteInterventionService.compterUtilisationsActe(idActe);
            ActeInterventionView.afficherInterventionsActe(dtos, nbUtilisations, "Interventions utilisant l'acte #" + idActe);
        } catch (ServiceException e) {
            ActeInterventionView.afficherErreur("Erreur lors de la récupération des interventions: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void ajouterActeAIntervention(Long idActe, Long idIM) throws ServiceException, ValidationException {
        try {
            acteInterventionService.ajouterActeAIntervention(idActe, idIM);
            ActeInterventionView.afficherSucces("Acte ajouté à l'intervention avec succès");
            afficherActesParIntervention(idIM);
        } catch (ServiceException | ValidationException e) {
            ActeInterventionView.afficherErreur("Erreur lors de l'ajout de l'acte: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void retirerActeDeIntervention(Long idActeIntervention) throws ServiceException {
        try {
            acteInterventionService.retirerActeDeIntervention(idActeIntervention);
            ActeInterventionView.afficherSucces("Acte retiré de l'intervention avec succès");
        } catch (ServiceException e) {
            ActeInterventionView.afficherErreur("Erreur lors du retrait de l'acte: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void retirerTousLesActesDeIntervention(Long idIM) throws ServiceException {
        try {
            acteInterventionService.retirerTousLesActesDeIntervention(idIM);
            ActeInterventionView.afficherSucces("Tous les actes ont été retirés de l'intervention");
        } catch (ServiceException e) {
            ActeInterventionView.afficherErreur("Erreur lors du retrait des actes: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void afficherMontantTotalIntervention(Long idIM) throws ServiceException {
        try {
            Double montantTotal = acteInterventionService.calculerMontantTotalIntervention(idIM);
            ActeInterventionView.afficherMontantTotal(montantTotal, formaterPrix(montantTotal));
        } catch (ServiceException e) {
            ActeInterventionView.afficherErreur("Erreur lors du calcul du montant total: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void afficherStatistiquesActe(Long idActe) throws ServiceException {
        try {
            int nbUtilisations = acteInterventionService.compterUtilisationsActe(idActe);
            Acte acte = acteService.obtenirActe(idActe);
            ActeDTO acteDTO = convertirActeEnDTO(acte);
            ActeInterventionView.afficherStatistiquesActe(acteDTO, nbUtilisations);
        } catch (ServiceException e) {
            ActeInterventionView.afficherErreur("Erreur lors de la récupération des statistiques: " + e.getMessage());
            throw e;
        }
    }

    private ActeDTO convertirActeEnDTO(Acte acte) {
        if (acte == null) {
            return null;
        }
        return ActeDTO.builder()
                .idActe(acte.getIdActe())
                .libelle(acte.getLibelle())
                .categorie(acte.getCategorie())
                .prixDeBase(acte.getPrixDeBase())
                .description(acte.getDescription())
                .code(acte.getCode())
                .prixFormate(formaterPrix(acte.getPrixDeBase()))
                .build();
    }

    private List<ActeDTO> convertirListeActesEnDTO(List<Acte> actes) {
        List<ActeDTO> dtos = new ArrayList<>();
        for (Acte acte : actes) {
            dtos.add(convertirActeEnDTO(acte));
        }
        return dtos;
    }

    private ActeInterventionDTO convertirActeInterventionEnDTO(ActeIntervention ai) {
        if (ai == null) {
            return null;
        }
        try {
            Acte acte = acteService.obtenirActe(ai.getIdActe());
            return ActeInterventionDTO.builder()
                    .idActeIntervention(ai.getIdActeIntervention())
                    .idActe(ai.getIdActe())
                    .idIM(ai.getIdIM())
                    .libelleActe(acte != null ? acte.getLibelle() : "N/A")
                    .categorieActe(acte != null ? acte.getCategorie() : "N/A")
                    .prixActe(acte != null ? acte.getPrixDeBase() : 0.0)
                    .prixFormate(acte != null ? formaterPrix(acte.getPrixDeBase()) : "0.00 MAD")
                    .build();
        } catch (ServiceException e) {
            return ActeInterventionDTO.builder()
                    .idActeIntervention(ai.getIdActeIntervention())
                    .idActe(ai.getIdActe())
                    .idIM(ai.getIdIM())
                    .libelleActe("Erreur")
                    .categorieActe("Erreur")
                    .prixActe(0.0)
                    .prixFormate("0.00 MAD")
                    .build();
        }
    }

    private List<ActeInterventionDTO> convertirListeActeInterventionEnDTO(List<ActeIntervention> actesInterventions) {
        List<ActeInterventionDTO> dtos = new ArrayList<>();
        for (ActeIntervention ai : actesInterventions) {
            dtos.add(convertirActeInterventionEnDTO(ai));
        }
        return dtos;
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
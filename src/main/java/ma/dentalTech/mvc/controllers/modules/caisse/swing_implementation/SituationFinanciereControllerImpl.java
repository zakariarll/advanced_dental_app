package ma.dentalTech.mvc.controllers.modules.caisse.swing_implementation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.caisse.SituationFinanciere;
import ma.dentalTech.mvc.controllers.modules.caisse.api.SituationFinanciereController;
import ma.dentalTech.mvc.dto.caisse.SituationFinanciereDTO;
import ma.dentalTech.mvc.ui.modules.caisse.SituationFinanciereView;
import ma.dentalTech.service.modules.caisse.api.SituationFinanciereService;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SituationFinanciereControllerImpl implements SituationFinanciereController {

    private SituationFinanciereService situationFinanciereService;

    @Override
    public void afficherToutesLesSituations() {
        try {
            List<SituationFinanciere> situations = situationFinanciereService.listerToutesLesSituations();
            List<SituationFinanciereDTO> situationDTOs = situations.stream()
                    .map(this::convertirEnDTO)
                    .collect(Collectors.toList());
            SituationFinanciereView.afficherListeSituations(situationDTOs, "Toutes les situations financières");
        } catch (ServiceException e) {
            SituationFinanciereView.afficherErreur("Erreur lors de la récupération des situations: " + e.getMessage());
        }
    }

    @Override
    public void afficherSituationParFacture(Long idFacture) {
        try {
            SituationFinanciere situation = situationFinanciereService.obtenirSituationParFacture(idFacture);
            if (situation != null) {
                SituationFinanciereView.afficherDetailSituation(convertirEnDTO(situation));
            }
        } catch (ServiceException e) {
            SituationFinanciereView.afficherErreur("Erreur lors de la récupération de la situation: " + e.getMessage());
        }
    }

    @Override
    public void afficherSituationsParStatut(String statut) {
        try {
            List<SituationFinanciere> situations = situationFinanciereService.listerSituationsParStatut(statut);
            List<SituationFinanciereDTO> situationDTOs = situations.stream()
                    .map(this::convertirEnDTO)
                    .collect(Collectors.toList());
            SituationFinanciereView.afficherListeSituations(situationDTOs, "Situations - Statut: " + statut);
        } catch (ServiceException e) {
            SituationFinanciereView.afficherErreur("Erreur lors de la récupération des situations: " + e.getMessage());
        }
    }

    @Override
    public void afficherSituationsParPatient(Long idPatient) {
        try {
            List<SituationFinanciere> situations = situationFinanciereService.listerSituationsParPatient(idPatient);
            List<SituationFinanciereDTO> situationDTOs = situations.stream()
                    .map(this::convertirEnDTO)
                    .collect(Collectors.toList());
            SituationFinanciereView.afficherListeSituations(situationDTOs, "Situations du patient #" + idPatient);
        } catch (ServiceException e) {
            SituationFinanciereView.afficherErreur("Erreur lors de la récupération des situations: " + e.getMessage());
        }
    }

    @Override
    public void afficherSituationsAvecCredit() {
        try {
            List<SituationFinanciere> situations = situationFinanciereService.listerSituationsAvecCredit();
            List<SituationFinanciereDTO> situationDTOs = situations.stream()
                    .map(this::convertirEnDTO)
                    .collect(Collectors.toList());
            SituationFinanciereView.afficherListeSituations(situationDTOs, "Situations avec crédit");
        } catch (ServiceException e) {
            SituationFinanciereView.afficherErreur("Erreur lors de la récupération des situations: " + e.getMessage());
        }
    }

    @Override
    public void reinitialiserSituation(Long idSF) {
        try {
            SituationFinanciere situation = situationFinanciereService.reinitialiserSituation(idSF);
            SituationFinanciereView.afficherSucces("Situation réinitialisée avec succès");
            SituationFinanciereView.afficherDetailSituation(convertirEnDTO(situation));
        } catch (ServiceException | ValidationException e) {
            SituationFinanciereView.afficherErreur("Erreur lors de la réinitialisation: " + e.getMessage());
        }
    }

    @Override
    public void appliquerPromotion(Long idSF, String promo, Double pourcentage) {
        try {
            SituationFinanciere situation = situationFinanciereService.appliquerPromotion(idSF, promo, pourcentage);
            SituationFinanciereView.afficherSucces("Promotion appliquée avec succès");
            SituationFinanciereView.afficherDetailSituation(convertirEnDTO(situation));
        } catch (ServiceException | ValidationException e) {
            SituationFinanciereView.afficherErreur("Erreur lors de l'application de la promotion: " + e.getMessage());
        }
    }

    @Override
    public void afficherTotalCredits() {
        try {
            Double total = situationFinanciereService.calculerTotalCredits();
            SituationFinanciereView.afficherMontantTotal(total, "Total des crédits");
        } catch (ServiceException e) {
            SituationFinanciereView.afficherErreur("Erreur lors du calcul: " + e.getMessage());
        }
    }

    @Override
    public void afficherCreditsPatient(Long idPatient) {
        try {
            Double total = situationFinanciereService.calculerCreditsPatient(idPatient);
            SituationFinanciereView.afficherMontantTotal(total, "Crédits du patient #" + idPatient);
        } catch (ServiceException e) {
            SituationFinanciereView.afficherErreur("Erreur lors du calcul: " + e.getMessage());
        }
    }

    @Override
    public void mettreAJourDepuisFacture(Long idFacture) {
        try {
            SituationFinanciere situation = situationFinanciereService.mettreAJourDepuisFacture(idFacture);
            SituationFinanciereView.afficherSucces("Situation mise à jour avec succès");
            SituationFinanciereView.afficherDetailSituation(convertirEnDTO(situation));
        } catch (ServiceException | ValidationException e) {
            SituationFinanciereView.afficherErreur("Erreur lors de la mise à jour: " + e.getMessage());
        }
    }

    @Override
    public void creerSituationFinanciere(SituationFinanciereDTO situationDTO) {
        try {
            SituationFinanciere situation = convertirEnEntite(situationDTO);
            SituationFinanciere nouvelleSituation = situationFinanciereService.creerSituationFinanciere(situation);
            SituationFinanciereView.afficherSucces("Situation créée avec succès (ID: " + nouvelleSituation.getIdSF() + ")");
        } catch (ServiceException | ValidationException e) {
            SituationFinanciereView.afficherErreur("Erreur lors de la création: " + e.getMessage());
        }
    }

    @Override
    public void modifierSituationFinanciere(SituationFinanciereDTO situationDTO) {
        try {
            SituationFinanciere situation = convertirEnEntite(situationDTO);
            situationFinanciereService.modifierSituationFinanciere(situation);
            SituationFinanciereView.afficherSucces("Situation modifiée avec succès");
        } catch (ServiceException | ValidationException e) {
            SituationFinanciereView.afficherErreur("Erreur lors de la modification: " + e.getMessage());
        }
    }

    @Override
    public void supprimerSituationFinanciere(Long idSF) {
        try {
            situationFinanciereService.supprimerSituationFinanciere(idSF);
            SituationFinanciereView.afficherSucces("Situation supprimée avec succès");
        } catch (ServiceException e) {
            SituationFinanciereView.afficherErreur("Erreur lors de la suppression: " + e.getMessage());
        }
    }

    private SituationFinanciereDTO convertirEnDTO(SituationFinanciere situation) {
        return SituationFinanciereDTO.builder()
                .idSF(situation.getIdSF())
                .totaleDesActes(situation.getTotaleDesActes())
                .totalePaye(situation.getTotalePaye())
                .credit(situation.getCredit())
                .statut(situation.getStatut())
                .enPromo(situation.getEnPromo())
                .idFacture(situation.getIdFacture())
                .build();
    }

    private SituationFinanciere convertirEnEntite(SituationFinanciereDTO dto) {
        return SituationFinanciere.builder()
                .idSF(dto.getIdSF())
                .totaleDesActes(dto.getTotaleDesActes())
                .totalePaye(dto.getTotalePaye())
                .credit(dto.getCredit())
                .statut(dto.getStatut())
                .enPromo(dto.getEnPromo())
                .idFacture(dto.getIdFacture())
                .build();
    }
}
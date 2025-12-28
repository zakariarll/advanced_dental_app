package ma.dentalTech.mvc.controllers.modules.caisse.swing_implementation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.caisse.Charge;
import ma.dentalTech.mvc.controllers.modules.caisse.api.ChargeController;
import ma.dentalTech.mvc.dto.caisse.ChargeDTO;
import ma.dentalTech.mvc.ui.modules.caisse.ChargeView;
import ma.dentalTech.service.modules.caisse.api.ChargeService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChargeControllerImpl implements ChargeController {

    private ChargeService chargeService;

    @Override
    public void afficherToutesLesCharges() {
        try {
            List<Charge> charges = chargeService.listerToutesLesCharges();
            List<ChargeDTO> chargeDTOs = charges.stream()
                    .map(this::convertirEnDTO)
                    .collect(Collectors.toList());
            ChargeView.afficherListeCharges(chargeDTOs, "Toutes les charges");
        } catch (ServiceException e) {
            ChargeView.afficherErreur("Erreur lors de la récupération des charges: " + e.getMessage());
        }
    }

    @Override
    public void afficherChargesParCabinet(Long idCabinet) {
        try {
            List<Charge> charges = chargeService.listerChargesParCabinet(idCabinet);
            List<ChargeDTO> chargeDTOs = charges.stream()
                    .map(this::convertirEnDTO)
                    .collect(Collectors.toList());
            ChargeView.afficherListeCharges(chargeDTOs, "Charges du cabinet #" + idCabinet);
        } catch (ServiceException e) {
            ChargeView.afficherErreur("Erreur lors de la récupération des charges: " + e.getMessage());
        }
    }

    @Override
    public void afficherChargesParPeriode(LocalDateTime debut, LocalDateTime fin) {
        try {
            List<Charge> charges = chargeService.listerChargesParPeriode(debut, fin);
            List<ChargeDTO> chargeDTOs = charges.stream()
                    .map(this::convertirEnDTO)
                    .collect(Collectors.toList());
            ChargeView.afficherListeCharges(chargeDTOs, "Charges de la période");
        } catch (ServiceException e) {
            ChargeView.afficherErreur("Erreur lors de la récupération des charges: " + e.getMessage());
        }
    }

    @Override
    public void rechercherChargesParTitre(String titre) {
        try {
            List<Charge> charges = chargeService.rechercherChargesParTitre(titre);
            List<ChargeDTO> chargeDTOs = charges.stream()
                    .map(this::convertirEnDTO)
                    .collect(Collectors.toList());
            ChargeView.afficherListeCharges(chargeDTOs, "Résultats de recherche: " + titre);
        } catch (ServiceException e) {
            ChargeView.afficherErreur("Erreur lors de la recherche: " + e.getMessage());
        }
    }

    @Override
    public void afficherTotalCharges(Long idCabinet) {
        try {
            Double total = chargeService.calculerTotalCharges(idCabinet);
            ChargeView.afficherMontantTotal(total, "Total des charges du cabinet");
        } catch (ServiceException e) {
            ChargeView.afficherErreur("Erreur lors du calcul: " + e.getMessage());
        }
    }

    @Override
    public void afficherTotalChargesParPeriode(Long idCabinet, LocalDateTime debut, LocalDateTime fin) {
        try {
            Double total = chargeService.calculerTotalChargesParPeriode(idCabinet, debut, fin);
            ChargeView.afficherMontantTotal(total, "Total des charges de la période");
        } catch (ServiceException e) {
            ChargeView.afficherErreur("Erreur lors du calcul: " + e.getMessage());
        }
    }

    @Override
    public void creerCharge(ChargeDTO chargeDTO) {
        try {
            Charge charge = convertirEnEntite(chargeDTO);
            Charge nouvelleCharge = chargeService.creerCharge(charge);
            ChargeView.afficherSucces("Charge créée avec succès (ID: " + nouvelleCharge.getIdCharge() + ")");
        } catch (ServiceException | ValidationException e) {
            ChargeView.afficherErreur("Erreur lors de la création: " + e.getMessage());
        }
    }

    @Override
    public void modifierCharge(ChargeDTO chargeDTO) {
        try {
            Charge charge = convertirEnEntite(chargeDTO);
            chargeService.modifierCharge(charge);
            ChargeView.afficherSucces("Charge modifiée avec succès");
        } catch (ServiceException | ValidationException e) {
            ChargeView.afficherErreur("Erreur lors de la modification: " + e.getMessage());
        }
    }

    @Override
    public void supprimerCharge(Long idCharge) {
        try {
            chargeService.supprimerCharge(idCharge);
            ChargeView.afficherSucces("Charge supprimée avec succès");
        } catch (ServiceException e) {
            ChargeView.afficherErreur("Erreur lors de la suppression: " + e.getMessage());
        }
    }

    private ChargeDTO convertirEnDTO(Charge charge) {
        return ChargeDTO.builder()
                .idCharge(charge.getIdCharge())
                .titre(charge.getTitre())
                .description(charge.getDescription())
                .montant(charge.getMontant())
                .date(charge.getDate())
                .idCabinet(charge.getIdCabinet())
                .build();
    }

    private Charge convertirEnEntite(ChargeDTO dto) {
        return Charge.builder()
                .idCharge(dto.getIdCharge())
                .titre(dto.getTitre())
                .description(dto.getDescription())
                .montant(dto.getMontant())
                .date(dto.getDate())
                .idCabinet(dto.getIdCabinet())
                .build();
    }
}
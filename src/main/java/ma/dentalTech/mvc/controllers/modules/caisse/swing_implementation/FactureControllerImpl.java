package ma.dentalTech.mvc.controllers.modules.caisse.swing_implementation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.caisse.Facture;
import ma.dentalTech.mvc.controllers.modules.caisse.api.FactureController;
import ma.dentalTech.mvc.dto.caisse.FactureDTO;
import ma.dentalTech.mvc.ui.modules.caisse.FactureView;
import ma.dentalTech.service.modules.caisse.api.FactureService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FactureControllerImpl implements FactureController {

    private FactureService factureService;

    @Override
    public void afficherToutesLesFactures() {
        try {
            List<Facture> factures = factureService.listerToutesLesFactures();
            List<FactureDTO> factureDTOs = factures.stream()
                    .map(this::convertirEnDTO)
                    .collect(Collectors.toList());
            FactureView.afficherListeFactures(factureDTOs, "Toutes les factures");
        } catch (ServiceException e) {
            FactureView.afficherErreur("Erreur lors de la récupération des factures: " + e.getMessage());
        }
    }

    @Override
    public void afficherFactureParConsultation(Long idConsultation) {
        try {
            Facture facture = factureService.obtenirFactureParConsultation(idConsultation);
            if (facture != null) {
                FactureView.afficherDetailFacture(convertirEnDTO(facture));
            }
        } catch (ServiceException e) {
            FactureView.afficherErreur("Erreur lors de la récupération de la facture: " + e.getMessage());
        }
    }

    @Override
    public void afficherFacturesParStatut(String statut) {
        try {
            List<Facture> factures = factureService.listerFacturesParStatut(statut);
            List<FactureDTO> factureDTOs = factures.stream()
                    .map(this::convertirEnDTO)
                    .collect(Collectors.toList());
            FactureView.afficherListeFactures(factureDTOs, "Factures - Statut: " + statut);
        } catch (ServiceException e) {
            FactureView.afficherErreur("Erreur lors de la récupération des factures: " + e.getMessage());
        }
    }

    @Override
    public void afficherFacturesParPeriode(LocalDateTime debut, LocalDateTime fin) {
        try {
            List<Facture> factures = factureService.listerFacturesParPeriode(debut, fin);
            List<FactureDTO> factureDTOs = factures.stream()
                    .map(this::convertirEnDTO)
                    .collect(Collectors.toList());
            FactureView.afficherListeFactures(factureDTOs, "Factures de la période");
        } catch (ServiceException e) {
            FactureView.afficherErreur("Erreur lors de la récupération des factures: " + e.getMessage());
        }
    }

    @Override
    public void afficherFacturesImpayees() {
        try {
            List<Facture> factures = factureService.listerFacturesImpayees();
            List<FactureDTO> factureDTOs = factures.stream()
                    .map(this::convertirEnDTO)
                    .collect(Collectors.toList());
            FactureView.afficherListeFactures(factureDTOs, "Factures impayées");
        } catch (ServiceException e) {
            FactureView.afficherErreur("Erreur lors de la récupération des factures: " + e.getMessage());
        }
    }

    @Override
    public void afficherFacturesParPatient(Long idPatient) {
        try {
            List<Facture> factures = factureService.listerFacturesParPatient(idPatient);
            List<FactureDTO> factureDTOs = factures.stream()
                    .map(this::convertirEnDTO)
                    .collect(Collectors.toList());
            FactureView.afficherListeFactures(factureDTOs, "Factures du patient #" + idPatient);
        } catch (ServiceException e) {
            FactureView.afficherErreur("Erreur lors de la récupération des factures: " + e.getMessage());
        }
    }

    @Override
    public void enregistrerPaiement(Long idFacture, Double montant) {
        try {
            Facture facture = factureService.enregistrerPaiement(idFacture, montant);
            FactureView.afficherSucces("Paiement enregistré avec succès. Reste à payer: " + facture.getReste() + " MAD");
        } catch (ServiceException | ValidationException e) {
            FactureView.afficherErreur("Erreur lors de l'enregistrement du paiement: " + e.getMessage());
        }
    }

    @Override
    public void genererFactureDepuisConsultation(Long idConsultation) {
        try {
            Facture facture = factureService.genererFactureDepuisConsultation(idConsultation);
            FactureView.afficherSucces("Facture générée avec succès (ID: " + facture.getIdFacture() + ")");
            FactureView.afficherDetailFacture(convertirEnDTO(facture));
        } catch (ServiceException | ValidationException e) {
            FactureView.afficherErreur("Erreur lors de la génération de la facture: " + e.getMessage());
        }
    }

    @Override
    public void annulerFacture(Long idFacture) {
        try {
            factureService.annulerFacture(idFacture);
            FactureView.afficherSucces("Facture annulée avec succès");
        } catch (ServiceException | ValidationException e) {
            FactureView.afficherErreur("Erreur lors de l'annulation: " + e.getMessage());
        }
    }

    @Override
    public void afficherTotalFactures(LocalDateTime debut, LocalDateTime fin) {
        try {
            Double total = factureService.calculerTotalFactures(debut, fin);
            FactureView.afficherMontantTotal(total, "Total des factures de la période");
        } catch (ServiceException e) {
            FactureView.afficherErreur("Erreur lors du calcul: " + e.getMessage());
        }
    }

    @Override
    public void afficherTotalPaye(LocalDateTime debut, LocalDateTime fin) {
        try {
            Double total = factureService.calculerTotalPaye(debut, fin);
            FactureView.afficherMontantTotal(total, "Total payé de la période");
        } catch (ServiceException e) {
            FactureView.afficherErreur("Erreur lors du calcul: " + e.getMessage());
        }
    }

    @Override
    public void afficherTotalImpaye() {
        try {
            Double total = factureService.calculerTotalImpaye();
            FactureView.afficherMontantTotal(total, "Total impayé");
        } catch (ServiceException e) {
            FactureView.afficherErreur("Erreur lors du calcul: " + e.getMessage());
        }
    }

    @Override
    public void creerFacture(FactureDTO factureDTO) {
        try {
            Facture facture = convertirEnEntite(factureDTO);
            Facture nouvelleFacture = factureService.creerFacture(facture);
            FactureView.afficherSucces("Facture créée avec succès (ID: " + nouvelleFacture.getIdFacture() + ")");
        } catch (ServiceException | ValidationException e) {
            FactureView.afficherErreur("Erreur lors de la création: " + e.getMessage());
        }
    }

    @Override
    public void modifierFacture(FactureDTO factureDTO) {
        try {
            Facture facture = convertirEnEntite(factureDTO);
            factureService.modifierFacture(facture);
            FactureView.afficherSucces("Facture modifiée avec succès");
        } catch (ServiceException | ValidationException e) {
            FactureView.afficherErreur("Erreur lors de la modification: " + e.getMessage());
        }
    }

    @Override
    public void supprimerFacture(Long idFacture) {
        try {
            factureService.supprimerFacture(idFacture);
            FactureView.afficherSucces("Facture supprimée avec succès");
        } catch (ServiceException e) {
            FactureView.afficherErreur("Erreur lors de la suppression: " + e.getMessage());
        }
    }

    private FactureDTO convertirEnDTO(Facture facture) {
        return FactureDTO.builder()
                .idFacture(facture.getIdFacture())
                .totaleFacture(facture.getTotaleFacture())
                .totalePaye(facture.getTotalePaye())
                .reste(facture.getReste())
                .statut(facture.getStatut())
                .dateFacture(facture.getDateFacture())
                .idConsultation(facture.getIdConsultation())
                .build();
    }

    private Facture convertirEnEntite(FactureDTO dto) {
        return Facture.builder()
                .idFacture(dto.getIdFacture())
                .totaleFacture(dto.getTotaleFacture())
                .totalePaye(dto.getTotalePaye())
                .reste(dto.getReste())
                .statut(dto.getStatut())
                .dateFacture(dto.getDateFacture())
                .idConsultation(dto.getIdConsultation())
                .build();
    }
}
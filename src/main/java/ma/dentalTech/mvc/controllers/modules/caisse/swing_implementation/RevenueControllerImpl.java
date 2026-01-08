package ma.dentalTech.mvc.controllers.modules.caisse.swing_implementation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.caisse.Revenue;
import ma.dentalTech.mvc.controllers.modules.caisse.api.RevenueController;
import ma.dentalTech.mvc.dto.caisse.RevenueDTO;
import ma.dentalTech.mvc.ui.modules.caisse.RevenueView;
import ma.dentalTech.service.modules.caisse.api.RevenueService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevenueControllerImpl implements RevenueController {

    private RevenueService revenueService;

    @Override
    public void afficherTousLesRevenues() {
        try {
            List<Revenue> revenues = revenueService.listerTousLesRevenues();
            List<RevenueDTO> revenueDTOs = revenues.stream()
                    .map(this::convertirEnDTO)
                    .collect(Collectors.toList());
            RevenueView.afficherListeRevenues(revenueDTOs, "Tous les revenus");
        } catch (ServiceException e) {
            RevenueView.afficherErreur("Erreur lors de la récupération des revenus: " + e.getMessage());
        }
    }

    @Override
    public void afficherRevenuesParCabinet(Long idCabinet) {
        try {
            List<Revenue> revenues = revenueService.listerRevenuesParCabinet(idCabinet);
            List<RevenueDTO> revenueDTOs = revenues.stream()
                    .map(this::convertirEnDTO)
                    .collect(Collectors.toList());
            RevenueView.afficherListeRevenues(revenueDTOs, "Revenus du cabinet #" + idCabinet);
        } catch (ServiceException e) {
            RevenueView.afficherErreur("Erreur lors de la récupération des revenus: " + e.getMessage());
        }
    }

    @Override
    public void afficherRevenuesParPeriode(LocalDateTime debut, LocalDateTime fin) {
        try {
            List<Revenue> revenues = revenueService.listerRevenuesParPeriode(debut, fin);
            List<RevenueDTO> revenueDTOs = revenues.stream()
                    .map(this::convertirEnDTO)
                    .collect(Collectors.toList());
            RevenueView.afficherListeRevenues(revenueDTOs, "Revenus de la période");
        } catch (ServiceException e) {
            RevenueView.afficherErreur("Erreur lors de la récupération des revenus: " + e.getMessage());
        }
    }

    @Override
    public void rechercherRevenuesParTitre(String titre) {
        try {
            List<Revenue> revenues = revenueService.rechercherRevenuesParTitre(titre);
            List<RevenueDTO> revenueDTOs = revenues.stream()
                    .map(this::convertirEnDTO)
                    .collect(Collectors.toList());
            RevenueView.afficherListeRevenues(revenueDTOs, "Résultats de recherche: " + titre);
        } catch (ServiceException e) {
            RevenueView.afficherErreur("Erreur lors de la recherche: " + e.getMessage());
        }
    }

    @Override
    public void afficherTotalRevenues(Long idCabinet) {
        try {
            Double total = revenueService.calculerTotalRevenues(idCabinet);
            RevenueView.afficherMontantTotal(total, "Total des revenus du cabinet");
        } catch (ServiceException e) {
            RevenueView.afficherErreur("Erreur lors du calcul: " + e.getMessage());
        }
    }

    @Override
    public void afficherTotalRevenuesParPeriode(Long idCabinet, LocalDateTime debut, LocalDateTime fin) {
        try {
            Double total = revenueService.calculerTotalRevenuesParPeriode(idCabinet, debut, fin);
            RevenueView.afficherMontantTotal(total, "Total des revenus de la période");
        } catch (ServiceException e) {
            RevenueView.afficherErreur("Erreur lors du calcul: " + e.getMessage());
        }
    }

    @Override
    public void creerRevenue(RevenueDTO revenueDTO) {
        try {
            Revenue revenue = convertirEnEntite(revenueDTO);
            Revenue nouveauRevenue = revenueService.creerRevenue(revenue);
            RevenueView.afficherSucces("Revenu créé avec succès (ID: " + nouveauRevenue.getIdRevenue() + ")");
        } catch (ServiceException | ValidationException e) {
            RevenueView.afficherErreur("Erreur lors de la création: " + e.getMessage());
        }
    }

    @Override
    public void modifierRevenue(RevenueDTO revenueDTO) {
        try {
            Revenue revenue = convertirEnEntite(revenueDTO);
            revenueService.modifierRevenue(revenue);
            RevenueView.afficherSucces("Revenu modifié avec succès");
        } catch (ServiceException | ValidationException e) {
            RevenueView.afficherErreur("Erreur lors de la modification: " + e.getMessage());
        }
    }

    @Override
    public void supprimerRevenue(Long idRevenue) {
        try {
            revenueService.supprimerRevenue(idRevenue);
            RevenueView.afficherSucces("Revenu supprimé avec succès");
        } catch (ServiceException e) {
            RevenueView.afficherErreur("Erreur lors de la suppression: " + e.getMessage());
        }
    }

    private RevenueDTO convertirEnDTO(Revenue revenue) {
        return RevenueDTO.builder()
                .idRevenue(revenue.getIdRevenue())
                .titre(revenue.getTitre())
                .description(revenue.getDescription())
                .montant(revenue.getMontant())
                .date(revenue.getDate())
                .idCabinet(revenue.getIdCabinet())
                .build();
    }

    private Revenue convertirEnEntite(RevenueDTO dto) {
        return Revenue.builder()
                .idRevenue(dto.getIdRevenue())
                .titre(dto.getTitre())
                .description(dto.getDescription())
                .montant(dto.getMontant())
                .date(dto.getDate())
                .idCabinet(dto.getIdCabinet())
                .build();
    }
}
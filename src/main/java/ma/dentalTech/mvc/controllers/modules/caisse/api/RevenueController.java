package ma.dentalTech.mvc.controllers.modules.caisse.api;

import ma.dentalTech.mvc.dto.caisse.RevenueDTO;
import java.time.LocalDateTime;

public interface RevenueController {
    void afficherTousLesRevenues();
    void afficherRevenuesParCabinet(Long idCabinet);
    void afficherRevenuesParPeriode(LocalDateTime debut, LocalDateTime fin);
    void rechercherRevenuesParTitre(String titre);
    void afficherTotalRevenues(Long idCabinet);
    void afficherTotalRevenuesParPeriode(Long idCabinet, LocalDateTime debut, LocalDateTime fin);
    void creerRevenue(RevenueDTO revenueDTO);
    void modifierRevenue(RevenueDTO revenueDTO);
    void supprimerRevenue(Long idRevenue);
}
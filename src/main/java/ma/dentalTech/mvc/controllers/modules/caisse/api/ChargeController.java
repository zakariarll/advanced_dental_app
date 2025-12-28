package ma.dentalTech.mvc.controllers.modules.caisse.api;

import ma.dentalTech.mvc.dto.caisse.ChargeDTO;
import java.time.LocalDateTime;

public interface ChargeController {
    void afficherToutesLesCharges();
    void afficherChargesParCabinet(Long idCabinet);
    void afficherChargesParPeriode(LocalDateTime debut, LocalDateTime fin);
    void rechercherChargesParTitre(String titre);
    void afficherTotalCharges(Long idCabinet);
    void afficherTotalChargesParPeriode(Long idCabinet, LocalDateTime debut, LocalDateTime fin);
    void creerCharge(ChargeDTO chargeDTO);
    void modifierCharge(ChargeDTO chargeDTO);
    void supprimerCharge(Long idCharge);
}
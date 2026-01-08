package ma.dentalTech.mvc.controllers.modules.caisse.api;

import ma.dentalTech.mvc.dto.caisse.FactureDTO;
import java.time.LocalDateTime;

public interface FactureController {
    void afficherToutesLesFactures();
    void afficherFactureParConsultation(Long idConsultation);
    void afficherFacturesParStatut(String statut);
    void afficherFacturesParPeriode(LocalDateTime debut, LocalDateTime fin);
    void afficherFacturesImpayees();
    void afficherFacturesParPatient(Long idPatient);
    void enregistrerPaiement(Long idFacture, Double montant);
    void genererFactureDepuisConsultation(Long idConsultation);
    void annulerFacture(Long idFacture);
    void afficherTotalFactures(LocalDateTime debut, LocalDateTime fin);
    void afficherTotalPaye(LocalDateTime debut, LocalDateTime fin);
    void afficherTotalImpaye();
    void creerFacture(FactureDTO factureDTO);
    void modifierFacture(FactureDTO factureDTO);
    void supprimerFacture(Long idFacture);
}
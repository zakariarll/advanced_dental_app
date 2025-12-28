package ma.dentalTech.mvc.controllers.modules.caisse.api;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface CaisseController {
    void afficherChiffreAffaires(Long idCabinet, LocalDateTime debut, LocalDateTime fin);
    void afficherBeneficeNet(Long idCabinet, LocalDateTime debut, LocalDateTime fin);
    void afficherStatistiquesJournalieres(Long idCabinet, LocalDate date);
    void afficherStatistiquesMensuelles(Long idCabinet, int mois, int annee);
    void afficherStatistiquesAnnuelles(Long idCabinet, int annee);
    void genererRapportFinancier(Long idCabinet, LocalDateTime debut, LocalDateTime fin);
    void afficherTableauDeBord(Long idCabinet);
    void afficherTauxRecouvrement(LocalDateTime debut, LocalDateTime fin);
    void afficherRepartitionRevenusParMois(Long idCabinet, int annee);
    void afficherRepartitionChargesParMois(Long idCabinet, int annee);
}
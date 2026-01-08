package ma.dentalTech.mvc.controllers.modules.caisse.api;

import ma.dentalTech.mvc.dto.caisse.SituationFinanciereDTO;

public interface SituationFinanciereController {
    void afficherToutesLesSituations();
    void afficherSituationParFacture(Long idFacture);
    void afficherSituationsParStatut(String statut);
    void afficherSituationsParPatient(Long idPatient);
    void afficherSituationsAvecCredit();
    void reinitialiserSituation(Long idSF);
    void appliquerPromotion(Long idSF, String promo, Double pourcentage);
    void afficherTotalCredits();
    void afficherCreditsPatient(Long idPatient);
    void mettreAJourDepuisFacture(Long idFacture);
    void creerSituationFinanciere(SituationFinanciereDTO situationDTO);
    void modifierSituationFinanciere(SituationFinanciereDTO situationDTO);
    void supprimerSituationFinanciere(Long idSF);
}
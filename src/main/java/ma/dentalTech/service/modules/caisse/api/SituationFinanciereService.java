package ma.dentalTech.service.modules.caisse.api;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.caisse.SituationFinanciere;

import java.util.List;

public interface SituationFinanciereService {

    SituationFinanciere creerSituationFinanciere(SituationFinanciere situation) throws ServiceException, ValidationException;

    SituationFinanciere modifierSituationFinanciere(SituationFinanciere situation) throws ServiceException, ValidationException;

    void supprimerSituationFinanciere(Long idSF) throws ServiceException;

    SituationFinanciere obtenirSituationFinanciere(Long idSF) throws ServiceException;

    List<SituationFinanciere> listerToutesLesSituations() throws ServiceException;

    SituationFinanciere obtenirSituationParFacture(Long idFacture) throws ServiceException;

    List<SituationFinanciere> listerSituationsParStatut(String statut) throws ServiceException;

    List<SituationFinanciere> listerSituationsParPatient(Long idPatient) throws ServiceException;

    List<SituationFinanciere> listerSituationsAvecCredit() throws ServiceException;

    SituationFinanciere reinitialiserSituation(Long idSF) throws ServiceException, ValidationException;

    SituationFinanciere appliquerPromotion(Long idSF, String promo, Double pourcentage) throws ServiceException, ValidationException;

    Double calculerTotalCredits() throws ServiceException;

    Double calculerCreditsPatient(Long idPatient) throws ServiceException;

    SituationFinanciere mettreAJourDepuisFacture(Long idFacture) throws ServiceException, ValidationException;
}
package ma.dentalTech.service.modules.caisse.api;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.caisse.Facture;

import java.time.LocalDateTime;
import java.util.List;

public interface FactureService {

    Facture creerFacture(Facture facture) throws ServiceException, ValidationException;

    Facture modifierFacture(Facture facture) throws ServiceException, ValidationException;

    void supprimerFacture(Long idFacture) throws ServiceException;

    Facture obtenirFacture(Long idFacture) throws ServiceException;

    List<Facture> listerToutesLesFactures() throws ServiceException;

    Facture obtenirFactureParConsultation(Long idConsultation) throws ServiceException;

    List<Facture> listerFacturesParStatut(String statut) throws ServiceException;

    List<Facture> listerFacturesParPeriode(LocalDateTime debut, LocalDateTime fin) throws ServiceException;

    List<Facture> listerFacturesImpayees() throws ServiceException;

    List<Facture> listerFacturesParPatient(Long idPatient) throws ServiceException;

    Facture enregistrerPaiement(Long idFacture, Double montant) throws ServiceException, ValidationException;

    Facture genererFactureDepuisConsultation(Long idConsultation) throws ServiceException, ValidationException;

    void annulerFacture(Long idFacture) throws ServiceException, ValidationException;

    Double calculerTotalFactures(LocalDateTime debut, LocalDateTime fin) throws ServiceException;

    Double calculerTotalPaye(LocalDateTime debut, LocalDateTime fin) throws ServiceException;

    Double calculerTotalImpaye() throws ServiceException;
}
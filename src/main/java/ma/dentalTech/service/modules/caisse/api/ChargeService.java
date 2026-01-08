package ma.dentalTech.service.modules.caisse.api;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.caisse.Charge;

import java.time.LocalDateTime;
import java.util.List;

public interface ChargeService {

    Charge creerCharge(Charge charge) throws ServiceException, ValidationException;

    Charge modifierCharge(Charge charge) throws ServiceException, ValidationException;

    void supprimerCharge(Long idCharge) throws ServiceException;

    Charge obtenirCharge(Long idCharge) throws ServiceException;

    List<Charge> listerToutesLesCharges() throws ServiceException;

    List<Charge> listerChargesParCabinet(Long idCabinet) throws ServiceException;

    List<Charge> listerChargesParPeriode(LocalDateTime debut, LocalDateTime fin) throws ServiceException;

    List<Charge> rechercherChargesParTitre(String titre) throws ServiceException;

    Double calculerTotalCharges(Long idCabinet) throws ServiceException;

    Double calculerTotalChargesParPeriode(Long idCabinet, LocalDateTime debut, LocalDateTime fin) throws ServiceException;
}
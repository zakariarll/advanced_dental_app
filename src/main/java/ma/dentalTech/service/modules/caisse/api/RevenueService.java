package ma.dentalTech.service.modules.caisse.api;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.caisse.Revenue;

import java.time.LocalDateTime;
import java.util.List;

public interface RevenueService {

    Revenue creerRevenue(Revenue revenue) throws ServiceException, ValidationException;

    Revenue modifierRevenue(Revenue revenue) throws ServiceException, ValidationException;

    void supprimerRevenue(Long idRevenue) throws ServiceException;

    Revenue obtenirRevenue(Long idRevenue) throws ServiceException;

    List<Revenue> listerTousLesRevenues() throws ServiceException;

    List<Revenue> listerRevenuesParCabinet(Long idCabinet) throws ServiceException;

    List<Revenue> listerRevenuesParPeriode(LocalDateTime debut, LocalDateTime fin) throws ServiceException;

    List<Revenue> rechercherRevenuesParTitre(String titre) throws ServiceException;

    Double calculerTotalRevenues(Long idCabinet) throws ServiceException;

    Double calculerTotalRevenuesParPeriode(Long idCabinet, LocalDateTime debut, LocalDateTime fin) throws ServiceException;
}
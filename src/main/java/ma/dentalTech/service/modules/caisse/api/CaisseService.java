package ma.dentalTech.service.modules.caisse.api;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.entities.caisse.Statistique;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface CaisseService {

    Double calculerChiffreAffaires(Long idCabinet, LocalDateTime debut, LocalDateTime fin) throws ServiceException;

    Double calculerBeneficeNet(Long idCabinet, LocalDateTime debut, LocalDateTime fin) throws ServiceException;

    Double calculerTotalEncaissements(LocalDateTime debut, LocalDateTime fin) throws ServiceException;

    Double calculerTotalDecaissements(Long idCabinet, LocalDateTime debut, LocalDateTime fin) throws ServiceException;

    Map<String, Double> obtenirStatistiquesJournalieres(Long idCabinet, LocalDate date) throws ServiceException;

    Map<String, Double> obtenirStatistiquesMensuelles(Long idCabinet, int mois, int annee) throws ServiceException;

    Map<String, Double> obtenirStatistiquesAnnuelles(Long idCabinet, int annee) throws ServiceException;

    List<Statistique> genererRapportFinancier(Long idCabinet, LocalDateTime debut, LocalDateTime fin) throws ServiceException;

    Map<String, Object> obtenirTableauDeBord(Long idCabinet) throws ServiceException;

    Double calculerTauxRecouvrement(LocalDateTime debut, LocalDateTime fin) throws ServiceException;

    Map<String, Double> obtenirRepartitionRevenusParMois(Long idCabinet, int annee) throws ServiceException;

    Map<String, Double> obtenirRepartitionChargesParMois(Long idCabinet, int annee) throws ServiceException;
}
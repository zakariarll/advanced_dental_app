package ma.dentalTech.service.modules.caisse.impl;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.caisse.Revenue;
import ma.dentalTech.repository.modules.caisse.api.RevenueRepository;
import ma.dentalTech.repository.modules.caisse.impl.mySQL.RevenueRepositoryImpl;
import ma.dentalTech.service.modules.caisse.api.RevenueService;

import java.time.LocalDateTime;
import java.util.List;

public class RevenueServiceImpl implements RevenueService {

    private final RevenueRepository revenueRepository;

    public RevenueServiceImpl() {
        this.revenueRepository = new RevenueRepositoryImpl();
    }

    public RevenueServiceImpl(RevenueRepository revenueRepository) {
        this.revenueRepository = revenueRepository;
    }

    @Override
    public Revenue creerRevenue(Revenue revenue) throws ServiceException, ValidationException {
        validerRevenue(revenue);

        if (revenue.getDate() == null) {
            revenue.setDate(LocalDateTime.now());
        }

        try {
            revenueRepository.create(revenue);
            return revenue;
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la création du revenu", e);
        }
    }

    @Override
    public Revenue modifierRevenue(Revenue revenue) throws ServiceException, ValidationException {
        if (revenue.getIdRevenue() == null) {
            throw new ValidationException("L'identifiant du revenu est obligatoire");
        }

        Revenue existant = obtenirRevenue(revenue.getIdRevenue());
        if (existant == null) {
            throw new ServiceException("Revenu introuvable avec l'ID : " + revenue.getIdRevenue());
        }

        validerRevenue(revenue);

        try {
            revenueRepository.update(revenue);
            return revenue;
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la modification du revenu", e);
        }
    }

    @Override
    public void supprimerRevenue(Long idRevenue) throws ServiceException {
        if (idRevenue == null) {
            throw new ServiceException("L'identifiant du revenu est obligatoire");
        }

        Revenue existant = obtenirRevenue(idRevenue);
        if (existant == null) {
            throw new ServiceException("Revenu introuvable avec l'ID : " + idRevenue);
        }

        try {
            revenueRepository.deleteById(idRevenue);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la suppression du revenu", e);
        }
    }

    @Override
    public Revenue obtenirRevenue(Long idRevenue) throws ServiceException {
        if (idRevenue == null) {
            throw new ServiceException("L'identifiant du revenu est obligatoire");
        }

        try {
            return revenueRepository.findById(idRevenue);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération du revenu", e);
        }
    }

    @Override
    public List<Revenue> listerTousLesRevenues() throws ServiceException {
        try {
            return revenueRepository.findAll();
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération des revenus", e);
        }
    }

    @Override
    public List<Revenue> listerRevenuesParCabinet(Long idCabinet) throws ServiceException {
        if (idCabinet == null) {
            throw new ServiceException("L'identifiant du cabinet est obligatoire");
        }

        try {
            return revenueRepository.findByCabinetId(idCabinet);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération des revenus du cabinet", e);
        }
    }

    @Override
    public List<Revenue> listerRevenuesParPeriode(LocalDateTime debut, LocalDateTime fin) throws ServiceException {
        if (debut == null || fin == null) {
            throw new ServiceException("Les dates de début et fin sont obligatoires");
        }

        if (debut.isAfter(fin)) {
            throw new ServiceException("La date de début doit être antérieure à la date de fin");
        }

        try {
            return revenueRepository.findByDateBetween(debut, fin);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération des revenus par période", e);
        }
    }

    @Override
    public List<Revenue> rechercherRevenuesParTitre(String titre) throws ServiceException {
        if (titre == null || titre.isBlank()) {
            throw new ServiceException("Le titre de recherche est obligatoire");
        }

        try {
            return revenueRepository.findByTitreContaining(titre);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la recherche des revenus", e);
        }
    }

    @Override
    public Double calculerTotalRevenues(Long idCabinet) throws ServiceException {
        if (idCabinet == null) {
            throw new ServiceException("L'identifiant du cabinet est obligatoire");
        }

        try {
            return revenueRepository.calculateTotalRevenues(idCabinet);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors du calcul du total des revenus", e);
        }
    }

    @Override
    public Double calculerTotalRevenuesParPeriode(Long idCabinet, LocalDateTime debut, LocalDateTime fin) throws ServiceException {
        if (idCabinet == null) {
            throw new ServiceException("L'identifiant du cabinet est obligatoire");
        }

        if (debut == null || fin == null) {
            throw new ServiceException("Les dates de début et fin sont obligatoires");
        }

        if (debut.isAfter(fin)) {
            throw new ServiceException("La date de début doit être antérieure à la date de fin");
        }

        try {
            return revenueRepository.calculateTotalRevenuesBetween(idCabinet, debut, fin);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors du calcul du total des revenus par période", e);
        }
    }

    private void validerRevenue(Revenue revenue) throws ValidationException {
        if (revenue == null) {
            throw new ValidationException("Le revenu ne peut pas être nul");
        }

        if (revenue.getTitre() == null || revenue.getTitre().isBlank()) {
            throw new ValidationException("Le titre est obligatoire");
        }

        if (revenue.getTitre().length() < 3) {
            throw new ValidationException("Le titre doit contenir au moins 3 caractères");
        }

        if (revenue.getMontant() == null) {
            throw new ValidationException("Le montant est obligatoire");
        }

        if (revenue.getMontant() <= 0) {
            throw new ValidationException("Le montant doit être supérieur à 0");
        }

        if (revenue.getIdCabinet() == null) {
            throw new ValidationException("Le cabinet est obligatoire");
        }
    }
}
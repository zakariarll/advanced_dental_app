package ma.dentalTech.service.modules.caisse.impl;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.caisse.Charge;
import ma.dentalTech.repository.modules.caisse.api.ChargeRepository;
import ma.dentalTech.repository.modules.caisse.impl.mySQL.ChargeRepositoryImpl;
import ma.dentalTech.service.modules.caisse.api.ChargeService;

import java.time.LocalDateTime;
import java.util.List;

public class ChargeServiceImpl implements ChargeService {

    private final ChargeRepository chargeRepository;

    public ChargeServiceImpl() {
        this.chargeRepository = new ChargeRepositoryImpl();
    }

    public ChargeServiceImpl(ChargeRepository chargeRepository) {
        this.chargeRepository = chargeRepository;
    }

    @Override
    public Charge creerCharge(Charge charge) throws ServiceException, ValidationException {
        validerCharge(charge);
        if (charge.getDate() == null) {
            charge.setDate(LocalDateTime.now());
        }
        try {
            chargeRepository.create(charge);
            return charge;
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la création de la charge", e);
        }
    }

    @Override
    public Charge modifierCharge(Charge charge) throws ServiceException, ValidationException {
        if (charge.getIdCharge() == null) {
            throw new ValidationException("L'identifiant de la charge est obligatoire");
        }
        Charge existante = obtenirCharge(charge.getIdCharge());
        if (existante == null) {
            throw new ServiceException("Charge introuvable avec l'ID : " + charge.getIdCharge());
        }
        validerCharge(charge);
        try {
            chargeRepository.update(charge);
            return charge;
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la modification de la charge", e);
        }
    }

    @Override
    public void supprimerCharge(Long idCharge) throws ServiceException {
        if (idCharge == null) {
            throw new ServiceException("L'identifiant de la charge est obligatoire");
        }
        Charge existante = obtenirCharge(idCharge);
        if (existante == null) {
            throw new ServiceException("Charge introuvable avec l'ID : " + idCharge);
        }
        try {
            chargeRepository.deleteById(idCharge);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la suppression de la charge", e);
        }
    }

    @Override
    public Charge obtenirCharge(Long idCharge) throws ServiceException {
        if (idCharge == null) {
            throw new ServiceException("L'identifiant de la charge est obligatoire");
        }
        try {
            return chargeRepository.findById(idCharge);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération de la charge", e);
        }
    }

    @Override
    public List<Charge> listerToutesLesCharges() throws ServiceException {
        try {
            return chargeRepository.findAll();
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération des charges", e);
        }
    }

    @Override
    public List<Charge> listerChargesParCabinet(Long idCabinet) throws ServiceException {
        if (idCabinet == null) {
            throw new ServiceException("L'identifiant du cabinet est obligatoire");
        }
        try {
            return chargeRepository.findByCabinetId(idCabinet);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération des charges du cabinet", e);
        }
    }

    @Override
    public List<Charge> listerChargesParPeriode(LocalDateTime debut, LocalDateTime fin) throws ServiceException {
        if (debut == null || fin == null) {
            throw new ServiceException("Les dates de début et fin sont obligatoires");
        }
        if (debut.isAfter(fin)) {
            throw new ServiceException("La date de début doit être antérieure à la date de fin");
        }
        try {
            return chargeRepository.findByDateBetween(debut, fin);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération des charges par période", e);
        }
    }

    @Override
    public List<Charge> rechercherChargesParTitre(String titre) throws ServiceException {
        if (titre == null || titre.isBlank()) {
            throw new ServiceException("Le titre de recherche est obligatoire");
        }
        try {
            return chargeRepository.findByTitreContaining(titre);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la recherche des charges", e);
        }
    }

    @Override
    public Double calculerTotalCharges(Long idCabinet) throws ServiceException {
        if (idCabinet == null) {
            throw new ServiceException("L'identifiant du cabinet est obligatoire");
        }
        try {
            return chargeRepository.calculateTotalCharges(idCabinet);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors du calcul du total des charges", e);
        }
    }

    @Override
    public Double calculerTotalChargesParPeriode(Long idCabinet, LocalDateTime debut, LocalDateTime fin) throws ServiceException {
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
            return chargeRepository.calculateTotalChargesBetween(idCabinet, debut, fin);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors du calcul du total des charges par période", e);
        }
    }

    private void validerCharge(Charge charge) throws ValidationException {
        if (charge == null) {
            throw new ValidationException("La charge ne peut pas être nulle");
        }
        if (charge.getTitre() == null || charge.getTitre().isBlank()) {
            throw new ValidationException("Le titre est obligatoire");
        }
        if (charge.getTitre().length() < 3) {
            throw new ValidationException("Le titre doit contenir au moins 3 caractères");
        }
        if (charge.getMontant() == null) {
            throw new ValidationException("Le montant est obligatoire");
        }
        if (charge.getMontant() <= 0) {
            throw new ValidationException("Le montant doit être supérieur à 0");
        }
        if (charge.getIdCabinet() == null) {
            throw new ValidationException("Le cabinet est obligatoire");
        }
    }
}
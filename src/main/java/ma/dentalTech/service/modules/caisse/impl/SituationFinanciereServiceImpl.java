package ma.dentalTech.service.modules.caisse.impl;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.caisse.Facture;
import ma.dentalTech.entities.caisse.SituationFinanciere;
import ma.dentalTech.repository.modules.caisse.api.FactureRepository;
import ma.dentalTech.repository.modules.caisse.api.SituationFinanciereRepository;
import ma.dentalTech.repository.modules.caisse.impl.mySQL.FactureRepositoryImpl;
import ma.dentalTech.repository.modules.caisse.impl.mySQL.SituationFinanciereRepositoryImpl;
import ma.dentalTech.service.modules.caisse.api.SituationFinanciereService;

import java.util.List;

public class SituationFinanciereServiceImpl implements SituationFinanciereService {

    private final SituationFinanciereRepository situationRepository;
    private final FactureRepository factureRepository;

    public SituationFinanciereServiceImpl() {
        this.situationRepository = new SituationFinanciereRepositoryImpl();
        this.factureRepository = new FactureRepositoryImpl();
    }

    public SituationFinanciereServiceImpl(SituationFinanciereRepository situationRepository, FactureRepository factureRepository) {
        this.situationRepository = situationRepository;
        this.factureRepository = factureRepository;
    }

    @Override
    public SituationFinanciere creerSituationFinanciere(SituationFinanciere situation) throws ServiceException, ValidationException {
        validerSituation(situation);

        if (situation.getStatut() == null || situation.getStatut().isBlank()) {
            situation.setStatut("EN_COURS");
        }

        calculerCredit(situation);

        try {
            situationRepository.create(situation);
            return situation;
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la création de la situation financière", e);
        }
    }

    @Override
    public SituationFinanciere modifierSituationFinanciere(SituationFinanciere situation) throws ServiceException, ValidationException {
        if (situation.getIdSF() == null) {
            throw new ValidationException("L'identifiant de la situation financière est obligatoire");
        }

        SituationFinanciere existante = obtenirSituationFinanciere(situation.getIdSF());
        if (existante == null) {
            throw new ServiceException("Situation financière introuvable avec l'ID : " + situation.getIdSF());
        }

        validerSituation(situation);
        calculerCredit(situation);

        try {
            situationRepository.update(situation);
            return situation;
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la modification de la situation financière", e);
        }
    }

    @Override
    public void supprimerSituationFinanciere(Long idSF) throws ServiceException {
        if (idSF == null) {
            throw new ServiceException("L'identifiant de la situation financière est obligatoire");
        }

        SituationFinanciere existante = obtenirSituationFinanciere(idSF);
        if (existante == null) {
            throw new ServiceException("Situation financière introuvable avec l'ID : " + idSF);
        }

        try {
            situationRepository.deleteById(idSF);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la suppression de la situation financière", e);
        }
    }

    @Override
    public SituationFinanciere obtenirSituationFinanciere(Long idSF) throws ServiceException {
        if (idSF == null) {
            throw new ServiceException("L'identifiant de la situation financière est obligatoire");
        }

        try {
            return situationRepository.findById(idSF);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération de la situation financière", e);
        }
    }

    @Override
    public List<SituationFinanciere> listerToutesLesSituations() throws ServiceException {
        try {
            return situationRepository.findAll();
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération des situations financières", e);
        }
    }

    @Override
    public SituationFinanciere obtenirSituationParFacture(Long idFacture) throws ServiceException {
        if (idFacture == null) {
            throw new ServiceException("L'identifiant de la facture est obligatoire");
        }

        try {
            return situationRepository.findByFactureId(idFacture).orElse(null);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération de la situation par facture", e);
        }
    }

    @Override
    public List<SituationFinanciere> listerSituationsParStatut(String statut) throws ServiceException {
        if (statut == null || statut.isBlank()) {
            throw new ServiceException("Le statut est obligatoire");
        }

        try {
            return situationRepository.findByStatut(statut);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération des situations par statut", e);
        }
    }

    @Override
    public List<SituationFinanciere> listerSituationsParPatient(Long idPatient) throws ServiceException {
        if (idPatient == null) {
            throw new ServiceException("L'identifiant du patient est obligatoire");
        }

        try {
            return situationRepository.findByPatientId(idPatient);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération des situations du patient", e);
        }
    }

    @Override
    public List<SituationFinanciere> listerSituationsAvecCredit() throws ServiceException {
        try {
            return situationRepository.findWithCredit();
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération des situations avec crédit", e);
        }
    }

    @Override
    public SituationFinanciere reinitialiserSituation(Long idSF) throws ServiceException, ValidationException {
        if (idSF == null) {
            throw new ServiceException("L'identifiant de la situation financière est obligatoire");
        }

        SituationFinanciere situation = obtenirSituationFinanciere(idSF);
        if (situation == null) {
            throw new ServiceException("Situation financière introuvable avec l'ID : " + idSF);
        }

        situation.setTotalePaye(0.0);
        situation.setCredit(situation.getTotaleDesActes());
        situation.setStatut("EN_COURS");
        situation.setEnPromo(null);

        return modifierSituationFinanciere(situation);
    }

    @Override
    public SituationFinanciere appliquerPromotion(Long idSF, String promo, Double pourcentage) throws ServiceException, ValidationException {
        if (idSF == null) {
            throw new ServiceException("L'identifiant de la situation financière est obligatoire");
        }

        if (promo == null || promo.isBlank()) {
            throw new ValidationException("Le nom de la promotion est obligatoire");
        }

        if (pourcentage == null || pourcentage <= 0 || pourcentage > 100) {
            throw new ValidationException("Le pourcentage doit être compris entre 0 et 100");
        }

        SituationFinanciere situation = obtenirSituationFinanciere(idSF);
        if (situation == null) {
            throw new ServiceException("Situation financière introuvable avec l'ID : " + idSF);
        }

        if (situation.getEnPromo() != null && !situation.getEnPromo().isBlank()) {
            throw new ServiceException("Une promotion est déjà appliquée à cette situation");
        }

        Double reduction = situation.getTotaleDesActes() * (pourcentage / 100);
        Double nouveauTotal = situation.getTotaleDesActes() - reduction;

        situation.setTotaleDesActes(nouveauTotal);
        situation.setEnPromo(promo + " (-" + pourcentage + "%)");
        calculerCredit(situation);

        return modifierSituationFinanciere(situation);
    }

    @Override
    public Double calculerTotalCredits() throws ServiceException {
        try {
            return situationRepository.calculateTotalCredit();
        } catch (Exception e) {
            throw new ServiceException("Erreur lors du calcul du total des crédits", e);
        }
    }

    @Override
    public Double calculerCreditsPatient(Long idPatient) throws ServiceException {
        if (idPatient == null) {
            throw new ServiceException("L'identifiant du patient est obligatoire");
        }

        try {
            return situationRepository.calculateTotalCreditsPatient(idPatient);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors du calcul des crédits du patient", e);
        }
    }

    @Override
    public SituationFinanciere mettreAJourDepuisFacture(Long idFacture) throws ServiceException, ValidationException {
        if (idFacture == null) {
            throw new ServiceException("L'identifiant de la facture est obligatoire");
        }

        Facture facture = factureRepository.findById(idFacture);
        if (facture == null) {
            throw new ServiceException("Facture introuvable avec l'ID : " + idFacture);
        }

        SituationFinanciere situation = obtenirSituationParFacture(idFacture);

        if (situation == null) {
            situation = SituationFinanciere.builder()
                    .idFacture(idFacture)
                    .totaleDesActes(facture.getTotaleFacture())
                    .totalePaye(facture.getTotalePaye())
                    .credit(facture.getReste())
                    .statut(determinerStatut(facture))
                    .build();

            return creerSituationFinanciere(situation);
        } else {
            situation.setTotaleDesActes(facture.getTotaleFacture());
            situation.setTotalePaye(facture.getTotalePaye());
            situation.setCredit(facture.getReste());
            situation.setStatut(determinerStatut(facture));

            return modifierSituationFinanciere(situation);
        }
    }

    private void validerSituation(SituationFinanciere situation) throws ValidationException {
        if (situation == null) {
            throw new ValidationException("La situation financière ne peut pas être nulle");
        }

        if (situation.getIdFacture() == null) {
            throw new ValidationException("La facture associée est obligatoire");
        }

        if (situation.getTotaleDesActes() == null) {
            throw new ValidationException("Le total des actes est obligatoire");
        }

        if (situation.getTotaleDesActes() < 0) {
            throw new ValidationException("Le total des actes ne peut pas être négatif");
        }

        if (situation.getTotalePaye() == null) {
            situation.setTotalePaye(0.0);
        }

        if (situation.getTotalePaye() < 0) {
            throw new ValidationException("Le total payé ne peut pas être négatif");
        }

        if (situation.getStatut() != null && !situation.getStatut().isBlank()) {
            String statut = situation.getStatut().toUpperCase();
            if (!List.of("EN_COURS", "SOLDE", "EN_CREDIT").contains(statut)) {
                throw new ValidationException("Statut invalide. Valeurs acceptées : EN_COURS, SOLDE, EN_CREDIT");
            }
        }
    }

    private void calculerCredit(SituationFinanciere situation) {
        if (situation.getTotaleDesActes() != null && situation.getTotalePaye() != null) {
            situation.setCredit(situation.getTotaleDesActes() - situation.getTotalePaye());
        }
    }

    private String determinerStatut(Facture facture) {
        if ("PAYEE".equals(facture.getStatut())) {
            return "SOLDE";
        } else if (facture.getReste() > 0) {
            return "EN_CREDIT";
        }
        return "EN_COURS";
    }
}
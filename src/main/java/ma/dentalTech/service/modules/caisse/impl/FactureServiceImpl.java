package ma.dentalTech.service.modules.caisse.impl;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.caisse.Facture;
import ma.dentalTech.entities.dossierMedical.InterventionMedecin;
import ma.dentalTech.repository.modules.caisse.api.FactureRepository;
import ma.dentalTech.repository.modules.caisse.impl.mySQL.FactureRepositoryImpl;
import ma.dentalTech.repository.modules.dossierMedical.api.InterventionMedecinRepository;
import ma.dentalTech.repository.modules.dossierMedical.impl.mySQL.InterventionMedecinRepositoryImpl;
import ma.dentalTech.service.modules.caisse.api.FactureService;

import java.time.LocalDateTime;
import java.util.List;

public class FactureServiceImpl implements FactureService {

    private final FactureRepository factureRepository;
    private final InterventionMedecinRepository interventionRepository;

    public FactureServiceImpl() {
        this.factureRepository = new FactureRepositoryImpl();
        this.interventionRepository = new InterventionMedecinRepositoryImpl();
    }

    public FactureServiceImpl(FactureRepository factureRepository, InterventionMedecinRepository interventionRepository) {
        this.factureRepository = factureRepository;
        this.interventionRepository = interventionRepository;
    }

    @Override
    public Facture creerFacture(Facture facture) throws ServiceException, ValidationException {
        validerFacture(facture);

        if (facture.getDateFacture() == null) {
            facture.setDateFacture(LocalDateTime.now());
        }

        if (facture.getStatut() == null || facture.getStatut().isBlank()) {
            facture.setStatut("EN_ATTENTE");
        }

        calculerReste(facture);

        try {
            factureRepository.create(facture);
            return facture;
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la création de la facture", e);
        }
    }

    @Override
    public Facture modifierFacture(Facture facture) throws ServiceException, ValidationException {
        if (facture.getIdFacture() == null) {
            throw new ValidationException("L'identifiant de la facture est obligatoire");
        }

        Facture existante = obtenirFacture(facture.getIdFacture());
        if (existante == null) {
            throw new ServiceException("Facture introuvable avec l'ID : " + facture.getIdFacture());
        }

        if ("ANNULEE".equals(existante.getStatut())) {
            throw new ServiceException("Impossible de modifier une facture annulée");
        }

        validerFacture(facture);
        calculerReste(facture);

        try {
            factureRepository.update(facture);
            return facture;
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la modification de la facture", e);
        }
    }

    @Override
    public void supprimerFacture(Long idFacture) throws ServiceException {
        if (idFacture == null) {
            throw new ServiceException("L'identifiant de la facture est obligatoire");
        }

        Facture existante = obtenirFacture(idFacture);
        if (existante == null) {
            throw new ServiceException("Facture introuvable avec l'ID : " + idFacture);
        }

        if ("PAYEE".equals(existante.getStatut())) {
            throw new ServiceException("Impossible de supprimer une facture payée");
        }

        try {
            factureRepository.deleteById(idFacture);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la suppression de la facture", e);
        }
    }

    @Override
    public Facture obtenirFacture(Long idFacture) throws ServiceException {
        if (idFacture == null) {
            throw new ServiceException("L'identifiant de la facture est obligatoire");
        }

        try {
            return factureRepository.findById(idFacture);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération de la facture", e);
        }
    }

    @Override
    public List<Facture> listerToutesLesFactures() throws ServiceException {
        try {
            return factureRepository.findAll();
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération des factures", e);
        }
    }

    @Override
    public Facture obtenirFactureParConsultation(Long idConsultation) throws ServiceException {
        if (idConsultation == null) {
            throw new ServiceException("L'identifiant de la consultation est obligatoire");
        }

        try {
            return factureRepository.findByConsultationId(idConsultation).orElse(null);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération de la facture par consultation", e);
        }
    }

    @Override
    public List<Facture> listerFacturesParStatut(String statut) throws ServiceException {
        if (statut == null || statut.isBlank()) {
            throw new ServiceException("Le statut est obligatoire");
        }

        try {
            return factureRepository.findByStatut(statut);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération des factures par statut", e);
        }
    }

    @Override
    public List<Facture> listerFacturesParPeriode(LocalDateTime debut, LocalDateTime fin) throws ServiceException {
        if (debut == null || fin == null) {
            throw new ServiceException("Les dates de début et fin sont obligatoires");
        }

        if (debut.isAfter(fin)) {
            throw new ServiceException("La date de début doit être antérieure à la date de fin");
        }

        try {
            return factureRepository.findByDateBetween(debut, fin);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération des factures par période", e);
        }
    }

    @Override
    public List<Facture> listerFacturesImpayees() throws ServiceException {
        try {
            return factureRepository.findFacturesImpayees();
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération des factures impayées", e);
        }
    }

    @Override
    public List<Facture> listerFacturesParPatient(Long idPatient) throws ServiceException {
        if (idPatient == null) {
            throw new ServiceException("L'identifiant du patient est obligatoire");
        }

        try {
            return factureRepository.findByPatientId(idPatient);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération des factures du patient", e);
        }
    }

    @Override
    public Facture enregistrerPaiement(Long idFacture, Double montant) throws ServiceException, ValidationException {
        if (idFacture == null) {
            throw new ServiceException("L'identifiant de la facture est obligatoire");
        }

        if (montant == null || montant <= 0) {
            throw new ValidationException("Le montant doit être supérieur à 0");
        }

        Facture facture = obtenirFacture(idFacture);
        if (facture == null) {
            throw new ServiceException("Facture introuvable avec l'ID : " + idFacture);
        }

        if ("ANNULEE".equals(facture.getStatut())) {
            throw new ServiceException("Impossible d'enregistrer un paiement sur une facture annulée");
        }

        if ("PAYEE".equals(facture.getStatut())) {
            throw new ServiceException("La facture est déjà entièrement payée");
        }

        if (montant > facture.getReste()) {
            throw new ValidationException("Le montant dépasse le reste à payer (" + facture.getReste() + ")");
        }

        Double nouveauTotalPaye = facture.getTotalePaye() + montant;
        facture.setTotalePaye(nouveauTotalPaye);
        calculerReste(facture);

        if (facture.getReste() == 0) {
            facture.setStatut("PAYEE");
        } else {
            facture.setStatut("PARTIELLEMENT_PAYEE");
        }

        return modifierFacture(facture);
    }

    @Override
    public Facture genererFactureDepuisConsultation(Long idConsultation) throws ServiceException, ValidationException {
        if (idConsultation == null) {
            throw new ServiceException("L'identifiant de la consultation est obligatoire");
        }

        Facture existante = obtenirFactureParConsultation(idConsultation);
        if (existante != null) {
            throw new ServiceException("Une facture existe déjà pour cette consultation");
        }

        try {
            List<InterventionMedecin> interventions = interventionRepository.findByConsultationId(idConsultation);

            Double totalActes = interventions.stream()
                    .map(InterventionMedecin::getPrixDePatient)
                    .filter(prix -> prix != null)
                    .reduce(0.0, Double::sum);

            Facture facture = Facture.builder()
                    .idConsultation(idConsultation)
                    .totaleFacture(totalActes)
                    .totalePaye(0.0)
                    .reste(totalActes)
                    .statut("EN_ATTENTE")
                    .dateFacture(LocalDateTime.now())
                    .build();

            return creerFacture(facture);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la génération de la facture", e);
        }
    }

    @Override
    public void annulerFacture(Long idFacture) throws ServiceException, ValidationException {
        if (idFacture == null) {
            throw new ServiceException("L'identifiant de la facture est obligatoire");
        }

        Facture facture = obtenirFacture(idFacture);
        if (facture == null) {
            throw new ServiceException("Facture introuvable avec l'ID : " + idFacture);
        }

        if ("ANNULEE".equals(facture.getStatut())) {
            throw new ServiceException("La facture est déjà annulée");
        }

        if ("PAYEE".equals(facture.getStatut())) {
            throw new ServiceException("Impossible d'annuler une facture entièrement payée");
        }

        facture.setStatut("ANNULEE");

        try {
            factureRepository.update(facture);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de l'annulation de la facture", e);
        }
    }

    @Override
    public Double calculerTotalFactures(LocalDateTime debut, LocalDateTime fin) throws ServiceException {
        if (debut == null || fin == null) {
            throw new ServiceException("Les dates de début et fin sont obligatoires");
        }

        try {
            return factureRepository.calculateTotalFactures(debut, fin);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors du calcul du total des factures", e);
        }
    }

    @Override
    public Double calculerTotalPaye(LocalDateTime debut, LocalDateTime fin) throws ServiceException {
        if (debut == null || fin == null) {
            throw new ServiceException("Les dates de début et fin sont obligatoires");
        }

        try {
            return factureRepository.calculateTotalPaye(debut, fin);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors du calcul du total payé", e);
        }
    }

    @Override
    public Double calculerTotalImpaye() throws ServiceException {
        try {
            return factureRepository.calculateTotalImpaye();
        } catch (Exception e) {
            throw new ServiceException("Erreur lors du calcul du total impayé", e);
        }
    }

    private void validerFacture(Facture facture) throws ValidationException {
        if (facture == null) {
            throw new ValidationException("La facture ne peut pas être nulle");
        }

        if (facture.getIdConsultation() == null) {
            throw new ValidationException("La consultation associée est obligatoire");
        }

        if (facture.getTotaleFacture() == null) {
            throw new ValidationException("Le total de la facture est obligatoire");
        }

        if (facture.getTotaleFacture() < 0) {
            throw new ValidationException("Le total de la facture ne peut pas être négatif");
        }

        if (facture.getTotalePaye() == null) {
            facture.setTotalePaye(0.0);
        }

        if (facture.getTotalePaye() < 0) {
            throw new ValidationException("Le total payé ne peut pas être négatif");
        }

        if (facture.getTotalePaye() > facture.getTotaleFacture()) {
            throw new ValidationException("Le total payé ne peut pas dépasser le total de la facture");
        }

        if (facture.getStatut() != null && !facture.getStatut().isBlank()) {
            String statut = facture.getStatut().toUpperCase();
            if (!List.of("EN_ATTENTE", "PARTIELLEMENT_PAYEE", "PAYEE", "ANNULEE").contains(statut)) {
                throw new ValidationException("Statut invalide. Valeurs acceptées : EN_ATTENTE, PARTIELLEMENT_PAYEE, PAYEE, ANNULEE");
            }
        }
    }

    private void calculerReste(Facture facture) {
        if (facture.getTotaleFacture() != null && facture.getTotalePaye() != null) {
            facture.setReste(facture.getTotaleFacture() - facture.getTotalePaye());
        }
    }
}
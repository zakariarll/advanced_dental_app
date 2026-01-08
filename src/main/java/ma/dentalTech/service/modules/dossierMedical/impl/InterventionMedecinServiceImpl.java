package ma.dentalTech.service.modules.dossierMedical.impl;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.dossierMedical.InterventionMedecin;
import ma.dentalTech.repository.modules.dossierMedical.api.InterventionMedecinRepository;
import ma.dentalTech.service.modules.dossierMedical.api.InterventionMedecinService;

import java.util.List;

public class InterventionMedecinServiceImpl implements InterventionMedecinService {

    private final InterventionMedecinRepository interventionRepository;

    public InterventionMedecinServiceImpl(InterventionMedecinRepository interventionRepository) {
        this.interventionRepository = interventionRepository;
    }

    @Override
    public InterventionMedecin creerIntervention(InterventionMedecin intervention) throws ServiceException, ValidationException {
        validerIntervention(intervention);

        try {
            interventionRepository.create(intervention);
            return intervention;
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la création de l'intervention", e);
        }
    }

    @Override
    public InterventionMedecin modifierIntervention(InterventionMedecin intervention) throws ServiceException, ValidationException {
        if (intervention.getIdIM() == null) {
            throw new ValidationException("L'identifiant de l'intervention est obligatoire");
        }

        InterventionMedecin existante = obtenirIntervention(intervention.getIdIM());
        if (existante == null) {
            throw new ServiceException("Intervention introuvable avec l'ID : " + intervention.getIdIM());
        }

        validerIntervention(intervention);

        try {
            interventionRepository.update(intervention);
            return intervention;
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la modification de l'intervention", e);
        }
    }

    @Override
    public void supprimerIntervention(Long idIntervention) throws ServiceException {
        if (idIntervention == null) {
            throw new ServiceException("L'identifiant de l'intervention est obligatoire");
        }

        InterventionMedecin existante = obtenirIntervention(idIntervention);
        if (existante == null) {
            throw new ServiceException("Intervention introuvable avec l'ID : " + idIntervention);
        }

        try {
            interventionRepository.deleteById(idIntervention);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la suppression de l'intervention", e);
        }
    }

    @Override
    public InterventionMedecin obtenirIntervention(Long idIntervention) throws ServiceException {
        if (idIntervention == null) {
            throw new ServiceException("L'identifiant de l'intervention est obligatoire");
        }

        try {
            return interventionRepository.findById(idIntervention);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération de l'intervention", e);
        }
    }

    @Override
    public List<InterventionMedecin> listerToutesLesInterventions() throws ServiceException {
        try {
            return interventionRepository.findAll();
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération des interventions", e);
        }
    }

    @Override
    public List<InterventionMedecin> listerInterventionsParConsultation(Long idConsultation) throws ServiceException {
        if (idConsultation == null) {
            throw new ServiceException("L'identifiant de la consultation est obligatoire");
        }

        try {
            return interventionRepository.findByConsultationId(idConsultation);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération des interventions de la consultation", e);
        }
    }

    @Override
    public Double calculerMontantTotalConsultation(Long idConsultation) throws ServiceException {
        if (idConsultation == null) {
            throw new ServiceException("L'identifiant de la consultation est obligatoire");
        }

        try {
            List<InterventionMedecin> interventions = listerInterventionsParConsultation(idConsultation);

            return interventions.stream()
                    .map(InterventionMedecin::getPrixDePatient)
                    .filter(prix -> prix != null)
                    .reduce(0.0, Double::sum);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors du calcul du montant total", e);
        }
    }

    private void validerIntervention(InterventionMedecin intervention) throws ValidationException {
        if (intervention == null) {
            throw new ValidationException("L'intervention ne peut pas être nulle");
        }

        if (intervention.getIdConsultation() == null) {
            throw new ValidationException("La consultation associée est obligatoire");
        }

        if (intervention.getIdMedecin() == null) {
            throw new ValidationException("Le médecin est obligatoire");
        }

        if (intervention.getPrixDePatient() == null) {
            throw new ValidationException("Le prix est obligatoire");
        }

        if (intervention.getPrixDePatient() < 0) {
            throw new ValidationException("Le prix ne peut pas être négatif");
        }
    }
}
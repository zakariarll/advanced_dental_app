package ma.dentalTech.service.modules.dossierMedical.impl;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.dossierMedical.Consultation;
import ma.dentalTech.repository.modules.dossierMedical.api.ConsultationRepository;
import ma.dentalTech.repository.modules.dossierMedical.impl.mySQL.ConsultationRepositoryImpl;
import ma.dentalTech.service.modules.dossierMedical.api.ConsultationService;

import java.time.LocalDate;
import java.util.List;

public class ConsultationServiceImpl implements ConsultationService {

    private final ConsultationRepository consultationRepository;

    public ConsultationServiceImpl() {
        this.consultationRepository = new ConsultationRepositoryImpl();
    }

    public ConsultationServiceImpl(ConsultationRepository consultationRepository) {
        this.consultationRepository = consultationRepository;
    }

    @Override
    public Consultation creerConsultation(Consultation consultation) throws ServiceException, ValidationException {
        validerConsultation(consultation, true);

        if (consultation.getDate() == null) {
            consultation.setDate(LocalDate.now());
        }

        if (consultation.getStatut() == null || consultation.getStatut().isBlank()) {
            consultation.setStatut("EN_COURS");
        }

        try {
            consultationRepository.create(consultation);
            return consultation;
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la création de la consultation", e);
        }
    }

    @Override
    public Consultation modifierConsultation(Consultation consultation) throws ServiceException, ValidationException {
        if (consultation.getIdConsultation() == null) {
            throw new ValidationException("L'identifiant de la consultation est obligatoire");
        }

        Consultation existante = obtenirConsultation(consultation.getIdConsultation());
        if (existante == null) {
            throw new ServiceException("Consultation introuvable avec l'ID : " + consultation.getIdConsultation());
        }

        validerConsultation(consultation, false);

        try {
            consultationRepository.update(consultation);
            return consultation;
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la modification de la consultation", e);
        }
    }

    @Override
    public void supprimerConsultation(Long idConsultation) throws ServiceException {
        if (idConsultation == null) {
            throw new ServiceException("L'identifiant de la consultation est obligatoire");
        }

        Consultation existante = obtenirConsultation(idConsultation);
        if (existante == null) {
            throw new ServiceException("Consultation introuvable avec l'ID : " + idConsultation);
        }

        try {
            consultationRepository.deleteById(idConsultation);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la suppression de la consultation", e);
        }
    }

    @Override
    public Consultation obtenirConsultation(Long idConsultation) throws ServiceException {
        if (idConsultation == null) {
            throw new ServiceException("L'identifiant de la consultation est obligatoire");
        }

        try {
            return consultationRepository.findById(idConsultation);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération de la consultation", e);
        }
    }

    @Override
    public List<Consultation> listerToutesLesConsultations() throws ServiceException {
        try {
            return consultationRepository.findAll();
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération des consultations", e);
        }
    }

    @Override
    public Consultation obtenirConsultationParRDV(Long idRDV) throws ServiceException {
        if (idRDV == null) {
            throw new ServiceException("L'identifiant du RDV est obligatoire");
        }

        try {
            return consultationRepository.findByRDVId(idRDV).orElse(null);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération de la consultation par RDV", e);
        }
    }

    @Override
    public List<Consultation> listerConsultationsParDate(LocalDate date) throws ServiceException {
        if (date == null) {
            throw new ServiceException("La date est obligatoire");
        }

        try {
            return consultationRepository.findByDate(date);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération des consultations par date", e);
        }
    }

    @Override
    public void terminerConsultation(Long idConsultation, String observation) throws ServiceException, ValidationException {
        if (idConsultation == null) {
            throw new ServiceException("L'identifiant de la consultation est obligatoire");
        }

        Consultation consultation = obtenirConsultation(idConsultation);
        if (consultation == null) {
            throw new ServiceException("Consultation introuvable avec l'ID : " + idConsultation);
        }

        if ("TERMINEE".equals(consultation.getStatut())) {
            throw new ServiceException("La consultation est déjà terminée");
        }

        consultation.setStatut("TERMINEE");
        if (observation != null && !observation.isBlank()) {
            consultation.setObservationMedecin(observation);
        }

        modifierConsultation(consultation);
    }

    private void validerConsultation(Consultation consultation, boolean isCreation) throws ValidationException {
        if (consultation == null) {
            throw new ValidationException("La consultation ne peut pas être nulle");
        }

        if (consultation.getIdRDV() == null) {
            throw new ValidationException("Le RDV associé est obligatoire");
        }

        if (!isCreation && consultation.getDate() == null) {
            throw new ValidationException("La date de la consultation est obligatoire");
        }

        if (consultation.getStatut() != null && !consultation.getStatut().isBlank()) {
            String statut = consultation.getStatut().toUpperCase();
            if (!List.of("EN_COURS", "TERMINEE", "ANNULEE").contains(statut)) {
                throw new ValidationException("Statut invalide. Valeurs acceptées : EN_COURS, TERMINEE, ANNULEE");
            }
        }
    }
}
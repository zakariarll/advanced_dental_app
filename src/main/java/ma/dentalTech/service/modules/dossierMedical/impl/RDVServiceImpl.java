package ma.dentalTech.service.modules.dossierMedical.impl;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.common.validation.Validators;
import ma.dentalTech.entities.dossierMedical.RDV;
import ma.dentalTech.entities.enums.StatutRDV;
import ma.dentalTech.repository.modules.dossierMedical.api.RDVRepository;
import ma.dentalTech.service.modules.dossierMedical.api.RDVService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class RDVServiceImpl implements RDVService {

    private final RDVRepository rdvRepository;

    public RDVServiceImpl(RDVRepository rdvRepository) {
        this.rdvRepository = rdvRepository;
    }

    @Override
    public RDV creerRDV(RDV rdv) throws ServiceException, ValidationException {
        validerRDV(rdv);

        if (rdv.getStatut() == null) {
            rdv.setStatut(StatutRDV.PLANIFIE);
        }

        if (!verifierDisponibilite(rdv.getIdMedecin(), rdv.getDate(), rdv.getHeure())) {
            throw new ServiceException("Le créneau horaire n'est pas disponible pour ce médecin");
        }

        try {
            rdvRepository.create(rdv);
            return rdv;
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la création du RDV", e);
        }
    }

    @Override
    public RDV modifierRDV(RDV rdv) throws ServiceException, ValidationException {
        if (rdv.getIdRDV() == null) {
            throw new ValidationException("L'identifiant du RDV est obligatoire");
        }

        RDV existant = obtenirRDV(rdv.getIdRDV());
        if (existant == null) {
            throw new ServiceException("RDV introuvable avec l'ID : " + rdv.getIdRDV());
        }

        if (existant.getStatut() == StatutRDV.TERMINE) {
            throw new ServiceException("Impossible de modifier un RDV terminé");
        }

        if (existant.getStatut() == StatutRDV.ANNULE) {
            throw new ServiceException("Impossible de modifier un RDV annulé");
        }

        validerRDV(rdv);

        boolean changementDateHeure = !existant.getDate().equals(rdv.getDate()) ||
                !existant.getHeure().equals(rdv.getHeure());

        if (changementDateHeure && !verifierDisponibilite(rdv.getIdMedecin(), rdv.getDate(), rdv.getHeure())) {
            throw new ServiceException("Le nouveau créneau horaire n'est pas disponible");
        }

        try {
            rdvRepository.update(rdv);
            return rdv;
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la modification du RDV", e);
        }
    }

    @Override
    public void supprimerRDV(Long idRDV) throws ServiceException {
        if (idRDV == null) {
            throw new ServiceException("L'identifiant du RDV est obligatoire");
        }

        RDV existant = obtenirRDV(idRDV);
        if (existant == null) {
            throw new ServiceException("RDV introuvable avec l'ID : " + idRDV);
        }

        if (existant.getStatut() == StatutRDV.TERMINE) {
            throw new ServiceException("Impossible de supprimer un RDV terminé");
        }

        try {
            rdvRepository.deleteById(idRDV);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la suppression du RDV", e);
        }
    }

    @Override
    public RDV obtenirRDV(Long idRDV) throws ServiceException {
        if (idRDV == null) {
            throw new ServiceException("L'identifiant du RDV est obligatoire");
        }

        try {
            return rdvRepository.findById(idRDV);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération du RDV", e);
        }
    }

    @Override
    public List<RDV> listerTousLesRDV() throws ServiceException {
        try {
            return rdvRepository.findAll();
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération des RDV", e);
        }
    }

    @Override
    public List<RDV> listerRDVParPatient(Long idPatient) throws ServiceException {
        if (idPatient == null) {
            throw new ServiceException("L'identifiant du patient est obligatoire");
        }

        try {
            return rdvRepository.findByPatientId(idPatient);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération des RDV du patient", e);
        }
    }

    @Override
    public List<RDV> listerRDVParDate(LocalDate date) throws ServiceException {
        if (date == null) {
            throw new ServiceException("La date est obligatoire");
        }

        try {
            return rdvRepository.findByDate(date);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération des RDV par date", e);
        }
    }

    @Override
    public List<RDV> listerRDVParMedecin(Long idMedecin) throws ServiceException {
        if (idMedecin == null) {
            throw new ServiceException("L'identifiant du médecin est obligatoire");
        }

        try {
            return rdvRepository.findByMedecinId(idMedecin);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération des RDV du médecin", e);
        }
    }

    @Override
    public void changerStatutRDV(Long idRDV, StatutRDV nouveauStatut) throws ServiceException, ValidationException {
        if (idRDV == null) {
            throw new ServiceException("L'identifiant du RDV est obligatoire");
        }

        if (nouveauStatut == null) {
            throw new ValidationException("Le nouveau statut est obligatoire");
        }

        RDV rdv = obtenirRDV(idRDV);
        if (rdv == null) {
            throw new ServiceException("RDV introuvable avec l'ID : " + idRDV);
        }

        rdv.setStatut(nouveauStatut);
        modifierRDV(rdv);
    }

    @Override
    public void confirmerRDV(Long idRDV) throws ServiceException, ValidationException {
        if (idRDV == null) {
            throw new ServiceException("L'identifiant du RDV est obligatoire");
        }

        RDV rdv = obtenirRDV(idRDV);
        if (rdv == null) {
            throw new ServiceException("RDV introuvable avec l'ID : " + idRDV);
        }

        if (rdv.getStatut() != StatutRDV.PLANIFIE && rdv.getStatut() != StatutRDV.EN_ATTENTE) {
            throw new ServiceException("Seuls les RDV planifiés ou en attente peuvent être confirmés");
        }

        changerStatutRDV(idRDV, StatutRDV.CONFIRME);
    }

    @Override
    public void annulerRDV(Long idRDV, String raison) throws ServiceException, ValidationException {
        if (idRDV == null) {
            throw new ServiceException("L'identifiant du RDV est obligatoire");
        }

        RDV rdv = obtenirRDV(idRDV);
        if (rdv == null) {
            throw new ServiceException("RDV introuvable avec l'ID : " + idRDV);
        }

        if (rdv.getStatut() == StatutRDV.TERMINE) {
            throw new ServiceException("Impossible d'annuler un RDV terminé");
        }

        if (rdv.getStatut() == StatutRDV.ANNULE) {
            throw new ServiceException("Le RDV est déjà annulé");
        }

        if (raison != null && !raison.isBlank()) {
            rdv.setNoteMedecin("Annulation: " + raison);
        }

        rdv.setStatut(StatutRDV.ANNULE);

        try {
            rdvRepository.update(rdv);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de l'annulation du RDV", e);
        }
    }

    @Override
    public boolean verifierDisponibilite(Long idMedecin, LocalDate date, LocalTime heure) throws ServiceException {
        if (idMedecin == null || date == null || heure == null) {
            throw new ServiceException("Tous les paramètres sont obligatoires pour vérifier la disponibilité");
        }

        try {
            List<RDV> rdvsJour = rdvRepository.findByDate(date);

            for (RDV rdv : rdvsJour) {
                if (rdv.getIdMedecin().equals(idMedecin) &&
                        rdv.getStatut() != StatutRDV.ANNULE &&
                        rdv.getHeure().equals(heure)) {
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la vérification de disponibilité", e);
        }
    }

    private void validerRDV(RDV rdv) throws ValidationException {
        if (rdv == null) {
            throw new ValidationException("Le RDV ne peut pas être nul");
        }

        if (rdv.getIdPatient() == null) {
            throw new ValidationException("Le patient est obligatoire");
        }

        if (rdv.getIdMedecin() == null) {
            throw new ValidationException("Le médecin est obligatoire");
        }

        if (rdv.getDate() == null) {
            throw new ValidationException("La date du RDV est obligatoire");
        }

        if (rdv.getHeure() == null) {
            throw new ValidationException("L'heure du RDV est obligatoire");
        }

        if (rdv.getDate().isBefore(LocalDate.now())) {
            throw new ValidationException("La date du RDV ne peut pas être dans le passé");
        }

        if (rdv.getDate().equals(LocalDate.now()) && rdv.getHeure().isBefore(LocalTime.now())) {
            throw new ValidationException("L'heure du RDV ne peut pas être dans le passé");
        }

        if (rdv.getMotif() == null || rdv.getMotif().isBlank()) {
            throw new ValidationException("Le motif du RDV est obligatoire");
        }

        Validators.minLen(rdv.getMotif(), 3, "Le motif");
    }
}
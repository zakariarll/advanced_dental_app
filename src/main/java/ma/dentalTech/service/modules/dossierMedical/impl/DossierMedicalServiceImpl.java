package ma.dentalTech.service.modules.dossierMedical.impl;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.entities.dossierMedical.DossierMedical;
import ma.dentalTech.repository.modules.dossierMedical.api.DossierMedicalRepository;
import ma.dentalTech.service.modules.dossierMedical.api.DossierMedicalService;

public class DossierMedicalServiceImpl implements DossierMedicalService {

    private final DossierMedicalRepository dossierMedicalRepository;

    public DossierMedicalServiceImpl(DossierMedicalRepository dossierMedicalRepository) {
        this.dossierMedicalRepository = dossierMedicalRepository;
    }

    @Override
    public DossierMedical obtenirDossierComplet(Long idPatient) throws ServiceException {
        if (idPatient == null) {
            throw new ServiceException("L'identifiant du patient est obligatoire");
        }

        try {
            DossierMedical dossier = dossierMedicalRepository.getDossierComplet(idPatient);

            if (dossier == null) {
                throw new ServiceException("Dossier médical introuvable pour le patient ID : " + idPatient);
            }

            return dossier;
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération du dossier médical", e);
        }
    }

    @Override
    public boolean patientADesRisquesCritiques(Long idPatient) throws ServiceException {
        if (idPatient == null) {
            throw new ServiceException("L'identifiant du patient est obligatoire");
        }

        try {
            DossierMedical dossier = obtenirDossierComplet(idPatient);
            return dossier != null && dossier.aDesRisquesCritiques();
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la vérification des risques critiques", e);
        }
    }

    @Override
    public int compterRDVPatient(Long idPatient) throws ServiceException {
        if (idPatient == null) {
            throw new ServiceException("L'identifiant du patient est obligatoire");
        }

        try {
            DossierMedical dossier = obtenirDossierComplet(idPatient);
            return dossier != null && dossier.getRdvs() != null ? dossier.getRdvs().size() : 0;
        } catch (Exception e) {
            throw new ServiceException("Erreur lors du comptage des RDV", e);
        }
    }

    @Override
    public int compterConsultationsPatient(Long idPatient) throws ServiceException {
        if (idPatient == null) {
            throw new ServiceException("L'identifiant du patient est obligatoire");
        }

        try {
            DossierMedical dossier = obtenirDossierComplet(idPatient);
            return dossier != null && dossier.getConsultations() != null ?
                    dossier.getConsultations().size() : 0;
        } catch (Exception e) {
            throw new ServiceException("Erreur lors du comptage des consultations", e);
        }
    }
}
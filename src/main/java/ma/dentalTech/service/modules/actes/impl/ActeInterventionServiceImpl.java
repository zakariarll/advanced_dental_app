package ma.dentalTech.service.modules.actes.impl;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.actes.Acte;
import ma.dentalTech.entities.actes.ActeIntervention;
import ma.dentalTech.repository.modules.actes.api.ActeInterventionRepository;
import ma.dentalTech.repository.modules.actes.api.ActeRepository;
import ma.dentalTech.repository.modules.actes.impl.mySQL.ActeInterventionRepositoryImpl;
import ma.dentalTech.repository.modules.actes.impl.mySQL.ActeRepositoryImpl;
import ma.dentalTech.service.modules.actes.api.ActeInterventionService;

import java.util.ArrayList;
import java.util.List;

public class ActeInterventionServiceImpl implements ActeInterventionService {

    private final ActeInterventionRepository acteInterventionRepository;
    private final ActeRepository acteRepository;

    public ActeInterventionServiceImpl() {
        this.acteInterventionRepository = new ActeInterventionRepositoryImpl();
        this.acteRepository = new ActeRepositoryImpl();
    }

    public ActeInterventionServiceImpl(ActeInterventionRepository acteInterventionRepository, ActeRepository acteRepository) {
        this.acteInterventionRepository = acteInterventionRepository;
        this.acteRepository = acteRepository;
    }

    @Override
    public ActeIntervention ajouterActeAIntervention(Long idActe, Long idIM) throws ServiceException, ValidationException {
        if (idActe == null) {
            throw new ValidationException("L'identifiant de l'acte est obligatoire");
        }
        if (idIM == null) {
            throw new ValidationException("L'identifiant de l'intervention est obligatoire");
        }
        Acte acte = acteRepository.findById(idActe);
        if (acte == null) {
            throw new ServiceException("Acte introuvable avec l'ID : " + idActe);
        }
        if (acteInterventionRepository.existsByActeIdAndInterventionMedecinId(idActe, idIM)) {
            throw new ServiceException("Cet acte est déjà associé à cette intervention");
        }
        ActeIntervention ai = ActeIntervention.builder()
                .idActe(idActe)
                .idIM(idIM)
                .build();
        try {
            acteInterventionRepository.create(ai);
            return ai;
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de l'ajout de l'acte à l'intervention", e);
        }
    }

    @Override
    public void retirerActeDeIntervention(Long idActeIntervention) throws ServiceException {
        if (idActeIntervention == null) {
            throw new ServiceException("L'identifiant de l'acte-intervention est obligatoire");
        }
        ActeIntervention ai = acteInterventionRepository.findById(idActeIntervention);
        if (ai == null) {
            throw new ServiceException("Acte-intervention introuvable avec l'ID : " + idActeIntervention);
        }
        try {
            acteInterventionRepository.deleteById(idActeIntervention);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors du retrait de l'acte de l'intervention", e);
        }
    }

    @Override
    public void retirerTousLesActesDeIntervention(Long idIM) throws ServiceException {
        if (idIM == null) {
            throw new ServiceException("L'identifiant de l'intervention est obligatoire");
        }
        try {
            acteInterventionRepository.deleteByInterventionMedecinId(idIM);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors du retrait des actes de l'intervention", e);
        }
    }

    @Override
    public List<ActeIntervention> listerActesParIntervention(Long idIM) throws ServiceException {
        if (idIM == null) {
            throw new ServiceException("L'identifiant de l'intervention est obligatoire");
        }
        try {
            return acteInterventionRepository.findByInterventionMedecinId(idIM);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération des actes de l'intervention", e);
        }
    }

    @Override
    public List<Acte> obtenirActesDetailsParIntervention(Long idIM) throws ServiceException {
        if (idIM == null) {
            throw new ServiceException("L'identifiant de l'intervention est obligatoire");
        }
        try {
            List<ActeIntervention> actesInterventions = acteInterventionRepository.findByInterventionMedecinId(idIM);
            List<Acte> actes = new ArrayList<>();
            for (ActeIntervention ai : actesInterventions) {
                Acte acte = acteRepository.findById(ai.getIdActe());
                if (acte != null) {
                    actes.add(acte);
                }
            }
            return actes;
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération des détails des actes", e);
        }
    }

    @Override
    public Double calculerMontantTotalIntervention(Long idIM) throws ServiceException {
        if (idIM == null) {
            throw new ServiceException("L'identifiant de l'intervention est obligatoire");
        }
        try {
            List<Acte> actes = obtenirActesDetailsParIntervention(idIM);
            return actes.stream()
                    .map(Acte::getPrixDeBase)
                    .filter(prix -> prix != null)
                    .reduce(0.0, Double::sum);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors du calcul du montant total", e);
        }
    }

    @Override
    public int compterUtilisationsActe(Long idActe) throws ServiceException {
        if (idActe == null) {
            throw new ServiceException("L'identifiant de l'acte est obligatoire");
        }
        try {
            return acteInterventionRepository.countByActeId(idActe);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors du comptage des utilisations de l'acte", e);
        }
    }

    @Override
    public boolean acteEstUtiliseDansIntervention(Long idActe, Long idIM) throws ServiceException {
        if (idActe == null || idIM == null) {
            throw new ServiceException("Les identifiants de l'acte et de l'intervention sont obligatoires");
        }
        try {
            return acteInterventionRepository.existsByActeIdAndInterventionMedecinId(idActe, idIM);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la vérification de l'utilisation de l'acte", e);
        }
    }

    @Override
    public List<ActeIntervention> listerInterventionsParActe(Long idActe) throws ServiceException {
        if (idActe == null) {
            throw new ServiceException("L'identifiant de l'acte est obligatoire");
        }
        try {
            return acteInterventionRepository.findByActeId(idActe);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération des interventions par acte", e);
        }
    }
}
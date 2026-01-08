package ma.dentalTech.service.modules.actes.impl;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.actes.Acte;
import ma.dentalTech.repository.modules.actes.api.ActeRepository;
import ma.dentalTech.repository.modules.actes.impl.mySQL.ActeRepositoryImpl;
import ma.dentalTech.service.modules.actes.api.ActeService;

import java.util.List;

public class ActeServiceImpl implements ActeService {

    private final ActeRepository acteRepository;

    public ActeServiceImpl() {
        this.acteRepository = new ActeRepositoryImpl();
    }

    public ActeServiceImpl(ActeRepository acteRepository) {
        this.acteRepository = acteRepository;
    }

    @Override
    public Acte creerActe(Acte acte) throws ServiceException, ValidationException {
        validerActe(acte);
        if (acteRepository.existsByCode(acte.getCode())) {
            throw new ValidationException("Un acte avec ce code existe déjà");
        }
        if (acteRepository.existsByLibelle(acte.getLibelle())) {
            throw new ValidationException("Un acte avec ce libellé existe déjà");
        }
        try {
            acteRepository.create(acte);
            return acte;
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la création de l'acte", e);
        }
    }

    @Override
    public Acte modifierActe(Acte acte) throws ServiceException, ValidationException {
        if (acte.getIdActe() == null) {
            throw new ValidationException("L'identifiant de l'acte est obligatoire");
        }
        Acte existant = obtenirActe(acte.getIdActe());
        if (existant == null) {
            throw new ServiceException("Acte introuvable avec l'ID : " + acte.getIdActe());
        }
        validerActe(acte);
        if (!existant.getCode().equals(acte.getCode()) && acteRepository.existsByCode(acte.getCode())) {
            throw new ValidationException("Un autre acte avec ce code existe déjà");
        }
        if (!existant.getLibelle().equals(acte.getLibelle()) && acteRepository.existsByLibelle(acte.getLibelle())) {
            throw new ValidationException("Un autre acte avec ce libellé existe déjà");
        }
        try {
            acteRepository.update(acte);
            return acte;
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la modification de l'acte", e);
        }
    }

    @Override
    public void supprimerActe(Long idActe) throws ServiceException {
        if (idActe == null) {
            throw new ServiceException("L'identifiant de l'acte est obligatoire");
        }
        Acte existant = obtenirActe(idActe);
        if (existant == null) {
            throw new ServiceException("Acte introuvable avec l'ID : " + idActe);
        }
        try {
            acteRepository.deleteById(idActe);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la suppression de l'acte", e);
        }
    }

    @Override
    public Acte obtenirActe(Long idActe) throws ServiceException {
        if (idActe == null) {
            throw new ServiceException("L'identifiant de l'acte est obligatoire");
        }
        try {
            return acteRepository.findById(idActe);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération de l'acte", e);
        }
    }

    @Override
    public List<Acte> listerTousLesActes() throws ServiceException {
        try {
            return acteRepository.findAll();
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération des actes", e);
        }
    }

    @Override
    public Acte obtenirActeParCode(Integer code) throws ServiceException {
        if (code == null) {
            throw new ServiceException("Le code est obligatoire");
        }
        try {
            return acteRepository.findByCode(code).orElse(null);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération de l'acte par code", e);
        }
    }

    @Override
    public List<Acte> listerActesParCategorie(String categorie) throws ServiceException {
        if (categorie == null || categorie.isBlank()) {
            throw new ServiceException("La catégorie est obligatoire");
        }
        try {
            return acteRepository.findByCategorie(categorie);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération des actes par catégorie", e);
        }
    }

    @Override
    public List<Acte> rechercherActesParLibelle(String libelle) throws ServiceException {
        if (libelle == null || libelle.isBlank()) {
            throw new ServiceException("Le libellé de recherche est obligatoire");
        }
        try {
            return acteRepository.findByLibelleContaining(libelle);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la recherche des actes", e);
        }
    }

    @Override
    public List<Acte> listerActesParPrix(Double prixMin, Double prixMax) throws ServiceException {
        if (prixMin == null || prixMax == null) {
            throw new ServiceException("Les prix minimum et maximum sont obligatoires");
        }
        if (prixMin < 0 || prixMax < 0) {
            throw new ServiceException("Les prix ne peuvent pas être négatifs");
        }
        if (prixMin > prixMax) {
            throw new ServiceException("Le prix minimum doit être inférieur au prix maximum");
        }
        try {
            return acteRepository.findByPrixBetween(prixMin, prixMax);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération des actes par prix", e);
        }
    }

    @Override
    public List<String> listerToutesLesCategories() throws ServiceException {
        try {
            return acteRepository.findAllCategories();
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération des catégories", e);
        }
    }

    @Override
    public Acte appliquerRemise(Long idActe, Double pourcentage) throws ServiceException, ValidationException {
        if (idActe == null) {
            throw new ServiceException("L'identifiant de l'acte est obligatoire");
        }
        if (pourcentage == null || pourcentage <= 0 || pourcentage > 100) {
            throw new ValidationException("Le pourcentage doit être compris entre 0 et 100");
        }
        Acte acte = obtenirActe(idActe);
        if (acte == null) {
            throw new ServiceException("Acte introuvable avec l'ID : " + idActe);
        }
        Double reduction = acte.getPrixDeBase() * (pourcentage / 100);
        Double nouveauPrix = acte.getPrixDeBase() - reduction;
        acte.setPrixDeBase(nouveauPrix);
        return modifierActe(acte);
    }

    @Override
    public Acte modifierPrix(Long idActe, Double nouveauPrix) throws ServiceException, ValidationException {
        if (idActe == null) {
            throw new ServiceException("L'identifiant de l'acte est obligatoire");
        }
        if (nouveauPrix == null || nouveauPrix < 0) {
            throw new ValidationException("Le nouveau prix doit être supérieur ou égal à 0");
        }
        Acte acte = obtenirActe(idActe);
        if (acte == null) {
            throw new ServiceException("Acte introuvable avec l'ID : " + idActe);
        }
        acte.setPrixDeBase(nouveauPrix);
        return modifierActe(acte);
    }

    @Override
    public boolean codeExiste(Integer code) throws ServiceException {
        if (code == null) {
            throw new ServiceException("Le code est obligatoire");
        }
        try {
            return acteRepository.existsByCode(code);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la vérification du code", e);
        }
    }

    @Override
    public boolean libelleExiste(String libelle) throws ServiceException {
        if (libelle == null || libelle.isBlank()) {
            throw new ServiceException("Le libellé est obligatoire");
        }
        try {
            return acteRepository.existsByLibelle(libelle);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la vérification du libellé", e);
        }
    }

    private void validerActe(Acte acte) throws ValidationException {
        if (acte == null) {
            throw new ValidationException("L'acte ne peut pas être nul");
        }
        if (acte.getLibelle() == null || acte.getLibelle().isBlank()) {
            throw new ValidationException("Le libellé est obligatoire");
        }
        if (acte.getLibelle().length() < 3) {
            throw new ValidationException("Le libellé doit contenir au moins 3 caractères");
        }
        if (acte.getCategorie() == null || acte.getCategorie().isBlank()) {
            throw new ValidationException("La catégorie est obligatoire");
        }
        if (acte.getPrixDeBase() == null) {
            throw new ValidationException("Le prix de base est obligatoire");
        }
        if (acte.getPrixDeBase() < 0) {
            throw new ValidationException("Le prix de base ne peut pas être négatif");
        }
        if (acte.getCode() == null) {
            throw new ValidationException("Le code est obligatoire");
        }
        if (acte.getCode() <= 0) {
            throw new ValidationException("Le code doit être un nombre positif");
        }
    }
}
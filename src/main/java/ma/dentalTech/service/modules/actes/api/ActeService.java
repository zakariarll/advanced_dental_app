package ma.dentalTech.service.modules.actes.api;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.actes.Acte;

import java.util.List;

public interface ActeService {

    Acte creerActe(Acte acte) throws ServiceException, ValidationException;

    Acte modifierActe(Acte acte) throws ServiceException, ValidationException;

    void supprimerActe(Long idActe) throws ServiceException;

    Acte obtenirActe(Long idActe) throws ServiceException;

    List<Acte> listerTousLesActes() throws ServiceException;

    Acte obtenirActeParCode(Integer code) throws ServiceException;

    List<Acte> listerActesParCategorie(String categorie) throws ServiceException;

    List<Acte> rechercherActesParLibelle(String libelle) throws ServiceException;

    List<Acte> listerActesParPrix(Double prixMin, Double prixMax) throws ServiceException;

    List<String> listerToutesLesCategories() throws ServiceException;

    Acte appliquerRemise(Long idActe, Double pourcentage) throws ServiceException, ValidationException;

    Acte modifierPrix(Long idActe, Double nouveauPrix) throws ServiceException, ValidationException;

    boolean codeExiste(Integer code) throws ServiceException;

    boolean libelleExiste(String libelle) throws ServiceException;
}
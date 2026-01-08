package ma.dentalTech.mvc.controllers.modules.actes.api;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.mvc.dto.actes.ActeDTO;

public interface ActeController {

    void afficherTousLesActes() throws ServiceException;

    void afficherActesParCategorie(String categorie) throws ServiceException;

    void rechercherActesParLibelle(String libelle) throws ServiceException;

    void afficherActesParPrix(Double prixMin, Double prixMax) throws ServiceException;

    void afficherCategories() throws ServiceException;

    void creerActe(ActeDTO acteDTO) throws ServiceException, ValidationException;

    void modifierActe(ActeDTO acteDTO) throws ServiceException, ValidationException;

    void supprimerActe(Long idActe) throws ServiceException;

    void afficherActe(Long idActe) throws ServiceException;

    void appliquerRemise(Long idActe, Double pourcentage) throws ServiceException, ValidationException;

    void modifierPrix(Long idActe, Double nouveauPrix) throws ServiceException, ValidationException;
}
package ma.dentalTech.mvc.controllers.modules.actes.swing_implementation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.actes.Acte;
import ma.dentalTech.mvc.controllers.modules.actes.api.ActeController;
import ma.dentalTech.mvc.dto.actes.ActeDTO;
import ma.dentalTech.mvc.ui.modules.actes.ActeView;
import ma.dentalTech.service.modules.actes.api.ActeService;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActeControllerImpl implements ActeController {

    private ActeService acteService;

    @Override
    public void afficherTousLesActes() throws ServiceException {
        try {
            List<Acte> actes = acteService.listerTousLesActes();
            List<ActeDTO> dtos = convertirListeActesEnDTO(actes);
            ActeView.afficherListeActes(dtos, "Tous les actes");
        } catch (ServiceException e) {
            ActeView.afficherErreur("Erreur lors de la récupération des actes: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void afficherActesParCategorie(String categorie) throws ServiceException {
        try {
            List<Acte> actes = acteService.listerActesParCategorie(categorie);
            List<ActeDTO> dtos = convertirListeActesEnDTO(actes);
            ActeView.afficherListeActes(dtos, "Actes de la catégorie: " + categorie);
        } catch (ServiceException e) {
            ActeView.afficherErreur("Erreur lors de la récupération des actes par catégorie: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void rechercherActesParLibelle(String libelle) throws ServiceException {
        try {
            List<Acte> actes = acteService.rechercherActesParLibelle(libelle);
            List<ActeDTO> dtos = convertirListeActesEnDTO(actes);
            ActeView.afficherListeActes(dtos, "Résultats de recherche pour: " + libelle);
        } catch (ServiceException e) {
            ActeView.afficherErreur("Erreur lors de la recherche: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void afficherActesParPrix(Double prixMin, Double prixMax) throws ServiceException {
        try {
            List<Acte> actes = acteService.listerActesParPrix(prixMin, prixMax);
            List<ActeDTO> dtos = convertirListeActesEnDTO(actes);
            ActeView.afficherListeActes(dtos, "Actes entre " + formaterPrix(prixMin) + " et " + formaterPrix(prixMax));
        } catch (ServiceException e) {
            ActeView.afficherErreur("Erreur lors de la récupération des actes par prix: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void afficherCategories() throws ServiceException {
        try {
            List<String> categories = acteService.listerToutesLesCategories();
            ActeView.afficherCategories(categories);
        } catch (ServiceException e) {
            ActeView.afficherErreur("Erreur lors de la récupération des catégories: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void creerActe(ActeDTO acteDTO) throws ServiceException, ValidationException {
        try {
            Acte acte = convertirDTOEnActe(acteDTO);
            acteService.creerActe(acte);
            ActeView.afficherSucces("Acte créé avec succès");
        } catch (ServiceException | ValidationException e) {
            ActeView.afficherErreur("Erreur lors de la création de l'acte: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void modifierActe(ActeDTO acteDTO) throws ServiceException, ValidationException {
        try {
            Acte acte = convertirDTOEnActe(acteDTO);
            acteService.modifierActe(acte);
            ActeView.afficherSucces("Acte modifié avec succès");
        } catch (ServiceException | ValidationException e) {
            ActeView.afficherErreur("Erreur lors de la modification de l'acte: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void supprimerActe(Long idActe) throws ServiceException {
        try {
            acteService.supprimerActe(idActe);
            ActeView.afficherSucces("Acte supprimé avec succès");
        } catch (ServiceException e) {
            ActeView.afficherErreur("Erreur lors de la suppression de l'acte: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void afficherActe(Long idActe) throws ServiceException {
        try {
            Acte acte = acteService.obtenirActe(idActe);
            ActeDTO dto = convertirActeEnDTO(acte);
            ActeView.afficherDetailActe(dto);
        } catch (ServiceException e) {
            ActeView.afficherErreur("Erreur lors de la récupération de l'acte: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void appliquerRemise(Long idActe, Double pourcentage) throws ServiceException, ValidationException {
        try {
            Acte acte = acteService.appliquerRemise(idActe, pourcentage);
            ActeDTO dto = convertirActeEnDTO(acte);
            ActeView.afficherSucces("Remise de " + pourcentage + "% appliquée avec succès");
            ActeView.afficherDetailActe(dto);
        } catch (ServiceException | ValidationException e) {
            ActeView.afficherErreur("Erreur lors de l'application de la remise: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void modifierPrix(Long idActe, Double nouveauPrix) throws ServiceException, ValidationException {
        try {
            Acte acte = acteService.modifierPrix(idActe, nouveauPrix);
            ActeDTO dto = convertirActeEnDTO(acte);
            ActeView.afficherSucces("Prix modifié avec succès");
            ActeView.afficherDetailActe(dto);
        } catch (ServiceException | ValidationException e) {
            ActeView.afficherErreur("Erreur lors de la modification du prix: " + e.getMessage());
            throw e;
        }
    }

    private ActeDTO convertirActeEnDTO(Acte acte) {
        if (acte == null) {
            return null;
        }
        return ActeDTO.builder()
                .idActe(acte.getIdActe())
                .libelle(acte.getLibelle())
                .categorie(acte.getCategorie())
                .prixDeBase(acte.getPrixDeBase())
                .description(acte.getDescription())
                .code(acte.getCode())
                .prixFormate(formaterPrix(acte.getPrixDeBase()))
                .build();
    }

    private Acte convertirDTOEnActe(ActeDTO dto) {
        if (dto == null) {
            return null;
        }
        return Acte.builder()
                .idActe(dto.getIdActe())
                .libelle(dto.getLibelle())
                .categorie(dto.getCategorie())
                .prixDeBase(dto.getPrixDeBase())
                .description(dto.getDescription())
                .code(dto.getCode())
                .build();
    }

    private List<ActeDTO> convertirListeActesEnDTO(List<Acte> actes) {
        return actes.stream()
                .map(this::convertirActeEnDTO)
                .collect(Collectors.toList());
    }

    private String formaterPrix(Double prix) {
        if (prix == null) {
            return "0.00 MAD";
        }
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("fr", "MA"));
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(2);
        return prix + " MAD";
    }
}
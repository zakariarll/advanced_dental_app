/**package ma.dentalTech.mvc.controllers.modules.dossierMedical.swing_implementation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.dossierMedical.RDV;
import ma.dentalTech.entities.enums.StatutRDV;
import ma.dentalTech.mvc.controllers.modules.dossierMedical.api.RDVController;
import ma.dentalTech.mvc.dto.dossierMedical.RDVDTO;
import ma.dentalTech.mvc.ui.modules.dossierMedical.RDVView;
import ma.dentalTech.service.modules.dossierMedical.api.RDVService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RDVControllerImpl implements RDVController {

    private RDVService rdvService;

    @Override
    public void afficherTousLesRDV() throws ServiceException {
        try {
            List<RDV> rdvs = rdvService.listerTousLesRDV();
            List<RDVDTO> dtos = convertirListeRDVsEnDTO(rdvs);
            RDVView.afficherListeRDV(dtos, "Tous les rendez-vous");
        } catch (ServiceException e) {
            RDVView.afficherErreur("Erreur lors de la récupération des RDV: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void afficherRDVParPatient(Long idPatient) throws ServiceException {
        try {
            List<RDV> rdvs = rdvService.listerRDVParPatient(idPatient);
            List<RDVDTO> dtos = convertirListeRDVsEnDTO(rdvs);
            RDVView.afficherListeRDV(dtos, "RDV du patient #" + idPatient);
        } catch (ServiceException e) {
            RDVView.afficherErreur("Erreur lors de la récupération des RDV du patient: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void afficherRDVParDate(LocalDate date) throws ServiceException {
        try {
            List<RDV> rdvs = rdvService.listerRDVParDate(date);
            List<RDVDTO> dtos = convertirListeRDVsEnDTO(rdvs);
            RDVView.afficherListeRDV(dtos, "RDV du " + formaterDate(date));
        } catch (ServiceException e) {
            RDVView.afficherErreur("Erreur lors de la récupération des RDV par date: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void afficherRDVParMedecin(Long idMedecin) throws ServiceException {
        try {
            List<RDV> rdvs = rdvService.listerRDVParMedecin(idMedecin);
            List<RDVDTO> dtos = convertirListeRDVsEnDTO(rdvs);
            RDVView.afficherListeRDV(dtos, "RDV du médecin #" + idMedecin);
        } catch (ServiceException e) {
            RDVView.afficherErreur("Erreur lors de la récupération des RDV du médecin: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void afficherRDV(Long idRDV) throws ServiceException {
        try {
            RDV rdv = rdvService.obtenirRDV(idRDV);
            RDVDTO dto = convertirRDVEnDTO(rdv);
            RDVView.afficherDetailRDV(dto);
        } catch (ServiceException e) {
            RDVView.afficherErreur("Erreur lors de la récupération du RDV: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void creerRDV(RDVDTO rdvDTO) throws ServiceException, ValidationException {
        try {
            RDV rdv = convertirDTOEnRDV(rdvDTO);
            rdvService.creerRDV(rdv);
            RDVView.afficherSucces("Rendez-vous créé avec succès");
        } catch (ServiceException | ValidationException e) {
            RDVView.afficherErreur("Erreur lors de la création du RDV: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void modifierRDV(RDVDTO rdvDTO) throws ServiceException, ValidationException {
        try {
            RDV rdv = convertirDTOEnRDV(rdvDTO);
            rdvService.modifierRDV(rdv);
            RDVView.afficherSucces("Rendez-vous modifié avec succès");
        } catch (ServiceException | ValidationException e) {
            RDVView.afficherErreur("Erreur lors de la modification du RDV: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void supprimerRDV(Long idRDV) throws ServiceException {
        try {
            rdvService.supprimerRDV(idRDV);
            RDVView.afficherSucces("Rendez-vous supprimé avec succès");
        } catch (ServiceException e) {
            RDVView.afficherErreur("Erreur lors de la suppression du RDV: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void changerStatutRDV(Long idRDV, StatutRDV nouveauStatut) throws ServiceException, ValidationException {
        try {
            rdvService.changerStatutRDV(idRDV, nouveauStatut);
            RDVView.afficherSucces("Statut du RDV modifié avec succès");
        } catch (ServiceException | ValidationException e) {
            RDVView.afficherErreur("Erreur lors du changement de statut: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void confirmerRDV(Long idRDV) throws ServiceException, ValidationException {
        try {
            rdvService.confirmerRDV(idRDV);
            RDVView.afficherSucces("Rendez-vous confirmé avec succès");
        } catch (ServiceException | ValidationException e) {
            RDVView.afficherErreur("Erreur lors de la confirmation du RDV: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void annulerRDV(Long idRDV, String raison) throws ServiceException, ValidationException {
        try {
            rdvService.annulerRDV(idRDV, raison);
            RDVView.afficherSucces("Rendez-vous annulé avec succès");
        } catch (ServiceException | ValidationException e) {
            RDVView.afficherErreur("Erreur lors de l'annulation du RDV: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void verifierDisponibilite(Long idMedecin, LocalDate date, LocalTime heure) throws ServiceException {
        try {
            boolean disponible = rdvService.verifierDisponibilite(idMedecin, date, heure);
            if (disponible) {
                RDVView.afficherSucces("Créneau disponible");
            } else {
                RDVView.afficherAlerte("Créneau non disponible");
            }
        } catch (ServiceException e) {
            RDVView.afficherErreur("Erreur lors de la vérification de disponibilité: " + e.getMessage());
            throw e;
        }
    }

    private RDVDTO convertirRDVEnDTO(RDV rdv) {
        if (rdv == null) {
            return null;
        }
        return RDVDTO.builder()
                .idRDV(rdv.getIdRDV())
                .date(rdv.getDate())
                .heure(rdv.getHeure())
                .motif(rdv.getMotif())
                .statut(rdv.getStatut())
                .noteMedecin(rdv.getNoteMedecin())
                .idPatient(rdv.getIdPatient())
                .idMedecin(rdv.getIdMedecin())
                .idSecretaire(rdv.getIdSecretaire())
                .dateFormatee(formaterDate(rdv.getDate()))
                .heureFormatee(formaterHeure(rdv.getHeure()))
                .statutAffichage(formaterStatut(rdv.getStatut()))
                .build();
    }

    private RDV convertirDTOEnRDV(RDVDTO dto) {
        if (dto == null) {
            return null;
        }
        return RDV.builder()
                .idRDV(dto.getIdRDV())
                .date(dto.getDate())
                .heure(dto.getHeure())
                .motif(dto.getMotif())
                .statut(dto.getStatut())
                .noteMedecin(dto.getNoteMedecin())
                .idPatient(dto.getIdPatient())
                .idMedecin(dto.getIdMedecin())
                .idSecretaire(dto.getIdSecretaire())
                .build();
    }

    private List<RDVDTO> convertirListeRDVsEnDTO(List<RDV> rdvs) {
        return rdvs.stream()
                .map(this::convertirRDVEnDTO)
                .collect(Collectors.toList());
    }

    private String formaterDate(LocalDate date) {
        if (date == null) {
            return "Non définie";
        }
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private String formaterHeure(LocalTime heure) {
        if (heure == null) {
            return "Non définie";
        }
        return heure.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    private String formaterStatut(StatutRDV statut) {
        if (statut == null) {
            return "INCONNU";
        }
        switch (statut) {
            case PLANIFIE:
                return "Planifié";
            case CONFIRME:
                return "Confirmé";
            case ANNULE:
                return "Annulé";
            case TERMINE:
                return "Terminé";
            case EN_ATTENTE:
                return "En attente";
            default:
                return statut.name();
        }
    }
}**/
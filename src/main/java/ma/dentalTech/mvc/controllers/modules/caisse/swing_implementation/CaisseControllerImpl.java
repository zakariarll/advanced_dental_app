package ma.dentalTech.mvc.controllers.modules.caisse.swing_implementation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.entities.caisse.Statistique;
import ma.dentalTech.mvc.controllers.modules.caisse.api.CaisseController;
import ma.dentalTech.mvc.dto.caisse.TableauDeBordDTO;
import ma.dentalTech.mvc.ui.modules.caisse.TableauDeBordView;
import ma.dentalTech.service.modules.caisse.api.CaisseService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaisseControllerImpl implements CaisseController {

    private CaisseService caisseService;

    @Override
    public void afficherChiffreAffaires(Long idCabinet, LocalDateTime debut, LocalDateTime fin) {
        try {
            Double chiffreAffaires = caisseService.calculerChiffreAffaires(idCabinet, debut, fin);
            TableauDeBordView.afficherMontant(chiffreAffaires, "Chiffre d'affaires de la période");
        } catch (ServiceException e) {
            TableauDeBordView.afficherErreur("Erreur lors du calcul du chiffre d'affaires: " + e.getMessage());
        }
    }

    @Override
    public void afficherBeneficeNet(Long idCabinet, LocalDateTime debut, LocalDateTime fin) {
        try {
            Double benefice = caisseService.calculerBeneficeNet(idCabinet, debut, fin);
            TableauDeBordView.afficherMontant(benefice, "Bénéfice net de la période");
        } catch (ServiceException e) {
            TableauDeBordView.afficherErreur("Erreur lors du calcul du bénéfice: " + e.getMessage());
        }
    }

    @Override
    public void afficherStatistiquesJournalieres(Long idCabinet, LocalDate date) {
        try {
            Map<String, Double> stats = caisseService.obtenirStatistiquesJournalieres(idCabinet, date);
            TableauDeBordView.afficherStatistiques(stats, "Statistiques du " + date);
        } catch (ServiceException e) {
            TableauDeBordView.afficherErreur("Erreur lors de la récupération des statistiques: " + e.getMessage());
        }
    }

    @Override
    public void afficherStatistiquesMensuelles(Long idCabinet, int mois, int annee) {
        try {
            Map<String, Double> stats = caisseService.obtenirStatistiquesMensuelles(idCabinet, mois, annee);
            TableauDeBordView.afficherStatistiques(stats, "Statistiques de " + mois + "/" + annee);
        } catch (ServiceException e) {
            TableauDeBordView.afficherErreur("Erreur lors de la récupération des statistiques: " + e.getMessage());
        }
    }

    @Override
    public void afficherStatistiquesAnnuelles(Long idCabinet, int annee) {
        try {
            Map<String, Double> stats = caisseService.obtenirStatistiquesAnnuelles(idCabinet, annee);
            TableauDeBordView.afficherStatistiques(stats, "Statistiques de l'année " + annee);
        } catch (ServiceException e) {
            TableauDeBordView.afficherErreur("Erreur lors de la récupération des statistiques: " + e.getMessage());
        }
    }

    @Override
    public void genererRapportFinancier(Long idCabinet, LocalDateTime debut, LocalDateTime fin) {
        try {
            List<Statistique> rapport = caisseService.genererRapportFinancier(idCabinet, debut, fin);
            TableauDeBordView.afficherRapportFinancier(rapport, debut, fin);
        } catch (ServiceException e) {
            TableauDeBordView.afficherErreur("Erreur lors de la génération du rapport: " + e.getMessage());
        }
    }

    @Override
    public void afficherTableauDeBord(Long idCabinet) {
        try {
            Map<String, Object> donnees = caisseService.obtenirTableauDeBord(idCabinet);
            TableauDeBordDTO tableauDeBord = construireTableauDeBordDTO(donnees);
            TableauDeBordView.afficherTableauDeBord(tableauDeBord);
        } catch (ServiceException e) {
            TableauDeBordView.afficherErreur("Erreur lors de la récupération du tableau de bord: " + e.getMessage());
        }
    }

    @Override
    public void afficherTauxRecouvrement(LocalDateTime debut, LocalDateTime fin) {
        try {
            Double taux = caisseService.calculerTauxRecouvrement(debut, fin);
            TableauDeBordView.afficherTaux(taux, "Taux de recouvrement de la période");
        } catch (ServiceException e) {
            TableauDeBordView.afficherErreur("Erreur lors du calcul du taux: " + e.getMessage());
        }
    }

    @Override
    public void afficherRepartitionRevenusParMois(Long idCabinet, int annee) {
        try {
            Map<String, Double> repartition = caisseService.obtenirRepartitionRevenusParMois(idCabinet, annee);
            TableauDeBordView.afficherRepartitionMensuelle(repartition, "Répartition des revenus " + annee);
        } catch (ServiceException e) {
            TableauDeBordView.afficherErreur("Erreur lors de la récupération de la répartition: " + e.getMessage());
        }
    }

    @Override
    public void afficherRepartitionChargesParMois(Long idCabinet, int annee) {
        try {
            Map<String, Double> repartition = caisseService.obtenirRepartitionChargesParMois(idCabinet, annee);
            TableauDeBordView.afficherRepartitionMensuelle(repartition, "Répartition des charges " + annee);
        } catch (ServiceException e) {
            TableauDeBordView.afficherErreur("Erreur lors de la récupération de la répartition: " + e.getMessage());
        }
    }

    private TableauDeBordDTO construireTableauDeBordDTO(Map<String, Object> donnees) {
        return TableauDeBordDTO.builder()
                .chiffreAffairesJour((Double) donnees.getOrDefault("chiffreAffairesJour", 0.0))
                .chiffreAffairesMois((Double) donnees.getOrDefault("chiffreAffairesMois", 0.0))
                .chiffreAffairesAnnee((Double) donnees.getOrDefault("chiffreAffairesAnnee", 0.0))
                .totalEncaissements((Double) donnees.getOrDefault("totalEncaissements", 0.0))
                .totalDecaissements((Double) donnees.getOrDefault("totalDecaissements", 0.0))
                .beneficeNet((Double) donnees.getOrDefault("beneficeNet", 0.0))
                .totalCredit((Double) donnees.getOrDefault("totalCredit", 0.0))
                .tauxRecouvrement((Double) donnees.getOrDefault("tauxRecouvrement", 0.0))
                .nombreFacturesImpayees((Integer) donnees.getOrDefault("nombreFacturesImpayees", 0))
                .repartitionMensuelle((Map<String, Double>) donnees.get("repartitionMensuelle"))
                .build();
    }
}
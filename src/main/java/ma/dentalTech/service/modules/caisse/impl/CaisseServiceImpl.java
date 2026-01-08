package ma.dentalTech.service.modules.caisse.impl;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.entities.caisse.Statistique;
import ma.dentalTech.repository.modules.caisse.api.ChargeRepository;
import ma.dentalTech.repository.modules.caisse.api.FactureRepository;
import ma.dentalTech.repository.modules.caisse.api.RevenueRepository;
import ma.dentalTech.repository.modules.caisse.impl.mySQL.ChargeRepositoryImpl;
import ma.dentalTech.repository.modules.caisse.impl.mySQL.FactureRepositoryImpl;
import ma.dentalTech.repository.modules.caisse.impl.mySQL.RevenueRepositoryImpl;
import ma.dentalTech.service.modules.caisse.api.CaisseService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CaisseServiceImpl implements CaisseService {

    private final FactureRepository factureRepository;
    private final RevenueRepository revenueRepository;
    private final ChargeRepository chargeRepository;

    public CaisseServiceImpl() {
        this.factureRepository = new FactureRepositoryImpl();
        this.revenueRepository = new RevenueRepositoryImpl();
        this.chargeRepository = new ChargeRepositoryImpl();
    }

    public CaisseServiceImpl(FactureRepository factureRepository, RevenueRepository revenueRepository, ChargeRepository chargeRepository) {
        this.factureRepository = factureRepository;
        this.revenueRepository = revenueRepository;
        this.chargeRepository = chargeRepository;
    }

    @Override
    public Double calculerChiffreAffaires(Long idCabinet, LocalDateTime debut, LocalDateTime fin) throws ServiceException {
        validerParametresPeriode(debut, fin);
        try {
            Double totalFactures = factureRepository.calculateTotalFactures(debut, fin);
            Double totalRevenues = revenueRepository.calculateTotalRevenuesBetween(idCabinet, debut, fin);
            return totalFactures + totalRevenues;
        } catch (Exception e) {
            throw new ServiceException("Erreur lors du calcul du chiffre d'affaires", e);
        }
    }

    @Override
    public Double calculerBeneficeNet(Long idCabinet, LocalDateTime debut, LocalDateTime fin) throws ServiceException {
        validerParametresPeriode(debut, fin);
        try {
            Double chiffreAffaires = calculerChiffreAffaires(idCabinet, debut, fin);
            Double totalCharges = chargeRepository.calculateTotalChargesBetween(idCabinet, debut, fin);
            return chiffreAffaires - totalCharges;
        } catch (Exception e) {
            throw new ServiceException("Erreur lors du calcul du bénéfice net", e);
        }
    }

    @Override
    public Double calculerTotalEncaissements(LocalDateTime debut, LocalDateTime fin) throws ServiceException {
        validerParametresPeriode(debut, fin);
        try {
            return factureRepository.calculateTotalPaye(debut, fin);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors du calcul des encaissements", e);
        }
    }

    @Override
    public Double calculerTotalDecaissements(Long idCabinet, LocalDateTime debut, LocalDateTime fin) throws ServiceException {
        validerParametresPeriode(debut, fin);
        try {
            return chargeRepository.calculateTotalChargesBetween(idCabinet, debut, fin);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors du calcul des décaissements", e);
        }
    }

    @Override
    public Map<String, Double> obtenirStatistiquesJournalieres(Long idCabinet, LocalDate date) throws ServiceException {
        if (date == null) {
            throw new ServiceException("La date est obligatoire");
        }
        LocalDateTime debut = date.atStartOfDay();
        LocalDateTime fin = date.atTime(LocalTime.MAX);
        Map<String, Double> stats = new HashMap<>();
        try {
            stats.put("chiffreAffaires", calculerChiffreAffaires(idCabinet, debut, fin));
            stats.put("encaissements", calculerTotalEncaissements(debut, fin));
            stats.put("decaissements", calculerTotalDecaissements(idCabinet, debut, fin));
            stats.put("beneficeNet", calculerBeneficeNet(idCabinet, debut, fin));
            stats.put("facturesEmises", (double) factureRepository.findByDateBetween(debut, fin).size());
            stats.put("facturesImpayees", (double) factureRepository.findFacturesImpayees().size());
            return stats;
        } catch (Exception e) {
            throw new ServiceException("Erreur lors du calcul des statistiques journalières", e);
        }
    }

    @Override
    public Map<String, Double> obtenirStatistiquesMensuelles(Long idCabinet, int mois, int annee) throws ServiceException {
        if (mois < 1 || mois > 12) {
            throw new ServiceException("Le mois doit être compris entre 1 et 12");
        }
        YearMonth yearMonth = YearMonth.of(annee, mois);
        LocalDateTime debut = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime fin = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);
        Map<String, Double> stats = new HashMap<>();
        try {
            stats.put("chiffreAffaires", calculerChiffreAffaires(idCabinet, debut, fin));
            stats.put("encaissements", calculerTotalEncaissements(debut, fin));
            stats.put("decaissements", calculerTotalDecaissements(idCabinet, debut, fin));
            stats.put("beneficeNet", calculerBeneficeNet(idCabinet, debut, fin));
            stats.put("totalFactures", factureRepository.calculateTotalFactures(debut, fin));
            stats.put("totalPaye", factureRepository.calculateTotalPaye(debut, fin));
            stats.put("totalImpaye", factureRepository.calculateTotalImpaye());
            stats.put("tauxRecouvrement", calculerTauxRecouvrement(debut, fin));
            return stats;
        } catch (Exception e) {
            throw new ServiceException("Erreur lors du calcul des statistiques mensuelles", e);
        }
    }

    @Override
    public Map<String, Double> obtenirStatistiquesAnnuelles(Long idCabinet, int annee) throws ServiceException {
        LocalDateTime debut = LocalDate.of(annee, 1, 1).atStartOfDay();
        LocalDateTime fin = LocalDate.of(annee, 12, 31).atTime(LocalTime.MAX);
        Map<String, Double> stats = new HashMap<>();
        try {
            stats.put("chiffreAffaires", calculerChiffreAffaires(idCabinet, debut, fin));
            stats.put("encaissements", calculerTotalEncaissements(debut, fin));
            stats.put("decaissements", calculerTotalDecaissements(idCabinet, debut, fin));
            stats.put("beneficeNet", calculerBeneficeNet(idCabinet, debut, fin));
            stats.put("totalRevenues", revenueRepository.calculateTotalRevenuesBetween(idCabinet, debut, fin));
            stats.put("totalCharges", chargeRepository.calculateTotalChargesBetween(idCabinet, debut, fin));
            return stats;
        } catch (Exception e) {
            throw new ServiceException("Erreur lors du calcul des statistiques annuelles", e);
        }
    }

    @Override
    public List<Statistique> genererRapportFinancier(Long idCabinet, LocalDateTime debut, LocalDateTime fin) throws ServiceException {
        validerParametresPeriode(debut, fin);
        List<Statistique> rapport = new ArrayList<>();
        try {
            rapport.add(Statistique.builder()
                    .nom("Chiffre d'Affaires")
                    .categorie("REVENUS")
                    .chiffre(calculerChiffreAffaires(idCabinet, debut, fin))
                    .dateCalcul(LocalDate.now())
                    .build());
            rapport.add(Statistique.builder()
                    .nom("Total Encaissements")
                    .categorie("REVENUS")
                    .chiffre(calculerTotalEncaissements(debut, fin))
                    .dateCalcul(LocalDate.now())
                    .build());
            rapport.add(Statistique.builder()
                    .nom("Total Décaissements")
                    .categorie("CHARGES")
                    .chiffre(calculerTotalDecaissements(idCabinet, debut, fin))
                    .dateCalcul(LocalDate.now())
                    .build());
            rapport.add(Statistique.builder()
                    .nom("Bénéfice Net")
                    .categorie("RESULTAT")
                    .chiffre(calculerBeneficeNet(idCabinet, debut, fin))
                    .dateCalcul(LocalDate.now())
                    .build());
            rapport.add(Statistique.builder()
                    .nom("Total Impayé")
                    .categorie("CREANCES")
                    .chiffre(factureRepository.calculateTotalImpaye())
                    .dateCalcul(LocalDate.now())
                    .build());
            rapport.add(Statistique.builder()
                    .nom("Taux de Recouvrement")
                    .categorie("PERFORMANCE")
                    .chiffre(calculerTauxRecouvrement(debut, fin))
                    .dateCalcul(LocalDate.now())
                    .build());
            return rapport;
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la génération du rapport financier", e);
        }
    }

    @Override
    public Map<String, Object> obtenirTableauDeBord(Long idCabinet) throws ServiceException {
        Map<String, Object> dashboard = new HashMap<>();
        LocalDate aujourd_hui = LocalDate.now();
        LocalDateTime debutJour = aujourd_hui.atStartOfDay();
        LocalDateTime finJour = aujourd_hui.atTime(LocalTime.MAX);
        LocalDateTime debutMois = aujourd_hui.withDayOfMonth(1).atStartOfDay();
        LocalDateTime finMois = YearMonth.from(aujourd_hui).atEndOfMonth().atTime(LocalTime.MAX);
        try {
            dashboard.put("statistiquesJournalieres", obtenirStatistiquesJournalieres(idCabinet, aujourd_hui));
            dashboard.put("statistiquesMensuelles", obtenirStatistiquesMensuelles(idCabinet, aujourd_hui.getMonthValue(), aujourd_hui.getYear()));
            dashboard.put("facturesImpayees", factureRepository.findFacturesImpayees());
            dashboard.put("totalImpaye", factureRepository.calculateTotalImpaye());
            dashboard.put("nombreFacturesJour", factureRepository.findByDateBetween(debutJour, finJour).size());
            dashboard.put("nombreFacturesMois", factureRepository.findByDateBetween(debutMois, finMois).size());
            return dashboard;
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération du tableau de bord", e);
        }
    }

    @Override
    public Double calculerTauxRecouvrement(LocalDateTime debut, LocalDateTime fin) throws ServiceException {
        validerParametresPeriode(debut, fin);
        try {
            Double totalFacture = factureRepository.calculateTotalFactures(debut, fin);
            Double totalPaye = factureRepository.calculateTotalPaye(debut, fin);
            if (totalFacture == 0) return 100.0;
            return (totalPaye / totalFacture) * 100;
        } catch (Exception e) {
            throw new ServiceException("Erreur lors du calcul du taux de recouvrement", e);
        }
    }

    @Override
    public Map<String, Double> obtenirRepartitionRevenusParMois(Long idCabinet, int annee) throws ServiceException {
        Map<String, Double> repartition = new HashMap<>();
        String[] mois = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"};
        try {
            for (int i = 1; i <= 12; i++) {
                YearMonth yearMonth = YearMonth.of(annee, i);
                LocalDateTime debut = yearMonth.atDay(1).atStartOfDay();
                LocalDateTime fin = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);
                Double total = revenueRepository.calculateTotalRevenuesBetween(idCabinet, debut, fin);
                repartition.put(mois[i - 1], total);
            }
            return repartition;
        } catch (Exception e) {
            throw new ServiceException("Erreur lors du calcul de la répartition des revenus", e);
        }
    }

    @Override
    public Map<String, Double> obtenirRepartitionChargesParMois(Long idCabinet, int annee) throws ServiceException {
        Map<String, Double> repartition = new HashMap<>();
        String[] mois = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"};
        try {
            for (int i = 1; i <= 12; i++) {
                YearMonth yearMonth = YearMonth.of(annee, i);
                LocalDateTime debut = yearMonth.atDay(1).atStartOfDay();
                LocalDateTime fin = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);
                Double total = chargeRepository.calculateTotalChargesBetween(idCabinet, debut, fin);
                repartition.put(mois[i - 1], total);
            }
            return repartition;
        } catch (Exception e) {
            throw new ServiceException("Erreur lors du calcul de la répartition des charges", e);
        }
    }

    private void validerParametresPeriode(LocalDateTime debut, LocalDateTime fin) throws ServiceException {
        if (debut == null || fin == null) {
            throw new ServiceException("Les dates de début et fin sont obligatoires");
        }
        if (debut.isAfter(fin)) {
            throw new ServiceException("La date de début doit être antérieure à la date de fin");
        }
    }
}
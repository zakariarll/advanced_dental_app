package ma.dentalTech.repository.modules.statistiques.api;
import ma.dentalTech.entities.caisse.Statistique;
import ma.dentalTech.repository.common.CrudRepository;

public interface StatistiqueRepository extends CrudRepository<Statistique, Long> {
    // Méthodes pour calculer à la volée
    double calculateTotalRevenue();
    long countPatients();
}
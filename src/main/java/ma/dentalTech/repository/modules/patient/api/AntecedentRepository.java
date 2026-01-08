package ma.dentalTech.repository.modules.patient.api;

import ma.dentalTech.entities.patient.Antecedent;
import ma.dentalTech.repository.common.CrudRepository;
import ma.dentalTech.entities.enums.CategorieAntecedent;
import ma.dentalTech.entities.enums.NiveauRisque;

import java.util.List;

public interface AntecedentRepository extends CrudRepository<Antecedent, Long> {

    // Méthode critique : Récupérer les antécédents d'un patient spécifique
    List<Antecedent> findByPatientId(Long idPatient);

    List<Antecedent> findByCategorie(CategorieAntecedent categorie);
    List<Antecedent> findByNiveauRisque(NiveauRisque niveau);
}
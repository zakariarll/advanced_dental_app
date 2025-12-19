package ma.dentalTech.repository.modules.medicament.api;

import ma.dentalTech.entities.medicament.Medicament;
import ma.dentalTech.repository.common.CrudRepository;

import java.util.List;

public interface MedicamentRepository extends CrudRepository<Medicament, Long> {

    // Recherche un médicament par nom (utile pour la prescription)
    List<Medicament> findByNomLike(String nom);

    // Filtrer par famille/type (ex: Antibiotique, Antalgique...)
    List<Medicament> findByType(String type);
}
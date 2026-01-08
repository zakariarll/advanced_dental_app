package ma.dentalTech.repository.modules.actes.api;

import ma.dentalTech.entities.actes.Acte;
import ma.dentalTech.repository.common.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ActeRepository extends CrudRepository<Acte, Long> {

    Optional<Acte> findByCode(Integer code);

    List<Acte> findByCategorie(String categorie);

    List<Acte> findByLibelleContaining(String libelle);

    List<Acte> findByPrixBetween(Double prixMin, Double prixMax);

    List<String> findAllCategories();

    boolean existsByCode(Integer code);

    boolean existsByLibelle(String libelle);
}
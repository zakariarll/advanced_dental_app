package ma.dentalTech.repository.modules.patient.api;

import ma.dentalTech.entities.patient.Patient;
import ma.dentalTech.repository.common.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends CrudRepository<Patient, Long> {

    Optional<Patient> findByTelephone(String telephone);

    // Recherche partielle (Case insensitive souvent géré par SQL)
    List<Patient> searchByNomOrPrenom(String keyword);

    // Pagination
    List<Patient> findPage(int limit, int offset);

    long count();
}
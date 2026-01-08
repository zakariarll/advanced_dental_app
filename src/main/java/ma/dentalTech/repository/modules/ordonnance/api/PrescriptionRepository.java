package ma.dentalTech.repository.modules.ordonnance.api;

import ma.dentalTech.entities.ordonnance.Prescription;
import ma.dentalTech.entities.ordonnance.Prescription;
import ma.dentalTech.repository.common.CrudRepository;

import java.util.List;

public interface PrescriptionRepository extends CrudRepository<Prescription, Long> {

    List<Prescription> findByOrdonnanceId(Long idOrdonnance);

    // Standard CRUD stub
    List<Prescription> findAll();

    Prescription findById(Long id);

    void create(Prescription p);

    void update(Prescription obj);

    void delete(Prescription obj);

    void deleteById(Long id);
}

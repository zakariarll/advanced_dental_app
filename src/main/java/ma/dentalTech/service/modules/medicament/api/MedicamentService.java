package ma.dentalTech.service.modules.medicament.api;

import ma.dentalTech.entities.medicament.Medicament;
import ma.dentalTech.service.common.CrudService;
import java.util.List;

public interface MedicamentService extends CrudService<Medicament, Long> {

    List<Medicament> searchByNom(String nom);
    List<Medicament> getRemboursablesOnly();
}
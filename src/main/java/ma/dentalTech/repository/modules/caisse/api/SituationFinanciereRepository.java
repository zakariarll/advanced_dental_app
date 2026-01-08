package ma.dentalTech.repository.modules.caisse.api;

import ma.dentalTech.entities.caisse.SituationFinanciere;
import ma.dentalTech.repository.common.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SituationFinanciereRepository extends CrudRepository<SituationFinanciere, Long> {

    Optional<SituationFinanciere> findByFactureId(Long idFacture);

    List<SituationFinanciere> findByStatut(String statut);

    List<SituationFinanciere> findByPatientId(Long idPatient);

    List<SituationFinanciere> findWithCredit();

    Double calculateTotalCredit();

    Double calculateTotalCreditsPatient(Long idPatient);
}
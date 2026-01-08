package ma.dentalTech.repository.modules.caisse.api;

import ma.dentalTech.entities.caisse.Charge;
import ma.dentalTech.repository.common.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ChargeRepository extends CrudRepository<Charge, Long> {

    List<Charge> findByCabinetId(Long idCabinet);

    List<Charge> findByDateBetween(LocalDateTime debut, LocalDateTime fin);

    List<Charge> findByTitreContaining(String titre);

    Double calculateTotalCharges(Long idCabinet);

    Double calculateTotalChargesBetween(Long idCabinet, LocalDateTime debut, LocalDateTime fin);
}
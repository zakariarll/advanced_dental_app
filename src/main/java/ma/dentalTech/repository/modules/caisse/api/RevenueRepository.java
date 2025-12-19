package ma.dentalTech.repository.modules.caisse.api;

import ma.dentalTech.entities.caisse.Revenue;
import ma.dentalTech.repository.common.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RevenueRepository extends CrudRepository<Revenue, Long> {

    List<Revenue> findByCabinetId(Long idCabinet);

    List<Revenue> findByDateBetween(LocalDateTime debut, LocalDateTime fin);

    List<Revenue> findByTitreContaining(String titre);

    Double calculateTotalRevenues(Long idCabinet);

    Double calculateTotalRevenuesBetween(Long idCabinet, LocalDateTime debut, LocalDateTime fin);
}
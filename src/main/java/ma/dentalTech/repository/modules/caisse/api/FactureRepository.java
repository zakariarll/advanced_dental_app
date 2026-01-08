package ma.dentalTech.repository.modules.caisse.api;

import ma.dentalTech.entities.caisse.Facture;
import ma.dentalTech.repository.common.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FactureRepository extends CrudRepository<Facture, Long> {

    Optional<Facture> findByConsultationId(Long idConsultation);

    List<Facture> findByStatut(String statut);

    List<Facture> findByDateBetween(LocalDateTime debut, LocalDateTime fin);

    List<Facture> findFacturesImpayees();

    List<Facture> findByPatientId(Long idPatient);

    Double calculateTotalFactures(LocalDateTime debut, LocalDateTime fin);

    Double calculateTotalPaye(LocalDateTime debut, LocalDateTime fin);

    Double calculateTotalImpaye();
}
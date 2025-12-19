package ma.dentalTech.repository.modules.dossierMedical.api;

import ma.dentalTech.entities.dossierMedical.Consultation;
import ma.dentalTech.repository.common.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ConsultationRepository extends CrudRepository<Consultation, Long> {

    // Retrouver la consultation liée à un RDV précis
    Optional<Consultation> findByRDVId(Long idRDV);

    // Consultations d'un jour donné
    List<Consultation> findByDate(LocalDate date);
}
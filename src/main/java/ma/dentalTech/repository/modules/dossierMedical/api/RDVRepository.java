package ma.dentalTech.repository.modules.dossierMedical.api;

import ma.dentalTech.entities.dossierMedical.RDV;
import ma.dentalTech.repository.common.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface RDVRepository extends CrudRepository<RDV, Long> {

    // Historique des RDV d'un patient
    List<RDV> findByPatientId(Long idPatient);

    // RDV d'une journée spécifique (pour l'Agenda)
    List<RDV> findByDate(LocalDate date);

    // RDV d'un médecin spécifique
    List<RDV> findByMedecinId(Long idMedecin);
}
package ma.dentalTech.repository.modules.ordonnance.api;

import ma.dentalTech.entities.ordonnance.Ordonnance;
import ma.dentalTech.repository.common.CrudRepository;

import java.util.List;

public interface OrdonnanceRepository extends CrudRepository<Ordonnance, Long> {

    // Récupérer l'historique des ordonnances d'un patient
    List<Ordonnance> findByPatientId(Long idPatient);

    // Récupérer les ordonnances créées par un médecin spécifique
    List<Ordonnance> findByMedecinId(Long idMedecin);
}
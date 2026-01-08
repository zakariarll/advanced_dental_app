package ma.dentalTech.repository.modules.dossierMedical.api;

import ma.dentalTech.entities.dossierMedical.InterventionMedecin;
import ma.dentalTech.repository.common.CrudRepository;

import java.util.List;

public interface InterventionMedecinRepository extends CrudRepository<InterventionMedecin, Long> {

    // Récupérer les interventions faites durant une consultation (pour la facturation)
    List<InterventionMedecin> findByConsultationId(Long idConsultation);
}
package ma.dentalTech.repository.modules.actes.api;

import ma.dentalTech.entities.actes.ActeIntervention;
import ma.dentalTech.repository.common.CrudRepository;

import java.util.List;

public interface ActeInterventionRepository extends CrudRepository<ActeIntervention, Long> {

    List<ActeIntervention> findByActeId(Long idActe);

    List<ActeIntervention> findByInterventionMedecinId(Long idIM);

    void deleteByInterventionMedecinId(Long idIM);

    boolean existsByActeIdAndInterventionMedecinId(Long idActe, Long idIM);

    int countByActeId(Long idActe);
}
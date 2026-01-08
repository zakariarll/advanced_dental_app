package ma.dentalTech.repository.modules.agenda.api;
import ma.dentalTech.entities.agenda.AgendaMensuel;
import ma.dentalTech.repository.common.CrudRepository;
import java.util.List;

public interface AgendaRepository extends CrudRepository<AgendaMensuel, Long> {
    List<AgendaMensuel> findByMedecin(Long idMedecin);
}
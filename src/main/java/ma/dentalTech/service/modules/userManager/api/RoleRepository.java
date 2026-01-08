package ma.dentalTech.repository.modules.userManager.api;
import ma.dentalTech.entities.userManager.Role;
import ma.dentalTech.repository.common.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByLibelle(String libelle);
}
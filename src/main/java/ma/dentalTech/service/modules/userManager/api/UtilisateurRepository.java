// UtilisateurRepository.java
package ma.dentalTech.repository.modules.userManager.api;
import ma.dentalTech.entities.userManager.Utilisateur;
import ma.dentalTech.repository.common.CrudRepository;
import java.util.Optional;

public interface UtilisateurRepository extends CrudRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByLogin(String login);
}
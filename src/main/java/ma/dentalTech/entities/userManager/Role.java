package ma.dentalTech.entities.userManager;

import java.util.List;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Role {
    private Long idRole;
    private String libelle;

    // Liste des privilèges (Table Role_Privileges)
    private List<String> privileges;
}
package ma.dentalTech.entities.userManager;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Admin {
    private Long idAdmin;
    private Long idUser;
    private Utilisateur utilisateur; // Pour l'accès facile aux données communes
}
package ma.dentalTech.entities.userManager;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Secretaire {
    private Long idSecretaire;
    private String numCNSS;
    private Double comission;

    private Long idUser;
    private Utilisateur utilisateur;
}
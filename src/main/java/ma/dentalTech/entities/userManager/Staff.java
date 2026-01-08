package ma.dentalTech.entities.userManager;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Staff {
    private Long idStaff;
    private Double salaire;
    private Double prime;
    private Integer soldConge;
    private Boolean etat;

    private Long idUser;
    private Utilisateur utilisateur;
}
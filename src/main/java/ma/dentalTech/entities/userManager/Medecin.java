package ma.dentalTech.entities.userManager;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Medecin {
    private Long idMedecin;
    private String specialite;

    private Long idUser;
    private Utilisateur utilisateur;

    @Override
    public String toString() {
        return utilisateur != null ? "Dr. " + utilisateur.getNom() : "Medecin " + idMedecin;
    }
}
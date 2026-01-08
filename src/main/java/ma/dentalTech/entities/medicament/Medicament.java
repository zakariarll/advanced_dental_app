package ma.dentalTech.entities.medicament;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Medicament {
    private Long idMct;
    private String nom;
    private String laboratoire;
    private String type;
    private String forme;
    private Boolean remboursable;
    private Double prixUnitaire;
    private String description;

    @Override
    public String toString() {
        return nom + " (" + forme + ")";
    }
}
package ma.dentalTech.entities.caisse;

import java.time.LocalDate;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Statistique {
    private Long idStatistique;
    private String nom;
    private String categorie;
    private Double chiffre;
    private LocalDate dateCalcul;

    private Long idCabinet;
}
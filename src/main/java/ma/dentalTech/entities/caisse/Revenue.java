package ma.dentalTech.entities.caisse;

import java.time.LocalDateTime;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Revenue {
    private Long idRevenue;
    private String titre;
    private String description;
    private Double montant;
    private LocalDateTime date;

    private Long idCabinet;
}
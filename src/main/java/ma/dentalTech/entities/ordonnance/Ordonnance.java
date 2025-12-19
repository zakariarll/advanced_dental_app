package ma.dentalTech.entities.ordonnance;

import java.time.LocalDate;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Ordonnance {
    private Long idOrd;
    private LocalDate date;

    private Long idPatient;
    private Long idMedecin;
}
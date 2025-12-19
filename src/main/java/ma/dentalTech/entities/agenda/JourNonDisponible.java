package ma.dentalTech.entities.agenda;

import java.time.LocalDate;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class JourNonDisponible {
    private Long idAgenda;
    private LocalDate jour;
}
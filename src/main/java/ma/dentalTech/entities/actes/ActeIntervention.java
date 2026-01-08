package ma.dentalTech.entities.actes;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ActeIntervention {
    private Long idActeIntervention;
    private Long idActe;
    private Long idIM;
}
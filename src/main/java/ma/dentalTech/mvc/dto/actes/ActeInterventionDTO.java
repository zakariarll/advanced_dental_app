package ma.dentalTech.mvc.dto.actes;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActeInterventionDTO {
    private Long idActeIntervention;
    private Long idActe;
    private Long idIM;
    private String libelleActe;
    private String categorieActe;
    private Double prixActe;
    private String prixFormate;
}
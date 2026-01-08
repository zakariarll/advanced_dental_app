package ma.dentalTech.mvc.dto.actes;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActeDTO {
    private Long idActe;
    private String libelle;
    private String categorie;
    private Double prixDeBase;
    private String description;
    private Integer code;
    private String prixFormate;
}
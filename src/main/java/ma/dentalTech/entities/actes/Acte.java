package ma.dentalTech.entities.actes;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Acte {
    private Long idActe;
    private String libelle;
    private String categorie;
    private Double prixDeBase;
    private String description;
    private Integer code;
}
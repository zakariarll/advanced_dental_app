package ma.dentalTech.entities.caisse;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SituationFinanciere {
    private Long idSF;
    private Double totaleDesActes;
    private Double totalePaye;
    private Double credit;
    private String statut;
    private String enPromo;

    private Long idFacture;
}
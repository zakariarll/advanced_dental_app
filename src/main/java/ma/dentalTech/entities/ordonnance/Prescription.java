package ma.dentalTech.entities.ordonnance;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Prescription {
    private Long idPr;
    private Integer quantite; // quantité
    private String frequence; // fréquence
    private Integer dureeEnJours; // duréeEnJours

    private Long idOrd; // FK Ordonnance
    private Long idMct; // FK Medicament
}
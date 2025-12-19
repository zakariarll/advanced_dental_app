package ma.dentalTech.entities.agenda;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AgendaMensuel {
    private Long idAgenda;
    private String mois;
    private Boolean etat;

    private Long idMedecin;
}
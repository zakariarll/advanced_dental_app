package ma.dentalTech.entities.dossierMedical;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class InterventionMedecin {
    private Long idIM;
    private Double prixDePatient;

    private Long idConsultation;
    private Long idMedecin;
}
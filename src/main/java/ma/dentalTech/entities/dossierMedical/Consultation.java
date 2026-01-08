package ma.dentalTech.entities.dossierMedical;

import java.time.LocalDate;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Consultation {
    private Long idConsultation;
    private LocalDate date;
    private String statut;
    private String observationMedecin;

    private Long idRDV;
}
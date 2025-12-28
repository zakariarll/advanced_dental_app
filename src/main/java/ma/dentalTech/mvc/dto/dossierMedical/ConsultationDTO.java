package ma.dentalTech.mvc.dto.dossierMedical;

import lombok.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConsultationDTO {
    private Long idConsultation;
    private LocalDate date;
    private String statut;
    private String observationMedecin;
    private Long idRDV;
    private String dateFormatee;
    private String statutAffichage;
}
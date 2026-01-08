package ma.dentalTech.mvc.dto.dossierMedical;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InterventionMedecinDTO {
    private Long idIM;
    private Double prixDePatient;
    private Long idConsultation;
    private Long idMedecin;
    private String nomMedecin;
    private String prixFormate;
}
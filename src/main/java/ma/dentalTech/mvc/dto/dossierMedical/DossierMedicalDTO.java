package ma.dentalTech.mvc.dto.dossierMedical;

import lombok.*;
import ma.dentalTech.mvc.dto.patient.AntecedentDTO;
import ma.dentalTech.mvc.dto.patient.PatientDTO;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DossierMedicalDTO {
    private PatientDTO patient;
    private List<AntecedentDTO> antecedents;
    private List<ConsultationDTO> consultations;
    private boolean aDesRisquesCritiques;
    private int nombreConsultations;
    private String statutRisque;
}
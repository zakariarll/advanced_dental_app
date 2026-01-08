package ma.dentalTech.entities.dossierMedical;

import java.util.ArrayList;
import java.util.List;
import lombok.*;
import ma.dentalTech.entities.patient.Antecedent;
import ma.dentalTech.entities.patient.Patient;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DossierMedical {
    private Patient patient;

    @Builder.Default
    private List<Antecedent> antecedents = new ArrayList<>();

    @Builder.Default
    private List<Consultation> consultations = new ArrayList<>();

    public boolean aDesRisquesCritiques() {
        return antecedents.stream()
                .anyMatch(a -> "CRITIQUE".equals(a.getNiveauRisque().name()));
    }
}
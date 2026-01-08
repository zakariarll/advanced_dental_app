package ma.dentalTech.entities.dossierMedical;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.*;
import ma.dentalTech.entities.enums.StatutRDV;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class RDV {
    private Long idRDV;
    private LocalDate date;
    private LocalTime heure;
    private String motif;
    private StatutRDV statut;
    private String noteMedecin;

    private Long idPatient;
    private Long idMedecin;
    private Long idSecretaire;
}

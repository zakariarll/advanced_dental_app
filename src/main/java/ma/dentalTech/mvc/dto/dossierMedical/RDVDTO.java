package ma.dentalTech.mvc.dto.dossierMedical;

import lombok.*;
import ma.dentalTech.entities.enums.StatutRDV;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RDVDTO {
    private Long idRDV;
    private LocalDate date;
    private LocalTime heure;
    private String motif;
    private StatutRDV statut;
    private String noteMedecin;
    private Long idPatient;
    private Long idMedecin;
    private Long idSecretaire;
    private String nomPatient;
    private String nomMedecin;
    private String nomSecretaire;
    private String dateFormatee;
    private String heureFormatee;
    private String statutAffichage;
}
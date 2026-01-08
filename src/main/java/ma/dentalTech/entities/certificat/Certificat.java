package ma.dentalTech.entities.certificat;

import java.time.LocalDate;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Certificat {
    private Long idCertif;
    private LocalDate dateDebut; // dateDébut
    private LocalDate dateFin;
    private Integer duree; // durée
    private String noteMedecin;

    private Long idPatient;
    private Long idMedecin;
}
package ma.dentalTech.mvc.dto.patient;

import lombok.*;
import ma.dentalTech.entities.enums.Assurance;
import ma.dentalTech.entities.enums.Sexe;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientDTO {
    private Long idPatient;
    private String nom;
    private String prenom;
    private LocalDate dateDeNaissance;
    private Sexe sexe;
    private String adresse;
    private String telephone;
    private Assurance assurance;
    private String nomComplet;
    private int age;
    private String dateCreationFormatee;
}
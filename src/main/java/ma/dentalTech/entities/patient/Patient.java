package ma.dentalTech.entities.patient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;
import ma.dentalTech.entities.enums.Assurance;
import ma.dentalTech.entities.enums.Sexe;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Patient {
    private Long idPatient;
    private String nom;
    private String prenom;
    private LocalDate dateDeNaissance;
    private Sexe sexe; // Mapped to VARCHAR in SQL
    private String adresse;
    private String telephone;
    private Assurance assurance; // Mapped to VARCHAR

    // Audit
    private LocalDate dateCreation;
    private LocalDateTime dateDerniereModification;
    private String modifiePar;
    private String creePar;

    @Override
    public String toString() {
        return nom + " " + prenom;
    }
}
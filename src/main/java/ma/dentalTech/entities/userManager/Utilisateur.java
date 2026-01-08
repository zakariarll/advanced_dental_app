package ma.dentalTech.entities.userManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;
import ma.dentalTech.entities.enums.Sexe;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Utilisateur {
    private Long idUser;
    private String nom;
    private String email;
    private String adresse;
    private String cin;
    private String tel;
    private Sexe sexe;
    private String login;
    private String motDePasse;
    private LocalDate lastLoginDate;
    private LocalDate dateNaissance;
    private LocalDate dateRecrutement;
    private String token;

    private Long idCabinet;
    private Long idRole;

    // Audit
    private LocalDate dateCreation;
    private LocalDateTime dateDerniereModification;
    private String modifiePar;
    private String creePar;

    // Helper pour afficher dans les combobox
    @Override
    public String toString() {
        return nom;
    }
}
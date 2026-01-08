package ma.dentalTech.entities.patient;

import lombok.*;
import ma.dentalTech.entities.enums.CategorieAntecedent;
import ma.dentalTech.entities.enums.NiveauRisque;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Antecedent {
    private Long idAntecedent;
    private String nom;
    private CategorieAntecedent categorie;
    private NiveauRisque niveauRisque; // niveauDeRisque in SQL

    private Long idPatient; // FK
}
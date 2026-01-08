package ma.dentalTech.mvc.dto.patient;

import ma.dentalTech.entities.enums.CategorieAntecedent;
import ma.dentalTech.entities.enums.NiveauRisque;

public record AntecedentDTO(
        Long idAntecedent,
        String nom,
        CategorieAntecedent categorie,
        NiveauRisque niveauRisque,
        Long idPatient
) {}
package ma.dentalTech.mvc.dto;


import java.util.List;
import ma.dentalTech.entities.patient.Antecedent;
import ma.dentalTech.entities.patient.Patient;

public record AntecedentDTO(Antecedent antecedent, List<Patient> patients) {}

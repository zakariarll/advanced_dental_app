package ma.dentalTech.mvc.dto.caisse;

import lombok.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.text.NumberFormat;
import java.util.Locale;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatistiqueDTO {
    private Long idStatistique;
    private String nom;
    private String categorie;
    private Double chiffre;
    private LocalDate dateCalcul;
    private Long idCabinet;

    private String chiffreFormate;
    private String dateCalculFormatee;

    public String getChiffreFormate() {
        if (chiffre == null) return "0.00";
        NumberFormat formatter = NumberFormat.getInstance(new Locale("fr", "MA"));
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter.format(chiffre);
    }

    public String getDateCalculFormatee() {
        if (dateCalcul == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return dateCalcul.format(formatter);
    }
}
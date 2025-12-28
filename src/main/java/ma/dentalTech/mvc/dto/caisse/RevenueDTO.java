package ma.dentalTech.mvc.dto.caisse;

import lombok.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.text.NumberFormat;
import java.util.Locale;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RevenueDTO {
    private Long idRevenue;
    private String titre;
    private String description;
    private Double montant;
    private LocalDateTime date;
    private Long idCabinet;

    private String montantFormate;
    private String dateFormatee;

    public String getMontantFormate() {
        if (montant == null) return "0.00 MAD";
        NumberFormat formatter = NumberFormat.getInstance(new Locale("fr", "MA"));
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter.format(montant) + " MAD";
    }

    public String getDateFormatee() {
        if (date == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return date.format(formatter);
    }
}
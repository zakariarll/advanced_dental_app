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
public class FactureDTO {
    private Long idFacture;
    private Double totaleFacture;
    private Double totalePaye;
    private Double reste;
    private String statut;
    private LocalDateTime dateFacture;
    private Long idConsultation;

    private String nomPatient;
    private String prenomPatient;
    private String totaleFactureFormate;
    private String totalePayeFormate;
    private String resteFormate;
    private String dateFactureFormatee;
    private Double pourcentagePaye;

    public String getTotaleFactureFormate() {
        return formaterMontant(totaleFacture);
    }

    public String getTotalePayeFormate() {
        return formaterMontant(totalePaye);
    }

    public String getResteFormate() {
        return formaterMontant(reste);
    }

    public String getDateFactureFormatee() {
        if (dateFacture == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dateFacture.format(formatter);
    }

    public Double getPourcentagePaye() {
        if (totaleFacture == null || totaleFacture == 0) return 0.0;
        if (totalePaye == null) return 0.0;
        return (totalePaye / totaleFacture) * 100;
    }

    private String formaterMontant(Double montant) {
        if (montant == null) return "0.00 MAD";
        NumberFormat formatter = NumberFormat.getInstance(new Locale("fr", "MA"));
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter.format(montant) + " MAD";
    }
}
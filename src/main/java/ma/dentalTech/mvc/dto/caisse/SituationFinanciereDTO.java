package ma.dentalTech.mvc.dto.caisse;

import lombok.*;
import java.text.NumberFormat;
import java.util.Locale;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SituationFinanciereDTO {
    private Long idSF;
    private Double totaleDesActes;
    private Double totalePaye;
    private Double credit;
    private String statut;
    private String enPromo;
    private Long idFacture;

    private String nomPatient;
    private String prenomPatient;
    private String totaleDesActesFormate;
    private String totalePayeFormate;
    private String creditFormate;
    private Double pourcentagePaye;

    public String getTotaleDesActesFormate() {
        return formaterMontant(totaleDesActes);
    }

    public String getTotalePayeFormate() {
        return formaterMontant(totalePaye);
    }

    public String getCreditFormate() {
        return formaterMontant(credit);
    }

    public Double getPourcentagePaye() {
        if (totaleDesActes == null || totaleDesActes == 0) return 0.0;
        if (totalePaye == null) return 0.0;
        return (totalePaye / totaleDesActes) * 100;
    }

    private String formaterMontant(Double montant) {
        if (montant == null) return "0.00 MAD";
        NumberFormat formatter = NumberFormat.getInstance(new Locale("fr", "MA"));
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter.format(montant) + " MAD";
    }
}
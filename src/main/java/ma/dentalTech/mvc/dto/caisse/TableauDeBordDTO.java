package ma.dentalTech.mvc.dto.caisse;

import lombok.*;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TableauDeBordDTO {
    private Double chiffreAffairesJour;
    private Double chiffreAffairesMois;
    private Double chiffreAffairesAnnee;
    private Double totalEncaissements;
    private Double totalDecaissements;
    private Double beneficeNet;
    private Double totalCredit;
    private Double tauxRecouvrement;
    private Integer nombreFacturesImpayees;
    private Map<String, Double> repartitionMensuelle;

    private String chiffreAffairesJourFormate;
    private String chiffreAffairesMoisFormate;
    private String chiffreAffairesAnneeFormate;
    private String totalEncaissementsFormate;
    private String totalDecaissementsFormate;
    private String beneficeNetFormate;
    private String totalCreditFormate;
    private String tauxRecouvrementFormate;

    public String getChiffreAffairesJourFormate() {
        return formaterMontant(chiffreAffairesJour);
    }

    public String getChiffreAffairesMoisFormate() {
        return formaterMontant(chiffreAffairesMois);
    }

    public String getChiffreAffairesAnneeFormate() {
        return formaterMontant(chiffreAffairesAnnee);
    }

    public String getTotalEncaissementsFormate() {
        return formaterMontant(totalEncaissements);
    }

    public String getTotalDecaissementsFormate() {
        return formaterMontant(totalDecaissements);
    }

    public String getBeneficeNetFormate() {
        return formaterMontant(beneficeNet);
    }

    public String getTotalCreditFormate() {
        return formaterMontant(totalCredit);
    }

    public String getTauxRecouvrementFormate() {
        if (tauxRecouvrement == null) return "0.00%";
        return String.format("%.2f%%", tauxRecouvrement);
    }

    private String formaterMontant(Double montant) {
        if (montant == null) return "0.00 MAD";
        NumberFormat formatter = NumberFormat.getInstance(new Locale("fr", "MA"));
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter.format(montant) + " MAD";
    }
}
package ma.dentalTech.mvc.ui.modules.caisse;

import ma.dentalTech.mvc.dto.caisse.SituationFinanciereDTO;

import javax.swing.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class SituationFinanciereView {

    public static void afficherListeSituations(List<SituationFinanciereDTO> situations, String titre) {
        SwingUtilities.invokeLater(() -> {
            if (situations == null || situations.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Aucune situation financière trouvée",
                        titre,
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder message = new StringBuilder();
            message.append(titre).append("\n");
            message.append("=".repeat(80)).append("\n\n");

            for (SituationFinanciereDTO situation : situations) {
                message.append("Situation #").append(situation.getIdSF()).append("\n");
                message.append("Facture: ").append(situation.getIdFacture()).append("\n");
                if (situation.getNomPatient() != null) {
                    message.append("Patient: ").append(situation.getNomPatient())
                            .append(" ").append(situation.getPrenomPatient()).append("\n");
                }
                message.append("Total des actes: ").append(situation.getTotaleDesActesFormate()).append("\n");
                message.append("Total payé: ").append(situation.getTotalePayeFormate()).append("\n");
                message.append("Crédit: ").append(situation.getCreditFormate()).append("\n");
                message.append("Statut: ").append(situation.getStatut()).append("\n");
                message.append("En promo: ").append(situation.getEnPromo() != null ? situation.getEnPromo() : "Non").append("\n");
                message.append("Pourcentage payé: ").append(String.format("%.2f%%", situation.getPourcentagePaye())).append("\n");
                message.append("-".repeat(80)).append("\n");
            }

            JTextArea textArea = new JTextArea(message.toString());
            textArea.setEditable(false);
            textArea.setCaretPosition(0);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(800, 500));

            JOptionPane.showMessageDialog(null,
                    scrollPane,
                    titre + " (" + situations.size() + " situation(s))",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    public static void afficherDetailSituation(SituationFinanciereDTO situation) {
        SwingUtilities.invokeLater(() -> {
            if (situation == null) {
                JOptionPane.showMessageDialog(null,
                        "Situation financière introuvable",
                        "Détail d'une situation",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            StringBuilder message = new StringBuilder();
            message.append("Détails de la situation #").append(situation.getIdSF()).append("\n");
            message.append("=".repeat(60)).append("\n\n");
            message.append("Facture: ").append(situation.getIdFacture()).append("\n");
            if (situation.getNomPatient() != null) {
                message.append("Patient: ").append(situation.getNomPatient())
                        .append(" ").append(situation.getPrenomPatient()).append("\n");
            }
            message.append("Total des actes: ").append(situation.getTotaleDesActesFormate()).append("\n");
            message.append("Total payé: ").append(situation.getTotalePayeFormate()).append("\n");
            message.append("Crédit: ").append(situation.getCreditFormate()).append("\n");
            message.append("Statut: ").append(situation.getStatut()).append("\n");
            message.append("En promo: ").append(situation.getEnPromo() != null ? situation.getEnPromo() : "Non").append("\n");
            message.append("Pourcentage payé: ").append(String.format("%.2f%%", situation.getPourcentagePaye())).append("\n");

            JOptionPane.showMessageDialog(null,
                    message.toString(),
                    "Détail d'une situation",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    public static void afficherMontantTotal(Double montant, String titre) {
        SwingUtilities.invokeLater(() -> {
            String montantFormate = formaterMontant(montant);
            StringBuilder message = new StringBuilder();
            message.append(titre).append("\n\n");
            message.append("Total: ").append(montantFormate).append("\n");

            JOptionPane.showMessageDialog(null,
                    message.toString(),
                    titre,
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    public static void afficherSucces(String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null,
                    message,
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    public static void afficherErreur(String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null,
                    message,
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        });
    }

    private static String formaterMontant(Double montant) {
        if (montant == null) return "0.00 MAD";
        NumberFormat formatter = NumberFormat.getInstance(new Locale("fr", "MA"));
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter.format(montant) + " MAD";
    }
}
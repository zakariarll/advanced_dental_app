package ma.dentalTech.mvc.ui.modules.caisse;

import ma.dentalTech.mvc.dto.caisse.RevenueDTO;

import javax.swing.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class RevenueView {

    public static void afficherListeRevenues(List<RevenueDTO> revenues, String titre) {
        SwingUtilities.invokeLater(() -> {
            if (revenues == null || revenues.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Aucun revenu trouvé",
                        titre,
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder message = new StringBuilder();
            message.append(titre).append("\n");
            message.append("=".repeat(70)).append("\n\n");

            for (RevenueDTO revenue : revenues) {
                message.append("ID: ").append(revenue.getIdRevenue()).append("\n");
                message.append("Titre: ").append(revenue.getTitre()).append("\n");
                if (revenue.getDescription() != null && !revenue.getDescription().isEmpty()) {
                    message.append("Description: ").append(revenue.getDescription()).append("\n");
                }
                message.append("Montant: ").append(revenue.getMontantFormate()).append("\n");
                message.append("Date: ").append(revenue.getDateFormatee()).append("\n");
                message.append("Cabinet: ").append(revenue.getIdCabinet()).append("\n");
                message.append("-".repeat(70)).append("\n");
            }

            JTextArea textArea = new JTextArea(message.toString());
            textArea.setEditable(false);
            textArea.setCaretPosition(0);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(700, 500));

            JOptionPane.showMessageDialog(null,
                    scrollPane,
                    titre + " (" + revenues.size() + " revenu(s))",
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
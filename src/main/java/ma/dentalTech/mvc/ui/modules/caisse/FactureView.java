package ma.dentalTech.mvc.ui.modules.caisse;

import ma.dentalTech.mvc.dto.caisse.FactureDTO;

import javax.swing.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class FactureView {

    public static void afficherListeFactures(List<FactureDTO> factures, String titre) {
        SwingUtilities.invokeLater(() -> {
            if (factures == null || factures.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Aucune facture trouvée",
                        titre,
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder message = new StringBuilder();
            message.append(titre).append("\n");
            message.append("=".repeat(80)).append("\n\n");

            for (FactureDTO facture : factures) {
                message.append("Facture #").append(facture.getIdFacture()).append("\n");
                message.append("Consultation: ").append(facture.getIdConsultation()).append("\n");
                if (facture.getNomPatient() != null) {
                    message.append("Patient: ").append(facture.getNomPatient())
                            .append(" ").append(facture.getPrenomPatient()).append("\n");
                }
                message.append("Total facture: ").append(facture.getTotaleFactureFormate()).append("\n");
                message.append("Total payé: ").append(facture.getTotalePayeFormate()).append("\n");
                message.append("Reste: ").append(facture.getResteFormate()).append("\n");
                message.append("Statut: ").append(facture.getStatut()).append("\n");
                message.append("Pourcentage payé: ").append(String.format("%.2f%%", facture.getPourcentagePaye())).append("\n");
                message.append("Date: ").append(facture.getDateFactureFormatee()).append("\n");
                message.append("-".repeat(80)).append("\n");
            }

            JTextArea textArea = new JTextArea(message.toString());
            textArea.setEditable(false);
            textArea.setCaretPosition(0);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(800, 500));

            JOptionPane.showMessageDialog(null,
                    scrollPane,
                    titre + " (" + factures.size() + " facture(s))",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    public static void afficherDetailFacture(FactureDTO facture) {
        SwingUtilities.invokeLater(() -> {
            if (facture == null) {
                JOptionPane.showMessageDialog(null,
                        "Facture introuvable",
                        "Détail d'une facture",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            StringBuilder message = new StringBuilder();
            message.append("Détails de la facture #").append(facture.getIdFacture()).append("\n");
            message.append("=".repeat(60)).append("\n\n");
            message.append("Consultation: ").append(facture.getIdConsultation()).append("\n");
            if (facture.getNomPatient() != null) {
                message.append("Patient: ").append(facture.getNomPatient())
                        .append(" ").append(facture.getPrenomPatient()).append("\n");
            }
            message.append("Total facture: ").append(facture.getTotaleFactureFormate()).append("\n");
            message.append("Total payé: ").append(facture.getTotalePayeFormate()).append("\n");
            message.append("Reste à payer: ").append(facture.getResteFormate()).append("\n");
            message.append("Statut: ").append(facture.getStatut()).append("\n");
            message.append("Pourcentage payé: ").append(String.format("%.2f%%", facture.getPourcentagePaye())).append("\n");
            message.append("Date d'émission: ").append(facture.getDateFactureFormatee()).append("\n");

            JOptionPane.showMessageDialog(null,
                    message.toString(),
                    "Détail d'une facture",
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
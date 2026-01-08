package ma.dentalTech.mvc.ui.modules.actes;

import ma.dentalTech.mvc.dto.actes.ActeDTO;
import ma.dentalTech.mvc.dto.actes.ActeInterventionDTO;

import javax.swing.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ActeInterventionView {

    public static void afficherActesIntervention(List<ActeDTO> actes, Double montantTotal, String titre) {
        SwingUtilities.invokeLater(() -> {
            if (actes == null || actes.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Aucun acte trouvé pour cette intervention",
                        titre,
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder message = new StringBuilder();
            message.append(titre).append("\n");
            message.append("=".repeat(50)).append("\n\n");

            for (int i = 0; i < actes.size(); i++) {
                ActeDTO acte = actes.get(i);
                message.append((i + 1)).append(". ");
                message.append(acte.getLibelle()).append("\n");
                message.append("   Catégorie: ").append(acte.getCategorie()).append("\n");
                message.append("   Prix: ").append(acte.getPrixFormate()).append("\n");
                message.append("-".repeat(50)).append("\n");
            }

            message.append("\nMontant total: ").append(formaterPrix(montantTotal)).append("\n");

            JTextArea textArea = new JTextArea(message.toString());
            textArea.setEditable(false);
            textArea.setCaretPosition(0);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(500, 400));

            JOptionPane.showMessageDialog(null,
                    scrollPane,
                    titre + " (" + actes.size() + " acte(s))",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    public static void afficherInterventionsActe(List<ActeInterventionDTO> interventions, int nbUtilisations, String titre) {
        SwingUtilities.invokeLater(() -> {
            if (interventions == null || interventions.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Cet acte n'a pas encore été utilisé dans des interventions",
                        titre,
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder message = new StringBuilder();
            message.append(titre).append("\n");
            message.append("=".repeat(50)).append("\n\n");
            message.append("Nombre total d'utilisations: ").append(nbUtilisations).append("\n\n");

            for (int i = 0; i < interventions.size(); i++) {
                ActeInterventionDTO ai = interventions.get(i);
                message.append((i + 1)).append(". Intervention #").append(ai.getIdIM()).append("\n");
                message.append("   Acte: ").append(ai.getLibelleActe()).append("\n");
                message.append("   Catégorie: ").append(ai.getCategorieActe()).append("\n");
                message.append("   Prix: ").append(ai.getPrixFormate()).append("\n");
                message.append("-".repeat(50)).append("\n");
            }

            JTextArea textArea = new JTextArea(message.toString());
            textArea.setEditable(false);
            textArea.setCaretPosition(0);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(500, 400));

            JOptionPane.showMessageDialog(null,
                    scrollPane,
                    titre + " (" + interventions.size() + " intervention(s))",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    public static void afficherMontantTotal(Double montant, String montantFormate) {
        SwingUtilities.invokeLater(() -> {
            StringBuilder message = new StringBuilder();
            message.append("Montant total de l'intervention\n");
            message.append("=".repeat(40)).append("\n\n");
            message.append("Montant: ").append(montantFormate).append("\n");

            JOptionPane.showMessageDialog(null,
                    message.toString(),
                    "Montant total",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    public static void afficherStatistiquesActe(ActeDTO acte, int nbUtilisations) {
        SwingUtilities.invokeLater(() -> {
            StringBuilder message = new StringBuilder();
            message.append("Statistiques de l'acte\n");
            message.append("=".repeat(40)).append("\n\n");
            message.append("Acte: ").append(acte.getLibelle()).append("\n");
            message.append("Code: ").append(acte.getCode()).append("\n");
            message.append("Catégorie: ").append(acte.getCategorie()).append("\n");
            message.append("Prix unitaire: ").append(acte.getPrixFormate()).append("\n\n");
            message.append("Nombre d'utilisations: ").append(nbUtilisations).append("\n");
            message.append("Revenu total généré: ").append(formaterPrix(acte.getPrixDeBase() * nbUtilisations)).append("\n");

            JOptionPane.showMessageDialog(null,
                    message.toString(),
                    "Statistiques de l'acte",
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

    private static String formaterPrix(Double prix) {
        if (prix == null) {
            return "0.00 MAD";
        }
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("fr", "MA"));
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(2);
        return prix + " MAD";
    }
}
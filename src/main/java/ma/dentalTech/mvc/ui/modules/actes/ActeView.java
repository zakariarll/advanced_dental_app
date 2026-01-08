package ma.dentalTech.mvc.ui.modules.actes;

import ma.dentalTech.mvc.dto.actes.ActeDTO;

import javax.swing.*;
import java.util.List;

public class ActeView {

    public static void afficherListeActes(List<ActeDTO> actes, String titre) {
        SwingUtilities.invokeLater(() -> {
            if (actes == null || actes.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Aucun acte trouvé",
                        titre,
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder message = new StringBuilder();
            message.append(titre).append("\n");
            message.append("=".repeat(50)).append("\n\n");

            for (ActeDTO acte : actes) {
                message.append("Code: ").append(acte.getCode()).append("\n");
                message.append("Libellé: ").append(acte.getLibelle()).append("\n");
                message.append("Catégorie: ").append(acte.getCategorie()).append("\n");
                message.append("Prix: ").append(acte.getPrixFormate()).append("\n");
                if (acte.getDescription() != null && !acte.getDescription().isEmpty()) {
                    message.append("Description: ").append(acte.getDescription()).append("\n");
                }
                message.append("-".repeat(50)).append("\n");
            }

            JTextArea textArea = new JTextArea(message.toString());
            textArea.setEditable(false);
            textArea.setCaretPosition(0);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(500, 400));

            JOptionPane.showMessageDialog(null,
                    scrollPane,
                    titre + " (" + actes.size() + " résultat(s))",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    public static void afficherDetailActe(ActeDTO acte) {
        SwingUtilities.invokeLater(() -> {
            if (acte == null) {
                JOptionPane.showMessageDialog(null,
                        "Acte introuvable",
                        "Détail d'un acte",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            StringBuilder message = new StringBuilder();
            message.append("Détails de l'acte\n");
            message.append("=".repeat(40)).append("\n\n");
            message.append("ID: ").append(acte.getIdActe()).append("\n");
            message.append("Code: ").append(acte.getCode()).append("\n");
            message.append("Libellé: ").append(acte.getLibelle()).append("\n");
            message.append("Catégorie: ").append(acte.getCategorie()).append("\n");
            message.append("Prix de base: ").append(acte.getPrixFormate()).append("\n");
            if (acte.getDescription() != null && !acte.getDescription().isEmpty()) {
                message.append("Description: ").append(acte.getDescription()).append("\n");
            }

            JOptionPane.showMessageDialog(null,
                    message.toString(),
                    "Détail d'un acte",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    public static void afficherCategories(List<String> categories) {
        SwingUtilities.invokeLater(() -> {
            if (categories == null || categories.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Aucune catégorie trouvée",
                        "Catégories d'actes",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder message = new StringBuilder();
            message.append("Catégories disponibles\n");
            message.append("=".repeat(40)).append("\n\n");

            for (int i = 0; i < categories.size(); i++) {
                message.append((i + 1)).append(". ").append(categories.get(i)).append("\n");
            }

            JOptionPane.showMessageDialog(null,
                    message.toString(),
                    "Catégories d'actes (" + categories.size() + ")",
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
}
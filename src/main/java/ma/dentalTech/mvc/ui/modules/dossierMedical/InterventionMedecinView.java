package ma.dentalTech.mvc.ui.modules.dossierMedical;

import ma.dentalTech.mvc.dto.dossierMedical.InterventionMedecinDTO;

import javax.swing.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class InterventionMedecinView {

    public static void afficherListeInterventions(List<InterventionMedecinDTO> interventions, String titre) {
        SwingUtilities.invokeLater(() -> {
            if (interventions == null || interventions.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Aucune intervention trouvée",
                        titre,
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder message = new StringBuilder();
            message.append(titre).append("\n");
            message.append("=".repeat(60)).append("\n\n");

            for (InterventionMedecinDTO intervention : interventions) {
                message.append("ID: ").append(intervention.getIdIM()).append("\n");
                message.append("Consultation: ").append(intervention.getIdConsultation()).append("\n");
                message.append("Médecin: ").append(intervention.getIdMedecin());
                if (intervention.getNomMedecin() != null) {
                    message.append(" - ").append(intervention.getNomMedecin());
                }
                message.append("\n");
                message.append("Prix: ").append(intervention.getPrixFormate()).append("\n");
                message.append("-".repeat(60)).append("\n");
            }

            JTextArea textArea = new JTextArea(message.toString());
            textArea.setEditable(false);
            textArea.setCaretPosition(0);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(600, 400));

            JOptionPane.showMessageDialog(null,
                    scrollPane,
                    titre + " (" + interventions.size() + " intervention(s))",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    public static void afficherDetailIntervention(InterventionMedecinDTO intervention) {
        SwingUtilities.invokeLater(() -> {
            if (intervention == null) {
                JOptionPane.showMessageDialog(null,
                        "Intervention introuvable",
                        "Détail d'une intervention",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            StringBuilder message = new StringBuilder();
            message.append("Détails de l'intervention\n");
            message.append("=".repeat(50)).append("\n\n");
            message.append("ID: ").append(intervention.getIdIM()).append("\n");
            message.append("Consultation ID: ").append(intervention.getIdConsultation()).append("\n");
            message.append("Médecin ID: ").append(intervention.getIdMedecin()).append("\n");
            if (intervention.getNomMedecin() != null) {
                message.append("Nom du médecin: ").append(intervention.getNomMedecin()).append("\n");
            }
            message.append("Prix facturé au patient: ").append(intervention.getPrixFormate()).append("\n");

            JOptionPane.showMessageDialog(null,
                    message.toString(),
                    "Détail d'une intervention",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    public static void afficherMontantTotal(Double montant, Long idConsultation) {
        SwingUtilities.invokeLater(() -> {
            String montantFormate = formaterPrix(montant);
            StringBuilder message = new StringBuilder();
            message.append("Montant total de la consultation #").append(idConsultation).append("\n\n");
            message.append("Total: ").append(montantFormate).append("\n");

            JOptionPane.showMessageDialog(null,
                    message.toString(),
                    "Montant Total",
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
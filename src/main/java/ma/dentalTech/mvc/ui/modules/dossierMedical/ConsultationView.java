package ma.dentalTech.mvc.ui.modules.dossierMedical;

import ma.dentalTech.mvc.dto.dossierMedical.ConsultationDTO;

import javax.swing.*;
import java.util.List;

public class ConsultationView {

    public static void afficherListeConsultations(List<ConsultationDTO> consultations, String titre) {
        SwingUtilities.invokeLater(() -> {
            if (consultations == null || consultations.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Aucune consultation trouvée",
                        titre,
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder message = new StringBuilder();
            message.append(titre).append("\n");
            message.append("=".repeat(60)).append("\n\n");

            for (ConsultationDTO consultation : consultations) {
                message.append("ID: ").append(consultation.getIdConsultation()).append("\n");
                message.append("Date: ").append(consultation.getDateFormatee()).append("\n");
                message.append("Statut: ").append(consultation.getStatutAffichage()).append("\n");
                message.append("RDV ID: ").append(consultation.getIdRDV()).append("\n");
                if (consultation.getObservationMedecin() != null && !consultation.getObservationMedecin().isEmpty()) {
                    message.append("Observation: ").append(consultation.getObservationMedecin()).append("\n");
                }
                message.append("-".repeat(60)).append("\n");
            }

            JTextArea textArea = new JTextArea(message.toString());
            textArea.setEditable(false);
            textArea.setCaretPosition(0);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(600, 400));

            JOptionPane.showMessageDialog(null,
                    scrollPane,
                    titre + " (" + consultations.size() + " résultat(s))",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    public static void afficherDetailConsultation(ConsultationDTO consultation) {
        SwingUtilities.invokeLater(() -> {
            if (consultation == null) {
                JOptionPane.showMessageDialog(null,
                        "Consultation introuvable",
                        "Détail d'une consultation",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            StringBuilder message = new StringBuilder();
            message.append("Détails de la consultation\n");
            message.append("=".repeat(50)).append("\n\n");
            message.append("ID: ").append(consultation.getIdConsultation()).append("\n");
            message.append("Date: ").append(consultation.getDateFormatee()).append("\n");
            message.append("Statut: ").append(consultation.getStatutAffichage()).append("\n");
            message.append("RDV associé: ").append(consultation.getIdRDV()).append("\n");

            if (consultation.getObservationMedecin() != null && !consultation.getObservationMedecin().isEmpty()) {
                message.append("\nObservation du médecin:\n");
                message.append(consultation.getObservationMedecin()).append("\n");
            }

            JTextArea textArea = new JTextArea(message.toString());
            textArea.setEditable(false);
            textArea.setCaretPosition(0);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(500, 300));

            JOptionPane.showMessageDialog(null,
                    scrollPane,
                    "Détail d'une consultation",
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
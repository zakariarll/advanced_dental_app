/**package ma.dentalTech.mvc.ui.modules.dossierMedical;

import ma.dentalTech.mvc.dto.dossierMedical.RDVDTO;

import javax.swing.*;
import java.util.List;

public class RDVView {

    public static void afficherListeRDV(List<RDVDTO> rdvs, String titre) {
        SwingUtilities.invokeLater(() -> {
            if (rdvs == null || rdvs.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Aucun rendez-vous trouvé",
                        titre,
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder message = new StringBuilder();
            message.append(titre).append("\n");
            message.append("=".repeat(70)).append("\n\n");

            for (RDVDTO rdv : rdvs) {
                message.append("RDV #").append(rdv.getIdRDV()).append("\n");
                message.append("Date: ").append(rdv.getDateFormatee());
                message.append(" à ").append(rdv.getHeureFormatee()).append("\n");
                message.append("Statut: ").append(rdv.getStatutAffichage()).append("\n");
                message.append("Motif: ").append(rdv.getMotif()).append("\n");

                if (rdv.getNomPatient() != null) {
                    message.append("Patient: ").append(rdv.getNomPatient()).append("\n");
                } else {
                    message.append("Patient ID: ").append(rdv.getIdPatient()).append("\n");
                }

                if (rdv.getNomMedecin() != null) {
                    message.append("Médecin: ").append(rdv.getNomMedecin()).append("\n");
                } else {
                    message.append("Médecin ID: ").append(rdv.getIdMedecin()).append("\n");
                }

                if (rdv.getNoteMedecin() != null && !rdv.getNoteMedecin().isEmpty()) {
                    message.append("Note: ").append(rdv.getNoteMedecin()).append("\n");
                }
                message.append("-".repeat(70)).append("\n");
            }

            JTextArea textArea = new JTextArea(message.toString());
            textArea.setEditable(false);
            textArea.setCaretPosition(0);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(700, 500));

            JOptionPane.showMessageDialog(null,
                    scrollPane,
                    titre + " (" + rdvs.size() + " RDV)",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    public static void afficherDetailRDV(RDVDTO rdv) {
        SwingUtilities.invokeLater(() -> {
            if (rdv == null) {
                JOptionPane.showMessageDialog(null,
                        "Rendez-vous introuvable",
                        "Détail d'un RDV",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            StringBuilder message = new StringBuilder();
            message.append("DÉTAILS DU RENDEZ-VOUS\n");
            message.append("=".repeat(50)).append("\n\n");
            message.append("ID: ").append(rdv.getIdRDV()).append("\n");
            message.append("Date: ").append(rdv.getDateFormatee()).append("\n");
            message.append("Heure: ").append(rdv.getHeureFormatee()).append("\n");
            message.append("Statut: ").append(rdv.getStatutAffichage()).append("\n\n");

            message.append("MOTIF\n");
            message.append("-".repeat(50)).append("\n");
            message.append(rdv.getMotif()).append("\n\n");

            message.append("INTERVENANTS\n");
            message.append("-".repeat(50)).append("\n");

            if (rdv.getNomPatient() != null) {
                message.append("Patient: ").append(rdv.getNomPatient()).append("\n");
            } else {
                message.append("Patient ID: ").append(rdv.getIdPatient()).append("\n");
            }

            if (rdv.getNomMedecin() != null) {
                message.append("Médecin: ").append(rdv.getNomMedecin()).append("\n");
            } else {
                message.append("Médecin ID: ").append(rdv.getIdMedecin()).append("\n");
            }

            if (rdv.getNomSecretaire() != null) {
                message.append("Secrétaire: ").append(rdv.getNomSecretaire()).append("\n");
            } else if (rdv.getIdSecretaire() != null) {
                message.append("Secrétaire ID: ").append(rdv.getIdSecretaire()).append("\n");
            }

            if (rdv.getNoteMedecin() != null && !rdv.getNoteMedecin().isEmpty()) {
                message.append("\nNOTE DU MÉDECIN\n");
                message.append("-".repeat(50)).append("\n");
                message.append(rdv.getNoteMedecin()).append("\n");
            }

            JTextArea textArea = new JTextArea(message.toString());
            textArea.setEditable(false);
            textArea.setCaretPosition(0);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(500, 400));

            JOptionPane.showMessageDialog(null,
                    scrollPane,
                    "Détail du RDV #" + rdv.getIdRDV(),
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

    public static void afficherAlerte(String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null,
                    message,
                    "⚠️ Alerte",
                    JOptionPane.WARNING_MESSAGE);
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
}**/
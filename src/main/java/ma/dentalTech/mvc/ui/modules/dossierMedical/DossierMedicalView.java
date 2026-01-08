package ma.dentalTech.mvc.ui.modules.dossierMedical;

import ma.dentalTech.mvc.dto.patient.AntecedentDTO;
import ma.dentalTech.mvc.dto.dossierMedical.ConsultationDTO;
import ma.dentalTech.mvc.dto.dossierMedical.DossierMedicalDTO;
import ma.dentalTech.mvc.dto.dossierMedical.RDVDTO;

import javax.swing.*;

public class DossierMedicalView {

    public static void afficherDossierComplet(DossierMedicalDTO dossier) {
        SwingUtilities.invokeLater(() -> {
            if (dossier == null) {
                JOptionPane.showMessageDialog(null,
                        "Dossier médical introuvable",
                        "Erreur",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            StringBuilder message = new StringBuilder();
            message.append("DOSSIER MÉDICAL COMPLET\n");
            message.append("=".repeat(70)).append("\n\n");

            if (dossier.getPatient() != null) {
                message.append("PATIENT\n");
                message.append("-".repeat(70)).append("\n");
                message.append("Nom: ").append(dossier.getPatient().getNom()).append(" ");
                message.append(dossier.getPatient().getPrenom()).append("\n");
                message.append("Date de naissance: ").append(dossier.getPatient().getDateDeNaissance()).append("\n");
                message.append("Sexe: ").append(dossier.getPatient().getSexe()).append("\n");
                message.append("Téléphone: ").append(dossier.getPatient().getTelephone()).append("\n");
                message.append("Assurance: ").append(dossier.getPatient().getAssurance()).append("\n\n");
            }

            message.append("STATUT MÉDICAL\n");
            message.append("-".repeat(70)).append("\n");
            message.append("Risques critiques: ").append(dossier.isADesRisquesCritiques() ? "OUI ⚠️" : "NON ✓").append("\n");
            message.append("Nombre de RDV: ").append(dossier.getNombreRDV()).append("\n");
            message.append("Nombre de consultations: ").append(dossier.getNombreConsultations()).append("\n\n");

            if (dossier.getAntecedents() != null && !dossier.getAntecedents().isEmpty()) {
                message.append("ANTÉCÉDENTS (").append(dossier.getAntecedents().size()).append(")\n");
                message.append("-".repeat(70)).append("\n");
                for (AntecedentDTO ant : dossier.getAntecedents()) {
                    message.append("• ").append(ant.nom());
                    message.append(" [").append(ant.categorie()).append("]");
                    message.append(" - Risque: ").append(ant.niveauRisque()).append("\n");
                }
                message.append("\n");
            }

            if (dossier.getRdvs() != null && !dossier.getRdvs().isEmpty()) {
                message.append("RENDEZ-VOUS (").append(dossier.getRdvs().size()).append(")\n");
                message.append("-".repeat(70)).append("\n");
                for (RDVDTO rdv : dossier.getRdvs()) {
                    message.append("• ").append(rdv.getDateFormatee()).append(" à ").append(rdv.getHeureFormatee());
                    message.append(" - ").append(rdv.getStatutAffichage()).append("\n");
                    message.append("  Motif: ").append(rdv.getMotif()).append("\n");
                }
                message.append("\n");
            }

            if (dossier.getConsultations() != null && !dossier.getConsultations().isEmpty()) {
                message.append("CONSULTATIONS (").append(dossier.getConsultations().size()).append(")\n");
                message.append("-".repeat(70)).append("\n");
                for (ConsultationDTO cons : dossier.getConsultations()) {
                    message.append("• ").append(cons.getDateFormatee());
                    message.append(" - ").append(cons.getStatutAffichage()).append("\n");
                    if (cons.getObservationMedecin() != null) {
                        message.append("  Observation: ").append(cons.getObservationMedecin()).append("\n");
                    }
                }
            }

            JTextArea textArea = new JTextArea(message.toString());
            textArea.setEditable(false);
            textArea.setCaretPosition(0);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(700, 500));

            JOptionPane.showMessageDialog(null,
                    scrollPane,
                    "Dossier Médical - " + (dossier.getPatient() != null ?
                            dossier.getPatient().getNom() + " " + dossier.getPatient().getPrenom() : ""),
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    public static void afficherStatistiques(int nombreRDV, int nombreConsultations, boolean risquesCritiques) {
        SwingUtilities.invokeLater(() -> {
            StringBuilder message = new StringBuilder();
            message.append("STATISTIQUES DU PATIENT\n");
            message.append("=".repeat(50)).append("\n\n");
            message.append("Nombre de rendez-vous: ").append(nombreRDV).append("\n");
            message.append("Nombre de consultations: ").append(nombreConsultations).append("\n");
            message.append("Risques critiques: ").append(risquesCritiques ? "OUI ⚠️" : "NON ✓").append("\n");

            JOptionPane.showMessageDialog(null,
                    message.toString(),
                    "Statistiques Patient",
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
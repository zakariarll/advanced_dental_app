package ma.dentalTech.mvc.ui.modules.caisse;

import ma.dentalTech.entities.caisse.Statistique;
import ma.dentalTech.mvc.dto.caisse.TableauDeBordDTO;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TableauDeBordView {

    public static void afficherTableauDeBord(TableauDeBordDTO tableau) {
        SwingUtilities.invokeLater(() -> {
            if (tableau == null) {
                JOptionPane.showMessageDialog(null,
                        "Aucune donnée disponible",
                        "Tableau de bord",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            JFrame frame = new JFrame("Tableau de Bord Financier");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(900, 700);
            frame.setLocationRelativeTo(null);

            JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel titre = new JLabel("Tableau de Bord Financier", SwingConstants.CENTER);
            titre.setFont(new Font("Arial", Font.BOLD, 24));
            titre.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
            mainPanel.add(titre, BorderLayout.NORTH);

            JPanel contentPanel = new JPanel(new GridLayout(0, 2, 15, 15));

            contentPanel.add(creerPanneauIndicateur("Chiffre d'affaires du jour",
                    tableau.getChiffreAffairesJourFormate(), new Color(52, 152, 219)));
            contentPanel.add(creerPanneauIndicateur("Chiffre d'affaires du mois",
                    tableau.getChiffreAffairesMoisFormate(), new Color(46, 204, 113)));
            contentPanel.add(creerPanneauIndicateur("Chiffre d'affaires de l'année",
                    tableau.getChiffreAffairesAnneeFormate(), new Color(155, 89, 182)));
            contentPanel.add(creerPanneauIndicateur("Total encaissements",
                    tableau.getTotalEncaissementsFormate(), new Color(26, 188, 156)));
            contentPanel.add(creerPanneauIndicateur("Total décaissements",
                    tableau.getTotalDecaissementsFormate(), new Color(231, 76, 60)));
            contentPanel.add(creerPanneauIndicateur("Bénéfice net",
                    tableau.getBeneficeNetFormate(), new Color(22, 160, 133)));
            contentPanel.add(creerPanneauIndicateur("Total crédit",
                    tableau.getTotalCreditFormate(), new Color(230, 126, 34)));
            contentPanel.add(creerPanneauIndicateur("Taux de recouvrement",
                    tableau.getTauxRecouvrementFormate(), new Color(41, 128, 185)));

            if (tableau.getNombreFacturesImpayees() != null) {
                contentPanel.add(creerPanneauIndicateur("Factures impayées",
                        String.valueOf(tableau.getNombreFacturesImpayees()), new Color(192, 57, 43)));
            }

            JScrollPane scrollPane = new JScrollPane(contentPanel);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            mainPanel.add(scrollPane, BorderLayout.CENTER);

            frame.add(mainPanel);
            frame.setVisible(true);
        });
    }

    private static JPanel creerPanneauIndicateur(String label, String valeur, Color couleur) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(couleur, 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setBackground(Color.WHITE);

        JLabel lblLabel = new JLabel(label, SwingConstants.CENTER);
        lblLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        lblLabel.setForeground(Color.DARK_GRAY);

        JLabel lblValeur = new JLabel(valeur, SwingConstants.CENTER);
        lblValeur.setFont(new Font("Arial", Font.BOLD, 20));
        lblValeur.setForeground(couleur);

        panel.add(lblLabel, BorderLayout.NORTH);
        panel.add(lblValeur, BorderLayout.CENTER);

        return panel;
    }

    public static void afficherStatistiques(Map<String, Double> stats, String titre) {
        SwingUtilities.invokeLater(() -> {
            if (stats == null || stats.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Aucune statistique disponible",
                        titre,
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder message = new StringBuilder();
            message.append(titre).append("\n");
            message.append("=".repeat(60)).append("\n\n");

            for (Map.Entry<String, Double> entry : stats.entrySet()) {
                message.append(entry.getKey()).append(": ")
                        .append(formaterMontant(entry.getValue())).append("\n");
            }

            JTextArea textArea = new JTextArea(message.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(600, 400));

            JOptionPane.showMessageDialog(null,
                    scrollPane,
                    titre,
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    public static void afficherRapportFinancier(List<Statistique> rapport, LocalDateTime debut, LocalDateTime fin) {
        SwingUtilities.invokeLater(() -> {
            if (rapport == null || rapport.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Aucune donnée disponible pour cette période",
                        "Rapport Financier",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            StringBuilder message = new StringBuilder();
            message.append("Rapport Financier\n");
            message.append("Période: ").append(debut.format(formatter))
                    .append(" - ").append(fin.format(formatter)).append("\n");
            message.append("=".repeat(70)).append("\n\n");

            for (Statistique stat : rapport) {
                message.append(stat.getNom()).append(" (").append(stat.getCategorie()).append(")\n");
                message.append("Valeur: ").append(formaterMontant(stat.getChiffre())).append("\n");
                message.append("Date: ").append(stat.getDateCalcul()).append("\n");
                message.append("-".repeat(70)).append("\n");
            }

            JTextArea textArea = new JTextArea(message.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(700, 500));

            JOptionPane.showMessageDialog(null,
                    scrollPane,
                    "Rapport Financier (" + rapport.size() + " statistique(s))",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    public static void afficherRepartitionMensuelle(Map<String, Double> repartition, String titre) {
        SwingUtilities.invokeLater(() -> {
            if (repartition == null || repartition.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Aucune donnée disponible",
                        titre,
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder message = new StringBuilder();
            message.append(titre).append("\n");
            message.append("=".repeat(60)).append("\n\n");

            String[] mois = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
                    "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"};

            for (int i = 1; i <= 12; i++) {
                String key = String.valueOf(i);
                Double valeur = repartition.getOrDefault(key, 0.0);
                message.append(mois[i - 1]).append(": ")
                        .append(formaterMontant(valeur)).append("\n");
            }

            JTextArea textArea = new JTextArea(message.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(600, 400));

            JOptionPane.showMessageDialog(null,
                    scrollPane,
                    titre,
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    public static void afficherMontant(Double montant, String titre) {
        SwingUtilities.invokeLater(() -> {
            String montantFormate = formaterMontant(montant);
            JOptionPane.showMessageDialog(null,
                    titre + "\n\nMontant: " + montantFormate,
                    titre,
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    public static void afficherTaux(Double taux, String titre) {
        SwingUtilities.invokeLater(() -> {
            String tauxFormate = taux != null ? String.format("%.2f%%", taux) : "0.00%";
            JOptionPane.showMessageDialog(null,
                    titre + "\n\nTaux: " + tauxFormate,
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
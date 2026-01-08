package ma.dentalTech.mvc.ui.modules.RDVPatient;

import ma.dentalTech.conf.ApplicationContext;
import ma.dentalTech.entities.dossierMedical.RDV;
import ma.dentalTech.mvc.controllers.modules.RDVPatient.api.IGestionRDVController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RDVView extends JPanel {

    // Composants graphiques
    private JTable tableRDV;
    private DefaultTableModel tableModel;
    private JTextField txtDateFiltre;
    private JButton btnRechercher, btnNouveau, btnConfirmer, btnAnnuler;

    // Contrôleur
    private final IGestionRDVController controller;

    public RDVView() {
        // 1. Récupération du contrôleur via l'ApplicationContext
        this.controller = ApplicationContext.getBean(IGestionRDVController.class);

        // 2. Initialisation de l'interface
        initUI();
        initListeners();

        // 3. Chargement initial (Date d'aujourd'hui)
        chargerDonnees(LocalDate.now());
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // --- HAUT : Barre de filtres ---
        JPanel panelNord = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelNord.setBorder(BorderFactory.createTitledBorder("Agenda / Planning"));

        JLabel lblDate = new JLabel("Date (AAAA-MM-JJ) :");
        txtDateFiltre = new JTextField(LocalDate.now().toString(), 10);
        btnRechercher = new JButton("Voir Planning");
        btnNouveau = new JButton("✚ Nouveau RDV");
        btnNouveau.setBackground(new Color(144, 238, 144)); // Vert clair

        panelNord.add(lblDate);
        panelNord.add(txtDateFiltre);
        panelNord.add(btnRechercher);
        panelNord.add(Box.createHorizontalStrut(20)); // Espace
        panelNord.add(btnNouveau);

        add(panelNord, BorderLayout.NORTH);

        // --- CENTRE : Tableau des RDV ---
        String[] colonnes = {"ID", "Date", "Heure", "Patient (ID)", "Médecin (ID)", "Motif", "Statut"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tableau non éditable directement
            }
        };
        tableRDV = new JTable(tableModel);
        tableRDV.setRowHeight(25);
        tableRDV.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tableRDV);

        add(scrollPane, BorderLayout.CENTER);

        // --- BAS : Actions sur la sélection ---
        JPanel panelSud = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnConfirmer = new JButton("✔ Confirmer");
        btnAnnuler = new JButton("✖ Annuler");
        btnAnnuler.setForeground(Color.RED);

        panelSud.add(btnConfirmer);
        panelSud.add(btnAnnuler);

        add(panelSud, BorderLayout.SOUTH);
    }

    private void initListeners() {
        // Action : Rechercher
        btnRechercher.addActionListener(e -> {
            try {
                LocalDate date = LocalDate.parse(txtDateFiltre.getText());
                chargerDonnees(date);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Format de date invalide (AAAA-MM-JJ)");
            }
        });

        // Action : Nouveau RDV (Ouvre un formulaire popup)
        btnNouveau.addActionListener(e -> afficherFormulaireAjout());

        // Action : Confirmer
        btnConfirmer.addActionListener(e -> {
            Long idRDV = getSelectedId();
            if (idRDV != null) {
                controller.confirmerRDV(idRDV);
                JOptionPane.showMessageDialog(this, "RDV confirmé avec succès !");
                rafraichirActuel();
            }
        });

        // Action : Annuler
        btnAnnuler.addActionListener(e -> {
            Long idRDV = getSelectedId();
            if (idRDV != null) {
                String motif = JOptionPane.showInputDialog(this, "Motif de l'annulation :");
                if (motif != null && !motif.trim().isEmpty()) {
                    controller.annulerRDV(idRDV, motif);
                    JOptionPane.showMessageDialog(this, "RDV annulé.");
                    rafraichirActuel();
                }
            }
        });
    }

    /**
     * Charge les données depuis le contrôleur dans le tableau
     */
    private void chargerDonnees(LocalDate date) {
        // Nettoyer le tableau
        tableModel.setRowCount(0);

        // Appel au contrôleur (supposons ID Medecin 1 pour l'exemple, ou un filtre global)
        List<RDV> planning = controller.chargerPlanning(1L, date);

        for (RDV rdv : planning) {
            Object[] row = {
                    rdv.getIdRDV(),
                    rdv.getDate(),
                    rdv.getHeure(),
                    rdv.getIdPatient(),
                    rdv.getIdMedecin(),
                    rdv.getMotif(),
                    rdv.getStatut()
            };
            tableModel.addRow(row);
        }
    }

    private void rafraichirActuel() {
        try {
            chargerDonnees(LocalDate.parse(txtDateFiltre.getText()));
        } catch (Exception e) {
            chargerDonnees(LocalDate.now());
        }
    }

    private Long getSelectedId() {
        int row = tableRDV.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un RDV dans la liste.");
            return null;
        }
        return (Long) tableModel.getValueAt(row, 0);
    }

    /**
     * Affiche un formulaire simple pour créer un RDV
     */
    private void afficherFormulaireAjout() {
        JTextField txtPatientId = new JTextField();
        JTextField txtMedecinId = new JTextField("1"); // Par défaut
        JTextField txtDate = new JTextField(txtDateFiltre.getText());
        JTextField txtHeure = new JTextField("09:00");
        JTextField txtMotif = new JTextField("Consultation");

        Object[] message = {
                "ID Patient:", txtPatientId,
                "ID Médecin:", txtMedecinId,
                "Date (AAAA-MM-JJ):", txtDate,
                "Heure (HH:mm):", txtHeure,
                "Motif:", txtMotif
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Nouveau RDV", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                Long pId = Long.parseLong(txtPatientId.getText());
                Long mId = Long.parseLong(txtMedecinId.getText());
                LocalDate d = LocalDate.parse(txtDate.getText());
                LocalTime h = LocalTime.parse(txtHeure.getText());
                String motif = txtMotif.getText();

                // Appel au contrôleur
                controller.planifierRDV(pId, mId, d, h, motif);

                JOptionPane.showMessageDialog(this, "RDV Planifié !");
                rafraichirActuel();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur de saisie : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
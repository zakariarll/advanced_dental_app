package ma.dentalTech.repository;

import ma.dentalTech.conf.SessionFactory;
import java.sql.Connection;
import java.sql.Statement;

public class DbTestUtils {

    /**
     * Vide les tables dans l'ordre inverse des dépendances (Foreign Keys)
     * pour éviter les erreurs de contraintes lors des tests.
     */
    public static void cleanDatabase() {
        /**
        String[] tables = {
                "Facture", "Revenue", "Charge", "Statistique",
                "Prescription", "Ordonnance", "Medicament", // Noms corrigés
                "Certificat", "Acte_Intervention", "InterventionMédecin", "Consultation", "RDV",
                "Antecedents", "Patient", // Noms corrigés
                "Médecin", "Secretaire", "Staff", "Admin",
                "Utilisateur", "Role", "Cabinet_Medical"
        };

        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement s = c.createStatement()) {

            // Désactiver temporairement les contraintes FK (spécifique PostgreSQL)
            // Si MySQL : "SET FOREIGN_KEY_CHECKS = 0;"
            // Pour PostgreSQL on truncate avec CASCADE
            for (String table : tables) {
                try {
                    s.executeUpdate("TRUNCATE TABLE " + table + " CASCADE");
                } catch (Exception e) {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }**/
    }
}
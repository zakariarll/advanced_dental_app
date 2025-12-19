package ma.dentalTech.repository.modules.dashboard.impl.mySQL;

import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.modules.dashboard.api.DashboardRepository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class DashboardRepositoryImpl implements DashboardRepository {

    @Override
    public long getNombrePatients() {
        String sql = "SELECT COUNT(*) FROM Patient";
        try (Connection c = SessionFactory.getInstance().getConnection();
             ResultSet rs = c.createStatement().executeQuery(sql)) {
            if(rs.next()) return rs.getLong(1);
        } catch (SQLException e) { throw new RuntimeException(e); }
        return 0;
    }

    @Override
    public long getRDVToday() {
        String sql = "SELECT COUNT(*) FROM RDV WHERE date = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(LocalDate.now()));
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) return rs.getLong(1);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return 0;
    }

    @Override
    public double getChiffreAffaireMois() {
        // Somme des factures payées ce mois-ci
        String sql = "SELECT SUM(totalePayé) FROM Facture WHERE EXTRACT(MONTH FROM dateFacture) = ? AND EXTRACT(YEAR FROM dateFacture) = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            LocalDate now = LocalDate.now();
            ps.setInt(1, now.getMonthValue());
            ps.setInt(2, now.getYear());
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) return rs.getDouble(1);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return 0.0;
    }

    @Override
    public long getAlertesStock() {
        // Exemple : Médicaments dont le stock est bas (si on gérait le stock)
        // Ici on retourne 0 ou une requête dummy si pas de table stock
        return 0;
    }
}
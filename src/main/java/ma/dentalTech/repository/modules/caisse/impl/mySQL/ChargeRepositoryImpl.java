package ma.dentalTech.repository.modules.caisse.impl.mySQL;

import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.entities.caisse.Charge;
import ma.dentalTech.repository.common.RowMappers;
import ma.dentalTech.repository.modules.caisse.api.ChargeRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChargeRepositoryImpl implements ChargeRepository {

    @Override
    public List<Charge> findAll() {
        String sql = "SELECT * FROM Charge ORDER BY date DESC";
        List<Charge> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(RowMappers.mapCharge(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public Charge findById(Long id) {
        String sql = "SELECT * FROM Charge WHERE idCharge = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapCharge(rs);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    @Override
    public void create(Charge ch) {
        String sql = "INSERT INTO Charge(titre, description, montant, date, idCabinet) VALUES(?,?,?,?,?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, ch.getTitre());
            ps.setString(2, ch.getDescription());
            ps.setDouble(3, ch.getMontant());
            ps.setTimestamp(4, Timestamp.valueOf(ch.getDate()));
            ps.setLong(5, ch.getIdCabinet());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) ch.setIdCharge(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(Charge ch) {
        String sql = "UPDATE Charge SET titre=?, description=?, montant=?, date=?, idCabinet=? WHERE idCharge=?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, ch.getTitre());
            ps.setString(2, ch.getDescription());
            ps.setDouble(3, ch.getMontant());
            ps.setTimestamp(4, Timestamp.valueOf(ch.getDate()));
            ps.setLong(5, ch.getIdCabinet());
            ps.setLong(6, ch.getIdCharge());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(Charge ch) {
        deleteById(ch.getIdCharge());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Charge WHERE idCharge = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Charge> findByCabinetId(Long idCabinet) {
        String sql = "SELECT * FROM Charge WHERE idCabinet = ? ORDER BY date DESC";
        List<Charge> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idCabinet);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(RowMappers.mapCharge(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public List<Charge> findByDateBetween(LocalDateTime debut, LocalDateTime fin) {
        String sql = "SELECT * FROM Charge WHERE date BETWEEN ? AND ? ORDER BY date DESC";
        List<Charge> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(debut));
            ps.setTimestamp(2, Timestamp.valueOf(fin));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(RowMappers.mapCharge(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public List<Charge> findByTitreContaining(String titre) {
        String sql = "SELECT * FROM Charge WHERE titre LIKE ? ORDER BY date DESC";
        List<Charge> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, "%" + titre + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(RowMappers.mapCharge(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public Double calculateTotalCharges(Long idCabinet) {
        String sql = "SELECT COALESCE(SUM(montant), 0) FROM Charge WHERE idCabinet = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idCabinet);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble(1);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return 0.0;
    }

    @Override
    public Double calculateTotalChargesBetween(Long idCabinet, LocalDateTime debut, LocalDateTime fin) {
        String sql = "SELECT COALESCE(SUM(montant), 0) FROM Charge WHERE idCabinet = ? AND date BETWEEN ? AND ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idCabinet);
            ps.setTimestamp(2, Timestamp.valueOf(debut));
            ps.setTimestamp(3, Timestamp.valueOf(fin));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble(1);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return 0.0;
    }
}
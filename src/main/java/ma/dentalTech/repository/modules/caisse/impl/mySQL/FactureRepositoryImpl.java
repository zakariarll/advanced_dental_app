package ma.dentalTech.repository.modules.caisse.impl.mySQL;

import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.entities.caisse.Facture;
import ma.dentalTech.repository.common.RowMappers;
import ma.dentalTech.repository.modules.caisse.api.FactureRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FactureRepositoryImpl implements FactureRepository {

    @Override
    public List<Facture> findAll() {
        String sql = "SELECT * FROM Facture ORDER BY dateFacture DESC";
        List<Facture> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(RowMappers.mapFacture(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public Facture findById(Long id) {
        String sql = "SELECT * FROM Facture WHERE idFacture = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapFacture(rs);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    @Override
    public void create(Facture f) {
        String sql = "INSERT INTO Facture(totaleFacture, totalePayé, reste, statut, dateFacture, idConsultation) VALUES(?,?,?,?,?,?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDouble(1, f.getTotaleFacture());
            ps.setDouble(2, f.getTotalePaye());
            ps.setDouble(3, f.getReste());
            ps.setString(4, f.getStatut());
            ps.setTimestamp(5, Timestamp.valueOf(f.getDateFacture()));
            ps.setLong(6, f.getIdConsultation());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) f.setIdFacture(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(Facture f) {
        String sql = "UPDATE Facture SET totaleFacture=?, totalePayé=?, reste=?, statut=?, dateFacture=?, idConsultation=? WHERE idFacture=?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, f.getTotaleFacture());
            ps.setDouble(2, f.getTotalePaye());
            ps.setDouble(3, f.getReste());
            ps.setString(4, f.getStatut());
            ps.setTimestamp(5, Timestamp.valueOf(f.getDateFacture()));
            ps.setLong(6, f.getIdConsultation());
            ps.setLong(7, f.getIdFacture());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(Facture f) {
        deleteById(f.getIdFacture());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Facture WHERE idFacture = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Optional<Facture> findByConsultationId(Long idConsultation) {
        String sql = "SELECT * FROM Facture WHERE idConsultation = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idConsultation);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(RowMappers.mapFacture(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return Optional.empty();
    }

    @Override
    public List<Facture> findByStatut(String statut) {
        String sql = "SELECT * FROM Facture WHERE statut = ? ORDER BY dateFacture DESC";
        List<Facture> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, statut);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(RowMappers.mapFacture(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public List<Facture> findByDateBetween(LocalDateTime debut, LocalDateTime fin) {
        String sql = "SELECT * FROM Facture WHERE dateFacture BETWEEN ? AND ? ORDER BY dateFacture DESC";
        List<Facture> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(debut));
            ps.setTimestamp(2, Timestamp.valueOf(fin));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(RowMappers.mapFacture(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public List<Facture> findFacturesImpayees() {
        String sql = "SELECT * FROM Facture WHERE reste > 0 ORDER BY dateFacture DESC";
        List<Facture> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(RowMappers.mapFacture(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public List<Facture> findByPatientId(Long idPatient) {
        String sql = """
            SELECT f.* FROM Facture f
            INNER JOIN Consultation c ON f.idConsultation = c.idConsultation
            INNER JOIN RDV r ON c.idRDV = r.idRDV
            WHERE r.idPatient = ?
            ORDER BY f.dateFacture DESC
            """;
        List<Facture> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idPatient);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(RowMappers.mapFacture(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public Double calculateTotalFactures(LocalDateTime debut, LocalDateTime fin) {
        String sql = "SELECT COALESCE(SUM(totaleFacture), 0) FROM Facture WHERE dateFacture BETWEEN ? AND ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(debut));
            ps.setTimestamp(2, Timestamp.valueOf(fin));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble(1);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return 0.0;
    }

    @Override
    public Double calculateTotalPaye(LocalDateTime debut, LocalDateTime fin) {
        String sql = "SELECT COALESCE(SUM(totalePayé), 0) FROM Facture WHERE dateFacture BETWEEN ? AND ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(debut));
            ps.setTimestamp(2, Timestamp.valueOf(fin));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble(1);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return 0.0;
    }

    @Override
    public Double calculateTotalImpaye() {
        String sql = "SELECT COALESCE(SUM(reste), 0) FROM Facture WHERE reste > 0";
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { throw new RuntimeException(e); }
        return 0.0;
    }
}
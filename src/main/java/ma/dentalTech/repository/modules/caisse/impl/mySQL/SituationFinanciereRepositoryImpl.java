package ma.dentalTech.repository.modules.caisse.impl.mySQL;

import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.entities.caisse.SituationFinanciere;
import ma.dentalTech.repository.common.RowMappers;
import ma.dentalTech.repository.modules.caisse.api.SituationFinanciereRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SituationFinanciereRepositoryImpl implements SituationFinanciereRepository {

    @Override
    public List<SituationFinanciere> findAll() {
        String sql = "SELECT * FROM SituationFinanciere";
        List<SituationFinanciere> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(RowMappers.mapSituation(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public SituationFinanciere findById(Long id) {
        String sql = "SELECT * FROM SituationFinanciere WHERE idSF = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapSituation(rs);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    @Override
    public void create(SituationFinanciere sf) {
        String sql = "INSERT INTO SituationFinanciere(totaleDesActes, totalePayé, crédit, statut, enPromo, idFacture) VALUES(?,?,?,?,?,?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDouble(1, sf.getTotaleDesActes());
            ps.setDouble(2, sf.getTotalePaye());
            ps.setDouble(3, sf.getCredit());
            ps.setString(4, sf.getStatut());
            ps.setString(5, sf.getEnPromo());
            ps.setLong(6, sf.getIdFacture());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) sf.setIdSF(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(SituationFinanciere sf) {
        String sql = "UPDATE SituationFinanciere SET totaleDesActes=?, totalePayé=?, crédit=?, statut=?, enPromo=?, idFacture=? WHERE idSF=?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, sf.getTotaleDesActes());
            ps.setDouble(2, sf.getTotalePaye());
            ps.setDouble(3, sf.getCredit());
            ps.setString(4, sf.getStatut());
            ps.setString(5, sf.getEnPromo());
            ps.setLong(6, sf.getIdFacture());
            ps.setLong(7, sf.getIdSF());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(SituationFinanciere sf) {
        deleteById(sf.getIdSF());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM SituationFinanciere WHERE idSF = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Optional<SituationFinanciere> findByFactureId(Long idFacture) {
        String sql = "SELECT * FROM SituationFinanciere WHERE idFacture = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idFacture);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(RowMappers.mapSituation(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return Optional.empty();
    }

    @Override
    public List<SituationFinanciere> findByStatut(String statut) {
        String sql = "SELECT * FROM SituationFinanciere WHERE statut = ?";
        List<SituationFinanciere> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, statut);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(RowMappers.mapSituation(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public List<SituationFinanciere> findByPatientId(Long idPatient) {
        String sql = """
            SELECT sf.* FROM SituationFinanciere sf
            INNER JOIN Facture f ON sf.idFacture = f.idFacture
            INNER JOIN Consultation c ON f.idConsultation = c.idConsultation
            INNER JOIN RDV r ON c.idRDV = r.idRDV
            WHERE r.idPatient = ?
            """;
        List<SituationFinanciere> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idPatient);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(RowMappers.mapSituation(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public List<SituationFinanciere> findWithCredit() {
        String sql = "SELECT * FROM SituationFinanciere WHERE crédit > 0";
        List<SituationFinanciere> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(RowMappers.mapSituation(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public Double calculateTotalCredit() {
        String sql = "SELECT COALESCE(SUM(crédit), 0) FROM SituationFinanciere WHERE crédit > 0";
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { throw new RuntimeException(e); }
        return 0.0;
    }

    @Override
    public Double calculateTotalCreditsPatient(Long idPatient) {
        String sql = """
            SELECT COALESCE(SUM(sf.crédit), 0) FROM SituationFinanciere sf
            INNER JOIN Facture f ON sf.idFacture = f.idFacture
            INNER JOIN Consultation c ON f.idConsultation = c.idConsultation
            INNER JOIN RDV r ON c.idRDV = r.idRDV
            WHERE r.idPatient = ? AND sf.crédit > 0
            """;
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idPatient);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble(1);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return 0.0;
    }
}
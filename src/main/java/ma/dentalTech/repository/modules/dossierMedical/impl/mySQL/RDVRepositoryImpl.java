package ma.dentalTech.repository.modules.dossierMedical.impl.mySQL;

import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.entities.dossierMedical.RDV;
import ma.dentalTech.repository.common.RowMappers;
import ma.dentalTech.repository.modules.dossierMedical.api.RDVRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RDVRepositoryImpl implements RDVRepository {

    @Override
    public List<RDV> findAll() {
        String sql = "SELECT * FROM RDV ORDER BY date, heure";
        List<RDV> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(RowMappers.mapRDV(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public RDV findById(Long id) {
        String sql = "SELECT * FROM RDV WHERE idRDV = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapRDV(rs);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    @Override
    public void create(RDV r) {
        String sql = "INSERT INTO RDV(date, heure, motif, statut, noteMedecin, idPatient, idMedecin) VALUES(?,?,?,?,?,?,?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, Date.valueOf(r.getDate()));
            ps.setTime(2, Time.valueOf(r.getHeure()));
            ps.setString(3, r.getMotif());
            ps.setString(4, r.getStatut().toString());
            ps.setString(5, r.getNoteMedecin());
            ps.setLong(6, r.getIdPatient());
            ps.setLong(7, r.getIdMedecin());

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) r.setIdRDV(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(RDV r) {
        String sql = "UPDATE RDV SET date=?, heure=?, motif=?, statut=?, noteMedecin=? WHERE idRDV=?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(r.getDate()));
            ps.setTime(2, Time.valueOf(r.getHeure()));
            ps.setString(3, r.getMotif());
            ps.setString(4, r.getStatut().toString());
            ps.setString(5, r.getNoteMedecin());
            ps.setLong(6, r.getIdRDV());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(RDV r) { deleteById(r.getIdRDV()); }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM RDV WHERE idRDV = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<RDV> findByPatientId(Long idPatient) {
        String sql = "SELECT * FROM RDV WHERE idPatient = ? ORDER BY date DESC";
        List<RDV> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idPatient);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(RowMappers.mapRDV(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public List<RDV> findByDate(LocalDate date) {
        String sql = "SELECT * FROM RDV WHERE date = ? ORDER BY heure";
        List<RDV> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(RowMappers.mapRDV(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public List<RDV> findByMedecinId(Long idMedecin) {
        return List.of();
    }
}
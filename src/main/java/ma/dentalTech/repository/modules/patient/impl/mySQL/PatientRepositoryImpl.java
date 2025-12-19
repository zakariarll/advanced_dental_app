package ma.dentalTech.repository.modules.patient.impl.mySQL;

import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.entities.patient.Patient;
import ma.dentalTech.repository.common.RowMappers;
import ma.dentalTech.repository.modules.patient.api.PatientRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatientRepositoryImpl implements PatientRepository {

    @Override
    public List<Patient> findAll() {
        String sql = "SELECT * FROM Patient ORDER BY nom, prenom";
        List<Patient> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(RowMappers.mapPatient(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public Patient findById(Long id) {
        String sql = "SELECT * FROM Patient WHERE idPatient = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapPatient(rs);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    @Override
    public void create(Patient p) {
        String sql = "INSERT INTO Patient(nom, prenom, dateDeNaissance, sexe, adresse, telephone, assurance, dateCreation, creePar) VALUES(?,?,?,?,?,?,?,?,?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getNom());
            ps.setString(2, p.getPrenom());
            ps.setDate(3, p.getDateDeNaissance() != null ? Date.valueOf(p.getDateDeNaissance()) : null);
            ps.setString(4, p.getSexe().name());
            ps.setString(5, p.getAdresse());
            ps.setString(6, p.getTelephone());
            ps.setString(7, p.getAssurance().name());
            ps.setDate(8, Date.valueOf(LocalDate.now()));
            ps.setString(9, "Admin"); // Par défaut, à lier au user connecté plus tard

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) p.setIdPatient(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(Patient p) {
        String sql = "UPDATE Patient SET nom=?, prenom=?, dateDeNaissance=?, sexe=?, adresse=?, telephone=?, assurance=? WHERE idPatient=?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getPrenom());
            ps.setDate(3, p.getDateDeNaissance() != null ? Date.valueOf(p.getDateDeNaissance()) : null);
            ps.setString(4, p.getSexe().name());
            ps.setString(5, p.getAdresse());
            ps.setString(6, p.getTelephone());
            ps.setString(7, p.getAssurance().name());
            ps.setLong(8, p.getIdPatient());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(Patient p) {
        deleteById(p.getIdPatient());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Patient WHERE idPatient = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Optional<Patient> findByTelephone(String telephone) {
        String sql = "SELECT * FROM Patient WHERE telephone = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, telephone);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(RowMappers.mapPatient(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return Optional.empty();
    }

    @Override
    public List<Patient> searchByNomOrPrenom(String keyword) {
        String sql = "SELECT * FROM Patient WHERE LOWER(nom) LIKE ? OR LOWER(prenom) LIKE ?";
        List<Patient> list = new ArrayList<>();
        String like = "%" + keyword.toLowerCase() + "%";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(RowMappers.mapPatient(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public List<Patient> findPage(int limit, int offset) {
        String sql = "SELECT * FROM Patient ORDER BY idPatient DESC LIMIT ? OFFSET ?";
        List<Patient> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(RowMappers.mapPatient(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM Patient";
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getLong(1);
        } catch (SQLException e) { throw new RuntimeException(e); }
        return 0;
    }
}
package ma.dentalTech.repository.modules.dossierMedical.impl.mySQL;

import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.entities.dossierMedical.Consultation;
import ma.dentalTech.repository.common.RowMappers;
import ma.dentalTech.repository.modules.dossierMedical.api.ConsultationRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConsultationRepositoryImpl implements ConsultationRepository {

    @Override
    public void create(Consultation c) {
        String sql = "INSERT INTO Consultation(date, statut, observationMedecin, idRDV) VALUES(?,?,?,?)";
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, Date.valueOf(c.getDate()));
            ps.setString(2, c.getStatut());
            ps.setString(3, c.getObservationMedecin());
            ps.setLong(4, c.getIdRDV());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) { if(keys.next()) c.setIdConsultation(keys.getLong(1)); }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Optional<Consultation> findByRDVId(Long idRDV) {
        String sql = "SELECT * FROM Consultation WHERE idRDV = ?";
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idRDV);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) return Optional.of(RowMappers.mapConsultation(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return Optional.empty();
    }

    // ... Implementer findAll, findById, delete standard ...
    @Override public List<Consultation> findAll() { return new ArrayList<>(); }
    @Override
    public Consultation findById(Long id) {
        String sql = "SELECT * FROM Consultation WHERE idConsultation = ?";
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) return RowMappers.mapConsultation(rs);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    @Override
    public void update(Consultation c) {
        String sql = "UPDATE Consultation SET date=?, statut=?, observationMedecin=?, idRDV=? WHERE idConsultation=?";
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(c.getDate()));
            ps.setString(2, c.getStatut());
            ps.setString(3, c.getObservationMedecin());
            ps.setLong(4, c.getIdRDV());
            ps.setLong(5, c.getIdConsultation());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override public void delete(Consultation c) {}
    @Override public void deleteById(Long id) {}
    @Override public List<Consultation> findByDate(LocalDate date) { return new ArrayList<>(); }
}
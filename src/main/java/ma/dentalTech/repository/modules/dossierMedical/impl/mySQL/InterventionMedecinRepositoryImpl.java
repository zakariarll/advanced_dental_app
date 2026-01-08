package ma.dentalTech.repository.modules.dossierMedical.impl.mySQL;

import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.entities.dossierMedical.InterventionMedecin;
import ma.dentalTech.repository.modules.dossierMedical.api.InterventionMedecinRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InterventionMedecinRepositoryImpl implements InterventionMedecinRepository {

    @Override
    public List<InterventionMedecin> findAll() {
        String sql = "SELECT * FROM InterventionMédecin";
        List<InterventionMedecin> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapInterventionMedecin(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public InterventionMedecin findById(Long id) {
        String sql = "SELECT * FROM InterventionMédecin WHERE idIM = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapInterventionMedecin(rs);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    @Override
    public void create(InterventionMedecin im) {
        String sql = "INSERT INTO InterventionMédecin(prixDePatient, idConsultation, idMedecin) VALUES(?,?,?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDouble(1, im.getPrixDePatient());
            ps.setLong(2, im.getIdConsultation());
            ps.setLong(3, im.getIdMedecin());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) im.setIdIM(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(InterventionMedecin im) {
        String sql = "UPDATE InterventionMédecin SET prixDePatient=?, idConsultation=?, idMedecin=? WHERE idIM=?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, im.getPrixDePatient());
            ps.setLong(2, im.getIdConsultation());
            ps.setLong(3, im.getIdMedecin());
            ps.setLong(4, im.getIdIM());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(InterventionMedecin im) {
        deleteById(im.getIdIM());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM InterventionMédecin WHERE idIM = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<InterventionMedecin> findByConsultationId(Long idConsultation) {
        String sql = "SELECT * FROM InterventionMédecin WHERE idConsultation = ?";
        List<InterventionMedecin> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idConsultation);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapInterventionMedecin(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    private InterventionMedecin mapInterventionMedecin(ResultSet rs) throws SQLException {
        InterventionMedecin im = new InterventionMedecin();
        im.setIdIM(rs.getLong("idIM"));
        im.setPrixDePatient(rs.getDouble("prixDePatient"));
        im.setIdConsultation(rs.getLong("idConsultation"));
        im.setIdMedecin(rs.getLong("idMedecin"));
        return im;
    }
}
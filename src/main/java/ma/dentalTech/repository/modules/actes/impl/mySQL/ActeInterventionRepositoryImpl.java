package ma.dentalTech.repository.modules.actes.impl.mySQL;

import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.entities.actes.ActeIntervention;
import ma.dentalTech.repository.modules.actes.api.ActeInterventionRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActeInterventionRepositoryImpl implements ActeInterventionRepository {

    @Override
    public List<ActeIntervention> findAll() {
        String sql = "SELECT * FROM Acte_Intervention";
        List<ActeIntervention> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapActeIntervention(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public ActeIntervention findById(Long id) {
        String sql = "SELECT * FROM Acte_Intervention WHERE idActeIntervention = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapActeIntervention(rs);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    @Override
    public void create(ActeIntervention ai) {
        String sql = "INSERT INTO Acte_Intervention(idActe, idIM) VALUES(?,?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, ai.getIdActe());
            ps.setLong(2, ai.getIdIM());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) ai.setIdActeIntervention(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(ActeIntervention ai) {
        String sql = "UPDATE Acte_Intervention SET idActe=?, idIM=? WHERE idActeIntervention=?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, ai.getIdActe());
            ps.setLong(2, ai.getIdIM());
            ps.setLong(3, ai.getIdActeIntervention());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(ActeIntervention ai) {
        deleteById(ai.getIdActeIntervention());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Acte_Intervention WHERE idActeIntervention = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<ActeIntervention> findByActeId(Long idActe) {
        String sql = "SELECT * FROM Acte_Intervention WHERE idActe = ?";
        List<ActeIntervention> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idActe);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapActeIntervention(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public List<ActeIntervention> findByInterventionMedecinId(Long idIM) {
        String sql = "SELECT * FROM Acte_Intervention WHERE idIM = ?";
        List<ActeIntervention> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idIM);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapActeIntervention(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public void deleteByInterventionMedecinId(Long idIM) {
        String sql = "DELETE FROM Acte_Intervention WHERE idIM = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idIM);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public boolean existsByActeIdAndInterventionMedecinId(Long idActe, Long idIM) {
        String sql = "SELECT COUNT(*) FROM Acte_Intervention WHERE idActe = ? AND idIM = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idActe);
            ps.setLong(2, idIM);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return false;
    }

    @Override
    public int countByActeId(Long idActe) {
        String sql = "SELECT COUNT(*) FROM Acte_Intervention WHERE idActe = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idActe);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return 0;
    }

    private ActeIntervention mapActeIntervention(ResultSet rs) throws SQLException {
        ActeIntervention ai = new ActeIntervention();
        ai.setIdActeIntervention(rs.getLong("idActeIntervention"));
        ai.setIdActe(rs.getLong("idActe"));
        ai.setIdIM(rs.getLong("idIM"));
        return ai;
    }
}
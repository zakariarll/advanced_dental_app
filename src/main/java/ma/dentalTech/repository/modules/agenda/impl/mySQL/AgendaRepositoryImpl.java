package ma.dentalTech.repository.modules.agenda.impl.mySQL;

import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.entities.agenda.AgendaMensuel;
import ma.dentalTech.repository.common.RowMappers;
import ma.dentalTech.repository.modules.agenda.api.AgendaRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AgendaRepositoryImpl implements AgendaRepository {

    @Override
    public List<AgendaMensuel> findAll() {
        String sql = "SELECT * FROM AgendaMensuel";
        List<AgendaMensuel> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             ResultSet rs = c.createStatement().executeQuery(sql)) {
            while(rs.next()) list.add(RowMappers.mapAgenda(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public AgendaMensuel findById(Long id) {
        String sql = "SELECT * FROM AgendaMensuel WHERE idAgenda = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()) return RowMappers.mapAgenda(rs);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    @Override
    public void create(AgendaMensuel a) {
        String sql = "INSERT INTO AgendaMensuel(mois, etat, idMedecin) VALUES(?,?,?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, a.getMois());
            ps.setBoolean(2, a.getEtat());
            ps.setLong(3, a.getIdMedecin());
            ps.executeUpdate();
            try(ResultSet keys = ps.getGeneratedKeys()){
                if(keys.next()) a.setIdAgenda(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(AgendaMensuel a) {
        String sql = "UPDATE AgendaMensuel SET mois=?, etat=? WHERE idAgenda=?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, a.getMois());
            ps.setBoolean(2, a.getEtat());
            ps.setLong(3, a.getIdAgenda());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(AgendaMensuel a) { deleteById(a.getIdAgenda()); }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM AgendaMensuel WHERE idAgenda=?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<AgendaMensuel> findByMedecin(Long idMedecin) {
        String sql = "SELECT * FROM AgendaMensuel WHERE idMedecin = ?";
        List<AgendaMensuel> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idMedecin);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()) list.add(RowMappers.mapAgenda(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }
}
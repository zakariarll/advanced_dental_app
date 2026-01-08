package ma.dentalTech.repository.modules.userManager.impl.mySQL;

import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.entities.userManager.Role;
import ma.dentalTech.repository.common.RowMappers;
import ma.dentalTech.repository.modules.userManager.api.RoleRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleRepositoryImpl implements RoleRepository {

    @Override
    public List<Role> findAll() {
        String sql = "SELECT * FROM Role";
        List<Role> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             ResultSet rs = c.createStatement().executeQuery(sql)) {
            while(rs.next()) list.add(RowMappers.mapRole(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public Role findById(Long id) {
        String sql = "SELECT * FROM Role WHERE idRole = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()) return RowMappers.mapRole(rs);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    @Override
    public Role findByLibelle(String libelle) {
        String sql = "SELECT * FROM Role WHERE libelle = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, libelle);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()) return RowMappers.mapRole(rs);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    @Override
    public void create(Role r) {
        String sql = "INSERT INTO Role(libelle) VALUES(?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, r.getLibelle());
            ps.executeUpdate();
            try(ResultSet keys = ps.getGeneratedKeys()){
                if(keys.next()) r.setIdRole(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(Role r) {
        String sql = "UPDATE Role SET libelle=? WHERE idRole=?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, r.getLibelle());
            ps.setLong(2, r.getIdRole());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(Role r) { deleteById(r.getIdRole()); }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Role WHERE idRole=?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}
package ma.dentalTech.repository.modules.actes.impl.mySQL;

import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.entities.actes.Acte;
import ma.dentalTech.repository.common.RowMappers;
import ma.dentalTech.repository.modules.actes.api.ActeRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ActeRepositoryImpl implements ActeRepository {

    @Override
    public List<Acte> findAll() {
        String sql = "SELECT * FROM Acte ORDER BY catégorie, libellé";
        List<Acte> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(RowMappers.mapActe(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public Acte findById(Long id) {
        String sql = "SELECT * FROM Acte WHERE idActe = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapActe(rs);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    @Override
    public void create(Acte acte) {
        String sql = "INSERT INTO Acte(libellé, catégorie, prixDeBase, description, code) VALUES(?,?,?,?,?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, acte.getLibelle());
            ps.setString(2, acte.getCategorie());
            ps.setDouble(3, acte.getPrixDeBase());
            ps.setString(4, acte.getDescription());
            ps.setInt(5, acte.getCode());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) acte.setIdActe(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(Acte acte) {
        String sql = "UPDATE Acte SET libellé=?, catégorie=?, prixDeBase=?, description=?, code=? WHERE idActe=?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, acte.getLibelle());
            ps.setString(2, acte.getCategorie());
            ps.setDouble(3, acte.getPrixDeBase());
            ps.setString(4, acte.getDescription());
            ps.setInt(5, acte.getCode());
            ps.setLong(6, acte.getIdActe());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(Acte acte) {
        deleteById(acte.getIdActe());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Acte WHERE idActe = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Optional<Acte> findByCode(Integer code) {
        String sql = "SELECT * FROM Acte WHERE code = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(RowMappers.mapActe(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return Optional.empty();
    }

    @Override
    public List<Acte> findByCategorie(String categorie) {
        String sql = "SELECT * FROM Acte WHERE catégorie = ? ORDER BY libellé";
        List<Acte> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, categorie);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(RowMappers.mapActe(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public List<Acte> findByLibelleContaining(String libelle) {
        String sql = "SELECT * FROM Acte WHERE libellé LIKE ? ORDER BY libellé";
        List<Acte> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, "%" + libelle + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(RowMappers.mapActe(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public List<Acte> findByPrixBetween(Double prixMin, Double prixMax) {
        String sql = "SELECT * FROM Acte WHERE prixDeBase BETWEEN ? AND ? ORDER BY prixDeBase";
        List<Acte> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, prixMin);
            ps.setDouble(2, prixMax);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(RowMappers.mapActe(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public List<String> findAllCategories() {
        String sql = "SELECT DISTINCT catégorie FROM Acte ORDER BY catégorie";
        List<String> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(rs.getString("catégorie"));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public boolean existsByCode(Integer code) {
        String sql = "SELECT COUNT(*) FROM Acte WHERE code = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return false;
    }

    @Override
    public boolean existsByLibelle(String libelle) {
        String sql = "SELECT COUNT(*) FROM Acte WHERE libellé = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, libelle);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return false;
    }
}
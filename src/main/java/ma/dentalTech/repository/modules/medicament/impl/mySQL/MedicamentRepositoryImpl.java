package ma.dentalTech.repository.modules.medicament.impl.mySQL;

import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.entities.medicament.Medicament;
import ma.dentalTech.repository.common.CrudRepository;
import ma.dentalTech.repository.common.RowMappers;
import ma.dentalTech.repository.modules.medicament.api.MedicamentRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicamentRepositoryImpl implements MedicamentRepository {

    private static final String TABLE = "Médicament";

    @Override
    public List<Medicament> findAll() {
        String sql = "SELECT * FROM " + TABLE;
        List<Medicament> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(RowMappers.mapMedicament(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public Medicament findById(Long id) {
        String sql = "SELECT * FROM " + TABLE + " WHERE idMct = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapMedicament(rs);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    @Override
    public void create(Medicament m) {
        String sql = "INSERT INTO " + TABLE + "(nom, laboratoire, type, forme, remboursable, prixUnitaire, description) VALUES(?,?,?,?,?,?,?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, m.getNom());
            ps.setString(2, m.getLaboratoire());
            ps.setString(3, m.getType());
            ps.setString(4, m.getForme());
            ps.setBoolean(5, m.getRemboursable());
            ps.setDouble(6, m.getPrixUnitaire());
            ps.setString(7, m.getDescription());

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) m.setIdMct(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(Medicament m) {
        String sql = "UPDATE " + TABLE + " SET nom=?, laboratoire=?, type=?, forme=?, remboursable=?, prixUnitaire=?, description=? WHERE idMct=?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, m.getNom());
            ps.setString(2, m.getLaboratoire());
            ps.setString(3, m.getType());
            ps.setString(4, m.getForme());
            ps.setBoolean(5, m.getRemboursable());
            ps.setDouble(6, m.getPrixUnitaire());
            ps.setString(7, m.getDescription());
            ps.setLong(8, m.getIdMct());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(Medicament m) { deleteById(m.getIdMct()); }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM " + TABLE + " WHERE idMct = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Medicament> findByNomLike(String nom) {
        // Utilisation de CONCAT ou de la concaténation simple pour ajouter les %
        // La colonne SQL s'appelle probablement 'nom'
        String sql = "SELECT * FROM Médicament WHERE nom LIKE ?";
        List<Medicament> list = new ArrayList<>();

        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            // La chaîne de recherche doit être formatée : %nom%
            ps.setString(1, "%" + nom + "%");

            try(ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    // Nécessite le RowMapper pour Medicament
                    list.add(RowMappers.mapMedicament(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur de recherche par nom : " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<Medicament> findByType(String type) {
        // La colonne SQL s'appelle probablement 'type'
        String sql = "SELECT * FROM Médicament WHERE type = ?";
        List<Medicament> list = new ArrayList<>();

        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            // La chaîne de recherche est utilisée telle quelle
            ps.setString(1, type);

            try(ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    // Nécessite le RowMapper pour Medicament
                    list.add(RowMappers.mapMedicament(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur de recherche par type : " + e.getMessage(), e);
        }
        return list;
    }
}
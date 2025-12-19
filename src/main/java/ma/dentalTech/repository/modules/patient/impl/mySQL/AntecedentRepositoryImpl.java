package ma.dentalTech.repository.modules.patient.impl.mySQL;

import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.entities.enums.CategorieAntecedent;
import ma.dentalTech.entities.enums.NiveauRisque;
import ma.dentalTech.entities.patient.Antecedent;
import ma.dentalTech.repository.common.RowMappers;
import ma.dentalTech.repository.modules.patient.api.AntecedentRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AntecedentRepositoryImpl implements AntecedentRepository {

    // Nom de table avec accents = guillemets obligatoires
    private static final String TABLE = "Antecedents";

    @Override
    public List<Antecedent> findAll() {
        String sql = "SELECT * FROM " + TABLE;
        List<Antecedent> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(RowMappers.mapAntecedent(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public Antecedent findById(Long id) {
        String sql = "SELECT * FROM " + TABLE + " WHERE idAntecedent = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapAntecedent(rs);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    @Override
    public void create(Antecedent a) {
        String sql = "INSERT INTO " + TABLE + "(nom, catégorie, niveauDeRisque, idPatient) VALUES(?,?,?,?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, a.getNom());
            ps.setString(2, a.getCategorie().name());
            ps.setString(3, a.getNiveauRisque().name());
            ps.setLong(4, a.getIdPatient());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) a.setIdAntecedent(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(Antecedent a) {
        String sql = "UPDATE " + TABLE + " SET nom=?, catégorie=?, niveauDeRisque=? WHERE idAntecedent=?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, a.getNom());
            ps.setString(2, a.getCategorie().name());
            ps.setString(3, a.getNiveauRisque().name());
            ps.setLong(4, a.getIdAntecedent());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(Antecedent a) { deleteById(a.getIdAntecedent()); }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM " + TABLE + " WHERE idAntecedent = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Antecedent> findByPatientId(Long idPatient) {
        String sql = "SELECT * FROM " + TABLE + " WHERE idPatient = ?";
        List<Antecedent> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idPatient);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(RowMappers.mapAntecedent(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public List<Antecedent> findByCategorie(CategorieAntecedent categorie) {
        String sql = "SELECT * FROM " + TABLE + " WHERE catégorie = ?";
        List<Antecedent> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, categorie.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(RowMappers.mapAntecedent(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public List<Antecedent> findByNiveauRisque(NiveauRisque niveau) {
        String sql = "SELECT * FROM " + TABLE + " WHERE niveauDeRisque = ?";
        List<Antecedent> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, niveau.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(RowMappers.mapAntecedent(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }
}
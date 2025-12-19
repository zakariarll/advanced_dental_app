package ma.dentalTech.repository.modules.ordonnance.impl.mySQL;

import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.entities.ordonnance.Ordonnance;
import ma.dentalTech.repository.common.CrudRepository;
import ma.dentalTech.repository.common.RowMappers;
import ma.dentalTech.repository.modules.ordonnance.api.OrdonnanceRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdonnanceRepositoryImpl implements OrdonnanceRepository {

    // Typo dans le script SQL original : Ordannance
    private static final String TABLE = "Ordonnance";

    @Override
    public List<Ordonnance> findAll() {
        String sql = "SELECT * FROM " + TABLE;
        List<Ordonnance> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(RowMappers.mapOrdonnance(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public Ordonnance findById(Long id) {
        String sql = "SELECT * FROM " + TABLE + " WHERE idOrd = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapOrdonnance(rs);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    @Override
    public void create(Ordonnance o) {
        String sql = "INSERT INTO " + TABLE + "(date, idPatient, idMedecin) VALUES(?,?,?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, Date.valueOf(o.getDate()));
            ps.setLong(2, o.getIdPatient());
            ps.setLong(3, o.getIdMedecin());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) o.setIdOrd(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(Ordonnance o) {
        String sql = "UPDATE " + TABLE + " SET date=? WHERE idOrd=?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(o.getDate()));
            ps.setLong(2, o.getIdOrd());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(Ordonnance o) { deleteById(o.getIdOrd()); }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM " + TABLE + " WHERE idOrd = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }


    @Override
    public List<Ordonnance> findByPatientId(Long idPatient) {
        String sql = "SELECT * FROM Ordonnance WHERE idPatient = ?"; // Requête SQL pour filtrer
        List<Ordonnance> list = new ArrayList<>();

        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, idPatient);

            try(ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {

                    list.add(RowMappers.mapOrdonnance(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    // Dans ma.dentalTech.repository.modules.ordonnance.impl.mySQL.OrdonnanceRepositoryImpl

    @Override
    public List<Ordonnance> findByMedecinId(Long idMedecin) {
        // Requête SQL pour sélectionner toutes les ordonnances où l'ID du médecin correspond
        String sql = "SELECT * FROM Ordonnance WHERE idMed = ?";
        List<Ordonnance> list = new ArrayList<>();

        // Utilisation de try-with-resources pour la gestion automatique de la connexion et du PreparedStatement
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            // 1. Lier la valeur de l'identifiant du médecin au premier paramètre (?)
            ps.setLong(1, idMedecin);

            // 2. Exécuter la requête
            try(ResultSet rs = ps.executeQuery()) {

                // 3. Parcourir les résultats et mapper chaque ligne en objet Ordonnance
                while(rs.next()) {
                    // IMPORTANT : Assurez-vous d'avoir une méthode mapOrdonnance dans votre RowMappers
                    list.add(RowMappers.mapOrdonnance(rs));
                }
            }

        } catch (SQLException e) {
            // En cas d'erreur SQL, on enveloppe et relance une RuntimeException
            throw new RuntimeException("Erreur lors de la récupération des ordonnances par ID Médecin : " + e.getMessage(), e);
        }

        return list;
    }
}
package ma.dentalTech.repository.modules.certificat.impl.mySQL;

import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.entities.certificat.Certificat;
import ma.dentalTech.repository.common.RowMappers;
import ma.dentalTech.repository.modules.certificat.api.CertificatRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CertificatRepositoryImpl implements CertificatRepository {

    @Override
    public List<Certificat> findAll() {
        String sql = "SELECT * FROM Certificat";
        List<Certificat> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             ResultSet rs = c.createStatement().executeQuery(sql)) {
            while(rs.next()) list.add(RowMappers.mapCertificat(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public Certificat findById(Long id) {
        String sql = "SELECT * FROM Certificat WHERE idCertif = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()) return RowMappers.mapCertificat(rs);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    @Override
    public void create(Certificat cert) {
        // dateDébut et durée avec accents
        String sql = "INSERT INTO Certificat(dateDébut, dateFin, durée, noteMedecin, idPatient, idMedecin) VALUES(?,?,?,?,?,?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, Date.valueOf(cert.getDateDebut()));
            ps.setDate(2, Date.valueOf(cert.getDateFin()));
            ps.setInt(3, cert.getDuree());
            ps.setString(4, cert.getNoteMedecin());
            ps.setLong(5, cert.getIdPatient());
            ps.setLong(6, cert.getIdMedecin());
            ps.executeUpdate();
            try(ResultSet keys = ps.getGeneratedKeys()){
                if(keys.next()) cert.setIdCertif(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(Certificat cert) {
        String sql = "UPDATE Certificat SET dateDébut=?, dateFin=?, durée=?, noteMedecin=? WHERE idCertif=?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(cert.getDateDebut()));
            ps.setDate(2, Date.valueOf(cert.getDateFin()));
            ps.setInt(3, cert.getDuree());
            ps.setString(4, cert.getNoteMedecin());
            ps.setLong(5, cert.getIdCertif());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(Certificat cert) { deleteById(cert.getIdCertif()); }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Certificat WHERE idCertif=?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Certificat> findByPatientId(Long idPatient) {
        String sql = "SELECT * FROM Certificat WHERE idPatient = ?";
        List<Certificat> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idPatient);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()) list.add(RowMappers.mapCertificat(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }
}
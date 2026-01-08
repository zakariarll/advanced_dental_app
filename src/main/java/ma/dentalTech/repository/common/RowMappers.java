package ma.dentalTech.repository.common;

import ma.dentalTech.entities.actes.Acte;
import ma.dentalTech.entities.agenda.AgendaMensuel;
import ma.dentalTech.entities.caisse.*;
import ma.dentalTech.entities.certificat.Certificat;
import ma.dentalTech.entities.dossierMedical.Consultation;
import ma.dentalTech.entities.dossierMedical.RDV;
import ma.dentalTech.entities.enums.*;
import ma.dentalTech.entities.medicament.Medicament;
import ma.dentalTech.entities.ordonnance.Ordonnance;
import ma.dentalTech.entities.patient.Antecedent;
import ma.dentalTech.entities.patient.Patient;
import ma.dentalTech.entities.userManager.Role;
import ma.dentalTech.entities.userManager.Utilisateur;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class RowMappers {

    private RowMappers(){}

    // --- PATIENT ---
    public static Patient mapPatient(ResultSet rs) throws SQLException {
        Patient p = new Patient();
        p.setIdPatient(rs.getLong("idPatient"));
        p.setNom(rs.getString("nom"));
        p.setPrenom(rs.getString("prenom"));
        p.setAdresse(rs.getString("adresse"));
        p.setTelephone(rs.getString("telephone"));
        if (rs.getDate("dateDeNaissance") != null) p.setDateDeNaissance(rs.getDate("dateDeNaissance").toLocalDate());
        if (rs.getString("sexe") != null) p.setSexe(Sexe.valueOf(rs.getString("sexe")));
        if (rs.getString("assurance") != null) p.setAssurance(Assurance.valueOf(rs.getString("assurance")));
        // Audit
        if (rs.getDate("dateCreation") != null) p.setDateCreation(rs.getDate("dateCreation").toLocalDate());
        return p;
    }

    public static Antecedent mapAntecedent(ResultSet rs) throws SQLException {
        Antecedent a = new Antecedent();
        a.setIdAntecedent(rs.getLong("idAntecedent"));
        a.setNom(rs.getString("nom"));
        // Attention aux accents du script SQL
        if(rs.getString("catégorie") != null) a.setCategorie(CategorieAntecedent.valueOf(rs.getString("catégorie")));
        if(rs.getString("niveauDeRisque") != null) a.setNiveauRisque(NiveauRisque.valueOf(rs.getString("niveauDeRisque")));
        a.setIdPatient(rs.getLong("idPatient"));
        return a;
    }

    // --- USER MANAGER ---
    public static Utilisateur mapUtilisateur(ResultSet rs) throws SQLException {
        Utilisateur u = new Utilisateur();
        u.setIdUser(rs.getLong("idUser"));
        u.setNom(rs.getString("nom"));
        u.setEmail(rs.getString("email"));
        u.setLogin(rs.getString("login"));
        u.setMotDePasse(rs.getString("motDePasse"));
        u.setCin(rs.getString("cin"));
        u.setTel(rs.getString("tel"));
        if(rs.getString("sexe") != null) u.setSexe(Sexe.valueOf(rs.getString("sexe")));
        u.setIdRole(rs.getLong("idRole"));
        u.setIdCabinet(rs.getLong("idCabinet"));
        return u;
    }

    // --- DOSSIER MEDICAL ---
    public static RDV mapRDV(ResultSet rs) throws SQLException {
        RDV r = new RDV();
        r.setIdRDV(rs.getLong("idRDV"));
        if(rs.getDate("date") != null) r.setDate(rs.getDate("date").toLocalDate());
        if(rs.getTime("heure") != null) r.setHeure(rs.getTime("heure").toLocalTime());
        r.setMotif(rs.getString("motif"));
        if(rs.getString("statut") != null) r.setStatut(StatutRDV.valueOf(rs.getString("statut"))); // Assure-toi que l'Enum existe ou utilise String
        r.setNoteMedecin(rs.getString("noteMedecin"));
        r.setIdPatient(rs.getLong("idPatient"));
        r.setIdMedecin(rs.getLong("idMedecin"));
        return r;
    }

    public static Consultation mapConsultation(ResultSet rs) throws SQLException {
        Consultation c = new Consultation();
        c.setIdConsultation(rs.getLong("idConsultation"));
        if(rs.getDate("date") != null) c.setDate(rs.getDate("date").toLocalDate());
        c.setStatut(rs.getString("statut"));
        c.setObservationMedecin(rs.getString("observationMedecin"));
        c.setIdRDV(rs.getLong("idRDV"));
        return c;
    }

    // --- ACTES ---
    public static Acte mapActe(ResultSet rs) throws SQLException {
        return Acte.builder()
                .idActe(rs.getLong("idActe"))
                .libelle(rs.getString("libellé"))
                .categorie(rs.getString("catégorie"))
                .prixDeBase(rs.getDouble("prixDeBase"))
                .code(rs.getInt("code"))
                .description(rs.getString("description"))
                .build();
    }

    // --- PHARMACIE ---
    public static Medicament mapMedicament(ResultSet rs) throws SQLException {
        return Medicament.builder()
                .idMct(rs.getLong("idMct"))
                .nom(rs.getString("nom"))
                .laboratoire(rs.getString("laboratoire"))
                .type(rs.getString("type"))
                .forme(rs.getString("forme"))
                .remboursable(rs.getBoolean("remboursable"))
                .prixUnitaire(rs.getDouble("prixUnitaire"))
                .description(rs.getString("description"))
                .build();
    }

    public static Ordonnance mapOrdonnance(ResultSet rs) throws SQLException {
        Ordonnance o = new Ordonnance();
        o.setIdOrd(rs.getLong("idOrd"));
        if(rs.getDate("date") != null) o.setDate(rs.getDate("date").toLocalDate());
        o.setIdPatient(rs.getLong("idPatient"));
        o.setIdMedecin(rs.getLong("idMedecin"));
        return o;
    }

    public static Facture mapFacture(ResultSet rs) throws SQLException {
        Facture f = new Facture();
        f.setIdFacture(rs.getLong("idFacture"));
        f.setTotaleFacture(rs.getDouble("totaleFacture"));
        f.setTotalePaye(rs.getDouble("totalePayé")); // Accent SQL
        f.setReste(rs.getDouble("reste"));
        f.setStatut(rs.getString("statut"));
        if(rs.getTimestamp("dateFacture") != null)
            f.setDateFacture(rs.getTimestamp("dateFacture").toLocalDateTime());
        f.setIdConsultation(rs.getLong("idConsultation"));
        return f;
    }

    public static SituationFinanciere mapSituation(ResultSet rs) throws SQLException {
        SituationFinanciere sf = new SituationFinanciere();
        sf.setIdSF(rs.getLong("idSF"));
        sf.setTotaleDesActes(rs.getDouble("totaleDesActes"));
        sf.setTotalePaye(rs.getDouble("totalePayé")); // Accent SQL
        sf.setCredit(rs.getDouble("crédit")); // Accent SQL
        sf.setStatut(rs.getString("statut"));
        sf.setEnPromo(rs.getString("enPromo"));
        sf.setIdFacture(rs.getLong("idFacture"));
        return sf;
    }

    public static Revenue mapRevenue(ResultSet rs) throws SQLException {
        Revenue r = new Revenue();
        r.setIdRevenue(rs.getLong("idRevenue"));
        r.setTitre(rs.getString("titre"));
        r.setDescription(rs.getString("description"));
        r.setMontant(rs.getDouble("montant"));
        if(rs.getTimestamp("date") != null) r.setDate(rs.getTimestamp("date").toLocalDateTime());
        r.setIdCabinet(rs.getLong("idCabinet"));
        return r;
    }

    public static Charge mapCharge(ResultSet rs) throws SQLException {
        Charge c = new Charge();
        c.setIdCharge(rs.getLong("idCharge"));
        c.setTitre(rs.getString("titre"));
        c.setDescription(rs.getString("description"));
        c.setMontant(rs.getDouble("montant"));
        if(rs.getTimestamp("date") != null) c.setDate(rs.getTimestamp("date").toLocalDateTime());
        c.setIdCabinet(rs.getLong("idCabinet"));
        return c;
    }

    public static Statistique mapStatistique(ResultSet rs) throws SQLException {
        Statistique s = new Statistique();
        s.setIdStatistique(rs.getLong("idStatistique"));
        s.setNom(rs.getString("nom"));
        s.setCategorie(rs.getString("categorie"));
        s.setChiffre(rs.getDouble("chiffre"));
        if(rs.getDate("dateCalcul") != null) s.setDateCalcul(rs.getDate("dateCalcul").toLocalDate());
        return s;
    }

    // --- CERTIFICAT ---
    public static Certificat mapCertificat(ResultSet rs) throws SQLException {
        Certificat c = new Certificat();
        c.setIdCertif(rs.getLong("idCertif"));
        if(rs.getDate("dateDébut") != null) c.setDateDebut(rs.getDate("dateDébut").toLocalDate());
        if(rs.getDate("dateFin") != null) c.setDateFin(rs.getDate("dateFin").toLocalDate());
        c.setDuree(rs.getInt("durée"));
        c.setNoteMedecin(rs.getString("noteMedecin"));
        c.setIdPatient(rs.getLong("idPatient"));
        c.setIdMedecin(rs.getLong("idMedecin"));
        return c;
    }

    // --- AGENDA ---
    public static AgendaMensuel mapAgenda(ResultSet rs) throws SQLException {
        AgendaMensuel a = new AgendaMensuel();
        a.setIdAgenda(rs.getLong("idAgenda"));
        a.setMois(rs.getString("mois"));
        a.setEtat(rs.getBoolean("etat"));
        a.setIdMedecin(rs.getLong("idMedecin"));
        return a;
    }

    // --- ROLE ---
    public static Role mapRole(ResultSet rs) throws SQLException {
        Role r = new Role();
        r.setIdRole(rs.getLong("idRole"));
        r.setLibelle(rs.getString("libelle"));
        return r;
    }
}
package ma.dentalTech.service.modules.RDVPatient.impl;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.dossierMedical.RDV;
import ma.dentalTech.entities.enums.StatutRDV;
import ma.dentalTech.entities.patient.Patient;
import ma.dentalTech.repository.modules.dossierMedical.api.RDVRepository;
import ma.dentalTech.repository.modules.dossierMedical.impl.mySQL.RDVRepositoryImpl;
import ma.dentalTech.service.modules.RDVPatient.api.IGestionRDVService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestionRDVServiceImpl implements IGestionRDVService {

    private final RDVRepository rdvRepo;

    // Simulation Liste d'attente (Mémoire) car pas de table SQL définie pour ça
    private final Map<LocalDate, List<Patient>> listeAttente = new HashMap<>();

    // Constructeur par défaut (utilisé par ApplicationContext si pas d'injection avancée)
    public GestionRDVServiceImpl() {
        this.rdvRepo = new RDVRepositoryImpl();
    }

    // Constructeur pour injection (Tests)
    public GestionRDVServiceImpl(RDVRepository rdvRepo) {
        this.rdvRepo = rdvRepo;
    }

    @Override
    public RDV planifierRDV(Long idPatient, Long idMedecin, LocalDate date, LocalTime heure, String motif)
            throws ServiceException, ValidationException {

        // 1. Validation de base
        if(date.isBefore(LocalDate.now())) throw new ValidationException("Date passée invalide");

        // 2. Vérifier disponibilité (Include Consulter Agenda)
        verifierDisponibilite(idMedecin, date, heure);

        // 3. Création
        RDV rdv = RDV.builder()
                .idPatient(idPatient)
                .idMedecin(idMedecin)
                .date(date)
                .heure(heure)
                .motif(motif)
                .statut(StatutRDV.PLANIFIE)
                .build();

        try {
            rdvRepo.create(rdv);
            return rdv;
        } catch (Exception e) {
            throw new ServiceException("Erreur technique création RDV", e);
        }
    }

    @Override
    public void confirmerRDV(Long idRDV) throws ServiceException {
        RDV rdv = rdvRepo.findById(idRDV);
        if(rdv == null) throw new ServiceException("RDV introuvable");

        rdv.setStatut(StatutRDV.CONFIRME);
        rdvRepo.update(rdv);

        // CAS : Envoyer Email (Extends)
        envoyerEmailConfirmation(rdv);
    }

    @Override
    public void annulerRDV(Long idRDV, String motifAnnulation) throws ServiceException {
        RDV rdv = rdvRepo.findById(idRDV);
        if(rdv == null) throw new ServiceException("RDV introuvable");

        rdv.setStatut(StatutRDV.ANNULE);
        rdv.setNoteMedecin("Annulé: " + motifAnnulation);
        rdvRepo.update(rdv);

        // Bonus : Vérifier liste d'attente pour ce jour là
        verifierListeAttente(rdv.getDate());
    }

    @Override
    public void modifierRDV(Long idRDV, LocalDate nouvelleDate, LocalTime nouvelleHeure)
            throws ServiceException, ValidationException {

        RDV rdv = rdvRepo.findById(idRDV);
        if(rdv == null) throw new ServiceException("RDV introuvable");

        // Include Consulter Agenda
        verifierDisponibilite(rdv.getIdMedecin(), nouvelleDate, nouvelleHeure);

        rdv.setDate(nouvelleDate);
        rdv.setHeure(nouvelleHeure);
        rdvRepo.update(rdv);
    }

    @Override
    public List<RDV> consulterPlanningMedecin(Long idMedecin, LocalDate date) {
        // Filtre mémoire rapide ou requête SQL spécifique
        List<RDV> duJour = rdvRepo.findByDate(date);
        List<RDV> resultat = new ArrayList<>();
        for(RDV r : duJour) {
            if(r.getIdMedecin().equals(idMedecin) && r.getStatut() != StatutRDV.ANNULE) {
                resultat.add(r);
            }
        }
        return resultat;
    }

    @Override
    public List<RDV> consulterHistoriquePatient(Long idPatient) {
        return rdvRepo.findByPatientId(idPatient);
    }

    @Override
    public void ajouterEnListeAttente(Patient patient, LocalDate dateSouhaitee) {
        listeAttente.putIfAbsent(dateSouhaitee, new ArrayList<>());
        listeAttente.get(dateSouhaitee).add(patient);
        System.out.println("Patient " + patient.getNom() + " ajouté en liste d'attente pour le " + dateSouhaitee);
    }

    @Override
    public List<Patient> getListeAttente(LocalDate date) {
        return listeAttente.getOrDefault(date, new ArrayList<>());
    }

    // --- Méthodes Privées (Logique Interne) ---

    private void verifierDisponibilite(Long idMedecin, LocalDate date, LocalTime heure) throws ServiceException {
        List<RDV> planning = consulterPlanningMedecin(idMedecin, date);
        for(RDV r : planning) {
            if(r.getHeure().equals(heure)) {
                throw new ServiceException("Créneau indisponible : Médecin occupé.");
            }
        }
    }

    private void envoyerEmailConfirmation(RDV rdv) {
        // Simulation d'envoi d'email
        System.out.println("[EMAIL] Envoyé au patient " + rdv.getIdPatient() +
                " : Votre RDV du " + rdv.getDate() + " est confirmé.");
    }

    private void verifierListeAttente(LocalDate dateLiberee) {
        if(listeAttente.containsKey(dateLiberee) && !listeAttente.get(dateLiberee).isEmpty()) {
            System.out.println("[INFO] Une place s'est libérée le " + dateLiberee +
                    ". Contacter les patients en liste d'attente.");
        }
    }
}
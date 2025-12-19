package ma.dentalTech.service.modules.agenda.impl;
import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.entities.agenda.AgendaMensuel;
import ma.dentalTech.entities.dossierMedical.RDV;
import ma.dentalTech.entities.enums.StatutRDV;
import ma.dentalTech.repository.modules.agenda.api.AgendaRepository;
import ma.dentalTech.repository.modules.agenda.impl.mySQL.AgendaRepositoryImpl;
import ma.dentalTech.repository.modules.dossierMedical.api.RDVRepository;
import ma.dentalTech.repository.modules.dossierMedical.impl.mySQL.RDVRepositoryImpl;
import ma.dentalTech.service.modules.agenda.api.AgendaService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AgendaServiceImpl implements AgendaService {


    private final RDVRepository rdvRepo = new RDVRepositoryImpl();

    private final AgendaRepository agendaRepo = new AgendaRepositoryImpl();

    //partie1:gestion des RDV
    @Override
    public void programmerRDV(RDV rdv) throws ServiceException {

        if (rdv.getDate() == null || rdv.getHeure() == null) { //validation
            throw new ServiceException("Date et heure obligatoires.");
        }
        if (rdv.getDate().isBefore(LocalDate.now())) {
            throw new ServiceException("Impossible de prendre un RDV dans le passé.");
        }


        verifierDisponibilite(rdv.getDate(), rdv.getHeure());//verification disponibilite


        rdv.setStatut(StatutRDV.PLANIFIE);//sauvegarde
        try {
            rdvRepo.create(rdv);
        } catch (Exception e) {
            throw new ServiceException("Erreur technique lors de la création du RDV : " + e.getMessage());
        }
    }

    @Override
    public void modifierRDV(RDV rdv) throws ServiceException {
        if (rdv.getIdRDV() == null) throw new ServiceException("ID RDV manquant.");

        try {
            rdvRepo.update(rdv);
        } catch (Exception e) {
            throw new ServiceException("Erreur update RDV : " + e.getMessage());
        }
    }

    @Override
    public void annulerRDV(Long idRDV) throws ServiceException {
        RDV rdv = rdvRepo.findById(idRDV);
        if (rdv == null) throw new ServiceException("RDV introuvable");

        rdv.setStatut(StatutRDV.ANNULE);
        rdvRepo.update(rdv);
    }

    @Override
    public List<RDV> getRDVsDuJour(LocalDate date) throws ServiceException {
        return rdvRepo.findByDate(date);
    }

    @Override
    public List<RDV> getHistoriquePatient(Long idPatient) throws ServiceException {
        return rdvRepo.findByPatientId(idPatient);
    }



    // PARTIE 2 : GESTION AGENDA MENSUEL
    @Override
    public void creerAgendaMensuel(AgendaMensuel agenda) throws ServiceException {
        if (agenda.getMois() == null || agenda.getMois().isEmpty()) {
            throw new ServiceException("Le mois est obligatoire (ex: 'Octobre 2023').");
        }
        // Ici on utilise ton AgendaRepositoryImpl
        agendaRepo.create(agenda);
    }

    @Override
    public List<AgendaMensuel> getAgendasMedecin(Long idMedecin) throws ServiceException {
        return agendaRepo.findByMedecin(idMedecin);
    }



    // METHODES PRIVEES
    private void verifierDisponibilite(LocalDate date, LocalTime heure) throws ServiceException {
        List<RDV> rdvs = rdvRepo.findByDate(date);
        for (RDV r : rdvs) {
            // Si on a déjà un RDV à la même heure et qu'il n'est pas annulé
            if (r.getHeure().equals(heure) && r.getStatut() != StatutRDV.ANNULE) {
                throw new ServiceException("Créneau indisponible à " + heure);
            }
        }
    }
}

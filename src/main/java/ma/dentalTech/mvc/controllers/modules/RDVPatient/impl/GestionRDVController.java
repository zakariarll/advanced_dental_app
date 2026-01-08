package ma.dentalTech.mvc.controllers.modules.RDVPatient.impl;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.dossierMedical.RDV;
import ma.dentalTech.entities.patient.Patient;
import ma.dentalTech.mvc.controllers.modules.RDVPatient.api.IGestionRDVController;
import ma.dentalTech.service.modules.RDVPatient.api.IGestionRDVService;

import javax.swing.*; // Pour les messages d'erreur (Optionnel ici, souvent fait dans la Vue)
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

public class GestionRDVController implements IGestionRDVController {

    private final IGestionRDVService service;

    // Injection du Service via le constructeur
    public GestionRDVController(IGestionRDVService service) {
        this.service = service;
    }

    @Override
    public void planifierRDV(Long idPatient, Long idMedecin, LocalDate date, LocalTime heure, String motif) {
        try {
            RDV rdv = service.planifierRDV(idPatient, idMedecin, date, heure, motif);
            System.out.println("[CONTROLLER] RDV planifié avec succès : ID " + rdv.getIdRDV());
            // Ici, on pourrait déclencher un rafraîchissement de la vue
        } catch (ValidationException e) {
            System.err.println("[CONTROLLER-VALIDATION] " + e.getMessage());
            // En Swing réel : JOptionPane.showMessageDialog(null, e.getMessage(), "Erreur de validation", JOptionPane.WARNING_MESSAGE);
        } catch (ServiceException e) {
            System.err.println("[CONTROLLER-ERROR] " + e.getMessage());
            // En Swing réel : JOptionPane.showMessageDialog(null, e.getMessage(), "Erreur technique", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void confirmerRDV(Long idRDV) {
        try {
            service.confirmerRDV(idRDV);
            System.out.println("[CONTROLLER] RDV confirmé.");
        } catch (Exception e) {
            System.err.println("[CONTROLLER-ERROR] " + e.getMessage());
        }
    }

    @Override
    public void annulerRDV(Long idRDV, String motif) {
        try {
            service.annulerRDV(idRDV, motif);
            System.out.println("[CONTROLLER] RDV annulé.");
        } catch (Exception e) {
            System.err.println("[CONTROLLER-ERROR] " + e.getMessage());
        }
    }

    @Override
    public void modifierRDV(Long idRDV, LocalDate date, LocalTime heure) {
        try {
            service.modifierRDV(idRDV, date, heure);
            System.out.println("[CONTROLLER] RDV modifié.");
        } catch (Exception e) {
            System.err.println("[CONTROLLER-ERROR] " + e.getMessage());
        }
    }

    @Override
    public List<RDV> chargerPlanning(Long idMedecin, LocalDate date) {
        try {
            return service.consulterPlanningMedecin(idMedecin, date);
        } catch (ServiceException e) {
            System.err.println("[CONTROLLER-ERROR] Impossible de charger le planning : " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public List<RDV> chargerHistoriquePatient(Long idPatient) {
        try {
            return service.consulterHistoriquePatient(idPatient);
        } catch (ServiceException e) {
            System.err.println("[CONTROLLER-ERROR] " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public List<Patient> voirListeAttente(LocalDate date) {
        return service.getListeAttente(date);
    }
}
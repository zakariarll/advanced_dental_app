package ma.dentalTech.mvc.controllers.modules.RDVPatient.api;

import ma.dentalTech.entities.dossierMedical.RDV;
import ma.dentalTech.entities.patient.Patient;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface IGestionRDVController {

    // Action : Clic sur bouton "Enregistrer" (Formulaire RDV)
    void planifierRDV(Long idPatient, Long idMedecin, LocalDate date, LocalTime heure, String motif);

    // Action : Clic sur bouton "Confirmer" (Tableau ou Détail)
    void confirmerRDV(Long idRDV);

    // Action : Clic sur bouton "Annuler"
    void annulerRDV(Long idRDV, String motif);

    // Action : Clic sur bouton "Modifier"
    void modifierRDV(Long idRDV, LocalDate date, LocalTime heure);

    // Action : Chargement de l'Agenda (Vue Agenda)
    List<RDV> chargerPlanning(Long idMedecin, LocalDate date);

    // Action : Chargement historique Patient (Vue Patient)
    List<RDV> chargerHistoriquePatient(Long idPatient);

    // Action : Vérifier liste d'attente
    List<Patient> voirListeAttente(LocalDate date);
}
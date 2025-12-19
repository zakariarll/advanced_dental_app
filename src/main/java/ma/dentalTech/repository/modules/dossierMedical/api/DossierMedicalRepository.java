package ma.dentalTech.repository.modules.dossierMedical.api;

import ma.dentalTech.entities.dossierMedical.DossierMedical;

public interface DossierMedicalRepository {

    // Construit l'objet complet (Patient + RDVs + Antécédents + Consultations)
    DossierMedical getDossierComplet(Long idPatient);
}
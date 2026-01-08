package ma.dentalTech.service.modules.ordonnance.api;

import ma.dentalTech.entities.ordonnance.Ordonnance;
import ma.dentalTech.entities.ordonnance.Prescription;
import ma.dentalTech.service.common.CrudService;
import java.util.List;

public interface OrdonnanceService extends CrudService<Ordonnance, Long> {

    // Méthode métier complexe : Crée l'entête et les lignes
    void creerOrdonnanceComplete(Ordonnance ordonnance, List<Prescription> prescriptions);

    List<Ordonnance> getHistoriquePatient(Long idPatient);

    // Pour récupérer le détail d'une ordonnance
    List<Prescription> getPrescriptionsByOrdonnance(Long idOrdonnance);
}
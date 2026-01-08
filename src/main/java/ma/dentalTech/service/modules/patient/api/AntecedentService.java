package ma.dentalTech.service.modules.patient.api;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.entities.patient.Antecedent;
import java.util.List;

public interface AntecedentService {

    void ajouterAntecedent(Antecedent antecedent) throws ServiceException;

    void modifierAntecedent(Antecedent antecedent) throws ServiceException;

    void supprimerAntecedent(Long id) throws ServiceException;

    // Récupérer tous les antécédents d'un patient spécifique
    List<Antecedent> getAntecedentsByPatient(Long idPatient) throws ServiceException;
}
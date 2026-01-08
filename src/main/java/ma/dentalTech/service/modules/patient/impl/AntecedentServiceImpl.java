package ma.dentalTech.service.modules.patient.impl;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.entities.patient.Antecedent;
import ma.dentalTech.repository.modules.patient.api.AntecedentRepository;
import ma.dentalTech.repository.modules.patient.impl.mySQL.AntecedentRepositoryImpl;
import ma.dentalTech.service.modules.patient.api.AntecedentService;

import java.util.List;

public class AntecedentServiceImpl implements AntecedentService {

    private final AntecedentRepository antecedentRepo = new AntecedentRepositoryImpl();

    @Override
    public void ajouterAntecedent(Antecedent a) throws ServiceException {
        if (a.getNom() == null || a.getNom().isEmpty()) {
            throw new ServiceException("Le nom de l'antécédent est obligatoire (Ex: Allergie Pénicilline).");
        }
        if (a.getIdPatient() == null) {
            throw new ServiceException("Cet antécédent doit être lié à un patient.");
        }


        antecedentRepo.create(a);
    }

    @Override
    public void modifierAntecedent(Antecedent a) throws ServiceException {
        if (a.getIdAntecedent() == null) throw new ServiceException("ID Antécédent manquant");
        antecedentRepo.update(a);
    }

    @Override
    public void supprimerAntecedent(Long id) throws ServiceException {
        antecedentRepo.deleteById(id);
    }

    @Override
    public List<Antecedent> getAntecedentsByPatient(Long idPatient) throws ServiceException {
        if(idPatient == null) throw new ServiceException("ID Patient invalide");
        return antecedentRepo.findByPatientId(idPatient);
    }
}
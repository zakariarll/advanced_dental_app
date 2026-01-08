package ma.dentalTech.service.modules.certificat.impl;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.entities.certificat.Certificat;
import ma.dentalTech.repository.modules.certificat.api.CertificatRepository;
import ma.dentalTech.repository.modules.certificat.impl.mySQL.CertificatRepositoryImpl;
import ma.dentalTech.service.modules.certificat.api.CertificatService;

import java.time.LocalDate;
import java.util.List;

public class CertificatServiceImpl implements CertificatService {


    private final CertificatRepository certificatRepo = new CertificatRepositoryImpl();

    @Override
    public void genererCertificat(Certificat c) throws ServiceException {
        // Validation des données
        if (c.getIdPatient() == null) {
            throw new ServiceException("Le certificat doit être lié à un patient.");
        }
        if (c.getDuree() == null || c.getDuree() <= 0) {
            throw new ServiceException("La durée de repos est obligatoire et doit être positive.");
        }

        //Initialisation Date Début
        if (c.getDateDebut() == null) {
            c.setDateDebut(LocalDate.now());
        }

        // 3. LOGIQUE METIER Calcul automatique de la Date de Fin

        LocalDate dateFinCalculee = c.getDateDebut().plusDays(c.getDuree());
        c.setDateFin(dateFinCalculee);

        // 4. Appel au Repository
        try {
            certificatRepo.create(c);
        } catch (Exception e) {
            throw new ServiceException("Erreur technique lors de la création du certificat : " + e.getMessage());
        }
    }

    @Override
    public List<Certificat> getCertificatsPatient(Long idPatient) throws ServiceException {
        if (idPatient == null) throw new ServiceException("ID Patient manquant.");
        return certificatRepo.findByPatientId(idPatient);
    }

    @Override
    public void supprimerCertificat(Long idCertificat) throws ServiceException {
        if (idCertificat == null) throw new ServiceException("ID Certificat manquant.");
        try {
            certificatRepo.deleteById(idCertificat);
        } catch (Exception e) {
            throw new ServiceException("Impossible de supprimer le certificat.");
        }
    }
}

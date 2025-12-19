package ma.dentalTech.service.modules.certificat.api;
import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.entities.certificat.Certificat;

import java.util.List;
public interface CertificatService {

    // creation certif,calcul auto du dateFin
    void genererCertificat(Certificat certificat) throws ServiceException;

    //Récupère l'historique
    List<Certificat> getCertificatsPatient(Long idPatient) throws ServiceException;

    void supprimerCertificat(Long idCertificat) throws ServiceException;

}

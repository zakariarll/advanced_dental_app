package ma.dentalTech.repository.modules.certificat.api;
import ma.dentalTech.entities.certificat.Certificat;
import ma.dentalTech.repository.common.CrudRepository;
import java.util.List;

public interface CertificatRepository extends CrudRepository<Certificat, Long> {
    List<Certificat> findByPatientId(Long idPatient);
}
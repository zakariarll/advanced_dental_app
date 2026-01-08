package ma.dentalTech.repository.modules.dossierMedical.impl.mySQL;

import ma.dentalTech.entities.dossierMedical.DossierMedical;
import ma.dentalTech.entities.patient.Patient;
import ma.dentalTech.repository.modules.dossierMedical.api.DossierMedicalRepository;
import ma.dentalTech.repository.modules.dossierMedical.api.RDVRepository;
import ma.dentalTech.repository.modules.patient.api.AntecedentRepository;
import ma.dentalTech.repository.modules.patient.api.PatientRepository;

public class DossierMedicalRepositoryImpl implements DossierMedicalRepository {

    private final PatientRepository patientRepo;
    private final AntecedentRepository antecedentRepo;
    private final RDVRepository rdvRepo;

    // CHANGEMENT : Injection par constructeur (IoC propre)
    public DossierMedicalRepositoryImpl(PatientRepository patientRepo,
                                        AntecedentRepository antecedentRepo,
                                        RDVRepository rdvRepo) {
        this.patientRepo = patientRepo;
        this.antecedentRepo = antecedentRepo;
        this.rdvRepo = rdvRepo;
    }

    @Override
    public DossierMedical getDossierComplet(Long idPatient) {
        Patient p = patientRepo.findById(idPatient);
        if (p == null) return null;

        return DossierMedical.builder()
                .patient(p)
                .antecedents(antecedentRepo.findByPatientId(idPatient))
                .rdvs(rdvRepo.findByPatientId(idPatient))
                .build();
    }
}
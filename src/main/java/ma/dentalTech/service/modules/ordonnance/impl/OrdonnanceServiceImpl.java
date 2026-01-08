package ma.dentalTech.service.modules.ordonnance.impl;

import ma.dentalTech.entities.ordonnance.Ordonnance;
import ma.dentalTech.entities.ordonnance.Prescription;
import ma.dentalTech.repository.modules.ordonnance.api.OrdonnanceRepository;
import ma.dentalTech.repository.modules.ordonnance.api.PrescriptionRepository;
import ma.dentalTech.service.common.ServiceException;
import ma.dentalTech.service.modules.ordonnance.api.OrdonnanceService;

import java.time.LocalDate;
import java.util.List;

public class OrdonnanceServiceImpl implements OrdonnanceService {

    private final OrdonnanceRepository ordonnanceRepo;
    private final PrescriptionRepository prescriptionRepo;


    public OrdonnanceServiceImpl(OrdonnanceRepository ordonnanceRepo, PrescriptionRepository prescriptionRepo) {
        this.ordonnanceRepo = ordonnanceRepo;
        this.prescriptionRepo = prescriptionRepo;
    }

    @Override
    public void creerOrdonnanceComplete(Ordonnance ord, List<Prescription> prescriptions) {
        // 1. Validations
        if (prescriptions == null || prescriptions.isEmpty()) {
            throw new ServiceException("Impossible de créer une ordonnance vide.");
        }
        if (ord.getIdPatient() == null) {
            throw new ServiceException("L'ordonnance doit être liée à un patient.");
        }

        // Règle métier : Date par défaut aujourd'hui si vide
        if (ord.getDate() == null) {
            ord.setDate(LocalDate.now());
        }

        try {
            // 2. Création de l'entête (Ordonnance)
            // Cela va générer l'ID de l'ordonnance dans l'objet 'ord'
            ordonnanceRepo.create(ord);

            if (ord.getIdOrd() == null) {
                throw new ServiceException("Erreur critique : L'ID de l'ordonnance n'a pas été généré.");
            }

            // 3. Création des lignes (Prescriptions)
            for (Prescription p : prescriptions) {
                // On lie la ligne à l'ordonnance qui vient d'être créée
                p.setIdOrd(ord.getIdOrd());

                // Validation ligne
                if(p.getQuantite() <= 0) throw new ServiceException("Quantité invalide pour le médicament ID: " + p.getIdMct());

                prescriptionRepo.create(p);
            }

        } catch (Exception e) {
            // Gestion d'erreur
            throw new ServiceException("Erreur lors de la création de l'ordonnance : " + e.getMessage(), e);
        }
    }

    @Override
    public List<Ordonnance> getAll() {
        return ordonnanceRepo.findAll();
    }

    @Override
    public Ordonnance getById(Long id) {
        return ordonnanceRepo.findById(id);
    }

    @Override
    public void create(Ordonnance entity) {
        // Utiliser creerOrdonnanceComplete de préférence, mais méthode requise par CrudService
        ordonnanceRepo.create(entity);
    }

    @Override
    public void update(Ordonnance entity) {
        ordonnanceRepo.update(entity);
    }

    @Override
    public void deleteById(Long id) {
        // Logique de suppression gérant la contrainte de clé étrangère
        List<Prescription> lignes = prescriptionRepo.findByOrdonnanceId(id);
        for(Prescription p : lignes) {
            prescriptionRepo.deleteById(p.getIdPr());
        }
        ordonnanceRepo.deleteById(id);
    }

    @Override
    public List<Ordonnance> getHistoriquePatient(Long idPatient) {
        return ordonnanceRepo.findByPatientId(idPatient);
    }

    @Override
    public List<Prescription> getPrescriptionsByOrdonnance(Long idOrdonnance) {
        return prescriptionRepo.findByOrdonnanceId(idOrdonnance);
    }
}
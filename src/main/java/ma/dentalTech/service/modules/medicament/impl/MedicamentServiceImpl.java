package ma.dentalTech.service.modules.medicament.impl;

import ma.dentalTech.entities.medicament.Medicament;
import ma.dentalTech.repository.modules.medicament.api.MedicamentRepository;
import ma.dentalTech.service.common.ServiceException;
import ma.dentalTech.service.modules.medicament.api.MedicamentService;

import java.util.List;
import java.util.stream.Collectors;

public class MedicamentServiceImpl implements MedicamentService {

    private final MedicamentRepository medicamentRepo;

    public MedicamentServiceImpl(MedicamentRepository medicamentRepo) {
        this.medicamentRepo = medicamentRepo;
    }

    @Override
    public List<Medicament> getAll() {
        return medicamentRepo.findAll();
    }

    @Override
    public Medicament getById(Long id) {
        Medicament m = medicamentRepo.findById(id);
        if (m == null) throw new ServiceException("Médicament introuvable avec l'ID : " + id);
        return m;
    }

    @Override
    public void create(Medicament m) {
        // Validation Métier (Restauration de la logique originale)
        if (m.getPrixUnitaire() < 0) {
            throw new ServiceException("Le prix d'un médicament ne peut pas être négatif.");
        }
        if (m.getNom() == null || m.getNom().trim().isEmpty()) {
            throw new ServiceException("Le nom du médicament est obligatoire.");
        }
        medicamentRepo.create(m);
    }

    @Override
    public void update(Medicament m) {
        // On vérifie d'abord que le médicament existe
        if (getById(m.getIdMct()) != null) {
            medicamentRepo.update(m);
        }
    }

    @Override
    public void deleteById(Long id) {
        // Logique conservée : la suppression peut être déléguée directement au Repository
        medicamentRepo.deleteById(id);
    }

    @Override
    public List<Medicament> searchByNom(String nom) {
        return medicamentRepo.findByNomLike(nom);
    }

    @Override
    public List<Medicament> getRemboursablesOnly() {
        // Implémentation basée sur l'original (filtrage Stream ou appel Repository spécifique)
        return medicamentRepo.findAll().stream()
                .filter(Medicament::getRemboursable)
                .collect(Collectors.toList());
    }
}
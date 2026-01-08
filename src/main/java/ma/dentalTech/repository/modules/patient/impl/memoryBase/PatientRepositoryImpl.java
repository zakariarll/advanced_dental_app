package ma.dentalTech.repository.modules.patient.impl.memoryBase;

import ma.dentalTech.entities.patient.Patient;
import ma.dentalTech.repository.modules.patient.api.PatientRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class PatientRepositoryImpl implements PatientRepository {

    private final List<Patient> data = new ArrayList<>();
    private long idCounter = 1;

    public PatientRepositoryImpl() {
        // Jeu de données de test
    }

    @Override
    public List<Patient> findAll() {
        return new ArrayList<>(data);
    }

    @Override
    public Patient findById(Long id) {
        return data.stream()
                .filter(p -> p.getIdPatient().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void create(Patient p) {
        p.setIdPatient(idCounter++);
        p.setDateCreation(LocalDate.now());
        data.add(p);
    }

    @Override
    public void update(Patient p) {
        deleteById(p.getIdPatient());
        data.add(p);
        // On garde le tri par ID
        data.sort(Comparator.comparing(Patient::getIdPatient));
    }

    @Override
    public void delete(Patient p) {
        deleteById(p.getIdPatient());
    }

    @Override
    public void deleteById(Long id) {
        data.removeIf(p -> p.getIdPatient().equals(id));
    }

    @Override
    public Optional<Patient> findByTelephone(String telephone) {
        return data.stream()
                .filter(p -> telephone.equals(p.getTelephone()))
                .findFirst();
    }

    @Override
    public List<Patient> searchByNomOrPrenom(String keyword) {
        String k = keyword.toLowerCase();
        return data.stream()
                .filter(p -> p.getNom().toLowerCase().contains(k) || p.getPrenom().toLowerCase().contains(k))
                .collect(Collectors.toList());
    }

    @Override
    public List<Patient> findPage(int limit, int offset) {
        return data.stream()
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return data.size();
    }
}
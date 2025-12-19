package ma.dentalTech.service.actes;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.actes.Acte;
import ma.dentalTech.entities.actes.ActeIntervention;
import ma.dentalTech.repository.modules.actes.api.ActeInterventionRepository;
import ma.dentalTech.repository.modules.actes.api.ActeRepository;
import ma.dentalTech.service.modules.actes.impl.ActeInterventionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class ActeInterventionServiceTest {

    private ActeInterventionServiceImpl acteInterventionService;
    private MockActeInterventionRepository mockActeInterventionRepository;
    private MockActeRepository mockActeRepository;

    @BeforeEach
    void setUp() {
        mockActeInterventionRepository = new MockActeInterventionRepository();
        mockActeRepository = new MockActeRepository();
        acteInterventionService = new ActeInterventionServiceImpl(mockActeInterventionRepository, mockActeRepository);
    }

    @Test
    @DisplayName("Ajouter un acte à une intervention")
    void ajouterActeAIntervention() throws ServiceException, ValidationException {
        Acte acte = creerActeTest();
        mockActeRepository.create(acte);

        ActeIntervention result = acteInterventionService.ajouterActeAIntervention(acte.getIdActe(), 1L);

        assertThat(result).isNotNull();
        assertThat(result.getIdActeIntervention()).isNotNull();
        assertThat(result.getIdActe()).isEqualTo(acte.getIdActe());
        assertThat(result.getIdIM()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Ajouter un acte sans ID acte doit échouer")
    void ajouterActeSansIdActe() {
        assertThatThrownBy(() -> acteInterventionService.ajouterActeAIntervention(null, 1L))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("acte");
    }

    @Test
    @DisplayName("Ajouter un acte sans ID intervention doit échouer")
    void ajouterActeSansIdIntervention() {
        assertThatThrownBy(() -> acteInterventionService.ajouterActeAIntervention(1L, null))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("intervention");
    }

    @Test
    @DisplayName("Ajouter un acte inexistant doit échouer")
    void ajouterActeInexistant() {
        assertThatThrownBy(() -> acteInterventionService.ajouterActeAIntervention(999L, 1L))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("introuvable");
    }

    @Test
    @DisplayName("Ajouter un acte déjà associé doit échouer")
    void ajouterActeDejaAssocie() throws ServiceException, ValidationException {
        Acte acte = creerActeTest();
        mockActeRepository.create(acte);
        acteInterventionService.ajouterActeAIntervention(acte.getIdActe(), 1L);

        assertThatThrownBy(() -> acteInterventionService.ajouterActeAIntervention(acte.getIdActe(), 1L))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("déjà associé");
    }

    @Test
    @DisplayName("Retirer un acte d'une intervention")
    void retirerActeDeIntervention() throws ServiceException, ValidationException {
        Acte acte = creerActeTest();
        mockActeRepository.create(acte);
        ActeIntervention ai = acteInterventionService.ajouterActeAIntervention(acte.getIdActe(), 1L);

        acteInterventionService.retirerActeDeIntervention(ai.getIdActeIntervention());

        assertThat(mockActeInterventionRepository.findById(ai.getIdActeIntervention())).isNull();
    }

    @Test
    @DisplayName("Retirer un acte inexistant doit échouer")
    void retirerActeInexistant() {
        assertThatThrownBy(() -> acteInterventionService.retirerActeDeIntervention(999L))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("introuvable");
    }

    @Test
    @DisplayName("Retirer un acte avec ID null doit échouer")
    void retirerActeIdNull() {
        assertThatThrownBy(() -> acteInterventionService.retirerActeDeIntervention(null))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("obligatoire");
    }

    @Test
    @DisplayName("Retirer tous les actes d'une intervention")
    void retirerTousLesActesDeIntervention() throws ServiceException, ValidationException {
        Acte acte1 = creerActeTest();
        mockActeRepository.create(acte1);

        Acte acte2 = Acte.builder()
                .libelle("Extraction")
                .categorie("SOINS")
                .prixDeBase(500.0)
                .code(102)
                .build();
        mockActeRepository.create(acte2);

        acteInterventionService.ajouterActeAIntervention(acte1.getIdActe(), 1L);
        acteInterventionService.ajouterActeAIntervention(acte2.getIdActe(), 1L);

        acteInterventionService.retirerTousLesActesDeIntervention(1L);

        List<ActeIntervention> result = acteInterventionService.listerActesParIntervention(1L);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Lister les actes par intervention")
    void listerActesParIntervention() throws ServiceException, ValidationException {
        Acte acte1 = creerActeTest();
        mockActeRepository.create(acte1);

        Acte acte2 = Acte.builder()
                .libelle("Extraction")
                .categorie("SOINS")
                .prixDeBase(500.0)
                .code(102)
                .build();
        mockActeRepository.create(acte2);

        acteInterventionService.ajouterActeAIntervention(acte1.getIdActe(), 1L);
        acteInterventionService.ajouterActeAIntervention(acte2.getIdActe(), 1L);

        List<ActeIntervention> result = acteInterventionService.listerActesParIntervention(1L);

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Lister les actes par intervention avec ID null doit échouer")
    void listerActesParInterventionIdNull() {
        assertThatThrownBy(() -> acteInterventionService.listerActesParIntervention(null))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("obligatoire");
    }

    @Test
    @DisplayName("Obtenir les détails des actes par intervention")
    void obtenirActesDetailsParIntervention() throws ServiceException, ValidationException {
        Acte acte1 = creerActeTest();
        mockActeRepository.create(acte1);

        Acte acte2 = Acte.builder()
                .libelle("Extraction")
                .categorie("SOINS")
                .prixDeBase(500.0)
                .code(102)
                .build();
        mockActeRepository.create(acte2);

        acteInterventionService.ajouterActeAIntervention(acte1.getIdActe(), 1L);
        acteInterventionService.ajouterActeAIntervention(acte2.getIdActe(), 1L);

        List<Acte> result = acteInterventionService.obtenirActesDetailsParIntervention(1L);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Acte::getLibelle).contains("Détartrage", "Extraction");
    }

    @Test
    @DisplayName("Calculer le montant total d'une intervention")
    void calculerMontantTotalIntervention() throws ServiceException, ValidationException {
        Acte acte1 = creerActeTest();
        acte1.setPrixDeBase(300.0);
        mockActeRepository.create(acte1);

        Acte acte2 = Acte.builder()
                .libelle("Extraction")
                .categorie("SOINS")
                .prixDeBase(500.0)
                .code(102)
                .build();
        mockActeRepository.create(acte2);

        acteInterventionService.ajouterActeAIntervention(acte1.getIdActe(), 1L);
        acteInterventionService.ajouterActeAIntervention(acte2.getIdActe(), 1L);

        Double result = acteInterventionService.calculerMontantTotalIntervention(1L);

        assertThat(result).isEqualTo(800.0);
    }

    @Test
    @DisplayName("Calculer le montant total d'une intervention sans actes")
    void calculerMontantTotalInterventionSansActes() throws ServiceException {
        Double result = acteInterventionService.calculerMontantTotalIntervention(1L);

        assertThat(result).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Compter les utilisations d'un acte")
    void compterUtilisationsActe() throws ServiceException, ValidationException {
        Acte acte = creerActeTest();
        mockActeRepository.create(acte);

        acteInterventionService.ajouterActeAIntervention(acte.getIdActe(), 1L);
        acteInterventionService.ajouterActeAIntervention(acte.getIdActe(), 2L);
        acteInterventionService.ajouterActeAIntervention(acte.getIdActe(), 3L);

        int result = acteInterventionService.compterUtilisationsActe(acte.getIdActe());

        assertThat(result).isEqualTo(3);
    }

    @Test
    @DisplayName("Vérifier si un acte est utilisé dans une intervention")
    void acteEstUtiliseDansIntervention() throws ServiceException, ValidationException {
        Acte acte = creerActeTest();
        mockActeRepository.create(acte);
        acteInterventionService.ajouterActeAIntervention(acte.getIdActe(), 1L);

        assertThat(acteInterventionService.acteEstUtiliseDansIntervention(acte.getIdActe(), 1L)).isTrue();
        assertThat(acteInterventionService.acteEstUtiliseDansIntervention(acte.getIdActe(), 2L)).isFalse();
    }

    @Test
    @DisplayName("Lister les interventions par acte")
    void listerInterventionsParActe() throws ServiceException, ValidationException {
        Acte acte = creerActeTest();
        mockActeRepository.create(acte);

        acteInterventionService.ajouterActeAIntervention(acte.getIdActe(), 1L);
        acteInterventionService.ajouterActeAIntervention(acte.getIdActe(), 2L);

        List<ActeIntervention> result = acteInterventionService.listerInterventionsParActe(acte.getIdActe());

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Lister les interventions par acte avec ID null doit échouer")
    void listerInterventionsParActeIdNull() {
        assertThatThrownBy(() -> acteInterventionService.listerInterventionsParActe(null))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("obligatoire");
    }

    @Test
    @DisplayName("Ajouter plusieurs actes à la même intervention")
    void ajouterPlusieursActesMemeIntervention() throws ServiceException, ValidationException {
        Acte acte1 = creerActeTest();
        mockActeRepository.create(acte1);

        Acte acte2 = Acte.builder()
                .libelle("Extraction")
                .categorie("SOINS")
                .prixDeBase(500.0)
                .code(102)
                .build();
        mockActeRepository.create(acte2);

        Acte acte3 = Acte.builder()
                .libelle("Plombage")
                .categorie("SOINS")
                .prixDeBase(400.0)
                .code(103)
                .build();
        mockActeRepository.create(acte3);

        acteInterventionService.ajouterActeAIntervention(acte1.getIdActe(), 1L);
        acteInterventionService.ajouterActeAIntervention(acte2.getIdActe(), 1L);
        acteInterventionService.ajouterActeAIntervention(acte3.getIdActe(), 1L);

        List<ActeIntervention> result = acteInterventionService.listerActesParIntervention(1L);
        assertThat(result).hasSize(3);

        Double montant = acteInterventionService.calculerMontantTotalIntervention(1L);
        assertThat(montant).isEqualTo(1200.0);
    }

    private Acte creerActeTest() {
        return Acte.builder()
                .libelle("Détartrage")
                .categorie("SOINS")
                .prixDeBase(300.0)
                .code(101)
                .description("Nettoyage dentaire")
                .build();
    }

    private static class MockActeInterventionRepository implements ActeInterventionRepository {
        private final List<ActeIntervention> actesInterventions = new ArrayList<>();
        private Long nextId = 1L;

        @Override
        public List<ActeIntervention> findAll() {
            return new ArrayList<>(actesInterventions);
        }

        @Override
        public ActeIntervention findById(Long id) {
            return actesInterventions.stream().filter(ai -> ai.getIdActeIntervention().equals(id)).findFirst().orElse(null);
        }

        @Override
        public void create(ActeIntervention ai) {
            ai.setIdActeIntervention(nextId++);
            actesInterventions.add(ai);
        }

        @Override
        public void update(ActeIntervention ai) {
            actesInterventions.removeIf(a -> a.getIdActeIntervention().equals(ai.getIdActeIntervention()));
            actesInterventions.add(ai);
        }

        @Override
        public void delete(ActeIntervention ai) {
            actesInterventions.remove(ai);
        }

        @Override
        public void deleteById(Long id) {
            actesInterventions.removeIf(ai -> ai.getIdActeIntervention().equals(id));
        }

        @Override
        public List<ActeIntervention> findByActeId(Long idActe) {
            return actesInterventions.stream().filter(ai -> ai.getIdActe().equals(idActe)).toList();
        }

        @Override
        public List<ActeIntervention> findByInterventionMedecinId(Long idIM) {
            return actesInterventions.stream().filter(ai -> ai.getIdIM().equals(idIM)).toList();
        }

        @Override
        public void deleteByInterventionMedecinId(Long idIM) {
            actesInterventions.removeIf(ai -> ai.getIdIM().equals(idIM));
        }

        @Override
        public boolean existsByActeIdAndInterventionMedecinId(Long idActe, Long idIM) {
            return actesInterventions.stream().anyMatch(ai -> ai.getIdActe().equals(idActe) && ai.getIdIM().equals(idIM));
        }

        @Override
        public int countByActeId(Long idActe) {
            return (int) actesInterventions.stream().filter(ai -> ai.getIdActe().equals(idActe)).count();
        }
    }

    private static class MockActeRepository implements ActeRepository {
        private final List<Acte> actes = new ArrayList<>();
        private Long nextId = 1L;

        @Override
        public List<Acte> findAll() {
            return new ArrayList<>(actes);
        }

        @Override
        public Acte findById(Long id) {
            return actes.stream().filter(a -> a.getIdActe().equals(id)).findFirst().orElse(null);
        }

        @Override
        public void create(Acte acte) {
            acte.setIdActe(nextId++);
            actes.add(acte);
        }

        @Override
        public void update(Acte acte) {
            actes.removeIf(a -> a.getIdActe().equals(acte.getIdActe()));
            actes.add(acte);
        }

        @Override
        public void delete(Acte acte) {
            actes.remove(acte);
        }

        @Override
        public void deleteById(Long id) {
            actes.removeIf(a -> a.getIdActe().equals(id));
        }

        @Override
        public Optional<Acte> findByCode(Integer code) {
            return actes.stream().filter(a -> a.getCode().equals(code)).findFirst();
        }

        @Override
        public List<Acte> findByCategorie(String categorie) {
            return actes.stream().filter(a -> categorie.equals(a.getCategorie())).toList();
        }

        @Override
        public List<Acte> findByLibelleContaining(String libelle) {
            return actes.stream().filter(a -> a.getLibelle().contains(libelle)).toList();
        }

        @Override
        public List<Acte> findByPrixBetween(Double prixMin, Double prixMax) {
            return actes.stream().filter(a -> a.getPrixDeBase() >= prixMin && a.getPrixDeBase() <= prixMax).toList();
        }

        @Override
        public List<String> findAllCategories() {
            return actes.stream().map(Acte::getCategorie).distinct().toList();
        }

        @Override
        public boolean existsByCode(Integer code) {
            return actes.stream().anyMatch(a -> a.getCode().equals(code));
        }

        @Override
        public boolean existsByLibelle(String libelle) {
            return actes.stream().anyMatch(a -> a.getLibelle().equals(libelle));
        }
    }
}
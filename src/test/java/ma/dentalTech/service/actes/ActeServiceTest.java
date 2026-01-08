package ma.dentalTech.service.actes;

import ma.dentalTech.common.exceptions.ServiceException;
import ma.dentalTech.common.exceptions.ValidationException;
import ma.dentalTech.entities.actes.Acte;
import ma.dentalTech.repository.modules.actes.api.ActeRepository;
import ma.dentalTech.service.modules.actes.impl.ActeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class ActeServiceTest {

    private ActeServiceImpl acteService;
    private MockActeRepository mockRepository;

    @BeforeEach
    void setUp() {
        mockRepository = new MockActeRepository();
        acteService = new ActeServiceImpl(mockRepository);
    }

    @Test
    @DisplayName("Créer un acte valide")
    void creerActeValide() throws ServiceException, ValidationException {
        Acte acte = Acte.builder()
                .libelle("Détartrage")
                .categorie("SOINS")
                .prixDeBase(300.0)
                .code(101)
                .description("Nettoyage dentaire")
                .build();

        Acte result = acteService.creerActe(acte);

        assertThat(result).isNotNull();
        assertThat(result.getIdActe()).isNotNull();
        assertThat(result.getLibelle()).isEqualTo("Détartrage");
    }

    @Test
    @DisplayName("Créer un acte sans libellé doit échouer")
    void creerActeSansLibelle() {
        Acte acte = Acte.builder()
                .categorie("SOINS")
                .prixDeBase(300.0)
                .code(101)
                .build();

        assertThatThrownBy(() -> acteService.creerActe(acte))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("libellé");
    }

    @Test
    @DisplayName("Créer un acte avec libellé trop court doit échouer")
    void creerActeLibelleTropCourt() {
        Acte acte = Acte.builder()
                .libelle("AB")
                .categorie("SOINS")
                .prixDeBase(300.0)
                .code(101)
                .build();

        assertThatThrownBy(() -> acteService.creerActe(acte))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("3 caractères");
    }

    @Test
    @DisplayName("Créer un acte sans catégorie doit échouer")
    void creerActeSansCategorie() {
        Acte acte = Acte.builder()
                .libelle("Détartrage")
                .prixDeBase(300.0)
                .code(101)
                .build();

        assertThatThrownBy(() -> acteService.creerActe(acte))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("catégorie");
    }

    @Test
    @DisplayName("Créer un acte sans prix doit échouer")
    void creerActeSansPrix() {
        Acte acte = Acte.builder()
                .libelle("Détartrage")
                .categorie("SOINS")
                .code(101)
                .build();

        assertThatThrownBy(() -> acteService.creerActe(acte))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("prix");
    }

    @Test
    @DisplayName("Créer un acte avec prix négatif doit échouer")
    void creerActePrixNegatif() {
        Acte acte = Acte.builder()
                .libelle("Détartrage")
                .categorie("SOINS")
                .prixDeBase(-100.0)
                .code(101)
                .build();

        assertThatThrownBy(() -> acteService.creerActe(acte))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("négatif");
    }

    @Test
    @DisplayName("Créer un acte sans code doit échouer")
    void creerActeSansCode() {
        Acte acte = Acte.builder()
                .libelle("Détartrage")
                .categorie("SOINS")
                .prixDeBase(300.0)
                .build();

        assertThatThrownBy(() -> acteService.creerActe(acte))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("code");
    }

    @Test
    @DisplayName("Créer un acte avec code négatif doit échouer")
    void creerActeCodeNegatif() {
        Acte acte = Acte.builder()
                .libelle("Détartrage")
                .categorie("SOINS")
                .prixDeBase(300.0)
                .code(-1)
                .build();

        assertThatThrownBy(() -> acteService.creerActe(acte))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("positif");
    }

    @Test
    @DisplayName("Créer un acte avec code existant doit échouer")
    void creerActeCodeExistant() throws ServiceException, ValidationException {
        Acte acte1 = creerActeTest();
        acteService.creerActe(acte1);

        Acte acte2 = Acte.builder()
                .libelle("Autre acte")
                .categorie("SOINS")
                .prixDeBase(400.0)
                .code(101)
                .build();

        assertThatThrownBy(() -> acteService.creerActe(acte2))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("code existe");
    }

    @Test
    @DisplayName("Créer un acte avec libellé existant doit échouer")
    void creerActeLibelleExistant() throws ServiceException, ValidationException {
        Acte acte1 = creerActeTest();
        acteService.creerActe(acte1);

        Acte acte2 = Acte.builder()
                .libelle("Détartrage")
                .categorie("SOINS")
                .prixDeBase(400.0)
                .code(102)
                .build();

        assertThatThrownBy(() -> acteService.creerActe(acte2))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("libellé existe");
    }

    @Test
    @DisplayName("Modifier un acte valide")
    void modifierActeValide() throws ServiceException, ValidationException {
        Acte acte = creerActeTest();
        acteService.creerActe(acte);

        acte.setPrixDeBase(350.0);
        acte.setDescription("Nouvelle description");

        Acte result = acteService.modifierActe(acte);

        assertThat(result.getPrixDeBase()).isEqualTo(350.0);
        assertThat(result.getDescription()).isEqualTo("Nouvelle description");
    }

    @Test
    @DisplayName("Modifier un acte inexistant doit échouer")
    void modifierActeInexistant() {
        Acte acte = Acte.builder()
                .idActe(999L)
                .libelle("Test")
                .categorie("SOINS")
                .prixDeBase(300.0)
                .code(101)
                .build();

        assertThatThrownBy(() -> acteService.modifierActe(acte))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("introuvable");
    }

    @Test
    @DisplayName("Modifier un acte sans ID doit échouer")
    void modifierActeSansId() {
        Acte acte = creerActeTest();

        assertThatThrownBy(() -> acteService.modifierActe(acte))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("identifiant");
    }

    @Test
    @DisplayName("Supprimer un acte valide")
    void supprimerActeValide() throws ServiceException, ValidationException {
        Acte acte = creerActeTest();
        acteService.creerActe(acte);

        acteService.supprimerActe(acte.getIdActe());

        assertThat(acteService.obtenirActe(acte.getIdActe())).isNull();
    }

    @Test
    @DisplayName("Supprimer un acte inexistant doit échouer")
    void supprimerActeInexistant() {
        assertThatThrownBy(() -> acteService.supprimerActe(999L))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("introuvable");
    }

    @Test
    @DisplayName("Lister les actes par catégorie")
    void listerActesParCategorie() throws ServiceException, ValidationException {
        Acte a1 = creerActeTest();
        a1.setCategorie("SOINS");
        acteService.creerActe(a1);

        Acte a2 = Acte.builder()
                .libelle("Extraction")
                .categorie("SOINS")
                .prixDeBase(500.0)
                .code(102)
                .build();
        acteService.creerActe(a2);

        Acte a3 = Acte.builder()
                .libelle("Prothèse")
                .categorie("PROTHESE")
                .prixDeBase(2000.0)
                .code(201)
                .build();
        acteService.creerActe(a3);

        List<Acte> result = acteService.listerActesParCategorie("SOINS");

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(a -> "SOINS".equals(a.getCategorie()));
    }

    @Test
    @DisplayName("Rechercher actes par libellé")
    void rechercherActesParLibelle() throws ServiceException, ValidationException {
        Acte a1 = creerActeTest();
        a1.setLibelle("Détartrage simple");
        acteService.creerActe(a1);

        Acte a2 = Acte.builder()
                .libelle("Détartrage complet")
                .categorie("SOINS")
                .prixDeBase(400.0)
                .code(102)
                .build();
        acteService.creerActe(a2);

        Acte a3 = Acte.builder()
                .libelle("Extraction")
                .categorie("SOINS")
                .prixDeBase(500.0)
                .code(103)
                .build();
        acteService.creerActe(a3);

        List<Acte> result = acteService.rechercherActesParLibelle("Détartrage");

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(a -> a.getLibelle().contains("Détartrage"));
    }

    @Test
    @DisplayName("Lister actes par prix")
    void listerActesParPrix() throws ServiceException, ValidationException {
        Acte a1 = creerActeTest();
        a1.setPrixDeBase(300.0);
        acteService.creerActe(a1);

        Acte a2 = Acte.builder()
                .libelle("Extraction")
                .categorie("SOINS")
                .prixDeBase(500.0)
                .code(102)
                .build();
        acteService.creerActe(a2);

        Acte a3 = Acte.builder()
                .libelle("Prothèse")
                .categorie("PROTHESE")
                .prixDeBase(2000.0)
                .code(201)
                .build();
        acteService.creerActe(a3);

        List<Acte> result = acteService.listerActesParPrix(200.0, 600.0);

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Lister actes par prix avec min > max doit échouer")
    void listerActesParPrixInvalide() {
        assertThatThrownBy(() -> acteService.listerActesParPrix(500.0, 100.0))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("inférieur");
    }

    @Test
    @DisplayName("Appliquer une remise valide")
    void appliquerRemiseValide() throws ServiceException, ValidationException {
        Acte acte = creerActeTest();
        acte.setPrixDeBase(1000.0);
        acteService.creerActe(acte);

        Acte result = acteService.appliquerRemise(acte.getIdActe(), 10.0);

        assertThat(result.getPrixDeBase()).isEqualTo(900.0);
    }

    @Test
    @DisplayName("Appliquer une remise avec pourcentage invalide doit échouer")
    void appliquerRemisePourcentageInvalide() throws ServiceException, ValidationException {
        Acte acte = creerActeTest();
        acteService.creerActe(acte);

        assertThatThrownBy(() -> acteService.appliquerRemise(acte.getIdActe(), 150.0))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("entre 0 et 100");
    }

    @Test
    @DisplayName("Appliquer une remise avec pourcentage négatif doit échouer")
    void appliquerRemisePourcentageNegatif() throws ServiceException, ValidationException {
        Acte acte = creerActeTest();
        acteService.creerActe(acte);

        assertThatThrownBy(() -> acteService.appliquerRemise(acte.getIdActe(), -10.0))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("entre 0 et 100");
    }

    @Test
    @DisplayName("Modifier le prix d'un acte")
    void modifierPrix() throws ServiceException, ValidationException {
        Acte acte = creerActeTest();
        acteService.creerActe(acte);

        Acte result = acteService.modifierPrix(acte.getIdActe(), 450.0);

        assertThat(result.getPrixDeBase()).isEqualTo(450.0);
    }

    @Test
    @DisplayName("Modifier le prix avec valeur négative doit échouer")
    void modifierPrixNegatif() throws ServiceException, ValidationException {
        Acte acte = creerActeTest();
        acteService.creerActe(acte);

        assertThatThrownBy(() -> acteService.modifierPrix(acte.getIdActe(), -100.0))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("supérieur ou égal à 0");
    }

    @Test
    @DisplayName("Obtenir un acte par code")
    void obtenirActeParCode() throws ServiceException, ValidationException {
        Acte acte = creerActeTest();
        acte.setCode(999);
        acteService.creerActe(acte);

        Acte result = acteService.obtenirActeParCode(999);

        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo(999);
    }

    @Test
    @DisplayName("Lister toutes les catégories")
    void listerToutesLesCategories() throws ServiceException, ValidationException {
        Acte a1 = creerActeTest();
        a1.setCategorie("SOINS");
        acteService.creerActe(a1);

        Acte a2 = Acte.builder()
                .libelle("Prothèse")
                .categorie("PROTHESE")
                .prixDeBase(2000.0)
                .code(201)
                .build();
        acteService.creerActe(a2);

        Acte a3 = Acte.builder()
                .libelle("Implant")
                .categorie("IMPLANT")
                .prixDeBase(5000.0)
                .code(301)
                .build();
        acteService.creerActe(a3);

        List<String> result = acteService.listerToutesLesCategories();

        assertThat(result).hasSize(3);
        assertThat(result).contains("SOINS", "PROTHESE", "IMPLANT");
    }

    @Test
    @DisplayName("Vérifier si un code existe")
    void codeExiste() throws ServiceException, ValidationException {
        Acte acte = creerActeTest();
        acteService.creerActe(acte);

        assertThat(acteService.codeExiste(101)).isTrue();
        assertThat(acteService.codeExiste(999)).isFalse();
    }

    @Test
    @DisplayName("Vérifier si un libellé existe")
    void libelleExiste() throws ServiceException, ValidationException {
        Acte acte = creerActeTest();
        acteService.creerActe(acte);

        assertThat(acteService.libelleExiste("Détartrage")).isTrue();
        assertThat(acteService.libelleExiste("Inexistant")).isFalse();
    }

    @Test
    @DisplayName("Lister tous les actes")
    void listerTousLesActes() throws ServiceException, ValidationException {
        acteService.creerActe(creerActeTest());

        Acte a2 = Acte.builder()
                .libelle("Extraction")
                .categorie("SOINS")
                .prixDeBase(500.0)
                .code(102)
                .build();
        acteService.creerActe(a2);

        List<Acte> result = acteService.listerTousLesActes();

        assertThat(result).hasSize(2);
    }

    private Acte creerActeTest() {
        return Acte.builder()
                .libelle("Détartrage")
                .categorie("SOINS")
                .prixDeBase(300.0)
                .code(101)
                .description("Nettoyage dentaire complet")
                .build();
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
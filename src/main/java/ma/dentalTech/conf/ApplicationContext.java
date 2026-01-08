package ma.dentalTech.conf;

import ma.dentalTech.mvc.controllers.modules.patient.api.PatientController;
//import ma.dentalTech.mvc.controllers.modules.patient.impl.swing_implementation.PatientControllerImpl; // Ou le bon chemin vers ton impl
import ma.dentalTech.repository.modules.agenda.api.AgendaRepository;
import ma.dentalTech.repository.modules.agenda.impl.mySQL.AgendaRepositoryImpl;
import ma.dentalTech.repository.modules.dossierMedical.api.RDVRepository;
import ma.dentalTech.repository.modules.dossierMedical.impl.mySQL.RDVRepositoryImpl;
import ma.dentalTech.repository.modules.patient.api.PatientRepository;
import ma.dentalTech.repository.modules.patient.impl.mySQL.PatientRepositoryImpl;

// Imports Module RDVPatient (GestionRDV)
import ma.dentalTech.service.modules.RDVPatient.api.IGestionRDVService;
import ma.dentalTech.service.modules.RDVPatient.impl.GestionRDVServiceImpl;
import ma.dentalTech.mvc.controllers.modules.RDVPatient.api.IGestionRDVController;
import ma.dentalTech.mvc.controllers.modules.RDVPatient.impl.GestionRDVController;

// Imports Module Patient
import ma.dentalTech.service.modules.patient.api.PatientService;
//import ma.dentalTech.service.modules.patient.impl.PatientServiceImpl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Fabrique de composants (IoC Container simplifié).
 * Charge et instancie les Repositories, Services et Contrôleurs.
 * Assure l'Injection de Dépendances (DI).
 */
public class ApplicationContext {

    // Conteneur principal (Map Type -> Instance)
    private static final Map<Class<?>, Object> context = new HashMap<>();
    // Conteneur secondaire (Map Nom -> Instance)
    private static final Map<String, Object> contextByName = new HashMap<>();

    static {
        System.out.println("[ApplicationContext] Initialisation du contexte...");

        try {
            // =========================================================
            // 1. INSTANCIATION DES REPOSITORIES (Couche Accès Données)
            // =========================================================
            PatientRepository patientRepo = new PatientRepositoryImpl();
            RDVRepository rdvRepo         = new RDVRepositoryImpl();
            AgendaRepository agendaRepo   = new AgendaRepositoryImpl();

            // Enregistrement
            registerBean(PatientRepository.class, "patientRepo", patientRepo);
            registerBean(RDVRepository.class,     "rdvRepo",     rdvRepo);
            registerBean(AgendaRepository.class,  "agendaRepo",  agendaRepo);

            // =========================================================
            // 2. INSTANCIATION DES SERVICES (Couche Métier)
            // =========================================================

            // --- Module Patient ---
            // Injection de patientRepo dans PatientService
            //PatientService patientService = new PatientServiceImpl(patientRepo);
            //registerBean(PatientService.class, "patientService", patientService);

            // --- Module Gestion RDV (RDVPatient) ---
            // Injection de rdvRepo dans GestionRDVService
            IGestionRDVService gestionRDVService = new GestionRDVServiceImpl(rdvRepo);
            registerBean(IGestionRDVService.class, "gestionRDVService", gestionRDVService);

            // =========================================================
            // 3. INSTANCIATION DES CONTROLLERS (Couche Présentation)
            // =========================================================

            // --- Controller Patient ---
            // Injection de patientService dans PatientController
            // Note: Adapte le new si ton PatientController a un autre constructeur
            //PatientController patientController = new PatientController(patientService);
            //registerBean(PatientController.class, "patientController", patientController);

            // --- Controller Gestion RDV ---
            // Injection de gestionRDVService dans GestionRDVController
            IGestionRDVController gestionRDVController = new GestionRDVController(gestionRDVService);
            registerBean(IGestionRDVController.class, "gestionRDVController", gestionRDVController);

            // =========================================================
            // 4. CONFIGURATION OPTIONNELLE
            // =========================================================
            loadPropertiesOverrides();

            System.out.println("[ApplicationContext] Contexte initialisé avec succès.");

        } catch (Exception e) {
            System.err.println("[ApplicationContext] ERREUR FATALE : Impossible d'initialiser l'application.");
            e.printStackTrace();
            // On arrête l'application si le contexte ne se charge pas
            throw new RuntimeException("Echec de l'initialisation du contexte", e);
        }
    }

    /**
     * Helper pour enregistrer un bean dans les deux maps.
     */
    private static <T> void registerBean(Class<T> clazz, String name, Object bean) {
        context.put(clazz, bean);
        contextByName.put(name, bean);
    }

    /**
     * (Optionnel) Permet d'écraser certaines configs via le fichier properties si besoin.
     */
    private static void loadPropertiesOverrides() {
        try (InputStream configFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/beans.properties")) {
            if (configFile != null) {
                Properties props = new Properties();
                props.load(configFile);
                // Logique d'override future ici
            }
        } catch (Exception e) {
            System.out.println("[ApplicationContext] Info : Pas de fichier beans.properties détecté.");
        }
    }

    // =========================================================
    // ACCESSEURS (Getters)
    // =========================================================

    /**
     * Retourne un composant bean en fonction de son nom (String).
     */
    public static Object getBean(String beanName) {
        return contextByName.get(beanName);
    }

    /**
     * Retourne un composant bean en fonction de sa classe (Interface ou Impl).
     */
    public static <T> T getBean(Class<T> beanClass) {
        Object bean = context.get(beanClass);
        if (bean == null) {
            // Recherche par assignabilité (utile si on demande l'interface mais qu'on a stocké l'implémentation)
            for (Object obj : context.values()) {
                if (beanClass.isInstance(obj)) {
                    return beanClass.cast(obj);
                }
            }
        }
        return beanClass.cast(bean);
    }
}
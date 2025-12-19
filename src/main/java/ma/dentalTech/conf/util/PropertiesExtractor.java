package ma.dentalTech.conf.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertiesExtractor {

    private static final Logger LOGGER = Logger.getLogger(PropertiesExtractor.class.getName());
    private static final java.util.Map<String, Properties> CACHE = new java.util.HashMap<>();

    public static Properties loadConfigFile(String propsPath) {

        if (propsPath == null || propsPath.trim().isEmpty()) {
            throw new IllegalArgumentException("Le chemin du fichier properties ne peut pas être null ou vide");
        }

        if (CACHE.containsKey(propsPath)) {
            LOGGER.log(Level.FINE, "Chargement depuis le cache: {0}", propsPath);
            return CACHE.get(propsPath);
        }

        Properties properties = new Properties();

        try (InputStream in = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(propsPath)) {

            if (in == null) {
                String errorMsg = String.format("Fichier de configuration introuvable: %s", propsPath);
                LOGGER.severe(errorMsg);
                throw new IllegalStateException(errorMsg);
            }

            properties.load(in);
            CACHE.put(propsPath, properties);
            LOGGER.log(Level.INFO, "Fichier chargé avec succès: {0} ({1} propriétés)",
                    new Object[]{propsPath, properties.size()});

            return properties;

        } catch (IOException e) {
            String errorMsg = String.format("Erreur lors de la lecture du fichier: %s", propsPath);
            LOGGER.log(Level.SEVERE, errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    public static String getPropertyValue(String key, Properties properties) {

        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("La clé ne peut pas être null ou vide");
        }

        if (properties == null) {
            throw new IllegalArgumentException("L'objet Properties ne peut pas être null");
        }

        String value = properties.getProperty(key);

        if (value == null) {
            String errorMsg = String.format("Propriété introuvable - Clé: '%s'", key);
            LOGGER.severe(errorMsg);
            throw new IllegalStateException(errorMsg);
        }

        return value.trim();
    }

    public static String getPropertyValue(String key, Properties properties, String defaultValue) {

        if (properties == null) {
            return defaultValue;
        }

        String value = properties.getProperty(key);
        return (value != null) ? value.trim() : defaultValue;
    }

    public static int getIntProperty(String key, Properties properties) {
        String value = getPropertyValue(key, properties);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            String errorMsg = String.format("La propriété '%s' n'est pas un entier valide: %s", key, value);
            LOGGER.severe(errorMsg);
            throw new NumberFormatException(errorMsg);
        }
    }

    public static int getIntProperty(String key, Properties properties, int defaultValue) {
        String value = getPropertyValue(key, properties, null);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Valeur invalide pour ''{0}'': {1}. Utilisation de la valeur par défaut: {2}",
                    new Object[]{key, value, defaultValue});
            return defaultValue;
        }
    }

    public static boolean getBooleanProperty(String key, Properties properties) {
        String value = getPropertyValue(key, properties);
        return Boolean.parseBoolean(value);
    }

    public static boolean getBooleanProperty(String key, Properties properties, boolean defaultValue) {
        String value = getPropertyValue(key, properties, null);
        return (value != null) ? Boolean.parseBoolean(value) : defaultValue;
    }

    public static boolean hasProperty(String key, Properties properties) {
        return properties != null && properties.containsKey(key);
    }

    public static void clearCache() {
        CACHE.clear();
        LOGGER.info("Cache des properties vidé");
    }

    public static void clearCache(String propsPath) {
        CACHE.remove(propsPath);
        LOGGER.log(Level.INFO, "Cache vidé pour: {0}", propsPath);
    }
}
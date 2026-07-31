package engine.utils.propertyFilesHandlers;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PropertyReader {

    private PropertyReader(){}

    private static final Map<String, String> CACHE = new ConcurrentHashMap<>();

    public static <T> T get(String key, Class<T> type) {
        String value = resolve(key);
       return PropertyParser.parseValue(key,value,type);
    }

    public static <T> T get(String key, Class<T> type,Map<String, String> variables) {
        String value = resolve(key,variables);
       return PropertyParser.parseValue(key,value,type);
    }

    /**
     *
     * @param key: Key to search for
     * @return the value of the key requested saving the key value combination into the cache for faster retrieving next calls
     */
    private static String resolve(String key) {
        return CACHE.computeIfAbsent(key, k -> {
            String sysValue = System.getProperty(k);
            if (sysValue != null && !sysValue.isBlank()) {
                return sysValue;
            }
            String fileValue = PropertyLoader.getAllProperties().getProperty(k);
            if (fileValue == null) {
                throw new IllegalStateException(
                        "Missing configuration key: " + k
                );
            }
            return fileValue.trim();
        });
    }

    private static String resolve(String key,Map<String, String> variables) {
        return CACHE.computeIfAbsent(key, k -> {
            String sysValue = System.getProperty(k);
            if (sysValue != null && !sysValue.isBlank()) {
                return sysValue;
            }
            String fileValue = PropertyLoader.getAllProperties().getProperty(k);
            if (fileValue == null) {
                throw new IllegalStateException(
                        "Missing configuration key: " + k
                );
            }
            return replaceVariables(fileValue.trim(),variables);
        });
    }

    private static String replaceVariables(
            String value,
            Map<String, String> variables) {
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            value = value.replace(
                    "${" + entry.getKey() + "}",
                    entry.getValue()
            );
        }
        return value;
    }

    public static void clearCache() {
        CACHE.clear();
    }
}

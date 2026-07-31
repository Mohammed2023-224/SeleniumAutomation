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

    public static void clearCache() {
        CACHE.clear();
    }
}

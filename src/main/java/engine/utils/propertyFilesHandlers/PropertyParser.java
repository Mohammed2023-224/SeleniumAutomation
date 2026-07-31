package engine.utils.propertyFilesHandlers;

import engine.exceptions.CustomExceptions;

import java.util.*;

public class PropertyParser {

    private PropertyParser(){}

    /**
     *
     * @param key: Current key
     * @param value: Current value for the key
     * @param type: Type to cast data into
     * @return the data casted into the tupe passed
     * @param <T>
     */
    static <T> T parseValue(String key, String value, Class<T> type) {
        if (value == null) {
            throw new IllegalStateException("Missing config key: " + key);
        }
        value=value.trim();
        try {
            if (type == String.class) {
                return type.cast(value);
            }
            if (type == Integer.class) {
                return type.cast(Integer.parseInt(value));
            }
            if (type == Boolean.class) {
                if (!value.equals("true") &&
                        !value.equals("false")) {
                    throw new CustomExceptions("wrong boolean value " + value);
                }
                return type.cast(Boolean.parseBoolean(value));
            }
            if (type == Long.class) {
                return type.cast(Long.parseLong(value));
            }
            if (type == Double.class) {
                return type.cast(Double.parseDouble(value));
            }
            if (type == List.class) {
                return type.cast(Arrays.stream(value.split(","))
                        .map(String::trim)
                        .toList());
            }
            if (type == Map.class) {
                Map<String, String> map = new LinkedHashMap<>();
                Arrays.stream(value.split(","))
                        .forEach(entry -> {
                            String[] pair = entry.split("=", 2);
                            if (pair.length != 2) {
                                throw new IllegalArgumentException(
                                        "Invalid map entry: " + entry);
                            }
                            map.put(
                                    pair[0].trim(),
                                    pair[1].trim()
                            );
                        });
                return (T) map;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Failed to parse config key '" + key +
                            "' as " + type.getSimpleName() +
                            " (value=" + value + ")", e
            );
        }
    throw new CustomExceptions("Unsupported type: " + type.getName());
    }
}

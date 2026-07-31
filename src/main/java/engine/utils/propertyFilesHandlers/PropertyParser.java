package engine.utils.propertyFilesHandlers;

import engine.exceptions.CustomExceptions;

import java.util.Arrays;
import java.util.List;

public class PropertyParser {

    private PropertyParser(){}

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

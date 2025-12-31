package engine.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import engine.reporters.Loggers;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JSONReader {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JSONReader() {
    }

    public static <T> T deserializeJson(String file, Class<T> classType) {
        try {
            Loggers.getLogger().info("Deserializing JSON file: {}", file);
            return objectMapper.readValue(new File(file), classType);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON from " + file, e);
        }
    }

    public static <T> List<T> deserializeJsonArray(String file, Class<T> classType) {
        try {
            Loggers.getLogger().info("Deserializing JSON array file: {}", file);
            JavaType type = objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, classType);
            return objectMapper.readValue(new File(file), type);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON array from " + file, e);
        }
    }

    public static <T> T convertToObject(Object data, Class<T> classType) {
        return objectMapper.convertValue(data, classType);
    }

    public static <T> List<T> convertToList(List<?> data, Class<T> classType) {
        return data.stream()
                .map(obj -> objectMapper.convertValue(obj, classType))
                .collect(Collectors.toList());
    }
}
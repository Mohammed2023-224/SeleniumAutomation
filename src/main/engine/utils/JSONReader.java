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
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
    }

    public static <T>T deserializeJson(String file,Class<T> className) {
        Object object;
        try {
           object=objectMapper.readValue(new File(file)
                    , className);
            Loggers.log.info("Deserialize file located at {}",file);
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
        return (T) object;
    }

    public static <T> T getJsonObject(Object data, Class<T> className) {
        if (data instanceof Map) {
            Loggers.log.info("Convert Json array into object");
            return objectMapper.convertValue(data, className);
        }
        return null;
    }

    public static <T> List<T> getJsonArray(Object data, Class<T> className) {
        if (data instanceof List) {
            Loggers.log.info("Convert Json array into list");
            return ((List<?>) data).stream()
                    .map(obj -> objectMapper.convertValue(obj, className))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
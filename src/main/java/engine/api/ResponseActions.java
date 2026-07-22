package engine.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import engine.reporters.Loggers;
import io.restassured.common.mapper.TypeRef;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.poi.ss.formula.functions.T;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ResponseActions {
    private static final ObjectMapper objectMapper;
    private static final String CASTING_FAILED_LOG = "Return value cannot be cast to ";
    private static final String EMPTY_LIST_LOG = "List is empty";

    static {
        objectMapper = new ObjectMapper();
    }

    private ResponseActions() {
    }

    public static void checkResponseStatus(Response res, int status) {
        try {
            if (res.statusCode() != status) {
                Loggers.logError("current status code: " + res.getStatusCode());
                Loggers.logError("current body: " + ResponseActions.getBodyAsString(res));
            }
            res.then().statusCode(status);
            Loggers.logInfo("Validate that status code = " + status);
        } catch (Exception e) {
            Loggers.logError("Failed to validate status code: " + e.getMessage());
            Loggers.logError("Expected: " + status + ", Actual: " + res.statusCode());
            throw new AssertionError("Status code validation failed", e);
        }
    }

    public static int getStatusCode(Response res) {
        try {
            return res.getStatusCode();
        } catch (Exception e) {
            Loggers.logError("Failed to get status code: " + e.getMessage());
            return -1;
        }
    }

    public static void checkResponseContent(Response res, String contentType) {
        try {
            res.then().contentType(contentType);
            Loggers.logInfo("Validate that contentType = " + contentType);
        } catch (Exception e) {
            Loggers.logError("Content type validation failed. Expected: " + contentType);
            Loggers.logError("Actual: " + res.contentType());
            throw new AssertionError("Content type validation failed", e);
        }
    }

    public static void checkResponseCookie(Response res, String cookieName, String value) {
        try {
            res.then().cookie(cookieName, value);
            Loggers.logInfo("Validate that cookie " + cookieName + " == " + value);
        } catch (Exception e) {
            Loggers.logError("Cookie validation failed. Cookie: " + cookieName);
            Loggers.logError("Expected value: " + value);
            Loggers.logError("Actual value: " + res.getCookie(cookieName));
            throw new AssertionError("Cookie validation failed", e);
        }
    }

    public static void checkResponseHeader(Response res, String headerName, String value) {
        try {
            res.then().header(headerName, value);
            Loggers.logInfo("Validate that header " + headerName + " == " + value);
        } catch (Exception e) {
            Loggers.logError("Header validation failed. Header: " + headerName);
            Loggers.logError("Expected value: " + value);
            Loggers.logError("Actual value: " + res.getHeader(headerName));
            throw new AssertionError("Header validation failed", e);
        }
    }

    public static void logResponse(Response response) {
        try {
            Loggers.logInfo("log all response");
            response.then().log().all();
        } catch (Exception e) {
            Loggers.logError("Failed to log response: " + e.getMessage());
        }
    }

    public static void logBody(Response response) {
        try {
            Loggers.logInfo("log body only");
            response.then().log().body();
        } catch (Exception e) {
            Loggers.logError("Failed to log body: " + e.getMessage());
        }
    }

    public static void logHeaders(Response response) {
        try {
            Loggers.logInfo("log all headers");
            response.then().log().headers();
        } catch (Exception e) {
            Loggers.logError("Failed to log headers: " + e.getMessage());
        }
    }

    public static void logCookies(Response response) {
        try {
            Loggers.logInfo("Log all cookies");
            response.then().log().cookies();
        } catch (Exception e) {
            Loggers.logError("Failed to log cookies: " + e.getMessage());
        }
    }

    public static String getBodyAsString(Response response) {
        try {
            Loggers.logInfo("Get body as string");
            return response.body().asString();
        } catch (Exception e) {
            Loggers.logError("Failed to get body as string: " + e.getMessage());
            return null;
        }
    }

    public static String getAllHeaders(Response response) {
        try {
            Loggers.logInfo("Get all headers as a string");
            return response.getHeaders().toString();
        } catch (Exception e) {
            Loggers.logError("Failed to get all headers: " + e.getMessage());
            return null;
        }
    }

    public static String getCertainHeader(Response response, String header) {
        try {
            Loggers.logInfo("Get header with the key : " + header);
            return response.getHeader(header);
        } catch (Exception e) {
            Loggers.logError("Failed to get header '" + header + "': " + e.getMessage());
            return null;
        }
    }

    public static Map<String, String> getAllCookies(Response response) {
        try {
            Loggers.logInfo("Get all cookies");
            return response.getCookies();
        } catch (Exception e) {
            Loggers.logError("Failed to get all cookies: " + e.getMessage());
            return Collections.emptyMap();
        }
    }

    public static String getCertainCookie(Response response, String value) {
        try {
            Loggers.logInfo("Get certain cookie");
            return response.getCookie(value);
        } catch (Exception e) {
            Loggers.logError("Failed to get cookie '" + value + "': " + e.getMessage());
            return null;
        }
    }

    public static <T> T getValueByPath(Response response, String path, Class<T> type) {
        try {
            return response.jsonPath().getObject(path, type);
        } catch (Exception e) {
            Loggers.logError("Failed to get value by path '" + path + "': " + e.getMessage());
            return null;
        }
    }

    public static <T> T getValueByPath(Response response, String path, TypeRef<T> typeRef) {
        try {
            return response.jsonPath().getObject(path, typeRef);
        } catch (Exception e) {
            Loggers.logError("Failed to get value by path '" + path + "': " + e.getMessage());
            return null;
        }
    }

    public static ExtractableResponse<Response> extractFullResponse(Response response) {
        try {
            Loggers.logInfo("Get full response");
            return response.then().extract();
        } catch (Exception e) {
            Loggers.logError("Failed to extract response: " + e.getMessage());
            return null;
        }
    }

    public static JsonPath getFullJsonPath(Response response) {
        try {
            Loggers.logInfo("get full json path");
            return response.body().jsonPath();
        } catch (Exception e) {
            Loggers.logError("Failed to get JsonPath: " + e.getMessage());
            return null;
        }
    }

    public static <T> T deserializeResponseIntoClass(Response response, Class<T> className) {
        try {
            T object = objectMapper.readValue(response.getBody().asString(), className);
            Loggers.logInfo("read json from current response and deserialize it into class " + className.getSimpleName());
            return object;
        } catch (Exception e) {
            Loggers.logError("Couldn't deserialize current response: " + e.getMessage());
            return null;
        }
    }

    /**
     * @param list:           Accepts list of Map<String,Object> that wil be used for the search
     * @param criteria:       The key that we will search for
     * @param searchCriteria: The value for the above key
     * @param returnValue:    The key that is required
     * @return object of the first value found for the return value [Key] where it has the key -> value of criteria -> searchCriteria
     * in the same map index in this list
     */
    public static Object getResponseItemWithACertainValue(List<Map<String, Object>> list, String criteria, String searchCriteria, String returnValue) {
        Object value = null;
        if (list.isEmpty()) {
            return EMPTY_LIST_LOG;
        }
        for (Map<String, Object> stringObjectMap : list) {
            if (stringObjectMap.get(criteria).equals(searchCriteria)) {
                value = stringObjectMap.get(returnValue);
            }
        }
        return value == null ? "No value found" : value;
    }

    /**
     *
     * @param list:           Accepts list of Map<String,Object> that wil be used for the search
     * @param criteria:       The key for the map that user is searching by
     * @param searchCriteria: the value for the map that user wants to search by
     * @return map of the list index where search -> search criteria is found in. and returns empty map if no combination are found
     */
    public static Map<String, Object> getResponseItemWithACertainValue(
            List<Map<String, Object>> list,
            String criteria,
            Object searchCriteria) {
        if (list.isEmpty()) {
            Loggers.logWarn("List of maps doesn't have any indexes");
            return Collections.emptyMap();
        }
        for (Map<String, Object> item : list) {
            Object value = item.get(criteria);
            if (value != null && value.equals(searchCriteria)) {
                return item;
            }
        }
        Loggers.logWarn("Map is empty for these search criteria");
        return Collections.emptyMap();
    }

    /**
     *
     * @param list:          Accepts list of Map<String,Object> that wil be used for the search
     * @param searchCriteria Map contains all key value pairs to search by
     * @param returnField    returns the wanted value for this key
     * @param returnType     casts the return value to the provided type
     * @param <T>            used to return any value
     * @return the value for the key returnField where the map in the list contain all the key value pairs provided in
     * the searchCriteria map
     */
    public static <T> T getResponseItemWithCertainValue(
            List<Map<String, Object>> list,
            Map<String, Object> searchCriteria,
            String returnField,
            Class<T> returnType) {
        if (list.isEmpty()) {
            Loggers.logWarn("List of maps doesn't have any indexes");
            return null;
        }
        for (Map<String, Object> item : list) {
            boolean matches = searchCriteria.entrySet().stream()
                    .allMatch(entry -> {
                        Object actual = item.get(entry.getKey());
                        Object expected = entry.getValue();
                        if (actual instanceof String ac && expected instanceof String ex) {
                            return (ac).equalsIgnoreCase(ex);
                        }
                        return Objects.equals(actual, expected);
                    });
            if (matches) {
                try {
                    return returnType.cast(item.get(returnField));
                } catch (ClassCastException e) {
                    Loggers.logError(CASTING_FAILED_LOG + returnType.getName());
                    return null;
                }
            }
        }
        Loggers.logError("No matching item found for criteria: " + searchCriteria);
        return null;
    }

    /**
     * @param jsonPath:       is the full json path from the response
     * @param path:           is the path destination
     * @param criteria:       The key that we are searching by it
     * @param searchCriteria: the criteria that must be met by the
     * @param returnValue:    The actual key that we want to return the value of
     * @return object of the value for the key known as return the first value found
     * where that json object has the criteria and search criteria met
     */
    public static Object getResponseItemWithACertainValue(
            JsonPath jsonPath, String path, String criteria, String searchCriteria,
            String returnValue, Class<T> returnType) {
        Object result = jsonPath.param("searchCriteria", searchCriteria)
                .param("criteria", criteria)
                .param("returnValue", returnValue)
                .param("path", path)
                .getString("path.find { it.criteria == searchCriteria }.returnValue");

        if (result == null) {
            Loggers.logError("No matching item found for criteria: " + searchCriteria);
            return null;
        }
        try {
            return returnType.cast(result);
        } catch (ClassCastException e) {
            Loggers.logError(CASTING_FAILED_LOG + returnType.getName());
            return null;
        }
    }

    /**
     *
     * @param jsonPath:         is the full json path from the response
     * @param groovyExpression: Accepts a groovy expression as a string
     * @return object of the first value found with this groovy expression
     * Example for groovy expression: path.find { it.criteria == searchCriteria }.returnValue
     */
    public static Object getResponseItemWithACertainValue(JsonPath jsonPath, String groovyExpression,
                                                          Class<T> returnType) {
        Object result = jsonPath.getString(groovyExpression);
        if (result == null) {
            Loggers.logError("No matching item found for the expression: " + groovyExpression);
            return null;
        }
        try {
            return returnType.cast(result);
        } catch (ClassCastException e) {
            Loggers.logError(CASTING_FAILED_LOG + returnType.getName());
            return null;
        }
    }
}
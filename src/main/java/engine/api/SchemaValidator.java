package engine.api;

import engine.assertions.HardAssertions;
import engine.reporters.Loggers;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class SchemaValidator {

    public static void schemaKeyValidation(Map<String, Object> responseMap, Map<String,Class<?>> definedSchema, boolean canBeEmpty) {
        if (responseMap == null || responseMap.isEmpty()) {
            if (!canBeEmpty) {
                HardAssertions.assertTru(
                        () -> false,
                        "Response is empty but should NOT be empty"
                );
            } else {
                Loggers.logInfo("Response is empty and allowed");
                return;
            }
            return;
        }
        for (Map.Entry<String,Object> entry : responseMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            Class<?> valueClass=value.getClass();
            Class<?> ketClass=definedSchema.get(key);
            HardAssertions.assertTru(
                    () -> definedSchema.containsKey(key),
                    key + " that exists in defined schema found at the response"
            );
            HardAssertions.assertTru(
                    () -> valueClass==ketClass,
                    key + "The data type for the key "+key+" which is defined at the schema as "+ketClass.getName()+" equals to the type received in the response "+valueClass.getName()
            );
        }
        for (String key : definedSchema.keySet()) {
            HardAssertions.assertTru(
                    () -> responseMap.containsKey(key),
                    key + " that exists in response found at the defined schema"
            );
        }
    }

    public static void schemaKeyValidation(List<Map<String, Object>> responseMap, Map<String,Class<?>> definedSchema, boolean canBeEmpty) {
        if (responseMap == null || responseMap.isEmpty()) {
            if (!canBeEmpty) {
                HardAssertions.assertTru(
                        () -> false,
                        "Response is empty but should NOT be empty"
                );
            } else {
                Loggers.logInfo("Response is empty and allowed stopping validation as no response exist");
                return;
            }
            return;
        }
        Map<String,Object> firstMap=responseMap.getFirst();
        for (Map.Entry<String,Object> entry : firstMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            Class<?> valueClass=value.getClass();
            Class<?> ketClass=definedSchema.get(key);
            HardAssertions.assertTru(
                    () -> definedSchema.containsKey(key),
                    key + " that exists in defined schema found at the response"
            );
            HardAssertions.assertTru(
                    () -> valueClass==ketClass,
                    key + "The data type for the key "+key+" which is defined at the schema as "+ketClass.getName()+" equals to the type received in the response "+valueClass.getName()
            );
        }
        for (String key : definedSchema.keySet()) {
            HardAssertions.assertTru(
                    () -> firstMap.containsKey(key),
                    key + " that exists in response found at the defined schema"
            );
        }
    }

    public static void validateJsonSchema(Response res, String filePath) {
        String newFilePath="jsonSchemas/"+filePath;
        Loggers.logInfo("Starting schema validation for response : "+ResponseActions.getBodyAsString(res)+" with json file at: "+newFilePath);
        try {
            res.then()
                    .assertThat()
                    .body(matchesJsonSchemaInClasspath(newFilePath));
            HardAssertions.assertTru(()->true, "Schema validated successfully against file at: "+ newFilePath);
        }
        catch (Exception e){
            Loggers.logError("Validation failed against schema located at: "+ newFilePath);
            HardAssertions.assertTru(()->false, "Schema validation failed against file at: "+ newFilePath +" "+e.getMessage() );
        }
    }
}
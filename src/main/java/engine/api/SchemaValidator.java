package engine.api;

import engine.assertions.HardAssertions;
import engine.reporters.Loggers;

import java.util.List;
import java.util.Map;

public class SchemaValidator {

    public static void schemaKeyValidation(Map<String, Object> responseMap, List<String> listOfKeys, boolean canBeEmpty) {
        if (responseMap == null || responseMap.isEmpty()) {
            if (!canBeEmpty) {
                HardAssertions.assertTru(
                        () -> false,
                        "Response is empty but should NOT be empty"
                );
            } else {
                Loggers.logInfo("Response is empty and allowed");
            }
            return;
        }
        for (String key : responseMap.keySet()) {
            HardAssertions.assertTru(
                    () -> listOfKeys.contains(key),
                    key + " that exists in defined schema found at the response"
            );
        }

        for (String key : listOfKeys) {
            HardAssertions.assertTru(
                    () -> responseMap.containsKey(key),
                    key + " that exists in response found at the defined schema"
            );
        }

    }


    public static void schemaKeyValidation(
            List<Map<String, Object>> responseList,
            List<String> listOfKeys,
            boolean canBeEmpty) {
        if (responseList == null || responseList.isEmpty()) {
            if (!canBeEmpty) {
                HardAssertions.assertTru(
                        () -> false,
                        "List is empty but should NOT be empty"
                );
            } else {
                Loggers.logInfo("List is empty and allowed");
            }
            return;
        }
        for (Map<String, Object> item : responseList) {
            schemaKeyValidation(item, listOfKeys, false);
        }
    }
}
package engine.utils.propertyFilesHandlers;

import engine.reporters.Loggers;
import engine.utils.Helpers;

import java.util.Map;

public class PropertyHelpers {

    public static String replaceVariablesInPropertyFile(
            String value,
            Map<String, String> variables) {
        if(!(Helpers.extractTextUsingRegex(value,"\\$\\{([^}]+)\\}") ==null)) {
            for (Map.Entry<String, String> entry : variables.entrySet()) {
                value = value.replace(
                        "${" + entry.getKey() + "}",
                        entry.getValue()
                );
            }
        }
        return value;
    }
}

package engine.utils;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

public class ReadExecutionFlow {
    private ReadExecutionFlow(){}

    private static volatile Set<String> cachedTests;

    private static final String CONTROL_FILE = PropertyReader.get("file_path", String.class);
    private static final String SHEET_NAME = PropertyReader.get("sheet_name", String.class);

    public static Set<String> getExecutionControl() {

        if (Boolean.FALSE.equals(PropertyReader.get("file_control", Boolean.class))) {
            return Set.of(); // safe empty set
        }

        if (cachedTests == null) {
            synchronized (ReadExecutionFlow.class) {
                if (cachedTests == null) {
                    cachedTests = loadFromExcel();
                }
            }
        }
        return cachedTests;
    }

    private static Set<String> loadFromExcel() {
        Object[][] obj =
                new ExcelReader().readRowAsLinkedHashMapThroughCondition(
                        CONTROL_FILE, SHEET_NAME, "Run", "TRUE");

        Set<String> allowedTests = new HashSet<>();

        for (Object[] row : obj) {
            @SuppressWarnings("unchecked")
            LinkedHashMap<String, String> rowMap =
                    (LinkedHashMap<String, String>) row[0];

            String methodName = rowMap.get("Method name");
            String className = rowMap.get("class name");

            allowedTests.add(
                    (className + "." + methodName).toLowerCase()
            );
        }
        return allowedTests;
    }
}


package engine.utils;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

public class ReadExecutionFlow {
    private static volatile Set<String> CACHED_TESTS;

    private static final String CONTROL_FILE = PropertyReader.get("file_path", String.class);
    private static final String SHEET_NAME = PropertyReader.get("sheet_name", String.class);

    public static Set<String> getExecutionControl() {

        if (!PropertyReader.get("file_control", Boolean.class)) {
            return Set.of(); // safe empty set
        }

        if (CACHED_TESTS == null) {
            synchronized (ReadExecutionFlow.class) {
                if (CACHED_TESTS == null) {
                    CACHED_TESTS = loadFromExcel();
                }
            }
        }
        return CACHED_TESTS;
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


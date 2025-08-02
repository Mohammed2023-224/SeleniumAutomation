package engine.utils;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

public class ReadExecutionFlow {
        private   String CONTROL_FILE = "src/test/resources/ExecutionFlow.xlsx";
        private  final String SHEET_NAME = "control";

    public Set<String> readExecutionControl(){
       Object [][] obj=
               new ExcelReader().readRowAsLinkedHashMapThroughCondition(CONTROL_FILE,SHEET_NAME,"Run","TRUE");
           Set<String> allowedTests = new HashSet<>();

        for (Object[] row : obj) {
            @SuppressWarnings("unchecked")
            LinkedHashMap<String, String> rowMap = (LinkedHashMap<String, String>) row[0];
            String methodName = rowMap.get("Method name");
            String className = rowMap.get("class name");
            allowedTests.add(className.toLowerCase() + "." + methodName.toLowerCase());
        }
        return allowedTests;
    }
    }


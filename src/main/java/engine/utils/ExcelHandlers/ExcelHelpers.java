package engine.utils.ExcelHandlers;

import engine.reporters.Loggers;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.Map;
import java.util.stream.Collectors;

public class ExcelHelpers {

    public static String normalizeMapIntoString(Map<String, String> row) {
        return row.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getKey().trim() + "=" + entry.getValue().trim())
                .collect(Collectors.joining("|"));
    }
    /*
    //    List<String> actual = actualRows.stream()
//            .map(this::normalizeRow)
//            .sorted()
//            .toList();
comparison usage example
     */


}

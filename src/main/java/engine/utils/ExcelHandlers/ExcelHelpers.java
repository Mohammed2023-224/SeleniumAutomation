package engine.utils.ExcelHandlers;

import engine.reporters.Loggers;
import engine.utils.DataTypes;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.*;
import java.util.stream.Collectors;

public class ExcelHelpers {

    public static String normalizeMapIntoString(DataTypes.ExcelRow excelRow) {
        return "RowNumber="+excelRow.rowNumber() +"|" +excelRow.values().entrySet()
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

    public static List<DataTypes.ExcelRow> sortRows(
            List<DataTypes.ExcelRow> rows , String firstColumn,String secondColumn) {
        return rows.stream()
                .sorted(
                        Comparator.comparing(
                                (DataTypes.ExcelRow row) ->
                                        row.values().getOrDefault(firstColumn, ""),
                                String.CASE_INSENSITIVE_ORDER
                        ).thenComparing(
                                row -> row.values().getOrDefault(secondColumn, ""),
                                String.CASE_INSENSITIVE_ORDER
                        )
                )
                .toList();
    }

    public static void compareRows(
            List<DataTypes.ExcelRow> file1,
            List<DataTypes.ExcelRow> file2,String column1,String column2) {

        List<DataTypes.ExcelRow> sorted1 = sortRows(file1,column1,column2);
        List<DataTypes.ExcelRow> sorted2 = sortRows(file2,column1,column2);
        int maxRows = Math.max(sorted1.size(), sorted2.size());
        boolean hasDifferences = false;

        for (int i = 0; i < maxRows; i++) {

            if (i >= sorted1.size()) {
                DataTypes.ExcelRow row2 = sorted2.get(i);

                Loggers.logError(
                        "Extra row in File 2: Excel row "
                                + row2.rowNumber()
                                + ", values=" + row2.values()
                );
                hasDifferences = true;
                continue;
            }

            if (i >= sorted2.size()) {
                DataTypes.ExcelRow row1 = sorted1.get(i);

                Loggers.logError(
                        "Extra row in File 1: Excel row "
                                + row1.rowNumber()
                                + ", values=" + row1.values()
                );
                hasDifferences = true;
                continue;
            }

            DataTypes.ExcelRow row1 = sorted1.get(i);
            DataTypes.ExcelRow row2 = sorted2.get(i);

            Set<String> columns = new TreeSet<>();
            columns.addAll(row1.values().keySet());
            columns.addAll(row2.values().keySet());

            for (String column : columns) {

                String value1 = row1.values().get(column);
                String value2 = row2.values().get(column);

                if (!Objects.equals(value1, value2)) {
                    Loggers.logError(
                            "Mismatch in column '" + column + "'"
                                    + " | File 1 Excel row " + row1.rowNumber()
                                    + ", value='" + value1 + "'"
                                    + " | File 2 Excel row " + row2.rowNumber()
                                    + ", value='" + value2 + "'"
                    );

                    hasDifferences = true;
                }
            }
        }

        if (!hasDifferences) {
            Loggers.logInfo("Excel files match.");
        }
    }
}

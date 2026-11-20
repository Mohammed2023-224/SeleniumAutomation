package engine.utils.ExcelHandlers;

import engine.enums.SortingTypes;
import engine.reporters.Loggers;
import engine.utils.DataTypes;
import engine.utils.Helpers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ExcelHelpers {

    private static List<DataTypes.ExcelRow> sortRows(
            List<DataTypes.ExcelRow> rows,
            DataTypes.SortConfig... configs) {
        if (rows == null || rows.isEmpty()) {
            return List.of();
        }
        if (configs == null || configs.length == 0) {
            return new ArrayList<>(rows);
        }
        Comparator<DataTypes.ExcelRow> comparator = (a, b) -> 0;
        for (DataTypes.SortConfig config : configs) {
            Comparator<DataTypes.ExcelRow> columnComparator = (rowA, rowB) -> {
                String valueA = rowA.values().get(config.column());
                String valueB = rowB.values().get(config.column());
                boolean blankA = valueA == null || valueA.isBlank();
                boolean blankB = valueB == null || valueB.isBlank();
                // Blanks always come last, independent of sort direction.
                if (blankA || blankB) {
                    if (blankA && blankB) {
                        return 0;
                    }
                    return blankA ? 1 : -1;
                }
                int result = compareValues(
                        valueA,
                        valueB,
                        config.type(),
                        config.column()
                );

                return config.ascending() ? result : -result;
            };

            comparator = comparator.thenComparing(columnComparator);
        }
        return rows.stream()
                .sorted(comparator)
                .toList();
    }


    private static int compareValues(
            String a,
            String b,
            SortingTypes type,
            String column) {

        try {
            String valueA = a.trim();
            String valueB = b.trim();
            return switch (type) {
                case STRING ->
                        valueA.compareTo(valueB);
                case INTEGER ->
                        new BigInteger(valueA)
                                .compareTo(new BigInteger(valueB));
                case DECIMAL ->
                        new BigDecimal(valueA)
                                .compareTo(new BigDecimal(valueB));
                case DATE ->
                        Helpers.parseDateForComparison(valueA)
                                .compareTo(Helpers.parseDateForComparison(valueB));
            };
        } catch (RuntimeException e) {
            throw new IllegalArgumentException(
                    "Cannot sort column '" + column
                            + "' as " + type
                            + ": values '" + a
                            + "' and '" + b + "'",
                    e
            );
        }
    }


    public static void  compareRows(
            String file1Name,
            String file2Name,
            List<DataTypes.ExcelRow> file1,
            List<DataTypes.ExcelRow> file2,
            List<DataTypes.SortConfig> firstFileConfigs,
            List<DataTypes.SortConfig> secondFileConfigs, String resultsPath) {

        List<DataTypes.ExcelRow> sorted1 =
                sortRows(file1,
                        firstFileConfigs.toArray(DataTypes.SortConfig[]::new));

        List<DataTypes.ExcelRow> sorted2 =
                sortRows(file2,
                        secondFileConfigs.toArray(DataTypes.SortConfig[]::new));

        List<String[]> csvRows = new ArrayList<>();

        csvRows.add(new String[]{
                file1Name+" Excel Row",
                file2Name+" Excel Row",
                "Column",
                file1Name+" Value",
                file2Name+" Value",
                "Mismatch Type"
        });

        int maxRows = Math.max(sorted1.size(), sorted2.size());
        boolean hasDifferences = false;


        Map<String, String> headers1 = sorted1.getFirst().values().keySet().stream()
                .collect(Collectors.toMap(
                        ExcelHelpers::normalizeHeader,
                        Function.identity(),
                        (first, second) -> first
                ));

        Map<String, String> headers2 = sorted2.getFirst().values().keySet().stream()
                .collect(Collectors.toMap(
                        ExcelHelpers::normalizeHeader,
                        Function.identity(),
                        (first, second) -> first
                ));

        List<String> missingColumnsFromSheet2 = headers1.keySet().stream()
                .filter(element -> !headers2.containsKey(element))
                .toList();
        List<String> missingColumnsFromSheet1 = headers2.keySet().stream()
                .filter(element -> !headers1.containsKey(element))
                .toList();
        if(! missingColumnsFromSheet1.isEmpty()) {
            Loggers.logWarn(missingColumnsFromSheet1 + " are extra columns in second file that doesn't exist in first file");
            csvRows.add(new String[]{
                    "",
                    missingColumnsFromSheet1.toString(),
                    "",
                    "",
                    "",
                    "extra columns in second file that doesn't exist in first file "
            });

        }
        if (! missingColumnsFromSheet2.isEmpty()) {
            Loggers.logWarn(missingColumnsFromSheet2 + " are extra columns in first file that doesn't exist in second file");
            csvRows.add(new String[]{
                    missingColumnsFromSheet1.toString(),
                    "",
                    "",
                    "",
                    "",
                    "extra columns in first file that doesn't exist in second file"
            });

        }
        Set<String> columns = new TreeSet<>();
        missingColumnsFromSheet2.forEach(headers1.keySet()::remove);
        missingColumnsFromSheet1.forEach(headers2.keySet()::remove);
        columns.addAll(headers1.keySet());
        columns.addAll(headers2.keySet());

        for (int i = 0; i < maxRows; i++) {
            if (i >= sorted1.size()) {
                DataTypes.ExcelRow row2 = sorted2.get(i);
                csvRows.add(new String[]{
                        "",
                        String.valueOf(row2.rowNumber()),
                        "",
                        "",
                        row2.values().toString(),
                        "Extra row in File 2"
                });
                hasDifferences = true;
                continue;
            }

            if (i >= sorted2.size()) {
                DataTypes.ExcelRow row1 = sorted1.get(i);
//                Loggers.logError(
//                        "Extra row in File 1: Excel row "
//                                + row1.rowNumber()
//                                + ", values=" + row1.values()
//                );
                csvRows.add(new String[]{
                        String.valueOf(row1.rowNumber()),
                        "",
                        "",
                        row1.values().toString(),
                        "",
                        "Extra row in File 1"
                });
                hasDifferences = true;
                continue;
            }

            DataTypes.ExcelRow row1 = sorted1.get(i);
            DataTypes.ExcelRow row2 = sorted2.get(i);

            for (String column : columns) {
                String originalHeader1 = headers1.get(column);
                String originalHeader2 = headers2.get(column);

                String value1 = originalHeader1 == null
                        ? null : row1.values().get(originalHeader1);

                String value2 = originalHeader2 == null
                        ? null : row2.values().get(originalHeader2);

                if ((Helpers.isDate(value1) || Helpers.isDate(value2)) && (value1 != null && value2 !=null)){
                  value1= Helpers.formatDate("yyyy-MM-dd",value1);
                  value2= Helpers.formatDate("yyyy-MM-dd",value2);
                }

                if (!Objects.equals(value1, value2)) {
//                    Loggers.logError(
//                            "Mismatch in column '" + column + "'"
//                                    + " | File 1 Excel row " + row1.rowNumber()
//                                    + ", value='" + value1 + "'"
//                                    + " | File 2 Excel row " + row2.rowNumber()
//                                    + ", value='" + value2 + "'"
//                    );
                    System.out.println("comparing "+value1 +" and "+value2);

                    csvRows.add(new String[]{
                            String.valueOf(row1.rowNumber()),
                            String.valueOf(row2.rowNumber()),
                            column,
                            value1,
                            value2,
                            "Value mismatch"
                    });

                    hasDifferences = true;
                }
            }
        }

        Path reportPath =
                Path.of(resultsPath);

        CsvWriter.writeCsv(reportPath, csvRows);

        Loggers.logInfo(
                "CSV comparison report saved to: "
                        + reportPath.toAbsolutePath()
        );

        if (!hasDifferences) {
            Loggers.logInfo("Excel files match.");
        }
    }

    private static String normalizeHeader(String header) {
        return header == null ? null :
                header.replaceAll("[_\\/&\s-]+", "")
                        .toLowerCase(Locale.ROOT);
    }

}

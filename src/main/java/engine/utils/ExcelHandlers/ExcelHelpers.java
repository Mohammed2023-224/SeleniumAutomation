package engine.utils.ExcelHandlers;

import engine.enums.SortingTypes;
import engine.reporters.Loggers;
import engine.utils.DataTypes;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ExcelHelpers {

    private static List<DataTypes.ExcelRow> sortRows(
            List<DataTypes.ExcelRow> rows,
            DataTypes.SortConfig... configs) {

        Comparator<DataTypes.ExcelRow> comparator = (a, b) -> 0;

        for (DataTypes.SortConfig config : configs) {
            comparator = comparator.thenComparing((a, b) -> {
                String valueA = a.values().get(config.column());
                String valueB = b.values().get(config.column());

                // Keep blank values last in either sort direction.
                boolean blankA = valueA == null || valueA.isBlank();
                boolean blankB = valueB == null || valueB.isBlank();

                if (blankA || blankB) {
                    if (blankA && blankB) return 0;
                    return blankA ? 1 : -1;
                }

                int result = compareValues(
                        valueA, valueB, config.type(), config.column()
                );

                return config.ascending()
                        ? result
                        : -Integer.signum(result);
            });
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
            return switch (type) {
                case STRING ->
                        a.trim().compareToIgnoreCase(b.trim());
                case INTEGER ->
                        new java.math.BigInteger(a.trim())
                                .compareTo(new java.math.BigInteger(b.trim()));

                case DECIMAL ->
                        new java.math.BigDecimal(a.trim())
                                .compareTo(new java.math.BigDecimal(b.trim()));
                case DATE ->
                        java.time.LocalDate.parse(a.trim())
                                .compareTo(java.time.LocalDate.parse(b.trim()));
            };
        } catch (RuntimeException e) {
            throw new IllegalArgumentException(
                    "Cannot sort column '" + column
                            + "' as " + type
                            + ": values '" + a + "' and '" + b + "'",
                    e
            );
        }
    }


    public void compareRows(
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
        for (int i = 0; i < maxRows; i++) {
            if (i >= sorted1.size()) {
                DataTypes.ExcelRow row2 = sorted2.get(i);
                Loggers.logError(
                        "Extra row in File 2: Excel row "
                                + row2.rowNumber()
                                + ", values=" + row2.values()
                );
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
                Loggers.logError(
                        "Extra row in File 1: Excel row "
                                + row1.rowNumber()
                                + ", values=" + row1.values()
                );
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

//            Set<String> columns = new TreeSet<>();
            Map<String, String> headers1 = row1.values().keySet().stream()
                    .collect(Collectors.toMap(
                            this::normalizeHeader,
                            Function.identity(),
                            (first, second) -> first
                    ));

            Map<String, String> headers2 = row2.values().keySet().stream()
                    .collect(Collectors.toMap(
                            this::normalizeHeader,
                            Function.identity(),
                            (first, second) -> first
                    ));

            Set<String> columns = new TreeSet<>();
            columns.addAll(headers1.keySet());
            columns.addAll(headers2.keySet());

            for (String column : columns) {
                String originalHeader1 = headers1.get(column);
                String originalHeader2 = headers2.get(column);

                String value1 = originalHeader1 == null
                        ? null : row1.values().get(originalHeader1);

                String value2 = originalHeader2 == null
                        ? null : row2.values().get(originalHeader2);

                if (!Objects.equals(value1, value2)) {
                    Loggers.logError(
                            "Mismatch in column '" + column + "'"
                                    + " | File 1 Excel row " + row1.rowNumber()
                                    + ", value='" + value1 + "'"
                                    + " | File 2 Excel row " + row2.rowNumber()
                                    + ", value='" + value2 + "'"
                    );

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

    private String normalizeHeader(String header) {
        return header == null ? null :
                header.replaceAll("[_\\/\s]+", "")
                        .toLowerCase(Locale.ROOT);
    }

}

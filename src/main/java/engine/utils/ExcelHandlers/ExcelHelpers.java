package engine.utils.ExcelHandlers;

import engine.enums.ColumnTypes;
import engine.reporters.Loggers;
import engine.utils.DataTypes;
import engine.utils.Helpers;
import engine.utils.propertyFilesHandlers.PropertyLoader;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        List<DataTypes.ExcelRow> sortedRows =
                new ArrayList<>(rows);
        Comparator<DataTypes.ExcelRow> comparator = null;

        for (DataTypes.SortConfig config : configs) {

            Comparator<DataTypes.ExcelRow> currentComparator =
                    (row1, row2) -> {

                        String value1 =
                                getSortValue(
                                        row1,
                                        config.column()
                                );

                        String value2 =
                                getSortValue(
                                        row2,
                                        config.column()
                                );

                        boolean empty1 =
                                value1 == null || value1.isBlank();

                        boolean empty2 =
                                value2 == null || value2.isBlank();

                        /*
                         * Always put empty values last.
                         */
                        if (empty1 && empty2) {
                            return 0;
                        }

                        if (empty1) {
                            return 1;
                        }

                        if (empty2) {
                            return -1;
                        }

                        int comparison =
                                compareValues(
                                        value1,
                                        value2,
                                        config.type(),
                                        config.column()
                                );

                        if (!config.ascending()) {
                            comparison = -comparison;
                        }

                        return comparison;
                    };

            if (comparator == null) {
                comparator = currentComparator;
            } else {
                comparator =
                        comparator.thenComparing(currentComparator);
            }
        }

        sortedRows.sort(comparator);

        return sortedRows;
    }

    private static String getSortValue(
            DataTypes.ExcelRow row,
            String column) {

        String normalizedColumn =
                normalizeHeader(column);

        for (Map.Entry<String, String> entry
                : row.values().entrySet()) {

            if (normalizeHeader(entry.getKey())
                    .equals(normalizedColumn)) {

                return entry.getValue();
            }
        }

        throw new IllegalArgumentException(
                "Sorting column '"
                        + column
                        + "' was not found. Available columns: "
                        + row.values().keySet()
        );
    }


    private static String getColumnValue(
            DataTypes.ExcelRow row,
            String requestedColumn) {

        if (row == null || requestedColumn == null) {
            return null;
        }

        String normalizedRequested =
                normalizeHeader(requestedColumn);

        for (Map.Entry<String, String> entry
                : row.values().entrySet()) {

            String normalizedActual =
                    normalizeHeader(entry.getKey());

            if (normalizedActual.equals(normalizedRequested)) {
                return entry.getValue();
            }
        }

        return null;
    }


    private static int compareValues(
            String a,
            String b,
            ColumnTypes type,
            String column) {

        try {
            String valueA = a.trim();
            String valueB = b.trim();
            return switch (type) {
                case STRING ->
                        valueA.compareTo(valueB);
                case INTEGER, DECIMAL ->
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

    private static String normalizeHeader(String header) {
        return header == null ? null :
                header.replaceAll("[_\\/&\s-]+", "")
                        .toLowerCase(Locale.ROOT);
    }


    public static void compareRows(String configurationFile) {
        // Load configuration
        Map<String, Object> configFile =
                PropertyLoader.loadAsMap(configurationFile);
        // Get List for both special columns
        List<DataTypes.SpecialColumnConfig> file1SpecialColumns =
                parseSpecialColumns(
                        (String) configFile.get("file1SpecialColumns")
                );
        List<DataTypes.SpecialColumnConfig> file2SpecialColumns =
                parseSpecialColumns(
                        (String) configFile.get("file2SpecialColumns")
                );
        // Convert lists into a map containing
        Map<String, DataTypes.SpecialColumnConfig> file1SpecialColumnMap =
                createSpecialColumnMap(file1SpecialColumns);

        Map<String, DataTypes.SpecialColumnConfig> file2SpecialColumnMap =
                createSpecialColumnMap(file2SpecialColumns);

//        List<String> specialColumns=file1SpecialColumnMap.get(column);

        // Create excel file objects
        ExcelReader file1Reader =
                new ExcelReader(
                        (String) configFile.get("firstFile"),
                        (String) configFile.get("file1Sheet")
                );

        ExcelReader file2Reader =
                new ExcelReader(
                        (String) configFile.get("secondFile"),
                        (String) configFile.get("file2Sheet")
                );

            // read excel as list containing the row number and the records for the row as linked hash map then sort the values as per any record in linked hash maps and return the sorted list
           List<DataTypes.ExcelRow> sorted1 =
                   sortRows(
                           file1Reader.readListedMapNoColumnCondition(),
                           getSortingValues(
                                   (String) configFile.get("file1Sorting")
                           ).toArray(DataTypes.SortConfig[]::new)
                   );

        List<DataTypes.ExcelRow> sorted2 =
                sortRows(
                        file2Reader.readListedMapNoColumnCondition(),
                        getSortingValues(
                                (String) configFile.get("file2Sorting")
                        ).toArray(DataTypes.SortConfig[]::new)
                );


        // Prepare the header for the CSV file
        List<String[]> csvRows = new ArrayList<>();
        csvRows.add(new String[]{
                configFile.get("firstFile") + " Excel Row",
                configFile.get("secondFile") + " Excel Row",
                "Column",
                configFile.get("file1Sheet") + " Original Value",
                configFile.get("file1Sheet") + " Compared Value",
                configFile.get("file2Sheet") + " Original Value",
                configFile.get("file2Sheet") + " Compared Value",
                "Transformation",
                "Mismatch Type"
        });

        // Check for number of records
        int maxRows = Math.max(sorted1.size(), sorted2.size());
        boolean hasDifferences = false;

        // Validation check if files are empty
        if (sorted1.isEmpty() && sorted2.isEmpty()) {
            Loggers.logInfo("Both Excel files are empty.");
            return;
        }

        // Get the headers of the excel sheet
        Map<String, String> headers1 =
                sorted1.isEmpty()
                        ? new HashMap<>()
                        : sorted1.getFirst()
                        .values()
                        .keySet()
                        .stream()
                        .collect(Collectors.toMap(
                                ExcelHelpers::normalizeHeader,
                                Function.identity(),
                                (first, second) -> first,
                                HashMap::new
                        ));

        Map<String, String> headers2 =
                sorted2.isEmpty()
                        ? new HashMap<>()
                        : sorted2.getFirst()
                        .values()
                        .keySet()
                        .stream()
                        .collect(Collectors.toMap(
                                ExcelHelpers::normalizeHeader,
                                Function.identity(),
                                (first, second) -> first,
                                HashMap::new
                        ));

        // Get extra columns in every sheet
        List<String> missingColumnsFromSheet2 =
                headers1.keySet()
                        .stream()
                        .filter(column -> !headers2.containsKey(column))
                        .toList();

        List<String> missingColumnsFromSheet1 =
                headers2.keySet()
                        .stream()
                        .filter(column -> !headers1.containsKey(column))
                        .toList();

        // check missing columns in both sheets and report them
        if (!missingColumnsFromSheet1.isEmpty()) {
            Loggers.logWarn(
                    missingColumnsFromSheet1
                            + " are extra columns in the second file "
                            + "that do not exist in the first file"
            );
            csvRows.add(new String[]{
                    "",
                    "",
                    missingColumnsFromSheet1.toString(),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "Extra columns in File 2"
            });
            hasDifferences = true;
        }

        if (!missingColumnsFromSheet2.isEmpty()) {
            Loggers.logWarn(
                    missingColumnsFromSheet2
                            + " are extra columns in the first file "
                            + "that do not exist in the second file"
            );
            csvRows.add(new String[]{
                    "",
                    "",
                    missingColumnsFromSheet2.toString(),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "Extra columns in File 1"
            });
            hasDifferences = true;
        }

        // Keep only common columns. and create the common columns
        Set<String> columns = new TreeSet<>();
        headers1.keySet().removeAll(missingColumnsFromSheet2);
        headers2.keySet().removeAll(missingColumnsFromSheet1);
        columns.addAll(headers1.keySet());
        columns.addAll(headers2.keySet());

// Compare the row count and detect extra row in every sheet
        for (int i = 0; i < maxRows; i++) {
            if (i >= sorted1.size()) {
                DataTypes.ExcelRow row2 = sorted2.get(i);
                csvRows.add(new String[]{
                        "",
                        String.valueOf(row2.rowNumber()),
                        "",
                        "",
                        "",
                        row2.values().toString(),
                        row2.values().toString(),
                        "",
                        "Extra row in File 2"
                });
                hasDifferences = true;
                continue;
            }

            if (i >= sorted2.size()) {
                DataTypes.ExcelRow row1 = sorted1.get(i);
                csvRows.add(new String[]{
                        String.valueOf(row1.rowNumber()),
                        "",
                        "",
                        row1.values().toString(),
                        row1.values().toString(),
                        "",
                        "",
                        "",
                        "Extra row in File 1"
                });
                hasDifferences = true;
                continue;
            }

            // Get rows
            DataTypes.ExcelRow row1 = sorted1.get(i);
            DataTypes.ExcelRow row2 = sorted2.get(i);

            // Loop over columns for the row
            for (String column : columns) {
                String originalHeader1 = headers1.get(column);
                String originalHeader2 = headers2.get(column);
                String originalValue1 =
                        originalHeader1 == null
                                ? null
                                : row1.values().get(originalHeader1);

                String originalValue2 =
                        originalHeader2 == null
                                ? null
                                : row2.values().get(originalHeader2);

                // create compare objects
                String comparedValue1 = originalValue1;
                String comparedValue2 = originalValue2;

                // create special configs
                DataTypes.SpecialColumnConfig specialConfig1 =
                        file1SpecialColumnMap.get(column);
                DataTypes.SpecialColumnConfig specialConfig2 =
                        file2SpecialColumnMap.get(column);

                List<String> appliedTransformations = new ArrayList<>();
                if (specialConfig1 != null) {

                    comparedValue1 = TransformationHelpers.transformValue(
                            originalValue1,
                            specialConfig1
                    );

                    appliedTransformations.add(
                            "File 1: "
                                    + TransformationHelpers.describeTransformations(
                                    specialConfig1
                            )
                    );
                }

                // CHANGE: Transform the File 2 value.
                if (specialConfig2 != null) {

                    comparedValue2 = TransformationHelpers.transformValue(
                            originalValue2,
                            specialConfig2
                    );

                    appliedTransformations.add(
                            "File 2: "
                                    + TransformationHelpers.describeTransformations(
                                    specialConfig2
                            )
                    );
                }

                if (specialConfig1 == null
                        && specialConfig2 == null
                        && comparedValue1 != null
                        && comparedValue2 != null
                        && Helpers.isDate(comparedValue1)
                        && Helpers.isDate(comparedValue2)) {

                    comparedValue1 =
                            Helpers.formatDates(
                                    "yyyy-MM-dd",
                                    comparedValue1
                            );

                    comparedValue2 =
                            Helpers.formatDates(
                                    "yyyy-MM-dd",
                                    comparedValue2
                            );

                    appliedTransformations.add(
                            "Automatic date normalization: yyyy-MM-dd"
                    );
                }

                String transformationDescription =
                        appliedTransformations.isEmpty()
                                ? ""
                                : String.join(
                                " | ",
                                appliedTransformations
                        );
                boolean piiMatched =
                        TransformationHelpers.isPiiMatch(
                                originalValue1,
                                originalValue2,
                                specialConfig1,
                                specialConfig2
                        );
                boolean bothEmpty =
                        (originalValue1 == null || originalValue1.isBlank())
                                &&
                                (originalValue2 == null || originalValue2.isBlank());
                if (bothEmpty
                        && (specialConfig1 != null || specialConfig2 != null)) {

                    csvRows.add(new String[]{
                            String.valueOf(row1.rowNumber()),
                            String.valueOf(row2.rowNumber()),
                            column,
                            safeValue(originalValue1),
                            safeValue(comparedValue1),
                            safeValue(originalValue2),
                            safeValue(comparedValue2),
                            transformationDescription,
                            "Matched values - both empty"
                    });

                } else if (piiMatched) {

                    csvRows.add(new String[]{
                            String.valueOf(row1.rowNumber()),
                            String.valueOf(row2.rowNumber()),
                            column,
                            safeValue(originalValue1),
                            safeValue(comparedValue1),
                            safeValue(originalValue2),
                            safeValue(comparedValue2),
                            transformationDescription,
                            "Matched PII encryption condition"
                    });

                } else if (!Objects.equals(comparedValue1, comparedValue2)) {

                    csvRows.add(new String[]{
                            String.valueOf(row1.rowNumber()),
                            String.valueOf(row2.rowNumber()),
                            column,
                            safeValue(originalValue1),
                            safeValue(comparedValue1),
                            safeValue(originalValue2),
                            safeValue(comparedValue2),
                            transformationDescription,
                            "Value mismatch"
                    });

                    hasDifferences = true;

                } else if (!transformationDescription.isBlank()) {

                    csvRows.add(new String[]{
                            String.valueOf(row1.rowNumber()),
                            String.valueOf(row2.rowNumber()),
                            column,
                            safeValue(originalValue1),
                            safeValue(comparedValue1),
                            safeValue(originalValue2),
                            safeValue(comparedValue2),
                            transformationDescription,
                            "Matched after transformation"
                    });
                }
            }
        }

        Path reportPath =
                Path.of((String) configFile.get("resultsPath"));

        CsvWriter.writeCsv(reportPath, csvRows);

        Loggers.logInfo(
                "CSV comparison report saved to: "
                        + reportPath.toAbsolutePath()
        );

        if (!hasDifferences) {
            Loggers.logInfo("Excel files match.");
        }
    }

    private static List<DataTypes.SortConfig> getSortingValues(String input){
        List<DataTypes.SortConfig> newList= new ArrayList<>();
        if (input.isEmpty()){
            return  List.of();
        }
        String[] splitInput= input.split(",");
        for (String s: splitInput){
            String[] secondSplitString= s.split(":");
            String columnName=secondSplitString[0].trim();
            String columnType=secondSplitString[1].trim();
            String columnSortingDirection=secondSplitString[2].trim();
            newList.add(new DataTypes.SortConfig(columnName, defineSortingClassesByString(columnType),defineDirection(columnSortingDirection)));
        }
        return newList;
    }

    private static String getSpecialCriteria(String input){
       return "";
    }

    private Class<?> defineClassesByString(String type) {
        return switch (type.trim().toLowerCase()) {
            case "string" -> String.class;
            case "integer", "int" -> Integer.class;
            case "long" -> Long.class;
            case "double" -> Double.class;
            case "float" -> Float.class;
            case "boolean" -> Boolean.class;
            case "date" -> LocalDate.class;
            case "datetime" -> LocalDateTime.class;
            default -> throw new IllegalArgumentException(
                    "Unsupported data type: " + type
            );
        };
    }

    private static ColumnTypes defineSortingClassesByString(String type) {
        return switch (type.trim().toLowerCase()) {
            case "string" -> ColumnTypes.STRING;
            case "integer", "int" -> ColumnTypes.INTEGER;
            case "float", "decimal" -> ColumnTypes.DECIMAL;
            case "date" -> ColumnTypes.DATE;
//            case "datetime" -> SortingTypes.DATETIME;
            default -> throw new IllegalArgumentException(
                    "Unsupported data type: " + type
            );
        };
    }

    private static Boolean defineDirection(String direction) {
        return switch (direction.trim().toLowerCase()) {
            case "asc" ,"ascending" -> true;
            case "desc", "descending" -> false;
            default -> throw new IllegalArgumentException(
                    "Unsupported direction: "
            );
        };
    }

    /**
     * 
     * @param input string with the format {column name:data type: transformation}
     * @return List<DataTypes.SpecialColumnConfig> from all consolidated blocks in the string {column: data type: transformation}
     *  ; as a split for another column transform
     *  | as a second transformation for the same column
     *  EX. column1: string: trim ; column2: int: add(5)
     *  Returns List<DataTypes.SpecialColumnConfig>
     */
    private static List<DataTypes.SpecialColumnConfig> parseSpecialColumns(String input){
                if (input == null || input.isBlank()) {
            return List.of();
        }
                List<DataTypes.SpecialColumnConfig> result = new ArrayList<>();
        String[] columnConfigurations = input.split(";");
        for (String columnConfiguration : columnConfigurations) {

            if (columnConfiguration.isBlank()) {
                continue;
            }
            String[] parts = columnConfiguration.trim().split(":");
                        if (parts.length < 2) {
                throw new IllegalArgumentException(
                        "Invalid special-column configuration: "
                                + columnConfiguration
                );
            }
            String column = parts[0].toLowerCase().trim();
            String type =
                    parts[1].toLowerCase().trim();
            List<DataTypes.Transformation> transformations =
                    parts.length == 3
                            ? TransformationHelpers.parseTransformations(parts[2])
                            : List.of();
            result.add(
                    new DataTypes.SpecialColumnConfig(
                            column,
                            defineSortingClassesByString(type),
                            transformations
                    )
            );
        }
        return result;
    }

    private static Map<String, DataTypes.SpecialColumnConfig>
    createSpecialColumnMap(
            List<DataTypes.SpecialColumnConfig> configurations) {
        if (configurations == null || configurations.isEmpty()) {
            return Map.of();
        }
        return configurations.stream()
                .collect(Collectors.toMap(
                        config -> ExcelHelpers.normalizeHeader(
                                config.column()
                        ),
                        Function.identity(),
                        (first, second) -> {
                            throw new IllegalArgumentException(
                                    "Duplicate special-column configuration "
                                            + "for column: "
                                            + first.column()
                            );
                        }
                ));
    }

    private static String safeValue(String value) {
        return value == null ? "" : value;
    }


}

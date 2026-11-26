package engine.utils.ExcelHandlers;

import engine.enums.ColumnTypes;
import engine.utils.DataTypes;
import engine.utils.Helpers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TransformationHelpers {

    private static final Pattern TRANSFORMATION_PATTERN =
            Pattern.compile("([a-zA-Z]+)(?:\\((.*)\\))?");

    public static List<DataTypes.Transformation> parseTransformations(
            String configuration) {
        if (configuration == null || configuration.isBlank()) {
            return List.of();
        }
        List<DataTypes.Transformation> transformations = new ArrayList<>();
        for (String transformationText : configuration.split("\\|")) {
            String value = transformationText.trim();
            Matcher matcher = TRANSFORMATION_PATTERN.matcher(value);
            if (!matcher.matches()) {
                throw new IllegalArgumentException(
                        "Invalid transformation: " + value
                );
            }
            String operation = matcher.group(1)
                    .trim()
                    .toLowerCase(Locale.ENGLISH);
            String argument = matcher.group(2);
            if (argument != null) {
                argument = argument.trim();
            }
            transformations.add(
                    new DataTypes.Transformation(operation, argument)
            );
        }
        return transformations;
    }

    private static String applyStringTransformation(
            String value,
            String operation,
            String argument) {
        return switch (operation) {
            case "trim" ->
                    value.trim();
            case "uppercase" ->
                    value.toUpperCase(Locale.ENGLISH);
            case "lowercase" ->
                    value.toLowerCase(Locale.ENGLISH);
            case "prefix" ->
                    requireArgument(operation, argument) + value;
            case "suffix" ->
                    value + requireArgument(operation, argument);
            case "remove" ->
                    value.replace(
                            requireArgument(operation, argument),
                            ""
                    );
            case "presence" ->
                    value == null || value.isBlank()
                            ? "EMPTY"
                            : "HAS_VALUE";
            case "replace" -> {
                String[] arguments =
                        requireArgument(operation, argument).split(",", 2);

                if (arguments.length != 2) {
                    throw new IllegalArgumentException(
                            "replace requires two arguments: replace(old,new)"
                    );
                }
                yield value.replace(arguments[0], arguments[1]);
            }
            case "removeletters" ->
                    value.replaceAll("[a-zA-Z]", "");
            case "removedigits" ->
                    value.replaceAll("\\d", "");
            case "removespaces" ->
                    value.replaceAll("\\s+", "");
            default ->
                    throw new IllegalArgumentException(
                            "Unsupported STRING operation: " + operation
                    );
        };
    }

    private static String applyIntegerTransformation(
            String value,
            String operation,
            String argument) {

        BigInteger number = new BigInteger(value.trim());

        return switch (operation) {

            case "add" ->
                    number.add(
                            new BigInteger(requireArgument(operation, argument))
                    ).toString();

            case "subtract" ->
                    number.subtract(
                            new BigInteger(requireArgument(operation, argument))
                    ).toString();

            case "multiply" ->
                    number.multiply(
                            new BigInteger(requireArgument(operation, argument))
                    ).toString();

            case "divide" ->
                    number.divide(
                            new BigInteger(requireArgument(operation, argument))
                    ).toString();

            case "absolute" ->
                    number.abs().toString();

            default ->
                    throw new IllegalArgumentException(
                            "Unsupported INTEGER operation: " + operation
                    );
        };
    }

    private static String applyDecimalTransformation(
            String value,
            String operation,
            String argument) {

        // Handle accounting negative first: (100) -> -100
        if ("accountingnegative".equals(operation)) {

            String trimmed = value.trim();

            if (trimmed.startsWith("(")
                    && trimmed.endsWith(")")) {

                String number =
                        trimmed.substring(
                                1,
                                trimmed.length() - 1
                        ).trim();

                return new BigDecimal(number)
                        .negate()
                        .stripTrailingZeros()
                        .toPlainString();
            }

            // Already normal numeric value
            return new BigDecimal(trimmed)
                    .stripTrailingZeros()
                    .toPlainString();
        }

        BigDecimal number =
                new BigDecimal(value.trim());

        return switch (operation) {

            case "add" ->
                    number.add(
                                    new BigDecimal(
                                            requireArgument(operation, argument)
                                    )
                            )
                            .stripTrailingZeros()
                            .toPlainString();

            case "subtract" ->
                    number.subtract(
                                    new BigDecimal(
                                            requireArgument(operation, argument)
                                    )
                            )
                            .stripTrailingZeros()
                            .toPlainString();

            case "multiply" ->
                    number.multiply(
                                    new BigDecimal(
                                            requireArgument(operation, argument)
                                    )
                            )
                            .stripTrailingZeros()
                            .toPlainString();

            case "divide" -> {

                BigDecimal divisor =
                        new BigDecimal(
                                requireArgument(operation, argument)
                        );

                yield number.divide(
                                divisor,
                                10,
                                RoundingMode.HALF_UP
                        )
                        .stripTrailingZeros()
                        .toPlainString();
            }

            case "round" ->
                    number.setScale(
                                    Integer.parseInt(
                                            requireArgument(operation, argument)
                                    ),
                                    RoundingMode.HALF_UP
                            )
                            .toPlainString();

            default ->
                    throw new IllegalArgumentException(
                            "Unsupported DECIMAL operation: "
                                    + operation
                    );
        };
    }

    private static String applyDateTransformation(
            String value,
            String operation,
            String argument) {

        return switch (operation) {

            case "format" ->
                    Helpers.formatDates(
                            requireArgument(operation, argument),
                            value
                    );

            case "adddays" ->
                    Helpers.parseDateForComparison(value)
                            .plusDays(Long.parseLong(
                                    requireArgument(operation, argument)
                            ))
                            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            case "subtractdays" ->
                    Helpers.parseDateForComparison(value)
                            .minusDays(Long.parseLong(
                                    requireArgument(operation, argument)
                            ))
                            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            default ->
                    throw new IllegalArgumentException(
                            "Unsupported DATE operation: " + operation
                    );
        };
    }

    private static String requireArgument(
            String operation,
            String argument) {
        if (argument == null || argument.isBlank()) {
            throw new IllegalArgumentException(
                    "Operation '" + operation + "' requires an argument"
            );
        }
        return argument;
    }

    public static String describeTransformations(
            DataTypes.SpecialColumnConfig config) {

        if (config == null) {
            return "";
        }

        if (config.transformations() == null
                || config.transformations().isEmpty()) {

            return config.type().name();
        }
        String operations = config.transformations()
                .stream()
                .map(transformation -> {

                    if (transformation.argument() == null
                            || transformation.argument().isBlank()) {

                        return transformation.operation();
                    }

                    return transformation.operation()
                            + "("
                            + transformation.argument()
                            + ")";
                })
                .collect(Collectors.joining("|"));
        return config.type().name() + ":" + operations;
    }

    public static String transformValue(
            String originalValue,
            DataTypes.SpecialColumnConfig config) {

        if (config == null
                || config.transformations() == null
                || config.transformations().isEmpty()) {

            return originalValue;
        }
        String result = originalValue;
        for (DataTypes.Transformation transformation
                : config.transformations()) {

            String operation =
                    transformation.operation()
                            .trim();

            switch (operation.toLowerCase(Locale.ENGLISH)) {
                case "piiplain" -> {
                    if (result == null || result.isBlank()) {
                        result = "";
                    } else if (isEncryptedValue(result)) {
                        result = "INVALID_ENCRYPTED";
                    } else {
                        result = "PII";
                    }
                }

                case "piiencrypted" -> {
                    if (result == null || result.isBlank()) {
                        result = "";
                    } else if (isEncryptedValue(result)) {
                        result = "PII";

                    } else {
                        result = "INVALID_PLAIN_TEXT";
                    }
                }

                default -> {

                    if (result == null) {
                        return null;
                    }

                    result = applyTransformation(
                            result,
                            config.type(),
                            transformation
                    );
                }
            }
        }

        return result;
    }

    public static String applyTransformation(
            String value,
            ColumnTypes type,
            DataTypes.Transformation transformation) {
        String operation = transformation.operation()
                .trim()
                .toLowerCase(Locale.ENGLISH);
        String argument = transformation.argument();
        try {
            return switch (type) {
                case STRING ->
                        applyStringTransformation(
                                value,
                                operation,
                                argument
                        );
                case INTEGER ->
                        applyIntegerTransformation(
                                value,
                                operation,
                                argument
                        );
                case DECIMAL ->
                        applyDecimalTransformation(
                                value,
                                operation,
                                argument
                        );
                case DATE ->
                        applyDateTransformation(
                                value,
                                operation,
                                argument
                        );
            };
        } catch (RuntimeException e) {
            throw new IllegalArgumentException(
                    "Failed to transform value '"
                            + value
                            + "' as "
                            + type
                            + " using "
                            + operation
                            + (argument == null
                            ? ""
                            : "(" + argument + ")"),
                    e
            );
        }
    }

    private static boolean isEncryptedValue(String value) {

        if (value == null || value.isBlank()) {
            return false;
        }

        String trimmed = value.trim();

        return trimmed.startsWith("ASbusE")
                && trimmed.matches(
                "^[A-Za-z0-9+/]+={0,2}$"
        );
    }

    private static boolean isBase64(String value) {

        if (value == null || value.isBlank()) {
            return false;
        }

        try {
            Base64.getDecoder().decode(value.trim());
            return true;

        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean isPiiMatch(
            String originalValue1,
            String originalValue2,
            DataTypes.SpecialColumnConfig config1,
            DataTypes.SpecialColumnConfig config2) {

        boolean file1PiiPlain =
                hasTransformation(config1, "piiplain");

        boolean file2PiiEncrypted =
                hasTransformation(config2, "piiencrypted");

        /*
         * Handle configuration where only File 2 declares
         * that encryption is required.
         */
        if (file2PiiEncrypted) {

            boolean file1Empty =
                    originalValue1 == null
                            || originalValue1.isBlank();

            boolean file2Empty =
                    originalValue2 == null
                            || originalValue2.isBlank();

            // Blank -> Blank is valid
            if (file1Empty && file2Empty) {
                return true;
            }

            // One blank and one populated is NOT valid
            if (file1Empty || file2Empty) {
                return false;
            }

            // File 1 contains PII and File 2 must be encrypted
            return isEncryptedValue(originalValue2);
        }

        /*
         * If both sides explicitly use PII transformations.
         */
        if (file1PiiPlain && file2PiiEncrypted) {

            return Objects.equals(
                    transformValue(originalValue1, config1),
                    transformValue(originalValue2, config2)
            );
        }

        return false;
    }

    private static boolean hasTransformation(
            DataTypes.SpecialColumnConfig config,
            String operation) {

        if (config == null
                || config.transformations() == null) {
            return false;
        }

        return config.transformations()
                .stream()
                .anyMatch(transformation ->
                        transformation.operation()
                                .equalsIgnoreCase(operation)
                );
    }
}
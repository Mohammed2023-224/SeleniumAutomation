package engine.utils;

import engine.reporters.Loggers;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQueries;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helpers {
    private Helpers(){}
    static String formatPattern="yyyy-MM-dd";

    public static String extractTextUsingRegex(String text,String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public static String extractTextUsingRegex(String text, String regex, int group) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(group);
        }
        return null;
    }

    public static <T> boolean isSorted(List<T> list, Comparator<T> comparator) {
        for (int i = 1; i < list.size(); i++) {
            if (comparator.compare(list.get(i - 1), list.get(i)) > 0) {
                return false;
            }
        }
        return true;
    }

    public static int daysBetweenToday(String date) {
        String formattedDate = formatDates("YYYY-MM-dd", date);
        LocalDate givenDate = LocalDate.parse(
                formattedDate,
                DateTimeFormatter.ISO_LOCAL_DATE
        );
        LocalDate today = LocalDate.now();
        return Math.toIntExact(ChronoUnit.DAYS.between(today, givenDate));
    }


    public static String getCurrentDay() {
        return LocalDate.now()
                .format(DateTimeFormatter.ofPattern(formatPattern));
    }

    public static String getCurrentDaysAddedNumberOfDays(int numberOfDays) {
        return LocalDate.now().plusDays(numberOfDays)
                .format(DateTimeFormatter.ofPattern(formatPattern));
    }

    public static String getDateAddedNumberOfDays(String date, int numberOfDays) {
        return LocalDate.parse(date).plusDays(numberOfDays)
                .format(DateTimeFormatter.ofPattern(formatPattern));
    }

    private static final List<DateTimeFormatter> SUPPORTED_FORMATS =
            List.of(
                    // ISO formats
                    DateTimeFormatter.ISO_OFFSET_DATE_TIME,
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME,
                    DateTimeFormatter.ISO_LOCAL_DATE,

                    // US Excel date and time: four-digit year
                    DateTimeFormatter.ofPattern(
                            "M/d/uuuu h:mm:ss a",
                            Locale.ENGLISH
                    ),
                    DateTimeFormatter.ofPattern(
                            "M/d/uuuu h:mm a",
                            Locale.ENGLISH
                    ),
                    DateTimeFormatter.ofPattern(
                            "M/d/uuuu H:mm:ss",
                            Locale.ENGLISH
                    ),
                    DateTimeFormatter.ofPattern(
                            "M/d/uuuu H:mm",
                            Locale.ENGLISH
                    ),

                    // US Excel date and time: two-digit year
                    DateTimeFormatter.ofPattern(
                            "M/d/uu H:mm:ss",
                            Locale.ENGLISH
                    ),
                    DateTimeFormatter.ofPattern(
                            "M/d/uu H:mm",
                            Locale.ENGLISH
                    ),

                    // US Excel date-only
                    DateTimeFormatter.ofPattern(
                            "M/d/uuuu",
                            Locale.ENGLISH
                    ),
                    DateTimeFormatter.ofPattern(
                            "M/d/uu",
                            Locale.ENGLISH
                    ),

                    // Text dates
                    DateTimeFormatter.ofPattern(
                            "MMMM d, uuuu",
                            Locale.ENGLISH
                    ),
                    DateTimeFormatter.ofPattern(
                            "d MMMM uuuu",
                            Locale.ENGLISH
                    ),
                    DateTimeFormatter.ofPattern(
                            "d MMM uuuu",
                            Locale.ENGLISH
                    ),

                    // Other unambiguous formats
                    DateTimeFormatter.ofPattern(
                            "uuuu/MM/dd",
                            Locale.ENGLISH
                    ),
                    DateTimeFormatter.ofPattern(
                            "uuuu-MMMM-d",
                            Locale.ENGLISH
                    )
            );


    public static String formatDates(String outFormat, String inputDate) {

        if (inputDate == null || inputDate.isBlank()) {
            return "";
        }

        String value = inputDate.trim();

        DateTimeFormatter outputFormatter =
                DateTimeFormatter.ofPattern(outFormat, Locale.ENGLISH);

        boolean outputRequiresTime = requiresTime(outFormat);
        boolean outputRequiresZone = requiresZone(outFormat);

        for (DateTimeFormatter inputFormatter : SUPPORTED_FORMATS) {
            try {
                TemporalAccessor parsed = inputFormatter.parse(value);

                LocalDate date = parsed.query(TemporalQueries.localDate());

                if (date == null) {
                    continue;
                }

                LocalTime time = parsed.query(TemporalQueries.localTime());
                ZoneOffset offset = parsed.query(TemporalQueries.offset());

                if (time == null && outputRequiresTime) {
                    time = LocalTime.MIDNIGHT;
                }

                if (time == null) {
                    return date.format(outputFormatter);
                }

                if (outputRequiresZone) {
                    ZoneOffset effectiveOffset =
                            offset != null ? offset : ZoneOffset.UTC;

                    return OffsetDateTime.of(
                            date,
                            time,
                            effectiveOffset
                    ).format(outputFormatter);
                }

                return LocalDateTime.of(date, time)
                        .format(outputFormatter);

            } catch (DateTimeParseException ignored) {
                // Expected: try the next supported input format
            }
        }

        throw new IllegalArgumentException(
                "Unsupported date format: " + inputDate
        );
    }

    public static Optional<LocalDate> tryParseDate(String input) {
        if (input == null || input.isBlank()) {
            return Optional.empty();
        }
        String value = input.trim();
        for (DateTimeFormatter formatter : SUPPORTED_FORMATS) {
            try {
                TemporalAccessor parsed = formatter.parse(value);
                LocalDate date = parsed.query(TemporalQueries.localDate());
                if (date != null) {
                    return Optional.of(date);
                }
            } catch (DateTimeParseException ignored) {
                // Expected when probing candidate formats—do not log as an error
            }
        }
        return Optional.empty();
    }

    public static boolean isDate(String input) {
        return tryParseDate(input).isPresent();
    }

    public static boolean isSameCell(String cell1, String cell2) {
        Optional<LocalDate> date1 = tryParseDate(cell1);
        Optional<LocalDate> date2 = tryParseDate(cell2);

        if (date1.isPresent() && date2.isPresent()) {
            return date1.get().getYear() == date2.get().getYear();
        }
        return Objects.equals(cell1, cell2);
    }


    private static boolean requiresTime(String pattern) {
        return pattern.matches(".*[HhKkmsSna].*");
    }

    private static boolean requiresZone(String pattern) {
        return pattern.matches(".*[XxZzOV].*");
    }


    public static LocalDateTime parseDateForComparison(String input) {
        String value = input.trim();
        for (DateTimeFormatter formatter : SUPPORTED_FORMATS) {
            try {
                TemporalAccessor parsed = formatter.parse(value);

                LocalDate date = parsed.query(TemporalQueries.localDate());
                LocalTime time = parsed.query(TemporalQueries.localTime());
                ZoneOffset offset = parsed.query(TemporalQueries.offset());
                if (date == null) {
                    continue;
                }
                // Treat date-only values as midnight.
                if (time == null) {
                    time = LocalTime.MIDNIGHT;
                }
                LocalDateTime dateTime = LocalDateTime.of(date, time);
                // Convert offset-aware values to UTC for comparison.
                if (offset != null) {
                    return OffsetDateTime.of(dateTime, offset)
                            .withOffsetSameInstant(ZoneOffset.UTC)
                            .toLocalDateTime();
                }

                return dateTime;

            } catch (DateTimeParseException ignored) {
                // Try the next supported format.
            }
        }
        throw new IllegalArgumentException(
                "Unsupported date format: " + input
        );
    }


    private static final List<DateTimeFormatter> SUPPORTED_DATE_FORMATS = List.of(

            // Excel display formats
            DateTimeFormatter.ofPattern(
                    "M/d/uuuu h:mm:ss a",
                    Locale.ENGLISH
            ),
            DateTimeFormatter.ofPattern(
                    "M/d/uuuu h:mm a",
                    Locale.ENGLISH
            ),
            DateTimeFormatter.ofPattern(
                    "M/d/uuuu H:mm:ss",
                    Locale.ENGLISH
            ),
            DateTimeFormatter.ofPattern(
                    "M/d/uuuu H:mm",
                    Locale.ENGLISH
            ),
            DateTimeFormatter.ofPattern(
                    "M/d/uuuu",
                    Locale.ENGLISH
            ),

            // Common normalized date-time formats
            DateTimeFormatter.ofPattern(
                    "MM/dd/uuuu HH:mm:ss",
                    Locale.ENGLISH
            ),
            DateTimeFormatter.ofPattern(
                    "MM/dd/uuuu HH:mm",
                    Locale.ENGLISH
            ),

            // ISO date-time formats
            DateTimeFormatter.ISO_OFFSET_DATE_TIME,
            DateTimeFormatter.ISO_LOCAL_DATE_TIME,

            // Text date formats
            DateTimeFormatter.ofPattern(
                    "MMMM d, uuuu",
                    Locale.ENGLISH
            ),
            DateTimeFormatter.ofPattern(
                    "d MMMM uuuu",
                    Locale.ENGLISH
            ),
            DateTimeFormatter.ofPattern(
                    "d MMM uuuu",
                    Locale.ENGLISH
            ),
            DateTimeFormatter.ofPattern(
                    "uuuu-MMMM-d",
                    Locale.ENGLISH
            ),

            // Date-only formats
            DateTimeFormatter.ISO_LOCAL_DATE,
            DateTimeFormatter.ofPattern("uuuu/MM/dd"),
            DateTimeFormatter.ofPattern("dd-MM-uuuu"),
            DateTimeFormatter.ofPattern("dd/MM/uuuu")
    );
}

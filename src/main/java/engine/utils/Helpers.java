package engine.utils;

import engine.reporters.Loggers;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQueries;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
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


    public static <T> boolean isSorted(List<T> list, Comparator<T> comparator) {
        for (int i = 1; i < list.size(); i++) {
            if (comparator.compare(list.get(i - 1), list.get(i)) > 0) {
                return false;
            }
        }
        return true;
    }

    public static int daysBetweenToday(String date) {
        String formattedDate = formatDate("YYYY-MM-dd", date);
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

    public static String formatDate(String outFormat, String inputDate) {
        List<DateTimeFormatter> supportedFormats = List.of(
                DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH),
                DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH),
                DateTimeFormatter.ofPattern(formatPattern, Locale.ENGLISH),
                DateTimeFormatter.ofPattern("yyyy-MMMM-d", Locale.ENGLISH),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                DateTimeFormatter.ofPattern("dd-MM-yyyy"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
        );
        String value = inputDate.trim();
        DateTimeFormatter outputFormatter =
                DateTimeFormatter.ofPattern(outFormat);
        boolean outputRequiresTime = requiresTime(outFormat);
        boolean outputRequiresZone = requiresZone(outFormat);
        for (DateTimeFormatter formatter : supportedFormats) {
            try {
                TemporalAccessor parsed = formatter.parse(value);
                LocalDate date = parsed.query(TemporalQueries.localDate());
                LocalTime time = parsed.query(TemporalQueries.localTime());
                ZoneOffset offset = parsed.query(TemporalQueries.offset());
                if (date == null) {
                    continue;
                }
                if (time == null && outputRequiresTime) {
                    time = LocalTime.MIDNIGHT;
                }
                if (time == null) {
                    return date.format(outputFormatter);
                }
                if (outputRequiresZone) {
                    if (offset == null) {
                        offset = ZoneOffset.UTC;
                    }
                    return OffsetDateTime.of(date, time, offset).format(outputFormatter);
                }

                return LocalDateTime.of(date, time).format(outputFormatter);

            } catch (DateTimeParseException e) {
                Loggers.logError(String.valueOf(e));
            }
        }
        throw new IllegalArgumentException("Unsupported date format: " + inputDate);
    }

    private static boolean requiresTime(String format) {
        return format.contains("H")
                || format.contains("h")
                || format.contains("m")
                || format.contains("s")
                || format.contains("S");
    }

    private static boolean requiresZone(String format) {
        return format.contains("X")
                || format.contains("x")
                || format.contains("Z")
                || format.contains("z");
    }

}

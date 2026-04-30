package engine.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helpers {

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

    public static int daysBetweenToday(String format,String date) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(format, Locale.ENGLISH);
        LocalDate givenDate = LocalDate.parse(date, formatter);
        LocalDate today = LocalDate.now();
        return Math.toIntExact(ChronoUnit.DAYS.between(today, givenDate));
    }
}

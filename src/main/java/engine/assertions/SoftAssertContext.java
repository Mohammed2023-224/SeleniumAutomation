package engine.assertions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SoftAssertContext {
    private SoftAssertContext() {}

    private static final ThreadLocal<List<SoftAssertScreenshot>> SCREENSHOTS =
            ThreadLocal.withInitial(ArrayList::new);

    private static final ThreadLocal<Integer> COUNTER =
            ThreadLocal.withInitial(() -> 0);

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss_SSS");

    public static void storeScreenshot(byte[] screenshot, String baseName) {
        if (screenshot == null || screenshot.length == 0) {
            return;
        }

        int nextCount = COUNTER.get() + 1;
        COUNTER.set(nextCount);

        String safeBaseName = (baseName == null || baseName.isBlank())
                ? "Soft Assertion Screenshot"
                : baseName.trim();

        String uniqueName = safeBaseName + " - " +
                LocalDateTime.now().format(FORMATTER) +
                " - #" + nextCount;

        SCREENSHOTS.get().add(new SoftAssertScreenshot(uniqueName, screenshot));
    }

    public static List<SoftAssertScreenshot> getScreenshots() {
        return new ArrayList<>(SCREENSHOTS.get());
    }

    public static void clear() {
        SCREENSHOTS.remove();
        COUNTER.remove();
    }

    public static final class SoftAssertScreenshot {
        private final String name;
        private final byte[] bytes;

        public SoftAssertScreenshot(String name, byte[] bytes) {
            this.name = name;
            this.bytes = bytes;
        }

        public String getName() {
            return name;
        }

        public byte[] getBytes() {
            return bytes;
        }
    }

}
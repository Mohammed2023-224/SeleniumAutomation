package engine.utils.ExcelHandlers;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CsvWriter {

        public static void writeCsv(Path path, List<String[]> rows)
        {
            Path parent = path.getParent();
            if (parent != null) {
                try {
                    Files.createDirectories(parent);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try (BufferedWriter writer = Files.newBufferedWriter(
                    path, StandardCharsets.UTF_8)) {

                // UTF-8 BOM helps Excel recognize UTF-8, including Arabic text.
                try {
                    writer.write('\uFEFF');
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                for (String[] row : rows) {
                    for (int i = 0; i < row.length; i++) {
                        if (i > 0) {
                            writer.write(',');
                        }

                        writer.write(escapeCsv(row[i]));
                    }

                    writer.newLine();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private static String escapeCsv(String value) {
            if (value == null) {
                value = "";
            }

            // CSV escapes quotes by doubling them.
            if (value.contains(",")
                    || value.contains("\"")
                    || value.contains("\n")
                    || value.contains("\r")) {

                return "\"" + value.replace("\"", "\"\"") + "\"";
            }

            return value;
        }

}

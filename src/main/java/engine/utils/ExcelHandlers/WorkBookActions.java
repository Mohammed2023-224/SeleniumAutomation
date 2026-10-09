package engine.utils.ExcelHandlers;

import engine.reporters.Loggers;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;

public class WorkBookActions  {
    private XSSFWorkbook workbook;
    private String currentFilePath;


    protected XSSFWorkbook openWorkBook(String path,XSSFSheet sheet) {
        // Reuse the workbook if the same file is already open
        if (workbook != null
                && workbook.getPackage() != null
                && path.equalsIgnoreCase(currentFilePath)) {
            Loggers.logInfo("Workbook already opened. Reusing: " + path);
            return workbook;
        }
        try {
            close(sheet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try (FileInputStream file = new FileInputStream(path)) {
            workbook = new XSSFWorkbook(file);
            currentFilePath = path;
            Loggers.logInfo("Workbook loaded from path: " + path);
            return workbook;
        } catch (Exception e) {
            workbook = null;
            currentFilePath = null;
            Loggers.logError(
                    "Failed to open workbook at path: " + path + " " + e
            );
            return workbook;
        }
    }


    protected void close(XSSFSheet sheet) {
        if (workbook != null) {
            try {
                workbook.close();
                workbook = null;
                sheet = null;
                Loggers.logInfo("Workbook closed");
            } catch (Exception e) {
                Loggers.logError("Failed to close workbook: " + e);
            }
        }
    }
}

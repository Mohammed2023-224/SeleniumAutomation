package engine.utils.ExcelHandlers;

import engine.reporters.Loggers;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SheetActions {

    private XSSFSheet sheet;

    protected XSSFSheet changeSheet(XSSFWorkbook workbook, String sheetName) {
        if (workbook != null) {
            sheet =workbook.getSheet(sheetName);
            Loggers.logInfo("found  sheet " + sheetName + " and changed to it");
        } else {
            Loggers.logError("Can't find sheet " + sheetName);
        }
        return sheet;
    }

    protected int getColumnNumFromHeaderName(String columnName) {
        XSSFRow headerRow = sheet.getRow(0);
        if (headerRow == null) {
            Loggers.logError("Header row is missing");
            return -1;
        }
        for (int i = 0; i < getNumberOfColumnsByHeaders(); i++) {
            XSSFCell cell = headerRow.getCell(i);
            if (cell != null && columnName.equals(new CellActions().getCellData(cell))) {
                return i;
            }
        }
        Loggers.logError("Couldn't find column header " + columnName);
        return -1;
    }

    public int getNumberOfColumnsByHeaders() {
        return sheet.getRow(0).getLastCellNum();
    }

    public int getNumberOfRows() {
        return sheet.getPhysicalNumberOfRows();
    }

}

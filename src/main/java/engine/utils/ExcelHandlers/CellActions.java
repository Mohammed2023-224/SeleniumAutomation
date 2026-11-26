package engine.utils.ExcelHandlers;

import engine.reporters.Loggers;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class CellActions {


    protected String getCellByColumnNumAndRowNum(int rowNum, int colNum , XSSFSheet sheet) {
        XSSFRow targetRow = sheet.getRow(rowNum);
        if (targetRow == null) {
            Loggers.logError("Row " + rowNum + " not found in sheet " + sheet.getSheetName());
            return "";
        }

        XSSFCell targetCell = targetRow.getCell(colNum);
        if (targetCell == null) {
//            Loggers.logError("Cell not found at row " + rowNum + ", column " + colNum);
            return "";
        }
        return getCellData(targetCell);
    }


    protected String getCellByColumnNameAndRowNum(int rowNum, int colNum,XSSFSheet sheet) {
        XSSFRow targetRow = sheet.getRow(rowNum);
        if (targetRow == null) {
            Loggers.logError("Row " + rowNum + " not found in sheet " + sheet.getSheetName());
            return "";
        }
        // ✅ EDIT: Null-check for cell
        XSSFCell targetCell = targetRow.getCell(colNum);
        if (targetCell == null) {
//            Loggers.logError("Cell not found at row " + rowNum + ", column " + colNum);
            return "";
        }
        return getCellData(targetCell);
    }

    protected String getCellData(XSSFCell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                    return new DataFormatter().formatCellValue(cell).trim();
            case FORMULA:
                return getFormulaCellData(cell);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());

            case BLANK:
            case _NONE:
                return "";

            default:
                return "unknown cell type";
        }
    }


    private String getFormulaCellData(XSSFCell cell) {
        switch (cell.getCachedFormulaResultType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return new DataFormatter()
                        .formatCellValue(cell)
                        .trim();
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case BLANK:
            case _NONE:
                return "";
            default:
                return "unknown formula result type";
        }
    }
    }

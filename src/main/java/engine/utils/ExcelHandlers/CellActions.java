package engine.utils.ExcelHandlers;

import engine.reporters.Loggers;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.text.SimpleDateFormat;

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

    protected String getCellData(XSSFCell cel) {
        String data = "";
        if ((cel == null)) {
            return "";
        } else {
            switch (cel.getCellType()) {
                case STRING:
                    data = cel.getStringCellValue();
                    return data;
                case NUMERIC, FORMULA:
                    if (DateUtil.isCellDateFormatted(cel)) {
                        // Convert date to string
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                        data = dateFormat.format(cel.getDateCellValue());
                    } else {
                        data = String.valueOf(cel.getNumericCellValue());
                    }
                    return data;
                case BLANK, _NONE:
                    return "";
                case BOOLEAN:
                    data = String.valueOf(cel.getBooleanCellValue());
                    return data;
                default:
                    return "unknown cell type";
            }
        }
    }


}

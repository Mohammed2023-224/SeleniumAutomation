package engine.utils;

import engine.reporters.Loggers;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;


public class ExcelReader {
    private static XSSFWorkbook workbook;
    private static XSSFSheet sheet;
    private static XSSFRow row;
    private static XSSFCell cell;
    static String path1 = "C:\\Users\\USER\\Desktop\\w.xlsx";


    public  String readSingleCell(String filePath,String sheetName, int rowNum,int colNum) {
        readExcel(filePath);
        changeSheet(sheetName);
        return getCellByColumnNumAndRowNum(rowNum,colNum);
    }

    public  String readSingleCell(String filePath,String sheetName, int rowNum,String colName) {
        readExcel(filePath);
        changeSheet(sheetName);
        return getCellByColumnNameAndRowNum(rowNum,colName);
    }


    private static void readExcel(String path) {
        try (
                FileInputStream file = new FileInputStream(path)) {
            workbook = new XSSFWorkbook(file);
            Loggers.log.info("Workbook loaded from path: {}", path);
        } catch (Exception e) {
            Loggers.log.error("Failed to open workbook at path: {}", path, e);
        }
    }

    private static void changeSheet(String sheetName) {
        if (!(workbook == null)) {
            sheet = workbook.getSheet(sheetName);
            Loggers.log.info("found  sheet {}", sheetName);
        } else {
            Loggers.log.error("Can't find sheet {}", sheetName);
        }
    }




    private static String getCellByColumnNameAndRowNum(int rowNum, String colName) {
        int colNum = getColumnNumFromHeaderName(colName);
        String data=getCellData(sheet.getRow(rowNum).getCell(colNum));
        Loggers.log.info("read data {} from row {} and column {}", data,rowNum,colName);
        return data;

    }

    private static String getCellByColumnNumAndRowNum(int rowNum, int colNum) {
        String data=getCellData(sheet.getRow(rowNum).getCell(colNum));
        Loggers.log.info("read {} from row {} and column {}", data,rowNum,colNum);
        return data;
    }

    private static int getColumnNumFromHeaderName(String columnName) {
        for (int i = 0; i < getNumberOfColumnsByHeaders(); i++) {
            if (getCellData(sheet.getRow(0).getCell(i)).equals(columnName)) {
                Loggers.log.info("Getting the header column number: {}", i);
                return i;
            }
        }
        Loggers.log.error("Couldn't find column header {}", columnName);
        return -1;
    }


    private static int getNumberOfColumnsByHeaders() {
        int numOfRows=sheet.getRow(0).getLastCellNum();
        Loggers.log.info("get number of columns: {}",numOfRows);
        return numOfRows;
    }

    private static String getCellData(XSSFCell cel) {
        String data="";
        if((cel==null)) {
            Loggers.log.info("cell is null");
           return "";
        }else {
            switch (cel.getCellType()) {
                case STRING:
                    data = cel.getStringCellValue();
                    Loggers.log.info("read string data: {}", data);
                    return data;
                case NUMERIC:
                case FORMULA:
                    data = String.valueOf(cel.getNumericCellValue());
                    Loggers.log.info(" convert number into string and read data: {}", data);
                    return data;
                case BLANK:
                case _NONE:
                    Loggers.log.info("cell is blank");
                    return "";
                case BOOLEAN:
                    data = String.valueOf(cel.getBooleanCellValue());
                    Loggers.log.info(" convert boolean into string and read data: {}", data);
                    return data;
            }
        }
        return "unknown cell type";
    }

}

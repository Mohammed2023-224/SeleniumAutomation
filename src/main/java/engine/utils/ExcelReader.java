package engine.utils;

import engine.reporters.Loggers;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;


public class ExcelReader {
    private static XSSFWorkbook workbook;
    private static XSSFSheet sheet;
    private static XSSFRow row;
    private static XSSFCell cell;


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

    public  Object[][] readRowAsLinkedHashMapThroughCondition(String filePath, String sheetName, String colName, String condition) {
        readExcel(filePath);
        changeSheet(sheetName);
        int numberOfRowsMeetingCondition=0;
        int conditionColumnNumber=getColumnNumFromHeaderName(colName);
        Loggers.getLogger().info("condition was found at column number " + conditionColumnNumber);
        for(int i=1 ;i<getNumberOfRows();i++){
            if(getCellByColumnNumAndRowNum(i,conditionColumnNumber).equalsIgnoreCase(condition)){
             Loggers.getLogger().info("Increase rows meeting condition by 1");
                numberOfRowsMeetingCondition++;
            }
        }
        Object[][] dataObj=new Object[numberOfRowsMeetingCondition][1];
        int num=0;
     Loggers.getLogger().info("total number of rows meeting condition is {}", numberOfRowsMeetingCondition);
        for (int i=1;i<getNumberOfRows();i++){
            if(getCellByColumnNumAndRowNum(i,conditionColumnNumber).equalsIgnoreCase(condition)){
                LinkedHashMap<String,String> linkedHashMap=new LinkedHashMap<>();
                for (int j=0; j<getNumberOfColumnsByHeaders();j++){
                    String currentKey=getCellByColumnNumAndRowNum(0,j);
                    String currentValue=getCellByColumnNumAndRowNum(i,j);
                    linkedHashMap.put(currentKey,currentValue);
                   Loggers.getLogger().info("Read data Key: {} --> Value: {} from row {} and column  {}",currentKey,currentValue,i,j);
                }
                dataObj[num++][0]=linkedHashMap;
            }
        }
        return dataObj;
    }

    private static void readExcel(String path) {
        try (
                FileInputStream file = new FileInputStream(path)) {
            workbook = new XSSFWorkbook(file);
         Loggers.getLogger().info("Workbook loaded from path: {}", path);
        } catch (Exception e) {
         Loggers.getLogger().error("Failed to open workbook at path: {}", path, e);
        }
    }

    private static void changeSheet(String sheetName) {
        if (!(workbook == null)) {
            sheet = workbook.getSheet(sheetName);
         Loggers.getLogger().info("found  sheet {} and changed to it", sheetName);
        } else {
         Loggers.getLogger().error("Can't find sheet {}", sheetName);
        }
    }

//    private static String getCellByColumnNameAndRowNum(int rowNum, String colName) {
//        int colNum = getColumnNumFromHeaderName(colName);
//        String data=getCellData(sheet.getRow(rowNum).getCell(colNum));
//     Loggers.log.info("read data {} from row {} and column {}", data,rowNum,colName);
//        return data;
//
//    }

    private static String getCellByColumnNameAndRowNum(int rowNum, String colName) {
        int colNum = getColumnNumFromHeaderName(colName);
        XSSFRow targetRow = sheet.getRow(rowNum);
        if (targetRow == null) {
         Loggers.getLogger().error("Row {} not found in sheet {}", rowNum, sheet.getSheetName());
            return "";
        }
        // ✅ EDIT: Null-check for cell
        XSSFCell targetCell = targetRow.getCell(colNum);
        if (targetCell == null) {
         Loggers.getLogger().error("Cell not found at row {}, column {}", rowNum, colNum);
            return "";
        }
        String data = getCellData(targetCell);
        return data;
    }

    private static String getCellByColumnNumAndRowNum(int rowNum, int colNum) {
        XSSFRow targetRow = sheet.getRow(rowNum);
        if (targetRow == null) {
         Loggers.getLogger().error("Row {} not found in sheet {}", rowNum, sheet.getSheetName());
            return "";
        }

        XSSFCell targetCell = targetRow.getCell(colNum);
        if (targetCell == null) {
         Loggers.getLogger().error("Cell not found at row {}, column {}", rowNum, colNum);
            return "";
        }
        String data = getCellData(targetCell);
        return data;
    }

    private static int getColumnNumFromHeaderName(String columnName) {
        for (int i = 0; i < getNumberOfColumnsByHeaders(); i++) {
            if (getCellData(sheet.getRow(0).getCell(i)).equals(columnName)) {
                return i;
            }
        }
     Loggers.getLogger().error("Couldn't find column header {}", columnName);
        return -1;
    }


    private static int getNumberOfColumnsByHeaders() {
        int numOfColumns=sheet.getRow(0).getLastCellNum();
        return numOfColumns;
    }

    private static int getNumberOfRows() {
        int numOfRows= sheet.getPhysicalNumberOfRows();
        return numOfRows;
    }

    private static String getCellData(XSSFCell cel) {
        String data="";
        if((cel==null)) {
           return "";
        }else {
            switch (cel.getCellType()) {
                case STRING:
                    data = cel.getStringCellValue();
                    return data;
                case NUMERIC:
                case FORMULA:
                    if (DateUtil.isCellDateFormatted(cel)) {
                        // Convert date to string
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                        data= dateFormat.format(cel.getDateCellValue());
                    }
                        else{
                            data = String.valueOf(cel.getNumericCellValue());
                        }
                    return data;
                case BLANK:
                case _NONE:
                    return "";
                case BOOLEAN:
                    data = String.valueOf(cel.getBooleanCellValue());
                    return data;
            }
        }
        return "unknown cell type";
    }
}

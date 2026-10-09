package engine.utils.ExcelHandlers;

import engine.reporters.Loggers;
import engine.utils.DataTypes;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelReader  {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    CellActions cellActions;
    SheetActions sheetActions;
    WorkBookActions workBookActions;


    public ExcelReader(String filePath, String sheetName){
        cellActions=new CellActions();
        sheetActions=new SheetActions();
        workBookActions=new WorkBookActions();
         workbook=workBookActions.openWorkBook(filePath,sheet);
        sheet= sheetActions.changeSheet(workbook  ,sheetName);
    }

    public String readSingleCell( int rowNum, int colNum) {
        return cellActions.getCellByColumnNumAndRowNum(rowNum, colNum,sheet);
    }

    public String readSingleCell(int rowNum, String colName) {
        int colNum = sheetActions.getColumnNumFromHeaderName(colName);
        return cellActions.getCellByColumnNameAndRowNum(rowNum, colNum,sheet);
    }

    public Object[][] readRowAsLinkedHashMapThroughCondition( String colName, String condition) {

        int numberOfRowsMeetingCondition = 0;
        int conditionColumnNumber = sheetActions.getColumnNumFromHeaderName(colName);
        Loggers.logInfo("condition was found at column number " + conditionColumnNumber);
        for (int i = 1; i < sheetActions.getNumberOfRows(); i++) {
            if (cellActions.getCellByColumnNumAndRowNum(i, conditionColumnNumber,sheet).equalsIgnoreCase(condition)) {
                Loggers.logDebug("Increase rows meeting condition by 1");
                numberOfRowsMeetingCondition++;
            }
        }
        Object[][] dataObj = new Object[numberOfRowsMeetingCondition][1];
        int num = 0;
        Loggers.logInfo("total number of rows meeting condition is " + numberOfRowsMeetingCondition);
        for (int i = 1; i < sheetActions.getNumberOfRows(); i++) {
            if (cellActions.getCellByColumnNumAndRowNum(i, conditionColumnNumber,sheet).equalsIgnoreCase(condition)) {
                LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
                for (int j = 0; j < sheetActions.getNumberOfColumnsByHeaders(); j++) {
                    String currentKey = cellActions.getCellByColumnNumAndRowNum(0, j,sheet);
                    String currentValue = cellActions.getCellByColumnNumAndRowNum(i, j,sheet);
                    linkedHashMap.put(currentKey, currentValue);
                    Loggers.logDebug("Read data Key:" + currentKey + " --> Value: " + currentValue + " from row " + i + " and column " + j);
                }
                dataObj[num++][0] = linkedHashMap;
            }
        }
        return dataObj;
    }

    public Object[][] readRowAsLinkedHashMapNoColumnCondition() {

        Object[][] dataObj = new Object[sheetActions.getNumberOfRows()][1];
        int num = 0;
        for (int i = 1; i < sheetActions.getNumberOfRows(); i++) {
                LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
                for (int j = 0; j < sheetActions.getNumberOfColumnsByHeaders(); j++) {
                    String currentKey = cellActions.getCellByColumnNumAndRowNum(0, j,sheet);
                    String currentValue = cellActions.getCellByColumnNumAndRowNum(i, j,sheet);
                    linkedHashMap.put(currentKey, currentValue);
                    Loggers.logDebug("Read data Key:" + currentKey + " --> Value: " + currentValue + " from row " + i + " and column " + j);
                }
                dataObj[num++][0] = linkedHashMap;
            }
        return dataObj;
    }

    public List<DataTypes.ExcelRow> readListedHashMapNoColumnCondition() {
        List<DataTypes.ExcelRow> rows = new ArrayList<>();
        for (int i = 1; i < sheetActions.getNumberOfRows(); i++) {
            LinkedHashMap<String, String> row = new LinkedHashMap<>();
            for (int j = 0;
                 j < sheetActions.getNumberOfColumnsByHeaders();
                 j++) {
                String currentKey =
                        cellActions.getCellByColumnNumAndRowNum(0, j, sheet);
                String currentValue =
                        cellActions.getCellByColumnNumAndRowNum(i, j, sheet);
                row.put(currentKey, currentValue);
            }
            rows.add(new DataTypes.ExcelRow(i + 1, row));
        }
        return rows;
    }

    public List<String> readColumnsHeaderAsAList() {
        List<String> rows = new ArrayList<>();
                for (int j = 0; j < sheetActions.getNumberOfColumnsByHeaders(); j++) {
                    String columnHeader = cellActions.getCellByColumnNumAndRowNum(0, j,sheet);
                    rows.add(columnHeader);
                }
        return rows;
    }


    public Object[][] readExcelSheet() {
        Object[][] dataObj = new Object[sheetActions.getNumberOfRows()][1];
        int num = 0;
        for (int i = 1; i < sheetActions.getNumberOfRows(); i++) {
            LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
            for (int j = 0; j < sheetActions.getNumberOfColumnsByHeaders(); j++) {
                String currentKey = cellActions.getCellByColumnNumAndRowNum(0, j,sheet);
                String currentValue = cellActions.getCellByColumnNumAndRowNum(i, j,sheet);
                linkedHashMap.put(currentKey, currentValue);
                Loggers.logDebug("Read data Key: " + currentKey + " --> Value: " + currentValue + " from row " + i + " and column " + j);
            }
            dataObj[num++][0] = linkedHashMap;
        }
        return dataObj;
    }

    public Object[][] readExcelSheetWithCertainColumnReference(String colName) {

        Object[][] dataObj = new Object[sheetActions.getNumberOfRows() - 1][2];
        int num = 0;
        for (int i = 1; i < sheetActions.getNumberOfRows(); i++) {
            LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
            String referenceCall = "";
            for (int j = 0; j < sheetActions.getNumberOfColumnsByHeaders(); j++) {
                referenceCall = cellActions.getCellByColumnNumAndRowNum(0, j,sheet).equalsIgnoreCase(colName)
                        ? cellActions.getCellByColumnNumAndRowNum(i, j,sheet)
                        : referenceCall;
                String currentKey = cellActions.getCellByColumnNumAndRowNum(0, j,sheet);
                String currentValue = cellActions.getCellByColumnNumAndRowNum(i, j,sheet);
                linkedHashMap.put(currentKey, currentValue);
                Loggers.logDebug("Read data Key: " + currentKey + " --> Value: " + currentValue + " from row " + i + " and column " + j);
            }
            dataObj[num][0] = referenceCall;
            dataObj[num++][1] = linkedHashMap;
        }

        return dataObj;
    }

    public SheetActions returnSheetObject(){
        return  sheetActions;
    }
}
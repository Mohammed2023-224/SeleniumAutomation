package engine.utils;

import engine.enums.SortingTypes;

import java.util.LinkedHashMap;

public class DataTypes {

    public record ExcelRow(
            int rowNumber,
            LinkedHashMap<String, String> values
    ) {}


    public record SortConfig(
            String column,
            SortingTypes type,
            boolean ascending
    ) {}
}

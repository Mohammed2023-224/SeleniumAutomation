package engine.utils;

import java.util.LinkedHashMap;

public class DataTypes {

    public record ExcelRow(
            int rowNumber,
            LinkedHashMap<String, String> values
    ) {}

}

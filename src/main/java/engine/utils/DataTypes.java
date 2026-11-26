package engine.utils;

import engine.enums.ColumnTypes;

import java.util.LinkedHashMap;
import java.util.List;

public class DataTypes {

    public record ExcelRow(
            int rowNumber,
            LinkedHashMap<String, String> values
    ) {}

    public record Transformation(
            String operation,
            String argument
    ) {
    }

    public record SpecialColumnConfig(
            String column,
            ColumnTypes type,
            List<Transformation> transformations
    ) {
    }

    public record SortConfig(
            String column,
            ColumnTypes type,
            boolean ascending
    ) {}
}

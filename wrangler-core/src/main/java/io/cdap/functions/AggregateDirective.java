package io.cdap.wrangler.directive;

import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.annotations.Public;
import io.cdap.wrangler.api.parser.*;
import io.cdap.wrangler.api.executor.*;
import java.util.List;

@Public
public class AggregateDirective implements Directive {
    private String sourceSizeColumn;
    private String sourceTimeColumn;
    private String targetSizeColumn;
    private String targetTimeColumn;
    private String sizeUnit = "B"; // Default to bytes
    private String timeUnit = "ms"; // Default to milliseconds
    private String aggregationType = "total"; // Default to total

    private long totalSize = 0;
    private long totalTime = 0;
    private int rowCount = 0;

    @Override
    public UsageDefinition define() {
        return new UsageDefinition()
            .add("sourceSizeColumn", TokenType.COLUMN)
            .add("sourceTimeColumn", TokenType.COLUMN)
            .add("targetSizeColumn", TokenType.COLUMN)
            .add("targetTimeColumn", TokenType.COLUMN)
            .add("sizeUnit", TokenType.STRING) // Optional
            .add("timeUnit", TokenType.STRING) // Optional
            .add("aggregationType", TokenType.STRING); // Optional
    }

    @Override
    public void initialize(Arguments arguments) throws DirectiveParseException {
        sourceSizeColumn = ((ColumnName) arguments.value("sourceSizeColumn")).value();
        sourceTimeColumn = ((ColumnName) arguments.value("sourceTimeColumn")).value();
        targetSizeColumn = ((ColumnName) arguments.value("targetSizeColumn")).value();
        targetTimeColumn = ((ColumnName) arguments.value("targetTimeColumn")).value();

        if (arguments.contains("sizeUnit")) {
            sizeUnit = ((StringToken) arguments.value("sizeUnit")).value();
        }
        if (arguments.contains("timeUnit")) {
            timeUnit = ((StringToken) arguments.value("timeUnit")).value();
        }
        if (arguments.contains("aggregationType")) {
            aggregationType = ((StringToken) arguments.value("aggregationType")).value();
        }
    }

    @Override
    public List<Row> execute(List<Row> rows, ExecutorContext context) throws DirectiveExecutionException {
        for (Row row : rows) {
            // Read byte size and time duration from source columns
            Object sizeValue = row.getValue(sourceSizeColumn);
            Object timeValue = row.getValue(sourceTimeColumn);

            if (sizeValue instanceof String && timeValue instanceof String) {
                ByteSize byteSize = new ByteSize((String) sizeValue);
                TimeDuration timeDuration = new TimeDuration((String) timeValue);

                // Accumulate totals
                totalSize += byteSize.getBytes();
                totalTime += timeDuration.getMilliseconds();
                rowCount++;
            }
        }
        return rows; // Return rows as-is for now
    }

    @Override
    public List<Row> finalize(List<Row> rows, ExecutorContext context) throws DirectiveExecutionException {
        // Perform unit conversions if required
        double finalSize = convertSize(totalSize, sizeUnit);
        double finalTime = convertTime(aggregationType.equals("average") ? totalTime / rowCount : totalTime, timeUnit);

        // Create a single new row with the aggregated values
        Row aggregateRow = new Row();
        aggregateRow.add(targetSizeColumn, finalSize);
        aggregateRow.add(targetTimeColumn, finalTime);

        return List.of(aggregateRow);
    }

    private double convertSize(long sizeInBytes, String unit) {
        switch (unit) {
            case "KB":
                return sizeInBytes / 1024.0;
            case "MB":
                return sizeInBytes / (1024.0 * 1024);
            case "GB":
                return sizeInBytes / (1024.0 * 1024 * 1024);
            default:
                return sizeInBytes; // Default to bytes
        }
    }

    private double convertTime(long timeInMilliseconds, String unit) {
        switch (unit) {
            case "seconds":
                return timeInMilliseconds / 1000.0;
            case "minutes":
                return timeInMilliseconds / (1000.0 * 60);
            case "hours":
                return timeInMilliseconds / (1000.0 * 60 * 60);
            default:
                return timeInMilliseconds; // Default to milliseconds
        }
    }

    @Override
    public void destroy() {
        // Reset totals
        totalSize = 0;
        totalTime = 0;
        rowCount = 0;
    }

    @Override
    public String name() {
        return "aggregate";
    }

    @Override
    public String description() {
        return "Aggregates byte sizes and time durations from source columns and outputs totals or averages.";
    }
}
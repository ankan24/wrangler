package io.cdap.wrangler.directive;

import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.executor.ExecutorContext;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AggregateDirectiveTest {
    @Test
    public void testAggregateDirectiveWithValidInputs() throws Exception {
        AggregateDirective directive = new AggregateDirective();
        directive.initialize(new MockArguments()
            .add("sourceSizeColumn", "size")
            .add("sourceTimeColumn", "time")
            .add("targetSizeColumn", "total_size_mb")
            .add("targetTimeColumn", "total_time_sec")
            .add("sizeUnit", "MB")
            .add("timeUnit", "seconds")
            .add("aggregationType", "total"));

        List<Row> rows = new ArrayList<>();
        rows.add(new Row().add("size", "10KB").add("time", "150ms"));
        rows.add(new Row().add("size", "20KB").add("time", "250ms"));

        directive.execute(rows, new MockExecutorContext());
        List<Row> result = directive.finalize(rows, new MockExecutorContext());

        Row aggregateRow = result.get(0);
        Assert.assertEquals(0.029296875, aggregateRow.getValue("total_size_mb")); // 30KB in MB
        Assert.assertEquals(0.4, aggregateRow.getValue("total_time_sec")); // 400ms in seconds
    }

    @Test(expected = DirectiveExecutionException.class)
    public void testAggregateDirectiveWithInvalidInputs() throws Exception {
        AggregateDirective directive = new AggregateDirective();
        directive.initialize(new MockArguments()
            .add("sourceSizeColumn", "size")
            .add("sourceTimeColumn", "time")
            .add("targetSizeColumn", "total_size_mb")
            .add("targetTimeColumn", "total_time_sec"));

        List<Row> rows = new ArrayList<>();
        rows.add(new Row().add("size", "invalid").add("time", "150ms")); // Invalid size

        directive.execute(rows, new MockExecutorContext());
    }
}
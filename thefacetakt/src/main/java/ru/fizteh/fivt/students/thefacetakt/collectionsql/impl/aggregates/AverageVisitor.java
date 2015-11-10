package ru.fizteh.fivt.students.thefacetakt.collectionsql.impl.aggregates;
import java.util.function.Function;
import ru.fizteh.fivt.students.thefacetakt.collectionsql.AggregatorVisitor;

/**
 * Created by thefacetakt on 27.10.15.
 */

public class AverageVisitor<T> implements AggregatorVisitor<T, Double> {
    private Function<T, ? extends Number> function;
    private int count;
    private double sum;

    public AverageVisitor(Function<T, ? extends Number> newFunction) {
        function = newFunction;
        count = 0;
        sum = 0.0;
    }
    @Override
    public Double result() {
        if (count == 0) {
            throw new IllegalStateException("average of empty set "
                    + "is invalid");
        }
        return sum / count;
    }

    @Override
    public void visit(T item) {
        ++count;
        sum += function.apply(item).doubleValue();
    }
}

package ru.fizteh.fivt.students.thefacetakt.collectionsql.impl.aggregates;

import ru.fizteh.fivt.students.thefacetakt.collectionsql.AggregatorVisitor;

import java.util.function.Function;

public class MinVisitor<T, R extends Comparable<R>>
        implements AggregatorVisitor<T, R> {

    private Function<T, R> function;
    private R minimum;

    public MinVisitor(Function<T, R> newFunction) {
        minimum = null;
        function = newFunction;
    }

    @Override
    public R result() {
        return minimum;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void visit(T item) {
        if (minimum == null || minimum
                .compareTo(function.apply(item)) == 1) {
            minimum = function.apply(item);
        }
    }
}

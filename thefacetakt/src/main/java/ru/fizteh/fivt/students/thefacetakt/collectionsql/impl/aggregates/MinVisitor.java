package ru.fizteh.fivt.students.thefacetakt.collectionsql.impl.aggregates;

import ru.fizteh.fivt.students.thefacetakt.collectionsql.AggregatorVisitor;

import java.util.function.Function;

public class MinVisitor<T>
        implements AggregatorVisitor<T, T> {

    private Function<T, ? extends Comparable> function;
    private T minimum;

    public MinVisitor(Function<T, ? extends Comparable> newFunction) {
        minimum = null;
        function = newFunction;
    }

    @Override
    public T result() {
        return minimum;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void visit(T item) {
        if (minimum == null || function.apply(minimum)
                .compareTo(function.apply(item)) == 1) {
            minimum = item;
        }
    }
}

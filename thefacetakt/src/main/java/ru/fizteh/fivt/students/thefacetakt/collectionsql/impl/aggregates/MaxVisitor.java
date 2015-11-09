package ru.fizteh.fivt.students.thefacetakt.collectionsql.impl.aggregates;

import ru.fizteh.fivt.students.thefacetakt.collectionsql.AggregatorVisitor;

import java.util.function.Function;

public class MaxVisitor<T>
        implements AggregatorVisitor<T, T> {

    private Function<T, ? extends Comparable> function;
    private T maximum;

    public MaxVisitor(Function<T, ? extends Comparable> newFunction) {
        maximum = null;
        function = newFunction;
    }

    @Override
    public T result() {
        return maximum;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void visit(T item) {
        if (maximum == null || function.apply(maximum)
                .compareTo(function.apply(item)) == -1) {
            maximum = item;
        }
    }
}

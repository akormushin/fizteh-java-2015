package ru.fizteh.fivt.students.thefacetakt.collectionsql.impl.aggregates;

import ru.fizteh.fivt.students.thefacetakt.collectionsql.AggregatorVisitor;

import java.util.function.Function;

public class MaxVisitor<T, R extends Comparable<R>>
        implements AggregatorVisitor<T, R> {

    private Function<T, R> function;
    private R maximum;

    public MaxVisitor(Function<T, R> newFunction) {
        maximum = null;
        function = newFunction;
    }

    @Override
    public R result() {
        return maximum;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void visit(T item) {
        if (maximum == null || maximum
                .compareTo(function.apply(item)) == -1) {
            maximum = function.apply(item);
        }
    }
}

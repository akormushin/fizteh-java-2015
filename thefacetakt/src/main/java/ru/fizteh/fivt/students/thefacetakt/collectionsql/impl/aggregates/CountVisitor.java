package ru.fizteh.fivt.students.thefacetakt.collectionsql.impl.aggregates;


import ru.fizteh.fivt.students.thefacetakt.collectionsql.AggregatorVisitor;

import java.util.function.Function;

public class CountVisitor<T> implements AggregatorVisitor<T, Long> {
    private Function<T, ?> function;
    private Long count;

    public CountVisitor(Function<T, ?> newFunction) {
        count = 0L;
        function = newFunction;
    }

    @Override
    public Long result() {
        return count;
    }

    @Override
    public void visit(T item) {
        if (function.apply(item) != null) {
            ++count;
        }
    }
}

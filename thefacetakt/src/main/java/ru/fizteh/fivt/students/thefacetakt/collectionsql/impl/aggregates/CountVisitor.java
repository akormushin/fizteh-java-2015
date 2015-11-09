package ru.fizteh.fivt.students.thefacetakt.collectionsql.impl.aggregates;


import ru.fizteh.fivt.students.thefacetakt.collectionsql.AggregatorVisitor;

import java.util.function.Function;

public class CountVisitor<T> implements AggregatorVisitor<T, Integer> {
    private Function<T, ?> function;
    private Integer count;

    public CountVisitor(Function<T, ?> newFunction) {
        count = 0;
        function = newFunction;
    }

    @Override
    public Integer result() {
        return count;
    }

    @Override
    public void visit(T item) {
        if (function.apply(item) != null) {
            ++count;
        }
    }
}

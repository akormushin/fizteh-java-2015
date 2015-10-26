package ru.fizteh.fivt.students.thefacetakt.collectionsql.impl.aggregates;

import ru.fizteh.fivt.students.thefacetakt.collectionsql.Aggregator;

import java.util.List;
import java.util.function.Function;

/**
 * Created by thefacetakt on 27.10.15.
 */
class Count<T> implements Aggregator<T, Integer> {
    private Function<T, ?> function;

    Count(Function<T, ?> newFunction) {
        function = newFunction;
    }

    @Override
    public Class getReturnClass() {
        return Integer.class;
    }

    @Override
    public Integer apply(List<? extends T> elements) {
        return (int) elements.stream().map(function)
                .filter(e -> e != null).count();
    }

    @Override
    public Integer apply(T t) {
        return null;
    }
}

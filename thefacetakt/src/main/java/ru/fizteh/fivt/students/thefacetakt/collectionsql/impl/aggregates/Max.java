package ru.fizteh.fivt.students.thefacetakt.collectionsql.impl.aggregates;

import ru.fizteh.fivt.students.thefacetakt.collectionsql.Aggregator;

import java.util.List;
import java.util.function.Function;

/**
 * Created by thefacetakt on 02.11.15.
 */
class Max<T, R extends Comparable<R>> implements Aggregator<T, R> {
    private Function<T, R> function;

    Max(Function<T, R> newFunction) {
        this.function = newFunction;
    }

    @Override
    public R apply(List<? extends T> elements) {
        return elements.stream().map(function).max(Comparable::compareTo).get();
    }

    @Override
    public R apply(T t) {
        return null;
    }
}

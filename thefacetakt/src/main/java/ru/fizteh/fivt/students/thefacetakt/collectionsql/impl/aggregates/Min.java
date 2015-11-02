package ru.fizteh.fivt.students.thefacetakt.collectionsql.impl.aggregates;

import ru.fizteh.fivt.students.thefacetakt.collectionsql.Aggregator;

import java.util.List;
import java.util.function.Function;

/**
 * Created by thefacetakt on 02.11.15.
 */
class Min<T, R extends Comparable<R>> implements Aggregator<T, R> {
    private Function<T, R> function;

    Min(Function<T, R> newFunction) {
        this.function = newFunction;
    }

    @Override
    public R apply(List<? extends T> elements) {
        return elements.stream().map(function).min(Comparable::compareTo).get();
    }

    @Override
    public R apply(T t) {
        return null;
    }
}

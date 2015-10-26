package ru.fizteh.fivt.students.thefacetakt.collectionsql.impl.aggregates;

import ru.fizteh.fivt.students.thefacetakt.collectionsql.Aggregator;

import java.util.function.Function;

public class Aggregates {
    public static <T> Aggregator<T, Double> avg(
            Function<T, ? extends Number> function) {
        return new Average<>(function);
    }

    public static <T> Aggregator<T, Integer> count(
            Function<T, ?> function) {
        return new Count<>(function);
    }
}

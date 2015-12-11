package ru.fizteh.fivt.students.thefacetakt.collectionsql.impl.aggregates;

import ru.fizteh.fivt.students.thefacetakt.collectionsql.Aggregator;

import java.util.function.Function;

public class Aggregates {
    public static <T> Aggregator<T, Double, AverageVisitor<T>> avg(
            Function<T, ? extends Number> function) {
        @SuppressWarnings("unchecked")
        Class<AverageVisitor<T>> clazz = (Class<AverageVisitor<T>>)
                new AverageVisitor<>(function).getClass();
        return new Aggregator<>(clazz, function);

    }

    public static <T> Aggregator<T, Long, CountVisitor<T>> count(
            Function<T, ?> function) {
        @SuppressWarnings("unchecked")
        Class<CountVisitor<T>> clazz = (Class<CountVisitor<T>>)
                new CountVisitor<>(function).getClass();
        return new Aggregator<>(clazz, function);
    }

    public static <T, R extends Comparable<R>>
    Aggregator<T, R, MaxVisitor<T, R>> max(Function<T, R> function) {

        @SuppressWarnings("unchecked")
        Class<MaxVisitor<T, R>> clazz = (Class<MaxVisitor<T, R>>)
                new MaxVisitor<>(function).getClass();
        return new Aggregator<>(clazz, function);
    }

    public static <T, R extends Comparable<R>>
    Aggregator<T, R, MinVisitor<T, R>> min(Function<T, R> function) {
        @SuppressWarnings("unchecked")
        Class<MinVisitor<T, R>> clazz = (Class<MinVisitor<T, R>>)
                new MinVisitor<>(function).getClass();
        return new Aggregator<>(clazz, function);
    }
}

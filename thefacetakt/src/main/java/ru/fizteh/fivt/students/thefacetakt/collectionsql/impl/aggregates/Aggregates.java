package ru.fizteh.fivt.students.thefacetakt.collectionsql.impl.aggregates;

import ru.fizteh.fivt.students.thefacetakt.collectionsql.Aggregator;

import java.util.function.Function;

public class Aggregates {
    public static <T> Aggregator<T, Double, AverageVisitor<T>> avg(
            Function<T, Double> function) {
        @SuppressWarnings("unchecked")
        Class<AverageVisitor<T>> clazz = (Class<AverageVisitor<T>>)
                new AverageVisitor<>(function).getClass();
        return new Aggregator<>(clazz, function);

    }

    public static <T> Aggregator<T, Integer, CountVisitor<T>> count(
            Function<T, ?> function) {
        @SuppressWarnings("unchecked")
        Class<CountVisitor<T>> clazz = (Class<CountVisitor<T>>)
                new CountVisitor<T>(function).getClass();
        return new Aggregator<>(clazz, function);
    }

    public static <T> Aggregator<T, T, MaxVisitor<T>> max(
            Function<T, ? extends Comparable> function) {
        @SuppressWarnings("unchecked")
        Class<MaxVisitor<T>> clazz = (Class<MaxVisitor<T>>)
                new MaxVisitor<T>(function).getClass();
        return new Aggregator<>(clazz, function);
    }

    public static <T> Aggregator<T, T, MinVisitor<T>> min(
            Function<T, ? extends Comparable> function) {
        @SuppressWarnings("unchecked")
        Class<MinVisitor<T>> clazz = (Class<MinVisitor<T>>)
                new MinVisitor<T>(function).getClass();
        return new Aggregator<>(clazz, function);
    }

//    public static <T> Aggregator<T, Integer> count(
//            Function<T, ?> function) {
//        return new Count<>(function);
//    }
//
//    public static <T, R extends Comparable<R>> Aggregator<T, R> min(
//            Function<T, R> function) {
//        return new Min<>(function);
//    }
//
//    public static <T, R extends Comparable<R>> Aggregator<T, R> max(
//            Function<T, R> function) {
//        return new Max<>(function);
//    }
}

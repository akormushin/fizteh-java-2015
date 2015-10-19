package ru.fizteh.fivt.students.akormushin.collectionquery;

import java.util.function.Function;

/**
 * Aggregate functions.
 *
 * @author akormushin
 */
public class Aggregates {

    /**
     * @param expression
     * @param <C>
     * @param <T>
     * @return
     */
    public static <C, T extends Comparable<T>> Function<C, T> max(Function<C, T> expression) {
        throw new UnsupportedOperationException();
    }

    /**
     * @param expression
     * @param <C>
     * @param <T>
     * @return
     */
    public static <C, T extends Comparable<T>> Function<C, T> min(Function<C, T> expression) {
        throw new UnsupportedOperationException();
    }

    /**
     * @param expression
     * @param <C>
     * @param <T>
     * @return
     */
    public static <C, T extends Comparable<T>> Function<C, T> count(Function<C, T> expression) {
        throw new UnsupportedOperationException();
    }

    /**
     * @param expression
     * @param <C>
     * @param <T>
     * @return
     */
    public static <C, T extends Comparable<T>> Function<C, T> avg(Function<C, T> expression) {
        throw new UnsupportedOperationException();
    }

}

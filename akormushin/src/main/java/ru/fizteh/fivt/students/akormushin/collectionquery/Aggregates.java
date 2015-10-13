package ru.fizteh.fivt.students.akormushin.collectionquery;

import java.util.function.Function;

/**
 * Created by kormushin on 13.10.15.
 */
public class Aggregates {

    public static <C, T extends Comparable<T>> Function<C, T> max(Function<C, T> name) {
        throw new UnsupportedOperationException();
    }

    public static <C, T extends Comparable<T>> Function<C, T> min(Function<C, T> name) {
        throw new UnsupportedOperationException();
    }


    public static <C, T extends Comparable<T>> Function<C, T> count(Function<C, T> name) {
        throw new UnsupportedOperationException();
    }

    public static <C, T extends Comparable<T>> Function<C, T> avg(Function<C, T> name) {
        throw new UnsupportedOperationException();
    }

}

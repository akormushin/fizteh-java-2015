package ru.fizteh.fivt.students.akormushin.collectionquery;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by kormushin on 06.10.15.
 */
public class Conditions<T> {

    public static <T> Predicate<T> rlike(Function<T, String> expr, String regexp) {
        throw new UnsupportedOperationException();
    }

    public static <T> Predicate<T> like(Function<T, String> expr, String pattern) {
        throw new UnsupportedOperationException();
    }

}

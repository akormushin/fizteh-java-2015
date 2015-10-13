package ru.fizteh.fivt.students.akormushin.collectionquery;

import java.util.Comparator;
import java.util.function.Function;

/**
 * Created by kormushin on 06.10.15.
 */
public class OrderByConditions {

    public static <T, R extends Comparable<R>> Comparator<T> asc(Function<T, R> expr) {
        return (o1, o2) -> expr.apply(o1).compareTo(expr.apply(o2));
    }

    public static <T, R extends Comparable<R>> Comparator<T> desc(Function<T, R> expr) {
        return (o1, o2) -> expr.apply(o2).compareTo(expr.apply(o1));
    }

}

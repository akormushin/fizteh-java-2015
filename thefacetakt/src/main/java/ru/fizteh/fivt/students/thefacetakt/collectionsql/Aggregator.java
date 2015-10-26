package ru.fizteh.fivt.students.thefacetakt.collectionsql;

import java.util.List;
import java.util.function.Function;

/**
 * Created by thefacetakt on 20.10.15.
 */

public interface Aggregator<T, R> extends Function<T, R> {
    Class getReturnClass();

    R apply(List<? extends T> elements);
}

package ru.fizteh.fivt.students.thefacetakt.collectionsql.impl;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


/**
 * Created by thefacetakt on 20.10.15.
 */

public final class FromStmt<T> {
    private List<T> elements;

    private FromStmt(Iterable<T> iterable) {
        elements = StreamSupport.stream(iterable.spliterator(), true).collect(Collectors.toList());
    }

    public static <T> FromStmt<T> from(Iterable<T> iterable) {
        return new FromStmt<>(iterable);
    }
    public <R> SelectSmth<T, R> select(Class<R> resultClass,
                                       Function<T, ?> constructorFunctions) {
        return new SelectSmth<>(elements, resultClass, constructorFunctions);
    }
}

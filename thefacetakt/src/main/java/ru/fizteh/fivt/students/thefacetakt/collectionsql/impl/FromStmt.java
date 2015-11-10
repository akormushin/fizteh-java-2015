package ru.fizteh.fivt.students.thefacetakt.collectionsql.impl;

import java.util.function.Function;


/**
 * Created by thefacetakt on 20.10.15.
 */

public final class FromStmt<T> {
    private Iterable<T> elements;

    private FromStmt(Iterable<T> iterable) {
        elements = iterable;
    }

    public static <T> FromStmt<T> from(Iterable<T> iterable) {
        return new FromStmt<>(iterable);
    }

    public <R> SelectSmth<T, R> select(Class<R> resultClass,
                                       Function<T, ?>... constructorFunctions) {
        return new SelectSmth<>(elements, resultClass, false,
                constructorFunctions);
    }

    public <R> SelectSmth<T, R> selectDistinct(Class<R> resultClass,
                                       Function<T, ?>... constructorFunctions) {
        return new SelectSmth<>(elements, resultClass, true,
                constructorFunctions);
    }

}

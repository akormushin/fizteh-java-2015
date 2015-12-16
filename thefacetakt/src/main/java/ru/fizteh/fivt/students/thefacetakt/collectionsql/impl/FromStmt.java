package ru.fizteh.fivt.students.thefacetakt.collectionsql.impl;

import java.util.function.Function;


public final class FromStmt<T> {
    private Iterable<T> elements;

    FromStmt(Iterable<T> iterable) {
        elements = iterable;
    }

    public static <T> FromStmt<T> from(Iterable<T> iterable) {
        return new FromStmt<>(iterable);
    }

    @SafeVarargs
    public final <R> SelectSmth<T, R> select(Class<R> resultClass,
                                       Function<T, ?>... constructorFunctions) {
        return new SelectSmth<>(elements, resultClass, false,
                constructorFunctions);
    }

    public <F, S> SelectSmth<T, Tuple<F, S>> select(Function<T, F> f1,
                                                    Function<T, S> f2) {
        return new SelectSmth<>(elements, null, false,
                f1, f2);
    }

    public <F, S> SelectSmth<T, Tuple<F, S>> selectDistinct(Function<T, F> f1,
                                                    Function<T, S> f2) {
        return new SelectSmth<>(elements, null, true,
                f1, f2);
    }


    @SafeVarargs
    public final <R> SelectSmth<T, R> selectDistinct(Class<R> resultClass,
                                       Function<T, ?>... constructorFunctions) {
        return new SelectSmth<>(elements, resultClass, true,
                constructorFunctions);
    }

    public <S> JoinStmt<T, S> join(Iterable<S> secondIterable) {
        return new JoinStmt<>(elements, secondIterable);
    }

}

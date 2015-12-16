package ru.fizteh.fivt.students.thefacetakt.collectionsql.impl;

import java.util.function.BiPredicate;
import java.util.function.Function;

public final class JoinStmt<F, S> {
    private Iterable<F> firstIterable;
    private Iterable<S> secondIterable;

    JoinStmt(Iterable<F> first, Iterable<S> second) {
        firstIterable = first;
        secondIterable = second;
    }

    public FromStmt<Tuple<F, S>> on(BiPredicate<F, S> predicate) {
        return new FromStmt<>(
                new PairIterable<>(firstIterable, secondIterable, predicate)
        );
    }

    public <T> FromStmt<Tuple<F, S>> on(Function<F, T> first,
                                        Function<S, T> second) {
        return new FromStmt<>(new HashPairIterable<>(firstIterable,
                secondIterable, first, second));
    }
}

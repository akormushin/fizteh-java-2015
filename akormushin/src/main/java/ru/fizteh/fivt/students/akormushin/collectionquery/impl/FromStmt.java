package ru.fizteh.fivt.students.akormushin.collectionquery.impl;

import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Created by kormushin on 06.10.15.
 */
public class FromStmt<T> {
    public static <T> FromStmt<T> from(Iterable<T> iterable) {
        throw new UnsupportedOperationException();
    }

    public static <T> FromStmt<T> from(Stream<T> stream) {
        throw new UnsupportedOperationException();
    }

    public static <T> FromStmt<T> from(Query query) {
        throw new UnsupportedOperationException();
    }

    @SafeVarargs
    public final <R> SelectStmt<T, R> select(Class<R> clazz, Function<T, ?>... s) {
        throw new UnsupportedOperationException();
    }

    /**
     * Selects the only defined expression as is without wrapper.
     *
     * @param s
     * @param <R>
     * @return statement resulting in collection of R
     */
    public final <R> SelectStmt<T, R> select(Function<T, R> s) {
        throw new UnsupportedOperationException();
    }

    /**
     * Selects the only defined expression as is without wrapper.
     *
     * @param first
     * @param second
     * @param <F>
     * @param <S>
     * @return statement resulting in collection of R
     */
    public final <F, S> SelectStmt<T, Tuple<F, S>> select(Function<T, F> first, Function<T, S> second) {
        throw new UnsupportedOperationException();
    }

    @SafeVarargs
    public final <R> SelectStmt<T, R> selectDistinct(Class<R> clazz, Function<T, ?>... s) {
        throw new UnsupportedOperationException();
    }

    /**
     * Selects the only defined expression as is without wrapper.
     *
     * @param s
     * @param <R>
     * @return statement resulting in collection of R
     */
    public final <R> SelectStmt<T, R> selectDistinct(Function<T, R> s) {
        throw new UnsupportedOperationException();
    }

    public <J> JoinClause<T, J> join(Iterable<J> iterable) {
        throw new UnsupportedOperationException();
    }

    public <J> JoinClause<T, J> join(Stream<J> stream) {
        throw new UnsupportedOperationException();
    }

    public <J> JoinClause<T, J> join(Query<J> stream) {
        throw new UnsupportedOperationException();
    }

    public class JoinClause<T, J> {

        public FromStmt<Tuple<T, J>> on(BiPredicate<T, J> condition) {
            throw new UnsupportedOperationException();
        }

        public <K extends Comparable<?>> FromStmt<Tuple<T, J>> on(
                Function<T, K> leftKey,
                Function<J, K> rightKey) {
            throw new UnsupportedOperationException();
        }
    }
}

package ru.fizteh.fivt.students.thefacetakt.collectionsql.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by thefacetakt on 14.12.15.
 */
public final class HashPairIterable<F, S, T> implements Iterable<Tuple<F, S>> {
    private Map<T, Tuple<F, S>> joiner;

    private Iterable<F> first;
    private Iterable<S> second;
    private Function<F, T> firstFunction;
    private Function<S, T> secondFunction;

    HashPairIterable(Iterable<F> newFirst, Iterable<S> newSecond,
                     Function<F, T> newFirstFunction,
                     Function<S, T> newSecondFunction) {
        first = newFirst;
        second = newSecond;
        firstFunction = newFirstFunction;
        secondFunction = newSecondFunction;
    }

    void init() {
        joiner = new HashMap<>();
        for (F element: first) {
            T key = firstFunction.apply(element);
            if (joiner.containsKey(key)) {
                throw new IllegalArgumentException("Several first elements "
                        + "with same key");
            }
            joiner.put(key, new Tuple<>(element, null));
        }
        for (S element: second) {
            T key = secondFunction.apply(element);
            if (!joiner.containsKey(key)) {
                throw new IllegalArgumentException("No mathching first "
                        + "element");
            }
            joiner.put(key, new Tuple<>(joiner.get(key).getFirst(), element));
        }
        for (Tuple<F, S> element: joiner.values()) {
            if (element.getSecond() == null) {
                throw new IllegalArgumentException("No mathing second "
                        + "element");
            }
        }
    }

    @Override
    public Iterator<Tuple<F, S>> iterator() {
        init();
        return joiner.values().iterator();
    }
}

package ru.fizteh.fivt.students.thefacetakt.collectionsql.impl;

/**
 * @author akormushin
 */
public class Tuple<F, S> {

    private final F first;
    private final S second;

    @SuppressWarnings("unchecked")
    public Tuple(Object first, Object second) {
        this.first = (F) first;
        this.second = (S) second;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "Tuple{"
                + "first=" + first
                + ", second=" + second
                + '}';
    }
}

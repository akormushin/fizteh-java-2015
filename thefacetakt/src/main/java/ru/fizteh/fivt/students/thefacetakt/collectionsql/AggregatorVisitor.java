package ru.fizteh.fivt.students.thefacetakt.collectionsql;

/**
 * Created by thefacetakt on 20.10.15.
 */


public interface AggregatorVisitor<T, R> {
    R result();
    void visit(T item);
}

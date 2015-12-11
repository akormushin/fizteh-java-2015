package ru.fizteh.fivt.students.thefacetakt.collectionsql.impl;

import java.util.Comparator;

/**
 * Created by thefacetakt on 26.10.15.
 */
class HugeComparator<R> implements Comparator<R> {
    private Comparator<R> [] comparators;

    HugeComparator(Comparator<R>... newComparators) {
        this.comparators = newComparators;
    }

    @Override
    public int compare(R o1, R o2) {
        for (Comparator<R> comparator : comparators) {
            int compareResult = comparator.compare(o1, o2);
            if (compareResult != 0) {
                return compareResult;
            }
        }
        return 0;
    }
}

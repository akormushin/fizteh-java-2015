package ru.fizteh.fivt.students.thefacetakt.collectionsql.impl;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * Created by thefacetakt on 20.10.15.
 */

public final class PairIterable<F, S> implements Iterable<Tuple<F, S>>{
    private Iterable<F> firstIterable;
    private Iterable<S> secondIterable;
    private BiPredicate<F, S> predicate;

    public PairIterable(Iterable<F> first, Iterable<S> second,
                        BiPredicate<F, S> joiner) {
        firstIterable = first;
        secondIterable = second;
        predicate = joiner;
    }

    @Override
    public Iterator<Tuple<F, S>> iterator() {
        return new PairIterator();
    }

    private class PairIterator implements Iterator<Tuple<F, S>> {
        private Iterator<F> currentFirst;
        private Iterator<S> currentSecond;
        private F firstValue;
        private Boolean isSecondEmpty;
        private Tuple<F, S> currentPair;

        PairIterator() {
            currentFirst = PairIterable.this.firstIterable.iterator();
            if (currentFirst.hasNext()) {
                firstValue = currentFirst.next();
            }
            currentSecond = PairIterable.this.secondIterable.iterator();
            isSecondEmpty = !currentSecond.hasNext();
            skipUseless();
        }

        boolean tryHasNext() {
            return currentSecond.hasNext()
                    || (currentFirst.hasNext()
                    && !isSecondEmpty);
        }

        public Tuple<F, S> tryNext() {
            if (!currentSecond.hasNext()) {
                currentSecond = PairIterable.this.secondIterable.iterator();
                firstValue = currentFirst.next();
            }
            return new Tuple<>(firstValue, currentSecond.next());
        }

        void skipUseless() {
            currentPair = null;
            while (tryHasNext()) {
                currentPair = tryNext();
                if (predicate.test(
                        currentPair.getFirst(), currentPair.getSecond())) {
                    break;
                }
                currentPair = null;
            }
        }

        @Override
        public boolean hasNext() {
            return currentPair != null;
        }

        @Override
        public Tuple<F, S> next() {
            Tuple<F, S> toReturn = currentPair;
            skipUseless();
            return toReturn;
        }
    }

    public static void main(String[] args) {
        List<Integer> x = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        PairIterable<Integer, Integer> y =
                new PairIterable<>(x, x,
                (f, s) -> (f % 2 == 0 && s % 2 != 0));
        y.forEach(System.out::println);
    }
}

package ru.fizteh.fivt.students.akormushin.collectionquery.impl;

import java.util.stream.Stream;

/**
 * @author akormushin
 */
public interface Query<R> {

    Iterable<R> execute();

    Stream<R> stream();
}

package ru.fizteh.fivt.students.akormushin.collectionquery;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Created by kormushin on 06.10.15.
 */
public class Sources {

    @SafeVarargs
    public static <T> List<T> list(T... items) {
        throw new UnsupportedOperationException();
    }

    @SafeVarargs
    public static <T> Set<T> set(T... items) {
        throw new UnsupportedOperationException();
    }

    public static <T> Stream<T> lines(InputStream inputStream) {
        throw new UnsupportedOperationException();
    }

    public static <T> Stream<T> lines(Path file) {
        throw new UnsupportedOperationException();
    }

}

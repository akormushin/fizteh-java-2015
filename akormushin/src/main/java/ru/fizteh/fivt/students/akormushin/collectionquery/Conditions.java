package ru.fizteh.fivt.students.akormushin.collectionquery;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Where clause conditions.
 *
 * @author akormushin
 */
public class Conditions<T> {

    /**
     * Matches string result of expression against regexp pattern.
     *
     * @param expression expression result to match
     * @param regexp     pattern to match to
     * @param <T>        source object type
     * @return
     */
    public static <T> Predicate<T> rlike(Function<T, String> expression, String regexp) {
        throw new UnsupportedOperationException();
    }

    /**
     * Matches string result of expression against SQL like pattern.
     *
     * @param expression expression result to match
     * @param pattern    pattern to match to
     * @param <T>        source object type
     * @return
     */
    public static <T> Predicate<T> like(Function<T, String> expression, String pattern) {
        throw new UnsupportedOperationException();
    }

}

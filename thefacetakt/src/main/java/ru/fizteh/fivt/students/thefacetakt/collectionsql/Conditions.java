package ru.fizteh.fivt.students.thefacetakt.collectionsql;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by thefacetakt on 20.10.15.
 */
public class Conditions {
    public static <T> Predicate<T> rlike(Function<T, String> expression,
                                         String regexp) {
        return (item -> expression.apply(item).matches(regexp));
    }

    public static <T> Predicate<T> like(Function<T, String> expression,
                                        String pattern) {
        return rlike(expression,
                String.join(".*", Arrays.asList(pattern.split("\\%")).stream()
                .map(x -> String.join(".", Arrays.asList(x.split("\\?"))
                        .stream()
                        .map(Pattern::quote).collect(Collectors.toList())))
                .collect(Collectors.toList())));
    }
}

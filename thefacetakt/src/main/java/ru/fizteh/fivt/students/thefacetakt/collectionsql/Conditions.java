package ru.fizteh.fivt.students.thefacetakt.collectionsql;

import java.util.ArrayList;
import java.util.List;
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

    static List<String> mySplit(String text, char pattern) {
        List<String> result = new ArrayList<>();
        int last = 0;
        for (int i = 0; i < text.length(); ++i) {
            if (text.charAt(i) == pattern) {
                result.add(text.substring(last, i));
                last = i + 1;
            }
        }
        result.add(text.substring(last, text.length()));
        return result;
    }

    public static <T> Predicate<T> like(Function<T, String> expression,
                                        String pattern) {
        return rlike(expression,
                String.join(".*", mySplit(pattern, '%').stream()
                        .map(x -> String.join(".", mySplit(x, '?')
                                .stream()
                                .map(Pattern::quote)
                                .collect(Collectors.toList())))
                        .collect(Collectors.toList())));
    }
}

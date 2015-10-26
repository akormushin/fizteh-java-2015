package ru.fizteh.fivt.students.thefacetakt.collectionsql;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

class Average<T> implements Aggregator<T, Double> {
    private Function<T, ? extends Number> function;

    Average(Function<T, ? extends Number> newFunction) {
        function = newFunction;
    }

    @Override
    public Class getReturnClass() {
        return Double.class;
    }

    @Override
    public Double apply(List<? extends T> elements) {
        if (elements.isEmpty()) {
            return 0.0;
        }
        Double result = 0.0;
        List<Number> convertedElements = elements.stream().map(function)
                .collect(Collectors.toList());
        for (Number element: convertedElements) {
            result += (Double) element;
        }
        return result / elements.size();
    }

    @Override
    public Double apply(T t) {
        return null;
    }
}

public class Aggregates {
    public static <T> Aggregator<T, Double> avg(
            Function<T, ? extends Number> function) {
        return new Average<>(function);
    }

    static void count() {
    }
}

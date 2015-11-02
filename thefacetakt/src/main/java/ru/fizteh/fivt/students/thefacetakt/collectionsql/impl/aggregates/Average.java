package ru.fizteh.fivt.students.thefacetakt.collectionsql.impl.aggregates;

import ru.fizteh.fivt.students.thefacetakt.collectionsql.Aggregator;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by thefacetakt on 27.10.15.
 */
class Average<T> implements Aggregator<T, Double> {
    private Function<T, ? extends Double> function;

    Average(Function<T, ? extends Double> newFunction) {
        function = newFunction;
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

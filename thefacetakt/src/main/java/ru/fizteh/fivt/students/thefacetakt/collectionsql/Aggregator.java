package ru.fizteh.fivt.students.thefacetakt.collectionsql;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

public class Aggregator<T, R, V extends AggregatorVisitor<T, R>>
        implements Function<T, R> {

    private Function<T, ?> function;

    private Class<V> visitorClass;

    public Aggregator(Class<V> newVisitorClass, Function<T, ?> newFunction) {
        visitorClass = newVisitorClass;
        function = newFunction;
    }

    public V getVisitor() throws IllegalAccessException, InstantiationException,
            NoSuchMethodException, InvocationTargetException {
        @SuppressWarnings("unchecked")
        V result = (V) visitorClass.getConstructors()[0].newInstance(function);
        return result;
    }

    @Override
    public R apply(T t) {
        throw new UnsupportedOperationException();
    }
}

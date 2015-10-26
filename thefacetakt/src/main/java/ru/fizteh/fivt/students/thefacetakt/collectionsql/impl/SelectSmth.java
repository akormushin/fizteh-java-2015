package ru.fizteh.fivt.students.thefacetakt.collectionsql.impl;

import ru.fizteh.fivt.students.thefacetakt.collectionsql.Aggregator;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by thefacetakt on 20.10.15.
 */
public class SelectSmth<T, R> {

    private List<T> elements;
    private Class resultClass;
    private Function<T, ?> [] constructorFunctions;
    private Function<T, ?> groupByFunction;

    private Predicate<T> wherePredicate;
    private boolean distinct;


    @SafeVarargs
    SelectSmth(List<T> newElements,
               Class<R> newResultClass,
               boolean newDistinct,
               Function<T, ?>... newConstructorFunctions
               ) {
        elements = newElements;
        resultClass = newResultClass;
        constructorFunctions = newConstructorFunctions;
        distinct = newDistinct;
    }

    public SelectSmth<T, R> where(Predicate<T> predicate) {
        wherePredicate = predicate;
        return this;
    }

    public SelectSmth<T, R> groupBy(Function<T, ?>
                                            newGroupByFunction) {
        this.groupByFunction = newGroupByFunction;
        this.distinct = true;
        return this;
    }

    public List<R> execute() throws NoSuchMethodException,
            IllegalAccessException,
            InvocationTargetException, InstantiationException {

        List<R> result = new ArrayList<>();
        Class[] returnClasses = null;

        Map<Object, ArrayList<T>> grouping = new HashMap<>();

        if (groupByFunction == null) {
            groupByFunction = Function.identity();
        }

        for (T element: elements) {
            if (wherePredicate == null || wherePredicate.test(element)) {

                if (returnClasses == null) {
                    returnClasses = new Class[constructorFunctions.length];
                    for (int i = 0; i < constructorFunctions.length; ++i) {
                        if (constructorFunctions[i] instanceof Aggregator) {
                            returnClasses[i] =
                                    ((Aggregator)
                                            constructorFunctions[i])
                                            .getReturnClass();
                        } else {
                            returnClasses[i] = constructorFunctions[i]
                                    .apply(element).getClass();
                        }
                    }
                }


                Object key = groupByFunction.apply(element);
                if (!grouping.containsKey(key)) {
                    grouping.put(key, new ArrayList<>());
                }
                grouping.get(key).add(element);
            }
        }

        if (groupByFunction != null) {
            for (Object key: grouping.keySet()) {
                List<T> values = grouping.get(key);
                int distinction = 1;
                if (!distinct) {
                    distinction = values.size();
                }
                for (int j = 0; j < distinction; ++j) {
                    Object[] arguments = new Object[constructorFunctions.length];
                    for (int i = 0; i < arguments.length; ++i) {
                        if (constructorFunctions[i] instanceof Aggregator) {
                            arguments[i] =
                                    ((Aggregator)
                                            constructorFunctions[i])
                                            .apply(grouping.get(key));
                        } else {
                            arguments[i] = constructorFunctions[i]
                                    .apply(values.get(j));
                        }
                    }
                    result.add((R) resultClass
                            .getConstructor(returnClasses)
                            .newInstance(arguments));
                }
            }
        }
        return result;
    }


}

package ru.fizteh.fivt.students.thefacetakt.collectionsql.impl;

import ru.fizteh.fivt.students.thefacetakt.collectionsql.Aggregator;
import ru.fizteh.fivt.students.thefacetakt.collectionsql.AggregatorVisitor;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by thefacetakt on 20.10.15.
 */
public class SelectSmth<T, R> {

    private Iterable<T> elements;
    private Class<R> resultClass;
    private Function<T, ?>[] constructorFunctions;
    private Function<T, ?>[] groupByFunctions;

    private Predicate<T> wherePredicate;
    private Predicate<R> havingPredicate;

    private HugeComparator<R> hugeComparator;

    private boolean distinct;
    private boolean hasAggregators;

    private int limitRange = Integer.MAX_VALUE;

    @SafeVarargs
    SelectSmth(Iterable<T> newElements,
               Class<R> newResultClass,
               boolean newDistinct,
               Function<T, ?>... newConstructorFunctions
    ) {
        elements = newElements;
        resultClass = newResultClass;
        constructorFunctions = newConstructorFunctions;
        hasAggregators = false;
        for (int i = 0; i < constructorFunctions.length; ++i) {
            if (constructorFunctions[i] instanceof Aggregator) {
                hasAggregators = true;
                break;
            }
        }
        distinct = newDistinct;
    }

    public SelectSmth<T, R> orderBy(Comparator<R>... comparators) {
        hugeComparator = new HugeComparator<>(comparators);
        return this;
    }

    public SelectSmth<T, R> limit(int newLimit) {
        this.limitRange = newLimit;
        return this;
    }

    public SelectSmth<T, R> where(Predicate<T> predicate) {
        wherePredicate = predicate;
        return this;
    }

    public SelectSmth<T, R> having(Predicate<R> predicate) {
        havingPredicate = predicate;
        return this;
    }

    public SelectSmth<T, R> groupBy(Function<T, ?>...
                                            newGroupByFunctions) {
        this.groupByFunctions = newGroupByFunctions;
        if (newGroupByFunctions.length == 0) {
            throw new IllegalStateException("Group only by non-zero params");
        }
        this.distinct = true;
        return this;
    }

    public List<R> execute() throws NoSuchMethodException,
            IllegalAccessException,
            InvocationTargetException, InstantiationException {
        List<R> result = new ArrayList<>();
        Class[] returnClasses = new Class[constructorFunctions.length];

        Map<Object, ArrayList<Object>> superGrouping = new HashMap<>();
        Function<T, ?> groupByFunction;
        if (groupByFunctions == null) {
            if (hasAggregators) {
                groupByFunction = (x -> true);
            } else {
                groupByFunction = Function.identity();
            }
        } else {
            groupByFunction = ((T x) -> {
                Object[] values = new Object[groupByFunctions.length];
                for (int i = 0; i < groupByFunctions.length; ++i) {
                    values[i] = groupByFunctions[i].apply(x);
                }
                return Objects.hash(values);
            });
        }

        for (T element : elements) {
            if (wherePredicate == null || wherePredicate.test(element)) {
                Object key = groupByFunction.apply(element);
                if (!superGrouping.containsKey(key)) {
                    ArrayList<Object> currentArrayList = new ArrayList<>();

                    superGrouping.put(key, currentArrayList);

                    for (Function<T, ?> constructorFunction
                            : constructorFunctions) {
                        if (constructorFunction instanceof Aggregator) {
                            currentArrayList
                                    .add(((Aggregator) constructorFunction)
                                            .getVisitor());
                        } else {
                            currentArrayList.add(constructorFunction
                                    .apply(element));
                        }
                    }
                }
                ArrayList<Object> currentArrayList
                        = superGrouping.get(key);
                for (int i = 0; i < constructorFunctions.length; ++i) {
                    if (constructorFunctions[i] instanceof Aggregator) {
                        ((AggregatorVisitor) currentArrayList.get(i))
                                .visit(element);
                    }
                    if (!distinct) {
                        currentArrayList.add(constructorFunctions[i]
                                .apply(element));
                    }
                }
            }
        }
        boolean breakFlag = false;

        for (Object key : superGrouping.keySet()) {
            if (breakFlag) {
                break;
            }
            ArrayList<Object> currentArrayList
                    = superGrouping.get(key);


            for (int j = 0; j < currentArrayList.size()
                    / constructorFunctions.length; ++j) {
                if (breakFlag) {
                    break;
                }
                Object[] arguments = new Object[constructorFunctions.length];
                for (int i = 0; i < arguments.length; ++i) {
                    if (constructorFunctions[i] instanceof Aggregator) {
                        arguments[i] =
                                ((AggregatorVisitor) currentArrayList
                                        .get(j * arguments.length + i))
                                        .result();
                    } else {
                        arguments[i] = currentArrayList.get(i);
                    }
                    if (arguments[i] != null) {
                        returnClasses[i] = arguments[i].getClass();
                    } else {
                        throw new IllegalStateException("Null result"
                                + " of operation");
                    }
                }

                R addItem = resultClass
                        .getConstructor(returnClasses)
                        .newInstance(arguments);
                if (havingPredicate == null
                        || havingPredicate.test(addItem)) {
                    result.add(addItem);
                    if (result.size() == limitRange) {
                        breakFlag = true;
                        break;
                    }
                }
            }
        }
        if (hugeComparator != null) {
            result.sort(hugeComparator);
        }
        return result;
    }

    public UnionStmt<T, R> union() {
        return new UnionStmt<>(this);
    }
}


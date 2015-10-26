package ru.fizteh.fivt.students.thefacetakt.collectionsql.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by thefacetakt on 20.10.15.
 */
public class SelectSmth<T, R> {

    private List<T> elements;
    private Class resultClass;
    private Function<T, ?> [] constructorFunctions;
    private Predicate<T> wherePredicate;
    private Class[] returnClasses;

    @SafeVarargs
    SelectSmth(List<T> newElements,
               Class<R> newResultClass,
               Function<T, ?>... newConstructorFunctions) {
        elements = newElements;
        resultClass = newResultClass;
        constructorFunctions = newConstructorFunctions;
        returnClasses = null;
    }

    public SelectSmth<T, R> where(Predicate<T> predicate) {
        wherePredicate = predicate;
        return this;
    }

    public List<R> execute() throws NoSuchMethodException,
            IllegalAccessException,
            InvocationTargetException, InstantiationException {

        List<R> result = new ArrayList<>();
        for (T element: elements) {
            if (wherePredicate == null || wherePredicate.test(element)) {
                Object[] arguments = new Object[constructorFunctions.length];

                if (returnClasses == null) {
                    returnClasses = new Class[constructorFunctions.length];
                    for (int i = 0; i < constructorFunctions.length; ++i) {
                        returnClasses[i] = constructorFunctions[i]
                                .apply(element).getClass();
                    }
                }



                for (int i = 0; i < constructorFunctions.length; ++i) {
                    arguments[i] = constructorFunctions[i].apply(element);
                }

                result.add((R) resultClass.getConstructor(returnClasses)
                        .newInstance(arguments));
            }
        }
        return result;
    }


}

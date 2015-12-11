package ru.fizteh.fivt.students.thefacetakt.collectionsql.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class UnionStmt<T, R> {
    private List<SelectSmth<T, R>> statements;
    private FromStmt<T> current;


    UnionStmt(SelectSmth<T, R> x) {
        statements = new ArrayList<>();
        statements.add(x);
    }

    public UnionStmt<T, R> from(Iterable<T> iterable) {
        current = FromStmt.from(iterable);
        return this;
    }

    public UnionStmt<T, R> select(Class<R> resultClass,
                          Function<T, ?>... constructorFunctions) {
        statements.add(current.select(resultClass, constructorFunctions));
        current = null;
        return this;
    }

    public UnionStmt<T, R> selectDistinct(Class<R> resultClass,
                                  Function<T, ?>... constructorFunctions) {
        statements.add(current.selectDistinct(resultClass
                , constructorFunctions));
        return this;
    }

    public UnionStmt<T, R> orderBy(Comparator<R>... comparators) {
        statements.set(statements.size() - 1,
                statements.get(statements.size() - 1).orderBy(comparators));
        return this;
    }

    public UnionStmt<T, R> limit(int newLimit) {
        statements.set(statements.size() - 1,
                statements.get(statements.size() - 1).limit(newLimit));
        return this;
    }

    public UnionStmt<T, R> where(Predicate<T> predicate) {
        statements.set(statements.size() - 1,
                statements.get(statements.size() - 1).where(predicate));
        return this;
    }

    public UnionStmt<T, R> having(Predicate<R> predicate) {
        statements.set(statements.size() - 1,
                statements.get(statements.size() - 1).having(predicate));
        return this;
    }

    public UnionStmt<T, R> groupBy(Function<T, ?>... newGroupByFunctions) {
        statements.set(statements.size() - 1,
                statements.get(statements.size() - 1)
                        .groupBy(newGroupByFunctions));
        return this;
    }

    public UnionStmt<T, R> union() {
        return this;
    }

    public List<R> execute() throws NoSuchMethodException,
            IllegalAccessException,
            InvocationTargetException, InstantiationException {
        List<R> result = new ArrayList<>();
        for (SelectSmth<T, R> statement: statements) {
            result.addAll(statement.execute());
        }
        return result;
    }

}

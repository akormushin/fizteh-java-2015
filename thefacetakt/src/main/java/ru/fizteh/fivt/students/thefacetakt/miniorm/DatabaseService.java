package ru.fizteh.fivt.students.thefacetakt.miniorm;

import com.google.common.base.CaseFormat;
import org.h2.jdbcx.JdbcConnectionPool;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.fizteh.fivt.students.thefacetakt.miniorm
        .GoodNameResolver.isGood;


public class DatabaseService<T> implements Closeable{
    private static final String CONNECTION_NAME = "jdbc:h2:~/test";
    private static final String USERNAME = "test";
    private static final String PASSWORD = "test";

    private Class<T> clazz;

    private JdbcConnectionPool pool;

    private String tableName;
    private Class[] classes;
    private String[] columnNames;
    private String pkName;

    String convert(String name) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
    }

    String getColumnName(Field f) {
        String name = f.getAnnotation(Column.class).name();
        if (name.equals("")) {
            return convert(name);
        }
        return name;
    }

    void init() {
        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new IllegalArgumentException("no @Table annotation");
        }

        tableName = clazz.getAnnotation(Table.class).name();
        if (tableName.equals("")) {
            tableName = convert(clazz.getSimpleName());
        }

        if (!isGood(tableName)) {
            throw new IllegalArgumentException("Bad table name");
        }

        Set<String> names = new HashSet<>();
        List<Class> classesList = new ArrayList<>();
        List<String> fieldsList = new ArrayList<>();
        for (Field f: clazz.getDeclaredFields()) {
            if (f.isAnnotationPresent(Column.class)) {
                String name = getColumnName(f);
                names.add(name);
                if (!isGood(name)) {
                    throw new IllegalArgumentException("Bad column name");
                }
                classesList.add(f.getType());
                fieldsList.add(name);
                if (f.isAnnotationPresent(PrimaryKey.class)) {
                    if (pkName == null) {
                        pkName = name;
                    } else {
                        throw new
                                IllegalArgumentException("Several @PrimaryKey");
                    }
                }
            } else if (f.isAnnotationPresent(PrimaryKey.class)) {
                throw new
                        IllegalArgumentException("@PrimaryKey without @Column");
            }
        }

        if (names.size() != fieldsList.size()) {
            throw new IllegalArgumentException("Duplicate columns");
        }

        classes = new Class[classesList.size()];
        classes = classesList.toArray(classes);
        columnNames = new String[fieldsList.size()];
        columnNames = fieldsList.toArray(columnNames);

        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("No H2 driver found");
        }

        pool = JdbcConnectionPool.create(CONNECTION_NAME, USERNAME, PASSWORD);
    }

    DatabaseService(Class<T> newClazz) {
        clazz = newClazz;
        init();
    }

    @Override
    public void close() throws IOException {
        if (pool != null) {
            pool.dispose();
        }
    }
}

package ru.fizteh.fivt.students.thefacetakt.miniorm;

import org.h2.jdbcx.JdbcConnectionPool;

import java.io.Closeable;
import java.io.IOException;

public class DatabaseService<T> implements Closeable{
    private Class<T> clazz;

    private JdbcConnectionPool pool;

    

    void init() {
        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new IllegalArgumentException("no @Table annotation");
        }


        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("No H2 driver found");
        }


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

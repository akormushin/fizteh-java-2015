package ru.fizteh.fivt.students.thefacetakt.miniorm;

import org.h2.jdbcx.JdbcConnectionPool;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

class H2StringsResolver {
    static private Map<Class, String> h2Mapper;
    static {
        h2Mapper.put(Integer.class, "INTEGER");
        h2Mapper.put(Boolean.class, "BOOLEAN");
        h2Mapper.put(Byte.class, "SHORT");
        h2Mapper.put(Short.class, "TINYINT");
        h2Mapper.put(Long.class, "BIGINT");
        h2Mapper.put(Double.class, "DOUBLE");
        h2Mapper.put(Float.class, "FLOAT");
        h2Mapper.put(Time.class, "TIME");
        h2Mapper.put(Date.class, "DATE");
        h2Mapper.put(Timestamp.class, "TIMESTAMP");
        h2Mapper.put(Character.class, "CHAR");
        h2Mapper.put(String.class, "CLOB");
        h2Mapper.put(UUID.class, "UUID");
    }

    String resolve(Class clazz) {
        if (clazz.isArray()) {
            return "ARRAY";
        }
        if (h2Mapper.containsKey(clazz)) {
            return h2Mapper.get(clazz);
        }
        return "OTHER";
    }
}

public class DatabaseService<T> implements Closeable{
    private Class<T> clazz;

    private JdbcConnectionPool pool;

    private String

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

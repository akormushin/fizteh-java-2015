package ru.fizteh.fivt.students.thefacetakt.miniorm;

import com.google.common.base.CaseFormat;
import org.h2.jdbcx.JdbcConnectionPool;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

import static ru.fizteh.fivt.students.thefacetakt.miniorm
        .GoodNameResolver.isGood;

public class DatabaseService<T> implements Closeable {
    private final String connectionName;
    private final String username;

    private final String password;

    private Class<T> clazz;

    private JdbcConnectionPool pool;

    private String tableName;
    private Field[] fields;
    private int pkIndex = -1;

    static String convert(String name) {
        if ('a' <= name.charAt(0) && name.charAt(0) <= 'z') {
            return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
        }
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
    }

    String getColumnName(Field f) {
        String name = f.getAnnotation(Column.class).name();
        if (name.equals("")) {
            return convert(f.getName());
        }
        return name;
    }

    void init() throws IllegalArgumentException, IOException {
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
        List<Field> fieldsList = new ArrayList<>();
        for (Field f: clazz.getDeclaredFields()) {
            if (f.isAnnotationPresent(Column.class)) {
                String name = getColumnName(f);
                names.add(name);
                if (!isGood(name)) {
                    throw new IllegalArgumentException("Bad column name");
                }

                f.setAccessible(true);
                fieldsList.add(f);
                if (f.isAnnotationPresent(PrimaryKey.class)) {
                    if (pkIndex == -1) {
                        pkIndex = fieldsList.size() - 1;
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

        fields = new Field[fieldsList.size()];
        fields = fieldsList.toArray(fields);

        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("No H2 driver found");
        }

        pool = JdbcConnectionPool.create(connectionName, username, password);
    }

    DatabaseService(Class<T> newClazz, String properties) throws IOException {
        Properties credits = new Properties();
        try (InputStream inputStream
                     = this.getClass().getResourceAsStream(properties)) {
            credits.load(inputStream);
        }
        connectionName = credits.getProperty("connection_name");
        username = credits.getProperty("username");
        password = credits.getProperty("password");

        clazz = newClazz;
        init();
    }

    DatabaseService(Class<T> newClazz) throws IOException {
        this(newClazz, "/h2.properties");
    }

    void createTable() throws SQLException {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("CREATE TABLE IF NOT EXISTS ")
                .append(tableName)
                .append("(");
        for (int i = 0; i < fields.length; ++i) {
            if (i != 0) {
                queryBuilder.append(", ");
            }
            queryBuilder.append(getColumnName(fields[i])).append(" ")
                    .append(H2StringsResolver.resolve(fields[i].getType()));
            if (i == pkIndex) {
                queryBuilder.append(" PRIMARY KEY");
            }
        }
        queryBuilder.append(")");
        try (Connection conn = pool.getConnection()) {
            conn.createStatement().execute(queryBuilder.toString());
        }

    }

    void dropTable() throws SQLException {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("DROP TABLE IF EXISTS ")
                .append(tableName);
        try (Connection conn = pool.getConnection()) {
            conn.createStatement().execute(queryBuilder.toString());
        }
    }

    public void insert(T record) throws SQLException, IllegalAccessException {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("INSERT INTO ").append(tableName).append(" (");
        for (int i = 0; i < fields.length; ++i) {
            if (i != 0) {
                queryBuilder.append(", ");
            }
            queryBuilder.append(getColumnName(fields[i])).append(" ");
        }
        queryBuilder.append(") VALUES (");
        for (int i = 0; i < fields.length; ++i) {
            if (i != 0) {
                queryBuilder.append(", ");
            }
            queryBuilder.append("?");
        }
        queryBuilder.append(")");


        try (Connection conn = pool.getConnection()) {
            PreparedStatement statement
                    = conn.prepareStatement(queryBuilder.toString());
            for (int i = 0; i < fields.length; ++i) {
                statement.setObject(i + 1, fields[i].get(record));
            }
            statement.execute();
        }
    }

    public void delete(T record) throws IllegalArgumentException,
            IllegalAccessException, SQLException {
        if (pkIndex == -1) {
            throw new IllegalArgumentException("NO @PrimaryKey");
        }
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("DELETE FROM ").append(tableName)
                .append(" WHERE ").append(fields[pkIndex].getName())
                .append(" = ?");



        try (Connection conn = pool.getConnection()) {
            PreparedStatement statement
                    = conn.prepareStatement(queryBuilder.toString());
            statement.setObject(1, fields[pkIndex].get(record));
            statement.execute();
        }
    }

    public void update(T record) throws IllegalArgumentException,
            SQLException, IllegalAccessException {
        if (pkIndex == -1) {
            throw new IllegalArgumentException("NO @PrimaryKey");
        }

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("UPDATE ").append(tableName).append(" SET ");
        for (int i = 0; i < fields.length; ++i) {
            if (i != 0) {
                queryBuilder.append(", ");
            }
            queryBuilder.append(getColumnName(fields[i])).append(" = ?");
        }
        queryBuilder.append(" WHERE ").append(getColumnName(fields[pkIndex]))
                .append(" = ?");


        try (Connection conn = pool.getConnection()) {
            PreparedStatement statement
                    = conn.prepareStatement(queryBuilder.toString());
            for (int i = 0; i < fields.length; ++i) {
                statement.setObject(i + 1, fields[i].get(record));
            }
            statement.setObject(fields.length + 1, fields[pkIndex].get(record));
            statement.execute();
        }
    }

    public List<T> queryForAll() throws SQLException {
        List<T> result = new ArrayList<>();

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT * FROM ").append(tableName);

        try (Connection conn = pool.getConnection()) {
            try (ResultSet rs = conn.createStatement()
                    .executeQuery(queryBuilder.toString())) {
                while (rs.next()) {
                    T record = clazz.newInstance();
                    for (int i = 0; i < fields.length; ++i) {
                        if (fields[i].getClass()
                                .isAssignableFrom(Number.class)) {
                            Long val = rs.getLong(i + 1);
                            fields[i].set(record, val);
                        } else if (fields[i].getType() != String.class) {
                            fields[i].set(record, rs.getObject(i + 1));
                        } else {
                            Clob data = rs.getClob(i + 1);
                            fields[i].set(record,
                                    data.getSubString(1, (int) data.length()));
                        }
                    }
                    result.add(record);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IllegalArgumentException("wrong class");
            }
        }
        return result;
    }

    public <K> T queryById(K key) throws IllegalArgumentException,
            SQLException {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT * FROM ").append(tableName)
                .append(" WHERE ").append(fields[pkIndex].getName())
                .append(" = ?");
        try (Connection conn = pool.getConnection()) {
            PreparedStatement statement
                    = conn.prepareStatement(queryBuilder.toString());
            statement.setString(1, key.toString());

            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                T record = clazz.newInstance();
                for (int i = 0; i < fields.length; ++i) {
                    if (fields[i].getClass()
                            .isAssignableFrom(Number.class)) {
                        Long val = rs.getLong(i + 1);
                        fields[i].set(record, val);
                    } else if (fields[i].getType() != String.class) {
                        fields[i].set(record, rs.getObject(i + 1));
                    } else {
                        Clob data = rs.getClob(i + 1);
                        fields[i].set(record,
                                data.getSubString(1, (int) data.length()));
                    }
                }
                return record;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IllegalArgumentException("wrong class");
            }
        }

    }

    @Override
    public void close() throws IOException {
        if (pool != null) {
            pool.dispose();
        }
    }
}

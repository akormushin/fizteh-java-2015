package ru.fizteh.fivt.students.nmakeenkov.miniorm;

import com.google.common.base.CaseFormat;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService<T> {
    private static final String CONNECTION_PATH = "~/test";
    private static final String USER_NAME = "user";
    private static final String PASSWORD = "password";

    private Connection connection;
    private String table;
    private Class<T> tClass;
    private Field primaryKey;
    private int primaryKeyID = -1;
    private List<Field> fieldList = new ArrayList<>();
    private List<String> fieldNames = new ArrayList<>();

    public DatabaseService(Class<T> clazz) throws Exception {
        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new IllegalArgumentException();
        }
        table = clazz.getAnnotation(Table.class).name();
        tClass = clazz;
        if (table.length() == 0) {
            table = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,
                    clazz.getSimpleName());
        }

        for (Field column : clazz.getDeclaredFields()) {
            if (!column.isAnnotationPresent(Column.class)) {
                throw new IllegalArgumentException();
            }

            String name = column.getAnnotation(Column.class).name();
            if (name.equals("")) {
                name = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, column.getName());
            }
            fieldNames.add(name);

            if (column.isAnnotationPresent(PrimaryKey.class)) {
                if (primaryKey != null) {
                    throw new IllegalArgumentException();
                } else {
                    primaryKey = column;
                    primaryKeyID = fieldList.size();
                }
            }
            fieldList.add(column);
        }
        if (primaryKeyID == -1) {
            throw new IllegalArgumentException("No primary key");
        }

        Class.forName("org.h2.Driver"); // loads the class if it is not already loaded
        connection = DriverManager.getConnection("jdbc:h2:" + CONNECTION_PATH,
                USER_NAME, PASSWORD);
    }

    public <K> T queryById(K key) throws Exception {
        PreparedStatement query = connection.prepareStatement(
                "SELECT * FROM " + table + " WHERE "
                        + fieldNames.get(primaryKeyID) + "=" + key);
        List<T> result = getListByResultSet(query.executeQuery());
        if (result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    public List<T> queryForAll() throws Exception {
        PreparedStatement query = connection.prepareStatement(
                "SELECT * FROM " + table);
        return getListByResultSet(query.executeQuery());
    }

    public void insert(T record) throws Exception {
        StringBuilder query = new StringBuilder("INSERT INTO "
                + table + " VALUES (");
        for (int i = 0; i < (int) fieldList.size(); ++i) {
            if (i != 0) {
                query.append(',');
            }
            if (fieldList.get(i).getGenericType() == String.class) {
                query.append('\'');
            }
            query.append(fieldList.get(i).get(record));
            if (fieldList.get(i).getGenericType() == String.class) {
                query.append('\'');
            }
        }
        connection.createStatement().execute(query.append(')').toString());
    }

    public void update(T record) throws Exception {
        StringBuilder query = new StringBuilder("UPDATE " + table
                + " SET ");
        for (int i = 0; i < (int) fieldList.size(); ++i) {
            if (i != 0) {
                query.append(',');
            }
            query.append(fieldNames.get(i)).append('=');

            if (fieldList.get(i).getGenericType() == String.class) {
                query.append('\'');
            }
            query.append((fieldList.get(i).get(record)));
            if (fieldList.get(i).getGenericType() == String.class) {
                query.append('\'');
            }
        }
        query.append(" WHERE ").append(fieldNames.get(primaryKeyID)).append("=")
                .append(primaryKey.get(record));
        connection.createStatement().execute(query.toString());
    }

    public <K> void delete(K key) throws Exception {
        StringBuilder query = new StringBuilder("DELETE FROM " + table + " WHERE ")
                .append(fieldNames.get(primaryKeyID)).append("=");
        query.append(key);
        connection.createStatement().execute(query.toString());
    }

    public void createTable() throws Exception {
        StringBuilder query = new StringBuilder("CREATE TABLE "
                + "IF NOT EXISTS " + table + "(");
        for (int i = 0; i < (int) fieldList.size(); i++) {
            if (i != 0) {
                query.append(",");
            }
            String type = classToSqlClass(fieldList.get(i).getType());
            if (i == primaryKeyID) {
                type += " PRIMARY KEY NOT NULL";
            }
            query.append(fieldNames.get(i)).append(" ").append(type);
        }
        connection.createStatement().execute(query.append(')').toString());
    }

    public void dropTable() throws Exception {
        PreparedStatement query = connection.prepareStatement(
                "DROP TABLE IF EXISTS " + table);
        query.execute();
    }

    private String classToSqlClass(Class clazz) {
        if (clazz == Short.class) {
            return "SMALLINT";
        }
        if (clazz == Integer.class) {
            return "INTEGER";
        }
        if (clazz == Long.class) {
            return "BIGINT";
        }
        if (clazz == Double.class) {
            return "DOUBLE";
        }
        if (clazz == Boolean.class) {
            return "BOOLEAN";
        }
        if (clazz == String.class) {
            return "CLOB";
        }
        throw new IllegalArgumentException();
    }

    public List<T> getListByResultSet(ResultSet resultSet) throws Exception {
        List<T> ans = new ArrayList<>();
        while (resultSet.next()) {
            T obj = tClass.newInstance();
            for (int i = 0; i < (int) fieldList.size(); ++i) {
                String value = resultSet.getString(i + 1);
                if (fieldList.get(i).getType().equals(String.class)) {
                    fieldList.get(i).set(obj, value);
                } else {
                    fieldList.get(i).set(obj,
                            fieldList.get(i).getType()
                                    .getMethod("valueOf", String.class)
                                    .invoke(null, value));
                }
            }
            ans.add(obj);
        }
        resultSet.close();
        return ans;
    }
}

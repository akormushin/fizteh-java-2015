package ru.fizteh.fivt.students.thefacetakt.miniorm;

import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.AdditionalMatchers.or;
import static ru.fizteh.fivt.students.thefacetakt.miniorm.DatabaseService.convert;

/**
 * Created by thefacetakt on 15.12.15.
 */
public class DatabaseServiceTest {

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalClass_1() throws Exception {
        class X {
            @Column
            int y;
        }
        new DatabaseService<X>(X.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalClass_2() throws Exception {
        @Table
        class X {
            @Column
            int y;

            @Column(name = "y")
            String x;
        }
        new DatabaseService<X>(X.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalClass_3_0() throws Exception {
        @Table
        class X {
            @Column
            int y;
        }
        DatabaseService<X> x = new DatabaseService<>(X.class);
        x.delete(new X());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalClass_3_1() throws Exception {
        @Table
        class X {
            @Column
            int y;
        }
        DatabaseService<X> x = new DatabaseService<>(X.class);
        x.update(new X());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalClass_4() throws Exception {
        @Table
        class X {
            @PrimaryKey
            @Column
            int y;


            @PrimaryKey
            String x;
        }
        new DatabaseService<X>(X.class);
    }

    @Test
    public void convertTest() throws Exception {
        assertThat(convert("camelCase"), is("camel_case"));
        assertThat(convert("CamelCase"), is("camel_case"));
    }


    private static final String CONNECTION_NAME;
    private static final String USERNAME;
    private static final String PASSWORD;

    static {
        Properties credits = new Properties();
        try (InputStream inputStream
                     = DatabaseServiceTest.class
                .getResourceAsStream("/h2test.properties")) {
            credits.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CONNECTION_NAME = credits.getProperty("connection_name");
        USERNAME = credits.getProperty("username");
        PASSWORD = credits.getProperty("password");
    }

    @Table
    static class MyClass {
        @PrimaryKey
        @Column
        Integer x;

        @Column
        String y;

        MyClass() {
        }

        MyClass(Integer x, String y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof MyClass) {
                MyClass omc = (MyClass) o;
                return x.equals(omc.x) && y.equals(omc.y);
            }
            return false;
        }
    }

    @Test
    public void bigTest() throws Exception {


        DatabaseService<MyClass> x = new DatabaseService<>(MyClass.class,
                "/h2test.properties");
        JdbcConnectionPool pool = JdbcConnectionPool
                .create(CONNECTION_NAME, USERNAME, PASSWORD);
        Connection tmp;
        tmp = pool.getConnection();
        tmp.createStatement()
                .execute("DROP TABLE IF EXISTS my_class");
        tmp.close();

        x.createTable();
        tmp = pool.getConnection();
        tmp.createStatement()
            .execute("INSERT INTO my_class (x, y) VALUES (1, 'one')");
        tmp.close();
        assertThat(x.queryById(1).y, is("one"));
        x.insert(new MyClass(2, "X"));
        x.insert(new MyClass(3, "X"));
        tmp = pool.getConnection();
        ResultSet rs = tmp.createStatement()
                .executeQuery("SELECT * FROM my_class WHERE y = 'X'");
        int count = 0;
        int sum = 0;
        while(rs.next()) {
            assertThat(rs.getInt(1), anyOf(is(2), is(3)));
            ++count;
            sum += rs.getInt(1);
        }

        rs.close();
        tmp.close();

        assertThat(count,  is(2));
        assertThat(sum, is(5));

        assertThat(x.queryForAll().size(), is(3));
        assertThat(x.queryForAll(), hasItem(new MyClass(1, "one")));
        x.update(new MyClass(1, "two"));
        assertThat(x.queryForAll(), not(hasItem(new MyClass(1, "one"))));
        assertThat(x.queryForAll(), hasItem(new MyClass(1, "two")));
        x.delete(new MyClass(1, ""));
        assertThat(x.queryForAll(), not(hasItem(new MyClass(1, "two"))));


        x.dropTable();
        x.close();
    }
}
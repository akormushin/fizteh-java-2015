package ru.fizteh.fivt.students.nmakeenkov.miniorm;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;

public class DatabaseServiceTest {

    @Table
    public static class MyTable {
        @PrimaryKey
        @Column
        Integer id;

        @Column
        String value;

        MyTable(Integer id, String value) {
            this.id = id;
            this.value = value;
        }

        MyTable() {
            this.id = 0;
            this.value = "";
        }

        public String toString() {
            return "(" + id.toString() + ", " + value + ")";
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof MyTable)) {
                return false;
            }
            MyTable other = (MyTable) obj;
            return id.equals(other.id)
                    && value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return value.hashCode() * 227 + id.hashCode();
        }
    }

    private DatabaseService databaseService;

    @Before
    public void setUp() throws Exception {
        databaseService = new DatabaseService(MyTable.class);
    }

    @Test
    public void myTest() throws Exception {
        databaseService.dropTable();
        databaseService.createTable();
        List<MyTable> q = new ArrayList<>();
        q.add(new MyTable(1, "a01"));
        q.add(new MyTable(2, "b02"));
        q.add(new MyTable(3, "c03"));
        q.add(new MyTable(4, "d04"));
        q.add(new MyTable(5, "e05"));
        for (MyTable i : q) {
            databaseService.insert(i);
        }
        List<MyTable> ans = databaseService.queryForAll();
        Assert.assertArrayEquals(q.toArray(), ans.toArray());

        q.set(2, new MyTable(3, "check"));
        databaseService.update(q.get(2));
        Assert.assertEquals(q.get(2), databaseService.queryById(3));

        q.remove(0);
        databaseService.delete(1);
        ans = databaseService.queryForAll();
        Assert.assertArrayEquals(q.toArray(), ans.toArray());
    }

    @Test(expected = Exception.class)
    public void testDropTable() throws Exception {
        databaseService.dropTable();
        databaseService.queryForAll();
    }
}
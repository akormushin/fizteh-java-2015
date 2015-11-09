package ru.fizteh.fivt.students.thefacetakt.collectionsql;

//import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

//
//import static ru.fizteh.fivt.students.thefacetakt.collectionsql.OrderByConditions.asc;
//import static ru.fizteh.fivt.students.thefacetakt.collectionsql.OrderByConditions.desc;
//import static ru.fizteh.fivt.students
//        .thefacetakt.collectionsql.Sources.list;
//import static ru.fizteh.fivt.students
//        .thefacetakt.collectionsql.impl.FromStmt.from;
//import static ru.fizteh.fivt.students.thefacetakt.collectionsql
//        .impl.aggregates.Aggregates.max;

public class CollectionsQL {
    public static class Temp {
        private Integer x;

        public Temp(Integer xx) {
            this.x = xx;
        }

        public Integer getX() {
            return x;
        }

        public String toString() {
            return x.toString();
        }
    }
    public static void main(String[] args)
            throws InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {
//        System.out.println(from(list(1, 2, 3, 4, 5, 6, 7, 8, 9, 9, 10))
//                .select(Temp.class, max((Integer x) -> x))
//
//                .groupBy(x -> x % 3).limit(2).orderBy(desc(x -> x.getX()))
//                .execute());
        //Comments for checkstyle
    }


    public static class Student {
        private final String name;

        private final LocalDate dateOfBith;

        private final String group;

        public String getName() {
            return name;
        }

        public Student(String name, LocalDate dateOfBith, String group) {
            this.name = name;
            this.dateOfBith = dateOfBith;
            this.group = group;
        }

        public LocalDate getDateOfBith() {
            return dateOfBith;
        }

        public String getGroup() {
            return group;
        }

        public long age() {
            return ChronoUnit.YEARS.between(getDateOfBith(), LocalDateTime.now());
        }

        public static Student student(String name, LocalDate dateOfBith, String group) {
            return new Student(name, dateOfBith, group);
        }
    }


    public static class Statistics {

        private final String group;
        private final Long count;
        private final Long age;

        public String getGroup() {
            return group;
        }

        public Long getCount() {
            return count;
        }

        public Long getAge() {
            return age;
        }

        public Statistics(String group, Long count, Long age) {
            this.group = group;
            this.count = count;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Statistics{"
                    + "group='" + group + '\''
                    + ", count=" + count
                    + ", age=" + age
                    + '}';
        }
    }
}

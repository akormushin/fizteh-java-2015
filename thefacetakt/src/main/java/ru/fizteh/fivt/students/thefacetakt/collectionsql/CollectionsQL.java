package ru.fizteh.fivt.students.thefacetakt.collectionsql;

//import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
//import java.util.Comparator;
//import java.util.function.Function;
//
//import static ru.fizteh.fivt.students
//        .thefacetakt.collectionsql.Sources.list;
//import static ru.fizteh.fivt.students
//        .thefacetakt.collectionsql.impl.FromStmt.from;
//import static ru.fizteh.fivt.students.thefacetakt.collectionsql
//        .impl.aggregates.Aggregates.min;

public class CollectionsQL {


    static void main(String[] args) {
        return;
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

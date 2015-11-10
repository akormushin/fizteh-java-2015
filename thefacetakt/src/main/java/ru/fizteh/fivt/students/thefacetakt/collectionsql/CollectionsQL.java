package ru.fizteh.fivt.students.thefacetakt.collectionsql;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static ru.fizteh.fivt.students.thefacetakt
        .collectionsql.Conditions.rlike;
import static ru.fizteh.fivt.students.thefacetakt.collectionsql
        .OrderByConditions.asc;
import static ru.fizteh.fivt.students.thefacetakt
        .collectionsql.OrderByConditions.desc;
import static ru.fizteh.fivt.students.thefacetakt
        .collectionsql.impl.FromStmt.from;
import static ru.fizteh.fivt.students.thefacetakt
        .collectionsql.Sources.list;
import static ru.fizteh.fivt.students.thefacetakt
        .collectionsql.impl.aggregates.Aggregates.avg;
import static ru.fizteh.fivt.students.thefacetakt
        .collectionsql.impl.aggregates.Aggregates.count;
import static ru.fizteh.fivt.students.thefacetakt.collectionsql.
        CollectionsQL.Student.student;

public class CollectionsQL {

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

        public static Student student(String name, LocalDate dateOfBith,
                                      String group) {
            return new Student(name, dateOfBith, group);
        }

    }



    public static class Statistics {

        private final String group;
        private final Long count;
        private final Double age;

        public String getGroup() {
            return group;
        }

        public Long getCount() {
            return count;
        }

        public Double getAge() {
            return age;
        }

        public Statistics(String group, Long count, Double age) {
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

    public static void main(String[] args) throws InvocationTargetException,
            NoSuchMethodException, InstantiationException,
            IllegalAccessException {
        final int twenty = 20;
        final int hundred = 100;
        @SuppressWarnings("unchecked")
        Iterable<Statistics> statistics =
                from(list(
                        student("ivanov", LocalDate.parse("1986-08-06"), "494"),
                        student("sidorov", LocalDate.parse("1986-08-06"),
                                "495"),
                        student("smith", LocalDate.parse("1986-08-06"), "495"),
                        student("petrov", LocalDate.parse("2006-08-06"),
                                "494")))
                        .select(Statistics.class, Student::getGroup,
                                count(Student::getGroup), avg(Student::age))
                        .where(rlike(Student::getName, ".*ov")
                                .and(s -> s.age() > twenty))
                        .groupBy(Student::getGroup)
                        .having(s -> s.getCount() > 0)
                        .orderBy(asc(Statistics::getGroup),
                                desc(Statistics::getCount))
                        .limit(hundred)
                        .union()
                        .from(list(student("ivanov",
                                LocalDate.parse("1985-08-06"), "494")))
                        .selectDistinct(Statistics.class,
                                s -> "all", count(s -> 1),
                                avg(Student::age))
                        .execute();
        System.out.println(statistics);
    }
}

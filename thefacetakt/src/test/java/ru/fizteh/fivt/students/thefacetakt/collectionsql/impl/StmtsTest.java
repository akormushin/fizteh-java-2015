package ru.fizteh.fivt.students.thefacetakt.collectionsql.impl;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.*;
import static ru.fizteh.fivt.students.thefacetakt
        .collectionsql.Conditions.rlike;
import static ru.fizteh.fivt.students.thefacetakt
        .collectionsql.OrderByConditions.asc;
import static ru.fizteh.fivt.students.thefacetakt
        .collectionsql.OrderByConditions.desc;
import static ru.fizteh.fivt.students.thefacetakt
        .collectionsql.Sources.list;
import static ru.fizteh.fivt.students.thefacetakt
        .collectionsql.impl.FromStmt.from;
import static ru.fizteh.fivt.students.thefacetakt
        .collectionsql.impl.StmtsTest.Student.student;
import static ru.fizteh.fivt.students.thefacetakt
        .collectionsql.impl.aggregates.Aggregates.count;
import static ru.fizteh.fivt.students.thefacetakt
        .collectionsql.impl.aggregates.Aggregates.max;
import static ru.fizteh.fivt.students.thefacetakt
        .collectionsql.impl.aggregates.Aggregates.min;

/**
 * Created by thefacetakt on 12.11.15.
 */

public class StmtsTest {
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

        public Statistics(String group, Long count, Long age) {
            this.group = group;
            this.count = count;
            this.age = age.doubleValue();
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

    @Test
    public void test1() throws Exception {
        @SuppressWarnings("unchecked")
        List<Statistics> statistics =
                from(list(
                        student("ivanov", LocalDate.parse("1970-08-06"), "494"),
                        student("sidorov", LocalDate.parse("1986-08-06"),
                                "495"),
                        student("smith", LocalDate.parse("1986-08-06"), "495"),
                        student("petrov", LocalDate.parse("2006-08-06"),
                                "494")))
                        .select(Statistics.class, Student::getGroup,
                                count(Student::getGroup), max(Student::age))
                        .where(rlike(Student::getName, ".*ov")
                                .and(s -> s.age() > 20))
                        .groupBy(Student::getGroup)
                        .having(s -> s.getCount() > 0)
                        .orderBy(asc(Statistics::getGroup),
                                desc(Statistics::getCount))
                        .union()
                        .from(list(student("ivanov",
                                LocalDate.parse("1985-08-06"), "494")))
                        .selectDistinct(Statistics.class,
                                s -> "all", count(s -> 1),
                                min(Student::age))
                        .execute();
        System.out.println(statistics);
        assertThat(statistics.stream().map(Statistics::toString)
                .collect(Collectors.toList()),
                hasItems("Statistics{group='494', count=1, age=45.0}",
                        "Statistics{group='495', count=1, age=29.0}",
                        "Statistics{group='all', count=1, age=30.0}"));
    }
}
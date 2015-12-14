package ru.fizteh.fivt.students.thefacetakt.collectionsql;


import ru.fizteh.fivt.students.thefacetakt.collectionsql.impl.Tuple;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


import static ru.fizteh.fivt.students.thefacetakt.collectionsql.
        CollectionsQL.Student.student;
import static ru.fizteh.fivt.students.thefacetakt.collectionsql.Sources.list;
import static ru.fizteh.fivt.students.thefacetakt.collectionsql.
        impl.FromStmt.from;

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
            return ChronoUnit.YEARS.between(getDateOfBith(),
                    LocalDateTime.now());
        }

        public static Student student(String name, LocalDate dateOfBith,
                                      String group) {
            return new Student(name, dateOfBith, group);
        }
    }

    public static class Group {
        private final String group;
        private final String mentor;

        public Group(String group, String mentor) {
            this.group = group;
            this.mentor = mentor;
        }

        public String getGroup() {
            return group;
        }

        public String getMentor() {
            return mentor;
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

    public static void main(String[] args) throws InvocationTargetException,
            NoSuchMethodException, InstantiationException,
            IllegalAccessException {
        Iterable<Tuple<String, String>> mentorsByStudent =
                from(list(student("ivanov",
                        LocalDate.parse("1985-08-06"), "494")))
                .join(list(new Group("494", "mr.sidorov")))
                .on(f -> 1, s -> 1)
                .select(sg -> sg.getFirst().getName(),
                        sg -> sg.getSecond().getMentor())
                        .execute();
        mentorsByStudent.forEach(System.out::println);
    }


}

package ru.fizteh.fivt.students.akormushin.annotations.junit;

import ru.fizteh.fivt.students.akormushin.annotations.xml.Group;
import ru.fizteh.fivt.students.akormushin.annotations.xml.Student;

/**
 * Created by kormushin on 22.09.15.
 */
public class Main {
    public static void main(String[] args) throws IllegalAccessException {
        Database database = new Database();

        Student student1 = new Student("Ivan", "Ivanov");
        Student student2 = new Student("Petr", "Petrov");

        Group group1 = new Group("494", student1, student2);
        Group group2 = new Group("495", student1, student2);

        database.insert(group1);
        database.insert(group2);

        System.out.println(database.select("494"));

        System.out.println(database.exists(group1));

        System.out.println(new GroupStatistics(database).getStudentsByGroup());
    }


}

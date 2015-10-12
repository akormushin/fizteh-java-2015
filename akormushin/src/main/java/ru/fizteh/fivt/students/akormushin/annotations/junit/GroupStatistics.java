package ru.fizteh.fivt.students.akormushin.annotations.junit;

import ru.fizteh.fivt.students.akormushin.annotations.xml.Group;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kormushin on 22.09.15.
 */
public class GroupStatistics {

    private final Database<String, Group> database;

    public GroupStatistics(Database<String, Group> database) {
        this.database = database;
    }

    public Map<String, Integer> getStudentsByGroup()
            throws IllegalAccessException {
        Map<String, Integer> studentsByGroup = new HashMap<>();
        for (Group group : database.select()) {
            studentsByGroup.put(group.getName(), group.getStudents().size());
        }
        return studentsByGroup;
    }
}

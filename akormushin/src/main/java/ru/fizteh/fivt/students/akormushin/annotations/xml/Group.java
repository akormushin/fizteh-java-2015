package ru.fizteh.fivt.students.akormushin.annotations.xml;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kormushin on 22.09.15.
 */
@XmlRootElement
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
@DatabaseTable
public class Group {

    @PrimaryKey
    @XmlAttribute
    private String name;

    @XmlElement(name = "student")
    private List<Student> students = new ArrayList<>();

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Group() {
    }

    public Group(String name, Student... students) {
        this.name = name;
        this.students = new ArrayList<Student>(Arrays.asList(students));
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                ", students=" + students +
                '}';
    }
}

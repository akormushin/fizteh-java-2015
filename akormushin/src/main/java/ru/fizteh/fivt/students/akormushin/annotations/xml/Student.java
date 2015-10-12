package ru.fizteh.fivt.students.akormushin.annotations.xml;

import javax.xml.bind.annotation.*;

/**
 * Created by kormushin on 22.09.15.
 */
@XmlRootElement
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Student implements Cloneable {

    @XmlAttribute
    private String firstName;

    @XmlAttribute
    private String secondName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public Student() {
    }

    public Student(String firstName, String secondName) {
        this.firstName = firstName;
        this.secondName = secondName;
    }

    @Override
    public String toString() {
        return "Student{"
                + "firstName='" + getFirstName() + '\''
                + ", secondName='" + getSecondName() + '\''
                + '}';
    }

    @Override
    public Student clone() {
        try {
            return (Student) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
    }
}

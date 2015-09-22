package ru.fizteh.fivt.students.akormushin.annotations.xml;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * Created by kormushin on 22.09.15.
 */
public class XmlMarshalling {

    public static void main(String[] args) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(
                Student.class, Group.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        Student student1 = new Student("Ivan", "Ivanov");
        Student student2 = new Student("Petr", "Petrov");

        Group group = new Group("495", student1, student2);

        marshaller.marshal(student1, System.out);
        System.out.println();
        marshaller.marshal(group, System.out);
    }
}

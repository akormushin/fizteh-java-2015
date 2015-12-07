package ru.fizteh.fivt.students.akormushin.annotations.junit;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.fizteh.fivt.students.akormushin.annotations.xml.Group;
import ru.fizteh.fivt.students.akormushin.annotations.xml.Student;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

/**
 * Created by kormushin on 22.09.15.
 */
@RunWith(MockitoJUnitRunner.class)
public class GroupStatisticsTest {

    @InjectMocks
    private GroupStatistics groupStatistics;

    @Mock
    private Database<String, Group> database;// = mock(Database.class);

    @BeforeClass
    public static void beforeAll() {
        System.out.println("before all");
    }

    @Before
    public void setUp1() throws IllegalAccessException {
        Student student1 = new Student("Ivan", "Ivanov");
        Student student2 = new Student("Petr", "Petrov");

        Group group1 = new Group("494", student1, student2);
        Group group2 = new Group("495", student1, student2);

        when(database.select()).thenReturn(Arrays.asList(group1, group2));
        when(database.select(anyObject())).thenReturn(group1);
        when(database.select(any())).thenAnswer(i -> {
            if (i.getArguments()[0].equals("494")) {
                return group1;
            }
            return null;
        });
    }

    @After
    public void afterTest() {
        System.out.println("after test");
    }


    @Test
    public void testGetStudentsByGroup() throws Exception {
        assertThat(groupStatistics.getStudentsByGroup().get("494"), is(2));

        assertThat(groupStatistics.getStudentsByGroup().get("495"), is(2));
    }

}
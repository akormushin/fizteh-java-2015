package ru.fizteh.fivt.students.akormushin.annotations.junit;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.security.acl.Group;

/**
 * Created by kormushin on 22.09.15.
 */
@RunWith(MockitoJUnitRunner.class)
public class GroupStatisticsTest extends TestCase {

    @InjectMocks
    private GroupStatistics groupStatistics;

    @Mock
    private Database<String, Group> database;

    @Before
    public void setUp(){

    }

    @Test
    public void testGetStudentsByGroup() throws Exception {
        System.out.println(groupStatistics.getStudentsByGroup());
    }
}
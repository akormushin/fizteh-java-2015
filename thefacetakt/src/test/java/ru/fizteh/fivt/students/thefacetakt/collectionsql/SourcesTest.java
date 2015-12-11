package ru.fizteh.fivt.students.thefacetakt.collectionsql;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.*;
import static ru.fizteh.fivt.students.thefacetakt.collectionsql.Sources.list;

/**
 * Created by thefacetakt on 11.11.15.
 */
public class SourcesTest {

    @Test
    public void testList_1() throws Exception {
        List<Object> testSubject = list(1, "2", 3, 4, 5);
        assertThat(testSubject.size(), is(5));
        assertThat(testSubject, contains(1, "2", 3, 4, 5));
    }

    @Test
    public void testList_2() throws Exception {
        List<Object> testSubject = list();
        assertThat(testSubject.size(), is(0));
    }
}
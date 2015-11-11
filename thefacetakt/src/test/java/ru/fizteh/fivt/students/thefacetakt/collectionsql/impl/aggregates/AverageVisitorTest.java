package ru.fizteh.fivt.students.thefacetakt.collectionsql.impl.aggregates;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by thefacetakt on 11.11.15.
 */
public class AverageVisitorTest {

    @Test
    public void test_1() throws Exception {
        AverageVisitor<Integer> visitor
                = new AverageVisitor<>((Integer x) -> x);
        visitor.visit(1);
        visitor.visit(2);
        assertThat(visitor.result(), is(1.5));
        visitor.visit(-1);
        visitor.visit(-2);
        assertThat(visitor.result(), is(0.0));
    }

    @Test(expected = IllegalStateException.class)
    public void test_2() throws Exception {
        AverageVisitor<Integer> visitor
                = new AverageVisitor<>((Integer x) -> x);
        visitor.result();
    }

}
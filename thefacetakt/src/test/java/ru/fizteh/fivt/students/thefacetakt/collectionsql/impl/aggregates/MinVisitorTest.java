package ru.fizteh.fivt.students.thefacetakt.collectionsql.impl.aggregates;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

/**
 * Created by thefacetakt on 12.11.15.
 */
public class MinVisitorTest {

    @Test
    public void test_1() throws Exception {
        MinVisitor<String, Integer> visitor =
                new MinVisitor<>(String::length);
        visitor.visit("aba");
        visitor.visit("abacaba");
        assertThat(visitor.result(), is("aba".length()));
        visitor.visit("aba");
        assertThat(visitor.result(), is("aba".length()));
    }

    @Test
    public void test_2() throws Exception {
        MinVisitor<String, Integer> visitor =
                new MinVisitor<>(String::length);

        assertThat(visitor.result(), is(nullValue()));
        visitor.visit("");
        assertThat(visitor.result(), is(0));
    }
}
package ru.fizteh.fivt.students.thefacetakt.collectionsql.impl.aggregates;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

/**
 * Created by thefacetakt on 11.11.15.
 */
public class MaxVisitorTest {

    @Test
    public void test_1() throws Exception {
        MaxVisitor<String, Integer> visitor =
                new MaxVisitor<>(String::length);
        visitor.visit("aba");
        visitor.visit("abacaba");
        assertThat(visitor.result(), is("abacaba".length()));
        visitor.visit("abacaba");
        assertThat(visitor.result(), is("abacaba".length()));
    }

    @Test
    public void test_2() throws Exception {
        MaxVisitor<String, Integer> visitor =
                new MaxVisitor<>(String::length);

        assertThat(visitor.result(), is(nullValue()));
        visitor.visit("");
        assertThat(visitor.result(), is(0));
    }
}
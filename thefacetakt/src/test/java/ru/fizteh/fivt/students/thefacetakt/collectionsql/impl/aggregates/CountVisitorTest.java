package ru.fizteh.fivt.students.thefacetakt.collectionsql.impl.aggregates;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by thefacetakt on 11.11.15.
 */
public class CountVisitorTest {

    @Test
    public void test() throws Exception {
        CountVisitor<String> visitor =
                new CountVisitor<>(x -> (x.length() % 3 == 0
                        ? null : x.length()));
        visitor.visit("aba");
        visitor.visit("abacaba");
        assertThat(visitor.result(), is(1L));
        visitor.visit("abacabaa");
        assertThat(visitor.result(), is(2L));
    }
}
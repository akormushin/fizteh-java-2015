package ru.fizteh.fivt.students.thefacetakt.collectionsql;

import org.junit.Test;

import java.util.Comparator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static ru.fizteh.fivt.students.thefacetakt.collectionsql.OrderByConditions.asc;
import static ru.fizteh.fivt.students.thefacetakt.collectionsql.OrderByConditions.desc;

/**
 * Created by thefacetakt on 11.11.15.
 */

class X implements Comparable<X> {
    private String xName;

    public String getName() {
        return xName;
    }

    X(String name) {
        xName = name;
    }

    @Override
    public int compareTo(X o) {
        return new Integer(xName.length())
                .compareTo(o.getName().length());
    }
}

public class OrderByConditionsTest {

    @Test
    public void testAsc() throws Exception {
        Comparator<String> comparator = asc(X::new);
        assertThat(comparator.compare("a", "aba"), is(-1));
        assertThat(comparator.compare("Sapog", "Poroh"), is(0));
        assertThat(comparator.compare("abacabadaba", "abacaba"), is(1));
    }

    @Test
    public void testDesc() throws Exception {
        Comparator<String> comparator = desc(X::new);
        assertThat(comparator.compare("a", "aba"), is(1));
        assertThat(comparator.compare("Sapog", "Poroh"), is(0));
        assertThat(comparator.compare("abacabadaba", "abacaba"), is(-1));
    }
}
package ru.fizteh.fivt.students.thefacetakt.miniorm;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static ru.fizteh.fivt.students.thefacetakt.miniorm.H2StringsResolver.resolve;

/**
 * Created by thefacetakt on 15.12.15.
 */
public class H2StringsResolverTest {

    @Test
    public void testResolve() throws Exception {
        Integer[] x = new Integer[5];
        assertThat(resolve(x.getClass()), is("ARRAY"));
        assertThat(resolve(Short.class), is("SMALLINT"));
        assertThat(resolve(H2StringsResolver.class), is("OTHER"));
        assertThat(resolve(String.class), is("CLOB"));
    }
}
package ru.fizteh.fivt.students.thefacetakt.miniorm;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static ru.fizteh.fivt.students.thefacetakt.miniorm.GoodNameResolver.isGood;

/**
 * Created by thefacetakt on 15.12.15.
 */
public class GoodNameResolverTest {

    @Test
    public void testIsGood() throws Exception {
        assertThat(isGood("abaCabaDaba9_01"), is(true));
        assertThat(isGood("ADMIN' OR 1=1"), is(false));
        assertThat(isGood("WHERE"), is(true));
        assertThat(isGood("WHERES"), is(true));
    }
}
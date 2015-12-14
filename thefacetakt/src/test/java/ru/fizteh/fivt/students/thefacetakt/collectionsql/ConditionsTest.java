package ru.fizteh.fivt.students.thefacetakt.collectionsql;

import org.junit.Test;

import java.util.function.Predicate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.*;
import static ru.fizteh.fivt.students.thefacetakt.collectionsql.Conditions.like;
import static ru.fizteh.fivt.students.thefacetakt.collectionsql.Conditions.mySplit;
import static ru.fizteh.fivt.students.thefacetakt.collectionsql
        .Conditions.rlike;

/**
 * Created by thefacetakt on 11.11.15.
 */
public class ConditionsTest {

    @Test
    public void testRlike() throws Exception {
        Predicate<String> x = rlike((String y) -> y, "[a-z][a-zA-Z]*End.");
        assertThat(x.test("myVariableNameEnd!"), is(true));
        assertThat(x.test("myVariableNameEnd"), is(false));
        assertThat(x.test("myVariableNameendq"), is(false));
        assertThat(x.test("MyVariableNameEndw"), is(false));
        assertThat(x.test("myVariable NamEnde"), is(false));
        assertThat(x.test(""), is(false));
        assertThat(x.test("eEndi"), is(true));
    }

    @Test
    public void testLike_1() throws Exception {
        Predicate<String> x = like((String y) -> y, ".%.");
        assertThat(x.test(".."), is(true));
        assertThat(x.test("..."), is(true));
        assertThat(x.test("a.a"), is(false));
        assertThat(x.test("alsdgh"), is(false));
        assertThat(x.test(""), is(false));
        assertThat(x.test(".aksjdhsjkd."), is(true));
    }


    @Test
    public void testLike_2() throws Exception {
        Predicate<String> x = like((String y) -> y, "*");
        assertThat(x.test("*"), is(true));
        assertThat(x.test("Stepa"), is(false));
    }

    @Test
    public void testLike_3() throws Exception {
        Predicate<String> x = like((String y) -> y, "??????");
        assertThat(x.test("abacab"), is(true));
        assertThat(x.test("abaca"), is(false));
    }

    @Test
    public void testLike_4() throws Exception {
        Predicate<String> x = like((String y) -> y, "[a-zA-Z]");
        assertThat(x.test("[a-zA-Z]"), is(true));
        assertThat(x.test("x"), is(false));
    }

    @Test
    public void testMySplit() throws Exception {
        assertThat(mySplit("abacaba", 'a'),
                contains("", "b", "c", "b", ""));
        assertThat(mySplit("abacaba", '?'), contains("abacaba"));
    }
}
package ru.fizteh.fivt.students.zakharovas.twitterstream.library;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by alexander on 04.11.15.
 */
public class ArgumentSeparatorTest {
    @Test
    public void testSeparateArguments() {
        String[] test = {"a b", "c", "ef g", "sdsd oko"};
        String[] answer = {"a", "b", "c", "ef", "g", "sdsd", "oko"};
        assertThat(ArgumentSeparator.separateArguments(test), is(equalTo(answer)));

    }
}

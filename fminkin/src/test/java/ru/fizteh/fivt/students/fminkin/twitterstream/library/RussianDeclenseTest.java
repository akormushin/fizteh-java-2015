package ru.fizteh.fivt.students.fminkin.twitterstream.library;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by Федор on 14.12.15.
 */

@RunWith(MockitoJUnitRunner.class)
public class RussianDeclenseTest extends TestCase {
    @Test
    public void testMinutesDeclension() throws Exception {
        Map<Integer, String> correct = new HashMap<>();
        correct.put(1, "минуту");
        correct.put(2, "минуты");
        correct.put(3, "минуты");
        correct.put(5, "минут");
        correct.put(9, "минут");
        correct.put(13, "минут");
        correct.put(42, "минуты");
        correct.put(67, "минут");
        correct.put(81, "минуту");
        correct.put(14, "минут");
        correct.put(12, "минут");

        for (Integer value: correct.keySet()) {
            assertThat(RussianDeclense.getMinutes(value), is(correct.get(value)));
        }

    }
    @Test
    public void testHoursDeclension() throws Exception {
        Map<Integer, String> correct = new HashMap<>();
        correct.put(1, "час");
        correct.put(2, "часа");
        correct.put(3, "часа");
        correct.put(5, "часов");
        correct.put(9, "часов");
        correct.put(13, "часов");
        correct.put(42, "часа");
        correct.put(67, "часов");
        correct.put(81, "час");
        correct.put(14, "часов");
        correct.put(12, "часов");

        for (Integer value: correct.keySet()) {
            assertThat(RussianDeclense.getHours(value), is(correct.get(value)));
        }

    }
    @Test
    public void testDaysDeclension() throws Exception {
        Map<Integer, String> correct = new HashMap<>();
        correct.put(1, "день");
        correct.put(1, "день");
        correct.put(2, "дня");
        correct.put(3, "дня");
        correct.put(5, "дней");
        correct.put(9, "дней");
        correct.put(13, "дней");
        correct.put(42, "дня");
        correct.put(67, "дней");
        correct.put(81, "день");
        correct.put(14, "дней");
        correct.put(12, "дней");
        for (Integer value : correct.keySet()) {
            assertThat(RussianDeclense.getDays(value), is(correct.get(value)));
        }
    }


    @Test
    public void testDeclensionRetweets() throws Exception {
        Map<Integer, String> correct = new HashMap<>();
        correct.put(1, "ретвит");
        correct.put(2, "ретвита");
        correct.put(3, "ретвита");
        correct.put(5, "ретвитов");
        correct.put(9, "ретвитов");
        correct.put(13, "ретвитов");
        correct.put(42, "ретвита");
        correct.put(67, "ретвитов");
        correct.put(81, "ретвит");
        correct.put(14, "ретвитов");
        correct.put(12, "ретвитов");

        for (Integer value: correct.keySet()) {
            assertThat(RussianDeclense.getRetweet(value), is(correct.get(value)));
        }
    }
}

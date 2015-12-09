package ru.fizteh.fivt.students.thefacetakt.twitterstream.library;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import ru.fizteh.fivt.students.thefacetakt.twitterstream.library.Declenser;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by thefacetakt on 06.10.15.
 */

@RunWith(MockitoJUnitRunner.class)
public class DeclenserTest extends TestCase {

    static final int DECLENSION_STEP = 100;
    static final int STEPS = 100;

    @Test
    public void testDeclensionMinutes() throws Exception {
        //query -- answer
        Map<Integer, String> answers = new HashMap<>();
        answers.put(1, "минуту");
        answers.put(2, "минуты");
        answers.put(3, "минуты");
        answers.put(5, "минут");
        answers.put(8, "минут");
        answers.put(12, "минут");
        answers.put(53, "минуты");
        answers.put(61, "минуту");
        answers.put(22, "минуты");

        for (Integer testValue: answers.keySet()) {
            for (int i = 0; i < STEPS; ++i) {
                assertThat(Declenser.minutesDeclension(
                        testValue + i * DECLENSION_STEP
                ), is(answers.get(testValue)));
            }
        }
    }

    @Test
    public void testDeclensionDays() throws Exception {
        //query -- answer
        Map<Integer, String> answers = new HashMap<>();
        answers.put(1, "день");
        answers.put(2, "дня");
        answers.put(3, "дня");
        answers.put(5, "дней");
        answers.put(8, "дней");
        answers.put(12, "дней");
        answers.put(53, "дня");
        answers.put(61, "день");
        answers.put(22, "дня");

        for (Integer testValue: answers.keySet()) {
            for (int i = 0; i < STEPS; ++i) {
                assertThat(Declenser.daysDeclension(
                        testValue + i * DECLENSION_STEP
                ), is(answers.get(testValue)));
            }
        }
    }

    @Test
    public void testDeclensionHours() throws Exception {
        //query -- answer
        Map<Integer, String> answers = new HashMap<>();
        answers.put(1, "час");
        answers.put(2, "часа");
        answers.put(3, "часа");
        answers.put(5, "часов");
        answers.put(8, "часов");
        answers.put(12, "часов");
        answers.put(53, "часа");
        answers.put(61, "час");
        answers.put(22, "часа");

        for (Integer testValue: answers.keySet()) {
            for (int i = 0; i < STEPS; ++i) {
                assertThat(Declenser.hoursDeclension(
                        testValue + i * DECLENSION_STEP
                ), is(answers.get(testValue)));
            }
        }
    }

    @Test
    public void testDeclensionRetweets() throws Exception {
        //query -- answer
        Map<Integer, String> answers = new HashMap<>();
        answers.put(1, "ретвит");
        answers.put(2, "ретвита");
        answers.put(3, "ретвита");
        answers.put(5, "ретвитов");
        answers.put(8, "ретвитов");
        answers.put(12, "ретвитов");
        answers.put(53, "ретвита");
        answers.put(61, "ретвит");
        answers.put(22, "ретвита");

        for (Integer testValue: answers.keySet()) {
            for (int i = 0; i < STEPS; ++i) {
                assertThat(Declenser.retweetDeclension(
                        testValue + i * DECLENSION_STEP
                ), is(answers.get(testValue)));
            }
        }
    }

}
package ru.fizteh.fivt.students.thefacetakt.twitterstream;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by thefacetakt on 06.10.15.
 */

@RunWith(MockitoJUnitRunner.class)
public class DeclenserTest extends TestCase {

    static final int DECLENSION_STEP = 100;
    static final int STEPS = 100;

    @Test
    public void testDeclensionMinutes() {
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
                assertTrue(Declenser.minutesDeclension(
                        testValue + i * DECLENSION_STEP
                ).equals(answers.get(testValue)));
            }
        }
    }

    @Test
    public void testDeclensionDays() {
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
                assertTrue(Declenser.daysDeclension(
                        testValue + i * DECLENSION_STEP
                ).equals(answers.get(testValue)));
            }
        }
    }

    @Test
    public void testDeclensionHours() {
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
                assertTrue(Declenser.hoursDeclension(
                        testValue + i * DECLENSION_STEP
                ).equals(answers.get(testValue)));
            }
        }
    }

    @Test
    public void testDeclensionRetweets() {
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
                assertTrue(Declenser.retweetDeclension(
                        testValue + i * DECLENSION_STEP
                ).equals(answers.get(testValue)));
            }
        }
    }

}
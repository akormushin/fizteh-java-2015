package ru.fizteh.fivt.students.okalitova.moduletests.library;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static junit.framework.Assert.assertEquals;

/**
 * Created by nimloth on 01.11.15.
 */
@RunWith(MockitoJUnitRunner.class)
public class TimeParserTest {

    @Test
    public void timeUnitsTest() {
        assertEquals("минуток", TimeParser.timeUnits(11, 0));
        assertEquals("минутки", TimeParser.timeUnits(24, 0));
        assertEquals("минутку", TimeParser.timeUnits(1, 0));
        assertEquals("часика", TimeParser.timeUnits(32, 1));
        assertEquals("часиков", TimeParser.timeUnits(5, 1));
        assertEquals("деньков", TimeParser.timeUnits(67, 2));
        assertEquals("деньков", TimeParser.timeUnits(111, 2));
    }

    @Test
    public void todayTest() {
        LocalDateTime curTime;
        LocalDateTime time;
        curTime = LocalDateTime.of(2015, 11, 1, 20, 27, 18);
        time  = LocalDateTime.of(2015, 11, 1, 20, 26, 56);
        assertEquals(true, TimeParser.today(time.toEpochSecond(ZoneOffset.UTC),
                curTime.toEpochSecond(ZoneOffset.UTC)));
        curTime = LocalDateTime.of(2014, 10, 5, 20, 27, 18);;
        time = LocalDateTime.of(2014, 10, 5, 19, 26, 54);
        assertEquals(true, TimeParser.today(time.toEpochSecond(ZoneOffset.UTC),
                curTime.toEpochSecond(ZoneOffset.UTC)));
        curTime = LocalDateTime.of(2015, 6, 2, 14, 45, 53);
        time = LocalDateTime.of(2015, 6, 1, 2, 34, 19);
        assertEquals(false, TimeParser.today(time.toEpochSecond(ZoneOffset.UTC),
                curTime.toEpochSecond(ZoneOffset.UTC)));
        curTime = LocalDateTime.of(2015, 4, 17, 21, 32, 43);
        time = LocalDateTime.of(2015, 4, 16, 3, 41, 22);
        assertEquals(false, TimeParser.today(time.toEpochSecond(ZoneOffset.UTC),
                curTime.toEpochSecond(ZoneOffset.UTC)));
        curTime = LocalDateTime.of(2015, 4, 17, 21, 32, 43);
        time = LocalDateTime.of(2015, 4, 15, 3, 41, 22);
        assertEquals(false, TimeParser.today(time.toEpochSecond(ZoneOffset.UTC),
                curTime.toEpochSecond(ZoneOffset.UTC)));
        curTime = LocalDateTime.of(2015, 4, 17, 21, 32, 43);
        time = LocalDateTime.of(2015, 2, 15, 3, 41, 22);
        assertEquals(false, TimeParser.today(time.toEpochSecond(ZoneOffset.UTC),
                curTime.toEpochSecond(ZoneOffset.UTC)));
    }

    @Test
    public void yesterdayTest() {
        LocalDateTime curTime;
        LocalDateTime time;
        curTime = LocalDateTime.of(2015, 11, 1, 20, 27, 18);
        time  = LocalDateTime.of(2015, 11, 1, 20, 26, 56);
        assertEquals(false, TimeParser.yesterday(time.toEpochSecond(ZoneOffset.UTC),
                curTime.toEpochSecond(ZoneOffset.UTC)));
        curTime = LocalDateTime.of(2014, 10, 5, 20, 27, 18);;
        time = LocalDateTime.of(2014, 10, 5, 19, 26, 54);
        assertEquals(false, TimeParser.yesterday(time.toEpochSecond(ZoneOffset.UTC),
                curTime.toEpochSecond(ZoneOffset.UTC)));
        curTime = LocalDateTime.of(2015, 6, 2, 14, 45, 53);
        time = LocalDateTime.of(2015, 6, 1, 2, 34, 19);
        assertEquals(true, TimeParser.yesterday(time.toEpochSecond(ZoneOffset.UTC),
                curTime.toEpochSecond(ZoneOffset.UTC)));
        curTime = LocalDateTime.of(2015, 4, 17, 21, 32, 43);
        time = LocalDateTime.of(2015, 4, 16, 3, 41, 22);
        assertEquals(true, TimeParser.yesterday(time.toEpochSecond(ZoneOffset.UTC),
                curTime.toEpochSecond(ZoneOffset.UTC)));
        curTime = LocalDateTime.of(2015, 4, 17, 21, 32, 43);
        time = LocalDateTime.of(2015, 4, 15, 3, 41, 22);
        assertEquals(false, TimeParser.yesterday(time.toEpochSecond(ZoneOffset.UTC),
                curTime.toEpochSecond(ZoneOffset.UTC)));
        curTime = LocalDateTime.of(2015, 4, 17, 21, 32, 43);
        time = LocalDateTime.of(2015, 2, 15, 3, 41, 22);
        assertEquals(false, TimeParser.yesterday(time.toEpochSecond(ZoneOffset.UTC),
                curTime.toEpochSecond(ZoneOffset.UTC)));
    }

    @Test
    public void getTimeTest() {
        LocalDateTime curTime;
        LocalDateTime time;
        curTime = LocalDateTime.of(2015, 11, 1, 20, 27, 18);
        time  = LocalDateTime.of(2015, 11, 1, 20, 26, 56);
        assertEquals("[только что]", TimeParser.getTime(time, curTime));
        curTime = LocalDateTime.of(2014, 10, 5, 20, 27, 18);;
        time = LocalDateTime.of(2014, 10, 5, 19, 26, 54);
        assertEquals("[1 часик назад]", TimeParser.getTime(time, curTime));
        curTime = LocalDateTime.of(2015, 6, 2, 14, 45, 53);
        time = LocalDateTime.of(2015, 6, 1, 2, 34, 19);
        assertEquals("[вчера]", TimeParser.getTime(time, curTime));
        curTime = LocalDateTime.of(2015, 4, 17, 21, 32, 43);
        time = LocalDateTime.of(2015, 4, 16, 3, 41, 22);
        assertEquals("[вчера]", TimeParser.getTime(time, curTime));
        curTime = LocalDateTime.of(2015, 4, 17, 21, 32, 43);
        time = LocalDateTime.of(2015, 4, 15, 3, 41, 22);
        assertEquals("[2 денька назад]", TimeParser.getTime(time, curTime));
        curTime = LocalDateTime.of(2015, 4, 17, 21, 32, 43);
        time = LocalDateTime.of(2015, 2, 15, 3, 41, 22);
        assertEquals("[61 денек назад]", TimeParser.getTime(time, curTime));
    }
}

package ru.fizteh.fivt.students.oshch.moduletests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import ru.fizteh.fivt.students.oshch.moduletests.library.TimeFormatter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TimeFormatterTest {

    @Test
    public void timeUnitsTest() {
        assertEquals("минут", TimeFormatter.myTime(11, TimeFormatter.MINUTES));
        assertEquals("минуты", TimeFormatter.myTime(24, TimeFormatter.MINUTES));
        assertEquals("минуту", TimeFormatter.myTime(1, TimeFormatter.MINUTES));
        assertEquals("часа", TimeFormatter.myTime(32, TimeFormatter.HOURS));
        assertEquals("часов", TimeFormatter.myTime(5, TimeFormatter.HOURS));
        assertEquals("дней", TimeFormatter.myTime(67, TimeFormatter.DAYS));
        assertEquals("дней", TimeFormatter.myTime(111, TimeFormatter.DAYS));
    }

    @Test
    public void todayTest() {
        LocalDateTime curTime;
        LocalDateTime time;
        curTime = LocalDateTime.of(2015, 11, 1, 20, 27, 18);
        time  = LocalDateTime.of(2015, 11, 1, 20, 26, 56);
        assertEquals(true, TimeFormatter.today(time.toEpochSecond(ZoneOffset.UTC),
                curTime.toEpochSecond(ZoneOffset.UTC)));
        curTime = LocalDateTime.of(2014, 10, 5, 20, 27, 18);;
        time = LocalDateTime.of(2014, 10, 5, 19, 26, 54);
        assertEquals(true, TimeFormatter.today(time.toEpochSecond(ZoneOffset.UTC),
                curTime.toEpochSecond(ZoneOffset.UTC)));
        curTime = LocalDateTime.of(2015, 6, 2, 14, 45, 53);
        time = LocalDateTime.of(2015, 6, 1, 2, 34, 19);
        assertEquals(false, TimeFormatter.today(time.toEpochSecond(ZoneOffset.UTC),
                curTime.toEpochSecond(ZoneOffset.UTC)));
        curTime = LocalDateTime.of(2015, 4, 17, 21, 32, 43);
        time = LocalDateTime.of(2015, 4, 16, 3, 41, 22);
        assertEquals(false, TimeFormatter.today(time.toEpochSecond(ZoneOffset.UTC),
                curTime.toEpochSecond(ZoneOffset.UTC)));
        curTime = LocalDateTime.of(2015, 4, 17, 21, 32, 43);
        time = LocalDateTime.of(2015, 4, 15, 3, 41, 22);
        assertEquals(false, TimeFormatter.today(time.toEpochSecond(ZoneOffset.UTC),
                curTime.toEpochSecond(ZoneOffset.UTC)));
        curTime = LocalDateTime.of(2015, 4, 17, 21, 32, 43);
        time = LocalDateTime.of(2015, 2, 15, 3, 41, 22);
        assertEquals(false, TimeFormatter.today(time.toEpochSecond(ZoneOffset.UTC),
                curTime.toEpochSecond(ZoneOffset.UTC)));
    }

    @Test
    public void yesterdayTest() {
        LocalDateTime curTime;
        LocalDateTime time;
        curTime = LocalDateTime.of(2015, 11, 1, 20, 27, 18);
        time  = LocalDateTime.of(2015, 11, 1, 20, 26, 56);
        assertEquals(false, TimeFormatter.yesterday(time.toEpochSecond(ZoneOffset.UTC),
                curTime.toEpochSecond(ZoneOffset.UTC)));
        curTime = LocalDateTime.of(2014, 10, 5, 20, 27, 18);;
        time = LocalDateTime.of(2014, 10, 5, 19, 26, 54);
        assertEquals(false, TimeFormatter.yesterday(time.toEpochSecond(ZoneOffset.UTC),
                curTime.toEpochSecond(ZoneOffset.UTC)));
        curTime = LocalDateTime.of(2015, 6, 2, 14, 45, 53);
        time = LocalDateTime.of(2015, 6, 1, 2, 34, 19);
        assertEquals(true, TimeFormatter.yesterday(time.toEpochSecond(ZoneOffset.UTC),
                curTime.toEpochSecond(ZoneOffset.UTC)));
        curTime = LocalDateTime.of(2015, 4, 17, 21, 32, 43);
        time = LocalDateTime.of(2015, 4, 16, 3, 41, 22);
        assertEquals(true, TimeFormatter.yesterday(time.toEpochSecond(ZoneOffset.UTC),
                curTime.toEpochSecond(ZoneOffset.UTC)));
        curTime = LocalDateTime.of(2015, 4, 17, 21, 32, 43);
        time = LocalDateTime.of(2015, 4, 15, 3, 41, 22);
        assertEquals(false, TimeFormatter.yesterday(time.toEpochSecond(ZoneOffset.UTC),
                curTime.toEpochSecond(ZoneOffset.UTC)));
        curTime = LocalDateTime.of(2015, 4, 17, 21, 32, 43);
        time = LocalDateTime.of(2015, 2, 15, 3, 41, 22);
        assertEquals(false, TimeFormatter.yesterday(time.toEpochSecond(ZoneOffset.UTC),
                curTime.toEpochSecond(ZoneOffset.UTC)));
    }

    @Test
    public void getTimeTest() {
        LocalDateTime curTime;
        LocalDateTime time;
        curTime = LocalDateTime.of(2015, 11, 1, 20, 27, 18);
        time  = LocalDateTime.of(2015, 11, 1, 20, 26, 56);
        assertEquals("[только что]", TimeFormatter.getTime(time, curTime));
        curTime = LocalDateTime.of(2015, 11, 1, 20, 30, 18);
        time  = LocalDateTime.of(2015, 11, 1, 20, 26, 56);
        assertEquals("[3 минуты назад]", TimeFormatter.getTime(time, curTime));
        curTime = LocalDateTime.of(2014, 10, 5, 20, 27, 18);
        time = LocalDateTime.of(2014, 10, 5, 19, 26, 54);
        assertEquals("[1 час назад]", TimeFormatter.getTime(time, curTime));
        curTime = LocalDateTime.of(2015, 6, 2, 14, 45, 53);
        time = LocalDateTime.of(2015, 6, 1, 2, 34, 19);
        assertEquals("[вчера]", TimeFormatter.getTime(time, curTime));
        curTime = LocalDateTime.of(2015, 4, 17, 21, 32, 43);
        time = LocalDateTime.of(2015, 4, 16, 3, 41, 22);
        assertEquals("[вчера]", TimeFormatter.getTime(time, curTime));
        curTime = LocalDateTime.of(2015, 4, 17, 21, 32, 43);
        time = LocalDateTime.of(2015, 4, 15, 3, 41, 22);
        assertEquals("[2 дня назад]", TimeFormatter.getTime(time, curTime));
        curTime = LocalDateTime.of(2015, 4, 17, 21, 32, 43);
        time = LocalDateTime.of(2015, 2, 15, 3, 41, 22);
        assertEquals("[61 день назад]", TimeFormatter.getTime(time, curTime));
    }
}
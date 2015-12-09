package ru.fizteh.fivt.students.thefacetakt.twitterstream.library;

import org.junit.Test;
import ru.fizteh.fivt.students.thefacetakt.twitterstream.library.TimeFormatter;

import java.time.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by thefacetakt on 07.10.15.
 */
public class TimeFormatterTest {

    static final ZoneId zone = ZoneId.of("Europe/Moscow");
    static long date(int year, int month, int day,
                     int hours, int minutes, int seconds) {
        return ZonedDateTime.of(year, month, day, hours, minutes, seconds, 0,
                zone)
                .toInstant().toEpochMilli();
    }

    @Test
    public void testZonedFormatTime() throws Exception {

        assertThat(TimeFormatter.zonedFormatTime(
                date(1997, Month.FEBRUARY.getValue(), 18, 23, 59, 59),
                date(1997, Month.FEBRUARY.getValue(), 18, 0, 0, 12),
                zone), is("23 часа назад"));
        assertThat(TimeFormatter.zonedFormatTime(
                date(1997, Month.FEBRUARY.getValue(), 18, 12, 20, 0),
                date(1997, Month.FEBRUARY.getValue(), 18, 12, 20, 1),
                zone), is("Только что"));
        assertThat(TimeFormatter.zonedFormatTime(
                date(2012, Month.MARCH.getValue(), 1, 12, 20, 0),
                date(2012, Month.FEBRUARY.getValue(), 28, 9, 18, 1),
                zone), is("2 дня назад"));

        assertThat(TimeFormatter.zonedFormatTime(
                date(2013, Month.MARCH.getValue(), 1, 12, 20, 0),
                date(2013, Month.FEBRUARY.getValue(), 28, 9, 18, 1),
                zone), is("вчера"));

        assertThat(TimeFormatter.zonedFormatTime(
                date(1997, Month.FEBRUARY.getValue(), 18, 12, 20, 1),
                date(1997, Month.FEBRUARY.getValue(), 18, 12, 18, 0),
                zone), is("2 минуты назад"));
    }


}
package ru.fizteh.fivt.students.fminkin.twitterstream.library;
import org.junit.Test;
import java.time.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by Федор on 14.12.2015.
 */
public class TimeAlignTest {

    static final ZoneId ZONE = ZoneId.of("Europe/Moscow");
    static long date(int year, int month, int day, int hours, int minutes, int seconds) {
        return ZonedDateTime.of(year, month, day, hours, minutes, seconds, 0, ZONE).toInstant().toEpochMilli();
    }

    @Test
    public void testZonedFormatTime() throws Exception {
        assertThat(TimeAlign.formatZoneTime(
                date(2000, Month.MAY.getValue(), 21, 23, 59, 59),
                date(2000, Month.MAY.getValue(), 21, 0, 0, 12),
                ZONE), is("23 часа назад"));
        assertThat(TimeAlign.formatZoneTime(
                date(2001, Month.DECEMBER.getValue(), 8, 12, 20, 0),
                date(2001, Month.DECEMBER.getValue(), 8, 12, 20, 1),
                ZONE), is("Только что"));
        assertThat(TimeAlign.formatZoneTime(
                date(2014, Month.MARCH.getValue(), 7, 11, 21, 0),
                date(2014, Month.MARCH.getValue(), 5, 8, 19, 1),
                ZONE), is("2 дня назад"));

        assertThat(TimeAlign.formatZoneTime(
                date(2013, Month.MARCH.getValue(), 1, 12, 20, 0),
                date(2013, Month.FEBRUARY.getValue(), 28, 9, 18, 1),
                ZONE), is("вчера"));

        assertThat(TimeAlign.formatZoneTime(
                date(1998, Month.MARCH.getValue(), 7, 2, 20, 1),
                date(1998, Month.MARCH.getValue(), 7, 2, 18, 0),
                ZONE), is("2 минуты назад"));
        assertThat(TimeAlign.formatZoneTime(
                date(1997, Month.FEBRUARY.getValue(), 18, 23, 59, 59),
                date(1997, Month.FEBRUARY.getValue(), 18, 0, 0, 12),
                ZONE), is("23 часа назад"));
        assertThat(TimeAlign.formatZoneTime(
                date(1997, Month.FEBRUARY.getValue(), 18, 12, 20, 0),
                date(1997, Month.FEBRUARY.getValue(), 18, 12, 20, 1),
                ZONE), is("Только что"));
        assertThat(TimeAlign.formatZoneTime(
                date(2012, Month.MARCH.getValue(), 1, 12, 20, 0),
                date(2012, Month.FEBRUARY.getValue(), 28, 9, 18, 1),
                ZONE), is("2 дня назад"));

        assertThat(TimeAlign.formatZoneTime(
                date(2013, Month.MARCH.getValue(), 1, 12, 20, 0),
                date(2013, Month.FEBRUARY.getValue(), 28, 9, 18, 1),
                ZONE), is("вчера"));

        assertThat(TimeAlign.formatZoneTime(
                date(1997, Month.FEBRUARY.getValue(), 18, 12, 20, 1),
                date(1997, Month.FEBRUARY.getValue(), 18, 12, 18, 0),
                ZONE), is("2 минуты назад"));
    }
}

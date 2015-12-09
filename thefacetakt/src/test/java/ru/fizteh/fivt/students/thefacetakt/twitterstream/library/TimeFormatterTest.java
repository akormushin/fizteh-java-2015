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

    @Test
    public void testZonedFormatTime() throws Exception {
        ZoneId zone = ZoneId.of("Europe/Moscow");
        assertThat(TimeFormatter.zonedFormatTime(
                LocalDate.of(1997, Month.FEBRUARY, 18)
                        .atTime(23, 59, 59).atZone(zone)
                        .toInstant().toEpochMilli(),
                LocalDate.of(1997, Month.FEBRUARY, 18)
                        .atTime(0, 0, 12).atZone(zone)
                        .toInstant().toEpochMilli(),
                zone), is("23 часа назад"));
        assertThat(TimeFormatter.zonedFormatTime(
                LocalDate.of(1997, Month.FEBRUARY, 18)
                        .atTime(12, 20, 00).atZone(zone)
                        .toInstant().toEpochMilli(),
                LocalDate.of(1997, Month.FEBRUARY, 18)
                        .atTime(12, 18, 01).atZone(zone)
                        .toInstant().toEpochMilli(),
                zone), is("Только что"));
        assertThat(TimeFormatter.zonedFormatTime(
                LocalDate.of(2012, Month.MARCH, 01)
                        .atTime(12, 20, 00).atZone(zone)
                        .toInstant().toEpochMilli(),
                LocalDate.of(2012, Month.FEBRUARY, 28)
                        .atTime(9, 18, 01).atZone(zone)
                        .toInstant().toEpochMilli(),
                zone), is("2 дня назад"));

        assertThat(TimeFormatter.zonedFormatTime(
                LocalDate.of(2013, Month.MARCH, 01)
                        .atTime(12, 20, 00).atZone(zone)
                        .toInstant().toEpochMilli(),
                LocalDate.of(2013, Month.FEBRUARY, 28)
                        .atTime(9, 18, 01).atZone(zone)
                        .toInstant().toEpochMilli(),
                zone), is("вчера"));

        assertThat(TimeFormatter.zonedFormatTime(
                LocalDate.of(1997, Month.FEBRUARY, 18)
                        .atTime(12, 20, 01).atZone(zone)
                        .toInstant().toEpochMilli(),
                LocalDate.of(1997, Month.FEBRUARY, 18)
                        .atTime(12, 18, 00).atZone(zone)
                        .toInstant().toEpochMilli(),
                zone), is("2 минуты назад"));
    }


}
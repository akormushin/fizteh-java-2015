package ru.fizteh.fivt.students.thefacetakt.twitterstream;

import org.junit.Test;

import java.time.*;
import java.util.Date;
import java.util.TimeZone;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by thefacetakt on 07.10.15.
 */
public class TimeFormatterTest {

    @Test
    public void testFormatTime() throws Exception {
        assertThat(TimeFormatter.formatTime(
                LocalDate.of(1997, Month.FEBRUARY, 18)
                        .atTime(23, 59, 59).atZone(ZoneId.systemDefault())
                        .toInstant().toEpochMilli(),
                LocalDate.of(1997, Month.FEBRUARY, 18)
                        .atTime(0, 0, 12).atZone(ZoneId.systemDefault())
                        .toInstant().toEpochMilli()), is("23 часа назад"));
        assertThat(TimeFormatter.formatTime(
                LocalDate.of(1997, Month.FEBRUARY, 18)
                        .atTime(12, 20, 00).atZone(ZoneId.systemDefault())
                        .toInstant().toEpochMilli(),
                LocalDate.of(1997, Month.FEBRUARY, 18)
                        .atTime(12, 18, 01).atZone(ZoneId.systemDefault())
                        .toInstant().toEpochMilli()), is("Только что"));
        assertThat(TimeFormatter.formatTime(
                LocalDate.of(2012, Month.MARCH, 01)
                        .atTime(12, 20, 00).atZone(ZoneId.systemDefault())
                        .toInstant().toEpochMilli(),
                LocalDate.of(2012, Month.FEBRUARY, 28)
                        .atTime(9, 18, 01).atZone(ZoneId.systemDefault())
                        .toInstant().toEpochMilli()), is("2 дня назад"));

        assertThat(TimeFormatter.formatTime(
                LocalDate.of(2013, Month.MARCH, 01)
                        .atTime(12, 20, 00).atZone(ZoneId.systemDefault())
                        .toInstant().toEpochMilli(),
                LocalDate.of(2013, Month.FEBRUARY, 28)
                        .atTime(9, 18, 01).atZone(ZoneId.systemDefault())
                        .toInstant().toEpochMilli()), is("вчера"));

        assertThat(TimeFormatter.formatTime(
                LocalDate.of(1997, Month.FEBRUARY, 18)
                        .atTime(12, 20, 01).atZone(ZoneId.systemDefault())
                        .toInstant().toEpochMilli(),
                LocalDate.of(1997, Month.FEBRUARY, 18)
                        .atTime(12, 18, 00).atZone(ZoneId.systemDefault())
                        .toInstant().toEpochMilli()), is("2 минуты назад"));
    }


}
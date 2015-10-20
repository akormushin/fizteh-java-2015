package ru.fizteh.fivt.students.andrewgark.TwitterStream;

import org.junit.Assert;
import org.junit.Test;
import twitter4j.Status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TSWordsFormTest {
    @Test
    public void testGetTimeForm() throws Exception {
        LocalDateTime localDateTime = LocalDateTime.of(2015, 10, 11, 1, 0, 0);
        Status status = mock(Status.class);
        when(status.getCreatedAt()).thenReturn(new Date(115, 9, 10, 23, 59, 59));
        Assert.assertEquals("[Вчера]", TSWordsForm.getTimeForm(status, localDateTime));
    }

    @Test
    public void testGetRetweetsForm() throws Exception {
        Assert.assertEquals(" (5 ретвитов)", TSWordsForm.getRetweetsForm(5));
        Assert.assertEquals("", TSWordsForm.getRetweetsForm(0));
    }

    @Test
    public void testGetTimeBetweenForm() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        Assert.assertEquals("Только что", TSWordsForm.getTimeBetweenForm(
                LocalDateTime.parse("2015-10-01T12:00:00", formatter),
                LocalDateTime.parse("2015-10-01T12:00:01", formatter)
        ));
        Assert.assertEquals("Только что", TSWordsForm.getTimeBetweenForm(
                LocalDateTime.parse("2015-10-01T11:59:50", formatter),
                LocalDateTime.parse("2015-10-01T12:01:45", formatter)
        ));
        Assert.assertEquals("2 минуты назад", TSWordsForm.getTimeBetweenForm(
                LocalDateTime.parse("2015-10-01T12:00:45", formatter),
                LocalDateTime.parse("2015-10-01T12:02:55", formatter)
        ));
        Assert.assertEquals("59 минут назад", TSWordsForm.getTimeBetweenForm(
                LocalDateTime.parse("2015-10-01T22:30:00", formatter),
                LocalDateTime.parse("2015-10-01T23:29:56", formatter)
        ));
        Assert.assertEquals("1 час назад", TSWordsForm.getTimeBetweenForm(
                LocalDateTime.parse("2015-10-01T22:30:00", formatter),
                LocalDateTime.parse("2015-10-01T23:31:05", formatter)
        ));
        Assert.assertEquals("23 часа назад", TSWordsForm.getTimeBetweenForm(
                LocalDateTime.parse("2015-10-01T00:30:00", formatter),
                LocalDateTime.parse("2015-10-01T23:40:00", formatter)
        ));
        Assert.assertEquals("Вчера", TSWordsForm.getTimeBetweenForm(
                LocalDateTime.parse("2015-10-01T23:59:59", formatter),
                LocalDateTime.parse("2015-10-02T00:00:01", formatter)
        ));
        Assert.assertEquals("Вчера", TSWordsForm.getTimeBetweenForm(
                LocalDateTime.parse("2015-02-28T12:00:00", formatter),
                LocalDateTime.parse("2015-03-01T12:00:00", formatter)
        ));
        Assert.assertEquals("2 дня назад", TSWordsForm.getTimeBetweenForm(
                LocalDateTime.parse("2012-02-28T12:00:00", formatter),
                LocalDateTime.parse("2012-03-01T12:00:00", formatter)
        ));
        Assert.assertEquals("7060 дней назад", TSWordsForm.getTimeBetweenForm(
                LocalDateTime.parse("1997-01-01T11:00:00", formatter),
                LocalDateTime.parse("2016-05-01T13:00:00", formatter)
        ));
    }

    @Test
    public void testGetForm() throws Exception {
        final String[] DAY_FORMS = {"день", "дня", "дней"};

        Assert.assertEquals("0 дней", TSWordsForm.getForm(0, DAY_FORMS));
        Assert.assertEquals("1 день", TSWordsForm.getForm(1, DAY_FORMS));
        Assert.assertEquals("2 дня", TSWordsForm.getForm(2, DAY_FORMS));
        Assert.assertEquals("3 дня", TSWordsForm.getForm(3, DAY_FORMS));
        Assert.assertEquals("4 дня", TSWordsForm.getForm(4, DAY_FORMS));
        Assert.assertEquals("5 дней", TSWordsForm.getForm(5, DAY_FORMS));
        Assert.assertEquals("6 дней", TSWordsForm.getForm(6, DAY_FORMS));
        Assert.assertEquals("10 дней", TSWordsForm.getForm(10, DAY_FORMS));
        Assert.assertEquals("11 дней", TSWordsForm.getForm(11, DAY_FORMS));
        Assert.assertEquals("12 дней", TSWordsForm.getForm(12, DAY_FORMS));
        Assert.assertEquals("13 дней", TSWordsForm.getForm(13, DAY_FORMS));
        Assert.assertEquals("14 дней", TSWordsForm.getForm(14, DAY_FORMS));
        Assert.assertEquals("15 дней", TSWordsForm.getForm(15, DAY_FORMS));
        Assert.assertEquals("20 дней", TSWordsForm.getForm(20, DAY_FORMS));
        Assert.assertEquals("21 день", TSWordsForm.getForm(21, DAY_FORMS));
        Assert.assertEquals("32 дня", TSWordsForm.getForm(32, DAY_FORMS));
        Assert.assertEquals("57 дней", TSWordsForm.getForm(57, DAY_FORMS));
        Assert.assertEquals("91 день", TSWordsForm.getForm(91, DAY_FORMS));
        Assert.assertEquals("101 день", TSWordsForm.getForm(101, DAY_FORMS));
        Assert.assertEquals("103 дня", TSWordsForm.getForm(103, DAY_FORMS));
        Assert.assertEquals("111 дней", TSWordsForm.getForm(111, DAY_FORMS));
        Assert.assertEquals("112 дней", TSWordsForm.getForm(112, DAY_FORMS));
        Assert.assertEquals("122 дня", TSWordsForm.getForm(122, DAY_FORMS));
    }
}
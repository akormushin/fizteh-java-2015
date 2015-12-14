package ru.fizteh.fivt.students.w4r10ck1337.moduletests;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import ru.fizteh.fivt.students.w4r10ck1337.moduletests.library.NiceTime;

@RunWith(JUnit4.class)
public class NiceTimeTest {
    @Test
    public void testDiff() {
        long current = new Date(2015, 11, 9, 18, 50, 0).getTime(); //09/11/15 18.50
        Assert.assertEquals("Только что", NiceTime.diff(new Date(2015, 11, 9, 18, 48, 01), current));
        Assert.assertEquals("2 минуты назад", NiceTime.diff(new Date(2015, 11, 9, 18, 48, 00), current));
        Assert.assertEquals("59 минут назад", NiceTime.diff(new Date(2015, 11, 9, 17, 50, 01), current));
        Assert.assertEquals("1 час назад", NiceTime.diff(new Date(2015, 11, 9, 17, 50, 00), current));
        Assert.assertEquals("12 часов назад", NiceTime.diff(new Date(2015, 11, 9, 6, 50, 00), current));
        Assert.assertEquals("18 часов назад", NiceTime.diff(new Date(2015, 11, 9, 0, 0, 00), current));
        Assert.assertEquals("Вчера", NiceTime.diff(new Date(2015, 11, 8, 23, 59, 59), current));
        Assert.assertEquals("Вчера", NiceTime.diff(new Date(2015, 11, 8, 0, 0, 0), current));
        Assert.assertEquals("2 дня назад", NiceTime.diff(new Date(2015, 11, 7, 23, 59, 59), current));
    }

    @Test
    public void testForms() {
        final String[] forms = {"дней", "день", "дня"};

        Assert.assertEquals("1 день назад", NiceTime.timeToString(1, forms));
        Assert.assertEquals("2 дня назад", NiceTime.timeToString(2, forms));
        Assert.assertEquals("3 дня назад", NiceTime.timeToString(3, forms));
        Assert.assertEquals("4 дня назад", NiceTime.timeToString(4, forms));
        Assert.assertEquals("5 дней назад", NiceTime.timeToString(5, forms));
        Assert.assertEquals("6 дней назад", NiceTime.timeToString(6, forms));
        Assert.assertEquals("10 дней назад", NiceTime.timeToString(10, forms));
        Assert.assertEquals("11 дней назад", NiceTime.timeToString(11, forms));
        Assert.assertEquals("12 дней назад", NiceTime.timeToString(12, forms));
        Assert.assertEquals("13 дней назад", NiceTime.timeToString(13, forms));
        Assert.assertEquals("14 дней назад", NiceTime.timeToString(14, forms));
        Assert.assertEquals("15 дней назад", NiceTime.timeToString(15, forms));
        Assert.assertEquals("20 дней назад", NiceTime.timeToString(20, forms));
        Assert.assertEquals("101 день назад", NiceTime.timeToString(101, forms));
        Assert.assertEquals("111 дней назад", NiceTime.timeToString(111, forms));
    }
}

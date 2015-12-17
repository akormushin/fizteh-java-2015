package ru.fizteh.fivt.students.nmakeenkov.twitterstream.library;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.io.IOException;
import java.util.Date;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.*;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.fizteh.fivt.students.nmakeenkov.twitterstream.library.Utils;

import javax.rmi.CORBA.Util;

public class UtilsTest {
    private static Utils utils = new Utils();

    @Test
    public void testGetTimeDifference() {
        long current = 1000 * 24 * 60 * 60 * 1000;
        Assert.assertEquals("[только что]", Utils.getTimeDifference(current
                - Utils.JUST_NOW * Utils.MILLIS_PER_SECOND, current));
        Assert.assertEquals("[2 минуты назад]", Utils.getTimeDifference(current
                - (Utils.JUST_NOW + 1) * Utils.MILLIS_PER_SECOND, current));
        Assert.assertEquals("[20 минут назад]", Utils.getTimeDifference(current
                - (20 * Utils.SECS_PER_MIN) * Utils.MILLIS_PER_SECOND, current));
        Assert.assertEquals("[21 минуту назад]", Utils.getTimeDifference(current
                - (21 * Utils.SECS_PER_MIN) * Utils.MILLIS_PER_SECOND, current));
        Assert.assertEquals("[59 минут назад]", Utils.getTimeDifference(current
                - (59 * Utils.SECS_PER_MIN) * Utils.MILLIS_PER_SECOND, current));
        Assert.assertEquals("[1 час назад]", Utils.getTimeDifference(current
                - (Utils.MINS_PER_HOUR * Utils.SECS_PER_MIN) * Utils.MILLIS_PER_SECOND, current));
        Assert.assertEquals("[2 часа назад]", Utils.getTimeDifference(current
                - (2 * Utils.MINS_PER_HOUR * Utils.SECS_PER_MIN + 200)
                * Utils.MILLIS_PER_SECOND, current));
        Assert.assertEquals("[15 часов назад]", Utils.getTimeDifference(current
                - (15 * Utils.MINS_PER_HOUR * Utils.SECS_PER_MIN)
                * Utils.MILLIS_PER_SECOND, current));
        Assert.assertEquals("[вчера]", Utils.getTimeDifference(current
                - (25 * Utils.MINS_PER_HOUR * Utils.SECS_PER_MIN)
                * Utils.MILLIS_PER_SECOND, current));
        Assert.assertEquals("[1 день назад]", Utils.getTimeDifference(current
                - (100 * Utils.MINS_PER_HOUR * Utils.SECS_PER_MIN)
                * Utils.MILLIS_PER_SECOND, current));
        Assert.assertEquals("[2 дня назад]", Utils.getTimeDifference(current
                - (120 * Utils.MINS_PER_HOUR * Utils.SECS_PER_MIN)
                * Utils.MILLIS_PER_SECOND, current));
        Assert.assertEquals("[20 дней назад]", Utils.getTimeDifference(current
                - (1200 * Utils.MINS_PER_HOUR * Utils.SECS_PER_MIN)
                * Utils.MILLIS_PER_SECOND, current));
    }

    @Test
    public void testGetDistance() {
        assertEquals(0, Utils.getDistance(1, 2, 1, 2), 1e-5);
        assertEquals(5552.46178, Utils.getDistance(1, 2, 2, 1), 1e-5);
    }

    @Test
    public void testMySleep() throws InterruptedException {
        Assert.assertEquals(true, Utils.mySleep(1));
    }
}
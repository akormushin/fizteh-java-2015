package ru.fizteh.fivt.students.zerts.TwitterStream;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by User on 20.09.2015.
 */

public class TimeParser {
    static final int MILSEC_IN_DAY = 1000 * 3600 * 24, MILLS_PER_SEC = 1000,
            SEC_IN_DAY = 24 * 3600, SEC_IN_HOUR = 3600, SEC_IN_MIN = 60;
    static final int SEC_MODE = 0, MIN_MODE = 1, HOUR_MODE = 2, DAY_MODE = 3,
            RT_MODE = 4;
    static final int TEN_MOD = 10, FIVE = 5, HUNDRED_MOD = 100, TWENTY = 20;
    public static void rightWordPrinting(long goneTime, long mode) {
        System.out.print(goneTime + " ");
        if (goneTime % TEN_MOD >= FIVE || goneTime % TEN_MOD == 0
                || (goneTime % HUNDRED_MOD > TEN_MOD
                && goneTime % HUNDRED_MOD < TWENTY)) {
            if (mode == SEC_MODE) {
                System.out.print("секунд");
            }
            if (mode == MIN_MODE) {
                System.out.print("минут");
            }
            if (mode == HOUR_MODE) {
                System.out.print("часов");
            }
            if (mode == DAY_MODE) {
                System.out.print("дней");
            }
            if (mode == RT_MODE) {
                System.out.print("ретвитов");
            }
        } else if (goneTime % TEN_MOD == 1) {
            if (mode == SEC_MODE) {
                System.out.print("секунда");
            }
            if (mode == MIN_MODE) {
                System.out.print("минута");
            }
            if (mode == HOUR_MODE) {
                System.out.print("час");
            }
            if (mode == DAY_MODE) {
                System.out.print("день");
            }
            if (mode == RT_MODE) {
                System.out.print("ретвит");
            }
        } else {
            if (mode == SEC_MODE) {
                System.out.print("секунды");
            }
            if (mode == MIN_MODE) {
                System.out.print("минуты");
            }
            if (mode == HOUR_MODE) {
                System.out.print("часа");
            }
            if (mode == DAY_MODE) {
                System.out.print("дня");
            }
            if (mode == RT_MODE) {
                System.out.print("ретвита");
            }
        }
        if (mode < RT_MODE) {
            System.out.print(" назад ");
        }
    }
    public static void printGoneDate(Date givenDate) {
        Date date = new Date();
        Calendar c = new GregorianCalendar();
        c.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        Date todayDate = c.getTime();
        long goneTime = (date.getTime() - givenDate.getTime()) / MILLS_PER_SEC,
                todayTime = todayDate.getTime();
        //System.out.print("Время: " + tweetTime + " " + currTime + "   ");
        if (goneTime <= 2) {
            System.out.print("Только что ");
            return;
        }
        if (goneTime < SEC_IN_MIN) {
            rightWordPrinting(goneTime, SEC_MODE);
            return;
        }
        if (goneTime < SEC_IN_HOUR) {
            rightWordPrinting(goneTime / SEC_IN_MIN, MIN_MODE);
            return;
        }
        if (goneTime < SEC_IN_DAY && givenDate.getTime() >= todayTime) {
            rightWordPrinting(goneTime / SEC_IN_HOUR, HOUR_MODE);
            return;
        }
        if (givenDate.getTime() < todayTime
                && givenDate.getTime() >= todayTime - MILSEC_IN_DAY) {
            System.out.print("Вчера ");
            return;
        }
        rightWordPrinting((todayTime - givenDate.getTime()) / MILSEC_IN_DAY,
                DAY_MODE);
    }
}

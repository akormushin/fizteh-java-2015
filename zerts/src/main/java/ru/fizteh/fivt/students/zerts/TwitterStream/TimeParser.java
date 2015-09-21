package ru.fizteh.fivt.students.zerts.TwitterStream;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by User on 20.09.2015.
 */

public class TimeParser {
    public static void rightWordPrinting(long goneTime, long mode)
    {
        System.out.print(goneTime + " ");
        if (goneTime % 10 >= 5 || goneTime % 10 == 0 || (goneTime % 100 > 10 && goneTime % 100 < 20)) {
            if (mode == 0)
                System.out.print("секунд");
            if (mode == 1)
                System.out.print("минут");
            if (mode == 2)
                System.out.print("часов");
            if (mode == 3)
                System.out.print("дней");
            if (mode == 4)
                System.out.print("ретвитов");
        }
        else if(goneTime % 10 == 1) {
            if (mode == 0)
                System.out.print("секунда");
            if (mode == 1)
                System.out.print("минута");
            if (mode == 2)
                System.out.print("час");
            if (mode == 3)
                System.out.print("день");
            if (mode == 4)
                System.out.print("ретвит");
        }
        else
        {
            if (mode == 0)
                System.out.print("секунды");
            if (mode == 1)
                System.out.print("минуты");
            if (mode == 2)
                System.out.print("часа");
            if (mode == 3)
                System.out.print("дня");
            if (mode == 4)
                System.out.print("ретвита");
        }
        if (mode < 4)
            System.out.print(" назад ");
    }
    public static void printGoneDate(Date givenDate) {
        Date date = new Date();
        Calendar c = new GregorianCalendar();
        c.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        Date todayDate = c.getTime();
        long goneTime = (date.getTime() - givenDate.getTime()) / 1000, todayTime = todayDate.getTime();
        //System.out.print("Время: " + tweetTime + " " + currTime + "   ");
        if (goneTime <= 2) {
            System.out.print("Только что ");
            return;
        }
        if (goneTime < 60) {
            rightWordPrinting(goneTime, 0);
            return;
        }
        if (goneTime < 3600) {
            rightWordPrinting(goneTime / 60, 1);
            return;
        }
        if (goneTime < 3600 * 24 && givenDate.getTime() >= todayTime) {
            rightWordPrinting(goneTime / 3600, 2);
            return;
        }
        if (givenDate.getTime() < todayTime && givenDate.getTime() >= todayTime - (60 * 60 * 24 * 1000)) {
            System.out.print("Вчера ");
            return;
        }
        rightWordPrinting((todayTime - givenDate.getTime()) / (1000 * 3600 * 24), 3);
    }
}


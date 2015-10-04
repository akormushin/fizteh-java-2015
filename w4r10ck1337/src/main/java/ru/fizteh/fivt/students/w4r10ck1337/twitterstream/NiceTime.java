package ru.fizteh.fivt.students.w4r10ck1337.twitterstream;

import java.util.Date;


public class NiceTime {
    private static final int FUCKING_100 = 100;
    private static final int FUCKING_19 = 19;
    private static final int FUCKING_4 = 4;
    private static final int FUCKING_11 = 11;
    private static final int FUCKING_10 = 10;
    private static final int SECOND = 1000;
    private static final int MINUTE = SECOND * 60;
    private static final int HOUR = MINUTE * 60;
    private static final int DAY = HOUR * 24;

    private static String timeToString(long time, String[] variants) {
        if (time % FUCKING_100 >= FUCKING_11
                && time % FUCKING_100 <= FUCKING_19) {
            return time + " " + variants[0] + " назад";
        }
        if (time % FUCKING_10 == 1) {
            return time + " " + variants[1] + " назад";
        }
        if (time % FUCKING_10 >= 2 && time % FUCKING_10 <= FUCKING_4) {
            return time + " " + variants[2] + " назад";
        }
        return time + " " + variants[0] + " назад";
    }

    public static String diff(Date date) {
        long currTime = System.currentTimeMillis();
        if (currTime - date.getTime() < MINUTE * 2) {
            return "Только что";
        } else if (currTime - date.getTime() < HOUR) {
            return timeToString(
                    (currTime - date.getTime()) / MINUTE,
                    new String[]{"минут", "минуту", "минуты"});
        } else if (currTime - date.getTime() < DAY) {
            return timeToString(
                    (currTime - date.getTime()) / HOUR,
                    new String[]{"часов", "час", "часа"});
        } else if (currTime - date.getTime() < DAY * 2) {
            return "Вчера";
        } else {
            return timeToString(
                    (currTime - date.getTime()) / DAY,
                    new String[]{"дней", "день", "дня"});
        }
    }
}

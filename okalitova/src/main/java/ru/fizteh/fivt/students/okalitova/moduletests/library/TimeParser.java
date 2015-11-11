package ru.fizteh.fivt.students.okalitova.moduletests.library;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Created by nimloth on 01.11.15.
 */
public class TimeParser {
    private static final long SEC_IN_MIN = 60;
    private static final long SEC_IN_HOUR = 60 * 60;
    private static final long SEC_IN_DAY = 60 * 60 * 24;
    private static final int MINUTS_ID = 0;
    private static final int HOURS_ID = 1;
    private static final int DAYS_ID = 2;
    private static final int FIRST_FORM = 0;
    private static final int SECOND_FORM = 1;
    private static final int THIRD_FORM = 2;
    private static final int FIVE = 5;
    private static final int ELEVEN = 11;
    private static final int TWELVE = 12;
    private static final int MOD10 = 10;
    public static final int  MOD100 = 100;

    private static final String[][] WORD =  {
        {"минутку", "минутки", "минуток"},
        {"часик", "часика", "часиков"},
        {"денек", "денька", "деньков"}
    };

    public static String timeUnits(long time, int unit) {
        if (time % MOD10 == 1 && time % MOD100 != ELEVEN) {
            return WORD[unit][FIRST_FORM];
        } else {
            if (time % MOD10 > 1 && time % MOD10 < FIVE && time % MOD100 != TWELVE) {
                return WORD[unit][SECOND_FORM];
            } else {
                return WORD[unit][THIRD_FORM];
            }
        }
    }
    public static boolean today(long time, long currentTime) {
        long pastDays = currentTime / SEC_IN_DAY;
        long todayTime = currentTime - pastDays * SEC_IN_DAY;
        return todayTime >= currentTime - time;
    }
    public static boolean yesterday(long time, long currentTime) {
        long pastDays = currentTime / SEC_IN_DAY;
        long todayTime = currentTime - (pastDays - 1) * SEC_IN_DAY;
        return ((todayTime >= (currentTime - time)) && !today(time, currentTime));
    }

    public static String getTime(LocalDateTime timeStatus, LocalDateTime curTime) {
        long currentTime = curTime.toEpochSecond(ZoneOffset.UTC);
        long time = timeStatus.toEpochSecond(ZoneOffset.UTC);
        long minBetween = (currentTime - time) / SEC_IN_MIN;
        long hoursBetween = (currentTime - time) / SEC_IN_HOUR;
        long daysBetween = (currentTime - time) / SEC_IN_DAY;
        StringBuilder stringTime = new StringBuilder();
        stringTime.append("[");
        if (minBetween <= 2) {
            stringTime.append("только что");
        } else {
            if (hoursBetween < 1) {
                stringTime.append(minBetween).append(" ").append(timeUnits(minBetween, MINUTS_ID))
                        .append(" назад");
            } else {
                if (today(time, currentTime)) {
                    stringTime.append(hoursBetween).append(" ").append(timeUnits(hoursBetween, HOURS_ID))
                            .append(" назад");
                } else {
                    if (yesterday(time, currentTime)) {
                        stringTime.append("вчера");
                    } else {
                        stringTime.append(daysBetween).append(" ").append(timeUnits(daysBetween, DAYS_ID))
                                .append(" назад");
                    }
                }
            }
        }
        stringTime.append("]");
        return stringTime.toString();
    }
}

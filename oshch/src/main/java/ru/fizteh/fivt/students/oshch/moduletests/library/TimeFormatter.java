package ru.fizteh.fivt.students.oshch.moduletests.library;

import twitter4j.Status;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class TimeFormatter {
    public static final int MILSEC_IN_SEC = 1000;
    public static final long SEC_IN_MIN = 60;
    public static final long SEC_IN_HOUR = SEC_IN_MIN * 60;
    public static final long SEC_IN_DAY = SEC_IN_HOUR * 24;
    public static final int FIVE = 5;
    public static final int ELEVEN = 11;
    public static final int TWELVE = 12;
    public static final int MOD10 = 10;
    public static final int MOD100 = 100;
    public static final String[] MINUTES = {"минуту", "минуты", "минут"};
    public static final String[] HOURS = {"час", "часа", "часов"};
    public static final String[] DAYS = {"день", "дня", "дней"};

    public static String myTime(long time, String[] timeType) {
        if (time % MOD10 == 1 && time % MOD100 != ELEVEN) {
            return timeType[0];
        } else {
            if (time % MOD10 > 1 && time % MOD10 < FIVE
                    && time % MOD100 != TWELVE) {
                return timeType[1];
            } else {
                return timeType[2];
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
                stringTime.append(minBetween).append(" ").append(myTime(minBetween, MINUTES))
                        .append(" назад");
            } else {
                if (today(time, currentTime)) {
                    stringTime.append(hoursBetween).append(" ").append(myTime(hoursBetween, HOURS))
                            .append(" назад");
                } else {
                    if (yesterday(time, currentTime)) {
                        stringTime.append("вчера");
                    } else {
                        stringTime.append(daysBetween).append(" ").append(myTime(daysBetween, DAYS))
                                .append(" назад");
                    }
                }
            }
        }
        stringTime.append("]");
        return stringTime.toString();
    }

    public static String printTime(Status status) {
        LocalDateTime time =
            LocalDateTime.ofEpochSecond(status.getCreatedAt().getTime() / MILSEC_IN_SEC, MILSEC_IN_SEC, ZoneOffset.UTC);
        return getTime(time, LocalDateTime.now(ZoneId.ofOffset("", ZoneOffset.UTC)));
    }
}

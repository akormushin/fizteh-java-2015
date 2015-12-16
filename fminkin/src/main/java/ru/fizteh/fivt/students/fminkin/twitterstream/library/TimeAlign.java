package ru.fizteh.fivt.students.fminkin.twitterstream.library;

/**
 * Created by Федор on 28.09.2015.
 */

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;


public class TimeAlign {
    public static String formatZoneTime(long currentTime, long timeToFormat,
                                  ZoneId zone) {
        LocalDateTime current = new Date(currentTime).toInstant()
                .atZone(zone).toLocalDateTime();
        LocalDateTime tweetTime = new Date(timeToFormat).toInstant()
                .atZone(zone).toLocalDateTime();

        if (ChronoUnit.MINUTES.between(tweetTime, current) < 2) {
            return "Только что";
        }
        if (ChronoUnit.HOURS.between(tweetTime, current) <= 0) {
            long n = ChronoUnit.MINUTES.between(tweetTime, current);
            return String.valueOf(n) + " " + RussianDeclense.getMinutes(n) + " назад";
        }

        if (ChronoUnit.DAYS.between(tweetTime, current) <= 0) {
            long n = ChronoUnit.HOURS.between(tweetTime, current);
            return String.valueOf(n) + " " + RussianDeclense.getHours(n) + " назад";
        }

        if (ChronoUnit.DAYS.between(tweetTime, current) == 1) {
            return "вчера";
        }
        long n = ChronoUnit.DAYS.between(tweetTime, current);
        return String.valueOf(n) + " " + RussianDeclense.getDays(n) + " назад";
    }

    public static String formatTime(long currentTime, long timeToFormat) {
        return formatZoneTime(currentTime, timeToFormat,
                ZoneId.systemDefault());
    }
}

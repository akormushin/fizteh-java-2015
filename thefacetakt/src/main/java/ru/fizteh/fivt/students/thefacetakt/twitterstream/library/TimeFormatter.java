package ru.fizteh.fivt.students.thefacetakt.twitterstream.library;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Created by thefacetakt on 26.09.15.
 */

class TimeFormatter {

    static String zonedFormatTime(long currentTime, long timeToFormat,
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
            return String.valueOf(n)
                    + " " + Declenser.minutesDeclension(n) + " назад";
        }

        if (ChronoUnit.DAYS.between(tweetTime, current) <= 0) {
            long n = ChronoUnit.HOURS.between(tweetTime, current);
            return String.valueOf(n) + " " + Declenser.hoursDeclension(n)
                    + " назад";
        }

        if (ChronoUnit.DAYS.between(tweetTime, current) == 1) {
            return "вчера";
        }
        long n = ChronoUnit.DAYS.between(tweetTime, current);
        return String.valueOf(n) + " " + Declenser.daysDeclension(n)
                + " назад";
    }

    static String formatTime(long currentTime, long timeToFormat) {
        return zonedFormatTime(currentTime, timeToFormat,
                ZoneId.systemDefault());
    }
}

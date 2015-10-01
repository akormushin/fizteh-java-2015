package ru.fizteh.fivt.students.thefacetakt.twitterstream;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Created by thefacetakt on 26.09.15.
 */

class TimeFormatter {

    static String formatTime(long currentTime, long timeToFormat) {
        LocalDateTime current = new Date(currentTime).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime tweetTime = new Date(timeToFormat).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();

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
}

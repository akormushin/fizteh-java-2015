package ru.fizteh.fivt.students.thefacetakt.twitterstream;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by thefacetakt on 26.09.15.
 */

class TimeFormatter {

    static String formatTime(long currentTime, long timeToFormat) {
        if (currentTime - timeToFormat < 2 * TimeUnit.MINUTES.toMillis(1L)) {
            return "Только что";
        }
        if (currentTime - timeToFormat < TimeUnit.HOURS.toMillis(1L)) {
            long n = ((currentTime - timeToFormat)
                    / TimeUnit.MINUTES.toMillis(1L));
            return String.valueOf(n)
                    + " " + Declenser.minutesDeclension(n) + " назад";
        }

        if (new Date(currentTime).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate()
                .equals(LocalDate.now())) {
            long n = (currentTime - timeToFormat) / TimeUnit.HOURS.toMillis(1L);
            return String.valueOf(n) + " " + Declenser.hoursDeclension(n)
                    + " назад";
        }

        if (new Date(currentTime).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate()
                .equals(LocalDate.now().minusDays(1))) {
            return "вчера";
        }
        long n = (currentTime - timeToFormat) / TimeUnit.DAYS.toMillis(1L);
        return String.valueOf(n) + " " + Declenser.daysDeclension(n)
                + " назад";
    }
}

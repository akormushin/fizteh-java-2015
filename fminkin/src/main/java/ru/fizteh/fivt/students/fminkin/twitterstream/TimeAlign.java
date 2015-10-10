package ru.fizteh.fivt.students.fminkin.twitterstream;

/**
 * Created by Федор on 28.09.2015.
 */
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;
public class TimeAlign {
    public static String printTime(long rawTime) {
        if (rawTime < 2 * TimeUnit.MINUTES.toMillis(1L)) {
            return "Только что";
        }
        if (rawTime < TimeUnit.HOURS.toMillis(1L)) {
            long n = rawTime / TimeUnit.MINUTES.toMillis(1L);
            return String.valueOf(n) + " " + RussianDeclense.getMinutes(n) + " назад";
        }
        if (new Date(System.currentTimeMillis() - rawTime).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                .equals(LocalDate.now())) {
            long n = rawTime / TimeUnit.HOURS.toMillis(1L);
            return String.valueOf(n) + " " + RussianDeclense.getHours(n) + " назад";
        }
        if (new Date(System.currentTimeMillis() - rawTime).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                .equals(LocalDate.now().minusDays(1))) {
            return "вчера";
        }
        long n = rawTime / TimeUnit.DAYS.toMillis(1L);
        return String.valueOf(n) + " " + RussianDeclense.getDays(n) + " назад";
    }
}

package ru.fizteh.fivt.students.duha666.TwitterStream;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.time.*;

public class TimeFormatter {
    public static String formatTime(Date date) {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime tweetTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        long minutesDifference = ChronoUnit.MINUTES.between(currentTime, tweetTime);
        long hoursDifference = ChronoUnit.HOURS.between(currentTime, tweetTime);
        long daysDifference = ChronoUnit.DAYS.between(currentTime, tweetTime);
        if (minutesDifference < 2) {
            return "только что";
        }
        if (hoursDifference == 0) {
            return Long.toString(minutesDifference) + " минут назад";
        }
        if (daysDifference == 0) {
            return Long.toString(hoursDifference) + "часов назад";
        }
        if (daysDifference == 1) {
            return "вчера";
        }
        return Long.toString(daysDifference) + "дней назад";
    }
}

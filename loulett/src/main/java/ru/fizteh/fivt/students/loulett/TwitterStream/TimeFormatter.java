package ru.fizteh.fivt.students.loulett.TwitterStream;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class TimeFormatter {

    public static String timeFromPublish(long timeCreateTwit, long currentTime) {
        LocalDateTime currTime = new Date(currentTime).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime tweetTime = new
                Date(timeCreateTwit).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        if (ChronoUnit.MINUTES.between(tweetTime, currTime) < 2) {
            return "Только что";
        } else if (ChronoUnit.HOURS.between(tweetTime, currTime) == 0) {
            long minutes = ChronoUnit.MINUTES.between(tweetTime, currTime);
            return RussianEnding.russianEnding((int) minutes, "минута") + " назад";
        } else if (ChronoUnit.DAYS.between(tweetTime, currTime) == 0) {
            long hours = ChronoUnit.HOURS.between(tweetTime, currTime);
            return RussianEnding.russianEnding((int) hours, "час") + " назад";
        } else if (ChronoUnit.DAYS.between(tweetTime, currTime) == 1) {
            return "вчера";
        } else {
            long days = ChronoUnit.DAYS.between(tweetTime, currTime);
            return RussianEnding.russianEnding((int) days, "день") + " назад";
        }
    }
}

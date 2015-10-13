package ru.fizteh.fivt.students.mamaevads.twitterstream;
import twitter4j.*;
import java.time.temporal.ChronoUnit;
import java.time.LocalDateTime;
import java.time.ZoneId;

class TimeHandler {
    static String getType(Status tweet) {
        LocalDateTime tweetDate = tweet.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime curDate = LocalDateTime.now();
        if (ChronoUnit.MINUTES.between(tweetDate, curDate) < 2) {
            return "только что";
        } else if (ChronoUnit.HOURS.between(tweetDate, curDate) == 0) {
            long between = ChronoUnit.MINUTES.between(tweetDate, curDate);
            return between + " " + WordForms.minutesForm(between);
        } else if (ChronoUnit.DAYS.between(tweetDate, curDate) == 0) {
            long between = ChronoUnit.HOURS.between(tweetDate, curDate);
            return between + " " + WordForms.hourForm(between);
        } else if (ChronoUnit.DAYS.between(tweetDate, curDate) == 1) {
            return "вчера";
        } else {
            long between = ChronoUnit.DAYS.between(tweetDate, curDate);
            return between + " " + WordForms.daysForm(between);
        }
    }
}


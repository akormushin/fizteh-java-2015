package ru.fizteh.fivt.students.cache_nez.TwitterStream;

import twitter4j.Status;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAmount;
import java.util.Date;

/**
 * Created by cache-nez on 9/28/15.
 */
public class TextFormatter {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";

    static String getRetweetText(Status status, boolean streamMode) {
        /*remove prefix "<person> retweeted <person>"*/
        String[] splitText = status.getText().split(":", 2);

        String text = "";
        if (!streamMode) {
            text = "[" + TimeConverter.getRelativeTime(status.getCreatedAt()) + "] ";
        }
        text += ANSI_BLUE + "@" + status.getUser().getScreenName() + ANSI_RESET;
        text += " ретвитнул " + ANSI_BLUE + "@" + status.getRetweetedStatus().getUser().getScreenName()
                + ANSI_RESET;
        text += ": " + splitText[1];
        return text;
    }

    static String getTweetText(Status status, boolean streamMode) {
        String text = "";
        if (!streamMode) {
            text = "[" + TimeConverter.getRelativeTime(status.getCreatedAt()) + "] ";
        }
        text += ANSI_BLUE + "@" + status.getUser().getScreenName() + ANSI_RESET;
        text += ": " + status.getText();
        if (status.getRetweetCount() > 0) {
            int retweets = status.getRetweetCount();
            text += " (" + retweets + Declenser.getDeclension(retweets, Declenser.ToDeclense.RETWEET) + ")";
        }
        return  text;
    }

}

class Declenser {
    public enum ToDeclense { MINUTE, HOUR, DAY, RETWEET }

    private static final int FIRST_CASE = 1;
    private static final int SECOND_CASE_START = 2;
    private static final int SECOND_CASE_END = 4;
    private static final int EXCEPTION_START = 11;
    private static final int EXCEPTION_END = 14;
    private static final int FIRST_MODULO = 100;
    private static final int SECOND_MODULO = 10;
    private static final String[] FIRST_TYPE = {"минуту", "час", "день", "ретвит"};
    private static final String[] SECOND_TYPE = {"минуты", "часа", "дня", "ретвита"};
    private static final String[] THIRD_TYPE = {"минут", "часов", "дней", "ретвитов"};


    public static String getDeclension(long numeral, ToDeclense word) {
        numeral %= FIRST_MODULO;
        if (numeral >= EXCEPTION_START && numeral <= EXCEPTION_END) {
            return SECOND_TYPE[word.ordinal()];
        }
        numeral %= SECOND_MODULO;
        if (numeral == FIRST_CASE) {
            return FIRST_TYPE[word.ordinal()];
        }
        if (numeral >= SECOND_CASE_START && numeral <= SECOND_CASE_END) {
            return SECOND_TYPE[word.ordinal()];
        }
        return THIRD_TYPE[word.ordinal()];
    }
}

class TimeConverter {
    /*TODO: передавать текущую дату внутрь (легче тестить)*/

    private static final TemporalAmount TWO_MINUTES = Duration.ofMinutes(2);
    private static final TemporalAmount HOUR = Duration.ofHours(1);
    private static final int MINUTES_IN_DAY = 24 * 60;
    private static final int HOURS_IN_DAY = 60;


    public static String getRelativeTime(Date date) {
        LocalDateTime tweetDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime currentDate = LocalDateTime.now();
        if (currentDate.minus(TWO_MINUTES).isBefore(tweetDate)) {
            return "только что";
        }
        int delta;
        if (currentDate.minus(HOUR).isBefore(tweetDate)) {
            delta = currentDate.get(ChronoField.MINUTE_OF_DAY) - tweetDate.get(ChronoField.MINUTE_OF_DAY);
            if (delta < 0) {
                delta = MINUTES_IN_DAY + delta;
            }
            return delta + Declenser.getDeclension(delta, Declenser.ToDeclense.MINUTE) + " назад";
        }
        if (tweetDate.toLocalDate().equals(LocalDate.now())) {
            delta = currentDate.get(ChronoField.HOUR_OF_DAY) - tweetDate.get(ChronoField.HOUR_OF_DAY);
            if (delta < 0) {
                delta = HOURS_IN_DAY + delta;
            }
            return delta + Declenser.getDeclension(delta, Declenser.ToDeclense.HOUR) + " назад";
        }
        if (tweetDate.toLocalDate().equals(LocalDate.now().minusDays(1))) {
            return "вчера";
        }
        long days = currentDate.getLong(ChronoField.EPOCH_DAY) - tweetDate.getLong(ChronoField.EPOCH_DAY);
        return  days + Declenser.getDeclension(days, Declenser.ToDeclense.DAY) + " назад";
    }
}

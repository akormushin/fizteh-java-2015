package ru.fizteh.fivt.students.ocksumoron.twitterstream;

import twitter4j.Status;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Created by ocksumoron on 30.09.15.
 */
public class FormatMaster {

    static final int GET_LAST_NUM = 10;
    static final int GET_LAST_TWO_NUMS = 100;
    static final int ZERO = 0;
    static final int ONE = 1;
    static final int TWO = 2;
    static final int FOUR = 4;
    static final int FIVE = 5;
    static final int NINE = 9;
    static final int ELEVEN = 11;
    static final int NINETEEN = 19;
    static final String SEPARATOR =
            "\n-----------------------------------------------------------------------------------------------------\n";

    private static final String[] MINUTES_FORMS = {"минуту", "минуты", "минут"};
    private static final String[] HOURS_FORMS = {"час", "часа", "часов"};
    private static final String[] DAYS_FORMS = {"день", "дня", "дней"};
    private static final String[] RETWEET_FORMS = {"ретвит", "ретвита", "ретвитов"};

    enum ETime {
        MINUTE,
        HOUR,
        DAY
    }

    private static int getCorrectForm(long number) {
        if (number % GET_LAST_NUM == ONE && number % GET_LAST_TWO_NUMS != ELEVEN) {
            return 0;
        }
        if (number % GET_LAST_NUM > ONE && number % GET_LAST_NUM < FIVE
                && !(number % GET_LAST_TWO_NUMS >= ELEVEN && number % GET_LAST_TWO_NUMS <= NINETEEN)) {
            return 1;
        }
        return 2;
    }

    private static String getTimeString(long number, ETime type) {
        if (type == ETime.MINUTE) {
            return " " + MINUTES_FORMS[getCorrectForm(number)] + " назад";
        } else if (type == ETime.HOUR) {
            return " " + HOURS_FORMS[getCorrectForm(number)] + " назад";
        } else {
            return " " + DAYS_FORMS[getCorrectForm(number)] + " назад";
        }
    }


    private static String formatTime(Date date) {
        LocalDateTime tweetDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime curDate = LocalDateTime.now();
        if (ChronoUnit.MINUTES.between(tweetDate, curDate) < 2) {
            return "только что";
        } else if (ChronoUnit.HOURS.between(tweetDate, curDate) == 0) {
            return Long.toString(ChronoUnit.MINUTES.between(tweetDate, curDate))
                    + getTimeString(ChronoUnit.MINUTES.between(tweetDate, curDate), ETime.MINUTE);
        } else if (ChronoUnit.DAYS.between(tweetDate, curDate) == 0) {
            return Long.toString(ChronoUnit.HOURS.between(tweetDate, curDate))
                    + getTimeString(ChronoUnit.HOURS.between(tweetDate, curDate), ETime.HOUR);
        } else if (ChronoUnit.DAYS.between(tweetDate, curDate) == ONE) {
            return "вчера";
        } else {
            return Long.toString(ChronoUnit.DAYS.between(tweetDate, curDate))
                    + getTimeString(ChronoUnit.DAYS.between(tweetDate, curDate), ETime.DAY);
        }
    }

    public static String format(Status s, boolean isHideRetweets, boolean isStream) {
        String result = "";
        if (!isStream) {
            String time = formatTime(s.getCreatedAt());
            result = "[" + time + "] ";
        }
        if (!isHideRetweets) {
            if (s.isRetweet()) {
                result += "@" + s.getUser().getName() + " ретвитнул @"
                        + s.getRetweetedStatus().getUser().getName() + ": " + s.getText();
            } else {
                result += "@" + s.getUser().getName() + ": " + s.getText();
                if (s.getRetweetCount() != 0) {
                    result += " (" + Long.toString(s.getRetweetCount()) + " "
                            + RETWEET_FORMS[getCorrectForm(s.getRetweetCount())] + ")";
                }
            }
            result += SEPARATOR;
            return result;
        } else if (!s.isRetweeted()) {
            result += "@" + s.getUser().getName() + ": " + s.getText() + SEPARATOR;
            return result;
        }
        return "";
    }


}

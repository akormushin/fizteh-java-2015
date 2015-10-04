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
    static final int NUMBER_ONE = 1;
    static final int NUMBER_FIVE = 5;
    static final int NUMBER_ELEVEN = 11;
    static final int NUMBER_NINETEEN = 19;
    static final String SEPARATOR =
            "\n-----------------------------------------------------------------------------------------------------\n";

    private static final String[] MINUTES_FORMS = {"минуту", "минуты", "минут"};
    private static final String[] HOURS_FORMS = {"час", "часа", "часов"};
    private static final String[] DAYS_FORMS = {"день", "дня", "дней"};

    private static final String[][] TIME_FORMS = {MINUTES_FORMS, HOURS_FORMS, DAYS_FORMS};

    private static final String[] RETWEET_FORMS = {"ретвит", "ретвита", "ретвитов"};

    private enum ETime {
        MINUTE (0),
        HOUR (1),
        DAY (2);

        private int type;

        private int getType() {
            return type;
        }

        ETime(int type) {
            this.type = type;
        }
    }

    private static ETime getCorrectForm(long number) {
        if (number % GET_LAST_NUM == NUMBER_ONE && number % GET_LAST_TWO_NUMS != NUMBER_ELEVEN) {
            return ETime.MINUTE;
        }
        if (number % GET_LAST_NUM > NUMBER_ONE && number % GET_LAST_NUM < NUMBER_FIVE
                && !(number % GET_LAST_TWO_NUMS >= NUMBER_ELEVEN && number % GET_LAST_TWO_NUMS <= NUMBER_NINETEEN)) {
            return ETime.HOUR;
        }
        return ETime.DAY;
    }

    private static String getTimeString(long number, ETime type) {
        ETime correctForm = getCorrectForm(number);
        return " " + TIME_FORMS[type.getType()][correctForm.getType()];
    }


    private static String formatTime(Date date) {
        LocalDateTime tweetDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime curDate = LocalDateTime.now();
        long minuteDiff = ChronoUnit.MINUTES.between(tweetDate, curDate);
        long hourDiff = ChronoUnit.HOURS.between(tweetDate, curDate);
        long dayDiff = ChronoUnit.DAYS.between(tweetDate, curDate);
        if (minuteDiff < 2) {
            return "только что";
        } else if (hourDiff == 0) {
            return Long.toString(minuteDiff)
                    + getTimeString(minuteDiff, ETime.MINUTE);
        } else if (ChronoUnit.DAYS.between(tweetDate, curDate) == 0) {
            return Long.toString(hourDiff)
                    + getTimeString(hourDiff, ETime.HOUR);
        } else if (dayDiff == NUMBER_ONE) {
            return "вчера";
        } else {
            return Long.toString(dayDiff)
                    + getTimeString(dayDiff, ETime.DAY);
        }
    }

    public static String format(Status s, boolean isHideRetweets, boolean isStream) {
        StringBuilder result = new StringBuilder("");
        if (!isStream) {
            String time = formatTime(s.getCreatedAt());
            result.append("[" + time + "]");
        }
        if (!isHideRetweets) {
            if (s.isRetweet()) {
                result.append("@" + s.getUser().getName() + " ретвитнул @"
                        + s.getRetweetedStatus().getUser().getName() + ": " + s.getRetweetedStatus().getText());
            } else {
                result.append("@" + s.getUser().getName() + ": " + s.getText());
                if (s.getRetweetCount() != 0) {
                    result.append(" (" + Long.toString(s.getRetweetCount()) + " "
                            + RETWEET_FORMS[getCorrectForm(s.getRetweetCount()).getType()] + ")");
                }
            }
            result.append(SEPARATOR);
            return result.toString();
        } else if (!s.isRetweeted()) {
            result.append("@" + s.getUser().getName() + ": " + s.getText() + SEPARATOR);
            return result.toString();
        }
        return "";
    }
}

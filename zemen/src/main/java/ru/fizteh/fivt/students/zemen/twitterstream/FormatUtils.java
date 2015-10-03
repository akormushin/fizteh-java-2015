/**
 * Created by zemen on 26.09.15.
 */

package ru.fizteh.fivt.students.zemen.twitterstream;

import twitter4j.Status;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class FormatUtils {
    public static final int FIVE = 5;
    public static final int TEN = 10;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";

    public static final String[] HOUR_FORMS = new String[]{"часов", "час", "часа"};
    public static final String[] DAY_FORMS = new String[]{"дней", "день", "дня"};
    public static final String[] MINUTE_FORMS = new String[]{"минут", "минута", "минуты"};
    public static final String[] RETWEET_FORMS = new String[]{"ретвитов", "ретвит", "ретвита"};

    public static String formatName(String name) {
        return "@" + ANSI_BLUE + name + ANSI_RESET;
    }

    public static String getRussianForm(String[] form, long count) {
        if (count / TEN == 1) {
            return form[0];
        }
        if (count % TEN == 1) {
            return form[1];
        }
        if (count % TEN > 0 && count % TEN < FIVE) {
            return form[2];
        }
        return form[0];
    }

    public static String formatTime(Date time) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime date = time.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
        long daysBetween = ChronoUnit.DAYS.between(date.toLocalDate(),
                now.toLocalDate());
        StringBuilder res = new StringBuilder().append("[");

        if (daysBetween == 1) {
            res.append("вчера");
        } else if (daysBetween > 0) {
            res.append(daysBetween).append(" ").append(FormatUtils.getRussianForm(DAY_FORMS, daysBetween))
                    .append(" назад");
        } else {
            long hours = ChronoUnit.HOURS.between(date, now);
            long minutes = ChronoUnit.MINUTES.between(date, now);
            if (hours > 0) {
                res.append(hours).append(" ").append(FormatUtils.getRussianForm(HOUR_FORMS, hours)).append(" назад");
            } else if (minutes > 2) {
                res.append(minutes).append(" ").append(FormatUtils.getRussianForm(MINUTE_FORMS, minutes))
                        .append(" назад");
            } else {
                res.append("только что");
            }
        }
        res.append("]");
        return res.toString();
    }

    public static String formatAll(Status tweet, Arguments arguments) {
        boolean isRetweet = tweet.isRetweet();
        StringBuilder formattedTweet = new StringBuilder();
        if (!arguments.isStream()) {
            formattedTweet.append(FormatUtils.formatTime(
                    tweet.getCreatedAt())).append(" ");
        }
        formattedTweet.append(FormatUtils.formatName(
                tweet.getUser().getScreenName())).append(": ");
        if (isRetweet) {
            tweet = tweet.getRetweetedStatus();
            formattedTweet.append("ретвитнул ").append(FormatUtils.formatName(tweet.getUser().getScreenName()))
                    .append(": ");
        }
        formattedTweet.append(tweet.getText());
        if (!isRetweet) {
            int retweets = tweet.getRetweetCount();
            formattedTweet.append(" (").append(retweets).append(" ")
                    .append(FormatUtils.getRussianForm(RETWEET_FORMS, retweets)).append(")");
        }
        return formattedTweet.toString();
    }
}

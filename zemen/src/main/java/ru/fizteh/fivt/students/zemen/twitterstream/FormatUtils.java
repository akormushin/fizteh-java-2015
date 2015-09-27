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
        String res = "[";

        if (daysBetween == 1) {
            res += "вчера";
        } else if (daysBetween > 0) {
            String[] form = new String[]{"дней", "день", "дня"};
            res += daysBetween + " "
                    + FormatUtils.getRussianForm(form, daysBetween) + " назад";
        } else {
            long hours = ChronoUnit.HOURS.between(date, now);
            long minutes = ChronoUnit.MINUTES.between(date, now);
            if (hours > 0) {
                String[] form = new String[]{"часов", "час", "часа"};
                res += hours + " "
                        + FormatUtils.getRussianForm(form, hours)
                        + " назад";
            } else if (minutes > 2) {
                String[] form = new String[]{"минут", "минуту", "минуты"};
                res += minutes + " "
                        + FormatUtils.getRussianForm(form, minutes)
                        + " назад";
            } else {
                res += "только что";
            }
        }
        res += "]";
        return res;
    }

    public static String formatAll(Status tweet, Arguments arguments) {
        boolean isRetweet = tweet.isRetweet();
        String formattedTweet = "";
        if (!arguments.isStream()) {
            formattedTweet += FormatUtils.formatTime(
                    tweet.getCreatedAt()) + " ";
        }
        formattedTweet += FormatUtils.formatName(
                tweet.getUser().getScreenName()) + ": ";
        if (isRetweet) {
            tweet = tweet.getRetweetedStatus();
            formattedTweet += "ретвитнул "
                    + FormatUtils.formatName(tweet.getUser().getScreenName())
                    + ": ";
        }
        formattedTweet += tweet.getText();
        if (!isRetweet) {
            String[] form = new String[]{"ретвитов", "ретвит", "ретвита"};
            int retweets = tweet.getRetweetCount();
            formattedTweet += " (" + retweets + " "
                    + FormatUtils.getRussianForm(form, retweets) + ")";
        }
        return formattedTweet;
    }
}

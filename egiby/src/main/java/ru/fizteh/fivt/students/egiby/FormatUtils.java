package ru.fizteh.fivt.students.egiby;

import twitter4j.Status;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

/**
 * Created by egiby on 30.09.15.
 */
public class FormatUtils {

    private static final long FIVE = 5L;
    private static final long ONE = 1L;
    private static final long TEN = 10L;
    private static final long ELEVEN = 11L;
    private static final long HUNDRED = 100L;
    private static final long TWENTY = 20L;
    private static final String[] DAYS = {"день", "дня", "дней"};
    private static final String[] HOURS = {"час", "часа", "часов"};
    private static final String[] MINUTES = {"минуту", "минуты", "минут"};

    private static String formatNumber(long number, String[] form) {
        if (number % TEN == ONE && number % HUNDRED != ELEVEN) {
            return number + " " + form[0];
        }

        if (number % TEN > ONE && number % TEN < FIVE && (number % HUNDRED < ELEVEN || number % HUNDRED > TWENTY)) {
            return number + " " + form[1];
        }

        return number + " " + form[2];
    }

    private static String getTweetTime(Status tweet) {
        String timeString;

        LocalDateTime current = LocalDateTime.now();
        LocalDateTime tweetTime = tweet.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        if (ChronoUnit.MINUTES.between(tweetTime, current) < 2) {
            timeString = "только что";
        } else if (ChronoUnit.HOURS.between(tweetTime, current) < 1) {
            timeString = formatNumber(ChronoUnit.MINUTES.between(tweetTime, current), MINUTES) + " назад";
        } else if (ChronoUnit.DAYS.between(tweetTime, current) < 1) {
            timeString = formatNumber(ChronoUnit.HOURS.between(tweetTime, current), HOURS) + " назад";
        } else if (ChronoUnit.DAYS.between(tweetTime, current) == 1) {
            timeString = "вчера";
        } else {
            timeString = formatNumber(ChronoUnit.DAYS.between(tweetTime, current), DAYS) + " назад";
        }

        return "[" + timeString + "] ";
    }

    private static final String[] RETWEETS = {"ретвит", "ретвита", "ретвитов"};

    public static String formatTweet(Status tweet, boolean isStream) {
        String formattedTweet = new String("");

        if (!isStream) {
            formattedTweet += getTweetTime(tweet);
        }

        formattedTweet += "@" + tweet.getUser().getScreenName() + ": ";

        boolean isRetweet = tweet.isRetweet();
        if (isRetweet) {
            tweet = tweet.getRetweetedStatus();
            formattedTweet += "ретвитнул " + "@" + tweet.getUser().getScreenName() + ": ";
        }

        formattedTweet += tweet.getText();

        if (!isRetweet) {
            formattedTweet += " (" + formatNumber(tweet.getRetweetCount(), RETWEETS) + ")";
        }

        return formattedTweet;
    }
}

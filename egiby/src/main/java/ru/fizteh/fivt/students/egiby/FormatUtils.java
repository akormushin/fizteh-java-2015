package ru.fizteh.fivt.students.egiby;

import twitter4j.Status;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

/**
 * Created by egiby on 30.09.15.
 */
public class FormatUtils {

    private static final Long FIVE = 5L;
    private static final Long ONE = 1L;
    private static final Long TEN = 10L;
    private static final String[] DAYS = {"день", "дня", "дней"};
    private static final String[] HOURS = {"час", "часа", "часов"};
    private static final String[] MINUTES = {"минуту", "минуты", "минут"};

    private static String formatTime(Long number, String[] form) {
        if (number == ONE) {
            return number + " " + form[0];
        }

        if (number > ONE && number < FIVE) {
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
            timeString = formatTime(ChronoUnit.MINUTES.between(tweetTime, current) % TEN, MINUTES) + " назад";
        } else if (ChronoUnit.DAYS.between(tweetTime, current) < 1) {
            timeString = formatTime(ChronoUnit.HOURS.between(tweetTime, current) % TEN, HOURS) + " назад";
        } else if (ChronoUnit.DAYS.between(tweetTime, current) == 1) {
            timeString = "вчера";
        } else {
            timeString = formatTime(ChronoUnit.DAYS.between(tweetTime, current) % TEN, DAYS) + " назад";
        }

        return "[" + timeString + "] ";
    }

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
            formattedTweet += " (" + tweet.getRetweetCount() + " ретвитов)";
        }

        return formattedTweet;
    }
}

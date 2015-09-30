package ru.fizteh.fivt.students.egiby;

import twitter4j.Status;

/**
 * Created by egiby on 30.09.15.
 */
public class FormatUtils {
    private static String formatTime(Status tweet) {
        return "[It is time] ";
    }

    public static String formatTweet(Status tweet, boolean isStream) {
        String formattedTweet = new String("");

        if (!isStream) {
            formattedTweet += formatTime(tweet);
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

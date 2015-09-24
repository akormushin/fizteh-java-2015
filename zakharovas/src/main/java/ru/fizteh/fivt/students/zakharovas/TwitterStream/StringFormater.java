package ru.fizteh.fivt.students.zakharovas.TwitterStream;

import twitter4j.*;

import java.util.Date;

public class StringFormater {
    private static final String COLOR_BLUE = "\u001B[34m";
    private static final String COLOR_RESET = "\u001B[0m";


    public static String retweetFormat(int numberOfretweets) {
        if (numberOfretweets >= Numbers.ELEVEN
                && numberOfretweets <= Numbers.NINETEEN) {
            return "ретвитов";
        }
        if (numberOfretweets % Numbers.TEN == Numbers.ZERO) {
            return "ретвитов";
        } else if (numberOfretweets % Numbers.TEN == Numbers.ONE) {
            return "ретвит";
        } else if (numberOfretweets % Numbers.TEN >= Numbers.TWO
                && numberOfretweets % Numbers.TEN <= Numbers.FOUR) {
            return "ретвита";
        } else if (numberOfretweets % Numbers.TEN >= Numbers.FIVE) {
            return "ретвитов";
        }
        return null;
    }

    public static String dateFormater(Date date) {
        return date.toString();
    }

    public static String tweetForOutput(Status tweet) {
        return dateFormater(tweet.getCreatedAt()) + tweetForOutputWithoutDate(tweet);

    }

    public static String tweetForOutputWithoutDate(Status tweet) {
        String formatedTweet = "";
        if (!tweet.isRetweet()) {
            formatedTweet += COLOR_BLUE
                    + " @" + tweet.getUser().getName() + COLOR_RESET + ": "
                    + tweet.getText();
            if (tweet.getRetweetCount() > 0) {
                formatedTweet += "(" + tweet.getRetweetCount() + " "
                        + retweetFormat(tweet.getRetweetCount()) + ")";
            }
        } else {
            formatedTweet += COLOR_BLUE
                    + " @" + tweet.getUser().getName() + COLOR_RESET + ": "
                    + "ретвитнул " + COLOR_BLUE + "@"
                    + tweet.getRetweetedStatus().getUser().getName() + COLOR_RESET
                    + ": " + tweet.getRetweetedStatus().getText();
        }
        return formatedTweet;
    }
}

package ru.fizteh.fivt.students.zakharovas.TwitterStream;

import twitter4j.*;

import java.util.Date;

public class StringFormater {
    private static final String COLOR_BLUE = "\u001B[34m";
    private static final String COLOR_RESET = "\u001B[0m";


    public static String retweetFormat(int numberOfretweets) {
        if (numberOfretweets >= 11 && numberOfretweets <= 19) {
            return "ретвитов";
        }
        switch (numberOfretweets % 10) {
            case 0:
                return "ретвитов";
            case 1:
                return "ретвит";
            case 2:
            case 3:
            case 4:
                return "ретвита";
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                return "ретвитов";
            default:
                return null;
        }
    }

    public static String dateFormater(Date date) {
        return date.toString();
    }

    public static String tweetForOutput(Status tweet) {
        String formatedTweet = StringFormater.
                dateFormater(tweet.getCreatedAt());
        if (!tweet.isRetweet()) {
            formatedTweet += COLOR_BLUE
                    + " @" + tweet.getUser().getName() + COLOR_RESET + ": "
                    + tweet.getText();
            if (tweet.getRetweetCount() > 0) {
                formatedTweet += "(" + tweet.getRetweetCount() + " "
                + StringFormater.retweetFormat(tweet.getRetweetCount()) + ")";
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

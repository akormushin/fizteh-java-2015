package ru.fizteh.fivt.students.w4r10ck1337.moduletests.library;

import twitter4j.Status;

public class TweetBuilder {
    private static final int FUCKING_3 = 3;

    public static String formatTweet(Status status) {
        String tweet;
        if (status.isRetweet()) {
            tweet = "@" + status.getUser().getScreenName() + " ретвитнул " + status.getText().substring(FUCKING_3);
        } else {
            tweet = "@" + status.getUser().getScreenName() + ": " + status.getText();
            if (status.isRetweeted()) {
                tweet += " (" + status.getRetweetCount() + " ретвитов)";
            }
        }
        return tweet;
    }
}

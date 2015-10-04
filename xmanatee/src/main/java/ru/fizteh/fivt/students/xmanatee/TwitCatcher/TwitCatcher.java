package ru.fizteh.fivt.students.xmanatee.TwitCatcher;

import twitter4j.*;
//import twitter4j.auth.OAuth2Token;

public class TwitCatcher {

    static final String TWEET_ID = "645958092161720320";

    public static void main(String[] args) {
        System.out.println("Trying to retweet...");

        try {
            Twitter twitter = new TwitterFactory().getInstance();
            twitter.retweetStatus(Long.parseLong(TWEET_ID));
            System.out.println("Successfully retweeted status [" + TWEET_ID + "].");
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to retweet: " + te.getMessage());
            System.exit(-1);
        }
    }
}

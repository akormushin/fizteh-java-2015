package ru.fizteh.fivt.students.xmanatee.TwitCatcher;

import twitter4j.*;
//import twitter4j.auth.OAuth2Token;
import twitter4j.conf.ConfigurationBuilder;

public class TwitCatcher {

    /**
     * Put Keys and tokens of other person to teach him to hide 'em.
     */
    static final String CONST_KEY = "4jmukVKfdKvbI18Rd4jZRO7MO";
    static final String CONST_SECRET =
            "8FG2eEA7RYnpX2lwc7cvSTWHq35jF0rrVKChEOHvjmTxbgtDJR";
    static final String ACC_TOKEN =
            "1054439755-ZIYKc6rRrO96uqrNmWNXFSTr4fF4rSfRJyH1V2o";
    static final String ACC_TOKEN_SECRET =
            "h8nh2yLpQZ2egkdLJ7C0MiWm4ujdvF7HJRw25VqbeCRQJ";

    static final String TWEET_ID = "645950334880350208";

    public static void main(String[] args) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey(CONST_KEY);
        cb.setOAuthConsumerSecret(CONST_SECRET);
        cb.setOAuthAccessToken(ACC_TOKEN);
        cb.setOAuthAccessTokenSecret(ACC_TOKEN_SECRET);

        System.out.println("Trying to retweet...");

        try {
            Twitter twitter = new TwitterFactory(cb.build()).getInstance();
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

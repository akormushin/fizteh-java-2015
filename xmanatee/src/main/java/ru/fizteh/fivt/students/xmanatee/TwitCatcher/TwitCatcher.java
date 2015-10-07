package ru.fizteh.fivt.students.xmanatee.TwitCatcher;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.*;
import java.util.Properties;
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
            System.err.println("Failed to retweet: " + te.getMessage());
            System.exit(-1);
        }
    }

    public static ConfigurationBuilder getOAuthConfigurationBuilder() {
        ConfigurationBuilder cb = new ConfigurationBuilder();

        Properties prop = new Properties();

        try (InputStream input = new FileInputStream(".properties")) {
            prop.load(input);
        } catch (FileNotFoundException e) {
            System.err.println("Problems finding file : " + e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Problems reading file : " + e.getMessage());
            System.exit(1);
        }


        String consumerKey = prop.getProperty("consumerKey");
        String consumerSecret = prop.getProperty("consumerSecret");
        String accessToken = prop.getProperty("accessToken");
        String accessTokenSecret = prop.getProperty("accessTokenSecret");

        cb.setDebugEnabled(false);
        cb.setPrettyDebugEnabled(false);
        cb.setOAuthConsumerKey(consumerKey);
        cb.setOAuthConsumerSecret(consumerSecret);
        cb.setOAuthAccessToken(accessToken);
        cb.setOAuthAccessTokenSecret(accessTokenSecret);

        return cb;
    }
}

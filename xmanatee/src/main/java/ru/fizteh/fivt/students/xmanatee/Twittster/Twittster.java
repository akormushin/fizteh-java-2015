package ru.fizteh.fivt.students.xmanatee.Twittster;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.*;
import java.util.*;

import static java.lang.Thread.sleep;

public class Twittster {
    public static final int MAX_NUMBER_OF_TRIES = 5;
    public static final int MAGIC_NUMBER = 100;

    public static void main(String[] args) {
        System.out.println(ANSI_YELLOW + "YOU'RE RUNNING TWITTSTER" + ANSI_RESET);
        Parameters parameters = new Parameters(args);

        if (parameters.isHelp()) {
            parameters.runHelp();
            System.exit(0);
        }

        try {
            if (parameters.isStream()) {
                runStreamer(parameters);
            } else {
                runSearch(parameters);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }

    public static void runSearch(Parameters parameters) throws Exception {
        ConfigurationBuilder cb = getOAuthConfigurationBuilder();
        Twitter twitter = new TwitterFactory(cb.build()).getInstance();
        Query query = composeQuery(parameters);


        QueryResult result = null;

        Integer tryNumber = 0;
        while (tryNumber < MAX_NUMBER_OF_TRIES) {
            tryNumber++;
            try {
                result = twitter.search(query);
                break;
            } catch (TwitterException e) {
                System.out.println("Problems with searching : " + e.getMessage());
            }
        }
        if (tryNumber == MAX_NUMBER_OF_TRIES) {
            throw new Exception("Can't search... Check your keys or something");
        }

        List<Status> tweets = result.getTweets();
        if (tweets.size() == 0) {
            System.out.println("No results");
        } else {
            for (Status tweet : tweets) {
                displayTweet(tweet, true);
            }
        }
    }

    static final int DELAY_X = 1000;
    public static void runStreamer(Parameters parameters) {
        ConfigurationBuilder cb = getOAuthConfigurationBuilder();
        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();

        StatusAdapter listener = new StatusAdapter() {
            @Override
            public void onStatus(Status status) {
                displayTweet(status, false);
                try {
                    Thread.sleep(DELAY_X);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
            @Override
            public void onException(Exception ex) {
                System.out.println("Problems listening : " + ex.getMessage());
            }
        };

        twitterStream.addListener(listener);
        FilterQuery filter = composeFilter(parameters);
        twitterStream.filter(filter);

        try (Scanner scan = new Scanner(System.in)) {
            while (scan.hasNextLine()) {
                sleep(DELAY_X);
            }
        } catch (InterruptedException e) {
            System.out.println("Ctrl+D => shutting down Twittster");
            twitterStream.cleanUp();
            twitterStream.shutdown();
        }
    }

    public static Query composeQuery(Parameters parameters) {
        Query query = new Query();
        String queryText = parameters.getQuery();
        query.setCount(parameters.getLimit());
        if (parameters.noRetweets()) {
            queryText += " +exclude:retweets";
        }
        query.setQuery(queryText);
        if (!parameters.getPlace().isEmpty()) {
            GoogleFindPlace gfm = new GoogleFindPlace(parameters.getPlace());

            GeoLocation gl = new GeoLocation(gfm.getLocation().lat, gfm.getLocation().lng);
            query.setGeoCode(gl, gfm.getRadius(), Query.Unit.km);
        }
        return query;
    }

    public static FilterQuery composeFilter(Parameters parameters) {
        FilterQuery filter = new FilterQuery();
        filter.track(parameters.getQuery());

        if (!parameters.getPlace().isEmpty()) {
            try {
                GoogleFindPlace gmp = new GoogleFindPlace(parameters.getPlace());
                filter.locations(gmp.getBounds());
            } catch (Exception e) {
                System.out.println("Problems with finding the place ...");
//                System.out.println(ANSI_RED + "LALKA" + e.getMessage() + ANSI_RESET);
                //e.printStackTrace();
                System.exit(1);
            }
        }
        return filter;
    }

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";

    public static final Integer RT_PREFIX_LENGTH = 3;
    public static void displayTweet(Status tweet, boolean showTime) {
        String formattedTweet = "";
        String tweetText = tweet.getText();
        if (showTime) {
            formattedTweet += "[" + new AdvTimeParser(tweet.getCreatedAt()).get() + "] ";
        }
        formattedTweet += "@" + tweet.getUser().getScreenName() + ": ";
        if (tweet.isRetweet()) {
            formattedTweet += "ретвитнул ";
            tweetText = tweetText.substring(RT_PREFIX_LENGTH);
        } else {
            Integer retweetedCount = tweet.getRetweetCount();
            if (retweetedCount > 0) {
                Word4declension retweetWord = new Word4declension("ретвит", "ретвита", "ретвитов");
                tweetText += ANSI_GREEN + " (" + retweetedCount + " "
                        + retweetWord.declension4Number(retweetedCount) + ")" + ANSI_RESET;
            }
        }
        formattedTweet += tweetText;
        formattedTweet = formattedTweet.replaceAll("@(\\w+): ", ANSI_BLUE + "@$1" + ANSI_RESET + ": ");


        formattedTweet += ANSI_YELLOW + "." + ANSI_RESET;
        System.out.println(formattedTweet);
    }

    public static ConfigurationBuilder getOAuthConfigurationBuilder() {
        ConfigurationBuilder cb = new ConfigurationBuilder();

        Properties prop = new Properties();

        try (InputStream input = new FileInputStream("mykeys.properties")) {
            prop.load(input);
        } catch (FileNotFoundException e) {
            System.out.println("Problems finding file : " + e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Problems reading file : " + e.getMessage());
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

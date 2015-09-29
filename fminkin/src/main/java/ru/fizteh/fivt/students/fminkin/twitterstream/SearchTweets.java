package ru.fizteh.fivt.students.fminkin.twitterstream;

import twitter4j.*;
import twitter4j.GeoLocation;

import java.util.List;

/*
 * Created by Федор on 23.09.2015.
*/

public class SearchTweets {
    public static final Integer DIAGONAL_BOX_NUMBER = 4;
    public static final Integer SLEEP_TIME = 1000;
    public static final Integer SYMBOLS_BEFORE_NAME = 3;
    public static final Integer RAD = 5;
    public static final String METRIC_CHAR = "km";

    public static void handleStream(JCommanderConfig jcc, Location curLoc) {
        twitter4j.TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(new StatusAdapter() {
            @Override
            public void onStatus(Status tweet) {
                if (jcc.isRetweetsHidden() && tweet.isRetweet()) {
                    return;
                }
                printTweet(tweet);
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                System.err.println("Tweet Listener exception");
            }
        }
    });
        double lat1 = curLoc.getLatitude() - DIAGONAL_BOX_NUMBER;
        double lat2 = curLoc.getLatitude() + DIAGONAL_BOX_NUMBER;
        double lng1 = curLoc.getLongitude() - DIAGONAL_BOX_NUMBER;
        double lng2 = curLoc.getLongitude() + DIAGONAL_BOX_NUMBER;
        double[][] loc = {{lng1, lat1}, {lng2, lat2}};
        twitterStream.filter(new FilterQuery().track(jcc.getQueries().toArray(
                new String[jcc.getQueries().size()])).locations(loc));
    }

    public static void printTweet(Status tweet) {
        System.out.print("@" + tweet.getUser().getScreenName() + ": ");
        if (!tweet.isRetweet()) {
            System.out.print(tweet.getText());
            Integer n = tweet.getRetweetCount();
            if (n != 0) {
                System.out.println("(" + n + " " + RussianDeclense.getRetweet(n) + ")");
            } else {
                System.out.println();
            }
        } else {
            System.out.print("ретвитнул ");
            String[] tokens = tweet.getText().split(":"); //tokens[0] contains name RT @NICK
            System.out.println(tokens[0].substring(SYMBOLS_BEFORE_NAME)
                    + tweet.getText().substring(tokens[0].length())); //3 symbols
        }
        for (int i = 0; i < TwitterStream.MINUSES_COUNT; ++i) {
            System.out.print("-");
        }
        System.out.println();
    }
    public static void search(JCommanderConfig jcc, Location curLoc) {
        Twitter twitter = new TwitterFactory().getInstance();
        try {
            Query query = new Query(jcc.getQueries().toString()).geoCode(
                   new GeoLocation(curLoc.getLatitude(), curLoc.getLongitude()), RAD, METRIC_CHAR);
            query.setCount(jcc.getTweetsLimit());
            QueryResult result = twitter.search(query);
            List<Status> tweets = result.getTweets();
            if (tweets.isEmpty()) {
                System.out.println("Нет результатов");
                for (int i = 0; i < TwitterStream.MINUSES_COUNT; ++i) {
                    System.out.print("-");
                }
                System.out.println();
            }
            for (Status tweet : tweets) {
                if (!tweet.isRetweet() || !jcc.isRetweetsHidden()) {
                    System.out.print("[" + TimeAlign.printTime(
                            System.currentTimeMillis() - tweet.getCreatedAt().getTime()) + "] ");
                    printTweet(tweet);
                }
            }
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.err.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
    }
}

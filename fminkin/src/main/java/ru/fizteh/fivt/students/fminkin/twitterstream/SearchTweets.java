package ru.fizteh.fivt.students.fminkin.twitterstream;

import twitter4j.*;

import java.util.List;

/**
 * Created by Федор on 23.09.2015.
 */

public class SearchTweets {
    public static final Integer SLEEP_TIME = 1000;
    public static final Integer SYMBOLS_BEFORE_NAME = 3;
    public static final Integer RAD = 5;
    public static final String METRIC_CHAR = "km";

    static final double EARTH_RADIUS = 6371;
    static final double RADIANS_IN_DEGREE = Math.PI / 180;

    static double toRadians(double angle) {
        return angle * RADIANS_IN_DEGREE;
    }
    static double sphereDistance(double phi1, double lambda1, double phi2, double lambda2) {
        phi1 = toRadians(phi1);
        phi2 = toRadians(phi2);
        lambda1 = toRadians(lambda1);
        lambda2 = toRadians(lambda2);

        double deltaPhi = phi2 - phi1;
        double deltaLambda = lambda2 - lambda1;

        return 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(deltaPhi / 2), 2)
                + Math.cos(phi1) * Math.cos(phi2)
                * Math.pow(Math.sin(deltaLambda / 2), 2)))
                * EARTH_RADIUS;
    }
    public static void handleStream(JCommanderConfig jcc, Location curLoc) {
        twitter4j.TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(new StatusAdapter() {
            @Override
            public void onStatus(Status tweet) {
                if (jcc.isRetweetsHidden() && tweet.isRetweet()) {
                    return;
                }

                Location tweetLocation;
                if (tweet.getGeoLocation() != null) {
                    tweetLocation = new Location(tweet.getGeoLocation().getLatitude(),
                            tweet.getGeoLocation().getLongitude());
                } else {
                    return;
                }
                assert tweetLocation != null;

                if (sphereDistance(tweetLocation.getLatitude(),
                        tweetLocation.getLongitude(),
                        curLoc.getLatitude(),
                        curLoc.getLongitude()
                ) < RAD) {
                    printTweet(tweet);
                }

            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                System.err.println("Tweet Listener exception");
            }
        }
    });

        twitterStream.filter(new FilterQuery().track(jcc.getQueries().toArray(
                new String[jcc.getQueries().size()])));
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

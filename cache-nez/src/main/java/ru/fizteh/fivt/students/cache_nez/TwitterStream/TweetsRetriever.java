package ru.fizteh.fivt.students.cache_nez.TwitterStream;

import twitter4j.*;

import java.util.List;
import java.util.concurrent.CompletionException;

/**
 * Created by cache-nez on 9/23/15.
 */


public class TweetsRetriever {
    public static final boolean STREAM_MODE_OFF = false;
    private static final double RADIUS = 10;
    public static final int STOP_TRY_TO_RECONNECT = 5;
    public static final long RECONNECTION_SLEEP_TIME = 1000L;

    public static void getTweets(ParseArguments description) {
            Twitter twitter = new TwitterFactory().getInstance();
            Query query = new Query(description.getQuery());
            int limit = description.getTweetsLimit();
            query.setCount(limit);
            GeoPosition position = null;
            try {
                if (description.getLocation().equals("nearby")) {
                    position = GeoLocater.getLocationByIP();
                } else {
                    position = GeoLocater.getLocation(description.getLocation());
                }
            } catch (GeoException e) {
                System.err.println(e.getMessage() + "; let's go without place parameter");
            }
            GeoLocation location = new GeoLocation(position.getLatitude(), position.getLongitude());
            query.setGeoCode(location, RADIUS, Query.Unit.km);
            QueryResult result = null;
            List<Status> tweets;
            do {
                int tries = 0;
                boolean successed = false;
                while (!successed) {
                    try {
                        result = twitter.search(query);
                        successed = true;
                    } catch (TwitterException e) {
                        ++tries;
                        if (tries == STOP_TRY_TO_RECONNECT) {
                            throw new CompletionException("Failed to retrieve tweets, "
                                    + "most probably because of connection error", e);
                        }
                        try {
                            Thread.sleep(RECONNECTION_SLEEP_TIME);
                        } catch (InterruptedException sleepEx) {
                            sleepEx.printStackTrace();
                        }
                    }
                }
                tweets = result.getTweets();
                if (tweets.size() > 0) {
                    for (Status status : tweets) {
                        if (status.isRetweet()) {
                            if (!description.doHideRetweets()) {
                                System.out.println(TextFormatter.getRetweetText(status, STREAM_MODE_OFF));
                                System.out.println(TextFormatter.getSeparator());
                                --limit;
                            }
                        } else {
                            System.out.println(TextFormatter.getTweetText(status, STREAM_MODE_OFF));
                            System.out.println(TextFormatter.getSeparator());
                            --limit;
                        }
                    }
                } else {
                    System.out.println("No tweets found");
                    return;
                }
                query = result.nextQuery();
            } while (limit > 0 && query != null);
    }
}


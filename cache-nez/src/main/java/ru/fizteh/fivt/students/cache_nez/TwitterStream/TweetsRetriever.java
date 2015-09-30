package ru.fizteh.fivt.students.cache_nez.TwitterStream;

import twitter4j.*;

import java.io.IOException;
import java.util.List;

/**
 * Created by cache-nez on 9/23/15.
 */


public class TweetsRetriever {
    public static final boolean STREAM_MODE_OFF = false;
    private static final double RADIUS = 10;

    public static void getTweets(ParseArguments description) throws TwitterException, IOException, GeoException {
        Twitter twitter = new TwitterFactory().getInstance();
        Query query = new Query(description.getQuery());
        int limit = description.getTweetsLimit();
        query.setCount(limit);
        if (!description.getLocation().equals("everywhere")) {
            GeoPosition position = GeoLocater.getLocation(description.getLocation());
            GeoLocation location = new GeoLocation(position.getLatitude(), position.getLongitude());
            query.setGeoCode(location, RADIUS, Query.Unit.km);
        }
        QueryResult result;
        List<Status> tweets;
        do {
            result = twitter.search(query);
            tweets = result.getTweets();
            if (tweets.size() > 0) {
                for (Status status : tweets) {
                    if (status.isRetweet()) {
                        if (!description.doHideRetweets()) {
                            System.out.println(TextFormatter.getRetweetText(status, STREAM_MODE_OFF));
                            --limit;
                        }
                    } else {
                        System.out.println(TextFormatter.getTweetText(status, STREAM_MODE_OFF));
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


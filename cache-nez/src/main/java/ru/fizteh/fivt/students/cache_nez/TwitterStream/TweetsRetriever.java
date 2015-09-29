package ru.fizteh.fivt.students.cache_nez.TwitterStream;

import twitter4j.*;
import java.util.List;

/**
 * Created by cache-nez on 9/23/15.
 */


public class TweetsRetriever {
    public static final boolean STREAM_MODE_OFF = false;

    public static void getTweets(String searchFor, int limit, boolean hideRetweets) throws TwitterException {
        Twitter twitter = new TwitterFactory().getInstance();
        Query query = new Query(searchFor);
        //GeoQuery query = new GeoQuery("93.175.2.172");
        query.setQuery(searchFor);
        query.setCount(limit);
        QueryResult result;
        List<Status> tweets;
        do {
            result = twitter.search(query);
            tweets = result.getTweets();
            if (tweets.size() > 0) {
                for (Status status : tweets) {
                    if (status.isRetweet()) {
                        if (!hideRetweets) {
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


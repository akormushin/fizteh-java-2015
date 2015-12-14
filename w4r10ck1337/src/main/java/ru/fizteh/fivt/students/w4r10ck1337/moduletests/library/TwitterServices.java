package ru.fizteh.fivt.students.w4r10ck1337.moduletests.library;

import twitter4j.Query;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Created by user on 30.11.2015.
 */
public class TwitterServices {
    private static final int Q_LIMIT = 100;

    public static List<Status> getTweets(Twitter twitter, Query searchQuery, int minCount) {
        List<Status> tweets = null;
        boolean success = false;
        while (!success) {
            try {
                tweets = twitter.search(searchQuery).getTweets();
                success = tweets.size() >= minCount;
            } catch (TwitterException e) { }
        }
        return tweets;
    }

    public static void streamTweets(String query, String place, Twitter twitter, int limit, Consumer<Status> printer) {
        Query searchQuery = QueryCreator.createQuery(query, 1, QueryCreator.findLocation(place, twitter));
        long prev = System.currentTimeMillis();
        while (limit > 0) {
            List<Status> tweets = getTweets(twitter, searchQuery, 1);
            try {
                Thread.sleep(Math.max(TimeUnit.SECONDS.toMillis(1) - System.currentTimeMillis() + prev, 0));
            } catch (InterruptedException e) { }
            printer.accept(tweets.get(0));
            searchQuery.setSinceId(tweets.get(0).getId());
            prev = System.currentTimeMillis();
            limit--;
        }
    }

    public static void printTweets(String query, String place, Twitter twitter, int limit, Consumer<Status> printer) {
        Query searchQuery = QueryCreator.createQuery(query, limit, QueryCreator.findLocation(place, twitter));
        while (limit > 0) {
            searchQuery.setCount(Math.min(Q_LIMIT, limit));
            limit -= Q_LIMIT;
            List<Status> tweets = getTweets(twitter, searchQuery, 0);
            if (tweets.isEmpty()) {
                return;
            }
            for (Status tweet : tweets) {
                printer.accept(tweet);
            }
            searchQuery.setMaxId(tweets.get(tweets.size() - 1).getId() - 1);
        }
    }
}

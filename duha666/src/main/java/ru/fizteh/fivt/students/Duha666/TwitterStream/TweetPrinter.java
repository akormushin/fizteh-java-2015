package ru.fizteh.fivt.students.duha666.TwitterStream;

import twitter4j.*;
import java.util.List;

public class TweetPrinter {
    public static final int SEARCH_DISTANCE = 5;
    public static final int MILLISECONDS_IN_SECOND = 1000;
    private static void printTweet(Status tweet) {
        System.out.print("[" + TimeFormatter.formatTime(tweet.getCreatedAt()) + "]");
        System.out.print("@" + tweet.getUser().getScreenName() + ": ");
        if (tweet.isRetweet()) {
            System.out.print("ретвитнул ");
            System.out.print(tweet.getRetweetedStatus().getUser().getScreenName() + ":");
            System.out.print(tweet.getRetweetedStatus().getText());
        } else {
            System.out.print(tweet.getText());
            int retweetsCount = tweet.getRetweetCount();
            if (retweetsCount > 0) {
                System.out.print("(" + retweetsCount + " ретвитов)");
            }
        }
        System.out.print(TwitterStream.TWEET_SEPARATOR);
    }
    public static void printTweets(JCommanderSettings jcs, GeoLocation location) throws TwitterException {
        Twitter twitter = new TwitterFactory().getInstance();
        Query query = new Query(jcs.getQuery()).geoCode(location, SEARCH_DISTANCE, "km");
        query.setCount(jcs.getLimitNumber());
        QueryResult queryResult = twitter.search(query);
        List<Status> tweets = queryResult.getTweets();
        tweets.stream().
                filter(tweet -> !jcs.isHideRetweets() || !tweet.isRetweet()).
                forEach(ru.fizteh.fivt.students.duha666.TwitterStream.TweetPrinter::printTweet);
    }
    public static void streamTweets(JCommanderSettings jcs, GeoLocation location) throws TwitterException {
        twitter4j.TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(new StatusAdapter() {
            @Override
            public void onStatus(Status tweet) {
                if (jcs.isHideRetweets() && tweet.isRetweet()) {
                    return;
                }
                printTweet(tweet);
                try {
                    Thread.sleep(MILLISECONDS_IN_SECOND);
                } catch (Exception e) {
                    System.err.print("Some error occured");
                }
            }
        });
        FilterQuery filterQuery = new FilterQuery();
        double[][] locations = {{location.getLongitude() - SEARCH_DISTANCE,
                location.getLatitude() - SEARCH_DISTANCE},
                {location.getLongitude() + SEARCH_DISTANCE,
                        location.getLatitude() + SEARCH_DISTANCE}};
        filterQuery.track(new String[]{jcs.getQuery()}).locations(locations);
        twitterStream.filter(filterQuery);
    }
}

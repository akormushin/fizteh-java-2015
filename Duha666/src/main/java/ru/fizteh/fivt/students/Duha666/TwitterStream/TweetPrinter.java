package ru.fizteh.fivt.students.Duha666.TwitterStream;

import com.sun.xml.internal.fastinfoset.util.StringArray;
import twitter4j.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Filter;

public class TweetPrinter {
    public static final int SEARCH_DISTANCE = 4;
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
        System.out.print(TwitterStream.tweetSeparator);
    }
    public static void printTweets(JCommanderSettings jcs, GeoLocation location) throws TwitterException {
        Twitter twitter = new TwitterFactory().getInstance();
        Query query = new Query(jcs.getQuery()).geoCode(location, 5, "km");
        query.setCount(jcs.getLimitNumber());
        QueryResult queryResult = twitter.search(query);
        List<Status> tweets = queryResult.getTweets();
        tweets.stream().
                filter(tweet -> !jcs.isHideRetweets() || !tweet.isRetweet()).
                forEach(ru.fizteh.fivt.students.Duha666.TwitterStream.TweetPrinter::printTweet);
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
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.print("Some error occured");
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

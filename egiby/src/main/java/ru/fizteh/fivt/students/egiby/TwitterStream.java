package ru.fizteh.fivt.students.egiby;

import com.beust.jcommander.JCommander;
import twitter4j.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by egiby on 24.09.15.
 */

public class TwitterStream {
    public static final int DEFAULT_NUMBER_OF_TWEETS = 100;

    public static void main(String[] args) {
        JCommanderParams jcp = new JCommanderParams();
        JCommander jcm = new JCommander(jcp, args);

        if (jcp.isHelp()) {
            printHelp(jcm);
        }

        if (jcp.isStream()) {
            if (jcp.getNumberTweets() != null) {
                printHelp(jcm);
            }
            getStream(jcp);
        } else {
            try {
                printAllTweets(jcp);
            } catch (TwitterException te) {
                te.printStackTrace();
            }
        }
    }

    private static void printHelp(JCommander jcm) {
        jcm.usage();
        System.exit(0);
    }

    private static void printAllTweets(JCommanderParams jcp) throws TwitterException {
        Twitter twitter = new TwitterFactory().getInstance();
        Query query = new Query(jcp.getKeyword());

        if (jcp.getLocation() != null) {
            LocationUtils.Location location = LocationUtils.getLocationByName(jcp.getLocation());
            query.setGeoCode(location.getCoordinates(), location.getRadius(), Query.KILOMETERS);
        }

        int numberOfTweets = 0;

        int limit = DEFAULT_NUMBER_OF_TWEETS;

        if (jcp.getNumberTweets() != null) {
            limit = jcp.getNumberTweets();
        }

        QueryResult result;
        while (numberOfTweets < limit && query != null) {
            result = twitter.search(query);

            List<Status> tweets = result.getTweets();
            for (Status tweet : tweets) {
                if (tweet.isRetweet() && jcp.isHideRetweets()) {
                    continue;
                }

                System.out.println(FormatUtils.formatTweet(tweet, false));

                numberOfTweets++;
                if (numberOfTweets == limit) {
                    return;
                }
            }

            query = result.nextQuery();
        }
    }

    private static void getStream(JCommanderParams jcp) {
        StatusAdapter listener = new StatusAdapter() {
            @Override
            public void onStatus(Status tweet) {
                if (jcp.isHideRetweets() && tweet.isRetweet()) {
                    return;
                }

                System.out.println(FormatUtils.formatTweet(tweet, true));

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }
        };

        twitter4j.TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(listener);

        FilterQuery filter = new FilterQuery(jcp.getKeyword());

        if (jcp.getLocation() != null) {
            filter.locations(LocationUtils.getLocationByName(jcp.getLocation()).getCoordinateBox());
        }

        twitterStream.filter(filter);
    }
}

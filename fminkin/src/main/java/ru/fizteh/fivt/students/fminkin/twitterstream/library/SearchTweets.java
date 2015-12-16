package ru.fizteh.fivt.students.fminkin.twitterstream.library;

import twitter4j.*;
import twitter4j.GeoLocation;
import twitter4j.TwitterStream;
import java.util.List;
import static java.util.stream.Collectors.toList;
import java.util.function.Consumer;

/*
 * Created by Федор on 23.09.2015.
*/

public class SearchTweets {
    public static final Integer DIAGONAL_BOX_NUMBER = 4;
    public static final Integer SLEEP_TIME = 0;
    public static final Integer RAD = 5;
    public static final String METRIC_CHAR = "km";

    public void handleStream(JCommanderConfig jcc, Location curLoc,
                             TwitterStream twitterStream,
                             Consumer<String> func) {
        twitterStream.addListener(new StatusAdapter() {
            @Override
            public void onStatus(Status tweet) {
                if (jcc.isRetweetsHidden() && tweet.isRetweet()) {
                    return;
                }
                func.accept(formatTweet(tweet));
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
    public static String formatTweet(Status tweet) {
        String s = "@" + tweet.getUser().getScreenName() + ": ";
        if (!tweet.isRetweet()) {
            s += tweet.getText();

            Integer n = tweet.getRetweetCount();
            if (n != 0) {
                s += "(" + n + " " + RussianDeclense.getRetweet(n) + ")";
            }
        } else {
            s += "ретвитнул ";
            s += tweet.getRetweetedStatus().getUser().getScreenName() + ": ";
            tweet = tweet.getRetweetedStatus();
            s += tweet.getText();
        }
        return s;
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
            System.out.print(tweet.getRetweetedStatus().getUser().getScreenName() + ": ");
            tweet = tweet.getRetweetedStatus();
            System.out.println(tweet.getText());

        }
        for (int i = 0; i < ru.fizteh.fivt.students.fminkin.twitterstream.TwitterStream.MINUSES_COUNT; ++i) {
            System.out.print("-");
        }
        System.out.println();
    }
    public List<Status> search(JCommanderConfig jcc, Location curLoc, Twitter twitter) throws TwitterException {
        Query query = new Query(jcc.getQueries().toString()).geoCode(
                new GeoLocation(curLoc.getLatitude(), curLoc.getLongitude()), RAD, METRIC_CHAR);
        query.setCount(jcc.getTweetsLimit());
        QueryResult result = twitter.search(query);
        List<Status> tweets = result.getTweets().stream()
                .filter(x -> (!jcc.isRetweetsHidden() || !x.isRetweet())).collect(toList());
        return tweets;
    }
}

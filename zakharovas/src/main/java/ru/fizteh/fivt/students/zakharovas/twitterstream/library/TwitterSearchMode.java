package ru.fizteh.fivt.students.zakharovas.twitterstream.library;

import ru.fizteh.fivt.students.zakharovas.twitterstream.CommandLineArgs;
import twitter4j.Query;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexander on 05.11.15.
 */
public class TwitterSearchMode {
    private Twitter twitter;
    private CommandLineArgs commandLineArgs;
    private GeoLocator geoLocator;

    public TwitterSearchMode(Twitter twitter, CommandLineArgs commandLineArgs, GeoLocator geoLocator) {
        this.twitter = twitter;
        this.commandLineArgs = commandLineArgs;
        this.geoLocator = geoLocator;
    }

    public void search() throws TwitterException, IllegalStateException {
        String search = String.join(" ", commandLineArgs.getStringForQuery());
        Query query = new Query(search);
        query.setCount(Integer.MAX_VALUE);
        query.geoCode(geoLocator.getLocationForSearch(), geoLocator.getRadius(), Query.KILOMETERS.toString());
        List<Status> tweets;
        while (true) {
            try {
                tweets = twitter.search(query).getTweets();
                break;
            } catch (TwitterException te) {
                if (te.isCausedByNetworkIssue()) {
                    System.err.println("Connection problem. Reconnecting");
                    try {
                        Thread.sleep(Numbers.SECOND);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    throw te;
                }
            }
        }
        List<Status> tweetsForOutput = new ArrayList<>();
        for (Status tweet : tweets) {
            if (tweetsForOutput.size() == commandLineArgs.getLimit()) {
                break;
            }
            if (!commandLineArgs.getHideRetweets() || !tweet.isRetweet()) {
                tweetsForOutput.add(tweet);
            }
        }
        if (tweetsForOutput.isEmpty()) {
            System.out.println("No tweets for this search has been found");
            return;
        }
        for (Status tweet : tweetsForOutput) {
            System.out.println(new TweetFormater(tweet).tweetForOutput());
        }

    }
}

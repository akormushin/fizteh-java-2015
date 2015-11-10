package ru.fizteh.fivt.students.zakharovas.twitterstream.library;

import ru.fizteh.fivt.students.zakharovas.twitterstream.CommandLineArgs;
import twitter4j.*;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by alexander on 05.11.15.
 */

public class TwitterStreamMode {
    private TwitterStream twitterStream;
    private CommandLineArgs commandLineArgs;
    private GeoLocator geoLocator;

    public TwitterStreamMode(TwitterStream twitterStream, CommandLineArgs commandLineArgs, GeoLocator geoLocator) {
        this.twitterStream = twitterStream;
        this.commandLineArgs = commandLineArgs;
        this.geoLocator = geoLocator;
    }

    public void startStreaming() {
        Queue<Status> tweetQueue = new LinkedList<>();
        StatusListener listener = new StatusAdapter() {
            @Override
            public void onStatus(Status tweet) {
                if (!commandLineArgs.getHideRetweets() || !tweet.isRetweet()) {
                    tweetQueue.add(tweet);
                }
            }
        };
        twitterStream.addListener(listener);
        FilterQuery query = new FilterQuery(String.join(" ", commandLineArgs.
                getStringForQuery()));
        query.locations(geoLocator.getLocationForStream());
        twitterStream.filter(query);
        while (true) {
            while (!tweetQueue.isEmpty()) {
                System.out.println(new TweetFormater(tweetQueue.poll()).tweetForOutputWithoutDate());
            }
            try {
                Thread.sleep(Numbers.SECOND);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

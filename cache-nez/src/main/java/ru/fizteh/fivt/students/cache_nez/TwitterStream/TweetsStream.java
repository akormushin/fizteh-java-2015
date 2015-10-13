package ru.fizteh.fivt.students.cache_nez.TwitterStream;

import com.beust.jcommander.JCommander;
import twitter4j.*;

import java.io.IOException;
import java.util.concurrent.CompletionException;


/**
 * Created by cache-nez on 9/27/15.
 */



public class TweetsStream {
    public static final int EXIT_FAILURE = 1;
    public static final boolean STREAM_MODE_ON = true;

    public static void twitterStream(ParseArguments description) {
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        StatusListener listener = new StatusAdapter() {
            @Override
            public void onStatus(Status status) {
                if (status.isRetweet()) {
                    if (!description.doHideRetweets()) {
                        System.out.println(TextFormatter.getRetweetText(status, STREAM_MODE_ON));
                        System.out.println(TextFormatter.getSeparator());
                    }
                } else {
                    System.out.println(TextFormatter.getTweetText(status, STREAM_MODE_ON));
                    System.out.println(TextFormatter.getSeparator());
                }
            }

        };
        String[] query = {description.getQuery()};
        FilterQuery filterQuery = new FilterQuery();
        filterQuery.track(query);
        try {
            if (description.getLocation().equals("nearby")) {
                filterQuery.locations(GeoLocater.getBoundingBoxByIP());
            } else {
                filterQuery.locations(GeoLocater.getBoundingBox(description.getLocation()));
            }
        } catch (GeoException e) {
            System.err.println(e.getMessage() + "; let's go without place parameter");
        }
        twitterStream.addListener(listener);
        twitterStream.filter(filterQuery);
    }


    public static void main(String[] args) throws InterruptedException, IOException, GeoException {
        ParseArguments parseArgs = new ParseArguments();
        JCommander jCommander;
        try {
            jCommander =  new JCommander(parseArgs, args);
        } catch (Exception wrongArgs) {
            System.err.println("Wrong arguments: " + wrongArgs.getMessage());
            jCommander = new JCommander(parseArgs);
            jCommander.setProgramName("TweetsStream");
            jCommander.usage();
            System.exit(EXIT_FAILURE);
        }
        if (parseArgs.doShowHelp()) {
            jCommander.setProgramName("TwitterStream");
            jCommander.usage();
            return;
        }

        if (parseArgs.isStreamMode()) {
            twitterStream(parseArgs);
            return;
        }

        try {
            TweetsRetriever.getTweets(parseArgs);
        } catch (CompletionException e) {
            System.err.println("Error: " +  e.getMessage());
            System.exit(EXIT_FAILURE);
        }
    }
}

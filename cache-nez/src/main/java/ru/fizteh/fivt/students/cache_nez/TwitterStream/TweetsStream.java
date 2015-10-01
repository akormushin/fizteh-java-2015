package ru.fizteh.fivt.students.cache_nez.TwitterStream;

import com.beust.jcommander.JCommander;
import twitter4j.*;

import java.io.IOException;


/**
 * Created by cache-nez on 9/27/15.
 */



public class TweetsStream {
    public static final int EXIT_FAILURE = 1;
    public static final boolean STREAM_MODE_ON = true;

    public static void twitterStream(ParseArguments description) throws IOException, GeoException {
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        StatusListener listener = new StatusAdapter() {
            @Override
            public void onStatus(Status status) {
                if (status.isRetweet()) {
                    if (!description.doHideRetweets()) {
                        System.out.println(TextFormatter.getRetweetText(status, STREAM_MODE_ON));
                    }
                } else {
                    System.out.println(TextFormatter.getTweetText(status, STREAM_MODE_ON));
                }
            }

        };
        String[] query = {description.getQuery()};
        FilterQuery filterQuery = new FilterQuery();
        filterQuery.track(query);
        if (!description.getLocation().equals("everywhere")) {
            filterQuery.locations(GeoLocater.getBoundingBox(description.getLocation()));
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
        } catch (TwitterException | GeoException | IOException e) {
            System.err.println("Error: " +  e.getMessage());
            e.printStackTrace();
            System.exit(EXIT_FAILURE);
        }
    }
}

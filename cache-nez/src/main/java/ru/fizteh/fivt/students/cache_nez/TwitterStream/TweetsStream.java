package ru.fizteh.fivt.students.cache_nez.TwitterStream;

import com.beust.jcommander.JCommander;
import twitter4j.*;


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
                    }
                } else {
                    System.out.println(TextFormatter.getTweetText(status, STREAM_MODE_ON));
                }
            }

        };
        String[] query = {description.getQuery()};
        FilterQuery filter = new FilterQuery();
        filter.track(query);
        twitterStream.addListener(listener);
        twitterStream.filter(filter);
    }


    public static void main(String[] args) throws InterruptedException {
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
            TweetsRetriever.getTweets(parseArgs.getQuery(), parseArgs.getTweetsLimit(), parseArgs.doHideRetweets());
        } catch (TwitterException e) {
            System.err.println("Error " + e.getErrorCode() + ": " +  e.getErrorMessage());
            System.exit(EXIT_FAILURE);
        }
    }
}

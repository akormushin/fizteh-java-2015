package ru.fizteh.fivt.students.cache_nez.TwitterStream;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import twitter4j.TwitterException;


/**
 * Created by cache-nez on 9/27/15.
 */



public class TwitterStream {
    private static final int DEFAULT_LIMIT = 100;

    @Parameter(names = {"--query", "-q"}, description = "Set keywords to find.")
    private String query = "физтех";

    @Parameter(names = {"--place", "-p"}, description = "Specify tweets location.")
    private  String location;

    @Parameter(names = {"--stream", "-s"},
            description = "Determines if the stream mode is on: print a bunch of tweets every second. "
                        + "Limit parameter is ignored.")
    private boolean streamMode = false;

    @Parameter(names = "--hideRetweets", description = "Show original tweets only.")
    private boolean hideRetweets = false;

    @Parameter(names = {"--limit", "-l"}, description = "Specify a number of tweets to show.",
            validateWith = IntegerValidator.class)
    private int tweetsLimit = DEFAULT_LIMIT;

    @Parameter(names = {"--help", "-h"}, help = true, description = "Display help message.")
    private boolean showHelp = false;


    public static void main(String[] args) {
        TwitterStream parseArgs = new TwitterStream();
        JCommander jCommander = null;
        try {
            jCommander =  new JCommander(parseArgs, args);
        } catch (Exception wrongArgs) {
            System.err.println("Wrong arguments: " + wrongArgs.getMessage());
            return;
        }
        if (parseArgs.showHelp) {
            jCommander.setProgramName("TwitterStream");
            jCommander.usage();
            return;
        }

        try {
            TweetsRetriever.getTweets(parseArgs.query, parseArgs.tweetsLimit, parseArgs.hideRetweets);
        } catch (TwitterException e) {
            System.err.println("Error " + e.getErrorCode() + ": " +  e.getErrorMessage());
        }
    }
}

package ru.fizteh.fivt.students.cache_nez.TwitterStream;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cache-nez on 9/27/15.
 */
public class TwitterStream {
    private static final int DEFAULT_LIMIT = 10;

    @Parameter
    private List<String> parameters = new ArrayList<>();

    @Parameter(names = {"--query", "-q"}, description = "Keywords to find")
    private String query = "физтех";

    @Parameter(names = {"--place", "-p"}, description = "location, used to search tweets")
    private  String location;

    @Parameter(names = {"--stream", "-s"}, description = "stream mode on: display a bunch of tweets every second")
    private boolean streamMode = false;

    @Parameter(names = "hideRetweets", description = "show original tweets only")
    private boolean hideRetweets = false;

    @Parameter(names = {"--limit", "-l"}, description = "number of tweets to show")
    private int tweetsLimit = DEFAULT_LIMIT;

    @Parameter(names = {"--help", "-h"}, help = true, description = "display help message")
    private boolean showHelp = false;


    public String getQuery() {
        return query;
    }

    public String getLocation() {
        return location;
    }

    public boolean isStreamMode() {
        return streamMode;
    }

    public boolean isHideRetweets() {
        return hideRetweets;
    }

    public int getTweetsLimit() {
        return tweetsLimit;
    }

    public boolean isShowHelp() {
        return showHelp;
    }

    public static void main(String[] args) {
        TwitterStream parseArgs = new TwitterStream();
        /*TODO: try-catch block for arguments parsing */
        JCommander jCommander = null;
        try {
            jCommander =  new JCommander(parseArgs, args);
        } catch (Exception wrongArgs) {
            System.err.println("Wrong arguments");
        }
        if (parseArgs.isShowHelp()) {
            assert jCommander != null;
            jCommander.setProgramName("JCommanderForTwitterStream");
            jCommander.usage();
            return;
        }

        try {
            TweetsRetriever.getTweets(parseArgs.getQuery(), parseArgs.getTweetsLimit());
        } catch (TwitterException e) {
            System.err.println("Error " + e.getErrorCode() + ": " +  e.getErrorMessage());
        }
    }
}

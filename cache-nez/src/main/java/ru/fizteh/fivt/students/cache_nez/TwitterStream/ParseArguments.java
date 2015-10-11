package ru.fizteh.fivt.students.cache_nez.TwitterStream;

import com.beust.jcommander.Parameter;

/**
 * Created by cache-nez on 9/28/15.
 */

public class ParseArguments {
    private static final int DEFAULT_LIMIT = 100;

    @Parameter(names = {"--query", "-q"}, description = "Set keywords to find.")
    private String query = "физтех";

    @Parameter(names = {"--place", "-p"}, description = "Specify tweets location.")
    private  String location = "nearby";

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

    public String getQuery() {
        return query;
    }

    public String getLocation() {
        return location;
    }

    public boolean isStreamMode() {
        return streamMode;
    }

    public boolean doHideRetweets() {
        return hideRetweets;
    }

    public int getTweetsLimit() {
        return tweetsLimit;
    }

    public boolean doShowHelp() {
        return showHelp;
    }
}

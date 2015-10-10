package ru.fizteh.fivt.students.riazanovskiy.TwitterStream;

import com.beust.jcommander.Parameter;

class ArgumentParser {
    @Parameter(names = {"--query", "-q"}, description = "Search query")
    private String keywords = "";

    @Parameter(names = {"--place", "-p"}, description = "Tweets from this location")
    private String location = "";

    @Parameter(names = {"--stream", "-s"}, description = "Activates stream mode. Tweets are printed continuously")
    private boolean stream;

    @Parameter(names = "--hideRetweets", description = "Hides retweets if set")
    private boolean hideRetweets;

    @Parameter(names = {"--limit", "-l"}, description = "Maximum number of tweets to show."
            + " Actual number of printed tweets may be less. Incompatible with --stream")
    private Integer limit = null;

    @Parameter(names = {"--help", "-h"}, help = true, description = "Displays this help and exits")
    private boolean help;

    public String getKeywords() {
        return keywords;
    }

    public String getLocation() {
        return location;
    }

    public boolean isStream() {
        return stream;
    }

    public boolean isShowRetweets() {
        return !hideRetweets;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public boolean isHelp() {
        return help;
    }
}

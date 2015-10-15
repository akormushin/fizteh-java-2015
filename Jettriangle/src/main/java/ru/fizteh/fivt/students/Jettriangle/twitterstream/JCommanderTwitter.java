package ru.fizteh.fivt.students.Jettriangle.twitterstream;

/**
 * Created by rtriangle on 09.10.15.
 */

import com.beust.jcommander.Parameter;

public class JCommanderTwitter {

    static final int DEFAULT_LIMIT = 18;

    @Parameter(names = { "--query", "-q"}, description = "Level of verbosity")
    private String query = "";

    @Parameter(names = {"--place", "-p"}, description = "Place")
    private String place = "nearby";

    @Parameter(names = {"--stream", "-s"}, description = "Stream")
    private boolean stream = false;

    @Parameter(names = "--hideRetweets", description = "Hide Retweets")
    private boolean hideRetweets = false;

    @Parameter(names = {"--help", "-h"}, description = "Help")
    private boolean help = false;

    public String getQuery() {
        return query;
    }

    public String getPlace() {
        return place;
    }

    public boolean isStream() {
        return stream;
    }

    public boolean isHideRetweets() {
        return hideRetweets;
    }

    public boolean isHelp() {
        return help;
    }

    public Integer getTweetsLimit() {
        Integer tweetsLimit = DEFAULT_LIMIT;
        return tweetsLimit;
    }
}

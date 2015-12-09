package ru.fizteh.fivt.students.loulett.TwitterStream;

import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

public class JCommanderPar {
    static final String NEARBY = "nearby";

    @Parameter(names = {"--query", "-q"},
            description = "Queries for searching tweets (this argument is requied)",
            variableArity = true)
    private List<String> queries = new ArrayList<>();

    @Parameter(names = {"--place", "-p"}, description = "Place for searching tweets")
    private String place = NEARBY;

    @Parameter(names = {"--stream", "-s"}, description = "Enable stream")
    private boolean stream = false;

    @Parameter(names = {"--hideRetweets"}, description = "Hide retweets")
    private boolean retweet = false;

    @Parameter(names = {"--limit", "-l"}, description = "Limit of tweets. Not usable in stream mode")
    private Integer limit = Integer.MAX_VALUE;

    @Parameter(names = {"--help", "-h"}, description = "Show help", help = true)
    private boolean help;

    public String getQueries() {
        return String.join(" ", queries);
    }

    public boolean isStream() {
        return stream;
    }

    public boolean isHideRetweets() {
        return retweet;
    }

    public Integer getLimit() {
        return limit;
    }

    public boolean isHelp() {
        return help;
    }
}

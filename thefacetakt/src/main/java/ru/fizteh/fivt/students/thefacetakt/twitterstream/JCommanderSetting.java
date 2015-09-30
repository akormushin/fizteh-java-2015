package ru.fizteh.fivt.students.thefacetakt.twitterstream;

import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by thefacetakt on 23.09.15.
 */
class JCommanderSetting {

    static final String DEFAULT_LOCATION = "nearby";

    @Parameter(names = {"--query", "-q"},
            description = "query or keywords for stream (required)",
            variableArity = true,
            required = true)
    private List<String> queries = new ArrayList<String>(
            Arrays.asList("thefacetakt"));

    @Parameter(names = {"--place", "-p"}, description = "location")
    private String location = DEFAULT_LOCATION;

    @Parameter(names = {"--stream", "-s"},
            description = "is stream enabled")
    private boolean stream = false;

    @Parameter(names = {"--hideRetweets"}, description = "hide retweets")
    private boolean hideRetweets = false;

    @Parameter(names = {"--limit", "-l"}, description = "limit of tweets")
    private Integer limit = Integer.MAX_VALUE;

    @Parameter(names = {"--help", "-h"},
            description = "show help", help = true)
    private boolean help;

    public List<String> getQueries() {
        return queries;
    }

    public String getLocation() {
        return location;
    }

    public boolean isStream() {
        return stream;
    }

    public boolean isHideRetweets() {
        return hideRetweets;
    }

    public Integer getLimit() {
        return limit;
    }

    public boolean isHelp() {
        return help;
    }
}

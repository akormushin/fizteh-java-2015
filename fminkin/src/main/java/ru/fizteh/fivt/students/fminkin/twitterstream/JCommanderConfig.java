package ru.fizteh.fivt.students.fminkin.twitterstream;

import com.beust.jcommander.Parameter;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Федор on 23.09.2015.
 */
public class JCommanderConfig {
    static final String DEFAULT_LOCATION = "nearby";
    @Parameter(names = {"--query", "-q"}, description = "query or keywords for stream", variableArity = true)
    private List<String> queries = new ArrayList<>();

    @Parameter(names = { "--place", "-p" }, description = "Level of verbosity")
    private String location = DEFAULT_LOCATION;

    @Parameter(names = { "--stream", "-s"}, description = "is stream-mode enabled")
    private boolean stream = false;

    @Parameter(names = "--hideRetweets", description = "is retweets hidden")
    private boolean retweetsHidden = false;

    @Parameter(names = { "--limit", "-l"}, description = "tweet limit")
    private Integer tweetsLimit = Integer.MAX_VALUE;

    @Parameter(names = { "--help", "-h"}, description = "show help")
    private boolean help = false;

    public final boolean isHelp() {
        return help;
    }

    public final List<String> getQueries() {
        return queries;
    }

    public final String getLocation() {
        return location;
    }

    public final boolean isStream() {
        return stream;
    }

    public final boolean isRetweetsHidden() {
        return retweetsHidden;
    }

    public final Integer getTweetsLimit() {
        return tweetsLimit;
    }
}

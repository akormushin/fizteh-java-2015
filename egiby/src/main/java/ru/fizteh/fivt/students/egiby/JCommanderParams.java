package ru.fizteh.fivt.students.egiby;

import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by egiby on 29.09.15.
 */
public class JCommanderParams {
    private static final Integer DEFAULT_NUMBER_OF_TWEETS = 100;
    @Parameter
    private List<String> parameters = new ArrayList<>();

    @Parameter(names = {"--query", "-q"}, description = "Query or keywords for stream")
    private String keyword = new String();

    @Parameter(names = {"--stream", "-s"}, description = "Is stream")
    private boolean stream = false;

    @Parameter(names = "--hideRetweets", description = "Is hiding retweets")
    private boolean hideRetweets = false;

    @Parameter(names = {"--limit", "-l"}, description = "Max number of tweets if not stream")
    private Integer limit = DEFAULT_NUMBER_OF_TWEETS;

    @Parameter(names = {"--help", "-h"}, description = "Get help")
    private boolean help = false;

    public String getKeyword() {
        return keyword;
    }

    public boolean isStream() {
        return stream;
    }

    public boolean isHideRetweets() {
        return hideRetweets;
    }

    public Integer numberTweets() {
        return limit;
    }

    public boolean isHelp() {
        return help;
    }
}

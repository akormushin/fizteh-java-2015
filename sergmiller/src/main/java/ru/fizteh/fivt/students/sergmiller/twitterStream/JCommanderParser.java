package ru.fizteh.fivt.students.sergmiller.twitterStream;

/**
 * Created by sergmiller on 22.09.15.
 */

import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

class JCommanderParser {
    @Parameter
    private List<String> parameters = new ArrayList<>();

    @Parameter(names = {"--stream", "-s"},
            description = "print stream of new tweets")
    private boolean stream = false;

    @Parameter(names = {"--help", "-h"},
            description = "print help man")
    private boolean help = false;

    @Parameter(names = {"--hideRetweets"},
            description = "print tweets without retweets")
    private boolean hideRetweets = false;

    @Parameter(names = {"--query", "-q"},
            description = "tweets with keyword or query")
    private List<String> query = new ArrayList<>();

    @Parameter(names = {"--limit", "-l"},
            description = "limit of max tweets")
    private Integer limit = Integer.MAX_VALUE;

    @Parameter(names = {"--place", "-p"},
            description = "tweets with keyword or query")
    private String location = "nearby";

    public boolean isStream() {
        return stream;
    }

    public boolean isHelp() {
        return help;
    }

    public boolean isHideRetweets() {
        return hideRetweets;
    }

    public List<String> getQuery() {
        return query;
    }

    public Integer getLimit() {
        return limit;
    }

    public String getLocation() {
        return location;
    }
} //Thread.sleep(10000) <- sleep

package ru.fizteh.fivt.students.andrewgark;

import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

public class JCommanderTwitterStream {
    @Parameter
    private List<String> parameters = new ArrayList<>();

    @Parameter(names = {"--query", "-q"})
    private List<String> keywords = new ArrayList<>();

    @Parameter(names = {"--place", "-p"}, arity = 1)
    private String location = "nearby";

    @Parameter(names = {"--stream", "-s"})
    private boolean stream = false;

    @Parameter(names = "--hideRetweets")
    private boolean hideRetweets = false;

    @Parameter(names = {"--limit", "-l"})
    private Integer limit = Integer.MAX_VALUE;

    @Parameter(names = {"--help", "-h"})
    private boolean help = false;

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

    public List<String> getKeywords() {
        return keywords;
    }
}

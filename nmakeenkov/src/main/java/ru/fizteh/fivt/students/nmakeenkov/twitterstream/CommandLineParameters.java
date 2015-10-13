package ru.fizteh.fivt.students.nmakeenkov.twitterstream;

import com.beust.jcommander.*;
// test
public class CommandLineParameters {
    @Parameter(names = {"-q", "--query"}, required = true)
    private String query = "";

    @Parameter(names = {"-p", "--place"})
    private String place = "";

    @Parameter(names = {"-s", "--stream"})
    private boolean stream = false;

    @Parameter(names = "--hideRetweets")
    private boolean hideRetweets = false;

    @Parameter(names = {"-l", "--limit"})
    private int limit = -1; // -1 - no limit

    @Parameter(names = {"-h", "--help"}, help = true)
    private boolean help = false;

    public final String getQuery() {
        return query;
    }

    public final String getPlace() {
        return place;
    }

    public final boolean isStream() {
        return stream;
    }

    public final boolean isHideRetweets() {
        return hideRetweets;
    }

    public final int getLimit() {
        return limit;
    }

    public final boolean isHelp() {
        return help;
    }
}

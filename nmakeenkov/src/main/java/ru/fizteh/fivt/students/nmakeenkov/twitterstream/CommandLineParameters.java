package ru.fizteh.fivt.students.nmakeenkov.twitterstream;

import com.beust.jcommander.Parameter;
import java.util.List;
import java.util.ArrayList;

public class CommandLineParameters {
    @Parameter(names = {"-q", "--query"}, required = true, variableArity = true,
            description = "Keywords")
    private List<String> query = new ArrayList<>();

    @Parameter(names = {"-p", "--place"}, variableArity = true,
            description = "Location of tweets")
    private List<String> place = new ArrayList<>();

    @Parameter(names = {"-s", "--stream"},
            description = "Stream mode")
    private boolean stream = false;

    @Parameter(names = "--hideRetweets",
            description = "hides retweets")
    private boolean hideRetweets = false;

    @Parameter(names = {"-l", "--limit"},
            description = "Limits number of tweets shown,"
                    + " -1 - no limit")
    private int limit = -1; // -1 - no limit

    @Parameter(names = {"-h", "--help"}, help = true,
            description = "Show this message ans exit")
    private boolean help = false;

    public final String getQuery() {
        String ans = "";
        for (String cur : query) {
            ans += cur + " ";
        }
        return ans;
    }

    public final String getPlace() {
        if (place.isEmpty()) {
            return "nearby";
        }
        String ans = "";
        for (String cur : place) {
            ans += cur + " ";
        }
        return ans;
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

package ru.fizteh.fivt.students.zakharovas.TwitterStream;

import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

public class CommandLineArgs {
    static final int DEFAULT_LIMIT = 100;
    @Parameter(names = {"--query", "-q"}, variableArity = true)
    private List<String> stringForQuery = new ArrayList<>();
    @Parameter(names = {"--help", "-h"}, help = true)
    private Boolean help = false;
    @Parameter(names = {"--stream", "-s"})
    private Boolean streamMode = false;
    /*@Parameter(names = {"--place", "-p"}, variableArity = true)
    private List<String> location = new ArrayList<>();*/
    @Parameter(names = {"--hideRetweets"})
    private Boolean hideRetweets = false;
    @Parameter(names = {"--limits", "-l"})
    private Integer limit = DEFAULT_LIMIT;

    public final List<String> getStringForQuery() {
        return stringForQuery;
    }

    public final Boolean getHelp() {
        return help;
    }

    public final Boolean getStreamMode() {
        return streamMode;
    }

    /*public final List<String> getLocation() {
        return location;
    }*/

    public final Boolean getHideRetweets() {
        return hideRetweets;
    }

    public final Integer getLimit() {
        return limit;
    }

}

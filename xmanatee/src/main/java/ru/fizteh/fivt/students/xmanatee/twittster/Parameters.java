package ru.fizteh.fivt.students.xmanatee.twittster;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

class Parameters {

    private JCommander cmd;

    @Parameter(names = { "-q", "--query" }, description = "Query or keywords for streaming", required = true)
    private String query = "";

    @Parameter(names = { "-p", "--place" }, description = "Location or 'nearby'")
    private String place = "";

    @Parameter(names = { "-s", "--stream"}, description = "Streaming mode")
    private Boolean isStream = false;

    @Parameter(names = { "-l", "--limit"}, description = "Number of tweets to show(only for no streaming mode)")
    private Integer limit = Integer.MAX_VALUE;

    @Parameter(names = "--hideRetweets", description = "Hiding Retweets")
    private boolean noRetweets = false;

    @Parameter(names = { "-h", "--help"}, description = "Help mode", help = true)
    private boolean help = false;

    public String getQuery() {
        return query;
    }

    public String getPlace() {
        return place;
    }

    public Integer getLimit() {
        return limit;
    }

    public boolean noRetweets() {
        return noRetweets;
    }

    public boolean isStream() {
        return isStream;
    }

    public boolean isHelp() {
        return help;
    }

    Parameters(String[] args) {
        try {
            cmd = new JCommander(this, args);
            if (getLimit() < Integer.MAX_VALUE & isStream()) {
                throw new Exception("Options -s and -l can't go together");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public void runHelp() {
        cmd.usage();
    }
}

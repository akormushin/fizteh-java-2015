package ru.fizteh.fivt.students.zerts.moduletests.library;
    import com.beust.jcommander.Parameter;

    import java.util.ArrayList;
    import java.util.List;

public class ArgsParser {
    @Parameter(names = {"-s", "--stream"}, description = "stream output")
    private boolean streamMode;
    @Parameter(names = {"-q", "--query"}, description = "query output")
    private List<String> query = new ArrayList<>();
    @Parameter(names = {"-p", "--place"}, description = "place output")
    private String place = "nearby";
    @Parameter(names = "--hideRetweets", description = "no retweets output")
    private boolean noRetweetMode;
    @Parameter(names = {"-l", "--limit"}, description = "limited output")
    private int numberOfTweets;
    @Parameter(names = {"-h", "--help"}, description = "help output")
    private boolean helpMode;

    public final boolean isStreamMode() {
        return streamMode;
    }
    public final List<String> getQuery() {
        return query;
    }
    public final String getPlace() {
        return place;
    }
    public final boolean isNoRetweetMode() {
        return noRetweetMode;
    }
    public final int getNumberOfTweets() {
        return numberOfTweets;
    }
    public final boolean isHelpMode() {
        return helpMode;
    }
}

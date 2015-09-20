package ru.fizteh.fivt.students.zerts.TwitterStream;
    import com.beust.jcommander.Parameter;
/**
 * Created by User on 20.09.2015.
 */
public class ArgsParser {
    //public int argMode = new int(0);
    @Parameter(names = {"-s", "--stream"}, description = "stream output")
    public boolean streamMode;
    @Parameter(names = {"-q", "--query"}, description = "query output")
    public String query;
    @Parameter(names = {"-p", "--place"}, description = "place output")
    public String place;
    @Parameter(names = "--hideRetweets", description = "no retweets output")
    public boolean noRetweetMode;
    @Parameter(names = {"-l", "--limit"}, description = "limited output")
    public int numberOfTweets;
    @Parameter(names = {"-h", "--help"}, description = "help output")
    public boolean helpMode;
}

package ru.fizteh.fivt.students.loulett.TwitterStream;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import twitter4j.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

class JCommanderPar {

    static final String NEARBY = "nearby";

    @Parameter(names = {"--query", "-q"},
            description = "query",
            variableArity = true)
    private List<String> queries = new ArrayList<>();

    @Parameter(names = {"--place", "-p"}, description = "place")
    private String place = NEARBY;

    @Parameter(names = {"--stream", "-s"}, description = "stream")
    private boolean stream = false;

    @Parameter(names = {"--hideRetweets"}, description = "hide retweets")
    private boolean retweet = false;

    @Parameter(names = {"--limit", "-l"}, description = "limit")
    private Integer limit = Integer.MAX_VALUE;

    @Parameter(names = {"--help", "-h"}, description = "help", help = true)
    private boolean help;

    public String getPlace() {
        return place;
    }

    public String getQueries() {
        return String.join(" ", queries);
    }

    public boolean getStream(){ return stream; }

    public boolean getHideRetweets(){ return retweet;}

    public Integer getLimit() {return limit;}

}

public class Main {

    private static void printMinuses(){
        System.out.println(new String(new char[140]).replace('\0', '-'));
    }

    private static String RetweetsCount(int retweets){
        if (retweets % 10 == 1) {
            return " (" + retweets + " ретвит)";
        }
        else if (retweets % 10 > 1 && retweets % 10 < 5){
            return " ("+retweets+" ретвита)";
        }
        else{
            return " ("+retweets+" ретвитов)";
        }
    }
    private static String TimeFromPublish (long TimeCreateTwit, long currentTime){
        LocalDateTime currTime = new Date(currentTime).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime tweetTime = new Date(TimeCreateTwit).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        if (ChronoUnit.MINUTES.between(tweetTime, currTime) < 2){
            return "Только что";
        }
        else if (ChronoUnit.HOURS.between(tweetTime, currTime) == 0){
            long minutes = ChronoUnit.MINUTES.between(tweetTime, currTime);
            if (minutes % 10 == 1){
                return minutes + "минуту назад";
            }
            else if (minutes % 10 < 5){
                return minutes + "минуты назад";
            }
            else {
                return minutes + "минут назад";
            }
        }
        else if (ChronoUnit.DAYS.between(tweetTime, currTime) == 0){
            long hours = ChronoUnit.HOURS.between(tweetTime, currTime);
            if (hours % 10 == 1){
                return hours + "час назад";
            }
            else if (hours % 10 < 5){
                return hours + "часа назад";
            }
            else{
                return hours + "часов назад";
            }
        }
        else if (ChronoUnit.DAYS.between(tweetTime, currTime) == 1){
            return "вчера";
        }
        else{
            long days = ChronoUnit.DAYS.between(tweetTime, currTime);
            if (days % 10 < 5){
                return days + "дня назад";
            }
            else{
                return days + "дней назад";
            }
        }

    }

    public static void main(String[] args) throws TwitterException, IOException {

        JCommanderPar jCommanderParameters = new JCommanderPar();
        JCommander jCommander = new JCommander(jCommanderParameters, args);

        Twitter twitter = TwitterFactory.getSingleton();
        Query query = new Query(jCommanderParameters.getQueries());
        int n = jCommanderParameters.getLimit();
        QueryResult result = twitter.search(query);
        List<Status> tweets = result.getTweets();
        for (Status status : tweets) {
            if (!status.isRetweet()) {
                printMinuses();
                System.out.println("[" + TimeFromPublish(status.getCreatedAt().getTime(), System.currentTimeMillis()) +
                        "] @" + status.getUser().getScreenName() + ": "
                        + status.getText() + RetweetsCount(status.getRetweetCount()));
            } else if (!jCommanderParameters.getHideRetweets()) {
                printMinuses();
                String[] splittedtext = status.getText().split(":");
                System.out.println("[" + TimeFromPublish(status.getCreatedAt().getTime(), System.currentTimeMillis()) +
                        "] @" + status.getUser().getScreenName() + " ретвитнул " + splittedtext[0].substring(4) + ": "
                        + String.join(" ", Arrays.asList(splittedtext).subList(1, splittedtext.length - 1)) +
                        RetweetsCount(status.getRetweetCount()));
            }
        }
    }
}

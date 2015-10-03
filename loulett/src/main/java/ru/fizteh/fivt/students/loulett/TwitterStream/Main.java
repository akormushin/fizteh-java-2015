package ru.fizteh.fivt.students.loulett.TwitterStream;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import twitter4j.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
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

    public boolean getStream() {
        return stream;
    }

    public boolean getHideRetweets() {
        return retweet;
    }

    public Integer getLimit() {
        return limit;
    }
}

public class Main {
    static final int TEN = 10;
    static final int FIVE = 5;
    static final int ELEVEN = 11;
    static final int TWELVE = 12;
    static final int RT_SUB = 4;
    static final int HUNDRED = 100;

    private static void printMinuses() {
        System.out.println(new String(new char[HUNDRED]).replace('\0', '-'));
    }

    private static String retweetsCount(int retweets) {

        if (retweets % TEN > FIVE || retweets % HUNDRED == ELEVEN
                || retweets % HUNDRED == TWELVE || retweets % TEN == 0) {
            return " (" + retweets + " ретвитов)";
        } else if (retweets % TEN == 1) {
            return " (" + retweets + " ретвит)";
        } else {
            return " (" + retweets + " ретвита)";
        }
    }
    private static String timeFromPublish(long timeCreateTwit, long currentTime) {
        LocalDateTime currTime = new Date(currentTime).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime tweetTime = new
                Date(timeCreateTwit).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        if (ChronoUnit.MINUTES.between(tweetTime, currTime) < 2) {
            return "Только что";
        } else if (ChronoUnit.HOURS.between(tweetTime, currTime) == 0) {
            long minutes = ChronoUnit.MINUTES.between(tweetTime, currTime);
            if (minutes % TEN > FIVE || minutes == ELEVEN || minutes == TWELVE) {
                return minutes + " минут назад";
            } else if (minutes % TEN == 1) {
                return minutes + " минуту назад";
            } else {
                return minutes + " минуты назад";
            }
        } else if (ChronoUnit.DAYS.between(tweetTime, currTime) == 0) {
            long hours = ChronoUnit.HOURS.between(tweetTime, currTime);
            if (hours % TEN > FIVE || hours == ELEVEN || hours == TWELVE) {
                return hours + " часов назад";
            } else if (hours % TEN == 1) {
                return hours + " час назад";
            } else {
                return hours + " часа назад";
            }
        } else if (ChronoUnit.DAYS.between(tweetTime, currTime) == 1) {
            return "вчера";
        } else {
            long days = ChronoUnit.DAYS.between(tweetTime, currTime);
            if (days % TEN < FIVE && days != ELEVEN && days != TWELVE) {
                return days + " дня назад";
            } else {
                return days + " дней назад";
            }
        }
    }

    public static void main(String[] args) throws TwitterException {
        JCommanderPar jCommanderParameters = new JCommanderPar();
        JCommander jCommander = new JCommander(jCommanderParameters, args);

        try {
            Twitter twitter = TwitterFactory.getSingleton();
            Query query = new Query(jCommanderParameters.getQueries());
            query.setCount(jCommanderParameters.getLimit());
            QueryResult result = twitter.search(query);
            List<Status> tweets = result.getTweets();
            Collections.reverse(tweets);

            if (tweets.size() == 0) {
                System.err.print("Не найдено ни одного твита.");
                System.exit(0);
            }

            for (Status status : tweets) {
                if (!status.isRetweet()) {
                    printMinuses();
                    System.out.println("["
                            + timeFromPublish(status.getCreatedAt().getTime(), System.currentTimeMillis())
                            + "] @" + status.getUser().getScreenName() + ": "
                            + status.getText() + retweetsCount(status.getRetweetCount()));
                } else if (!jCommanderParameters.getHideRetweets()) {
                    printMinuses();
                    System.out.println("["
                            + timeFromPublish(status.getCreatedAt().getTime(), System.currentTimeMillis())
                            + "] @" + status.getUser().getScreenName() + " ретвитнул "
                            + status.getRetweetedStatus().getUser().getScreenName() + ": "
                            + status.getRetweetedStatus().getText()
                            + retweetsCount(status.getRetweetCount()));
                }
            }
        } catch (TwitterException te) {
            System.err.println("Something get wrong" + te.getMessage());
            System.exit(-1);
        }
    }
}

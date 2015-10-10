package ru.fizteh.fivt.students.loulett.TwitterStream;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
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
            description = "Queries for searching tweets (this argument is requied)",
            variableArity = true)
    private List<String> queries = new ArrayList<>();

    @Parameter(names = {"--place", "-p"}, description = "Place for searching tweets")
    private String place = NEARBY;

    @Parameter(names = {"--stream", "-s"}, description = "Enable stream")
    private boolean stream = false;

    @Parameter(names = {"--hideRetweets"}, description = "Hide retweets")
    private boolean retweet = false;

    @Parameter(names = {"--limit", "-l"}, description = "Limit of tweets. Not usable in stream mode")
    private Integer limit = Integer.MAX_VALUE;

    @Parameter(names = {"--help", "-h"}, description = "Show help", help = true)
    private boolean help;

    public String getQueries() {
        return String.join(" ", queries);
    }

    public boolean isStream() {
        return stream;
    }

    public boolean isHideRetweets() {
        return retweet;
    }

    public Integer getLimit() {
        return limit;
    }

    public boolean isHelp() {
        return help;
    }
}

public class Main {
    static final int TEN = 10;
    static final int FIVE = 5;
    static final int THREE = 3;
    static final int ELEVEN = 11;
    static final int TWENTY = 20;
    static final int HUNDRED = 100;

    private static void printMinuses() {
        System.out.println(new String(new char[HUNDRED]).replace('\0', '-'));
    }

    private static String russianEnding(int count, int flag) {
        String[][] ending = {{"ретвитов", "ретвит", "ретвита"},
                {"минут", "минуту", "минуты"},
                {"часов", "час", "часа"},
                {"дней", "день", "дня"}};

        if (count % TEN >= FIVE || count % HUNDRED >= ELEVEN
                || count % HUNDRED <= TWENTY || count % TEN == 0) {
            return count + " " + ending[flag][0];
        } else if (count % TEN == 1) {
            return count + " " + ending[flag][1];
        } else {
            return count + " " + ending[flag][2];
        }
    }

    private static String retweetsCount(int retweets) {

        return "(" + russianEnding(retweets, 0) + ")";
    }
    private static String timeFromPublish(long timeCreateTwit, long currentTime) {
        LocalDateTime currTime = new Date(currentTime).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime tweetTime = new
                Date(timeCreateTwit).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        if (ChronoUnit.MINUTES.between(tweetTime, currTime) < 2) {
            return "Только что";
        } else if (ChronoUnit.HOURS.between(tweetTime, currTime) == 0) {
            long minutes = ChronoUnit.MINUTES.between(tweetTime, currTime);
            return russianEnding((int) minutes, 1) + " назад";
        } else if (ChronoUnit.DAYS.between(tweetTime, currTime) == 0) {
            long hours = ChronoUnit.HOURS.between(tweetTime, currTime);
            return russianEnding((int) hours, 2) + " назад";
        } else if (ChronoUnit.DAYS.between(tweetTime, currTime) == 1) {
            return "вчера";
        } else {
            long days = ChronoUnit.DAYS.between(tweetTime, currTime);
            return russianEnding((int) days, THREE) + " назад";
        }
    }

    public static void main(String[] args) throws TwitterException {
        JCommanderPar jCommanderParameters = new JCommanderPar();
        try {
            JCommander jCommander = new JCommander(jCommanderParameters, args);
            if (jCommanderParameters.isHelp()) {
                throw new ParameterException("");
            }
        } catch (ParameterException pe) {
            JCommander jCommander = new JCommander(jCommanderParameters, args);
            jCommander.setProgramName("TwitterStream");
            jCommander.usage();
            return;
        }

        if (!jCommanderParameters.isStream()) {

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
                    } else if (!jCommanderParameters.isHideRetweets()) {
                        printMinuses();
                        System.out.println("["
                                + timeFromPublish(status.getCreatedAt().getTime(), System.currentTimeMillis())
                                + "] @" + status.getUser().getScreenName() + " ретвитнул @"
                                + status.getRetweetedStatus().getUser().getScreenName() + ": "
                                + status.getRetweetedStatus().getText());
                    }
                }
            } catch (TwitterException te) {
                System.err.println("Something get wrong" + te.getMessage());
                System.exit(-1);
            }

        } else {
            TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
            twitterStream.addListener(new StatusAdapter() {
                @Override
                public void onStatus(Status status) {
                    if (!status.isRetweet()) {
                        printMinuses();
                        System.out.println("@" + status.getUser().getScreenName() + ": "
                                + status.getText() + retweetsCount(status.getRetweetCount()));
                    } else if (!jCommanderParameters.isHideRetweets()) {
                        printMinuses();
                        System.out.println("@" + status.getUser().getScreenName() + " ретвитнул @"
                                + status.getRetweetedStatus().getUser().getScreenName() + ": "
                                + status.getRetweetedStatus().getText());
                    }
                }
            });

            String[] trackArray = new String[1];
            trackArray[0] = jCommanderParameters.getQueries();
            FilterQuery filterQuery = new FilterQuery(0, new long[0], trackArray);
            twitterStream.filter(filterQuery);
        }
    }
}

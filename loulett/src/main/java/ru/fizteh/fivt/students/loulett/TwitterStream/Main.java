package ru.fizteh.fivt.students.loulett.TwitterStream;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import twitter4j.*;

import java.util.Collections;
import java.util.List;

public class Main {
    static final int HUNDRED = 100;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";

    private static void printMinuses() {
        System.out.println(new String(new char[HUNDRED]).replace('\0', '-'));
    }

    private static String retweetsCount(int retweets) {

        return "(" + RussianEnding.russianEnding(retweets, "ретвит") + ")";
    }

    private static String printOneTweet(Status status, boolean isStream) {
        StringBuilder  tweet = new StringBuilder();
        if (!isStream) {
            tweet.append("["
                    + TimeFormatter.timeFromPublish(status.getCreatedAt().getTime(), System.currentTimeMillis()) + "]");
        }
        tweet.append(ANSI_BLUE + " @" + status.getUser().getScreenName() + ANSI_RESET);
        if (status.isRetweet()) {
            tweet.append(" ретвитнул @" + status.getRetweetedStatus().getUser().getScreenName());
            tweet.append(" : " + status.getRetweetedStatus().getText());
        } else {
            tweet.append(" : " + status.getText());
            if (status.getRetweetCount() != 0) {
                tweet.append(retweetsCount(status.getRetweetCount()));
            }
        }
        return tweet.toString();
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
                    if (!(status.isRetweet() && jCommanderParameters.isHideRetweets())) {
                        printMinuses();
                        System.out.println(printOneTweet(status, false));
                    }
                }
            } catch (TwitterException te) {
                System.err.println("Something get wrong " + te.getMessage());
                System.exit(-1);
            }

        } else {
            if (jCommanderParameters.getQueries().isEmpty()) {
                System.err.println("Пустой запрос");
                System.exit(-1);
            }
            TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
            twitterStream.addListener(new StatusAdapter() {
                @Override
                public void onStatus(Status status) {
                    if (!(status.isRetweet() && jCommanderParameters.isHideRetweets())) {
                        printMinuses();
                        System.out.println(printOneTweet(status, true));
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

package ru.fizteh.fivt.students.egiby;

import com.beust.jcommander.JCommander;
import twitter4j.*;
import java.util.List;

import static java.lang.System.exit;

/**
 * Created by egiby on 24.09.15.
 */

public class TwitterStream {
    public static void main(String[] args) {
        JCommanderParams jcp = new JCommanderParams();
        JCommander jcm = new JCommander(jcp, args);

        if (jcp.isHelp()) {
            jcm.usage();
            exit(0);
        }

        try {
            printAllTweets(jcp);
        } catch (TwitterException te) {
            te.printStackTrace();
        }

    }

    private static void printAllTweets(JCommanderParams jcp) throws TwitterException {
        Twitter twitter = new TwitterFactory().getInstance();
        Query query = new Query(jcp.getKeyword());
        QueryResult result;

        Integer numberOfTweets = 0;

        while ((numberOfTweets < jcp.numberTweets() || jcp.isStream()) && query != null) {
            result = twitter.search(query);

            List<Status> tweets = result.getTweets();
            for (Status tweet : tweets) {
                printTweet(tweet, jcp.isStream());
                numberOfTweets++;
            }

            query = result.nextQuery();
        }

        exit(0);
    }

    private static void printTweet(Status tweet, boolean isStream) {
        System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
    }
}

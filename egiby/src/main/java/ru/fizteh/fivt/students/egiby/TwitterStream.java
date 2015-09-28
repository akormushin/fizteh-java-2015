package ru.fizteh.fivt.students.egiby;

import twitter4j.*;
import java.util.List;

/**
 * Created by egiby on 24.09.15.
 */

public class TwitterStream {
    private static final Integer DEFAULT_NUMBER_TWEETS = 100;

    public static void main(String[] args) {
        if (args.length == 0) {
            return;
        }

        if (args[0].equals("--query") || args[0].equals("-q")) {
            try {
                printAllTweets("Moscow");
            } catch (TwitterException te) {
                te.printStackTrace();
            }
        }
    }

    private static void printAllTweets(String keywords) throws TwitterException {
        Twitter twitter = new TwitterFactory().getInstance();
        Query query = new Query(keywords);
        QueryResult result;

        Integer numberOfTweets = 0;

        while (numberOfTweets < DEFAULT_NUMBER_TWEETS && query != null) {
            result = twitter.search(query);

            List<Status> tweets = result.getTweets();
            for (Status tweet : tweets) {
                printTweet(tweet);
                numberOfTweets++;
            }

            query = result.nextQuery();
        }

        System.exit(0);
    }

    private static void printTweet(Status tweet) {
        System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
    }
}

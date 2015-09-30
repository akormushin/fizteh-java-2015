package ru.fizteh.fivt.students.egiby;

import com.beust.jcommander.JCommander;
import twitter4j.*;
import java.util.List;

/**
 * Created by egiby on 24.09.15.
 */

public class TwitterStream {
    public static void main(String[] args) {
        JCommanderParams jcp = new JCommanderParams();
        JCommander jcm = new JCommander(jcp, args);

        if (jcp.isHelp()) {
            printHelp(jcm);
        }

        if (jcp.isStream()) {
            if (jcp.getNumberTweets() != JCommanderParams.DEFAULT_NUMBER_OF_TWEETS) {
                printHelp(jcm);
            }
            getStream(jcp);
        } else {
            try {
                printAllTweets(jcp);
            } catch (TwitterException te) {
                te.printStackTrace();
            }
        }
    }

    private static void printHelp(JCommander jcm) {
        jcm.usage();
        System.exit(0);
    }

    private static void printAllTweets(JCommanderParams jcp) throws TwitterException {
        Twitter twitter = new TwitterFactory().getInstance();
        Query query = new Query(jcp.getKeyword());
        QueryResult result;

        Integer numberOfTweets = 0;

        while (numberOfTweets < jcp.getNumberTweets() && query != null) {
            result = twitter.search(query);

            List<Status> tweets = result.getTweets();
            for (Status tweet : tweets) {
                if (numberOfTweets == jcp.getNumberTweets()) {
                    return;
                }

                if (tweet.isRetweet() && jcp.isHideRetweets()) {
                    continue;
                }

                System.out.println(FormatUtils.formatTweet(tweet, false));
                numberOfTweets++;
            }

            query = result.nextQuery();
        }
    }

    private static void getStream(JCommanderParams jcp) {
        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status tweet) {
                if (jcp.isHideRetweets() && tweet.isRetweet()) {
                    return;
                }

                System.out.println(FormatUtils.formatTweet(tweet, true));
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
            }

            @Override
            public void onStallWarning(StallWarning warning) {
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };
        twitter4j.TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(listener);
        twitterStream.filter(jcp.getKeyword());
    }
}

package ru.fizteh.fivt.students.zakharovas.TwitterStream;


import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import twitter4j.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TwitterStreamMain {

    public static void main(String[] args) {
        String[] separatedArgs = ArgumentSepatator.separateArguments(args);
        CommandLineArgs commandLineArgs = new CommandLineArgs();
        JCommander jCommander = null;
        try {
            jCommander = new JCommander(commandLineArgs, separatedArgs);
        } catch (ParameterException pe) {
            System.err.println(pe.getMessage());
            System.exit(1);
        }
        checkArguments(commandLineArgs, jCommander);
        if (commandLineArgs.getHelp()) {
            helpMode(jCommander);
        } else {
            if (commandLineArgs.getStringForQuery().isEmpty()) {
                System.err.println("Empty search");
                System.exit(1);
            }
            if (commandLineArgs.getStreamMode()) {
                streamMode(commandLineArgs);
            } else {
                twitterWork(commandLineArgs);
            }
        }
    }

    private static void streamMode(CommandLineArgs commandLineArgs) {
        twitter4j.TwitterStream twitterStream =
                TwitterStreamFactory.getSingleton();
        Queue<Status> tweetQueue = new LinkedList<>();
        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status tweet) {
                if (!commandLineArgs.getHideRetweets() || !tweet.isRetweet()) {
                    tweetQueue.add(tweet);
                }
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice
                                                 statusDeletionNotice) {

            }

            @Override
            public void onTrackLimitationNotice(int i) {

            }

            @Override
            public void onScrubGeo(long l, long l1) {

            }

            @Override
            public void onStallWarning(StallWarning stallWarning) {

            }

            @Override
            public void onException(Exception e) {
                e.getMessage();
                System.exit(1);

            }
        };
        twitterStream.addListener(listener);
        twitterStream.filter(String.join(" ", commandLineArgs.
                getStringForQuery()));
        while (true) {
            while (!tweetQueue.isEmpty()) {
                System.out.println(StringFormater.tweetForOutputWithoutDate(tweetQueue.poll()));
            }
            try {
                Thread.sleep(Numbers.SECOND);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void twitterWork(CommandLineArgs commandLineArgs) {
        String search = String.join(" ", commandLineArgs.getStringForQuery());

        Twitter twitter = TwitterFactory.getSingleton();
        Query query = new Query(search);
        query.setCount(Integer.MAX_VALUE);
        List<Status> tweets = new ArrayList<>();
        try {
            tweets = twitter.search(query).getTweets();
        } catch (TwitterException te) {
            System.err.println(te.getMessage());
            System.exit(1);
        }
        //System.out.println(tweets.size());
        List<Status> tweetsForOutput = new ArrayList<>();
        for (Status tweet : tweets) {
            if (tweetsForOutput.size() == commandLineArgs.getLimit()) {
                break;
            }
            if (!commandLineArgs.getHideRetweets() || !tweet.isRetweet()) {
                tweetsForOutput.add(tweet);
            }
        }
        for (Status tweet : tweetsForOutput) {
            System.out.println(StringFormater.tweetForOutput(tweet));
        }

    }

    private static void helpMode(JCommander jCommander) {
        jCommander.usage();
        System.exit(0);
    }


    private static void checkArguments(CommandLineArgs commandLineArgs,
                                       JCommander jCommander) {
        if (commandLineArgs.getStreamMode()
                && commandLineArgs.getLimit()
                != commandLineArgs.DEFAULT_LIMIT) {
            jCommander.usage();
            System.err.println("Stream mode does not have limits");
            System.exit(1);
        }

    }
}

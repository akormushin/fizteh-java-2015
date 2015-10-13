package ru.fizteh.fivt.students.zakharovas.twitterstream;


import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import twitter4j.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TwitterStreamMain {

    private static GeoLocator geoLocator;

    public static void main(String[] args) {
        String[] separatedArgs = StringFormater.separateArguments(args);
        CommandLineArgs commandLineArgs = new CommandLineArgs();
        JCommander jCommander = null;
        try {
            jCommander = new JCommander(commandLineArgs, separatedArgs);
        } catch (ParameterException pe) {
            System.err.println("Problems with parametes " + pe.getMessage());
            System.exit(1);
        }
        try {
            checkArguments(commandLineArgs, jCommander);
        } catch (ParameterException pe) {
            System.err.println("Problems with parametes " + pe.getMessage());
            System.exit(1);
        }
        if (commandLineArgs.getHelp()) {
            helpMode(jCommander);
            System.exit(0);
        } else {
            try {
                geoLocator = new GeoLocator(commandLineArgs.getLocation());
            } catch (Exception e) {
                System.err.println("Problems with geolocation " + e.getMessage());
                System.exit(1);
            }
            if (commandLineArgs.getStringForQuery().isEmpty()) {
                System.err.println("Empty search");
                System.exit(1);
            }
            if (commandLineArgs.getStreamMode()) {
                streamMode(commandLineArgs);
            } else {
                try {
                    searchMode(commandLineArgs);
                } catch (TwitterException | IllegalStateException e) {
                    System.err.println("Problems with search mode " + e.getMessage());
                    System.exit(1);
                }
            }
        }
    }

    private static void streamMode(CommandLineArgs commandLineArgs) {
        twitter4j.TwitterStream twitterStream =
                TwitterStreamFactory.getSingleton();
        Queue<Status> tweetQueue = new LinkedList<>();
        StatusListener listener = new StatusAdapter() {
            @Override
            public void onStatus(Status tweet) {
                if (!commandLineArgs.getHideRetweets() || !tweet.isRetweet()) {
                    tweetQueue.add(tweet);
                }
            }
        };
        twitterStream.addListener(listener);
        FilterQuery query = new FilterQuery(String.join(" ", commandLineArgs.
                getStringForQuery()));
        query.locations(geoLocator.getLocationForStream());
        twitterStream.filter(query);
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

    private static void searchMode(CommandLineArgs commandLineArgs) throws IllegalStateException, TwitterException {
        String search = String.join(" ", commandLineArgs.getStringForQuery());

        Twitter twitter = TwitterFactory.getSingleton();
        Query query = new Query(search);
        query.setCount(Integer.MAX_VALUE);
        query.geoCode(geoLocator.getLocationForSearch(), geoLocator.getRadius(), Query.KILOMETERS.toString());
        List<Status> tweets;
        while (true) {
            try {
                tweets = twitter.search(query).getTweets();
                break;
            } catch (TwitterException te) {
                if (te.isCausedByNetworkIssue()) {
                    System.err.println("Connection problem. Reconnecting");
                    try {
                        Thread.sleep(Numbers.SECOND);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    throw te;
                }
            }
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
        if (tweetsForOutput.isEmpty()) {
            System.out.println("No tweets for this search has been found");
            return;
        }
        for (Status tweet : tweetsForOutput) {
            System.out.println(StringFormater.tweetForOutput(tweet));
        }

    }

    private static void helpMode(JCommander jCommander) {
        jCommander.usage();
    }


    private static void checkArguments(CommandLineArgs commandLineArgs,
                                       JCommander jCommander) throws ParameterException {
        if (commandLineArgs.getStreamMode()
                && commandLineArgs.getLimit()
                != commandLineArgs.DEFAULT_LIMIT) {
            jCommander.usage();
            ParameterException parameterException = new ParameterException("Stream mode does not have limits");
            throw parameterException;
        }

    }
}

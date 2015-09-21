package ru.fizteh.fivt.students.zakharovas.TwitterStream;


import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import twitter4j.*;

import java.util.ArrayList;
import java.util.List;

public class TwitterStream {

    public static void main(String[] args) {
        String[] separatedArgs = ArgumentSepatator.separateArguments(args);
        CommandLineArgs commandLineArgs = new CommandLineArgs();
        try {
            new JCommander(commandLineArgs, separatedArgs);
        } catch (ParameterException pe) {
            System.err.println(pe.getMessage());
            System.exit(1);
        }
        checkArguments(commandLineArgs);
        if (commandLineArgs.getHelp()) {
            helpMode();
        } else if (commandLineArgs.getStreamMode()) {
            streamMode(commandLineArgs);
        } else {
            twitterWork(commandLineArgs);
        }
    }

    private static void streamMode(CommandLineArgs commandLineArgs) {

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

    private static void helpMode() {
        System.out.println("No help now");
        System.exit(0);
    }


    private static void checkArguments(CommandLineArgs commandLineArgs) {
        if (commandLineArgs.getStreamMode()
                && commandLineArgs.getLimit()
                != commandLineArgs.DEFAULT_LIMIT) {
            System.err.println("Stream mode does not have limits");
            System.exit(1);
        }

    }
}

package ru.fizteh.fivt.students.zakharovas.TwitterStream;


import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import twitter4j.*;

import java.util.ArrayList;
import java.util.List;

public class TwitterStream {

    public final static double RADIUS = 100;

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
        twitterWork(commandLineArgs);


    }

    private static void twitterWork(CommandLineArgs commandLineArgs) {
        String serch = String.join(" ", commandLineArgs.stringForQuery);
        Twitter twitter = TwitterFactory.getSingleton();
        Query query = new Query(serch);
        List<Status> tweets = new ArrayList<>();
        try {
            tweets = twitter.search(query).getTweets();
        } catch (TwitterException te) {
            System.err.println(te.getMessage());
            System.exit(1);
        }
        for (Status status : tweets) {
            System.out.println(status.getText());
        }

    }

    private static void helpMode() {
        System.out.println("No help now");
        System.exit(0);
    }


    private static void checkArguments(CommandLineArgs commandLineArgs) {
        return;

    }
}

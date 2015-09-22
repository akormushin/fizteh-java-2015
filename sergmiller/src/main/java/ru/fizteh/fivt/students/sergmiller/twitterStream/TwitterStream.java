package ru.fizteh.fivt.students.sergmiller.twitterStream;

import twitter4j.*;
import twitter4j.StatusListener;
import com.beust.jcommander.JCommander;

import java.util.ArrayList;

//import java.util.ArrayList;
//import java.util.ArrayList;
//import java.util.List;
//import sun.jvm.hotspot.utilities.Assert;
//import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by sergmiller on 15.09.15.
 */
final class TwitterStream {
    private TwitterStream() {
    }
    static final String ANSI_RESET = "\u001B[0m";
    static final String ANSI_BLUE = "\u001B[34m";
    public static void printTwitterStream(final JCommanderParser jct) {
        twitter4j.TwitterStream twStream = twitter4j
                .TwitterStreamFactory.getSingleton();


        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(final Status status) {
                System.out
                        .println(ANSI_BLUE
                                + status.getUser()
                                .getScreenName()
                                + ANSI_RESET
                                + " : " + status.getText());
            }

            @Override
            public void onDeletionNotice(
                    final StatusDeletionNotice statusDeletionNotice) {
            }

            @Override
            public void onTrackLimitationNotice(
                    final int numberOfLimitedStatuses) {
            }

            @Override
            public void onScrubGeo(final long var1, final long var3) {
            }

            @Override
            public void onStallWarning(final StallWarning var1) {
            }

            @Override
            public void onException(final Exception ex) {
                ex.printStackTrace();
            }
        };

        twStream.addListener(listener);
        twStream.filter(new FilterQuery("DoctorWho"));
    }

    public static void printHelpMan() {
        System.out.println("Unacceptable helper");
    }

    public static void main(final String[] args) throws TwitterException {
        JCommanderParser jcp = new JCommanderParser();

        ArrayList<String> buffExp = new ArrayList<String>();
        int size = 0;

        for (int i = 0; i < args.length; ++i) {
            String[] parsedArgs = args[i].split("(\\s)+");
            for (int j = 0; j < parsedArgs.length; ++j) {
                buffExp.add(parsedArgs[j]);
            }
            size += parsedArgs.length;
        }
        String[] inputExpression = new String[size];
        for (int i = 0; i < buffExp.size(); ++i) {
            inputExpression[i] = buffExp.get(i);
        }

        JCommander jcr = new JCommander(jcp, inputExpression);
        jcr.setProgramName("TwitterStream");

        if (jcp.isHelp()) {
            printHelpMan();
        }

        if (jcp.isStream()) {
                printTwitterStream(jcp);
        }

        //    System.out.println(jct.debug
        /*
        String expression = "";

        for (int i = 0; i < args.length; ++i) {
            expression += (args[i] + " ");
        }

        Twitter twitter = TwitterFactory.getSingleton();
        if (expression == "") {
            // The factory instance is re-useable and thread safe.
            Status status = twitter.updateStatus(
                    "Success!I can to write a tweet from terminal.");
            System.out.println("Successfully updated the status to ["
                    + status.getText()
                    + "].");
        } else {
            twitter.search(new Query(expression))
                    .getTweets().stream()
                    .map(s ->
                            "@"
                                    + s.getUser().getScreenName()
                                    + ": "
                                    + s.getText()
                                    + s.getCreatedAt().toString()
                                    + "\n")
                    .forEach(System.out::println);*/
    }
}





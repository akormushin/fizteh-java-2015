package ru.fizteh.fivt.students.zerts.TwitterStream;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import twitter4j.*;

import java.io.*;
import java.util.List;
import static java.lang.Thread.*;

public class TwitterReader {
    private static int printedTweets = 0;
    static final int MILLS_PER_PER = 1000;
    static final int LOCATE_RADIUS = 50;
    static final int RT_MODE = 4;
    static final int LINE_LENGTH = 100;
    public static void printLine() {
        System.out.print("\n");
        for (int i = 0; i < LINE_LENGTH; i++) {
            System.out.print("-");
        }
        System.out.print("\n");
    }
    public static void printTweet(Status tweet, ArgsParser argsPars, boolean streamMode) {
        if (tweet.isRetweet()) {
            if (argsPars.isNoRetweetMode()) {
                return;
            }
        }
        try {
            sleep(MILLS_PER_PER);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!streamMode) {
            TimeParser timePars = new TimeParser();
            TimeParser.printGoneDate(tweet.getCreatedAt().getTime());
        }
        printedTweets++;
        System.out.print("@" + tweet.getUser().getScreenName() + ": ");
        int start = 0;
        String text = tweet.getText();
        if (tweet.isRetweet()) {
            if (argsPars.isNoRetweetMode()) {
                return;
            }
            System.out.print("ретвитнул ");
            while (text.charAt(start) != '@') {
                start++;
            }
        }
        for (int i = start; i < text.length(); i++) {
            if (text.charAt(i) != '\n') {
                System.out.print(text.charAt(i));
            } else {
                System.out.print(" ");
            }
        }
        if (!argsPars.isStreamMode() && !tweet.isRetweet() && tweet.getRetweetCount() != 0) {
            System.out.print(" (");
            TimeParser.rightWordPrinting(tweet.getRetweetCount(), RT_MODE);
            System.out.print(")");
        }
        printLine();
        if (argsPars.getNumberOfTweets() == printedTweets) {
            System.exit(0);
        }
    }
    public static void stream(ArgsParser argsPars) {
        if (argsPars.getQuery() == null) {
            System.err.println("Any query, please!");
            System.exit(-1);
        }
        twitter4j.TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(new StatusAdapter() {
            @Override
            public void onStatus(Status status) {
                if (argsPars.getPlace() == null) {
                    printTweet(status, argsPars, true);
                } else {
                    //System.out.println("another one");
                    GeoLocation tweetLocation = null;
                    if (status.getGeoLocation() != null) {
                        tweetLocation = new GeoLocation(status.getGeoLocation().getLatitude(),
                                status.getGeoLocation().getLongitude());
                    } else if (!status.getUser().getLocation().isEmpty()) {
                        try {
                            //System.out.println(status.getUser().getLocation());
                            tweetLocation = GeoParser.getCoordinates(status.getUser().getLocation());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        return;
                    }
                    try {
                        GeoLocation queryLocation = GeoParser.getCoordinates(argsPars.getPlace());
                        if (queryLocation == null) {
                            System.err.println("Bad place");
                            System.exit(-1);
                        }
                        if (tweetLocation == null) {
                            return;
                        }
                        if (GeoParser.near(tweetLocation, queryLocation, LOCATE_RADIUS)) {
                            printTweet(status, argsPars, true);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        String[] trackArray = argsPars.getQuery().toArray(new String[argsPars.getQuery().size()]);
        twitterStream.filter(new FilterQuery().track(trackArray));
    }
    public static void query(ArgsParser argsPars) throws IOException {
        Twitter twitter = new TwitterFactory().getInstance();
        try {
            Query query;
            if (argsPars.getPlace() != null) {
                GeoLocation queryLocation = GeoParser.getCoordinates(argsPars.getPlace());
                if (queryLocation == null) {
                    System.err.println("Bad place");
                    System.exit(-1);
                }
                query = new Query(argsPars.getQuery().toString()).geoCode(queryLocation, LOCATE_RADIUS, "km");
            } else {
                query = new Query(argsPars.getQuery().toString());
            }
            QueryResult result;
            do {
                result = twitter.search(query);
                List<Status> tweets = result.getTweets();
                System.out.print("Tweets with " + argsPars.getQuery());
                if (argsPars.getPlace() != null) {
                    System.out.print(" near " + argsPars.getPlace());
                }
                printLine();
                if (tweets.isEmpty()) {
                    System.out.println("Sorry, no tweets found :(");
                    System.exit(0);
                }
                for (Status tweet : tweets) {
                    printTweet(tweet, argsPars, false);
                }
                query = result.nextQuery();
            } while (query != null);
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.err.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
    }
    public static void userStream(ArgsParser argsPars) {
        Twitter twitter = new TwitterFactory().getInstance();
        try {
            int currPage = 1;
            User user = twitter.verifyCredentials();
            System.out.println("\nShowing @" + user.getScreenName() + "'s home timeline.\n");
            printLine();
            do {
                Paging p = new Paging(currPage);
                List<Status> tweets = twitter.getHomeTimeline(p);
                for (Status tweet : tweets) {
                    printTweet(tweet, argsPars, true);
                }
                currPage++;
            } while (true);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.err.println("Failed to get timeline: " + te.getMessage());
            System.exit(-1);
        }
    }
    public static void main(String[] args) throws IOException {
        ArgsParser argsPars = new ArgsParser();
        try {
            JCommander jComm = new JCommander(argsPars, args);
            if (argsPars.isHelpMode()) {
                jComm.usage();
            }
        } catch (ParameterException pe) {
            System.err.print("Invalid Paramters:\n" + pe.getMessage());
            System.exit(-1);
        }
        /*if (argsPars.isHelpMode()) {
            BufferedReader in = new BufferedReader(new FileReader("./zerts/src/main/java/ru/fizteh"
                            + "/fivt/students/zerts/TwitterStream/help.txt"));
            String currentLine = in.readLine();
            while (currentLine != null) {
                System.out.println(currentLine);
                currentLine = in.readLine();
            }
            System.exit(0);
        }*/
        if (argsPars.isStreamMode()) {
            stream(argsPars);
        } else if (argsPars.getQuery() == null) {
            userStream(argsPars);
        } else {
            query(argsPars);
        }
    }
}

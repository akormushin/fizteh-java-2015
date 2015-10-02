package ru.fizteh.fivt.students.zerts.TwitterStream;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import ru.fizteh.fivt.students.zerts.TwitterStream.exceptions.GeoExeption;
import ru.fizteh.fivt.students.zerts.TwitterStream.exceptions.GetTimelineExeption;
import ru.fizteh.fivt.students.zerts.TwitterStream.exceptions.NoQueryExeption;
import ru.fizteh.fivt.students.zerts.TwitterStream.exceptions.SearchTweetExeption;
import twitter4j.*;

import java.io.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.*;

public class TwitterReader {
    private static int printedTweets = 0;
    static final int LOCATE_RADIUS = 50;
    static final int RT_MODE = 4;
    static final int LINE_LENGTH = 170;
    public static void printLine() {
        System.out.print("\n");
        for (int i = 0; i < LINE_LENGTH; i++) {
            System.out.print("-");
        }
        System.out.print("\n");
    }
    public static void printTweet(Status tweet, ArgsParser argsPars, boolean streamMode) {
        if (tweet.isRetweet() && argsPars.isNoRetweetMode()) {
            return;
        }
        try {
            sleep(TimeUnit.SECONDS.toMillis(1L));
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
    public static void stream(ArgsParser argsPars) throws NoQueryExeption, GeoExeption {
        if (argsPars.getQuery() == null) {
            throw new NoQueryExeption();
        }
        twitter4j.TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(new StatusAdapter() {
            @Override
            public void onStatus(Status status) {
                if (argsPars.getPlace() == null) {
                    printTweet(status, argsPars, true);
                } else {
                    GeoLocation tweetLocation = null;
                    if (status.getGeoLocation() != null) {
                        tweetLocation = new GeoLocation(status.getGeoLocation().getLatitude(),
                                status.getGeoLocation().getLongitude());
                    } else if (!status.getUser().getLocation().isEmpty()) {
                        try {
                            //System.out.println(status.getUser().getLocation());
                            try {
                                tweetLocation = GeoParser.getCoordinates(status.getUser().getLocation());
                            } catch (GeoExeption | InterruptedException | JSONException e) {
                                e.printStackTrace();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        return;
                    }
                    try {
                        GeoLocation queryLocation = null;
                        try {
                            queryLocation = GeoParser.getCoordinates(argsPars.getPlace());
                        } catch (GeoExeption | InterruptedException | JSONException e) {
                            e.printStackTrace();
                        }
                        if (queryLocation == null) {
                            throw new GeoExeption();
                        }
                        if (tweetLocation == null) {
                            return;
                        }
                        if (GeoParser.near(tweetLocation, queryLocation, LOCATE_RADIUS)) {
                            printTweet(status, argsPars, true);
                        }
                    } catch (IOException | GeoExeption e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        String[] trackArray = argsPars.getQuery().toArray(new String[argsPars.getQuery().size()]);
        twitterStream.filter(new FilterQuery().track(trackArray));
    }
    public static void query(ArgsParser argsPars) throws IOException, GeoExeption, SearchTweetExeption,
            InterruptedException, JSONException {
        Twitter twitter = new TwitterFactory().getInstance();
        try {
            GeoLocation queryLocation = GeoParser.getCoordinates(argsPars.getPlace());
            if (queryLocation == null) {
                throw new GeoExeption("Bad query");
            }
            Query query = new Query(argsPars.getQuery().toString()).geoCode(queryLocation, LOCATE_RADIUS, "km");
            QueryResult result;
            do {
                result = twitter.search(query);
                List<Status> tweets = result.getTweets();
                System.out.print("Tweets with " + argsPars.getQuery());
                if (argsPars.getPlace() != null) {
                    System.out.print(" near ");
                    if (argsPars.getPlace().equals("nearby")) {
                        System.out.print(GeoParser.getMyPlace());
                    } else {
                        System.out.print(argsPars.getPlace());
                    }
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
            throw new SearchTweetExeption();
        }
    }
    //Printing user timeline (not a part of the task)
    public static void userStream(ArgsParser argsPars) throws GetTimelineExeption {
        Twitter twitter = new TwitterFactory().getInstance();
        try {
            int currPage = 1;
            User user = twitter.verifyCredentials();
            System.out.println("\nShowing @" + user.getScreenName() + "'s home timeline.\n");
            printLine();
            List<Status> tweets;
            do {
                Paging p = new Paging(currPage);
                tweets = twitter.getHomeTimeline(p);
                for (Status tweet : tweets) {
                    printTweet(tweet, argsPars, true);
                }
                currPage++;
            } while (!tweets.isEmpty());
        } catch (TwitterException te) {
            throw new GetTimelineExeption("Can't read the timeline");
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
        }
        if (argsPars.isStreamMode()) {
            try {
                stream(argsPars);
            } catch (NoQueryExeption | GeoExeption e) {
                e.printStackTrace();
            }
        } else if (argsPars.getQuery() == null) {
            try {
                userStream(argsPars);
            } catch (GetTimelineExeption getTimelineExeption) {
                getTimelineExeption.printStackTrace();
            }
        } else {
            try {
                query(argsPars);
            } catch (GeoExeption | SearchTweetExeption | InterruptedException | JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

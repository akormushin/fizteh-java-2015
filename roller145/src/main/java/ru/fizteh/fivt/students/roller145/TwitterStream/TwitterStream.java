package ru.fizteh.fivt.students.roller145.TwitterStream;

import com.beust.jcommander.JCommander;
import twitter4j.*;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import static ru.fizteh.fivt.students.roller145.TwitterStream.GetGeolocation.getGeolocation;
import static ru.fizteh.fivt.students.roller145.TwitterStream.TimeMethods.printTime;

public class TwitterStream {

    public static void main(String[] args) throws Exception {
        String str = null;
        for (int i =0; i < args.length; ++i){
            str += args[i] +' ';
        }
        TwitterStreamParser twParse = new TwitterStreamParser();
        JCommander command = new JCommander(twParse, args);
        if (twParse.isHelpOn()) {
            printHelp(command);
        }
        if (twParse.isTweet()) {
            makeTweet();
        }
        if (twParse.isStreamOn() && twParse.isLimit()) {
            System.out.println("Command conflict");
            return;
        }
        if (twParse.isStreamOn()) {
            streamMode(twParse.isFilterRetweet(), twParse.getQueryWords(), twParse.getWhere());
        }
        if (twParse.isLimit()){
            limitedMode(twParse.isFilterRetweet(), twParse.getNumber(), twParse.getQueryWords(),twParse.getWhere(),twParse.isPlace());
        }
        return;
    }

    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String UNIT_RADIUS = "km";
    public static final int RADIUS = 10;

    public static void printHelp(JCommander command){
        command.usage();
    }

    public static void makeTweet() throws TwitterException {
        Twitter twitter = TwitterFactory.getSingleton();
        Scanner sc = new Scanner(System.in);
        String tweet;
        System.out.println("Your tweet: ");
        tweet = sc.nextLine();
        twitter.updateStatus(tweet);
    }

    static void printName(String name){
        System.out.print(ANSI_PURPLE + "@" + name + ":" + ANSI_RESET);
    }
    static void printTweet(Status tweet){
        printName(tweet.getUser().getScreenName());
        if (tweet.isRetweet()){
            System.out.print("retweeted ");
            String[] splited = tweet.getText().split(" ");
            System.out.print(
                    "@" + ANSI_PURPLE + splited[1].split("@|:")[1]
                            + ANSI_RESET + ": ");
            for (int i = 2; i < splited.length; ++i){
                System.out.print(splited[i]);
            }
            System.out.println();
        }
        else {
            System.out.println(tweet.getText() + " (" + tweet.getRetweetCount()
                            + " retweet(s)"+ ")");
        }

    }

    private static void limitedMode(boolean filterRetweet, int numberOfTweets, String queryWords, String were, boolean isLocation) {
        twitter4j.Twitter twitter = new TwitterFactory().getInstance();
        int i = 0;
        try {
            Query query = new Query(queryWords);
            if (isLocation){
                query.setGeoCode(getGeolocation(were), RADIUS, UNIT_RADIUS);
            }
            QueryResult result;
            do {
                result = twitter.search(query);
                List<Status> tweets = result.getTweets();
                for (Status tweet : tweets) {
                    printTime(tweet);
                    printTweet(tweet);
                    ++i;
                    if (i >= numberOfTweets) {break;}
                }
            } while ((query = result.nextQuery()) != null && i <numberOfTweets);
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void streamMode(boolean filterRetweet, String queryWords, String where) {

    }
}


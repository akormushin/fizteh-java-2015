package ru.fizteh.fivt.students.zerts.TwitterStream;

        import com.beust.jcommander.JCommander;
        import com.beust.jcommander.ParameterException;
        import twitter4j.*;

        import java.io.*;
        import java.net.UnknownHostException;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Scanner;

        import static java.lang.Thread.*;

public class PrintUserStream {
    private static int printedTweets = 0;

    public static void printTweet(Status tweet, ArgsParser argsPars) {
        if (tweet.isRetweet()) {
            if (argsPars.noRetweetMode) {
                return;
            }
        }
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TimeParcer timeParc = new TimeParcer();
        TimeParcer.printGoneDate(tweet.getCreatedAt());
        printedTweets++;
        System.out.print("@" + tweet.getUser().getScreenName() + ": ");
        int start = 0;
        String text = tweet.getText();
        if (tweet.isRetweet()) {
            if (argsPars.noRetweetMode) {
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
        if (!tweet.isRetweet()) {
            System.out.print(" (");
            TimeParcer.rightWordPrinting(tweet.getRetweetCount(), 4);
            System.out.print(")");
        }
        System.out.print("\n\n");
        if (argsPars.numberOfTweets == printedTweets) {
            System.exit(0);
        }
    }
    public static void main(String[] args) throws IOException {
        ArgsParser argsPars = new ArgsParser();
        try {
            new JCommander(argsPars, args);
        } catch (ParameterException pe) {
            System.err.print("Invalid Paramters:\n" + pe.getMessage());
            System.exit(-1);
        }
        //System.out.print(argsPars.place + "\n");
        if (argsPars.helpMode) {
            BufferedReader in = new BufferedReader(new FileReader("./zerts/src/main/java/ru/fizteh/fivt/students/zerts/TwitterStream/help.txt"));
            String currentLine = in.readLine();
            while(currentLine != null){
                System.out.println(currentLine);
                currentLine = in.readLine();
            }
            System.exit(0);
        }
        Twitter twitter = new TwitterFactory().getInstance();
        if (argsPars.place == null && argsPars.query == null) {
            try {
                int currPage = 1;
                User user = twitter.verifyCredentials();
                do {
                    Paging p = new Paging(currPage);
                    List<Status> tweets = twitter.getHomeTimeline(p);
                    System.out.println("\nShowing @" + user.getScreenName() + "'s home timeline.\n");
                    for (Status tweet : tweets) {
                        printTweet(tweet, argsPars);
                    }
                    currPage++;
                } while (true);
            } catch (TwitterException te) {
                te.printStackTrace();
                System.err.println("Failed to get timeline: " + te.getMessage());
                System.exit(-1);
            }
        }
        if (argsPars.query != null) {
            try {
                Query query = new Query(argsPars.query);
                QueryResult result;
                do {
                    result = twitter.search(query);
                    List<Status> tweets = result.getTweets();
                    System.out.print("Tweets with " + argsPars.query + ":\n\n");
                    for (Status tweet : tweets) {
                        printTweet(tweet, argsPars);
                    }
                } while ((query = result.nextQuery()) != null);
                System.exit(0);
            } catch (TwitterException te) {
                te.printStackTrace();
                System.err.println("Failed to search tweets: " + te.getMessage());
                System.exit(-1);
            }
        }
    }
}
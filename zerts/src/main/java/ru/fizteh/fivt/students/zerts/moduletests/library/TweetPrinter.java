package ru.fizteh.fivt.students.zerts.moduletests.library;

import ru.fizteh.fivt.students.zerts.TwitterStream.Printer;
import twitter4j.Status;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class TweetPrinter {
    static final int RT_MODE = 4;
    private static int printedTweets = 0;
    public static int getPrintedTweets() {
        return printedTweets;
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
        Printer.print("@" + tweet.getUser().getScreenName() + ": ");
        String text = tweet.getText();
        if (tweet.isRetweet()) {
            if (argsPars.isNoRetweetMode()) {
                return;
            }
            Printer.print("ретвитнул ");
            Printer.print("@" + tweet.getRetweetedStatus().getUser().getScreenName());
            Printer.print(tweet.getRetweetedStatus().getText());
        } else {
            Printer.print(tweet.getText());
        }
        if (!argsPars.isStreamMode() && !tweet.isRetweet() && tweet.getRetweetCount() != 0) {
            Printer.print(" (");
            TimeParser.rightWordPrinting(tweet.getRetweetCount(), RT_MODE);
            Printer.print(")");
        }
        Printer.printLine();
    }
}

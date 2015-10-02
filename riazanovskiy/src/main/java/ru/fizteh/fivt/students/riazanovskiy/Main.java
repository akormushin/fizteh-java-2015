package ru.fizteh.fivt.students.riazanovskiy;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import java.util.concurrent.TimeUnit;

import static org.fusesource.jansi.Ansi.ansi;

public class Main {
    public static void main(String[] args) {
        ArgumentParser argumentParser = new ArgumentParser();
        try {
            new JCommander(argumentParser, args);
        } catch (ParameterException ignored) {
            new JCommander(argumentParser).usage();
            return;
        }

        if (argumentParser.isHelp()) {
            new JCommander(argumentParser).usage();
            return;
        }
        AnsiConsole.systemInstall();
        printTweets(argumentParser);
    }

    private static void printTweets(ArgumentParser argumentParser) {
        if (argumentParser.isStream()) {
            TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
            twitterStream.addListener(new StatusAdapter() {
                @Override
                public void onStatus(Status status) {
                    if (!status.isRetweet() || !argumentParser.isHideRetweets()) {
                        printSingleTweet(status, argumentParser.isStream());
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            });

            twitterStream.sample();
        }
    }

    private static void printSingleTweet(Status status, boolean showTime) {
        Ansi formattedTweet = ansi();
        if (showTime) {
            formattedTweet.a(RecentDateFormatter.format(status.getCreatedAt()) + " ");
        }

        formattedTweet.fg(Ansi.Color.BLUE).a("@" + status.getUser().getScreenName()).fg(Ansi.Color.DEFAULT);
        formattedTweet.a(": ");

        if (status.isRetweet()) {
            formattedTweet.a("ретвитнул ");
            formattedTweet.fg(Ansi.Color.BLUE).a("@" + status.getRetweetedStatus().getUser().getScreenName());
            formattedTweet.fg(Ansi.Color.DEFAULT).a(": " + status.getText());
        } else {
            formattedTweet.a(status.getText());
            if (status.isRetweeted()) {
                formattedTweet.a(" (").a(status.getRetweetCount()).a(" ретвитов)");
            }
        }

        System.out.println(formattedTweet);
        System.out.println(new String(new char[140]).replace('\0', '-'));
    }

}

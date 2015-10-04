package ru.fizteh.fivt.students.riazanovskiy;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import twitter4j.*;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static org.fusesource.jansi.Ansi.ansi;

class Main {
    private static final int DELIMITER_LENGTH = 140;
    private static final int MAXIMUM_TRIES = 3;

    public static void main(String[] args) {
        ArgumentParser argumentParser = new ArgumentParser();
        try {
            new JCommander(argumentParser, args);
        } catch (ParameterException ignored) {
            new JCommander(new ArgumentParser()).usage();
            return;
        }

        if (argumentParser.isHelp() || (argumentParser.getLimit() != null && argumentParser.isStream())) {
            new JCommander(new ArgumentParser()).usage();
            return;
        }

        AnsiConsole.systemInstall();

        for (int tries = 0; tries < MAXIMUM_TRIES; tries++) {
            try {
                printTweets(argumentParser);
                System.exit(0);
            } catch (TwitterException e) {
                System.err.println("Trying to reconnect");
            }
        }
        System.err.println("Giving up after " + MAXIMUM_TRIES + " tries");
    }

    private static void printTweets(ArgumentParser argumentParser) throws TwitterException {
        if (argumentParser.isStream()) {
            printTweetsInStream(argumentParser);
        } else {
            printTweetsByQuery(argumentParser);
        }
    }

    private static void printTweetsByQuery(ArgumentParser argumentParser) throws TwitterException {
        Twitter twitter = TwitterFactory.getSingleton();
        Query query = new Query(argumentParser.getKeywords());
        if (argumentParser.getLimit() != null) {
            query.setCount(argumentParser.getLimit());
        }

        QueryResult result = twitter.search(query);
        if (result.getCount() == 0) {
            System.out.println("Нет твитов по запросу " + query);
            return;
        }

        result.getTweets().stream().filter(status -> !status.isRetweet()
                || argumentParser.isShowRetweets()).forEach(status -> printSingleTweet(status, true));
    }

    private static void printTweetsInStream(final ArgumentParser argumentParser) {
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(new StatusAdapter() {
            @Override
            public void onStatus(Status status) {
                if (!status.isRetweet() || argumentParser.isShowRetweets()) {
                    printSingleTweet(status, false);
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });

        if (argumentParser.getKeywords().isEmpty() && argumentParser.getLocation().isEmpty()) {
            twitterStream.sample();
        } else {
            FilterQuery query = new FilterQuery().track(argumentParser.getKeywords());
            twitterStream.filter(query);
        }

        waitUntilEndOfInput();
    }

    private static void waitUntilEndOfInput() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNext()) {
                scanner.next();
                Thread.yield();
            }
        }
    }

    private static void printSingleTweet(Status status, boolean showTime) {
        Ansi formattedTweet = ansi();
        if (showTime) {
            formattedTweet.a("[" + RecentDateFormatter.format(status.getCreatedAt()) + "] ");
        }

        formattedTweet.fg(Ansi.Color.BLUE).a("@" + status.getUser().getScreenName()).fg(Ansi.Color.DEFAULT);
        formattedTweet.a(": ");

        if (status.isRetweet()) {
            formattedTweet.a("ретвитнул ");
            formattedTweet.fg(Ansi.Color.BLUE).a("@" + status.getRetweetedStatus().getUser().getScreenName());
            formattedTweet.fg(Ansi.Color.DEFAULT).a(": " + status.getText());
        } else {
            formattedTweet.a(status.getText());
            if (status.getRetweetCount() > 0) {
                formattedTweet.a(" (").a(status.getRetweetCount()).a(" ");
                formattedTweet.a(RussianWordForms.getWordForm("ретвит", status.getRetweetCount())).a(")");
            }
        }

        System.out.println(formattedTweet);
        System.out.println(new String(new char[DELIMITER_LENGTH]).replace('\0', '-'));
    }
}

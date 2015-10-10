package ru.fizteh.fivt.students.riazanovskiy.TwitterStream;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.bytebybyte.google.geocoding.service.response.LatLng;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Color;
import org.fusesource.jansi.AnsiConsole;
import twitter4j.*;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static org.fusesource.jansi.Ansi.ansi;

class Main {
    private static final int DELIMITER_LENGTH = 140;
    private static final int MAXIMUM_TRIES = 3;
    private static LatLng currentLocation;

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

        if (argumentParser.getLimit() == null) {
            argumentParser.setLimit(Integer.MAX_VALUE);
        }

        if (!argumentParser.getLocation().isEmpty()) {
            currentLocation = GeocodeWrapper.getCoordinatesByString(argumentParser.getLocation());
        }

        AnsiConsole.systemInstall();

        for (int tries = 0; tries < MAXIMUM_TRIES; tries++) {
            try {
                printTweets(argumentParser);
                System.exit(0);
            } catch (TwitterException ignored) {
                System.err.println("Trying to reconnect");
            }
        }
        System.err.println("Giving up after " + MAXIMUM_TRIES + " tries");
    }

    static void printTweets(ArgumentParser argumentParser) throws TwitterException {
        if (argumentParser.isStream()) {
            printTweetsInStream(argumentParser);
        } else {
            printTweetsByQuery(argumentParser);
        }
    }

    static void printTweetsByQuery(ArgumentParser argumentParser) throws TwitterException {
        if (argumentParser.getKeywords().isEmpty()) {
            System.err.println("Пустой запрос. Попробуйте --stream, если хотите видеть все твиты подряд");
            return;
        }
        Twitter twitter = TwitterFactory.getSingleton();
        Query query = new Query(argumentParser.getKeywords());
        query.setCount(argumentParser.getLimit());

        QueryResult result = twitter.search(query);
        if (result.getCount() == 0) {
            System.out.println("Нет твитов по запросу " + query);
            return;
        }

        result.getTweets().stream().filter(status -> shouldShowTweet(argumentParser, status)).forEach(status
                -> printSingleTweet(status, true));
    }

    static boolean shouldShowTweet(ArgumentParser argumentParser, Status status) {
        return (!status.isRetweet() || argumentParser.isShowRetweets())
                && ((currentLocation == null || (status.getGeoLocation() != null
                && GeocodeWrapper.isNearby(currentLocation,
                new GeocodeWrapper.Location(status.getGeoLocation().getLatitude(),
                        status.getGeoLocation().getLongitude())))));
    }

    static void printTweetsInStream(ArgumentParser argumentParser) {
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();

        twitterStream.addListener(new StatusAdapter() {
            @Override
            public void onStatus(Status status) {
                if (shouldShowTweet(argumentParser, status)) {
                    printSingleTweet(status, false);
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException ignored) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });

        if (argumentParser.getKeywords().isEmpty()) {
            twitterStream.sample();
        } else {
            FilterQuery query = new FilterQuery().track(argumentParser.getKeywords());
            twitterStream.filter(query);
        }

        waitUntilEndOfInput();
    }

    static void waitUntilEndOfInput() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNext()) {
                scanner.next();
                Thread.yield();
            }
        }
    }

    static void printSingleTweet(Status status, boolean showTime) {
        Ansi formattedTweet = ansi();
        if (showTime) {
            formattedTweet.a('[' + RecentDateFormatter.format(status.getCreatedAt()) + "] ");
        }

        formattedTweet.fg(Color.BLUE).a('@' + status.getUser().getScreenName()).fg(Color.DEFAULT);
        formattedTweet.a(": ");

        if (status.isRetweet()) {
            formattedTweet.a("ретвитнул ");
            formattedTweet.fg(Color.BLUE).a('@' + status.getRetweetedStatus().getUser().getScreenName());
            formattedTweet.fg(Color.DEFAULT).a(": " + status.getRetweetedStatus().getText());
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

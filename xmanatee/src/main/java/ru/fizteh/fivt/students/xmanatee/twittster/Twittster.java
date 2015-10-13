package ru.fizteh.fivt.students.xmanatee.twittster;

import twitter4j.*;
import java.util.*;
import static java.lang.Thread.sleep;

public class Twittster {
    public static final int MAX_NUMBER_OF_TRIES = 5;

    public static void main(String[] args) {
        System.err.println(ANSI_YELLOW + "YOU'RE RUNNING TWITTSTER" + ANSI_RESET);
        Parameters parameters = new Parameters(args);

        if (parameters.isHelp()) {
            parameters.runHelp();
            return;
        }

        try {
            if (parameters.isStream()) {
                runStreamer(parameters);
            } else {
                runSearch(parameters);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }

    public static void runSearch(Parameters parameters) throws Exception {
        Twitter twitter = new TwitterFactory().getInstance();
        Query query = composeQuery(parameters);

        QueryResult result = null;

        Integer tryNumber = 0;
        while (tryNumber < MAX_NUMBER_OF_TRIES) {
            tryNumber++;
            try {
                result = twitter.search(query);
                break;
            } catch (TwitterException e) {
                System.err.println("Problems with searching : " + e.getMessage());
            }
        }
        if (tryNumber == MAX_NUMBER_OF_TRIES) {
            throw new Exception("Can't search... Check your keys or something");
        }

        List<Status> tweets = result.getTweets();
        if (tweets.size() == 0) {
            System.err.println("No results");
        } else {
            for (Status tweet : tweets) {
                displayTweet(tweet, true);
            }
        }
    }

    static final int DELAY_X = 1000;
    public static void runStreamer(Parameters parameters) throws Exception {
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();

        StatusAdapter listener = new StatusAdapter() {
            @Override
            public void onStatus(Status status) {
                displayTweet(status, false);
                try {
                    Thread.sleep(DELAY_X);
                } catch (InterruptedException e) {
                    System.err.println(e.getMessage());
                }
            }
            @Override
            public void onException(Exception ex) {
                System.err.println("Problems listening : " + ex.getMessage());
            }
        };

        twitterStream.addListener(listener);
        FilterQuery filter = composeFilter(parameters);
        twitterStream.filter(filter);

        try (Scanner scan = new Scanner(System.in)) {
            while (scan.hasNextLine()) {
                sleep(DELAY_X);
            }
        } catch (InterruptedException e) {
            System.err.println("Ctrl+D => shutting down twittster");
            twitterStream.cleanUp();
            twitterStream.shutdown();
        }
    }

    public static Query composeQuery(Parameters parameters) throws Exception {
        Query query = new Query();
        String queryText = parameters.getQuery();
        query.setCount(parameters.getLimit());
        if (parameters.noRetweets()) {
            queryText += " +exclude:retweets";
        }
        query.setQuery(queryText);
        if (!parameters.getPlace().isEmpty()) {
            GoogleFindPlace gfm = new GoogleFindPlace(parameters.getPlace());

            GeoLocation gl = new GeoLocation(gfm.getLocation().lat, gfm.getLocation().lng);
            query.setGeoCode(gl, gfm.getRadius(), Query.Unit.km);
        }
        return query;
    }

    public static FilterQuery composeFilter(Parameters parameters) throws Exception {
        FilterQuery filter = new FilterQuery();
        filter.track(parameters.getQuery());

        if (!parameters.getPlace().isEmpty()) {
            GoogleFindPlace gmp = new GoogleFindPlace(parameters.getPlace());
            filter.locations(gmp.getBounds());

        }
        return filter;
    }

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";

    public static final Integer RT_PREFIX_LENGTH = 3;
    public static void displayTweet(Status tweet, boolean showTime) {
        StringBuilder builder = new StringBuilder();
        String tweetText = tweet.getText();
        if (showTime) {
            builder.append("[")
                    .append(new AdvTimeParser(tweet.getCreatedAt()).get())
                    .append("] ");
        }
        builder.append("@")
                .append(tweet.getUser().getScreenName())
                .append(": ");

        if (tweet.isRetweet()) {
            builder.append("ретвитнул ");
            tweetText = tweetText.substring(RT_PREFIX_LENGTH);
        }
        builder.append(tweetText);

        Integer retweetedCount = tweet.getRetweetCount();
        if (!tweet.isRetweet() & (retweetedCount > 0)) {
            Word4declension retweetWord = new Word4declension("ретвит", "ретвита", "ретвитов");
            builder.append(ANSI_GREEN)
                    .append(" (")
                    .append(retweetedCount)
                    .append(" ")
                    .append(retweetWord.declension4Number(retweetedCount))
                    .append(")")
                    .append(ANSI_RESET);
        }

        String output = builder.toString();
        output = output.replaceAll("@(\\w+): ", ANSI_BLUE + "@$1" + ANSI_RESET + ": ");
        System.err.println(output);
    }

}

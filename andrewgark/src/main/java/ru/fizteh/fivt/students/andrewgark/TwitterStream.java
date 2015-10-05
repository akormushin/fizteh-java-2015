package ru.fizteh.fivt.students.andrewgark;

import com.beust.jcommander.JCommander;
import twitter4j.*;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.joining;
import static ru.fizteh.fivt.students.andrewgark.GeolocationSearch.getCoordinatesByIp;
import static ru.fizteh.fivt.students.andrewgark.GeolocationSearch.getCoordinatesByQuery;
import static ru.fizteh.fivt.students.andrewgark.TSWordsForm.getTimeForm;
import static ru.fizteh.fivt.students.andrewgark.TSWordsForm.retweetsForm;

public class TwitterStream {
    private static final Double RADIUS = 5.0;
    private static final Integer NUMBER_TWEETS = 100;
    private static final String SEPARATOR =
            "----------------------------------------------------------------------------------------";


    public static class ParameterException extends Exception {
        public ParameterException(String message) {
            super(message);
        }
    }

    public static void main(String[] args) {
        JCommanderTwitterStream jcts = new JCommanderTwitterStream();
        new JCommander(jcts, args);
        if (jcts.isHelp()) {
            JCommander jc = new JCommander(jcts, args);
            jc.usage();
            return;
        }
        String stringKeywords = jcts.getKeywords().stream().collect(joining(" "));
        if (jcts.isStream()) {
            try {
                if (jcts.getLimit() < Integer.MAX_VALUE) {
                    throw new ParameterException("You can't have stream with limit.");
                }
                streamTweets(stringKeywords, jcts.getLocation(), jcts.isHideRetweets());
            } catch (URLs.ConnectionException | URLs.HTTPQueryException | MalformedURLException | ParameterException
                    | GeolocationSearch.NoKeyException | GeolocationSearch.SearchLocationException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
        } else {
            try {
                printTweets(stringKeywords, jcts.getLocation(), jcts.getLimit(), jcts.isHideRetweets());
            } catch (TwitterException | URLs.ConnectionException | GeolocationSearch.SearchLocationException
                    | MalformedURLException | GeolocationSearch.NoKeyException | URLs.HTTPQueryException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }
    }

    public static void streamTweets(String keywords, String location, Boolean hideRetweets)
            throws URLs.ConnectionException, URLs.HTTPQueryException, GeolocationSearch.NoKeyException,
            GeolocationSearch.SearchLocationException, MalformedURLException {
        twitter4j.TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(new StatusAdapter() {
            public void onStatus(Status status) {
                if (!(status.isRetweet() && hideRetweets)) {
                    printTweet(status, true);
                }
            }
        });
        Double[] coordinates = getCoordinatesByQuery(location);
        double[][] doubleCoordinates = {{coordinates[1] - RADIUS, coordinates[0] - RADIUS},
                {coordinates[1] + RADIUS, coordinates[0] + RADIUS}};
        String[] arrayKeywords = {keywords};
        FilterQuery fq = new FilterQuery();
        fq.locations(doubleCoordinates);
        fq.track(arrayKeywords);
        twitterStream.filter(fq);
    }

    public static void printTweets(String keywords, String location, Integer limit, Boolean hideRetweets)
            throws TwitterException, URLs.ConnectionException, GeolocationSearch.SearchLocationException,
            MalformedURLException, GeolocationSearch.NoKeyException, URLs.HTTPQueryException {
        Twitter twitter = new TwitterFactory().getInstance();
        Query query = new Query(keywords);
        query.setGeoCode(getLocation(location), RADIUS, Query.KILOMETERS);
        Integer needTweets;
        Integer numberTweets = 0;
        if (limit == Integer.MAX_VALUE) {
            needTweets = NUMBER_TWEETS;
        } else {
            needTweets = limit;
        }
        QueryResult result;
        List<Status> tweets;

        do {
            result = twitter.search(query);
            tweets = result.getTweets();
            for (Status tweet : tweets) {
                if (!(hideRetweets && tweet.isRetweet()) && (numberTweets < needTweets)) {
                    printTweet(tweet, false);
                    numberTweets++;
                }
            }
            query = result.nextQuery();
        } while ((numberTweets < needTweets) && (query != null));

        if (numberTweets == 0) {
            System.out.println("Твитов по заданному запросу не найдено.");
        } else {
            System.out.println(SEPARATOR);
        }
    }

    public static GeoLocation getLocation(String location) throws URLs.ConnectionException,
            GeolocationSearch.SearchLocationException, MalformedURLException, URLs.HTTPQueryException,
            GeolocationSearch.NoKeyException {
        Double[] coordinates;
        if (Objects.equals(location, "nearby")) {
            coordinates = getCoordinatesByIp();
        } else {
            coordinates = getCoordinatesByQuery(location);
        }
        return new GeoLocation(coordinates[0], coordinates[1]);
    }

    public static void printTweet(Status tweet, boolean isStream) {
        String time;
        if (isStream) {
            time = "";
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        } else {
            time = getTimeForm(tweet) + " ";
        }
        System.out.println(SEPARATOR);
        String uName = tweet.getUser().getScreenName();
        String text = tweet.getText();
        Integer retweets = tweet.getRetweetCount();
        if (tweet.isRetweet()) {
            Pattern myPattern = Pattern.compile("RT @([^ ]*): (.*)");
            Matcher m = myPattern.matcher(text);
            m.find();
            String uRTName = m.group(1);
            text = m.group(2);
            System.out.println(time + "@" + uName + ": ретвитнул @" + uRTName + ": " + text);
        } else {
            System.out.println(time + "@" + uName + ": " + text + retweetsForm(retweets));
        }
    }
}


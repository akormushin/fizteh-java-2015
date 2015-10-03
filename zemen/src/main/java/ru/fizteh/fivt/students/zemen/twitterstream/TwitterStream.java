package ru.fizteh.fivt.students.zemen.twitterstream;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.apache.log4j.Logger;
import org.apache.log4j.varia.NullAppender;
import twitter4j.*;

/**
 * Twitter streaming.
 */
public class TwitterStream {
    public static final int KM_RADIUS = 50;

    private static void streamTweets(Arguments arguments,
                                     GeoLocation location) {
        StatusListener statusListener = new StatusAdapter() {
            @Override
            public void onStatus(Status status) {
                if (arguments.isHideRetweets() && status.isRetweet()) {
                    return;
                }
                GeoLocation statusLocation = status.getGeoLocation();

                if (statusLocation == null) {
                    String currentAddress = status.getUser().getLocation();
                    try {
                        statusLocation = GeoUtils
                                .getGeoLocationByAddress(currentAddress);
                    } catch (LocationNotFoundException e) {
                        statusLocation = null;
                    }
                }
                if (statusLocation == null) {
                    return;
                }
                double distance = GeoUtils.sphereDistance(
                        statusLocation.getLatitude(),
                        statusLocation.getLongitude(),
                        location.getLatitude(),
                        location.getLongitude());
                if (distance > KM_RADIUS) {
                    return;
                }
                System.out.println(FormatUtils.formatAll(status, arguments));
            }
        };
        twitter4j.TwitterStream twitterStream =
                new TwitterStreamFactory().getInstance();
        twitterStream.addListener(statusListener);
        String[] track = new String[arguments.getKeywords().size()];
        arguments.getKeywords().toArray(track);
        twitterStream.filter(new FilterQuery(0, new long[0], track));
    }

    public static void main(String[] argv) {
        //BasicConfigurator.configure();
        Logger.getRootLogger().addAppender(new NullAppender());
        Arguments arguments = new Arguments();
        JCommander jCommander;
        try {
            jCommander = new JCommander(arguments, argv);
        } catch (ParameterException ex) {
            jCommander = new JCommander(arguments);
            jCommander.usage();
            return;
        }
        if (arguments.isHelp()) {
            jCommander.usage();
            return;
        }

        GeoLocation location;
        try {
            if (arguments.getLocation().equals("nearby")) {
                location = GeoUtils.getGeoLocationByIP();
            } else {
                location = GeoUtils.getGeoLocationByAddress(arguments.getLocation());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (arguments.isStream()) {
            streamTweets(arguments, location);
            return;
        }

        Twitter twitter = TwitterFactory.getSingleton();
        Query query = new Query(String.join(" ", arguments.getKeywords()));
        query.setGeoCode(location, KM_RADIUS, Query.KILOMETERS);
        QueryResult response;
        try {
            response = twitter.search(query);
        } catch (TwitterException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Твиты по запросу " + query.getQuery() + " для " + arguments.getLocation());

        if (response.getTweets().isEmpty()) {
            System.out.println("Не найдены :(");
            return;
        }
        int remainingTweets = arguments.getLimit();
        for (Status tweet : response.getTweets()) {
            if (tweet.isRetweet() && arguments.isHideRetweets()) {
                continue;
            }
            System.out.println(FormatUtils.formatAll(tweet, arguments));
            if (remainingTweets >= 0) {
                --remainingTweets;
                if (remainingTweets <= 0) {
                    break;
                }
            }
        }
    }
}


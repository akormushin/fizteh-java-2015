package ru.fizteh.fivt.students.Jettriangle.twitterstream;

/**
 * Created by rtriangle on 01.10.15.
 */

import com.beust.jcommander.JCommander;
import twitter4j.*;

public class MainTwitter {

    private static final long MILLISEC_IN_SEC = 1000;

    public static void main(String[] argv) throws TwitterException {
        JCommanderTwitter jct = new JCommanderTwitter();
        try {
            JCommander jcparser = new JCommander(jct, argv);
            if (jct.isHelp()) {
                jcparser.usage();
            }

        } catch (Exception e) {
            e.printStackTrace();
            new JCommander(jct).usage();
        }
        if (jct.isStream()) {
            printStream(jct);
        } else {
            printTweets(jct);
        }
    }

    public static void printTweets(JCommanderTwitter jct) {

        LocationBuilder locationBuilder = new LocationBuilder();
        Location location = locationBuilder.getLocation(jct.getPlace());

        if (location.getError() != 0) {
            System.err.println("Bad location");
            System.exit(1);
        }

        GeoLocation geoLocation = new GeoLocation(location.getLatitudeCenter(), location.getLongitudeCenter());
        Twitter twitter = new TwitterFactory().getInstance();
        Query query = new Query(jct.getQuery());
        query.setCount(jct.getTweetsLimit());
        Query.Unit resUnit = Query.Unit.km;
        query.setGeoCode(geoLocation, location.getRes(), resUnit);
        query.setCount(jct.getTweetsLimit());
        FormatFactory formatter = new FormatFactory();

        try {
            twitter.search(query).getTweets().stream()
                    .map(s -> formatter.getTweetFormat(s, jct)).forEach(System.out::print);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    public static void printStream(JCommanderTwitter jct) {
        FormatFactory formatter = new FormatFactory();
        LocationBuilder locationBuilder = new LocationBuilder();
        Location location = locationBuilder.getLocation(jct.getPlace());
        if (location.getError() == 0) {
            FilterQuery filterQuery = new FilterQuery();
            String[] keyword = {jct.getQuery()};
            double[][] locationBox = {{location.getLongitudeSWCorner(), location.getLatitudeSWCorner()},
                    {location.getLongitudeNECorner(), location.getLatitudeNECorner()}};
            filterQuery.track(keyword);
            filterQuery.locations(locationBox);
            TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
            twitterStream.addListener(new StatusAdapter() {
                @Override
                public void onStatus(Status status) {
                    try {
                        Thread.sleep(MILLISEC_IN_SEC);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.print(formatter.getTweetFormat(status, jct));
                }
                @Override
                public void onException(Exception ex) {
                    TwitterException twitterex = (TwitterException) ex;
                    twitterex.printStackTrace();
                }
            });
            twitterStream.filter(filterQuery);
        } else {
            System.err.println("Bad location");
        }
    }
}

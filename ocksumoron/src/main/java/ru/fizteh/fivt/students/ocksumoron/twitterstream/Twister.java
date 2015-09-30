package ru.fizteh.fivt.students.ocksumoron.twitterstream;

import com.beust.jcommander.JCommander;
import twitter4j.*;

/**
 * Created by ocksumoron on 16.09.15.
 */

public class Twister {

    private static final long MILISEC_IN_SEC = 1000;

    public static void main(String[] args)  {
        JCommanderProperties jcp = new JCommanderProperties();
        new JCommander(jcp, args);
        if (jcp.isPrintHelp()) {
            new JCommander(jcp, args).usage();
        }
        if (!jcp.isStream()) {
            printTweets(jcp);
        } else {
            printStream(jcp);
        }
    }

    public static void printTweets(JCommanderProperties jcp) {
        LocationMaster locationMaster = new LocationMaster();
        try {
            Location location = locationMaster.getLocation(jcp.getPlace());
            GeoLocation geoLocation = new GeoLocation(location.getLatitudeCenter(), location.getLongitudeCenter());
            Twitter twitter = new TwitterFactory().getInstance();
            Query query = new Query(jcp.getQuery());
            query.setCount(jcp.getLimitNumber());
            Query.Unit resUnit = Query.Unit.km;
            query.setGeoCode(geoLocation, location.getRes(), resUnit);
            query.setCount(jcp.getLimitNumber());
            FormatMaster formatter = new FormatMaster();
            twitter.search(query).getTweets().stream()
                    .map(s -> formatter.format(s, jcp.isHideRetweets(), false)).forEach(System.out::print);

        } catch (TwitterException e) {

        }
    }

    public static void printStream(JCommanderProperties jcp) {
        FormatMaster formatter = new FormatMaster();
        LocationMaster locationMaster = new LocationMaster();
        Location location = locationMaster.getLocation(jcp.getPlace());
        FilterQuery filterQuery = new FilterQuery();
        String[] keyword = {jcp.getQuery()};

        double[][] locationBox = {{location.getLongitudeSWCorner(), location.getLatitudeSWCorner()},
                                {location.getLongitudeNECorner(), location.getLatitudeNECorner()}};

        filterQuery.track(keyword);

        filterQuery.locations(locationBox);

        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(new StatusAdapter() {
            @Override
            public void onStatus(Status status) {
                try {
                    Thread.sleep(MILISEC_IN_SEC);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print(formatter.format(status, jcp.isHideRetweets(), true));
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        });
        twitterStream.filter(filterQuery);
    }
}

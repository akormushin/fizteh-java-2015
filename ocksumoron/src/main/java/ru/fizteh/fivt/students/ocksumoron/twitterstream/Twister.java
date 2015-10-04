package ru.fizteh.fivt.students.ocksumoron.twitterstream;

import com.beust.jcommander.JCommander;
import twitter4j.*;

/**
 * Created by ocksumoron on 16.09.15.
 */

public class Twister {

    private static final long MILLISEC_IN_SEC = 1000;

    public static void main(String[] args)  {
        JCommanderProperties jcp = new JCommanderProperties();
        try {
            JCommander jParser = new JCommander(jcp, args);
            if (jcp.isPrintHelp()) {
                jParser.usage();
            }
            if (!jcp.isStream()) {
                printTweets(jcp);
            } else {
                printStream(jcp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            new JCommander(jcp).usage();
        }
    }

    public static void printTweets(JCommanderProperties jcp) {
        LocationMaster locationMaster = new LocationMaster();
        try {
            Location location = locationMaster.getLocation(jcp.getPlace());
            if (location.getError() != 0) {
                System.err.println("Bad location");
                System.exit(1);
            }
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
            e.printStackTrace();
        }
    }

    public static void printStream(JCommanderProperties jcp) {
        FormatMaster formatter = new FormatMaster();
        LocationMaster locationMaster = new LocationMaster();
        Location location = locationMaster.getLocation(jcp.getPlace());
        if (location.getError() == 0) {
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
                        Thread.sleep(MILLISEC_IN_SEC);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.print(formatter.format(status, jcp.isHideRetweets(), true));
                }

                @Override
                public void onException(Exception ex) {
                    TwitterException twex = (TwitterException) ex;
                    twex.printStackTrace();
                }
            });
            twitterStream.filter(filterQuery);
        } else {
            System.err.println("Bad location");
        }
    }
}

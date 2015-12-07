package ru.fizteh.fivt.students.zerts.moduletests.library;

import ru.fizteh.fivt.students.zerts.TwitterStream.TwitterReader;
import ru.fizteh.fivt.students.zerts.TwitterStream.exceptions.GeoExeption;
import ru.fizteh.fivt.students.zerts.TwitterStream.exceptions.NoQueryExeption;
import twitter4j.*;

import java.io.IOException;

public class TwitterStream {
    public static void stream(ArgsParser argsPars) throws NoQueryExeption, GeoExeption {
        if (argsPars.getQuery() == null) {
            throw new NoQueryExeption();
        }
        twitter4j.TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(new StatusAdapter() {
            @Override
            public void onStatus(Status status) {
                if (argsPars.getPlace() == null) {
                    TweetPrinter.printTweet(status, argsPars, true);
                } else {
                    GeoLocation tweetLocation = null;
                    if (status.getGeoLocation() != null) {
                        tweetLocation = new GeoLocation(status.getGeoLocation().getLatitude(),
                                status.getGeoLocation().getLongitude());
                    } else if (!status.getUser().getLocation().isEmpty()) {
                        try {
                            //System.out.println(status.getUser().getLocation());
                            try {
                                tweetLocation = GeoParser.getCoordinates(status.getUser().getLocation());
                            } catch (GeoExeption | InterruptedException | JSONException e) {
                                e.printStackTrace();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        return;
                    }
                    try {
                        GeoLocation queryLocation = null;
                        try {
                            queryLocation = GeoParser.getCoordinates(argsPars.getPlace());
                        } catch (GeoExeption | InterruptedException | JSONException e) {
                            e.printStackTrace();
                        }
                        if (queryLocation == null) {
                            throw new GeoExeption();
                        }
                        if (tweetLocation == null) {
                            return;
                        }
                        if (GeoParser.near(tweetLocation, queryLocation, TwitterReader.getLocateRadius())) {
                            TweetPrinter.printTweet(status, argsPars, true);
                        }
                    } catch (IOException | GeoExeption e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        String[] trackArray = argsPars.getQuery().toArray(new String[argsPars.getQuery().size()]);
        twitterStream.filter(new FilterQuery().track(trackArray));
    }

}

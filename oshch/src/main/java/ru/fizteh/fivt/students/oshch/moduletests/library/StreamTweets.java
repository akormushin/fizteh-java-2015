package ru.fizteh.fivt.students.oshch.moduletests.library;

import twitter4j.*;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class StreamTweets {
    private static twitter4j.TwitterStream twitterStream = TwitterStreamFactory.getSingleton();

    public StreamTweets(twitter4j.TwitterStream ts) {
        twitterStream = ts;
    }

    private static FilterQuery setFilter(Parameters param) throws Exception {
        String[] trackArray = new String[1];
        trackArray[0] = param.getQuery();
        long[] followArray = new long[0];
        FilterQuery filter = new FilterQuery(0, followArray, trackArray);

        if (!param.getPlace().isEmpty()) {
            PlaceApi googleFindPlace;
            googleFindPlace = new PlaceApi(param.getPlace());
            double[][] bounds = {{googleFindPlace.getBounds().southwest.lng,
                    googleFindPlace.getBounds().southwest.lat},
                    {googleFindPlace.getBounds().northeast.lng,
                            googleFindPlace.getBounds().northeast.lat}};
            filter.locations(bounds);
        }
        return filter;
    }

    public static void stream(Parameters param, Consumer<Status> printer) throws Exception {
        StatusAdapter listener = new StatusAdapter() {
            @Override
            public void onStatus(Status status) {
                if (param.isHideRt() && status.isRetweet()) {
                    return;
                }
                printer.accept(status);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (Exception e) {
                }

            }
        };
        twitterStream.addListener(listener);
        FilterQuery filter = setFilter(param);
        twitterStream.filter(filter);
    }
}

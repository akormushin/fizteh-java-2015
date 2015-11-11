package ru.fizteh.fivt.students.okalitova.moduletests.library;

import twitter4j.*;
import twitter4j.util.function.Consumer;

import static java.lang.Thread.sleep;

/**
 * Created by nimloth on 10.11.15.
 */
public class Stream {
    public static final int PAUSE = 1000;
    private static TwitterStream twitterStream;

    public Stream(TwitterStream twitterStream) {
        this.twitterStream = twitterStream;
    }
    public static FilterQuery setFilter(ParametersParser param) throws Exception {
        //set query filter
        String[] trackArray = new String[1];
        trackArray[0] = param.getQuery();
        long[] followArray = new long[0];
        FilterQuery filter = new FilterQuery(0, followArray, trackArray);
        //set place filter
        if (!param.getPlace().equals("")) {
            if (!param.getPlace().equals("nearby")) {
                PlaceParser placeParser;
                placeParser = new PlaceParser(param.getPlace());
                double[][] bounds = {{placeParser.getBounds().southwest.lng,
                        placeParser.getBounds().southwest.lat},
                        {placeParser.getBounds().northeast.lng,
                                placeParser.getBounds().northeast.lat}};
                filter.locations(bounds);
            } else {
                double[][] bounds = {{NearbyParser.getBounds().southwest.lng,
                        NearbyParser.getBounds().southwest.lat},
                        {NearbyParser.getBounds().northeast.lng,
                                NearbyParser.getBounds().northeast.lat}};
                filter.locations(bounds);
            }
        }
        return filter;
    }
    public static void streamResult(ParametersParser param, Consumer<String> outputStatuses) throws Exception {
        StatusListener listener = new StatusListener() {
            @Override
            public void onException(Exception e) {
            }

            @Override
            public void onStatus(Status status) {
                outputStatuses.accept(new StatusParser(param).getStatus(status));
                try {
                    sleep(PAUSE);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice
                                                 statusDeletionNotice) {
            }

            @Override
            public void onTrackLimitationNotice(int i) {
            }

            @Override
            public void onScrubGeo(long l, long l1) {
            }

            @Override
            public void onStallWarning(StallWarning stallWarning) {
            }
        };
        twitterStream.addListener(listener);
        if (param.getQuery().equals("") && param.getPlace().equals("")) {
            twitterStream.sample();
        } else {
            FilterQuery filter = setFilter(param);
            twitterStream.filter(filter);
        }
    }
}

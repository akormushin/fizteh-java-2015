package ru.fizteh.fivt.students.oshch.moduletests.library;

import twitter4j.*;
import twitter4j.util.function.Consumer;

import java.io.IOException;
import java.util.List;

public class SearchTweets {
    private static Twitter twitter = new TwitterFactory().getInstance();

    public SearchTweets(Twitter twitter) {
        this.twitter = twitter;
    }

    private static Query setQuery(Parameters param) throws IOException {
        Query query = new Query(param.getQuery());

        if (!param.getPlace().isEmpty()) {
            PlaceApi googleFindPlace;
            googleFindPlace = new PlaceApi(param.getPlace());
            GeoLocation geoLocation;
            geoLocation = new GeoLocation(googleFindPlace.getLocation().lat,
                    googleFindPlace.getLocation().lng);
            query.setGeoCode(geoLocation,
                    googleFindPlace.getRadius(), Query.KILOMETERS);
        }
        return query;
    }

    public static void search(Parameters param, Consumer<Status> printer) throws Exception {
        Query query = setQuery(param);
        QueryResult result;
        int limit = param.getLimit();
        do {
            result = twitter.search(query);
            List<Status> tweets = result.getTweets();
            for (Status status : tweets) {
                if (status.isRetweet() && param.isHideRt()) {
                    continue;
                }
                printer.accept(status);
                limit--;
                if (limit == 0) {
                    break;
                }
            }
            query = result.nextQuery();
        } while (query != null && limit > 0);
    }


}

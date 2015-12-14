package ru.fizteh.fivt.students.w4r10ck1337.moduletests.library;

import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.Twitter;

public class QueryCreator {
    private static final int Q_LIMIT = 100;

    public static double[] findLocation(String place, Twitter twitter) {
        while (true) {
            try {
                return GeoApi.getLocation(place, twitter);
            } catch (Exception e) { }
        }
    }

    public static Query createQuery(String query, int limit, double[] location) {
        Query searchQuery = new Query(query);
        searchQuery.setCount(Math.min(Q_LIMIT, limit));
        searchQuery.setQuery(query);
        searchQuery.geoCode(new GeoLocation(location[0], location[1]), location[2], "mi");
        return searchQuery;
    }
}

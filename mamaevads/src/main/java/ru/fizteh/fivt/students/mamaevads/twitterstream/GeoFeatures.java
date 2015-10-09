package ru.fizteh.fivt.students.mamaevads.twitterstream;

import twitter4j.*;
import java.util.List;

public class GeoFeatures {

    private static final double[][] MOSCOW = {{55.489926, 37.31932}, {56.009657, 37.9456611}};
    private static final double HALF_CIRCLE = 180.0;
    private static final double ONE = 60;
    private static final double TWO = 1.1515;
    private static final double KILOMETERS = 0.8684;
    public static double getBetween(double[][] square) {
        double lat1 = square[0][1];
        double lat2 = square[1][1];
        double lon1 = square[0][0];
        double lon2 = square[1][0];
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * ONE * TWO;
        dist = dist * KILOMETERS;

        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / HALF_CIRCLE);
    }

    private static double rad2deg(double rad) {
        return (rad * HALF_CIRCLE / Math.PI);
    }

    public static double[] getLocation(String place, Twitter twitter) {
        double[][] square = getFilter(place);
        double minimumLatitude = square[0][1];
        double minimumLongitude = square[0][0];
        double maximumLatitude = square[1][1];
        double maximumLongitude = square[1][0];
        return new double[]{
                (minimumLatitude + maximumLatitude) / 2,
                (minimumLongitude + maximumLongitude) / 2,
                getBetween(square) / 2
        };
    }

    public static double[][] getFilter(String place) {
        Twitter twitter = new TwitterFactory().getInstance();
        try {
            GeoQuery geoQuery = new GeoQuery((String) null);
            geoQuery.setQuery(place);
            List<Place> placeResponseList = twitter.searchPlaces(geoQuery);
            double minimumLatitude = Double.MAX_VALUE,
                    maximumLatitude = -Double.MAX_VALUE,
                    minimumLongitude = Double.MAX_VALUE,
                    maximumLongitude = -Double.MAX_VALUE;
            for (GeoLocation location : placeResponseList.get(0).getBoundingBoxCoordinates()[0]) {
                minimumLongitude = Math.min(minimumLongitude, location.getLongitude());
                maximumLongitude = Math.max(maximumLongitude, location.getLongitude());
                minimumLatitude = Math.min(minimumLatitude, location.getLatitude());
                maximumLatitude = Math.max(maximumLatitude, location.getLatitude());

            }

            double[][] coordinates = new double[2][2];
            coordinates[0][0] = minimumLongitude;
            coordinates[0][1] = minimumLatitude;
            coordinates[1][0] = maximumLongitude;
            coordinates[1][1] = maximumLatitude;
            return coordinates;
        } catch (Exception e) {
            System.err.println("Error, while trying to get location.");
        }
        return MOSCOW;
    }
}


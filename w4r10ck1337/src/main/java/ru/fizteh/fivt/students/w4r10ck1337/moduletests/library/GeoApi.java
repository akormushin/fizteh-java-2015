package ru.fizteh.fivt.students.w4r10ck1337.moduletests.library;

import twitter4j.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class GeoApi {
    private static final double DEFAULT_RADIUS = 15;
    private static final double MILES = 34.5;
    private static final int BUF_SIZE = 1000;
    private static final int INF = 1000000000;

    public static String parseLocation(String answer) {
        try {
            return answer.split("\"loc\": \"")[1].split("\"")[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            return "";
        }
    }

    public static double[] getLocationByIP() throws Exception {
        String location = "";
        try {
            URL url = new URL("http://ipinfo.io/geo");
            try (InputStream is = url.openStream()) {
                byte[] b = new byte[BUF_SIZE + 1];
                char[] c = new char[is.read(b) + 2];
                for (int i = 0; b[i] > 0 && i < BUF_SIZE; i++) {
                    c[i] = (char) b[i];
                }
                location = parseLocation(String.valueOf(c));
                try {
                    is.close();
                } catch (IOException e) { }
            } catch (IOException e) {
                throw e;
            }
        } catch (MalformedURLException e) {
            throw e;
        }

        try {
            return new double[]{
                    Double.parseDouble(location.split(",")[0]),
                    Double.parseDouble(location.split(",")[1]),
                    DEFAULT_RADIUS
            };
        } catch (NumberFormatException e) {
            throw e;
        }
    }

    public static double[] getLocationByName(String place, Twitter twitter) throws Exception {
        try {
            GeoQuery geoQuery = new GeoQuery((String) null);
            geoQuery.setQuery(place);
            List<Place> places = twitter.searchPlaces(geoQuery);
            if (places.size() == 0) {
                throw new Exception("Нет такого места");
            }
            double minlat = INF, maxlat = -INF, minlong = INF, maxlong = -INF;
            for (GeoLocation g : places.get(0).getBoundingBoxCoordinates()[0]) {
                minlat = Math.min(minlat, g.getLatitude());
                maxlat = Math.max(maxlat, g.getLatitude());
                minlong = Math.min(minlong, g.getLongitude());
                maxlong = Math.max(maxlong, g.getLongitude());
            }
            return new double[]{
                    (minlat + maxlat) / 2,
                    (minlong + maxlong) / 2,
                    Math.hypot(maxlat - minlat, maxlong - minlong) * MILES / 2
            };
        } catch (TwitterException e) {
            throw e;
        }
    }

    public static double[] getLocation(String place, Twitter twitter) throws Exception {
        if (place.equals("nearby")) {
            return getLocationByIP();
        }
        return getLocationByName(place, twitter);
    }
}

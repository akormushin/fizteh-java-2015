package ru.fizteh.fivt.students.w4r10ck1337.twitterstream;

import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.Place;
import twitter4j.Twitter;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class GeoApi {
    private static final double DEFAULT_RADIUS = 15;
    private static final double MILES = 34.5;
    private static final int BUF_SIZE = 1000;
    private static final int INF = 1000000000;

    public static double[] getLocationByIP() {
        String location = "";
        try {
            URL url = new URL("http://ipinfo.io/geo");
            try (InputStream is = url.openStream()) {
                byte[] b = new byte[BUF_SIZE + 1];
                char[] c = new char[is.read(b) + 2];
                for (int i = 0; b[i] > 0 && i < BUF_SIZE; i++) {
                    c[i] = (char) b[i];
                }
                location = String.valueOf(c).
                        split("\"loc\": \"")[1].split("\"")[0];
                try {
                    is.close();
                } catch (Exception e) {
                    System.err.println("Соединение с ipinfo не закрылось");
                }
            } catch (Exception e) {
                System.err.println(
                        "Не получается определить местоположение,"
                                + " попробуйте использовать --place");
                return null;
            }
        } catch (MalformedURLException e) {
            System.err.println(
                    "http уже не считается протоколом, а вы используете мою программу О_о"
            );
        }


        return new double[]{
                Double.parseDouble(location.split(",")[0]),
                Double.parseDouble(location.split(",")[1]),
                DEFAULT_RADIUS
        };
    }

    public static double[] getLocationByName(String place, Twitter twitter) {
        try {
            GeoQuery geoQuery = new GeoQuery((String) null);
            geoQuery.setQuery(place);
            List<Place> places = twitter.searchPlaces(geoQuery);
            if (places.size() == 0) {
                System.err.println("Нет такого места");
                return new double[] {
                    0, 0, 0
                };
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
        } catch (Exception e) {
            System.err.println("Нет соединения с твиттером");
        }
        return null;
    }

    public static double[] getLocation(String place, Twitter twitter) {
        if (place.equals("nearby")) {
            return getLocationByIP();
        }
        return getLocationByName(place, twitter);
    }
}

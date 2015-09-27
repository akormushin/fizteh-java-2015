package ru.fizteh.fivt.students.zerts.TwitterStream;

import twitter4j.GeoLocation;

import java.io.*;
import java.net.URL;
import java.math.*;

import static java.lang.Double.parseDouble;

/**
 * Created by User on 21.09.2015.
 */

public class GeoParser {
    private static String getKey() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("../fizteh-java-2015/zerts/src/main/resources/yandexkey.properties"));
        String key = in.readLine();
        System.out.println(key);
        return key;
    }
    public static GeoLocation getCoordinates(String place) throws IOException {
        URL getTheLL =
                new URL("https://geocode-maps.yandex.ru/1.x/?geocode=" + place
                        + "&apikey=" + getKey());
        BufferedReader in = new BufferedReader(new InputStreamReader(getTheLL.openStream()));
        String xmlParse;
        do {
            xmlParse = in.readLine();
            //System.out.println(xmlParse);
            if (xmlParse != null && xmlParse.contains("<pos>")) {
                break;
            }
        } while (xmlParse != null);
        in.close();
        if (xmlParse == null) {
            return null;
        }
        int i = 0;
        while (xmlParse.charAt(i) != '>') {
            i++;
        }
        i++;
        String currLattitude = "", currLongtitude = "";
        while (xmlParse.charAt(i) != ' ') {
            currLongtitude += xmlParse.charAt(i);
            i++;
        }
        i++;
        while (xmlParse.charAt(i) != '<') {
            currLattitude += xmlParse.charAt(i);
            i++;
        }
        //System.out.println(currLattitude + " " + currLongtitude);
        return new GeoLocation(parseDouble(currLattitude), parseDouble(currLongtitude));
    }
    static double sqr(double number) {
        return number * number;
    }
    static final double EARTH_RADIUS = 6371;
    static final double RADIANS_IN_DEGREE = Math.PI / 180;
    private static double toRadians(double angle) {
        return angle * RADIANS_IN_DEGREE;
    }
    public static boolean near(GeoLocation first, GeoLocation second, double radius) {
        double firstLatitude = toRadians(first.getLatitude());
        double firstLongtitute = toRadians(first.getLongitude());
        double secondLatitude = toRadians(second.getLatitude());
        double secondLongtitude = toRadians(second.getLongitude());
        double deltaPhi = secondLatitude - firstLatitude;
        double deltaLambda = secondLongtitude - firstLongtitute;

        double distance = 2 * Math.asin(Math.sqrt(sqr(Math.sin(deltaPhi / 2))
                + Math.cos(firstLatitude) * Math.cos(secondLatitude) * sqr(Math.sin(deltaLambda / 2)))) * EARTH_RADIUS;
        System.out.println(distance);
        return distance < radius;
    }
}

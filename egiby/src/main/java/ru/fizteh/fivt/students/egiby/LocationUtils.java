package ru.fizteh.fivt.students.egiby;

import twitter4j.GeoLocation;
import twitter4j.JSONObject;

import java.io.*;

/**
 * Created by egiby on 04.10.15.
 */
public class LocationUtils {
    private static final double[][] MOSCOW = {{55.48992699999999, 37.3193288}, {56.009657, 37.9456611}};
    private static final String URL = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=";

    private static String getAPIKey() {
        try {
            BufferedReader input = new BufferedReader(new FileReader("GoogleMapsAPI.properties"));
            String key = input.readLine();
            input.close();
            return key;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static JSONObject getGoogleAPIQuery(String location) {
        String key = getAPIKey();
        String json = HttpQuery.getQuery(URL + location + "&key=" + key);
        return new JSONObject();
    }

    public static GeoLocation getLocationByName(String location) {
        return new GeoLocation((MOSCOW[0][0] + MOSCOW[1][0]) / 2, (MOSCOW[0][1] + MOSCOW[1][1]) / 2);
    }

    public static double[][] getLocationBoxByName(String location) {
        return MOSCOW;
    }
}

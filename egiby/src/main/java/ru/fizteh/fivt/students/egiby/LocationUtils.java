package ru.fizteh.fivt.students.egiby;

import twitter4j.GeoLocation;
import twitter4j.JSONException;
import twitter4j.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static GeoLocation getLocationByName(String location) {
        JSONObject json = getGoogleAPIQuery(location);

        try {
            JSONObject results = json.getJSONArray("results").getJSONObject(0);
            JSONObject geometry = results.getJSONObject("geometry");
            JSONObject coordinates = geometry.getJSONObject("location");

            String lat = coordinates.getString("lat");
            String lng = coordinates.getString("lng");

            return new GeoLocation(Double.parseDouble(lat), Double.parseDouble(lng));
        } catch (JSONException e) {
            e.printStackTrace();
            System.err.println("Twittes will be found in Moscow");
            return new GeoLocation((MOSCOW[0][0] + MOSCOW[1][0]) / 2, (MOSCOW[0][1] + MOSCOW[1][1]) / 2);
        }
    }

    public static double[][] getLocationBoxByName(String location) {
        JSONObject json = getGoogleAPIQuery(location);

        JSONObject results = null;
        try {
            results = json.getJSONArray("results").getJSONObject(0);
            JSONObject geometry = results.getJSONObject("geometry");
            JSONObject northeast = geometry.getJSONObject("viewport").getJSONObject("northeast");
            JSONObject southwest = geometry.getJSONObject("viewport").getJSONObject("southwest");

            double[][] coordinateBox = {
                    {Double.parseDouble(southwest.getString("lat")), Double.parseDouble(southwest.getString("lng"))},
                    {Double.parseDouble(northeast.getString("lat")), Double.parseDouble(northeast.getString("lng"))}
            };

            return coordinateBox;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return MOSCOW;
    }
}

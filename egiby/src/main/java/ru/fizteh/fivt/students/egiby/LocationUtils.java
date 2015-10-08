package ru.fizteh.fivt.students.egiby;

import twitter4j.GeoLocation;
import twitter4j.JSONException;
import twitter4j.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by egiby on 04.10.15.
 */
public class LocationUtils {
    private static final double[][] MOSCOW_COORDINATE_BOX = {{55.48992699999999, 37.3193288}, {56.009657, 37.9456611}};
    private static final GeoLocation MOSCOW_COORDINATES = new GeoLocation(55.755826, 37.6173);
    private static final String URL = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=";

    private static String getAPIKey() {
        try (BufferedReader input = new BufferedReader(new FileReader("GoogleMapsAPI.properties"))) {
            String key = input.readLine();
            return key;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static JSONObject getGoogleAPIQuery(String location) {
        String key = getAPIKey();
        String json;

        try {
            json = HttpQuery.getQuery(URL + location + "&key=" + key);
            return new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class Location {
        private double[][] coordinateBox = null;
        private GeoLocation coordinates = null;

        Location(double[][] newCoordinateBox, GeoLocation newCoordinates) {
            coordinateBox = newCoordinateBox;
            coordinates = newCoordinates;
        }

        public double[][] getCoordinateBox() {
            return coordinateBox;
        }

        public GeoLocation getCoordinates() {
            return coordinates;
        }

        public static final int DEFAULT_RADIUS = 30;
        public long getRadius() {
            return DEFAULT_RADIUS;
        }
    }

    public static Location getLocationByName(String location) {
        JSONObject json = getGoogleAPIQuery(location);

        JSONObject results;
        try {
            results = json.getJSONArray("results").getJSONObject(0);
            JSONObject geometry = results.getJSONObject("geometry");

            JSONObject northeast = geometry.getJSONObject("viewport").getJSONObject("northeast");
            JSONObject southwest = geometry.getJSONObject("viewport").getJSONObject("southwest");

            double[][] coordinateBox = {
                    {Double.parseDouble(southwest.getString("lat")), Double.parseDouble(southwest.getString("lng"))},
                    {Double.parseDouble(northeast.getString("lat")), Double.parseDouble(northeast.getString("lng"))}
            };

            JSONObject coordinates = geometry.getJSONObject("location");

            String lat = coordinates.getString("lat");
            String lng = coordinates.getString("lng");

            return new Location(coordinateBox, new GeoLocation(Double.parseDouble(lat), Double.parseDouble(lng)));
        } catch (JSONException e) {
            e.printStackTrace();
            return new Location(MOSCOW_COORDINATE_BOX, MOSCOW_COORDINATES);
        }
    }
}

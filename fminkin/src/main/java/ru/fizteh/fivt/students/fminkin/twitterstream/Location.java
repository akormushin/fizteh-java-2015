package ru.fizteh.fivt.students.fminkin.twitterstream;

/**
 * Created by Федор on 24.09.2015.
 */
import org.json.JSONException;
import java.net.URL;
import java.net.URISyntaxException;
import java.io.IOException;
import java.util.Properties;
import java.io.*;


public class Location {
    private double latitude;
    private double longitude;
    private String googleMapsKey;
    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
    Location(double lat, double lng) {
        latitude = lat;
        longitude = lng;
    }
    Location() {
    }
    public void getLocationGoogle(String base) throws IOException {
        Properties properties = new Properties();
        try (InputStream input = this.getClass().getResourceAsStream("/geo.properties")) {
            properties.load(input);
        } catch (IOException e) {
            System.err.println("IOException has occured");
            e.printStackTrace();
        }

        googleMapsKey = properties.getProperty("google");
        if (googleMapsKey == null) {
            System.out.println("NoKeyFoundException has occured\n");
            throw new IOException();
        }
        base = base.replaceAll(" ", "+");
        base = base.replaceAll(", ", ",+");
        String name = "https://maps.googleapis.com/maps/api/geocode/json?address=" + base + "&key=" + googleMapsKey;
        URL u;
        try {
            u = new URL(name);
            u.toURI();
        } catch (URISyntaxException e) {
            System.err.println("URI Syntax Exception has occured");
            return;
        }

        try {
            org.json.JSONObject json = JsonReader.readJsonFromUrl(u.toString());
            json = json
                    .getJSONArray("results")
                    .getJSONObject(0)
                    .getJSONObject("geometry")
                    .getJSONObject("location");
            latitude = Double.parseDouble(json.getString("lat"));
            longitude = Double.parseDouble(json.getString("lng"));
        } catch (JSONException e) {
            System.err.println("JSONException has occured");
            return;
        }
    }
}

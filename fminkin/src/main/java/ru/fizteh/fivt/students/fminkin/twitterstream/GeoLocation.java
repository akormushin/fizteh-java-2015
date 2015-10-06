package ru.fizteh.fivt.students.fminkin.twitterstream;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

/**
 * Created by Федор on 29.09.2015.
 */
public class GeoLocation {
    private static String googleMapsKey;
    public static Location getLocationGoogle(String base) throws IOException, URISyntaxException, JSONException {
        Properties properties = new Properties();
        InputStream input = GeoLocation.class.getResourceAsStream("/geo.properties");
        properties.load(input);

        googleMapsKey = properties.getProperty("google");
        if (googleMapsKey == null) {
            System.out.println("NoKeyFoundException has occured\n");
            throw new IOException();
        }
        base = base.replaceAll(" ", "+");
        base = base.replaceAll(", ", ",+");
        String name = "https://maps.googleapis.com/maps/api/geocode/json?address=" + base + "&key=" + googleMapsKey;
        URL u;

        u = new URL(name);
        u.toURI();


        org.json.JSONObject json = JsonReader.readJsonFromUrl(u.toString());
        json = json
                .getJSONArray("results")
                .getJSONObject(0)
                .getJSONObject("geometry")
                .getJSONObject("location");
        return new Location(Double.parseDouble(json.getString("lat")), Double.parseDouble(json.getString("lng")));

    }
}

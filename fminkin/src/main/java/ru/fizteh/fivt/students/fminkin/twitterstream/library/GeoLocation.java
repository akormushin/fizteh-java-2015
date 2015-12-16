package ru.fizteh.fivt.students.fminkin.twitterstream.library;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import org.json.JSONException;
/**
 * Created by Федор on 29.09.2015.
 */
public class GeoLocation {
    private static String googleMapsKey;

    public Location getLocationGoogle(String base, JSonReader r) throws IOException, URISyntaxException, JSONException {
        Properties properties = new Properties();
        InputStream input = GeoLocation.class.getResourceAsStream("/geo.properties");
        properties.load(input);

        googleMapsKey = properties.getProperty("google");
        if (googleMapsKey == null) {
            throw new IOException();
        }
        base = base.replaceAll(" ", "+");
        base = base.replaceAll(", ", ",+");
        String name = "https://maps.googleapis.com/maps/api/geocode/json?address=" + base + "&key=" + googleMapsKey;

        try {
            org.json.JSONObject json = r.readJsonFromUrl(name);
            String status = json.getString("status");
            if (status.equals("ZERO_RESULTS") || status.equals("OVER_QUERY_LIMIT") || status.equals("REQUEST_DENIED")
                    || !status.equals("OK")) {
                throw new IOException();
            }
            json = json
                    .getJSONArray("results")
                    .getJSONObject(0)
                    .getJSONObject("geometry")
                    .getJSONObject("location");
            return new Location(Double.parseDouble(json.getString("lat")), Double.parseDouble(json.getString("lng")));
        } catch (JSONException e) {
            throw new IOException();
        }
    }

    public static Location getCurrentLocation(JSonReader r) throws IOException, URISyntaxException {
        URL myUrl = new URL("http://ipinfo.io/json");
        myUrl.toURI();
        try {
            org.json.JSONObject json = r.readJsonFromUrl(myUrl.toString());

            String[] coordinates = json.getString("loc").split(",");

            return new Location(
                    Double.parseDouble(coordinates[0]),
                    Double.parseDouble(coordinates[1]),
                    json.getString("city"));
        } catch (JSONException e) {
            throw new IOException();
        }
    }
}

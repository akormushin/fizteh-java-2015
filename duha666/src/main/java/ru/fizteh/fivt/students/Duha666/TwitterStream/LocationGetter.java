package ru.fizteh.fivt.students.duha666.TwitterStream;

import twitter4j.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;

public class LocationGetter {
    public static GeoLocation getLocationByPlace(String placeString) throws IOException,
            URISyntaxException, org.json.JSONException {
        Properties properties = new Properties();
        try (InputStream inputStream = GeoLocation.class.getResourceAsStream("/geo.properties")) {
            properties.load(inputStream);
        }
        String googleMapsKey = properties.getProperty("google");
        String urlString = "https://maps.googleapis.com/maps/api/geocode/json?key="
                + googleMapsKey + "&address=" + URLEncoder.encode(placeString, "UTF-8");
        URL url = new URL(urlString);
        org.json.JSONObject jsonObject = JsonReader.readJsonFromUrl(url.toString());
        jsonObject = jsonObject.
                getJSONArray("results").
                getJSONObject(0).
                getJSONObject("geometry").
                getJSONObject("location");
        return new GeoLocation(Double.parseDouble(jsonObject.getString("lat")),
                Double.parseDouble(jsonObject.getString("lng")));
    }
}

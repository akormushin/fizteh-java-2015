package ru.fizteh.fivt.students.sergmiller.twitterStream;


import com.beust.jcommander.internal.Maps;
import org.json.JSONException;
import org.json.JSONObject;
import ru.fizteh.fivt.students.sergmiller.twitterStream.exceptions.GettingMyLocationException;
import twitter4j.GeoLocation;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.util.Pair;


/**
 * Created by sergmiller on 29.09.15.
 */
//Used http://habrahabr.ru/post/148986/


final class GeoLocationResolver {
    public static final int MAX_QUANTITY_OF_TRIES = 2;
    static final double EARTH_RADIUS = 6371;

    public static Pair<GeoLocation, Double> getGeoLocation(final String geoRequest)
            throws IOException, JSONException {
        final String baseUrl = "http://maps.googleapis.com/maps/api/geocode/json";
        final Map<String, String> params = Maps.newHashMap();
        params.put("sensor", "false");
        params.put("address", geoRequest);
        final String url = baseUrl + '?' + encodeParams(params);
        final JSONObject response = GeoLocationResolver.read(url);
        double northEastBoundLongitude, northEastBoundLatitude;
        double southWestBoundLongitude, southWestBoundLatitude;
        double latitude, longitude;
        double approximatedRadius;
        JSONObject location = response.getJSONArray("results").getJSONObject(0);
        location = location.getJSONObject("geometry");
        JSONObject northEastBound = location.getJSONObject("bounds");
        JSONObject southWestBound = location.getJSONObject("bounds");
        location = location.getJSONObject("location");
        northEastBound = northEastBound.getJSONObject("northeast");
        southWestBound = southWestBound.getJSONObject("southwest");
        latitude = location.getDouble("lat");
        longitude = location.getDouble("lng");
        northEastBoundLatitude = northEastBound.getDouble("lat");
        northEastBoundLongitude = northEastBound.getDouble("lng");
        southWestBoundLatitude = southWestBound.getDouble("lat");
        southWestBoundLongitude = southWestBound.getDouble("lng");
        approximatedRadius = getSphereDist(
                northEastBoundLatitude, northEastBoundLongitude,
                southWestBoundLatitude, southWestBoundLongitude
        );
        approximatedRadius /= 2;
        return new Pair((new GeoLocation(latitude, longitude)), new Double(approximatedRadius));
    }

    public static String readAll(final Reader rd) throws IOException {
        final StringBuilder sb = new StringBuilder();
        try {
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
        } finally {
            return sb.toString();
        }
    }


    public static JSONObject read(final String url) throws IOException, JSONException {
        final InputStream is = new URL(url).openStream();
        try {
            final BufferedReader rd = new BufferedReader(
                    new InputStreamReader(is, Charset.forName("UTF-8")));
            final String jsonText = readAll(rd);
            final JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public static double getSphereDist(double latitude1, double longitude1,
                                       double latitude2, double longitude2) {
        latitude1 = Math.toRadians(latitude1);
        latitude2 = Math.toRadians(latitude2);
        longitude1 = Math.toRadians(longitude1);
        longitude2 = Math.toRadians(longitude2);
        return EARTH_RADIUS
                * Math.acos(Math.sin(latitude1)
                * Math.sin(latitude2)
                + Math.cos(latitude1)
                * Math.cos(latitude2)
                * Math.cos(longitude1 - longitude2));
    }

    private static String encodeParams(final Map<String, String> params) {
        String query = params.entrySet().stream().map(param -> {
            try {
                return URLEncoder.encode(param.getKey(), "UTF-8") + "=" + URLEncoder.encode(param.getValue(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.joining("&"));
        return query;
    }

    public static String getNameOfCurrentLocation()
            throws MalformedURLException, GettingMyLocationException {
        int numberOfTries = 0;

        do {
            URL currentIP = new URL("http://ipinfo.io/json");

            try (BufferedReader in = new BufferedReader(new InputStreamReader(
                    currentIP.openStream()))) {

                String currentInfo;
                StringBuilder responseBuilder = new StringBuilder();
                while ((currentInfo = in.readLine()) != null) {
                    responseBuilder.append(currentInfo);
                }

                JSONObject locationInfo =
                        new JSONObject(responseBuilder.toString());

                return locationInfo.getString("city");
            } catch (IOException | JSONException e) {
                ++numberOfTries;
            }
        }
        while (numberOfTries < MAX_QUANTITY_OF_TRIES);
        throw new GettingMyLocationException();
    }
}


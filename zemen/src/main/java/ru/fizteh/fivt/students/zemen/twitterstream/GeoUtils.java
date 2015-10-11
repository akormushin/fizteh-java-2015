/**
 * Created by zemen on 26.09.15.
 */

package ru.fizteh.fivt.students.zemen.twitterstream;

import com.bytebybyte.google.geocoding.service.*;
import com.bytebybyte.google.geocoding.service.request.*;
import com.bytebybyte.google.geocoding.service.response.*;
import com.bytebybyte.google.geocoding.service.standard.*;
import twitter4j.GeoLocation;
import twitter4j.JSONException;
import twitter4j.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class GeoUtils {

    static final double EARTH_RADIUS = 6371;

    //https://en.wikipedia.org/wiki/Great-circle_distance#Formulas
    public static double sphereDistance(double phi1, double lambda1,
                                        double phi2, double lambda2) {
        phi1 = Math.toRadians(phi1);
        lambda1 = Math.toRadians(lambda1);
        phi2 = Math.toRadians(phi2);
        lambda2 = Math.toRadians(lambda2);
        double deltaLambda = Math.abs(lambda2 - lambda1);

        return EARTH_RADIUS * Math.acos(Math.sin(phi1) * Math.sin(phi2)
                + Math.cos(phi1) * Math.cos(phi2) * Math.cos(deltaLambda));
    }

    private static Result queryLocation(String address)
            throws LocationNotFoundException {
        StandardGeocodingService geocoding = new StandardGeocodingService();
        GeocodeRequest request = new GeocodeRequestBuilder()
                .output("json").address(address).build();
        IResponse response = geocoding.geocode(request);
        if (response.getResults().length == 0) {
            throw new LocationNotFoundException("Invalid location " + address);
        }
        return response.getResults()[0];
    }

    public static GeoLocation getGeoLocationByAddress(String address)
            throws LocationNotFoundException {
        LatLng location = queryLocation(address).getGeometry().getLocation();
        return new GeoLocation(location.getLat(), location.getLng());
    }

    public static GeoLocation getGeoLocationByIP()
            throws IOException, JSONException, LocationNotFoundException {
        URL getMyFuckingLocation = new URL("https://wtfismyip.com/json");
        GeoLocation location;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(getMyFuckingLocation.openStream()))) {
            StringBuilder responseBuilder = new StringBuilder();
            String newLine;
            while ((newLine = in.readLine()) != null) {
                responseBuilder.append(newLine);
            }
            JSONObject myFuckingLocation = new JSONObject(responseBuilder.toString());
            String address = myFuckingLocation.getString("YourFuckingLocation");
            location = getGeoLocationByAddress(address);
        }
        return location;
    }
}

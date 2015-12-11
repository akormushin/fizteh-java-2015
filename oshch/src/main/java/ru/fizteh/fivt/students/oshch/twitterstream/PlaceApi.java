package ru.fizteh.fivt.students.oshch.twitterstream;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.Bounds;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PlaceApi {
    private static final double R = 6371;
    private GeocodingResult[] result;
    private double radius;

    private static String getKey() throws IOException {
        BufferedReader reader;
        reader = new BufferedReader(new FileReader("googleFindPlace.properties"));
        StringBuilder sbuild = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sbuild.append(line);
        }
        String key = sbuild.toString();
        return key;
    }

    PlaceApi(String place) throws IOException {
        GeoApiContext context = new GeoApiContext()
                .setApiKey(getKey());
        try {
            result = GeocodingApi.geocode(context, place).await();
            radius = calculateRadius();
        } catch (Exception e) {
            System.err.println("Place api error");
            System.exit(-1);
        }
    }

    private double calculateRadius() {
        double phi1 = Math.toRadians(result[0].geometry.bounds.northeast.lat);
        double phi2 = Math.toRadians(result[0].geometry.bounds.southwest.lat);
        double dPhi = phi1 - phi2;
        double lambda1;
        lambda1 = Math.toRadians(result[0].geometry.bounds.northeast.lng);
        double lambda2;
        lambda2 = Math.toRadians(result[0].geometry.bounds.southwest.lng);
        double dLambda = lambda1 - lambda2;

        double a = Math.sin(dPhi / 2) * Math.sin(dPhi / 2)
                + Math.cos(phi1) * Math.cos(phi2)
                * Math.sin(dLambda / 2) * Math.sin(dLambda / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;
        return distance / 2;
    }

    public final LatLng getLocation() {
        return result[0].geometry.location;
    }
    public final double getRadius() {
        return radius;
    }
    public final Bounds getBounds() {
        return result[0].geometry.bounds;
    }
}

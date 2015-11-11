package ru.fizteh.fivt.students.okalitova.moduletests.library;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.Bounds;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by okalitova on 24.09.15.
 */
public class PlaceParser {
    private static final double R = 6371;
    private GeocodingResult[] result;
    private double radius;

    private static String getKey() throws IOException {
        Properties prop = new Properties();
        try (FileInputStream in = new FileInputStream("/home/nimloth/coding/3sem/"
                + "fizteh-java-2015/okalitova/googleFindPlace.properties")) {
            prop.load(in);
            in.close();
        } catch (FileNotFoundException e) {
            throw e;
        }
        return prop.getProperty("key");
    }

    public PlaceParser(String place) throws Exception {
        GeoApiContext context = new GeoApiContext()
                .setApiKey(getKey());
        result = GeocodingApi.geocode(context, place).await();
        radius = calculateRadius();
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

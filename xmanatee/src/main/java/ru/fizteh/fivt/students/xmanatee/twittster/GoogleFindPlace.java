package ru.fizteh.fivt.students.xmanatee.twittster;


import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.Bounds;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class GoogleFindPlace {
    private static final double R = 6371;
    private GeocodingResult[] result;
    private double radius;
    GoogleFindPlace(String place) throws Exception {

        if (place.equals("nearby")) {
            place = new TrackMe().getPlace();
        }

        String apiKey = getKeyFromProperties();
        GeoApiContext context = new GeoApiContext()
                .setApiKey(apiKey);

        result = GeocodingApi.geocode(context, place).await();
        radius = calculateRadius();
    }

    private String getKeyFromProperties() throws IOException {
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream("twitter4j.properties")) {
            prop.load(input);
        } catch (FileNotFoundException e) {
            System.err.println("Problems finding .properties file : " + e.getMessage());
            throw e;
        } catch (IOException e) {
            System.err.println("Problems reading .properties file : " + e.getMessage());
            throw e;
        }
        return prop.getProperty("googleApiKey");
    }

    public LatLng getLocation() {
        return result[0].geometry.location;
    }

    public double getRadius() {
        return radius;
    }

    private double calculateRadius() {
        // Heavy formulas for distance from Wiki
        double phi1 = Math.toRadians(result[0].geometry.bounds.northeast.lat);
        double phi2 = Math.toRadians(result[0].geometry.bounds.southwest.lat);
        double dPhi = phi1 - phi2;
        double lambda1 = Math.toRadians(result[0].geometry.bounds.northeast.lng);
        double lambda2 = Math.toRadians(result[0].geometry.bounds.southwest.lng);
        double dLambda = lambda1 - lambda2;

        double a = Math.sin(dPhi / 2) * Math.sin(dPhi / 2)
                + Math.cos(phi1) * Math.cos(phi2)
                * Math.sin(dLambda / 2) * Math.sin(dLambda / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;
        return distance / 2;
    }

    private static final double EPSILON = 0.001;
    public boolean isInside(double lat, double lng) {
        boolean check = (result[0].geometry.bounds.southwest.lat <= lat + EPSILON);
        check &= (result[0].geometry.bounds.northeast.lat >= lat - EPSILON);
        check &= (result[0].geometry.bounds.southwest.lng <= lng + EPSILON);
        check &= (result[0].geometry.bounds.northeast.lng >= lng - EPSILON);
        return check;
    }

    public double[][] getBounds() {
        Bounds bounds = result[0].geometry.bounds;
        double[][] boundsArr = {{bounds.southwest.lng, bounds.southwest.lat},
                {bounds.northeast.lng, bounds.northeast.lat}};
        return boundsArr;
    }
}

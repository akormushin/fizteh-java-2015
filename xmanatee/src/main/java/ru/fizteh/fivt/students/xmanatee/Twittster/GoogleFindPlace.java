package ru.fizteh.fivt.students.xmanatee.Twittster;


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
    GoogleFindPlace(String place) {

        if (place.equals("nearby")) {
            place = new TrackMe().getPlace();
        }

        GeoApiContext context = new GeoApiContext()
                .setApiKey(getKeyFromProperties());
        try {
            result = GeocodingApi.geocode(context, place).await();
        } catch (Exception e) {
            System.out.println("Problems with finding specified place: " + e.getMessage());
        }
        radius = calculateRadius();
    }

    private String getKeyFromProperties() {
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream("mykeys.properties")) {
            prop.load(input);
        } catch (FileNotFoundException e) {
            System.out.println("Problems finding .properties file : " + e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Problems reading .properties file : " + e.getMessage());
            System.exit(1);
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

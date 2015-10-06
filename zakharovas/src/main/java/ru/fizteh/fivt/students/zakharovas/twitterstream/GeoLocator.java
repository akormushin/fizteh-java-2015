package ru.fizteh.fivt.students.zakharovas.twitterstream;


import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import twitter4j.GeoLocation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;

public class GeoLocator {

    private static final double EARTH_RADIUS = 6371;
    private static final double DEFAULT_RADIUS = 100;
    private String location;
    private double radius;
    private double[][] borders;
    private double[] locationCenter;
    private GeoApiContext context;

    public GeoLocator(List<String> locationInList) throws IOException, GeoSearchException {
        location = String.join(" ", locationInList);
        if (location.isEmpty()) {
            location = "nearby";
        }
        enableGoogleMaps();
        setCoordinates();
    }

    public double[][] getLocationForStream() {
        return borders;
    }

    public GeoLocation getLocationForSearch() {
        return new GeoLocation(locationCenter[0], locationCenter[1]);
    }

    public double getRadius() {
        return radius;
    }

    private void enableGoogleMaps() throws IOException {
        Properties properties = new Properties();
        if (this.getClass().getResourceAsStream("/googlemaps.properties") == null) {
            throw new NoSuchElementException("No flie with googlemaps properties");
        }
        try (InputStream inputStream = this.getClass().getResourceAsStream("/googlemaps.properties")) {
            properties.load(inputStream);
        }
        String googleKey = properties.getProperty("googleMapsKey");
        context = new GeoApiContext().setApiKey(googleKey);
    }

    private void setCoordinates() throws GeoSearchException, MalformedURLException {
        if (location.equals("nearby")) {
            location = findAddressByIP();
            if (location.isEmpty()) {
                setCoordinatesByIP();
                return;
            }
        }
        GeocodingResult[] results;
        try {
            results = GeocodingApi.geocode(context, location).await();
        } catch (Exception e) {
            throw new GeoSearchException(e.getMessage());
        }
        if (results.length == 0 || results[0] == null) {
            throw new GeoSearchException("Location has not been found");
        }
        locationCenter = new double[]{results[0].geometry.location.lat, results[0].geometry.location.lng};
        if (results[0].geometry.bounds == null) {
            calcBordersByCenter();
            return;
        }
        borders = new double[][]{{results[0].geometry.bounds.southwest.lat, results[0].geometry.bounds.southwest.lng},
                {results[0].geometry.bounds.northeast.lat, results[0].geometry.bounds.northeast.lng}};
        radius = calcRadius(results[0]);
    }

    private double calcRadius(GeocodingResult result) {
        //Frightening Formula from Internet
        double lat1 = Math.toRadians(result.geometry.bounds.northeast.lat);
        double lat2 = Math.toRadians(result.geometry.bounds.southwest.lat);
        double lng1 = Math.toRadians(result.geometry.bounds.northeast.lng);
        double lng2 = Math.toRadians(result.geometry.bounds.southwest.lng);
        double dLng = lng2 - lng1;
        double angleDistance = Math.atan(Math.sqrt(Math.pow(Math.cos(lat2) * Math.sin(dLng), 2.0)
                + Math.pow(Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLng), 2.0))
                / (Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(dLng)));
        return EARTH_RADIUS * angleDistance;

    }

    private void setCoordinatesByIP() throws MalformedURLException {
        URL url = new URL("http://ipinfo.io/loc");
        while (true) {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                String coordinates = bufferedReader.readLine();
                String[] separatedCoordinates = coordinates.split(",");
                locationCenter = new double[separatedCoordinates.length];
                for (int i = 0; i < separatedCoordinates.length; ++i) {
                    locationCenter[i] = Double.parseDouble(separatedCoordinates[i]);
                }
                calcBordersByCenter();
            } catch (IOException e) {
                System.err.println("Connection error. Reconnecting");
                try {
                    Thread.sleep(Numbers.SECOND);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private void calcBordersByCenter() {
        radius = DEFAULT_RADIUS;
        calcBorders(locationCenter);
    }

    private void calcBorders(double[] numberCoordinates) {
        //Borders are vertices of square, inscribed into circle with radius from this class
        borders = new double[Numbers.TWO][Numbers.TWO];
        double angle = Math.asin(radius / Math.sqrt(2.0) / EARTH_RADIUS);

        for (int i = 0; i < numberCoordinates.length; ++i) {
            borders[0][i] = numberCoordinates[i] - angle;
            borders[1][i] = numberCoordinates[i] + angle;
        }
    }

    private String findAddressByIP() throws MalformedURLException {
        URL url = new URL("http://ipinfo.io/city");
        while (true) {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                return bufferedReader.readLine();
            } catch (IOException e) {
                System.err.println("Connection error. Reconnecting");
                try {
                    Thread.sleep(Numbers.SECOND);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}

class GeoSearchException extends Exception {
    GeoSearchException(String errorMessage) {
        super(errorMessage);
    }


}


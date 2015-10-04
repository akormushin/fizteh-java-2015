package ru.fizteh.fivt.students.zakharovas.twitterstream;


import com.google.maps.GeoApiContext;
import twitter4j.GeoLocation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

public class GeoLocator {

    private static final double EARTH_RADIUS = 6371;
    private static final double DEFAULT_RADIUS = 100;
    private String location;
    private double radius = 0;
    private double[][] borders;
    private GeoApiContext context;

    public GeoLocator(List<String> locationInList) throws IOException {
        location = String.join(" ", locationInList);
        if (location.isEmpty()) {
            location = "nearby";
        }
        enableGoogleMaps();
        setCoordinates();
    }

    public double[] getLocationForStream() {
        return null;
    }

    public GeoLocation getLocationForSearch() {
        return /*new GeoLocation();*/ null;

    }

    public double getRadius() {
        return radius;
    }

    private void enableGoogleMaps() throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = this.getClass().getResourceAsStream("/googlemaps.properties");) {
            properties.load(inputStream);
        } catch (IOException ex) {
            throw ex;
        }
        String googleKey = properties.getProperty("googleMapsKey");
        context = new GeoApiContext().setApiKey(googleKey);
    }

    private void setCoordinates() throws IOException {
        if (location.equals("nearby")) {
            location = findAddressByIP();
            if (location.isEmpty()) {
                setCoordinatesByIP();
                return;
            }
        }
    }

    private void setCoordinatesByIP() throws MalformedURLException {
        URL url = new URL("http://ipinfo.io/loc");
        while (true) {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                String coordinates = bufferedReader.readLine();
                String[] separatedCoordinates = coordinates.split(",");
                double[] numberCoordinates = new double[separatedCoordinates.length];
                for (int i = 0; i < separatedCoordinates.length; ++i) {
                    numberCoordinates[i] = Double.parseDouble(separatedCoordinates[i]);
                }
                radius = DEFAULT_RADIUS;
                calcBorders(numberCoordinates);
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

    private void calcBorders(double[] numberCoordinates) {
        //Borders are vertices of square, inscribed into circle with radius from this class
        borders = new double[Numbers.TWO][Numbers.TWO];
        double angle = Math.asin(radius / Math.sqrt(2.0) / EARTH_RADIUS);

        for (int i = 0; i < numberCoordinates.length; ++i) {
            borders[0][i] = numberCoordinates[i] + angle;
            borders[1][i] = numberCoordinates[i] - angle;
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


